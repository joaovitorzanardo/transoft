import type AutomakerPresenter from "./AutomakerPresenter";

export default interface VehicleModelPresenter {
    vehicleModelId: string;
    modelName: string;
    modelYear: number;
    automaker: AutomakerPresenter;
}