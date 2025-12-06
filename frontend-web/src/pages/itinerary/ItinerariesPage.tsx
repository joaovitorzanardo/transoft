/* eslint-disable @typescript-eslint/no-explicit-any */
import { Box, Button, Checkbox, Divider, FormControl, InputLabel, ListItemText, MenuItem, OutlinedInput, Paper, Select, Stack, Typography, type SelectChangeEvent } from "@mui/material";
import SideMenu from "../../components/SideManu";
import PageTitle from "../../components/ui/PageTitle";
import AddIcon from '@mui/icons-material/Add';
import { StatsCard } from "../../components/StatsCard";
import ItineraryTable from "../../components/itineraries/ItineraryTable";
import { useNavigate } from "react-router";
import BoltIcon from '@mui/icons-material/Bolt';
import React from "react";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import { DatePicker } from "@mui/x-date-pickers/DatePicker";
import { getItinerariesStats } from "../../services/itinerary.service";
import type ItineraryFilters from "../../models/itinerary/ItineraryFilters";
import { getAllRoutes } from "../../services/route.service";
import { getAllVehicles } from "../../services/vehicle.service";
import { getAllDrivers } from "../../services/driver.service";
import type DriverPresenter from "../../models/driver/DriverPresenter";
import type VehiclePresenter from "../../models/vehicle/VehiclePresenter";
import type RouteSelectPresenter from "../../models/route/RouteSelectPresenter";

const status = ["Agendado", "Andamento", "Concluido", "Cancelado"]
const tipos = ["Ida", "Volta"]

const ITEM_HEIGHT = 48;
const ITEM_PADDING_TOP = 8;

const MenuProps = {
    PaperProps: {
        style: {
            maxHeight: ITEM_HEIGHT * 4.5 + ITEM_PADDING_TOP,
            width: 250,
        },
    },
};

interface StatsItineraries {
    total: number;
    scheduled: number;
    finished: number;
    canceled: number;
    missed?: number;
}

