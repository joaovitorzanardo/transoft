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
import { DemoContainer } from "@mui/x-date-pickers/internals/demo";
import { getItinerariesStats } from "../../services/itinerary.service";

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
}

export default function ItinerariesPage() {
    const [selectedStatus, setSelectedStatus] = React.useState<string[]>([]);
    const [selectedType, setSelectedType] = React.useState<string[]>([]);

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

    const navigate = useNavigate();

    const navigateToGenerateItineraryPage = () => {
        navigate('/itineraries/generate');
    };

    const [stats, setStats] = React.useState<StatsItineraries>({total: 0, scheduled: 0, finished: 0, canceled: 0});
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

        getStats();
    }, [])

    return (
        <Stack direction="row" sx={{ backgroundColor: '#F7F9FA'}}>
            <SideMenu />
            <Stack sx={{ paddingLeft: 5, paddingTop: 5 }}>
                <Stack direction="row" sx={{display: 'flex', justifyContent: 'space-between', alignItems: 'center'}} >
                    <PageTitle title="Itinerários" description="Gerencie os itinerários da frota" />
                    <Stack direction="row" spacing={2}>
                        <Button variant="outlined" color="primary" startIcon={<AddIcon/>} onClick={navigateToGenerateItineraryPage}>Gerar Itinerário</Button>
                    </Stack>
                </Stack>
                <Stack direction="row" spacing={5} sx={{ marginBottom: 5, marginTop: 5 }}>
                    <StatsCard title="Total" value={stats.total} loading={loading}/>
                    <StatsCard title="Agendados" value={stats.scheduled} loading={loading}/>
                    <StatsCard title="Concluídos" value={stats.finished} loading={loading}/>
                    <StatsCard title="Cancelados" value={stats.canceled} loading={loading}/>
                </Stack>
                <Paper sx={{ padding: 2, marginBottom: 5 }}>
                    <Stack direction="row" spacing={1}>
                        <Typography variant="subtitle1" >Filtros Rápidos</Typography>
                        <BoltIcon />
                    </Stack>
                    <Box sx={{height: '5px'}}/>
                    <Divider/>
                    <Box sx={{height: '15px'}}/>
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
                                label="Data" 
                                sx={{ flex: 1 }}
                                slotProps={{
                                    textField: {
                                      size: 'small',
                                    },
                                  }}
                            />
                        </LocalizationProvider>
                        <Button 
                            variant="contained" 
                            color="primary" 
                            size="small">Aplicar
                        </Button>
                    </Stack>
                </Paper>
                <ItineraryTable />
            </Stack>
        </Stack>
    );
}