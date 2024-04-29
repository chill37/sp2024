package com.lgcns.test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.lgcns.test.WorkflowManager.WorkflowsDto.WorkflowDto;
import com.lgcns.test.WorkflowManager.WorkflowsDto.WorkflowDto.StateDto;
import com.lgcns.test.state.ActionState;
import com.lgcns.test.state.ParallelState;

public class WorkflowManager {

	private static Map<String, Workflow> workflows = new ConcurrentHashMap<>();

	class WorkflowsDto {
		public Map<String, WorkflowDto> workflow;

		class WorkflowDto {
			public String startFrom;
			public Map<String, StateDto> state;
			public List<String> responses;

			class StateDto {
				public String type;
				public String next;
				public String url;
				public List<String> parameters;
				public List<WorkflowDto> branches;
			}
		}
	}

	public static Workflow get(String name) {
		return workflows.get(name);
	}

	public static void load() throws JsonSyntaxException, IOException {
//		Type type = new TypeToken<Map<String, WorkflowDto>>(){}.getType();
		WorkflowsDto workflowsDto = new Gson().fromJson(new String(Files.readAllBytes(Paths.get("Workflow.JSON"))),
				WorkflowsDto.class);

		for (Entry<String, WorkflowDto> e : workflowsDto.workflow.entrySet()) {
			workflows.put(e.getKey(), makeWorkflow(e.getValue()));
		}
	}

	private static Workflow makeWorkflow(WorkflowDto workflowDto) {
		Set<State> states = new HashSet<>();

		for (Entry<String, StateDto> nameStateDto : workflowDto.state.entrySet()) {
			states.add(makeState(nameStateDto.getKey(), nameStateDto.getValue()));
		}

		return new Workflow(workflowDto.startFrom, states, workflowDto.responses);
	}

	private static State makeState(String stateName, StateDto stateDto) {
		if ("action".equals(stateDto.type)) {
			return new ActionState(stateName, stateDto.next, stateDto.url, stateDto.parameters);

		} else if ("parallel".equals(stateDto.type)) {
			List<Workflow> branches = new ArrayList<>();
			for (WorkflowDto branchWorkflowDto : stateDto.branches) {
				branches.add(makeWorkflow(branchWorkflowDto));
			}
			return new ParallelState(stateName, stateDto.next, branches);

		}
		return null;
	}
}
