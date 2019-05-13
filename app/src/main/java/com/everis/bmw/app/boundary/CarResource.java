package com.everis.bmw.app.boundary;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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

	@EJB
	private CarService carService;

	@Context
	UriInfo uriInfo;

	private final Logger LOG = Logger.getLogger(this.getClass().getName());

	@GET
	public Response getAllCars(@QueryParam("start") int start, @QueryParam("size") int size) {
		LOG.info("Called getAllCars with the params  => start:" + start + " and size:" + size);
		List<Car> cars;
		if (start >= 0 && size > 0) {
			cars = this.carService.getCarsPaginated(start, size);
		} else {
			cars = this.carService.getCars();
		}
		LOG.info("Returning from getAllCars with the collection size of:"+cars.size());
		return Response.status(Status.OK).entity(cars).build();
	}
	
	@GET
	@Path("/count")
	public Response getCollectionSize() {
		Long count = this.carService.getCarsCount();
		return Response.status(Status.OK).entity(count).build();
	}

	@GET
	@Path("{id}")
	public Response getCarById(@PathParam("id") String id) {

		Response response;

		if (id == null)
			response = Response.status(Status.BAD_REQUEST).build();
		try {
			Car car = this.carService.getCar(id);
			response = Response.status(Status.OK).entity(car).build();
		} catch (CarNotFoundException e) {
			response = Response.status(Status.NOT_FOUND).build();
		}
		return response;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createCar(Car car) {
		Car carAux;
		if (carService.getCar(car.getId()) == null) {
			carAux = this.carService.createCar(car);
			UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
			uriBuilder.path(carAux.getId().toString());
			return Response.created(uriBuilder.build()).entity(carAux).build();
		} else {
			return Response.status(Status.BAD_REQUEST).build();
		}

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
	public Response deleteCar(@PathParam("id") String id) {
		Response response;
		if (id == null)
			response = Response.status(Status.BAD_REQUEST).build();
		try {
			Car removedCar = this.carService.deleteCar(id);
			response = Response.status(Status.NO_CONTENT).entity(removedCar).build();
		} catch (CarNotFoundException e) {
			response = Response.status(Status.NOT_FOUND).build();
		}
		return response;
	}
}