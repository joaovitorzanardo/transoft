import { axiosInstance } from './axios-instance';
import type VehicleModel from '../types/VehicleModel';

export async function getVehicleModelsByAutomaker(automakerId: string): Promise<VehicleModel[]> {
    return axiosInstance.get(`/vehicle-model/automaker/${automakerId}`).then(response => {
        return response.data as VehicleModel[];
    }).catch(error => {
        console.error('Error fetching vehicle models:', error);
        throw error;
    });
}