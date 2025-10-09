import type VehicleDto from '../models/VehicleDto';
import { axiosInstance } from './axios-instance';
import type { AxiosResponse } from 'axios';

export function saveVehicle(VehicleDto: VehicleDto): Promise<AxiosResponse> {
    return axiosInstance.post('/vehicles', VehicleDto, {
        headers: {
            'Authorization': `Bearer ${sessionStorage.getItem("apiToken")}`
        }
    });
}

export function getVehicles(): Promise<AxiosResponse> {
    return axiosInstance.get('/vehicles', {
        headers: {
            'Authorization': `Bearer ${sessionStorage.getItem("apiToken")}`
        }
    });
}

export function disableVehicle(vehicleId: string): Promise<AxiosResponse> {
    return axiosInstance.delete(`/vehicles/${vehicleId}/disable`, {
        headers: {
            'Authorization': `Bearer ${sessionStorage.getItem("apiToken")}`
        }
    });
}

export function enableVehicle(vehicleId: string): Promise<AxiosResponse> {
    return axiosInstance.patch(`/vehicles/${vehicleId}/enable`, {}, {
        headers: {
            'Authorization': `Bearer ${sessionStorage.getItem("apiToken")}`
        }
    });
}

export function getVehicleById(vehicleId: string): Promise<AxiosResponse> {
    return axiosInstance.get(`/vehicles/${vehicleId}`, {
        headers: {
            'Authorization': `Bearer ${sessionStorage.getItem("apiToken")}`
        }
    });
}

export function getVehiclesStats(): Promise<AxiosResponse> {
    return axiosInstance.get(`/vehicles/stats`, {
        headers: {
            'Authorization': `Bearer ${sessionStorage.getItem("apiToken")}`
        }
    });
}