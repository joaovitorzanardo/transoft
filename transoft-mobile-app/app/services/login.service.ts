import type { AxiosResponse } from 'axios';
import LoginDto from '../model/LoginDto';
import { axiosInstance } from './axios-instance';

export function login(loginDto: LoginDto): Promise<AxiosResponse> {
    return axiosInstance.post('/login', loginDto);
}

