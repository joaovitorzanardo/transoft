import type AddressDto from "./address/AddressDto";

export default interface SchoolPresenter {
    schoolId: string;
    name: string;
    address: AddressDto;
}