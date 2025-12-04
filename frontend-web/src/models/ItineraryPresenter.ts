import type DriverPresenter from "./driver/DriverPresenter";
import type RoutePresenter from "./route/RoutePresenter";
import type VehiclePresenter from "./vehicle/VehiclePresenter";

export default interface ItineraryPresenter {
    itineraryId: string;
    type: 'IDA' | 'VOLTA';
    status: 'AGENDADO' | 'EM_ANDAMENTO' | 'CONCLUIDO' | 'CANCELADO' | 'PERDIDO';
    date: Date;
    startTime: string;
    endTime: string;
    route: RoutePresenter;
    driver: DriverPresenter;
    vehicle: VehiclePresenter;
}