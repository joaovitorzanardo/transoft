import { AxiosResponse } from "axios";
import AccountDto from "../model/AccountDto";
import { axiosInstance } from "./axios-instance";

export function getDriverAccount(): Promise<AxiosResponse> {
    return axiosInstance.get(`/drivers/account`);
}

export function updateDriverAccount(driversAccount: AccountDto): Promise<AxiosResponse> {
    return axiosInstance.patch(`/drivers/account`, driversAccount);
}