export default function ItinerariesPage() {
    const [filters, setFilters] = React.useState<ItineraryFilters>({
        status: [],
        type: [],
        date: null,
        routeId: null,
        driverId: null,
        vehicleId: null
    });

    const [selectedStatus, setSelectedStatus] = React.useState<string[]>([]);
    const [selectedType, setSelectedType] = React.useState<string[]>([]);
    const [selectedRoute, setSelectedRoute] = React.useState<string>('');
    const [selectedDriver, setSelectedDriver] = React.useState<string>('');
    const [selectedVehicle, setSelectedVehicle] = React.useState<string>('');
    const [selectedDate, setSelectedDate] = React.useState<any>(null);

    const [routes, setRoutes] = React.useState<RouteSelectPresenter[]>([]);
    const [drivers, setDrivers] = React.useState<DriverPresenter[]>([]);
    const [vehicles, setVehicles] = React.useState<VehiclePresenter[]>([]);

    const [refreshKey, setRefreshKey] = React.useState(0);

    const handleChangeStatus = (event: SelectChangeEvent<typeof selectedStatus>) => {
        const {
            target: { value },
        } = event;
        setSelectedStatus(
            typeof value === 'string' ? value.split(',') : value,
        );
    };

    const handleChangeType = (event: SelectChangeEvent<typeof selectedType>) => {
        const {
            target: { value },
        } = event;
        setSelectedType(
            typeof value === 'string' ? value.split(',') : value,
        );
    };

    const handleDateChange = (value: any) => {
        setSelectedDate(value);
    };

    const handleChangeRoute = (event: SelectChangeEvent) => {
        setSelectedRoute(event.target.value);
    }

    const handleChangeDriver = (event: SelectChangeEvent) => {
        setSelectedDriver(event.target.value);
    }

    const handleChangeVehicle = (event: SelectChangeEvent) => {
        setSelectedVehicle(event.target.value);
    }

    const navigate = useNavigate();

    const navigateToGenerateItineraryPage = () => {
        navigate('/itineraries/generate');
    };

    const [stats, setStats] = React.useState<StatsItineraries>({ total: 0, scheduled: 0, finished: 0, canceled: 0 });
    const [loading, setLoading] = React.useState<boolean>(false);

    React.useEffect(() => {
        async function getStats() {
            setLoading(true);
            const response = await getItinerariesStats();
            setLoading(false);

            if (response.status !== 200) {
                return;
            }

            setStats(response.data);
        }

        async function getFilterData() {
            const responseRoutes = await getAllRoutes();
            const responseVehicles = await getAllVehicles();
            const responseDrivers = await getAllDrivers();

            if (responseRoutes.status === 200) {
                setRoutes(responseRoutes.data);
            }

            if (responseVehicles.status === 200) {
                setVehicles(responseVehicles.data);
            }

            if (responseDrivers.status === 200) {
                setDrivers(responseDrivers.data);
            }
        }
        getStats();
        getFilterData();
    }, [])

    const applyFilters = () => {
        setFilters({
            status: selectedStatus,
            type: selectedType,
            date: selectedDate,
            routeId: selectedRoute === "" ? null : selectedRoute,
            driverId: selectedDriver === "" ? null : selectedDriver,
            vehicleId: selectedVehicle === "" ? null : selectedVehicle
        });
    }

    const clearFilters = () => {
        setSelectedDate(null);
        setSelectedRoute('');
        setSelectedDriver('');
        setSelectedVehicle('');
        setSelectedStatus([]);
        setSelectedType([]);
        setFilters({
            status: [],
            type: [],
            date: null,
            routeId: null,
            driverId: null,
            vehicleId: null
        });
        setRefreshKey(oldKey => oldKey + 1);
    }

    return (
        <Stack direction="row" sx={{ backgroundColor: '#F7F9FA' }}>
            <SideMenu />
            <Stack sx={{ paddingLeft: 5, paddingTop: 5 }}>
                <Stack direction="row" sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }} >
                    <PageTitle title="Itinerários" description="Gerencie os itinerários da frota" />
                    <Stack direction="row" spacing={2}>
                        <Button variant="outlined" color="primary" startIcon={<AddIcon />} onClick={navigateToGenerateItineraryPage}>Gerar Itinerário</Button>
                    </Stack>
                </Stack>
                <Stack direction="row" spacing={5} sx={{ marginBottom: 5, marginTop: 5 }}>
                    <StatsCard title="Total" value={stats.total} loading={loading} />
                    <StatsCard title="Agendados" value={stats.scheduled} loading={loading} />
                    <StatsCard title="Concluídos" value={stats.finished} loading={loading} />
                    <StatsCard title="Cancelados" value={stats.canceled} loading={loading} />
                    <StatsCard title="Perdidos" value={stats.missed} loading={loading} />
                </Stack>
                <Paper sx={{ padding: 2, marginBottom: 5 }}>
                    <Stack direction="row" spacing={1}>
                        <Typography variant="subtitle1" >Filtros Rápidos</Typography>
                        <BoltIcon />
                    </Stack>
                    <Box sx={{ height: '5px' }} />
                    <Divider />
                    <Box sx={{ height: '15px' }} />
                    <Stack>
                        <Stack direction="row" spacing={2} justifyContent="space-evenly">
                            <FormControl sx={{ flex: 1 }}>
                                <InputLabel id="status-label" size="small">Status</InputLabel>
                                <Select
                                    labelId="status-label"
                                    size="small"
                                    multiple
                                    value={selectedStatus}
                                    onChange={handleChangeStatus}
                                    input={<OutlinedInput label="Tag" />}
                                    renderValue={(selected) => selected.join(', ')}
                                    MenuProps={MenuProps}
                                >
                                    {status.map((stat) => (
                                        <MenuItem key={stat} value={stat}>
                                            <Checkbox checked={selectedStatus.includes(stat)} />
                                            <ListItemText primary={stat} />
                                        </MenuItem>
                                    ))}
                                </Select>
                            </FormControl>
                            <FormControl sx={{ flex: 1 }}>
                                <InputLabel id="tipo-label" size="small">Tipo</InputLabel>
                                <Select
                                    labelId="tipo-label"
                                    multiple
                                    size="small"
                                    value={selectedType}
                                    onChange={handleChangeType}
                                    input={<OutlinedInput label="Tag" />}
                                    renderValue={(selected) => selected.join(', ')}
                                    MenuProps={MenuProps}
                                >
                                    {tipos.map((tipo) => (
                                        <MenuItem key={tipo} value={tipo}>
                                            <Checkbox size="small" checked={selectedType.includes(tipo)} />
                                            <ListItemText primary={tipo} />
                                        </MenuItem>
                                    ))}
                                </Select>
                            </FormControl>
                            <LocalizationProvider dateAdapter={AdapterDayjs}>
                                <DatePicker
                                    key={refreshKey}
                                    label="Data"
                                    sx={{ flex: 1 }}
                                    onChange={handleDateChange}
                                    format="DD/MM/YYYY"
                                    value={selectedDate}
                                    slotProps={{
                                        textField: {
                                            size: 'small'
                                        },
                                    }}
                                />
                            </LocalizationProvider>
                        </Stack>
                        <Stack direction="row" spacing={2} justifyContent="space-evenly" style={{ marginTop: '15px' }}>
                            <FormControl sx={{ flex: 1 }}>
                                <InputLabel id="status-label" size="small">Rota</InputLabel>
                                <Select
                                    sx={{ width: '100%' }}
                                    label="Rota"
                                    size="small"
                                    onChange={handleChangeRoute}
                                    value={selectedRoute}
                                >
                                    <MenuItem value="">Nenhum</MenuItem>
                                    {routes.map((route) => (
                                        <MenuItem value={route.routeId}>
                                            {route.name}
                                        </MenuItem>
                                    ))}
                                </Select>
                            </FormControl>
                            <FormControl sx={{ flex: 1 }}>
                                <InputLabel id="tipo-label" size="small">Motorista</InputLabel>
                                <Select
                                    sx={{ width: '100%' }}
                                    label="Motorista"
                                    size="small"
                                    onChange={handleChangeDriver}
                                    value={selectedDriver}
                                >
                                    <MenuItem value="">Nenhum</MenuItem>
                                    {drivers.map((driver) => (
                                        <MenuItem value={driver.driverId}>
                                            {driver.name}
                                        </MenuItem>
                                    ))}
                                </Select>
                            </FormControl>
                            <FormControl sx={{ flex: 1 }}>
                                <InputLabel id="tipo-label" size="small">Veículo</InputLabel>
                                <Select
                                    sx={{ width: '100%' }}
                                    label="Veículo"
                                    size="small"
                                    onChange={handleChangeVehicle}
                                    value={selectedVehicle}
                                >
                                    <MenuItem value="">Nenhum</MenuItem>
                                    {vehicles.map((vehicle) => (
                                        <MenuItem value={vehicle.vehicleId}>
                                            {vehicle.vehicleModel.automaker.name} - {vehicle.vehicleModel.modelName} - {vehicle.plateNumber}
                                        </MenuItem>
                                    ))}
                                </Select>
                            </FormControl>
                        </Stack>
                        <Stack direction="row" spacing={2} justifyContent="flex-end" sx={{ marginTop: 2 }}>
                            <Button variant="outlined" size="small" color="info" onClick={clearFilters}>Limpar</Button>
                            <Button variant="contained" size="small" color="primary" onClick={applyFilters}>Aplicar</Button>
                        </Stack>
                    </Stack>
                </Paper>
                <ItineraryTable filters={filters} />
            </Stack>
        </Stack>
    );
}