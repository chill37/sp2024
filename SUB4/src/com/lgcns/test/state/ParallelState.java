package com.lgcns.test.state;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import com.lgcns.test.State;
import com.lgcns.test.Workflow;

public class ParallelState extends State {

	private List<Workflow> workflows;

	public ParallelState(String name, String next, List<Workflow> workflows) {
		super(name, next);
		this.workflows = workflows;
	}

	@Override
	public String run() throws Exception {
		CountDownLatch latch = new CountDownLatch(workflows.size());
		for (Workflow w : workflows) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						w.run();
						latch.countDown();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
		latch.await();

		return getNext();
	}

}
