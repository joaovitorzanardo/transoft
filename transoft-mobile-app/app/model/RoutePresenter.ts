import AddressDto from "./AddressDto";
import DriverPresenter from "./DriverPresenter";
import VehiclePresenter from "./VehiclePresenter";

export default interface RoutePresenter {
    routeId: string;
    name: string;
    active: boolean;
    school: SchoolPresenter;
    defaultDriver: DriverPresenter;
    defaultVehicle: VehiclePresenter;
}

interface SchoolPresenter {
    schoolId: string;
    name: string;
    address: AddressDto;
}



