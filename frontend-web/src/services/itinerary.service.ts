import type { AxiosResponse } from "axios";
import { axiosInstance } from "./axios-instance";
import type ItineraryDto from "../models/ItineraryDto";

export function getItinerariesStats(): Promise<AxiosResponse> {
    return axiosInstance.get(`/itineraries/stats`, {
        headers: {
            'Authorization': `Bearer ${sessionStorage.getItem("apiToken")}`
        }
    });
}

export function getItineraries(page: number, size: number): Promise<AxiosResponse> {
    return axiosInstance.get(`/itineraries?page=${page}&size=${size}`, {
        headers: {
            'Authorization': `Bearer ${sessionStorage.getItem("apiToken")}`
        }
    });
}

export function getItineraryById(itineraryId: string): Promise<AxiosResponse> {
    return axiosInstance.get(`/itineraries/${itineraryId}`, {
        headers: {
            'Authorization': `Bearer ${sessionStorage.getItem("apiToken")}`
        }
    });
}

export function generateItineraries(itineraryDto: ItineraryDto): Promise<AxiosResponse> {
    return axiosInstance.post(`/itineraries`, itineraryDto, {
        headers: {
            'Authorization': `Bearer ${sessionStorage.getItem("apiToken")}`
        }
    });
}

export function getPassengerFromItinerary(itineraryId: string): Promise<AxiosResponse> {
    return axiosInstance.get(`/itineraries/${itineraryId}/passengers`, {
        headers: {
            'Authorization': `Bearer ${sessionStorage.getItem("apiToken")}`
        }
    });
}
