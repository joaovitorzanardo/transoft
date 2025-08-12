import { Box, Button, Container, Grid, InputAdornment, Stack, TextField, Typography } from "@mui/material";
import SideMenu from "../components/SideManu";
import { VehicleTable } from "../components/vehicles/VehicleTable";
import { VehicleStatsCard } from "../components/vehicles/VehicleStatsCard";
import AddIcon from '@mui/icons-material/Add';
import SearchIcon from '@mui/icons-material/Search';
import React from "react";
import { VehicleDialog } from "../components/vehicles/VehicleDialog";

export default function VehiclesPage() {
    const [open, setOpen] = React.useState(false);

    const handleClickOpen = () => {
        setOpen(true);
    };
    
    const handleClose = () => {
        setOpen(false);
    };

    return (
        <Grid container spacing={5}>
            <Grid>
                <SideMenu />
            </Grid>
            <Grid>
                <Stack>
                    <Container sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 2 }}>
                        <Box>
                            <Typography variant="h4" >Veículos</Typography>
                            <Typography variant="body1" >Gerencie os veículos da frota</Typography>
                        </Box>
                        <Box>
                            <TextField id="outlined-basic" placeholder="Buscar veículos..." variant="outlined" slotProps={{
                                input: {
                                    startAdornment: (
                                    <InputAdornment position="start">
                                        <SearchIcon />
                                    </InputAdornment>
                                    ),
                                },
                                }}/>
                            <Button variant="contained" color="primary" startIcon={<AddIcon/>} onClick={handleClickOpen}>Adicionar Veículo</Button>
                        </Box>
                    </Container>
                    <Grid container sx={{ marginBottom: 5 }} spacing={3}>
                        <Grid>
                            <VehicleStatsCard title="Total de Veículos" value={12}/>
                        </Grid>
                        <Grid>
                            <VehicleStatsCard title="Ativos" value={8}/>
                        </Grid>
                        <Grid>
                            <VehicleStatsCard title="Em Manutenção" value={1}/>
                        </Grid>
                        <Grid>
                            <VehicleStatsCard title="Inativos" value={3}/>
                        </Grid>
                    </Grid>
                    <Container>
                        <VehicleTable />
                    </Container>
                </Stack>
            </Grid>
            <VehicleDialog open={open} onClose={handleClose} />
        </Grid>
    );
}