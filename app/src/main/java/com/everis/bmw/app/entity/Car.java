package com.everis.bmw.app.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@NotNull(message = "registration date cannot be null")
	private Date registration;

	@Column(name = "COUNTRY", nullable = false)
	@NotEmpty(message = "country field cannot be null or empty")
	private String country;

	@Column(name = "CREATED_AT", nullable = false, updatable = false)
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date createdAt;

	@Column(name = "LAST_UPDATED", nullable = false)
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date lastUpdated;

	@PrePersist
	private void initEntity() {
		this.createdAt = new Date();
		this.lastUpdated = new Date();
	}
	
	@PreUpdate
	private void updateEntity() {
		this.lastUpdated = new Date();
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

	public Date getRegistration() {
		return registration;
	}

	public void setRegistration(Date registration) {
		this.registration = registration;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	
	public Car update(Car car) {
		this.brand = car.getBrand();
		this.country = car.getCountry();
		return this;
	}


}
