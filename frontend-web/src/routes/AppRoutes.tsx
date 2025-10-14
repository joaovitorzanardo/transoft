import { BrowserRouter, Route, Routes } from "react-router";
import RoutesPage from "../pages/route/RoutesPage";
import DriversPage from "../pages/driver/DriversPage";
import ItinerariesPage from "../pages/itinerary/ItinerariesPage";
import ConfigurationPage from "../pages/ConfigurationPage";
import DashboardPage from "../pages/DashboardPage";
import ReportsPage from "../pages/ReportsPage";
import VehiclesPage from "../pages/vehicle/VehiclesPage";
import { LoginPage } from "../pages/LoginPage";
import RegisterPage from "../pages/register/RegisterPage";
import ProtectedRoute from "../auth/ProtectedRoute";
import PassengersPage from "../pages/passenger/PassengersPage";
import PassengerInfoPage from "../pages/passenger/PassengerInfoPage";
import DriverInfoPage from "../pages/driver/DriverInfoPage";
import VehicleInfoPage from "../pages/vehicle/VehicleInfoPage";
import GenerateItineraryPage from "../pages/itinerary/GenerateItineraryPage";
import RouteInfoPage from "../pages/route/RouteInfoPage";
import ItineraryInfoPage from "../pages/itinerary/ItineraryInfoPage";

export default function AppRoutes() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/login" element={<LoginPage />} />
                <Route path="/register" element={<RegisterPage />} />
                <Route element={<ProtectedRoute allowedRoles="MANAGER"/>}>
                    <Route path="/passengers" element={<PassengersPage />} />
                    <Route path="/passengers/:passengerId" element={<PassengerInfoPage />} />
                    <Route path="/routes" element={<RoutesPage />} />
                    <Route path="/routes/:routeId" element={<RouteInfoPage />} />
                    <Route path="/vehicles" element={<VehiclesPage />} />
                    <Route path="/vehicles/:vehicleId" element={<VehicleInfoPage />} />
                    <Route path="/drivers" element={<DriversPage />} />
                    <Route path="/drivers/:driverId" element={<DriverInfoPage />} />
                    <Route path="/itineraries" element={<ItinerariesPage />} />
                    <Route path="/itineraries/generate" element={<GenerateItineraryPage />} />
                    <Route path="/itineraries/:itineraryId" element={<ItineraryInfoPage />} />
                    <Route path="/configuration" element={<ConfigurationPage />} />
                    {/* <Route path="/dashboard" element={<DashboardPage />} />
                    <Route path="/reports" element={<ReportsPage />} /> */}
                </Route>
            </Routes>
        </BrowserRouter>
    );
}