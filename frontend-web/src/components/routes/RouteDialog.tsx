import { Box, Chip, Container, Dialog, Stack, Typography } from "@mui/material";
import DialogHeader from "../ui/DialogHeader";

interface RouteDialogProps {
    open: boolean;
    onClose: () => void;
}

export default function RouteDialog({open, onClose}: RouteDialogProps) {
    return (
        <Dialog open={open} onClose={onClose} >
            <Container sx={{ padding: 3 }}>
                <DialogHeader title="Rota" onClose={onClose} />
                <Box sx={{height: 10}}/>
                <Typography variant="body1" >Rota: Rota URI - Campus I</Typography>
                <Typography variant="body1" >Escola: URI Erechim</Typography>
                <Typography variant="body1" >Motorista: Joao Zich</Typography>
                <Typography variant="body1" >Veiculo: Sprinter</Typography>
                <Stack direction="row">
                    <Typography variant="body1" >Status:</Typography>
                    <Box sx={{width: 5}}/>
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