import { Button, InputAdornment, Stack, TextField } from "@mui/material";
import SideMenu from "../../components/SideManu";
import { GridSearchIcon } from "@mui/x-data-grid";
import AddIcon from '@mui/icons-material/Add';
import { StatsCard } from "../../components/StatsCard";
import DriversTable from "../../components/drivers/DriversTable";
import PageTitle from "../../components/ui/PageTitle";
import { useNavigate } from "react-router";
import React from "react";
import { getDriversStats } from "../../services/driver.service";

interface StatsDrivers {
    total: number;
    active: number;
    inactive: number;
    pending: 0;
}

export default function DriversPage() {
    const navigate = useNavigate();
    const [stats, setStats] = React.useState<StatsDrivers>({total: 0, active: 0, inactive: 0, pending: 0});
    const [loading, setLoading] = React.useState<boolean>(false);
    
    const navigateToDriverInfo = () => {
        navigate('/drivers/edit');
    };

    React.useEffect(() => {
        async function getStats() {
            setLoading(true);
            const response = await getDriversStats();
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
                    <PageTitle title="Motoristas" description="Gerencie os motoristas da empresa" />
                    <Stack direction="row" spacing={2}>
                        <Button variant="outlined" color="primary" startIcon={<AddIcon/>} onClick={navigateToDriverInfo}>Adicionar Motorista</Button>
                    </Stack>
                </Stack>
                <Stack direction="row" spacing={5} sx={{ marginBottom: 5, marginTop: 5 }}>
                    <StatsCard title="Total" value={stats.total} loading={loading}/>
                    <StatsCard title="Ativos" value={stats.pending} loading={loading}/>
                    <StatsCard title="Inativos" value={stats.inactive} loading={loading}/>
                    <StatsCard title="Pendentes" value={stats.pending} loading={loading}/>
                </Stack>
                <DriversTable />
            </Stack>
       </Stack>
    );
}