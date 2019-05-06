package com.everis.bmw.app.exceptions;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class CarStateNotValidExceptionMapper implements ExceptionMapper<CarStateNotValidException> {

	@Override
	public Response toResponse(CarStateNotValidException exception) {
		return Response.status(Response.Status.BAD_REQUEST)
				.entity(exception.getMessage())
				.type(MediaType.APPLICATION_JSON)
				.build();
	}
}
