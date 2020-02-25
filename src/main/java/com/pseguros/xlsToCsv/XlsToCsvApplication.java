package com.pseguros.xlsToCsv;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class XlsToCsvApplication {

	  private static Logger logger = LogManager.getLogger(XlsToCsvApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(XlsToCsvApplication.class, args);
		logger.debug("InicioApp XlsToCsvApplication");
	}

}
