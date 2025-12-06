import { Box, Chip, Container, Dialog, Stack, Typography } from "@mui/material";
import DialogHeader from "../ui/DialogHeader";
import { getRouteById } from "../../services/route.service";
import React from "react";
import type RoutePresenter from "../../models/route/RoutePresenter";

interface RouteDialogProps {
    open: boolean;
    onClose: () => void;
    routeId: string;
}

export default function RouteDialog({ open, onClose, routeId }: RouteDialogProps) {
    const [routeData, setRouteData] = React.useState<RoutePresenter | undefined>(undefined);

    React.useEffect(() => {
        async function getById() {
            if (routeId === undefined || routeId === 'edit') {
                return;
            }

            const response = await getRouteById(routeId);

            if (response.status !== 200) {
                return;
            }

            setRouteData(response.data);
        }

        getById();
    }, [routeId]);

    return (
        routeData &&
        <Dialog open={open} onClose={onClose} >
            <Container sx={{ padding: 3 }}>
                <DialogHeader title="Rota" onClose={onClose} />
                <Box sx={{ height: 10 }} />
                <Typography variant="body1" >Rota: {routeData.name}</Typography>
                <Typography variant="body1" >Escola: {routeData.school.name}</Typography>
                <Typography variant="body1" >Motorista: {routeData.defaultDriver.name}</Typography>
                <Typography variant="body1" >Veiculo: {routeData.defaultVehicle.plateNumber}</Typography>
                <Stack direction="row">
                    <Typography variant="body1" >Status:</Typography>
                    <Box sx={{ width: 5 }} />
                    <Chip
                        label="Ativo"
                        color="success"
                        size="small"
                        variant="filled"
                    />
                </Stack>
                <Typography variant="h6" >Viagem de Ida</Typography>
                <Typography variant="body1" >Horário Início: 18:00</Typography>
                <Typography variant="body1" >Horário Fim: 19:15</Typography>
                <Typography variant="h6" >Viagem de Volta</Typography>
                <Typography variant="body1" >Horário Início: 22:25</Typography>
                <Typography variant="body1" >Horário Fim: 23:15</Typography>
                <Typography variant="body1" >Dias da Semana: Segunda, Terça, Quarta, Quinta, Sexta</Typography>
            </Container>
        </Dialog>
    )
}