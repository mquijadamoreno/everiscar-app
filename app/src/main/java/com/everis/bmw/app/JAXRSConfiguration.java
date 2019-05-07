package com.everis.bmw.app;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.glassfish.jersey.jackson.JacksonFeature;

import com.everis.bmw.app.boundary.CarResource;
import com.everis.bmw.app.exceptions.CarNotFoundExceptionMapper;
import com.everis.bmw.app.exceptions.CarStateNotValidExceptionMapper;
import com.everis.bmw.app.utils.CorsFilter;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

import io.swagger.v3.jaxrs2.integration.JaxrsOpenApiContextBuilder;
import io.swagger.v3.jaxrs2.integration.resources.AcceptHeaderOpenApiResource;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import io.swagger.v3.oas.integration.OpenApiConfigurationException;

@ApplicationPath("api/v1")
public class JAXRSConfiguration extends Application {

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> classes = new HashSet<>();
		// Resources
		classes.add(CarResource.class);
		classes.add(CorsFilter.class);

		// ExceptionMappers
		classes.add(CarNotFoundExceptionMapper.class);
		classes.add(CarStateNotValidExceptionMapper.class);
		classes.add(OpenApiResource.class);
		classes.add(AcceptHeaderOpenApiResource.class);

		try {
			new JaxrsOpenApiContextBuilder().configLocation("/configfile.json").buildContext(true);
		} catch (OpenApiConfigurationException e) {
			throw new RuntimeException(e.getMessage(), e);
		}

		return classes;
	}

	@Override
	public Map<String, Object> getProperties() {
		Map<String, Object> props = new HashMap<>();
		props.putIfAbsent("jersey.config.server.disableMoxyJson", true);
		return props;
	}

	@Override
	public Set<Object> getSingletons() {
		final Set<Object> instances = new HashSet<>();
		instances.add(new JacksonFeature());
		instances.add(new JacksonJaxbJsonProvider());
		return instances;
	}

}