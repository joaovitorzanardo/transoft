import type { AxiosResponse } from "axios";
import { axiosInstance } from "./axios-instance";
import type CompanyDto from "../models/CompanyDto";

export function getCompany(): Promise<AxiosResponse> {
    return axiosInstance.get(`/companies`, {
        headers: {
            'Authorization': `Bearer ${sessionStorage.getItem("apiToken")}`
        }
    });
}

export function updateCompany(companyDto: CompanyDto): Promise<AxiosResponse> {
    return axiosInstance.patch(`/companies`, companyDto, {
        headers: {
            'Authorization': `Bearer ${sessionStorage.getItem("apiToken")}`
        }
    });
}