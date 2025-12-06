import { Box, Button, Container, Dialog, Divider, MenuItem, Select, TextField, type SelectChangeEvent } from "@mui/material";
import React from "react";
import BlockIcon from '@mui/icons-material/Block';

import { getAutomakers } from "../../services/automaker.service";
import { getVehicleModelsByAutomaker } from "../../services/vehiclemodel.service";

import type Automaker from "../../models/vehicle/AutomakerPresenter";
import type VehicleModel from "../../models/vehicle/VehicleModel";
import DialogHeader from "../ui/DialogHeader";

interface VehicleDialogProps {
    open: boolean;
    onClose: () => void;
}

export function VehicleDialog({ open, onClose }: VehicleDialogProps) {
    const [automakers, setAutomakers] = React.useState<Automaker[]>([]);
    const [vehicleModels, setVehicleModels] = React.useState<VehicleModel[]>([]);

    const [selectedAutomaker, setSelectedAutomaker] = React.useState<string>();
    const [selectedVehicleModel, setSelectedVehicleModel] = React.useState<string>();

    const handleChangeAutomaker = (event: SelectChangeEvent) => {
        setSelectedAutomaker(event.target.value as string);
    };

    const handleChangeVehicleModel = (event: SelectChangeEvent) => {
        setSelectedVehicleModel(event.target.value as string);
    };

    React.useEffect(() => {
        const fetchAutomakers = async () => {
            try {
                const automakersData = await getAutomakers();
                setAutomakers(automakersData.data as Automaker[]);
            } catch (error) {
                console.error('Error fetching automakers:', error);
            }
        }

        fetchAutomakers();
    }, []);

    React.useEffect(() => {
        const fetchVehicleModels = async () => {
            if (!selectedAutomaker) return;

            try {
                const vehicleModelsData = await getVehicleModelsByAutomaker(selectedAutomaker);
                setVehicleModels(vehicleModelsData.data as VehicleModel[]);
            } catch (error) {
                console.error('Error fetching automakers:', error);
            }
        }

        fetchVehicleModels();
    }, [selectedAutomaker]);


    return (
        <Dialog open={open} onClose={onClose} >
            <Container sx={{ padding: 3 }}>
                <DialogHeader title="Cadastrar veículo" onClose={onClose} />
                <Container >
                    <TextField label="Placa" variant="outlined" />
                    <Container sx={{ display: 'flex', gap: 2, marginTop: 2 }}>
                        <Select
                            value={selectedAutomaker}
                            label="Montadora"
                            onChange={handleChangeAutomaker}>
                            {automakers.map((automaker) => (
                                <MenuItem value={automaker.automakerId}>
                                    {automaker.name}
                                </MenuItem>
                            ))}
                        </Select>
                        <Select
                            value={selectedVehicleModel}
                            label="Modelo"
                            onChange={handleChangeVehicleModel}>
                            {vehicleModels.map((vehicleModel) => (
                                <MenuItem value={vehicleModel.modelName}>
                                    {vehicleModel.modelName}
                                </MenuItem>
                            ))}
                        </Select>
                    </Container>
                    <Container>
                        <TextField label="Capacidade" variant="outlined" type="number" />
                    </Container>
                </Container>

                <Divider sx={{ marginTop: "20px", marginBottom: "20px" }} />

                <Container sx={{ display: 'flex', justifyContent: 'flex-end', }}>
                    <Button variant="outlined" color="error" startIcon={<BlockIcon />}>Desabilitar</Button>
                    <Box sx={{ width: "10px" }} />
                    <Button variant="outlined" color="success">Salvar</Button>
                </Container>
            </Container>
        </Dialog>
    )
}