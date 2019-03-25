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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.everis.bmw.app.entity.Car;

@Path("cars")
@Produces(MediaType.APPLICATION_JSON)
public class CarResource {
	
	@Inject
	private CarService carService;
	
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
		} catch (Exception e) {
			response = Response.status(Status.NOT_FOUND).build();
		}
		return response;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createCar(Car car) {
		Car createdCar = this.carService.createCar(car);
		return Response.status(Status.CREATED).entity(createdCar).build();
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateCar(Car car) {
		Car updatedCar = this.carService.updateCar(car);
		return Response.status(Status.OK).entity(updatedCar).build();
	}

	@DELETE
	@Path("{id}")
	public Response deleteUser(@PathParam("id")UUID id) {
		Car car = (Car) getCarById(id).getEntity();
		this.carService.deleteCar(car);
		return Response.status(Status.OK).entity(car).build();
	}
}