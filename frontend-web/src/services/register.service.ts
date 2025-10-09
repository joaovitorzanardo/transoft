import { axiosInstance } from './axios-instance';
import type RegisterDto from "../models/RegisterDto";
import type { AxiosResponse } from 'axios';

export function register(registerDto: RegisterDto): Promise<AxiosResponse> {
    return axiosInstance.post('/register', registerDto);
}