import type { AxiosResponse } from "axios";
import axios from "axios";

const axiosInstance = axios.create({
    baseURL: 'https://brasilapi.com.br/api/cep/v1',
});

export function getAddressInfo(cep: string): Promise<AxiosResponse> {
    return axiosInstance.get(`/${cep}`);
}