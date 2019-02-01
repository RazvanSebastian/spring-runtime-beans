package com.example.demo.job;

import java.util.ArrayList;
import java.util.List;

import com.example.demo.job.step.Step;

public class Job<I, O> {

	private String name;
	private List<Step<I, O>> steps = new ArrayList<>();

	public static <I, O> Job<I, O> getNewInstance(String name) {
		return new Job<I, O>(name);
	}

	private Job(String name) {
		super();
		this.name = name;
	}

	public void registerStep(Step<I, O> newStep) {
		for (Step<I, O> step : steps) {
			if (step.getName().equals(newStep.getName()))
				return;
		}
		steps.add(newStep);
	}

	public String getName() {
		return name;
	}

	public List<Step<I, O>> getSteps() {
		return steps;
	}

}
