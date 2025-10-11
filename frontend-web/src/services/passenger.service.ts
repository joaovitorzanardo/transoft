import type { AxiosResponse } from "axios";
import { axiosInstance } from "./axios-instance";

export function getPassengersStats(): Promise<AxiosResponse> {
    return axiosInstance.get(`/passengers/stats`, {
        headers: {
            'Authorization': `Bearer ${sessionStorage.getItem("apiToken")}`
        }
    });
}

export function getPassenges(): Promise<AxiosResponse> {
    return axiosInstance.get(`/passengers`, {
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