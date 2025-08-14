import { axiosInstance } from "./axios-instance";
import type Automaker from "../types/Automaker";

export async function getAutomakers(): Promise<Automaker[]> {
    return axiosInstance.get('/automaker').then(response => {
        console.log(response);
        return response.data as Automaker[];
    }).catch(error => {
        console.error('Error fetching automakers:', error);
        throw error;
    });
}