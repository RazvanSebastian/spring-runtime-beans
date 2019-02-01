package com.example.demo.job.step;

public class Reader<I> {

	public static <I> Reader<I> getNewInstance() {
		return new Reader<I>();
	}

	public I read(String query) {
		return null;
	}

}
