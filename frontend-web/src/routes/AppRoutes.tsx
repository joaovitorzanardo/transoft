import { BrowserRouter, Route, Routes } from "react-router";
import PassengersPage from "../pages/PassengersPage";
import RoutesPage from "../pages/RoutesPage";
import DriversPage from "../pages/DriversPage";
import ItinerariesPage from "../pages/ItinerariesPage";
import ConfigurationPage from "../pages/ConfigurationPage";
import DashboardPage from "../pages/DashboardPage";
import ReportsPage from "../pages/ReportsPage";

export default function AppRoutes() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/passengers" element={<PassengersPage />} />
                <Route path="/routes" element={<RoutesPage />} />
                <Route path="/drivers" element={<DriversPage />} />
                <Route path="/itineraries" element={<ItinerariesPage />} />
                <Route path="/configuration" element={<ConfigurationPage />} />
                <Route path="/dashboard" element={<DashboardPage />} />
                <Route path="/reports" element={<ReportsPage />} />
            </Routes>
        </BrowserRouter>
    );
}