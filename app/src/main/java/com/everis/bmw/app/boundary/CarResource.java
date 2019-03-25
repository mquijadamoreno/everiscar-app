package com.everis.bmw.app.boundary;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import com.everis.bmw.app.entity.Car;
import com.everis.bmw.app.exceptions.CarNotFoundException;

@Path("cars")
@Produces(MediaType.APPLICATION_JSON)
public class CarResource {
	
	@Inject
	private CarService carService;
	
	@Context
	UriInfo uriInfo;
	
	@GET
	public Response getAllCars() {
		List<Car> cars = this.carService.getCars();
		return Response.status(Status.OK).entity(cars).build();
	}
	
	@GET
	@Path("{id}")
	public Response getCarById(@PathParam("id" )UUID id) {
		
		Response response;
		
		if(id == null)
			response = Response.status(Status.BAD_REQUEST).build();
		try {
			Car car = this.carService.getCar(id);
			response =  Response.status(Status.OK).entity(car).build();
		} catch (CarNotFoundException e) {
			response = Response.status(Status.NOT_FOUND).build();
		}
		return response;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createCar(Car car) {
		Car carAux;
		try {
			carAux = carService.getCar(car.getId());
		} catch (CarNotFoundException e) {
			carAux = this.carService.createCar(car);
			UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
			uriBuilder.path(carAux.getId().toString());
			return Response.created(uriBuilder.build()).build();
		}
		return Response.status(Status.CONFLICT).entity(carAux).build();
		
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateCar(Car car) {
		Response response;
		try {
			Car updatedCar = this.carService.updateCar(car);
			response = Response.status(Status.OK).entity(updatedCar).build();
		} catch (CarNotFoundException e) {
			response = Response.status(Status.NOT_FOUND).build();
		}
		return response;
	}

	@DELETE
	@Path("{id}")
	public Response deleteUser(@PathParam("id")UUID id) {
		Car car = (Car) getCarById(id).getEntity();
		this.carService.deleteCar(car);
		return Response.status(Status.NO_CONTENT).build();
	}
}