import type { AxiosResponse } from "axios";
import { axiosInstance } from "./axios-instance";
import type DriverDto from "../models/driver/DriverDto";

export function getDrivers(page: number, size: number): Promise<AxiosResponse> {
    return axiosInstance.get(`/drivers?page=${page}&size=${size}`)
}

export function getAllDrivers(): Promise<AxiosResponse> {
    return axiosInstance.get(`/drivers/all`);
}

export function getAllEnabledDrivers(): Promise<AxiosResponse> {
    return axiosInstance.get(`/drivers/all/enabled`);
}

export function getDriverById(driverId: string): Promise<AxiosResponse> {
    return axiosInstance.get(`/drivers/${driverId}`);
}

export function getDriversStats(): Promise<AxiosResponse> {
    return axiosInstance.get(`/drivers/stats`);
}

export function saveDriver(driver: DriverDto): Promise<AxiosResponse> {
    return axiosInstance.post(`/drivers`, driver);
}

export function updateDriver(driverId: string, driver: DriverDto): Promise<AxiosResponse> {
    return axiosInstance.patch(`/drivers/${driverId}`, driver);
}

export function disableDriver(driverId: string): Promise<AxiosResponse> {
    return axiosInstance.patch(`/drivers/${driverId}/disable`);
}

export function enableDriver(driverId: string): Promise<AxiosResponse> {
    return axiosInstance.patch(`/drivers/${driverId}/enable`);
}