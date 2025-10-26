import { AxiosResponse } from "axios";
import AccountDto from "../model/AccountDto";
import { axiosInstance } from "./axios-instance";

export function getPassengerAccount(): Promise<AxiosResponse> {
    return axiosInstance.get(`/passengers/account`);
}

export function updatePassengerAccount(passengerAccount: AccountDto): Promise<AxiosResponse> {
    return axiosInstance.put(`/passengers/account`, passengerAccount);
}