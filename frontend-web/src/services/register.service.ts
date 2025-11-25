import { axiosInstance } from './axios-instance';
import type { AxiosResponse } from 'axios';
import type CompanyRegistrationDto from '../models/CompanyRegistrationDto';

export function register(companyRegistrationDto: CompanyRegistrationDto): Promise<AxiosResponse> {
    return axiosInstance.post('/register', companyRegistrationDto);
}