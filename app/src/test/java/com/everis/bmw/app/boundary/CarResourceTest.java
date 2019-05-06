package com.everis.bmw.app.boundary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriBuilderException;
import javax.ws.rs.core.UriInfo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.everis.bmw.app.entity.Car;
import com.everis.bmw.app.exceptions.CarNotFoundException;

@RunWith(MockitoJUnitRunner.class)
public class CarResourceTest {

	@InjectMocks
	private CarResource carResource;

	@Mock
	private CarService carService;

	@Mock
	private UriInfo uriInfo;

	@Mock
	private UriBuilder uriBuilder;

	@Test
	public void whenGettingAllCars_listShouldReturnSuccessfully() {
		final List<Car> cars = new ArrayList<>();

		Mockito.when(carService.getCarsPaginated(0, 10)).thenReturn(cars);
		Mockito.when(carService.getCarsPaginated(1, 10)).thenReturn(cars);
		Mockito.when(carService.getCars()).thenReturn(cars);
		Mockito.when(carService.getCarsPaginated(1, 0)).thenReturn(cars);

		final Response response = carResource.getAllCars(0, 10);
		final Response response2 = carResource.getAllCars(1, 10);
		final Response response3 = carResource.getAllCars(0, 0);
		final Response response4 = carResource.getAllCars(1, 0);
		final Response response5 = carResource.getAllCars(-1, -1);

		assertEquals(Status.OK.getStatusCode(), response.getStatus());
		assertEquals(cars, response.getEntity());
		
		assertEquals(Status.OK.getStatusCode(), response2.getStatus());
		assertEquals(cars, response2.getEntity());
		
		assertEquals(Status.OK.getStatusCode(), response3.getStatus());
		assertEquals(cars, response3.getEntity());
		
		assertEquals(Status.OK.getStatusCode(), response4.getStatus());
		assertEquals(cars, response4.getEntity());
		
		assertEquals(Status.OK.getStatusCode(), response5.getStatus());
		assertEquals(cars, response5.getEntity());
	}

	@Test
	public void whenGettingCollectionSize_methodShouldReturnSizeSuccesfully() {
		final List<Car> cars = new ArrayList<>();

		Mockito.when(carService.getCarsCount()).thenReturn((long) cars.size());

		final Response response = carResource.getCollectionSize();

		assertEquals(Status.OK.getStatusCode(), response.getStatus());
		assertEquals((long) cars.size(), response.getEntity());

	}

	@Test
	public void whenGettingCarById_methodShouldReturnSuccesfully() {
		final Car car = new Car();
		car.setBrand("mock_brand");
		car.setCountry("mock_country");
		car.setCreatedAt(new Date());
		car.setLastUpdated(new Date());
		car.setRegistration(new Date());
		car.setId("mock_id");

		Mockito.when(carService.getCar(Mockito.eq("mock_id"))).thenReturn(car);

		final Response response = carResource.getCarById("mock_id");

		assertEquals(Status.OK.getStatusCode(), response.getStatus());
		assertEquals(car, response.getEntity());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void whenGettingCarById_ifNotFound_methodReturnsNoEntity() throws Exception {

		Mockito.when(carService.getCar(Mockito.anyString())).thenThrow(CarNotFoundException.class);

		final Response response = carResource.getCarById("mock_id2");

		assertEquals(Status.NOT_FOUND.getStatusCode(), response.getStatus());
		assertEquals(null, response.getEntity());
	}

	@Test
	public void whenGettingCarById_ifIdIsNull_methodReturnsBadRequest() {

		final Response response = carResource.getCarById(null);

		assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatus());
		assertEquals(null, response.getEntity());
	}

