package org.example.authorize.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;

/**
 * Spring context, it use to get bean.
 */
@Component
public class SpringContext implements ApplicationContextAware {

    private static ApplicationContext context;

    /**
     * Returns the Spring managed bean instance of the given class type if it exists.
     * Returns null otherwise.
     *
     * @param beanClass bean class
     * @return return bean of class
     */
    public static <T> T getBean(Class<T> beanClass) {
        return context.getBean(beanClass);
    }

    /**
     * Returns the Spring managed bean instance of the given class type if it exists.
     * Returns null otherwise.
     *
     * @param beanClass    bean class
     * @param genericClass generic class
     * @return return bean of class
     */
    public static <T, G> T getBean(Class<T> beanClass, Class<G> genericClass) {
        ObjectProvider<T> beanProvider = context.getBeanProvider(ResolvableType.forClassWithGenerics(beanClass, genericClass));
        return beanProvider.getObject();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // Store ApplicationContext reference to access required beans later on
        SpringContext.context = applicationContext;
    }
}
