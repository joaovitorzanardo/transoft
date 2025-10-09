import { Button, Stack } from "@mui/material";
import SideMenu from "../../components/SideManu";
import { StatsCard } from "../../components/StatsCard";
import AddIcon from '@mui/icons-material/Add';
import PassengersTable from "../../components/passengers/PassengersTable";
import PageTitle from "../../components/ui/PageTitle";
import { SearchInput } from "../../components/ui/SearchInput";
import { useNavigate } from "react-router";

export default function PassengersPage() {
    const navigate = useNavigate();

    const navigateToPassengerInfo = () => {
        navigate('/passengers/edit');
    }

    return (
        <Stack direction="row" sx={{ backgroundColor: '#F7F9FA'}}>
            <SideMenu />
            <Stack sx={{ paddingLeft: 5, paddingTop: 5 }}>
                <Stack direction="row" sx={{display: 'flex', justifyContent: 'space-between', alignItems: 'center'}} >
                    <PageTitle title="Passageiros" description="Gerencie os passageiros da empresa" />
                    <Stack direction="row" spacing={2}>
                        <SearchInput title="Buscar passageiro..." />
                        <Button variant="outlined" color="primary" startIcon={<AddIcon/>} onClick={navigateToPassengerInfo}>Adicionar Passageiro</Button>
                    </Stack>
                </Stack>
                <Stack direction="row" spacing={5} sx={{ marginBottom: 5, marginTop: 5 }}>
                    <StatsCard title="Total" value={30}/>
                    <StatsCard title="Ativos" value={20}/>
                    <StatsCard title="Inativos" value={8}/>
                    <StatsCard title="Pendentes" value={2}/>
                </Stack>
                <PassengersTable />
            </Stack>
        </Stack>
    );
}