import DriverPresenter from "./DriverPresenter";
import RoutePresenter from "./RoutePresenter";
import VehiclePresenter from "./VehiclePresenter";

export default interface ItineraryPresenter {
    itineraryId: string;
    type: 'IDA' | 'VOLTA';
    status: 'AGENDADO' | 'EM_ANDAMENTO' | 'CONCLUIDO' | 'CANCELADO';
    date: string
    startTime: string;
    endTime: string;
    route: RoutePresenter;
    driver: DriverPresenter;
    vehicle: VehiclePresenter;
}