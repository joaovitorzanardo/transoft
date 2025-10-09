import { axiosInstance } from "./axios-instance";
import type { AxiosResponse } from "axios";

export function getAutomakers(): Promise<AxiosResponse> {
    return axiosInstance.get('/automakers', {
        headers: {
            'Authorization': `Bearer ${sessionStorage.getItem("apiToken")}`
        }
    });
}