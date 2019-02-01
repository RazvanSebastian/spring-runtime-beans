package com.example.demo.job;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.SingletonBeanRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import com.example.demo.job.step.Processor;
import com.example.demo.job.step.Reader;
import com.example.demo.job.step.RuntimeStepBuilder;
import com.example.demo.job.step.Step;
import com.example.demo.job.step.Writer;

@Component
public class RuntimeJobBuilder {

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private RuntimeStepBuilder runtimeStepBuilder;

	/**
	 * If the bean is already registered into the {@link SingletonBeanRegistry} then
	 * we will return the instance of bean, otherwise we will create, register and
	 * return a new bean
	 * 
	 * If there are some {@link Step} object in the step list provided, we the
	 * {@link RuntimeStepBuilder} will register automatically the step
	 * 
	 * Also based on the input and output types provided, the {@link Reader},
	 * {@link Processor} and {@link Writer} will be generated accordingly.
	 * 
	 * @param beanName    - name of the bean job to register
	 * @param inputClass  - input type
	 * @param outputClass - output type
	 * @param steps       - a list of steps.
	 * @return
	 */
	public <I, O> Job<I, O> load(String beanName, Class<I> inputClass, Class<O> outputClass, List<Step<I, O>> steps) {
		return registerNewJob(beanName, inputClass, outputClass, steps);
	}

	/**
	 * Load from {@link SingletonBeanRegistry} the bean with the name. Exception at
	 * runtime may be triggered if the bean was not registered!
	 * 
	 * @param name
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <I, O> Job<I, O> load(String beanName) {
		ConfigurableApplicationContext configContext = (ConfigurableApplicationContext) applicationContext;
		SingletonBeanRegistry beanRegistry = configContext.getBeanFactory();

		if (!beanRegistry.containsSingleton(beanName))
			throw new RuntimeException("Bean " + beanName + "not found!");
		return (Job<I, O>) beanRegistry.getSingleton(beanName);
	}

	@SuppressWarnings("unchecked")
	private <I, O> Job<I, O> registerNewJob(String beanName, Class<I> inputClass, Class<O> outputClass,
			List<Step<I, O>> steps) {
		ConfigurableApplicationContext configContext = (ConfigurableApplicationContext) applicationContext;
		SingletonBeanRegistry beanRegistry = configContext.getBeanFactory();

		if (beanRegistry.containsSingleton(beanName)) {
			return (Job<I, O>) beanRegistry.getSingleton(beanName);
		} else {
			Job<I, O> job = Job.getNewInstance(beanName);

			for (Step<I, O> step : steps) {
				job.registerStep(runtimeStepBuilder.load(step.getName(), inputClass, outputClass));
			}

			beanRegistry.registerSingleton(beanName, job);

			return (Job<I, O>) beanRegistry.getSingleton(beanName);
		}
	}

}
