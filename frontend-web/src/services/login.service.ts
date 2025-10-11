import { axiosInstance } from './axios-instance';
import type { AxiosResponse } from 'axios';
import type LoginDto from '../models/auth/LoginDto';

export function login(loginDto: LoginDto): Promise<AxiosResponse> {
    return axiosInstance.post('/login', loginDto);
}