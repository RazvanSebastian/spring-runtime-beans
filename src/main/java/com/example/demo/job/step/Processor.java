package com.example.demo.job.step;

public class Processor<I, O> {

	public static <I, O> Processor<I, O> getNewInstance() {
		return new Processor<I, O>();
	}

	public O process(I input) {
		return null;
	}
}
