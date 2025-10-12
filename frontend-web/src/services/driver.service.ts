import type { AxiosResponse } from "axios";
import { axiosInstance } from "./axios-instance";
import type DriverDto from "../models/driver/DriverDto";

export function getDrivers(page: number, size: number): Promise<AxiosResponse> {
    return axiosInstance.get(`/drivers?page=${page}&size=${size}`, {
        headers: {
            'Authorization': `Bearer ${sessionStorage.getItem("apiToken")}`
        }
    });
}

export function getAllDrivers(): Promise<AxiosResponse> {
    return axiosInstance.get(`/drivers/all`, {
        headers: {
            'Authorization': `Bearer ${sessionStorage.getItem("apiToken")}`
        }
    });
}

export function getDriverById(driverId: string): Promise<AxiosResponse> {
    return axiosInstance.get(`/drivers/${driverId}`, {
        headers: {
            'Authorization': `Bearer ${sessionStorage.getItem("apiToken")}`
        }
    });
}

export function getDriversStats(): Promise<AxiosResponse> {
    return axiosInstance.get(`/drivers/stats`, {
        headers: {
            'Authorization': `Bearer ${sessionStorage.getItem("apiToken")}`
        }
    });
}

export function saveDriver(driver: DriverDto): Promise<AxiosResponse> {
    return axiosInstance.post(`/drivers`, driver ,{
        headers: {
            'Authorization': `Bearer ${sessionStorage.getItem("apiToken")}`
        }
    });
}