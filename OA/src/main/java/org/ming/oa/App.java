package org.ming.oa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("org.ming.oa")
public class App 
{

    public static void main( String[] args )
    {
        SpringApplication.run(App.class, args);
    }
}
