package org.example.authorize.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
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
    public static <T extends Object> T getBean(Class<T> beanClass) {
        return context.getBean(beanClass);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // Store ApplicationContext reference to access required beans later on
        SpringContext.context = applicationContext;
    }
}
