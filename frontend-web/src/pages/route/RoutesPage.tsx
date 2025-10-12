import { Button, Stack } from "@mui/material";
import SideMenu from "../../components/SideManu";
import PageTitle from "../../components/ui/PageTitle";
import AddIcon from '@mui/icons-material/Add';
import { StatsCard } from "../../components/StatsCard";
import { useNavigate } from "react-router";
import RouteTable from "../../components/routes/RouteTable";
import React from "react";
import { getRoutesStats } from "../../services/route.service";

interface StatsRoutes {
    total: number;
    active: number;
    inactive: number;
}

export default function RoutesPage() {
    const navigate = useNavigate();

    const [stats, setStats] = React.useState<StatsRoutes>({total: 0, active: 0, inactive: 0});
    const [loading, setLoading] = React.useState<boolean>(false);

    const navigateToRouteEditPage = () => {
        navigate('/routes/edit');
    };

    React.useEffect(() => {
        async function getStats() {
            setLoading(true);
            const response = await getRoutesStats();
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
                    <PageTitle title="Rotas" description="Gerencie as rotas da frota" />
                    <Stack direction="row" spacing={2}>
                        <Button variant="outlined" color="primary" startIcon={<AddIcon/>} onClick={navigateToRouteEditPage}>Adicionar Rota</Button>
                    </Stack>
                </Stack>
                <Stack direction="row" spacing={5} sx={{ marginBottom: 5, marginTop: 5 }}>
                    <StatsCard title="Total" value={stats.total} loading={loading} />
                    <StatsCard title="Ativos" value={stats.active} loading={loading}/>
                    <StatsCard title="Inativos" value={stats.inactive} loading={loading}/>
                </Stack>
                <RouteTable />
            </Stack>
        </Stack>
    );
}