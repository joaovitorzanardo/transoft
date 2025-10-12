import type { AxiosResponse } from "axios";
import { axiosInstance } from "./axios-instance";
import type PassengerDto from "../models/PassengerDto";

export function getPassengersStats(): Promise<AxiosResponse> {
    return axiosInstance.get(`/passengers/stats`, {
        headers: {
            'Authorization': `Bearer ${sessionStorage.getItem("apiToken")}`
        }
    });
}

export function getPassengers(page: number, size: number): Promise<AxiosResponse> {
    return axiosInstance.get(`/passengers?page=${page}&size=${size}`, {
        headers: {
            'Authorization': `Bearer ${sessionStorage.getItem("apiToken")}`
        }
    });
}

export function getPassengersById(passengerId: string): Promise<AxiosResponse> {
    return axiosInstance.get(`/passengers/${passengerId}`, {
        headers: {
            'Authorization': `Bearer ${sessionStorage.getItem("apiToken")}`
        }
    });
}

export function savePassenger(passengerDto: PassengerDto): Promise<AxiosResponse> {
    return axiosInstance.post(`/passengers`, passengerDto, {
        headers: {
            'Authorization': `Bearer ${sessionStorage.getItem("apiToken")}`
        }
    });
}

export function getPassengerById(passengerId: string): Promise<AxiosResponse> {
    return axiosInstance.get(`/passengers/${passengerId}`, {
        headers: {
            'Authorization': `Bearer ${sessionStorage.getItem("apiToken")}`
        }
    });
}