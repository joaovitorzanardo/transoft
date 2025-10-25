import { AxiosResponse } from "axios";
import { axiosInstance } from "./axios-instance";

export function getItineraries(page: number, size: number): Promise<AxiosResponse> {
    return axiosInstance.get(`/itineraries/account?page=${page}&size=${size}`);
}

export function getItinerariesHistory(page: number, size: number): Promise<AxiosResponse> {
    return axiosInstance.get(`/itineraries/history/account?page=${page}&size=${size}`);
}

export function getItineraryById(itineraryId: string): Promise<AxiosResponse> {
    return axiosInstance.get(`/itineraries/${itineraryId}`);
}

export function getPassengersFromItinerary(itineraryId: string): Promise<AxiosResponse> {
    return axiosInstance.get(`/itineraries/${itineraryId}/passengers`);
}

export function getNextItinerary(): Promise<AxiosResponse> {
    return axiosInstance.get(`/itineraries/next`);
}