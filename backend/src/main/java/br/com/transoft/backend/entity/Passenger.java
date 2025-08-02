package br.com.transoft.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "passenger")
public class Passenger {

    @Id
    @Column(name = "passenger_id")
    private String passengerId;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    private UserAccount userAccount;

    private School school;

    private Address address;

    public Passenger() {
    }

    public Passenger(String passengerId, String name, String email, String phoneNumber) {
        this.passengerId = passengerId;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(String passengerId) {
        this.passengerId = passengerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
