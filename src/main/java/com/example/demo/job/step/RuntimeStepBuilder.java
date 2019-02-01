package com.example.demo.job.step;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.SingletonBeanRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Runtime bean builder
 * 
 * @author razvan.parautiu
 *
 */
@Component
public class RuntimeStepBuilder {

	@Autowired
	private ApplicationContext applicationContext;

	/**
	 * If the bean is already registered into the {@link SingletonBeanRegistry} then
	 * we will return the instance of bean, otherwise we will create, register and
	 * return a new bean
	 * 
	 * @param beanName    - name of the bean step to register
	 * @param inputClass  - input type
	 * @param outputClass - output type
	 * @return Generic step depending on the type of input and output
	 */
	public <I, O> Step<I, O> load(String beanName, Class<I> inputClass, Class<O> outputClass) {
		return (Step<I, O>) registerNewStepBean(beanName, inputClass, outputClass);
	}

	@SuppressWarnings("unchecked")
	private <I, O> Step<I, O> registerNewStepBean(String beanName, Class<I> inputClass, Class<O> outputClass) {
		ConfigurableApplicationContext configContext = (ConfigurableApplicationContext) applicationContext;
		SingletonBeanRegistry beanRegistry = configContext.getBeanFactory();

		if (beanRegistry.containsSingleton(beanName)) {
			return (Step<I, O>) beanRegistry.getSingleton(beanName);
		} else {
			Step<I, O> step = Step.getNewInstance(beanName);
			step.setReader(Reader.<I>getNewInstance());
			step.setProcessor(Processor.<I, O>getNewInstance());
			step.setWriter(Writer.<O>getNewInstance());

			beanRegistry.registerSingleton(beanName, step);

			return (Step<I, O>) beanRegistry.getSingleton(beanName);
		}

	}

}
