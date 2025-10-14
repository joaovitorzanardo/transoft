import type DateIntervalDto from "../DateIntervalDto";

export default interface GenerateItineraryDto {
    routeId: string;
    dateInterval: DateIntervalDto;
}