import type { AxiosResponse } from "axios";
import { axiosInstance } from "./axios-instance";
import type RouteDto from "../models/route/RouteDto";

export function getAllRoutes(): Promise<AxiosResponse> {
    return axiosInstance.get(`/routes/all`, {
        headers: {
            'Authorization': `Bearer ${sessionStorage.getItem("apiToken")}`
        }
    });
}

export function getRoutes(page: number, size: number): Promise<AxiosResponse> {
    return axiosInstance.get(`/routes?page=${page}&size=${size}`, {
        headers: {
            'Authorization': `Bearer ${sessionStorage.getItem("apiToken")}`
        }
    });
}

export function getRouteById(routeId: string): Promise<AxiosResponse> {
    return axiosInstance.get(`/routes/${routeId}`, {
        headers: {
            'Authorization': `Bearer ${sessionStorage.getItem("apiToken")}`
        }
    });
}

export function saveRoute(routeDto: RouteDto): Promise<AxiosResponse> {
    return axiosInstance.post(`/routes`, routeDto, {
        headers: {
            'Authorization': `Bearer ${sessionStorage.getItem("apiToken")}`
        }
    });
}

export function getRoutesStats(): Promise<AxiosResponse> {
    return axiosInstance.get(`/routes/stats`, {
        headers: {
            'Authorization': `Bearer ${sessionStorage.getItem("apiToken")}`
        }
    });
}