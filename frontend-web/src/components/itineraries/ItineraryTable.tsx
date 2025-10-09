import { Chip, Container, IconButton, Tooltip } from "@mui/material";
import { DataGrid, type GridColDef } from "@mui/x-data-grid";
import FindInPageIcon from '@mui/icons-material/FindInPage';
import EditIcon from '@mui/icons-material/Edit';
import { useNavigate } from "react-router";
import React from "react";
import ItineraryDialog from "./ItineraryDialog";

interface ItineraryData {
    id: string;
    rota: string;
    data: string
    motorista: string;
    veiculo: string;
    tipo: string;
    status: string;
}

export default function ItineraryTable() {
    const navigate = useNavigate();

    const navigateToItineraryEditPage = () => {
        navigate('/itineraries/edit');
    };

    const [openInfoDialog, setOpenInfoDialog] = React.useState(false)

    const handleOpenDialog = () => {
        setOpenInfoDialog(true)
    }

    const handleCloseDialog = () => {
        setOpenInfoDialog(false)
    }

    const columns: GridColDef[] = [
        { field: 'rota', headerName: 'Rota', width: 90 },
        { field: 'data', headerName: 'Data', width: 150 },
        { field: 'motorista', headerName: 'Motorista', width: 150 },
        { field: 'veiculo', headerName: 'Veículo', width: 150 },
        { field: 'tipo', headerName: 'Tipo', width: 120 },
        {
            field: 'status',
            headerName: 'Status',
            width: 120,
            renderCell: (params) => {
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

                const chipProps = getChipProps(params.value);
                
                return (
                    <Chip 
                        label={chipProps.label}
                        color={chipProps.color}
                        size="small"
                        variant="filled"
                    />
                );
            }
        }, 
        {
            field: 'actions',
            headerName: 'Ações',
            width: 120,
            renderCell: () => {
                return (
                    <>
                        <Tooltip title="Ver Detalhes">
                            <IconButton>
                                <FindInPageIcon onClick={handleOpenDialog}/>
                            </IconButton>
                        </Tooltip>
                        <Tooltip title="Editar">
                            <IconButton onClick={navigateToItineraryEditPage}>
                                <EditIcon />
                            </IconButton>
                        </Tooltip>
                    </>
                )
            }
        }
    ];

    const data: ItineraryData[] = [
        { id: "1", rota: 'Rota URI - Campus I', data: '23/08/2025', motorista: 'João Zich', veiculo: 'Sprinter', tipo: 'IDA', status: "AGENDADO" },
        { id: "2", rota: 'Rota URI - Campus I', data: '23/08/2025', motorista: 'João Zich', veiculo: 'Sprinter', tipo: 'VOLTA', status: "AGENDADO" },
        { id: "3", rota: 'Rota URI - Campus I', data: '23/08/2025', motorista: 'João Zich', veiculo: 'Sprinter', tipo: 'IDA', status: "EM_ANDAMENTO" },
        { id: "4", rota: 'Rota URI - Campus I', data: '23/08/2025', motorista: 'João Zich', veiculo: 'Sprinter', tipo: 'IDA', status: "CONCLUIDO" },
        { id: "5", rota: 'Rota URI - Campus I', data: '23/08/2025', motorista: 'João Zich', veiculo: 'Sprinter', tipo: 'VOLTA', status: "CANCELADO" }
    ]

    return (
        <Container>
            <DataGrid columns={columns} rows={data}/>
            <ItineraryDialog open={openInfoDialog} onClose={handleCloseDialog}/>
        </Container>
    )
}