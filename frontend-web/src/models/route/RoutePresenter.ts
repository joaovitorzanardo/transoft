import type DriverPresenter from "../driver/DriverPresenter";
import type SchoolPresenter from "../SchoolPresenter";
import type VehiclePresenter from "../vehicle/VehiclePresenter";

export default interface RoutePresenter {
    routeId: string;
    name: string;
    active: boolean;
    school: SchoolPresenter;
    defaultDriver: DriverPresenter;
    defaultVehicle: VehiclePresenter;
}