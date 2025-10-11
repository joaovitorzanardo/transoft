import type AddressDto from "./address/AddressDto";
import type PhoneNumberDto from "./PhoneNumberDto";
import type SchoolPresenter from "./SchoolPresenter";

export default interface PassengerPresenter {
    passengerId: string;
    name: string;
    email: string;
    phoneNumber: PhoneNumberDto;
    school: SchoolPresenter;
    address: AddressDto;
}