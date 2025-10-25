import { AxiosResponse } from "axios";
import ChangePasswordDto from "../model/ChangePasswordDto";
import { axiosInstance } from "./axios-instance";

export function getUserAccount(): Promise<AxiosResponse> {
    return axiosInstance.get('/account');
}

export function changePassword(changePasswordDto: ChangePasswordDto): Promise<AxiosResponse> {
    return axiosInstance.patch('/account/password', changePasswordDto);
}
