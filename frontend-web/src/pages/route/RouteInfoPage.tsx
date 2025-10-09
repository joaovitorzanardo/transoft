import { Box, Breadcrumbs, Button, ButtonGroup, FormControl, Grid, Link, MenuItem, Paper, Select, Stack, TextField, Typography, type SelectChangeEvent } from "@mui/material";
import SideMenu from "../../components/SideManu";
import React from "react";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import { DemoContainer } from "@mui/x-date-pickers/internals/demo";
import { TimePicker } from '@mui/x-date-pickers/TimePicker';
import BlockIcon from '@mui/icons-material/Block';

const escolas = [
    {id: '1', name: 'Escola 1'},
    {id: '2', name: 'Escola 2'},
    {id: '3', name: 'Escola 3'},
];

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


export default function RouteInfoPage() {
    const [selectedSchool, setSelectedSchool] = React.useState<string>('Selecionar Escola');
    const [selectedDriver, setSelectedDriver] = React.useState<string>('Selecionar Motorista');
    const [selectedVehicle, setSelectedVehicle] = React.useState<string>('Selecionar Veiculo');
    
    const handleChangeSchool = (event: SelectChangeEvent) => {
        setSelectedSchool(event.target.value as string);
    };    
        
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
                    <Link underline="hover" color="inherit" href="/routes">Rotas</Link>
                    <Typography sx={{ color: 'text.primary' }}>Cadastro</Typography>
                </Breadcrumbs>
                <Box sx={{height: 10}}/>
                <Stack direction="row" sx={{display: 'flex', justifyContent: 'space-between', alignItems: 'center'}} >
                    <Typography variant="h4" >Cadastrar Rota</Typography>
                </Stack>
                <Box sx={{height: 20}}/>
                <FormControl component="form">
                    <Paper sx={{ padding: 3 }} >
                        <Typography variant="h5" >Informações Gerais</Typography>
                        <TextField label="Nome" variant="outlined" sx={{width: '100%', marginTop: 3}}/>
                        <Grid container spacing={2} sx={{marginTop: 3}}>
                            <Grid size={6}>
                                <Select sx={{width: '100%'}}
                                    value={selectedSchool}
                                    label="Escola"
                                    onChange={handleChangeSchool}
                                    >
                                    {escolas.map((escola) => (
                                        <MenuItem value={escola.name}>
                                            {escola.name}
                                        </MenuItem> 
                                    ))}
                                </Select>
                            </Grid>
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
                        </Grid>
                        <Select sx={{width: '100%', marginTop: 3}}
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
                    </Paper>
                    <Box sx={{height: 20}}/>
                    <Paper sx={{ padding: 3 }} >
                        <Typography variant="h5" >Viagem de Ida</Typography>
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
                    <Paper sx={{ padding: 3 }} >
                        <Typography variant="h5" >Viagem de Volta</Typography>
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
                    <Paper sx={{ padding: 3 }} >
                        <Typography variant="h5" >Dias da Semana</Typography>
                        <ButtonGroup variant="outlined" aria-label="Basic button group" sx={{marginTop: 2}}>
                            <Button>Segunda</Button>
                            <Button>Terça</Button>
                            <Button>Quarta</Button>
                            <Button>Quinta</Button>
                            <Button>Sexta</Button>
                        </ButtonGroup>
                    </Paper>        
                    <Box sx={{height: 20}}/>
                    <Stack direction="row" justifyContent="flex-start" spacing={2}>
                        <Button variant="contained" color="primary" type="submit">Salvar</Button>
                        <Button variant="outlined" color="error" startIcon={<BlockIcon />}>Desabilitar</Button>
                    </Stack>
                </FormControl>
            </Stack>
        </Stack>
    )
}