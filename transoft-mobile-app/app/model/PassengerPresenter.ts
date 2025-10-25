import AddressDto from "./AddressDto";
import PhoneNumberDto from "./PhoneNumberDto";

export default interface PassengerPresenter {
    passengerId: string;
    name: string;
    email: string;
    phoneNumber: PhoneNumberDto;
    routeName: string;
    address: AddressDto;
    active: boolean;
    enabled: boolean;
}