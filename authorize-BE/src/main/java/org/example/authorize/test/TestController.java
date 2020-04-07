package org.example.authorize.test;

import org.example.authorize.generator.Generator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private final Generator<String> generator;

    public TestController(Generator<String> generator) {
        this.generator = generator;
    }

    @GetMapping("/test")
    public String test() {
        return generator.generate();
    }
}
