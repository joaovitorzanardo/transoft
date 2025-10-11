import { Button, Stack } from "@mui/material";
import SideMenu from "../../components/SideManu";
import { StatsCard } from "../../components/StatsCard";
import AddIcon from '@mui/icons-material/Add';
import PassengersTable from "../../components/passengers/PassengersTable";
import PageTitle from "../../components/ui/PageTitle";
import { useNavigate } from "react-router";
import React from "react";
import { getPassengersStats } from "../../services/passenger.service";

interface StatsPassengers {
    total: number;
    active: number;
    inactive: number;
    pending: 0;
}

export default function PassengersPage() {
    const navigate = useNavigate();

    const [stats, setStats] = React.useState<StatsPassengers>({total: 0, active: 0, inactive: 0, pending: 0});
    const [loading, setLoading] = React.useState<boolean>(false);

    const navigateToPassengerInfo = () => {
        navigate('/passengers/edit');
    }

    React.useEffect(() => {
        async function getStats() {
            setLoading(true);
            const response = await getPassengersStats();
            setLoading(false);

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
                    <PageTitle title="Passageiros" description="Gerencie os passageiros da empresa" />
                    <Stack direction="row" spacing={2}>
                        <Button variant="outlined" color="primary" startIcon={<AddIcon/>} onClick={navigateToPassengerInfo}>Adicionar Passageiro</Button>
                    </Stack>
                </Stack>
                <Stack direction="row" spacing={5} sx={{ marginBottom: 5, marginTop: 5 }}>
                    <StatsCard title="Total" value={stats.total} loading={loading}/>
                    <StatsCard title="Ativos" value={stats.active} loading={loading}/>
                    <StatsCard title="Inativos" value={stats.inactive} loading={loading}/>
                    <StatsCard title="Pendentes" value={stats.pending} loading={loading}/>
                </Stack>
                <PassengersTable />
            </Stack>
        </Stack>
    );
}