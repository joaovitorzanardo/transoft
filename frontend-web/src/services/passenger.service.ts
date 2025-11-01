import type { AxiosResponse } from "axios";
import { axiosInstance } from "./axios-instance";
import type PassengerDto from "../models/PassengerDto";

export function getPassengersStats(): Promise<AxiosResponse> {
    return axiosInstance.get(`/passengers/stats`);
}

export function getPassengers(page: number, size: number): Promise<AxiosResponse> {
    return axiosInstance.get(`/passengers?page=${page}&size=${size}`);
}

export function getPassengersById(passengerId: string): Promise<AxiosResponse> {
    return axiosInstance.get(`/passengers/${passengerId}`);
}

export function savePassenger(passengerDto: PassengerDto): Promise<AxiosResponse> {
    return axiosInstance.post(`/passengers`, passengerDto);
}

export function updatePassenger(passengerId: string, passengerDto: PassengerDto): Promise<AxiosResponse> {
    return axiosInstance.patch(`/passengers/${passengerId}`, passengerDto);
}

export function getPassengerById(passengerId: string): Promise<AxiosResponse> {
    return axiosInstance.get(`/passengers/${passengerId}`);
}

export function enablePassenger(passengerId: string): Promise<AxiosResponse> {
    return axiosInstance.patch(`/passengers/${passengerId}/enable`);
}

export function disablePassenger(passengerId: string): Promise<AxiosResponse> {
    return axiosInstance.patch(`/passengers/${passengerId}/disable`);
}