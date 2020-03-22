package org.example.authorize;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class Application {

    private final Environment env;

    public Application(Environment env) {
        this.env = env;
    }

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        com.mmedic.utils.DefaultProfileUtil.addDefaultProfile(app);
        Environment env = app.run(args).getEnvironment();
    }
}
