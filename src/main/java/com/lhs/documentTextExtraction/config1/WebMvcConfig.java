package com.lhs.documentTextExtraction.config1;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import nz.net.ultraq.thymeleaf.LayoutDialect;






@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer{


			
			@Override
			public void addResourceHandlers(ResourceHandlerRegistry registry) {
				registry.addResourceHandler("/resources/**").addResourceLocations("/resources/").addResourceLocations("classpath:/resources/");
				registry.addResourceHandler("/config/**").addResourceLocations("/config/").addResourceLocations("classpath:/config/");
				registry.addResourceHandler("/static/css/**").addResourceLocations("/css/").addResourceLocations("classpath:/static/css/");
				registry.addResourceHandler("/static/images/**").addResourceLocations("/images/").addResourceLocations("classpath:/static/images/");
				registry.addResourceHandler("/static/js/**").addResourceLocations("/js/").addResourceLocations("classpath:/static/js/");
				registry.addResourceHandler("/static/sass/**").addResourceLocations("/sass/").addResourceLocations("classpath:/static/sass/");
				registry.addResourceHandler("/static/stylesheets/**").addResourceLocations("/stylesheets/").addResourceLocations("classpath:/static/stylesheets/");
				registry.addResourceHandler("/templates/**").addResourceLocations("/templates/").addResourceLocations("classpath:/templates/");
				registry.addResourceHandler("/templates/pages/**").addResourceLocations("/pages/").addResourceLocations("classpath:/templates/pages/");
				registry.addResourceHandler("/static/font/**").addResourceLocations("/font/").addResourceLocations("classpath:/static/font/");
				registry.addResourceHandler("/static/font-awesome/**").addResourceLocations("/img/").addResourceLocations("classpath:/static/font-awesome/");
			}// End Method
			
		
		
		


	@Bean
	public LayoutDialect layoutDialect() {
		return new LayoutDialect();
	}// End Method

}
