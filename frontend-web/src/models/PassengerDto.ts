import type AddressDto from "./address/AddressDto";
import type PhoneNumberDto from "./PhoneNumberDto";

export default interface PassengerDto {
    name: string;
    email: string;
    phoneNumber: PhoneNumberDto;
    routeId: string;
    address: AddressDto;
}