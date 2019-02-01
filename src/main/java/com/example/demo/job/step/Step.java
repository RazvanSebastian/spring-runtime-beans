package com.example.demo.job.step;

public class Step<I, O> {

	private String name;
	private Reader<I> reader;
	private Processor<I, O> processor;
	private Writer<O> writer;

	public static <I, O> Step<I, O> getNewInstance(String name) {
		return new Step<I, O>(name);
	}

	private Step(String name) {
		this.name = name;
	}

	public void setReader(Reader<I> reader) {
		this.reader = reader;
	}

	public void setProcessor(Processor<I, O> processor) {
		this.processor = processor;
	}

	public void setWriter(Writer<O> writer) {
		this.writer = writer;
	}

	public Reader<I> getReader() {
		return reader;
	}

	public Processor<I, O> getProcessor() {
		return processor;
	}

	public Writer<O> getWriter() {
		return writer;
	}

	public String getName() {
		return name;
	}

}
