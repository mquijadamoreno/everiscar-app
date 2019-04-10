package com.everis.bmw.app;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.glassfish.jersey.jackson.JacksonFeature;

import com.everis.bmw.app.boundary.CarResource;

@ApplicationPath("api/v1")
public class JAXRSConfiguration extends Application {
	
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        //Resources
        classes.add(CarResource.class);
                       
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
        return instances;
    }

}