export default interface VehiclePresenter {
    vehicleId: string;
    plateNumber: string;
    capacity: number;
    isActive: boolean;
    vehicleModel: VehicleModelPresenter;
}

interface VehicleModelPresenter {
    vehicleModelId: string;
    modelName: string;
    modelYear: number;
    automaker: AutomakerPresenter;
}

interface AutomakerPresenter {
    automakerId: string;
    name: string;
}