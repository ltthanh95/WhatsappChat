package com.tl.whatapps;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.tl.whatapps")

public class WhatappsApplication {

	public static void main(String[] args) {
		SpringApplication.run(WhatappsApplication.class, args);
	}

}
