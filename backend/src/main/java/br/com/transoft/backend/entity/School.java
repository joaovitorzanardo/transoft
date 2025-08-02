package br.com.transoft.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "school")
public class School {

    @Id
    @Column(name = "school_id")
    private String schoolId;

    @Column(name = "name")
    private String name;

    private Address address;

    public School() {
    }

    public School(String schoolId, String name, Address address) {
        this.schoolId = schoolId;
        this.name = name;
        this.address = address;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

}
