package com.everis.bmw.app.exceptions;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class CarNotFoundExceptionMapper implements ExceptionMapper<CarNotFoundException> {

	@Override
	public Response toResponse(CarNotFoundException exception) {
		return Response.status(Response.Status.NOT_FOUND)
				.entity(exception.getMessage())
				.type(MediaType.APPLICATION_JSON)
				.build();
	}

}
