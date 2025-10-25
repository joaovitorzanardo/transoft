import { Box, Chip, Container, Dialog, Paper, Stack, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Typography } from "@mui/material";
import DialogHeader from "../ui/DialogHeader";
import React from "react";
import type ItineraryPresenter from "../../models/ItineraryPresenter";
import type PassengerItineraryPresenter from "../../models/itinerary/PassengerItineraryPresenter";
import { getItineraryById, getPassengerFromItinerary } from "../../services/itinerary.service";

interface ItineraryDialogProps {
    open: boolean;
    onClose: () => void;
    itineraryId: string;
}


export default function ItineraryDialog({open, onClose, itineraryId}: ItineraryDialogProps) {
    const [chipProps, setChipProps] = React.useState<{ color: 'default' | 'primary' | 'secondary' | 'error' | 'info' | 'success' | 'warning'; label: string }>({ color: 'default', label: 'Desconhecido' });
    const [passengers, setPassengers] = React.useState<PassengerItineraryPresenter[]>([]);
    const [itineraryData, setItineraryData] = React.useState<ItineraryPresenter>();

    React.useEffect(() => {
        async function getById() {
            if (itineraryId === undefined || itineraryId === 'edit') {
                return;
            }

            const itinerary = await getItineraryById(itineraryId);
            const itineraryPassengers = await getPassengerFromItinerary(itineraryId);

            if (itinerary.status !== 200 || itineraryPassengers.status !== 200) {
                return;
            }

            setItineraryData(itinerary.data);
            setPassengers(itineraryPassengers.data);
            setChipProps(getChipProps(itinerary.data.status));
        }

        getById();
    }, [itineraryId]);

    const getChipProps = (status: string) => {
        switch (status) {
            case 'AGENDADO':
                return { color: 'primary' as const, label: 'Agendado' };
            case 'EM_ANDAMENTO':
                return { color: 'warning' as const, label: 'Andamento' };
            case 'CONCLUIDO':
                return { color: 'success' as const, label: 'Concluído' };
            case 'CANCELADO':
                    return { color: 'error' as const, label: 'Cancelado' };    
            default:
                return { color: 'default' as const, label: 'Desconhecido' };
        }
    };

    return (
        <Dialog open={open} onClose={onClose} >
            <Container sx={{ padding: 3 }}>
                <DialogHeader title="Itinerário" onClose={onClose} />
                <Box sx={{height: 10}}/>
                <Typography variant="body1" >Rota: {itineraryData?.route.name}</Typography>
                <Typography variant="body1" >Data: {itineraryData?.date.toString()}</Typography>
                <Typography variant="body1" >Motorista: {itineraryData?.driver.name}</Typography>
                <Typography variant="body1" >Veiculo: {itineraryData?.vehicle.plateNumber}</Typography>
                <Typography variant="body1" >Tipo: {itineraryData?.type}</Typography>
                <Stack direction="row">
                    <Typography variant="body1" >Status:</Typography>
                    <Box sx={{width: 5}}/>
                    <Chip 
                        label={chipProps.label}
                        color={chipProps.color}
                        size="small"
                        variant="filled"
                    />
                </Stack>                
                <Box sx={{height: 20}}/>
                <TableContainer component={Paper}>
                    <Table sx={{ minWidth: 350 }} aria-label="simple table">
                        <TableHead>
                            <TableRow>
                                <TableCell>Nome</TableCell>
                                <TableCell align="right">Status</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {passengers.map((passenger) => (
                                <TableRow
                                    key={passenger.passenger.passengerId}
                                    sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
                                >
                                    <TableCell component="th" scope="row">
                                        {passenger.passenger.name}
                                    </TableCell>
                                    <TableCell component="th" scope="row">
                                        {
                                            (passenger.status === 'CONFIRMADO') ? 
                                            <Chip 
                                                label="Confirmado"
                                                color="success"
                                                size="small"
                                                variant="filled"
                                            /> : 
                                            <Chip 
                                                label="Não Vai"
                                                color="error"
                                                size="small"
                                                variant="filled"
                                            />
                                        }
                                    </TableCell>
                                </TableRow>
                            ))}
                        </TableBody>
                    </Table>
                </TableContainer>
            </Container>
        </Dialog>
    );
}