package com.everis.bmw.app.boundary;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.everis.bmw.app.entity.Car;
import com.everis.bmw.app.exceptions.CarNotFoundException;
import com.everis.bmw.app.exceptions.CarStateNotValidException;

@Stateless
public class CarService {
	
	@PersistenceContext
	private EntityManager em;

	private final Logger LOG = Logger.getLogger(this.getClass().getName());
	
	public Car getCar(String id) {
		if(id == null) 
			return null;
		
		Car car = em.find(Car.class, id);
		if(car == null) {
			throw new CarNotFoundException("Car not found");
		}
		return car;
	}
	
	public long getCarsCount(){
		Query getCarCount = em.createNamedQuery(Car.CAR_GET_CAR_COUNT, Car.class);
		long count = (long) getCarCount.getSingleResult();
		return count;
	}
	
	public List<Car> getCars(){
		TypedQuery<Car> getAllCars = em.createNamedQuery(Car.CAR_GET_ALL_NAMED_QUERY, Car.class);
		List<Car> cars = getAllCars.getResultList();
		return cars;
	}
	
	public List<Car> getCarsPaginated(int start, int size){
		List<Car> cars = getCars();
		if(start + size > cars.size()) return new ArrayList<Car>();
		return cars.subList(start, start+size);
	}
	
	public Car createCar(Car car) {
		try {
			car.setId(UUID.randomUUID().toString());
			car.setCreatedAt(new Date());
			car.setLastUpdated(new Date());
			car.setRegistration(new Date());
			this.em.persist(car);
			this.em.flush();
			this.em.refresh(car);
		} catch(IllegalStateException exception) {
			throw new CarStateNotValidException("Car state not valid");
		}
		
		return car;
	}
	
	public Car updateCar(Car car) {
		Car carToUpdate = getCar(car.getId()).update(car);
		return carToUpdate;
	}
	
	public Car deleteCar(String id) {
		Car carToRemove = getCar(id);
		em.remove(carToRemove);
		return carToRemove;
	}
}
