import { Box, Button, Container, Dialog, Divider, IconButton, MenuItem, Select, TextField, Typography, type SelectChangeEvent } from "@mui/material";
import React from "react";
import BlockIcon from '@mui/icons-material/Block';
import CloseIcon from '@mui/icons-material/Close';

interface VehicleDialogProps {
    open: boolean;
    onClose: () => void;
}

export function VehicleDialog({ open, onClose }: VehicleDialogProps) {
    const [automaker, setAutomaker] = React.useState('');
    const [vehicleModel, setVehicleModel] = React.useState('');

    const handleChangeAutomaker = (event: SelectChangeEvent) => {
      setAutomaker(event.target.value as string);
    };

    const handleChangeVehicleModel = (event: SelectChangeEvent) => {
        setVehicleModel(event.target.value as string);
      };

    return (
        <Dialog open={open} onClose={onClose} >
            <Container sx={{ padding: 3 }}>
                <Container sx={{ display: 'flex', justifyContent: 'space-between'}}>
                    <Typography variant="h5" sx={{ marginBottom: 2 }}>Cadastrar Veículo</Typography>
                    <IconButton>
                        <CloseIcon onClick={onClose} />
                    </IconButton>
                </Container>
                <Divider sx={{marginTop: "20px", marginBottom: "20px"}}/>
                <Container >
                    <TextField label="Placa" variant="outlined" />
                    <Container sx={{ display: 'flex', gap: 2, marginTop: 2 }}>
                        <Select
                            value={automaker}
                            label="Montadora"
                            onChange={handleChangeAutomaker}>
                            <MenuItem value={10}>Mercedes-Benz</MenuItem>
                            <MenuItem value={20}>Volvo</MenuItem>
                        </Select>
                        <Select
                            value={vehicleModel}
                            label="Modelo"
                            onChange={handleChangeVehicleModel}>
                            <MenuItem value={10}>Sprinter</MenuItem>
                            <MenuItem value={20}>Modelo 1</MenuItem>
                        </Select>
                    </Container>
                    <Container>
                        <TextField label="Capacidade" variant="outlined" type="number" />
                    </Container>
                </Container>
                
                <Divider sx={{marginTop: "20px", marginBottom: "20px"}}/>
                
                <Container sx={{ display: 'flex', justifyContent: 'flex-end', }}>
                    <Button variant="outlined" color="error" startIcon={<BlockIcon />}>Desabilitar</Button>
                    <Box sx={{ width: "10px" }} />
                    <Button variant="outlined" color="success">Salvar</Button>
                </Container>
            </Container>
        </Dialog>
    )
}