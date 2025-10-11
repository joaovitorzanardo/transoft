import type PhoneNumberDto from "../PhoneNumberDto";

export default interface DriverDto {
    name: string;
    email: string;
    cnhNumber: string;
    cnhExpirationDate: Date
    phoneNumber: PhoneNumberDto
}