import { AxiosResponse } from "axios";
import PassengerAccountDto from "../model/PassengerAccountDto";
import { axiosInstance } from "./axios-instance";

export function getPassengerAccount(): Promise<AxiosResponse> {
    return axiosInstance.get(`/passengers/account`);
}

export function updatePassengerAccount(passengerAccount: PassengerAccountDto): Promise<AxiosResponse> {
    return axiosInstance.put(`/passengers/account`, passengerAccount);
}