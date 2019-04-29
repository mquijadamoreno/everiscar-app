package com.everis.bmw.app.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "CARS")
@NamedQueries({ 
	@NamedQuery(name = Car.CAR_GET_ALL_NAMED_QUERY, query = "SELECT c FROM Car c"), 
	@NamedQuery(name = Car.CAR_GET_CAR_COUNT, query = "SELECT COUNT(c) FROM Car c")})
public class Car implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String CAR_GET_ALL_NAMED_QUERY = "getAllCars";
	public static final String CAR_GET_CAR_COUNT = "getCarCount";

	@Id
	@Column(name = "ID", nullable = false, unique = true)
	@NotNull(message = "id field cannot be null")
	private String id;

	@Column(name = "BRAND", nullable = false)
	@NotEmpty(message = "brand field cannot be null or empty")
	private String brand;

	@Column(name = "REGISTRATION", nullable = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone="UTC")
	@NotNull(message = "registration date cannot be null")
	private LocalDate registration;

	@Column(name = "COUNTRY", nullable = false)
	@NotEmpty(message = "country field cannot be null or empty")
	private String country;

	@Column(name = "CREATED_AT", nullable = false, updatable = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone="UTC")
	private LocalDate createdAt;

	@Column(name = "LAST_UPDATED", nullable = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone="UTC")
	private LocalDate lastUpdated;

	@PrePersist
	private void initEntity() {
		this.createdAt = LocalDate.now(ZoneId.of("UTC"));
		this.lastUpdated = LocalDate.now(ZoneId.of("UTC"));
		
	}
	
	@PreUpdate
	private void updateEntity() {
		this.lastUpdated = LocalDate.now(ZoneId.of("UTC"));
	}
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public LocalDate getRegistration() {
		return registration;
	}

	public void setRegistration(LocalDate registration) {
		this.registration = registration;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
	public LocalDate getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDate createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDate getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(LocalDate lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	
	public Car update(Car car) {
		this.brand = car.getBrand();
		this.country = car.getCountry();
		return this;
	}


}
