export default interface ItineraryFilters {
    status: string[];
    type: string[];
    date: Date | null;
    vehicleId: string | null;
    driverId: string | null;
    routeId: string | null;
}