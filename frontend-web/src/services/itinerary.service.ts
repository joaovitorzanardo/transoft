import type { AxiosResponse } from "axios";
import { axiosInstance } from "./axios-instance";
import type GenerateItineraryDto from "../models/itinerary/GenerateItineraryDto";
import type ItineraryDto from "../models/itinerary/ItineraryDto";
import type ItineraryFilters from "../models/itinerary/ItineraryFilters";

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

export function getItinerariesFilters(filter: ItineraryFilters): Promise<AxiosResponse> {
    return axiosInstance.post(`/itineraries/filter`, filter, {
        headers: {
            'Authorization': `Bearer ${sessionStorage.getItem("apiToken")}`
        }
    });
}

export function updateItinerary(itineraryId: string, itineraryDto: ItineraryDto): Promise<AxiosResponse> {
    return axiosInstance.patch(`/itineraries/${itineraryId}`, itineraryDto, {
        headers: {
            'Authorization': `Bearer ${sessionStorage.getItem("apiToken")}`
        }
    });
}

export function cancelItinerary(itineraryId: string): Promise<AxiosResponse> {
    return axiosInstance.patch(`/itineraries/${itineraryId}/cancel`, {}, {
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

export function generateItineraries(itineraryDto: GenerateItineraryDto): Promise<AxiosResponse> {
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