	@Test
	public void whenCreatingCar_methodShouldReturnSuccesfully() {
		try {
			final Car car = new Car();
			car.setBrand("mock_brand");
			car.setCountry("mock_country");
			car.setCreatedAt(new Date());
			car.setLastUpdated(new Date());
			car.setRegistration(new Date());

			final Car serviceCar = new Car();
			serviceCar.setBrand(car.getBrand());
			serviceCar.setCountry(car.getCountry());
			serviceCar.setCreatedAt(car.getCreatedAt());
			serviceCar.setLastUpdated(car.getLastUpdated());
			serviceCar.setRegistration(car.getRegistration());
			serviceCar.setId("mock_id");

			Mockito.when(carService.createCar(car)).thenReturn(serviceCar);
			Mockito.when(uriInfo.getAbsolutePathBuilder()).thenReturn(uriBuilder);
			Mockito.when(uriBuilder.path(Mockito.anyString())).thenReturn(uriBuilder);
			Mockito.when(uriBuilder.build()).thenReturn(new URI("www.mock.com"));

			final Response response = carResource.createCar(car);

			assertEquals(Status.CREATED.getStatusCode(), response.getStatus());
			assertEquals(serviceCar, response.getEntity());

		} catch (IllegalArgumentException | UriBuilderException | URISyntaxException e) {
			assertTrue(false);
		}

	}

	@Test
	public void whenCreatingCar_withAnyId_methodShouldReturnBadRequest() {
		final Car car = new Car();
		car.setId("mock_id");

		final Response response = carResource.createCar(car);

		assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatus());
		assertEquals(null, response.getEntity());
	}

	@Test
	public void whenCreatingCar_ifCarNull_methodShouldReturnBadRequest() {
		final Car car = null;
		final Response response = carResource.createCar(car);

		assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatus());
		assertEquals(null, response.getEntity());
	}

	@Test
	public void whenUpdatingCar_ifCarNull_methodShouldReturnBadRequest() {
		final Car car = null;
		final Response response = carResource.updateCar("mock_id", car);

		assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatus());
		assertEquals(null, response.getEntity());
	}

	@Test
	public void whenUpdatingCar_ifCarIdNull_methodShouldReturnBadRequest() {
		final Car car = new Car();
		final Response response = carResource.updateCar("mock_id", car);

		assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatus());
		assertEquals(null, response.getEntity());
	}

	@Test
	public void whenUpdatingCar_ifCarIdNotEquals_methodShouldReturnBadRequest() {
		final Car car = new Car();
		car.setId("mock_id_2");
		final Response response = carResource.updateCar("mock_id", car);

		assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatus());
		assertEquals(null, response.getEntity());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void whenUpdatingCar_ifIdNotValid_methodShouldReturnNotFound() {
		final Car car = new Car();
		car.setBrand("mock_brand");
		car.setCountry("mock_country");
		car.setCreatedAt(new Date());
		car.setLastUpdated(new Date());
		car.setRegistration(new Date());
		car.setId("mock_id");

		Mockito.when(carService.updateCar("mock_id", car)).thenThrow(CarNotFoundException.class);
		final Response response = carResource.updateCar("mock_id", car);

		assertEquals(Status.NOT_FOUND.getStatusCode(), response.getStatus());
		assertEquals(null, response.getEntity());
	}

	@Test
	public void whenUpdatingCar_methodShouldReturnSuccesfully() {
		final Car car = new Car();
		car.setBrand("mock_brand");
		car.setCountry("mock_country");
		car.setCreatedAt(new Date());
		car.setLastUpdated(new Date());
		car.setRegistration(new Date());
		car.setId("mock_id");

		Mockito.when(carService.updateCar("mock_id", car)).thenReturn(car);
		final Response response = carResource.updateCar("mock_id", car);

		assertEquals(Status.OK.getStatusCode(), response.getStatus());
		assertEquals(car, response.getEntity());
	}

	@Test
	public void whenDeletingCar_ifIdNull_methodShouldReturnBadRequest() {
		final Response response = carResource.deleteCar(null);
		assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatus());
		assertEquals(null, response.getEntity());
	}

	@Test
	public void whenDeletingCar_ifIdNotValid_methodShouldReturnNotFound() {
		Mockito.doThrow(CarNotFoundException.class).when(carService).deleteCar(Mockito.anyString());

		final Response response = carResource.deleteCar("mock_id");
		assertEquals(Status.NOT_FOUND.getStatusCode(), response.getStatus());
		assertEquals(null, response.getEntity());
	}

	@Test
	public void whenDeletingCar_methodShouldReturnSuccesfully() {
		Mockito.doNothing().when(carService).deleteCar(Mockito.anyString());

		final Response response = carResource.deleteCar("mock_id");
		assertEquals(Status.NO_CONTENT.getStatusCode(), response.getStatus());
		assertEquals(null, response.getEntity());
	}

}
