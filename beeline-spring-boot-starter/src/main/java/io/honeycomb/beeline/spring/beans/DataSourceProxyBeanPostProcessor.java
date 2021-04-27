package io.honeycomb.beeline.spring.beans;

import net.ttddyy.dsproxy.support.ProxyDataSource;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;

import javax.sql.DataSource;

public class DataSourceProxyBeanPostProcessor implements BeanFactoryAware, BeanPostProcessor {

    private BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public Object postProcessAfterInitialization(final Object bean, final String beanName) {
        if (!DataSource.class.isAssignableFrom(bean.getClass()) || ProxyDataSource.class.isAssignableFrom(bean.getClass())) {
            return bean;
        }

        if (beanFactory != null) {
            return ProxyDataSourceBuilder.create((DataSource) bean)
                .name(beanName)
                .listener(beanFactory.getBean(BeelineQueryListenerForJDBC.class))
                .build();
        }

        return bean;
    }

}
