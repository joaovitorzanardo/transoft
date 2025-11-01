import type VehicleDto from '../models/vehicle/VehicleDto';
import { axiosInstance } from './axios-instance';
import type { AxiosResponse } from 'axios';

export function saveVehicle(VehicleDto: VehicleDto): Promise<AxiosResponse> {
    return axiosInstance.post('/vehicles', VehicleDto);
}

export function updateVehicle(vehicleId: string, vehicleDto: VehicleDto): Promise<AxiosResponse> {
    return axiosInstance.patch(`/vehicles/${vehicleId}`, vehicleDto);
}

export function getVehicles(page: number, size: number): Promise<AxiosResponse> {
    return axiosInstance.get(`/vehicles?page=${page}&size=${size}`);
}

export function getAllVehicles(): Promise<AxiosResponse> {
    return axiosInstance.get(`/vehicles/all`);
}

export function getAllActiveVehicles(): Promise<AxiosResponse> {
    return axiosInstance.get(`/vehicles/all/active`);
}

export function disableVehicle(vehicleId: string): Promise<AxiosResponse> {
    return axiosInstance.delete(`/vehicles/${vehicleId}/disable`);
}

export function enableVehicle(vehicleId: string): Promise<AxiosResponse> {
    return axiosInstance.patch(`/vehicles/${vehicleId}/enable`, {});
}

export function getVehicleById(vehicleId: string): Promise<AxiosResponse> {
    return axiosInstance.get(`/vehicles/${vehicleId}`);
}

export function getVehiclesStats(): Promise<AxiosResponse> {
    return axiosInstance.get(`/vehicles/stats`);
}