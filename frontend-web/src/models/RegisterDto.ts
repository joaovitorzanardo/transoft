import type CompanyDto from "./CompanyDto";

export default interface RegisterDto {
    name: string;
    email: string;
    password: string;
    company: CompanyDto;
}