import { Box, Breadcrumbs, Button, FormControl, Link, MenuItem, Paper, Select, Stack, Typography, type SelectChangeEvent } from "@mui/material";
import SideMenu from "../../components/SideManu";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import { DatePicker } from "@mui/x-date-pickers/DatePicker";
import { DemoContainer } from "@mui/x-date-pickers/internals/demo";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import React from "react";

const rotas = [
    {id: '1', name: 'Rota 1'},
    {id: '2', name: 'Rota 2'},
    {id: '3', name: 'Rota 3'},
];

export default function GenerateItineraryPage() {
    const [selectedRoute, setSelectedRoute] = React.useState<string>('Selecionar Rota');
    
    const handleChangeRoute = (event: SelectChangeEvent) => {
        setSelectedRoute(event.target.value as string);
    };

    return (
        <Stack direction="row" sx={{ backgroundColor: '#F7F9FA'}}>
            <SideMenu />
            <Stack sx={{ padding: '3rem'}}>
                <Breadcrumbs aria-label="breadcrumb">
                    <Link underline="hover" color="inherit" href="/itineraries">Itinerários</Link>
                    <Typography sx={{ color: 'text.primary' }}>Gerar</Typography>
                </Breadcrumbs>
                <Box sx={{height: 10}}/>
                <Stack direction="row" sx={{display: 'flex', justifyContent: 'space-between', alignItems: 'center'}} >
                    <Typography variant="h4" >Gerar Itinerários</Typography>
                </Stack>
                <Box sx={{height: 20}}/>
                <FormControl component="form">
                    <Paper sx={{ padding: 3 }}>
                        <Select sx={{width: '100%'}}
                            value={selectedRoute}
                            label="Rota"
                            onChange={handleChangeRoute}
                            >
                            {rotas.map((rota) => (
                                <MenuItem value={rota.name}>
                                    {rota.name}
                                </MenuItem> 
                            ))}
                        </Select>
                        <LocalizationProvider dateAdapter={AdapterDayjs}>
                            <DemoContainer components={['DatePicker']}>
                                <DatePicker label="Data Inicial"/>
                            </DemoContainer>
                        </LocalizationProvider>
                        <LocalizationProvider dateAdapter={AdapterDayjs}>
                            <DemoContainer components={['DatePicker']}>
                                <DatePicker label="Data Final"/>
                            </DemoContainer>
                        </LocalizationProvider>
                    </Paper>
                    <Box sx={{height: 30}}/>
                    <Stack direction="row" justifyContent="flex-start">
                        <Button variant="contained" color="primary" type="submit">Gerar</Button>
                    </Stack>
                </FormControl>
            </Stack>
        </Stack>
    )
}