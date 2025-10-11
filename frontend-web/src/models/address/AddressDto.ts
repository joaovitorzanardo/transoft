import type CoordinateDto from "./CoordinateDto";

export default interface AddressDto {
    cep: string;
    street: string;
    district: string;
    number: number;
    city: string;
    uf: string;
    complement?: string;
    coordinate?: CoordinateDto;
}