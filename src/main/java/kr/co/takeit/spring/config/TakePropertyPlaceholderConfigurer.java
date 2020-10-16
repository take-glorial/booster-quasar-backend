/**
 * Nexacro Request/Response 정보를 가지고 있는 객체
 * @filename kr.co.takeit.spring.config.TakePropertyPlaceholderConfigurer.java
 * @author Take
 * @since 2019.01.01
 * @version 1.0
 * @see
 * 
 * << 변경 이력(Modification Information) >>
 * 
 * 변경번호 : #1
 * 변경일자 : 2019.01.01
 * 변경사람 : Take
 * 변경내용 : 신규 생성
 * 
 */
package kr.co.takeit.spring.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class TakePropertyPlaceholderConfigurer implements BeanFactoryPostProcessor {
	/**
	 * 
	 */
	private PropertyPlaceholderConfigurer configurer;

	/**
	 * 생성자 함수
	 * 
	 * @param configurer
	 */
	public TakePropertyPlaceholderConfigurer(PropertyPlaceholderConfigurer configurer)
	{
		this.configurer = configurer;
	}

	/**
	 * 
	 * 
	 */
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException
	{
		this.configurer.postProcessBeanFactory(beanFactory);
	}
}
