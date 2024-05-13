package com.lgcns.test.server;

import java.io.IOException;
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

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
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