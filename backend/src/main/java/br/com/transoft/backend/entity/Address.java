package br.com.transoft.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "address")
public class Address {

    @Id
    @Column(name = "address_id")
    private String addressId;

    @Column(name = "cep")
    private String cep;

    @Column(name = "street")
    private String street;

    @Column(name = "district")
    private String district;

    @Column(name = "number")
    private Integer number;

    @Column(name = "complement")
    private String complement;

    @Column(name = "latitude")
    private String latitude;

    @Column(name = "longitude")
    private String longitude;

    public Address() {
    }

    public Address(String addressId, String cep, String street, String district, Integer number, String complement, String latitude, String longitude) {
        this.addressId = addressId;
        this.cep = cep;
        this.street = street;
        this.district = district;
        this.number = number;
        this.complement = complement;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getComplement() {
        return complement;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

}
