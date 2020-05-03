package org.example.authorize;

import org.example.authorize.config.prop.ApplicationProperties;
import org.example.authorize.config.prop.JwtProperties;
import org.example.authorize.config.prop.OTPProperties;
import org.example.authorize.config.prop.VelocityProperties;
import org.example.authorize.utils.DefaultProfileUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;

@SpringBootApplication
@EnableConfigurationProperties({
        ApplicationProperties.class,
        JwtProperties.class,
        OTPProperties.class,
        VelocityProperties.class
})
public class Application {

    private final Environment env;

    public Application(Environment env) {
        this.env = env;
    }

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        DefaultProfileUtil.addDefaultProfile(app);
        Environment env = app.run(args).getEnvironment();
    }
}
