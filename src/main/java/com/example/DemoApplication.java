package com.example;

import com.example.bean.MathCalculator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;

import javax.sound.midi.Soundbank;

@SpringBootApplication
@EnableCaching
public class DemoApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(DemoApplication.class, args);
		MathCalculator mathCalculator = (MathCalculator) context.getBean("mathCalculator");
		mathCalculator.div(3, 1);
		mathCalculator.div(3, 1);
		mathCalculator.div(4, 1);
		mathCalculator.div(5, 1);
		mathCalculator.div(5, 1);
		System.out.println("debug");
	}

}
