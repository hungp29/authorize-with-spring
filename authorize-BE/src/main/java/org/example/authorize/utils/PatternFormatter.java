package org.example.authorize.utils;

import lombok.RequiredArgsConstructor;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.example.authorize.config.prop.VelocityProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;

/**
 * Pattern Formatter.
 */
@Component
@RequiredArgsConstructor
public class PatternFormatter implements InitializingBean {

    private final VelocityProperties velocityProps;
    private VelocityEngine velocityEngine;

    /**
     * Init Velocity Engine.
     */
    @Override
    public void afterPropertiesSet() {
        velocityEngine = new VelocityEngine();
        Properties p = new Properties();
        p.setProperty("resource.loader", velocityProps.getResourceLoader());
        p.setProperty("class.resource.loader.class", velocityProps.getClassLoader());
        velocityEngine.init(p);
    }

    /**
     * Merge data and template.
     *
     * @param templatePath template path
     * @param data         data to merge
     * @return value of template after merging
     */
    public String mergeDataAndTemplate(String templatePath, Map<String, String> data) {
        // Get template
        Template template = velocityEngine.getTemplate(velocityProps.getTemplatePath() + templatePath + velocityProps.getSuffix());
        // Create Velocity Context with data
        VelocityContext velocityContext = new VelocityContext(data);
        // Merge data to template
        StringWriter writer = new StringWriter();
        template.merge(velocityContext, writer);
        return writer.toString();
    }
}
