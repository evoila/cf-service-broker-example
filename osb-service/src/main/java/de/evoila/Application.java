/**
 * 
 */
package de.evoila;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Johannes Hiemer.
 * Deploying to Space for testing purposes is as follows:
 * `cf create-service-broker name username password url --space-scoped`
 */
@SpringBootApplication
public class Application {

	@Bean(name = "customProperties")
	public Map<String, String> customProperties() {
		Map<String, String> customProperties = new HashMap<String, String>();

		return customProperties;
	}

	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(Application.class);
		springApplication.addListeners(new ApplicationPidFileWriter());
		ApplicationContext ctx = springApplication.run(args);

		Assert.notNull(ctx);
	}

}
