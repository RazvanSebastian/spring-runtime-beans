package com.example.demo.job.step;

public class Writer<O> {

	public static <O> Writer<O> getNewInstance() {
		return new Writer<O>();
	}

	public String write(O output) {
		return output.toString();
	}
}
