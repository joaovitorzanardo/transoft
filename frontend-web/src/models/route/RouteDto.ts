import type DaysOfWeekDto from "./DaysOfWeekDto";
import type DepartureTripDto from "./DepartureTripDto";
import type ReturnTripDto from "./ReturnTripDto";

export default interface RouteDto {
    name: string;
    schoolId: string;
    defaultDriverId: string;
    defaultVehicleId: string;
    departureTrip: DepartureTripDto;
    returnTrip: ReturnTripDto;
    daysOfWeek: DaysOfWeekDto;
}