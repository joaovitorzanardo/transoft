import type VehicleModelPresenter from "./VehicleModel";

export default interface VehiclePresenter {
    vehicleId: string;
    plateNumber: string;
    capacity: number;
    isActive: boolean;
    vehicleModel: VehicleModelPresenter;
}