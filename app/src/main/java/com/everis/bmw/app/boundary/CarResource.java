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

/**
 * CarResource class.
 * 
 * @author Miguel Quijada Moreno
 *
 */
@Path("cars")
@Produces(MediaType.APPLICATION_JSON)
public class CarResource {

	@EJB
	private CarService carService;

	@Context
	UriInfo uriInfo;

	private final Logger LOG = Logger.getLogger(this.getClass().getName());

	/**
	 * Get all the cars.
	 * 
	 * @return Returns the Response with the list of the cars.
	 */

	@GET
	public Response getAllCars(@QueryParam("start") int start, @QueryParam("size") int size) {
		LOG.info("Entering getAllCars(start,size) with start: " + start + " and size: " + size + " ..");
		List<Car> cars;
		if (start >= 0 && size > 0) {
			cars = this.carService.getCarsPaginated(start, size);
		} else {
			cars = this.carService.getCars();
		}
		LOG.info("Returning from getAllCars(start,size) with the collection size of:" + cars.size()
				+ ", HTTP CODE -> OK");
		return Response.status(Status.OK).entity(cars).build();
	}

	/**
	 * Get the total size of the cars collection stored in the database.
	 * 
	 * @return Returns the response with the size of the collection.
	 */
	@GET
	@Path("/count")
	public Response getCollectionSize() {
		LOG.info("Entering getCollectionSize() ..");
		Long count = this.carService.getCarsCount();
		LOG.info("Returning from getCollectionSize() with size = " + count + " .., HTTP CODE -> OK");
		return Response.status(Status.OK).entity(count).build();
	}

	/**
	 * Get an specific car distinguished by its id.
	 * 
	 * @param id Parameter used to search the car.
	 * @return Returns the response with the car if it exists with the appropiated
	 *         HTTP code.
	 */
	@GET
	@Path("{id}")
	public Response getCarById(@PathParam("id") String id) {

		LOG.info("Entering getCarById(id) method with id = " + id + " ..");
		Response response;
		String logInfo;
		if (id == null) {
			response = Response.status(Status.BAD_REQUEST).build();
			logInfo = "HTTP CODE -> BAD_REQUEST";
		} else {
			try {
				Car car = this.carService.getCar(id);
				response = Response.status(Status.OK).entity(car).build();
				logInfo = "HTTP CODE -> OK";
			} catch (CarNotFoundException e) {
				response = Response.status(Status.NOT_FOUND).build();
				logInfo = "HTTP CODE -> NOT_FOUND";
			}
		}

		LOG.info("Returning from getCarById(id) method, " + logInfo);
		return response;
	}

	/**
	 * Post method to request the creation of the car.
	 * 
	 * @param car Parameter given with the data needed to create the car.
	 * @return Returns the response with the car data if was succesfully created. If
	 *         doesnt, the response will contain the appropiated HTTP response.
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createCar(Car car) {
		LOG.info("Entering createCar(car) method..");
		Car carAux;
		if (car.getId() == null) {
			carAux = this.carService.createCar(car);
			UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
			uriBuilder.path(carAux.getId().toString());
			LOG.info("Returning from getCarById(car) method, HTTP CODE -> CREATED");
			return Response.created(uriBuilder.build()).entity(carAux).build();
		} else {
			LOG.info("Returning from getCarById(car) method, HTTP CODE -> BAD_REQUEST");
			return Response.status(Status.BAD_REQUEST).build();
		}

	}

	/**
	 * Put method used to update an existing car within the database.
	 * 
	 * @param id  Parameter that represents the id of the car to be updated.
	 * @param car Parameter that represents the car with the updated data.
	 * @return Returns the Response with the car data if it was succesfully updated.
	 *         If doesnt, the response will contain the appropiate HTTP response.
	 */
	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateCar(@PathParam("id") String id, Car car) {
		LOG.info("Entering updateCar(car) method..");
		String logInfo;
		Response response;

		if (!id.equals(car.getId())) {
			logInfo = "HTTP CODE -> BAD_REQUEST";
			response = Response.status(Status.BAD_REQUEST).build();
		} else {
			try {
				Car updatedCar = this.carService.updateCar(id, car);
				response = Response.status(Status.OK).entity(updatedCar).build();
				logInfo = "HTTP CODE -> OK";
			} catch (CarNotFoundException e) {
				response = Response.status(Status.NOT_FOUND).build();
				logInfo = "HTTP CODE -> NOT_FOUND";
			}
		}

		LOG.info("Returning from updateCar(Car) method, " + logInfo);
		return response;
	}

	/**
	 * Delete method used to delete an instance of a car from the database.
	 * 
	 * @param id Parameter that represents the id of the car to be deleted.
	 * @return Returns the response with the car deleted if found. If doesnt, it
	 *         returns the appropiated HTTP code.
	 */
	@DELETE
	@Path("{id}")
	public Response deleteCar(@PathParam("id") String id) {
		LOG.info("Entering deleteCar(id) with id = " + id + " ..");
		String logInfo;
		Response response;
		if (id == null) {
			response = Response.status(Status.BAD_REQUEST).build();
			logInfo = "HTTP CODE -> BAD_REQUEST";
		} else {
			try {
				this.carService.deleteCar(id);
				response = Response.status(Status.NO_CONTENT).build();
				logInfo = "HTTP CODE -> NO_CONTENT";
			} catch (CarNotFoundException e) {
				logInfo = "HTTP CODE -> NOT_FOUND";
				response = Response.status(Status.NOT_FOUND).build();
			}
		}
		LOG.info("Returning from deleteCar(id), " + logInfo);
		return response;
	}
}