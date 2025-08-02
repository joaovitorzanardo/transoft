package br.com.transoft.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "driver")
public class Driver {

    @Id
    @Column(name = "driver_id")
    private String driverId;

    @Column(name = "name")
    private String name;

    @Column(name = "cnh_number")
    private String cnhNumber;

    @Column(name = "cnh_expiration_date")
    private LocalDate cnhExpirationDate;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    private UserAccount userAccount;

    public Driver() {

    }

    public Driver(String driverId, String name, String cnhNumber, LocalDate cnhExpirationDate, String email, String phoneNumber) {
        this.driverId = driverId;
        this.name = name;
        this.cnhNumber = cnhNumber;
        this.cnhExpirationDate = cnhExpirationDate;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String id) {
        this.driverId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCnhNumber() {
        return cnhNumber;
    }

    public void setCnhNumber(String cnhNumber) {
        this.cnhNumber = cnhNumber;
    }

    public LocalDate getCnhExpirationDate() {
        return cnhExpirationDate;
    }

    public void setCnhExpirationDate(LocalDate cnhExpirationDate) {
        this.cnhExpirationDate = cnhExpirationDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}
