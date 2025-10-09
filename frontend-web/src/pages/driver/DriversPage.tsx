import { Button, InputAdornment, Stack, TextField } from "@mui/material";
import SideMenu from "../../components/SideManu";
import { GridSearchIcon } from "@mui/x-data-grid";
import AddIcon from '@mui/icons-material/Add';
import { StatsCard } from "../../components/StatsCard";
import DriversTable from "../../components/drivers/DriversTable";
import PageTitle from "../../components/ui/PageTitle";
import { useNavigate } from "react-router";

export default function DriversPage() {
    const navigate = useNavigate();
    
    const navigateToDriverInfo = () => {
        navigate('/drivers/edit');
    };

    return (
       <Stack direction="row" sx={{ backgroundColor: '#F7F9FA'}}>
            <SideMenu />
            <Stack sx={{ paddingLeft: 5, paddingTop: 5 }}>
                <Stack direction="row" sx={{display: 'flex', justifyContent: 'space-between', alignItems: 'center'}} >
                    <PageTitle title="Motoristas" description="Gerencie os motoristas da empresa" />
                    <Stack direction="row" spacing={2}>
                        <TextField id="outlined-basic" placeholder="Buscar motoristas..." variant="outlined" slotProps={{
                            input: {
                                startAdornment: (
                                <InputAdornment position="start">
                                    <GridSearchIcon />
                                </InputAdornment>
                                ),
                            },
                            }}/>
                        <Button variant="outlined" color="primary" startIcon={<AddIcon/>} onClick={navigateToDriverInfo}>Adicionar Motorista</Button>
                    </Stack>
                </Stack>
                <Stack direction="row" spacing={5} sx={{ marginBottom: 5, marginTop: 5 }}>
                    <StatsCard title="Total" value={30}/>
                    <StatsCard title="Ativos" value={20}/>
                    <StatsCard title="Inativos" value={8}/>
                    <StatsCard title="Pendentes" value={2}/>
                </Stack>
                <DriversTable />
            </Stack>
       </Stack>
    );
}