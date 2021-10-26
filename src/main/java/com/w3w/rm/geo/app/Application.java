package com.w3w.rm.geo.app;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The main What3WordsV3 which makes use of the SpringApplication to run the rest server in the mentioned jetty/tomcat embedded servers.
 * @author Rajagopal
 */
@SpringBootApplication
public class Application {
    /**
     * Method main() is the entry point for the application created to work with What3Words API.
     *
     * @param args with which the application is invoked.
     */
    public static void main(String... args) {
        SpringApplication.run(Application.class, args);
    }

}