/**
 * 
 */
package com.niit.collaboration.restbackend.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @author Deepika
 *
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.niit")
public class WebConfig {

}
