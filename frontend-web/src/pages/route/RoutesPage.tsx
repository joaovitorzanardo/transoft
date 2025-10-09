import { Button, Stack } from "@mui/material";
import SideMenu from "../../components/SideManu";
import PageTitle from "../../components/ui/PageTitle";
import { SearchInput } from "../../components/ui/SearchInput";
import AddIcon from '@mui/icons-material/Add';
import { StatsCard } from "../../components/StatsCard";
import { useNavigate } from "react-router";
import RouteTable from "../../components/routes/RouteTable";

export default function RoutesPage() {
    const navigate = useNavigate();

    const navigateToRouteEditPage = () => {
        navigate('/routes/edit');
    };

    return (
        <Stack direction="row" sx={{ backgroundColor: '#F7F9FA'}}>
            <SideMenu />
            <Stack sx={{ paddingLeft: 5, paddingTop: 5 }}>
                <Stack direction="row" sx={{display: 'flex', justifyContent: 'space-between', alignItems: 'center'}} >
                    <PageTitle title="Rotas" description="Gerencie as rotas da frota" />
                    <Stack direction="row" spacing={2}>
                        <SearchInput title="Buscar rotas..."/>
                        <Button variant="outlined" color="primary" startIcon={<AddIcon/>} onClick={navigateToRouteEditPage}>Adicionar Rota</Button>
                    </Stack>
                </Stack>
                <Stack direction="row" spacing={5} sx={{ marginBottom: 5, marginTop: 5 }}>
                    <StatsCard title="Total" value={12}/>
                    <StatsCard title="Ativos" value={8}/>
                    <StatsCard title="Inativos" value={3}/>
                </Stack>
                <RouteTable />
            </Stack>
        </Stack>
    );
}