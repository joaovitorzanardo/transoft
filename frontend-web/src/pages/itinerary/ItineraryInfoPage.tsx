import { Box, Breadcrumbs, Button, FormControl, Grid, Link, MenuItem, Paper, Select, Stack, Typography, type SelectChangeEvent } from "@mui/material";
import SideMenu from "../../components/SideManu";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import { DemoContainer } from "@mui/x-date-pickers/internals/demo";
import { TimePicker } from "@mui/x-date-pickers/TimePicker";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import React from "react";
import CancelIcon from '@mui/icons-material/Cancel';

const motoristas = [
    {id: '1', name: 'Motorista 1'},
    {id: '2', name: 'Motorista 2'},
    {id: '3', name: 'Motorista 3'},
];

const veiculos = [
    {id: '1', name: 'Veiculo 1'},
    {id: '2', name: 'Veiculo 2'},
    {id: '3', name: 'Veiculo 3'},
];

export default function ItineraryInfoPage() {
    const [selectedDriver, setSelectedDriver] = React.useState<string>('Selecionar Motorista');
    const [selectedVehicle, setSelectedVehicle] = React.useState<string>('Selecionar Veiculo');

    const handleChangeDriver = (event: SelectChangeEvent) => {
        setSelectedDriver(event.target.value as string);
    };
    
    const handleChangeVehicle = (event: SelectChangeEvent) => {
        setSelectedVehicle(event.target.value as string);
    };

    return (
        <Stack direction="row" sx={{ backgroundColor: '#F7F9FA'}}>
            <SideMenu />
            <Stack sx={{ padding: '3rem'}}>
                <Breadcrumbs aria-label="breadcrumb">
                    <Link underline="hover" color="inherit" href="/itineraries">Itinerários</Link>
                    <Typography sx={{ color: 'text.primary' }}>Editar</Typography>
                </Breadcrumbs>
                <Box sx={{height: 10}}/>
                <Stack direction="row" sx={{display: 'flex', justifyContent: 'space-between', alignItems: 'center'}} >
                    <Typography variant="h4" >Editar Itinerário</Typography>
                </Stack>
                <Box sx={{height: 20}}/>
                <FormControl component="form">
                    <Paper sx={{ padding: 3 }}>
                        <Typography variant="h5" >Informações Gerais</Typography>
                        <Grid container spacing={2} sx={{marginTop: 3}}>
                            <Grid size={6}>
                                <Select sx={{width: '100%'}}
                                    value={selectedDriver}
                                    label="Motorista"
                                    onChange={handleChangeDriver}
                                    >
                                    {motoristas.map((motorista) => (
                                        <MenuItem value={motorista.name}>
                                            {motorista.name}
                                        </MenuItem> 
                                    ))}
                                </Select>
                            </Grid>
                            <Grid size={6}>
                                <Select sx={{width: '100%'}}
                                    value={selectedVehicle}
                                    label="Veículo"
                                    onChange={handleChangeVehicle}
                                    >
                                    {veiculos.map((veiculo) => (
                                        <MenuItem value={veiculo.name}>
                                            {veiculo.name}
                                        </MenuItem> 
                                    ))}
                                </Select>
                            </Grid>
                        </Grid>
                    </Paper>
                    <Box sx={{height: 30}}/>
                    <Paper sx={{ padding: 3 }}>
                        <Typography variant="h5" >Horário</Typography>
                        <LocalizationProvider dateAdapter={AdapterDayjs}>
                            <Stack direction="row" spacing={2} sx={{marginTop: 2}}>
                                <DemoContainer components={['TimePicker']}>
                                    <TimePicker label="Início" />
                                </DemoContainer>
                                <DemoContainer components={['TimePicker']}>
                                    <TimePicker label="Fim" />
                                </DemoContainer>
                            </Stack>
                        </LocalizationProvider>
                    </Paper>
                    <Box sx={{height: 20}}/>
                    <Stack direction="row" justifyContent="flex-start" spacing={2}>
                        <Button variant="contained" color="primary" type="submit">Salvar</Button>
                        <Button variant="outlined" color="error" startIcon={<CancelIcon />}>Cancelar</Button>
                    </Stack>
                </FormControl>
            </Stack>
        </Stack>
    )
}