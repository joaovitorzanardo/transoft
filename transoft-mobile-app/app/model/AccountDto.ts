import PhoneNumberDto from "./PhoneNumberDto";

export default interface AccountDto {
    name: string;
    email:string;
    phoneNumber: PhoneNumberDto;
}