package com.everis.bmw.app.car.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "CARS")
@NamedQueries({ @NamedQuery(name = Car.CAR_GET_ALL_NAMED_QUERY, query = "SELECT c FROM Car c") })
public class Car implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String CAR_GET_ALL_NAMED_QUERY = "getAllCars";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", nullable = false, unique = true)
	@NotNull(message = "id field cannot be null")
	private UUID id;

	@Column(name = "BRAND", nullable = false)
	@NotEmpty(message = "brand field cannot be null or empty")
	private String brand;

	@Column(name = "REGISTRATION", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull(message = "registration date cannot be null")
	private Timestamp registration;

	@Column(name = "COUNTRY", nullable = false)
	@NotEmpty(message = "country field cannot be null or empty")
	private String country;

	@Column(name = "CREATED_AT", nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull(message = "created_at date cannot be null")
	private Timestamp created_at;

	@Column(name = "LAST_UPDATED", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull(message = "last_updated date cannot be null")
	private Timestamp last_updated;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public Timestamp getRegistration() {
		return registration;
	}

	public void setRegistration(Timestamp registration) {
		this.registration = registration;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Timestamp getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Timestamp created_at) {
		this.created_at = created_at;
	}

	public Timestamp getLast_updated() {
		return last_updated;
	}

	public void setLast_updated(Timestamp last_updated) {
		this.last_updated = last_updated;
	}
	
	public Car update(Car car) {
		this.brand = car.getBrand();
		this.country = car.getCountry();
		this.created_at = car.getCreated_at();
		this.registration = car.getRegistration();
		this.last_updated = car.getLast_updated();
		return this;
	}

}
