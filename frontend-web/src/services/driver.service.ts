import type { AxiosResponse } from "axios";
import { axiosInstance } from "./axios-instance";

export function getDrivers(page: number, size: number): Promise<AxiosResponse> {
    return axiosInstance.get(`/drivers?page=${page}&size=${size}`, {
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