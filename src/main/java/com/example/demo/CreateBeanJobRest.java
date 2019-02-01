package com.example.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.SingletonBeanRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.job.Job;
import com.example.demo.job.RuntimeJobBuilder;
import com.example.demo.job.model.Input1;
import com.example.demo.job.model.Input2;
import com.example.demo.job.model.Output1;
import com.example.demo.job.model.Output2;
import com.example.demo.job.step.Step;

@RestController
public class CreateBeanJobRest {

	@Autowired
	private RuntimeJobBuilder runtimeJobBuilder;

	@Autowired
	private ApplicationContext applicationContext;

	@GetMapping("/hello")
	public String hello() {
		return "hello";
	}

	@GetMapping("/job1")
	public String getJob1() {
		List<Step<Input1, Output1>> steps = new ArrayList<Step<Input1, Output1>>();
		steps.add(Step.<Input1, Output1>getNewInstance("step11"));
		steps.add(Step.<Input1, Output1>getNewInstance("step12"));

		return runtimeJobBuilder.load("job1", Input1.class, Output1.class, steps).toString();
	}

	@GetMapping("/job2")
	public String getJob2() {
		List<Step<Input2, Output2>> steps = new ArrayList<Step<Input2, Output2>>();
		steps.add(Step.<Input2, Output2>getNewInstance("step21"));
		steps.add(Step.<Input2, Output2>getNewInstance("step22"));

		return runtimeJobBuilder.load("job2", Input2.class, Output2.class, steps).toString();
	}

	@GetMapping("/all")
	public String getAll() {
		ConfigurableApplicationContext configContext = (ConfigurableApplicationContext) applicationContext;
		SingletonBeanRegistry beanRegistry = configContext.getBeanFactory();
		return "No of beans = " + beanRegistry.getSingletonCount();
	}

	@GetMapping("/{jobId}")
	public String executeJob(@PathVariable("jobId") int jobId) {
		String stepname = jobId == 1 ? "job1" : "job2";
		StringBuilder builder = new StringBuilder();

		if (jobId == 1) {
			Job<Input1, Output1> job = runtimeJobBuilder.load(stepname);
			job.getSteps().forEach(step -> {
				step.getReader().read("query");
				step.getProcessor().process(new Input1(UUID.randomUUID().toString()));
				builder.append(step.getWriter().write(new Output1(UUID.randomUUID().toString()))).append('\n');
			});

		} else {
			Job<Input2, Output2> job = runtimeJobBuilder.load(stepname);
			job.getSteps().forEach(step -> {
				step.getReader().read("query");
				step.getProcessor().process(new Input2(UUID.randomUUID().toString()));
				builder.append(step.getWriter().write(new Output2(UUID.randomUUID().toString()))).append('\n');
			});
		}

		return builder.toString();

	}
}
