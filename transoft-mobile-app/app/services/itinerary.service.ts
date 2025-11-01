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

export function getOngoingItinerary(): Promise<AxiosResponse> {
    return axiosInstance.get(`/itineraries/ongoing`);
}

export function cancelItineraryForPassenger(itineraryId: string): Promise<AxiosResponse> {
    return axiosInstance.patch(`/itineraries/${itineraryId}/passenger/cancel`);
}

export function confirmItineraryForPassenger(itineraryId: string): Promise<AxiosResponse> {
    return axiosInstance.patch(`/itineraries/${itineraryId}/passenger/confirm`);
}

export function startItinerary(itineraryId: string): Promise<AxiosResponse> {
    return axiosInstance.patch(`/itineraries/${itineraryId}/start`);
}

export function finishItinerary(itineraryId: string): Promise<AxiosResponse> {
    return axiosInstance.patch(`/itineraries/${itineraryId}/finish`);
}