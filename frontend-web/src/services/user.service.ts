import { axiosInstance } from './axios-instance';
import type { AxiosResponse } from 'axios';

export function getLoggedUser(token: string): Promise<AxiosResponse> {
    const config = {
        headers: {Authorization: `Bearer ${token}`}
    }
    return axiosInstance.get('/account', config);
}