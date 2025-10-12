import type { AxiosResponse } from "axios";
import { axiosInstance } from "./axios-instance";

export function getAllSchools(): Promise<AxiosResponse> {
    return axiosInstance.get(`/schools/all`, {
        headers: {
            'Authorization': `Bearer ${sessionStorage.getItem("apiToken")}`
        }
    });
}