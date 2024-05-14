package com.lgcns.test.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;

import com.lgcns.test.StateManager;

public class EngineServlet extends HttpServlet {

	private static final long serialVersionUID = 8572241974921679005L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String requestURI = req.getRequestURI();

		if (requestURI.startsWith("/fromServer")) {
			handleRequest1(req, res);
		} else if (requestURI.startsWith("/fromDevice")) {
			handleRequest2(req, res);
		}
	}

	private void handleRequest1(HttpServletRequest req, HttpServletResponse res) {
		try {
			String requestBody = getJsonBodyFromRequest(req);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void handleRequest2(HttpServletRequest req, HttpServletResponse res) {
		try {
			String requestBody = getJsonBodyFromRequest(req);

			HttpClient httpClient = new HttpClient();
			try {
				httpClient.start();
				Request request = httpClient.newRequest("http://127.0.0.1:7010/fromEdge").method(HttpMethod.POST);
				request.header(HttpHeader.CONTENT_TYPE, "application/json");
				request.content(new StringContentProvider(body, "utf-8"));
				ContentResponse contentResponse = request.send();
				httpClient.stop();

			} catch (Exception e) {
				e.printStackTrace();
			}

			res.setStatus(200);
			res.getWriter().flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// POST req의 body
	private String getJsonBodyFromRequest(HttpServletRequest req) throws Exception {
		String strBody = "";
		try (InputStreamReader isr = new InputStreamReader(req.getInputStream());
				BufferedReader br = new BufferedReader(isr)) {
			String buffer;
			StringBuilder sb = new StringBuilder();
			while ((buffer = br.readLine()) != null) {
				sb.append(buffer + "\n");
			}
			strBody = sb.toString();

		} catch (Exception e) {
			throw e;
		}
		return strBody;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		String requestURL = request.getRequestURL().toString();
		String path = requestURL.substring(requestURL.lastIndexOf("/") + 1);
		System.out.println(path);

		try {
			StateManager.get(path).run();
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	protected void doPost2(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String requestURL = request.getRequestURL().toString();
		String path = requestURL.substring(requestURL.lastIndexOf("/") + 1);
		System.out.println(path);

		try {

			HttpClient httpClient = new HttpClient();
			httpClient.start();
			// Construct your request body
			String requestBody = "{\"key\":\"value\"}"; // Modify this as needed
			Request jettyRequest = httpClient.POST("http://your.target.url/");
			jettyRequest.header("Content-Type", "application/json");
			jettyRequest.content(new StringContentProvider(requestBody));
			ContentResponse jettyResponse = jettyRequest.send();

			if (jettyResponse.getStatus() != HttpServletResponse.SC_OK) {
				throw new RuntimeException("Failed : HTTP error code : " + jettyResponse.getStatus());
			}

			System.out.println("Output from Server .... \n");
			System.out.println(jettyResponse.getContentAsString());
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().close();
			httpClient.stop();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	protected void doGetWithParamQuery(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Getting request parameters
		String parameter = request.getParameter("parameter");
		// Getting request query
		String query = request.getQueryString();

		try {
			HttpClient httpClient = new HttpClient();
			httpClient.start();
			// Construct URL with parameter and query
			String urlString = "http://your.target.url/?parameter=" + parameter;

			if (query != null) {
				urlString += "&" + query;
			}

			URI uri = URI.create(urlString);
			Request jettyRequest = httpClient.newRequest(uri);
			jettyRequest.method("GET");
			ContentResponse jettyResponse = jettyRequest.send();
			if (jettyResponse.getStatus() != HttpServletResponse.SC_OK) {
				throw new RuntimeException("Failed : HTTP error code : " + jettyResponse.getStatus());
			}

			System.out.println("Output from Server .... \n");
			System.out.println(jettyResponse.getContentAsString());
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().close();
			httpClient.stop();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}