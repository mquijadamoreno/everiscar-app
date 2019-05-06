package com.everis.bmw.app.boundary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.everis.bmw.app.entity.Car;
import com.everis.bmw.app.exceptions.CarNotFoundException;

@RunWith(MockitoJUnitRunner.class)
public class CarServiceTest {
	
	@Mock
	private EntityManager em;
	
	@Mock
	private Query query;
	
	@Mock
	private TypedQuery<Car> typedQuery;
	
	@Mock
	private Car car;
	
	@InjectMocks
	private CarService carService;

	@Test
	public void whenGettingCar_ifIdNullOrInvalid_shouldThrowException() {
		try {
			carService.getCar(null);	
		} catch (RuntimeException e) {
			assertTrue(e instanceof CarNotFoundException);
		}
		
		Mockito.when(em.find(Car.class, "mock_id")).thenReturn(null);
		try {
			carService.getCar("mock_id");	
		} catch (RuntimeException e) {
			assertTrue(e instanceof CarNotFoundException);
		}
		
	}
	
	@Test
	public void whenGettingCar_methodShouldReturnSuccesfully() {
		final Car car = new Car();
		car.setBrand("mock_brand");
		car.setCountry("mock_country");
		car.setCreatedAt(new Date());
		car.setLastUpdated(new Date());
		car.setRegistration(new Date());
		car.setId("mock_id");
		
		Mockito.when(em.find(Car.class, "mock_id")).thenReturn(car);
		
		Car carResponse = carService.getCar("mock_id");
		
		assertEquals(car, carResponse);
		
	}
	
	@Test
	public void whenGettingCarsCount_methodShouldReturnSuccessfully() {
		final List<Car> cars = new ArrayList<>();
		
		Mockito.when(em.createQuery("SELECT COUNT(c) FROM Car c")).thenReturn(query);
		Mockito.when(query.getSingleResult()).thenReturn((long) cars.size());
		
		long size = carService.getCarsCount();
		
		assertEquals((long)cars.size(), size);
	}
	
	@Test
	public void whenGettingCarsPaginated_methodShouldReturnSuccesfully() {
		final List<Car> cars = new ArrayList<>();
		Mockito.when(em.createNamedQuery(Car.CAR_GET_ALL_NAMED_QUERY, Car.class)).thenReturn(typedQuery);
		Mockito.when(typedQuery.setMaxResults(Mockito.anyInt())).thenReturn(typedQuery);
		Mockito.when(typedQuery.setFirstResult(Mockito.anyInt())).thenReturn(typedQuery);
		Mockito.when(typedQuery.getResultList()).thenReturn(cars);
		
		List<Car> carsResponse = carService.getCarsPaginated(10, 10);
		
		assertEquals(cars, carsResponse);
	}
	
	@Test
	public void whenGettingCars_methodShouldReturnSuccesfully() {
		final List<Car> cars = new ArrayList<>();
		Mockito.when(em.createNamedQuery(Car.CAR_GET_ALL_NAMED_QUERY, Car.class)).thenReturn(typedQuery);
		Mockito.when(typedQuery.getResultList()).thenReturn(cars);
		
		List<Car> carsResponse = carService.getCars();
		
		assertEquals(cars, carsResponse);
	}
	
	@Test
	public void whenCreatingCar_methodShouldReturnSuccessfully() {
		final Car car = new Car();
		car.setBrand("mock_brand");
		car.setCountry("mock_country");
		car.setCreatedAt(new Date());
		car.setLastUpdated(new Date());
		car.setRegistration(new Date());
		
		Mockito.doNothing().when(em).persist(Mockito.any());
		Mockito.doNothing().when(em).flush();
		Mockito.doNothing().when(em).refresh(Mockito.any());
		
		Car carResponse = carService.createCar(car);
		
		assertEquals(car, carResponse);
	}
	
	@Test
	public void whenUpdatingCar_andIdNull_methodShouldFail() {
		Exception exception = null;
		try {
			final Car car = new Car();
			car.setId(null);
			carService.updateCar(car.getId(), car);
		} catch (Exception e) {
			exception = e;
			assertTrue(e instanceof CarNotFoundException);
		}
		assertNotEquals(null, exception);
	}
	
	@Test
	public void whenUpdatingCar_andIdNotValid_methodShouldFail() {
		Exception exception = null;
		try {
			final Car car = new Car();
			car.setId("mock_id");
			carService.updateCar(car.getId(), car);
		} catch (Exception e) {
			exception = e;
			assertTrue(e instanceof CarNotFoundException);
		}
		assertNotEquals(null, exception);
	}
	
	@Test
	public void whenUpdatingCar_methodShouldReturnSuccesfully() {
		final Car car = new Car();
		car.setId("mock_id");
		car.setBrand("mock_brand");
		car.setCountry("mock_country");
		car.setCreatedAt(new Date());
		car.setLastUpdated(new Date());
		car.setRegistration(new Date());
		
		Mockito.when(em.find(Car.class, "mock_id")).thenReturn(car);
		Mockito.when(this.car.update(car)).thenReturn(car);
		
		Car carResponse = carService.updateCar("mock_id", car);
		
		assertEquals(car, carResponse);
	}
	
	@Test
	public void whenDeletingCar_ifIdNull_methodShouldFail() {
		Exception exception = null;
		try {
			final Car car = new Car();
			car.setId(null);
			carService.deleteCar(car.getId());
		} catch (Exception e) {
			exception = e;
			assertTrue(e instanceof CarNotFoundException);
		}
		assertNotEquals(null, exception);
	}
	
	@Test
	public void whenDeletingCar_ifIdNotValid_methodShouldFail() {
		Exception exception = null;
		try {
			final Car car = new Car();
			car.setId("mock_id");
			carService.deleteCar(car.getId());
		} catch (Exception e) {
			exception = e;
			assertTrue(e instanceof CarNotFoundException);
		}
		assertNotEquals(null, exception);
	}
	
	@Test
	public void whenDeletingCar_methodShouldReturnSuccesfully() {
		Exception exception = null;
		try {
			final Car car = new Car();
			car.setId("mock_id");
			car.setBrand("mock_brand");
			car.setCountry("mock_country");
			car.setCreatedAt(new Date());
			car.setLastUpdated(new Date());
			car.setRegistration(new Date());
			
			Mockito.when(em.find(Car.class, "mock_id")).thenReturn(car);
			Mockito.doNothing().when(em).remove(car);
			carService.deleteCar(car.getId());
		} catch (Exception e) {
			exception = e;
		}
		
		assertEquals(null, exception);
	}
	
	
	
	
}
