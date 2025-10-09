import { Button, Stack } from "@mui/material";
import SideMenu from "../../components/SideManu";
import { VehicleTable } from "../../components/vehicles/VehicleTable";
import { StatsCard } from "../../components/StatsCard";
import AddIcon from '@mui/icons-material/Add';
import PageTitle from "../../components/ui/PageTitle";
import { SearchInput } from "../../components/ui/SearchInput";
import { useNavigate } from "react-router";
import React from "react";
import { getVehiclesStats } from "../../services/vehicle.service";

interface StatsVehicles {
    total: number;
    active: number;
    inactive: number;
}

export default function VehiclesPage() {
    const [stats, setStats] = React.useState<StatsVehicles>({total: 0, active: 0, inactive: 0});

    const navigate = useNavigate();

    const navigateToVehicleInfo = () => {
        navigate('/vehicles/edit');
    };

    React.useEffect(() => {
        async function getStats() {
            const response = await getVehiclesStats();

            if (response.status !== 200) {
                return;
            }

            setStats(response.data);
        }

        getStats();
    }, [])

    return (
        <Stack direction="row" sx={{ backgroundColor: '#F7F9FA'}}>
            <SideMenu />
            <Stack sx={{ paddingLeft: 5, paddingTop: 5 }}>
                <Stack direction="row" sx={{display: 'flex', justifyContent: 'space-between', alignItems: 'center'}} >
                    <PageTitle title="Veículos" description="Gerencie os veículos da frota" />
                    <Stack direction="row" spacing={2}>
                        <SearchInput title="Buscar veículos..."/>
                        <Button variant="outlined" color="primary" startIcon={<AddIcon/>} onClick={navigateToVehicleInfo}>Adicionar Veículo</Button>
                    </Stack>
                </Stack>
                <Stack direction="row" spacing={5} sx={{ marginBottom: 5, marginTop: 5 }}>
                    <StatsCard title="Total" value={stats.total}/>
                    <StatsCard title="Ativos" value={stats.active}/>
                    <StatsCard title="Inativos" value={stats.inactive}/>
                </Stack>
                <VehicleTable />
            </Stack>
        </Stack>
    );
}