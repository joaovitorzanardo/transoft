import type DateIntervalDto from "./DateIntervalDto";

export default interface ItineraryDto {
    routeId: string;
    dateInterval: DateIntervalDto;
}