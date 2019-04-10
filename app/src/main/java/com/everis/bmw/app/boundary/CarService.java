package com.everis.bmw.app.boundary;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.everis.bmw.app.entity.Car;
import com.everis.bmw.app.exceptions.CarNotFoundException;
import com.everis.bmw.app.exceptions.CarStateNotValidException;

/**
 * CarService class.
 * 
 * @author Miguel Quijada Moreno
 *
 */
@Stateless
public class CarService {
	
	@PersistenceContext
	private EntityManager em;

	private final Logger LOG = Logger.getLogger(this.getClass().getName());
	
	/** 
	 * Method to retrieve a car from the database with a specific given id.
	 * @param id Parameter used to search the car.
	 * @return Returns the car if found.
	 * @throws CarNotFoundException Exception thrown if the car wasnt found.
	 */
	public Car getCar(String id) throws CarNotFoundException {
		LOG.info("Entering getCar(id) method with id " + id + " ..");
		if(id == null) 
			return null;
		
		Car car = em.find(Car.class, id);
		if(car == null) {
			LOG.info("Returning from getCar(id) method, CarNotFoundException thrown..");
			throw new CarNotFoundException("Car not found");
		}
		LOG.info("Returning from getCar(id) method, Car succesfully retrieved..");
		return car;
	}
	
	/**
	 * Method to retrieve the list of all cars from the database.
	 * @return Returns the list with the cars retrieved.
	 */
	public List<Car> getCars(){
		LOG.info("Entering getAllCars() method..");
		TypedQuery<Car> getAllCars = em.createNamedQuery(Car.CAR_GET_ALL_NAMED_QUERY, Car.class);
		List<Car> cars = getAllCars.getResultList();
		LOG.info("Returning from getAllCars() method..");
		return cars;
	}
	
	
	/**
	 * Method to create a car in the database. Id is generated in this method before persisting.
	 * @param car Parameter that represents the data of the car.
	 * @return Returns the car if created.
	 * @throws CarStateNotValidException Exception thrown if the state of the cars is invalid after persisting.
	 */
	public Car createCar(Car car) throws CarStateNotValidException {
		LOG.info("Entering createCar(car) method..");
		try {
			car.setId(UUID.randomUUID().toString());
			this.em.persist(car);
			this.em.flush();
			this.em.refresh(car);
		} catch(IllegalStateException exception) {
			LOG.info("Returning from createCar(car) method, CarStateNotValidException thrown..");
			throw new CarStateNotValidException("Car state not valid");
		}
		LOG.info("Returning from createCar(car) method, Car succesfully created..");
		return car;
	}
	
	/**
	 * Method to update an existing car.
	 * @param car Parameter that represents the data of a car to be updated.
	 * @return Returns the car with the updated fields if found.
	 */
	public Car updateCar(Car car) {
		LOG.info("Entering updateCar(car) method..");
		Car carToUpdate = getCar(car.getId());
		carToUpdate.update(car);
		LOG.info("Returning from updateCar() method, Car succesfully updated..");
		return carToUpdate;
	}
	
	/**
	 * Method to delete an existing car.
	 * @param id Parameter given to retrieve the car to delete.
	 */
	public void deleteCar(String id) {
		LOG.info("Entering deleteCar(car) method..");
		Car carToRemove = getCar(id);
		em.remove(carToRemove);
		LOG.info("Returning from deleteCar(car) method, Car succesfully deleted..");
	}
}
