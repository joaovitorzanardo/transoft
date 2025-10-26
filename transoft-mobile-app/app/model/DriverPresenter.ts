import PhoneNumberDto from "./PhoneNumberDto";

export default interface DriverPresenter {
    driverId: string;
    name: string;
    email: string;
    cnhNumber: string;
    cnhExpirationDate: string;
    phoneNumber: PhoneNumberDto;
    enabled: boolean;
    active: boolean;
}