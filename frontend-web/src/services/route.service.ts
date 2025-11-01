import type { AxiosResponse } from "axios";
import { axiosInstance } from "./axios-instance";
import type RouteDto from "../models/route/RouteDto";
import type RouteUpdateDto from "../models/route/RouteUpdateDto";

export function getAllRoutes(): Promise<AxiosResponse> {
    return axiosInstance.get(`/routes/all`);
}

export function getAllActiveRoutes(): Promise<AxiosResponse> {
    return axiosInstance.get(`/routes/all/active`);
}

export function getRoutes(page: number, size: number): Promise<AxiosResponse> {
    return axiosInstance.get(`/routes?page=${page}&size=${size}`);
}

export function getRouteById(routeId: string): Promise<AxiosResponse> {
    return axiosInstance.get(`/routes/${routeId}`);
}

export function saveRoute(routeDto: RouteDto): Promise<AxiosResponse> {
    return axiosInstance.post(`/routes`, routeDto);
}

export function updateRoute(routeId:string, routeUpdateDto: RouteUpdateDto): Promise<AxiosResponse> {
    return axiosInstance.patch(`/routes/${routeId}`, routeUpdateDto);
}

export function getRoutesStats(): Promise<AxiosResponse> {
    return axiosInstance.get(`/routes/stats`);
}

export function enableRoute(routeId: string): Promise<AxiosResponse> {
    return axiosInstance.patch(`/routes/${routeId}/enable`);
}

export function disableRoute(routeId: string): Promise<AxiosResponse> {
    return axiosInstance.patch(`/routes/${routeId}/disable`);
}