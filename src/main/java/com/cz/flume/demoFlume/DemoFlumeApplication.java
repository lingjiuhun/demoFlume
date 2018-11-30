package com.cz.flume.demoFlume;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class DemoFlumeApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoFlumeApplication.class, args);
	}
}
