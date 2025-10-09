import { Box, Chip, Container, Dialog, Paper, Stack, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Typography } from "@mui/material";
import DialogHeader from "../ui/DialogHeader";

interface ItineraryDialogProps {
    open: boolean;
    onClose: () => void;
}

const passageiros = [
    {nome: "Joao Vitor", status: "CONFIRMADO"},
    {nome: "Pedro", status: "CONFIRMADO"},
    {nome: "Ale", status: "NAO VAI"}
]

export default function ItineraryDialog({open, onClose}: ItineraryDialogProps) {

    return (
        <Dialog open={open} onClose={onClose} >
            <Container sx={{ padding: 3 }}>
                <DialogHeader title="Itinerário" onClose={onClose} />
                <Box sx={{height: 10}}/>
                <Typography variant="body1" >Rota: Rota URI - Campus I</Typography>
                <Typography variant="body1" >Data: 23/08/2025</Typography>
                <Typography variant="body1" >Motorista: Joao Zich</Typography>
                <Typography variant="body1" >Veiculo: Sprinter</Typography>
                <Typography variant="body1" >Tipo: IDA</Typography>
                <Stack direction="row">
                    <Typography variant="body1" >Status:</Typography>
                    <Box sx={{width: 5}}/>
                    <Chip 
                        label="Aguardando"
                        color="primary"
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
                            {passageiros.map((passageiro) => (
                                <TableRow
                                    key={passageiro.nome}
                                    sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
                                >
                                    <TableCell component="th" scope="row">
                                        {passageiro.nome}
                                    </TableCell>
                                    <TableCell component="th" scope="row">
                                        {
                                            (passageiro.status === 'CONFIRMADO') ? 
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