import { axiosInstance } from './axios-instance';
import type { AxiosResponse } from 'axios';

export function getVehicleModelsByAutomaker(automakerId: string): Promise<AxiosResponse> {
    return axiosInstance.get(`/vehicle-models/automaker/${automakerId}`);
}