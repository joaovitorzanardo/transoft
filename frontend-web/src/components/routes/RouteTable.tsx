import { Chip, Container, IconButton, Tooltip } from "@mui/material";
import { DataGrid, type GridColDef } from "@mui/x-data-grid";
import FindInPageIcon from '@mui/icons-material/FindInPage';
import EditIcon from '@mui/icons-material/Edit';
import { useNavigate } from "react-router";
import React from "react";
import RouteDialog from "./RouteDialog";

interface RouteData {
    id: string;
    nome: string;
    escola: string;
    motorista: string;
    veiculo: string;
    status: React.ReactNode;
}

export default function RouteTable() {
    const [openRouteDialog, setOpenRouteDialog] = React.useState(false);

    const handleOpenRouteDialog = () => {
        setOpenRouteDialog(true)
    }

    const handleCloseRouteDialog = () => {
        setOpenRouteDialog(false)
    }

    const navigate = useNavigate();

    const navigateToRouteEditPage = () => {
        navigate('/routes/edit');
    };

    const columns: GridColDef[] = [
        { field: 'nome', headerName: 'Nome', width: 90 },
        { field: 'escola', headerName: 'Escola', width: 150 },
        { field: 'motorista', headerName: 'Motorista', width: 150 },
        { field: 'veiculo', headerName: 'Veículo', width: 150 },
        {
            field: 'status',
            headerName: 'Status',
            width: 120,
            renderCell: (params) => {
                const getChipProps = (status: string) => {
                    switch (status) {
                        case 'ativo':
                            return { color: 'success' as const, label: 'Ativo' };
                        case 'inativo':
                            return { color: 'error' as const, label: 'Inativo' };
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
                                <FindInPageIcon onClick={handleOpenRouteDialog}/>
                            </IconButton>
                        </Tooltip>
                        <Tooltip title="Editar">
                            <IconButton>
                                <EditIcon onClick={navigateToRouteEditPage}/>
                            </IconButton>
                        </Tooltip>
                    </>
                )
            }
        }
    ];

    const data: RouteData[] = [
        { id: "1", nome: 'Rota URI I', escola: 'Uri Erechim - Campus I', motorista: 'João Zich', veiculo: 'Sprinter', status: "ativo" },
        { id: "2", nome: 'Rota URI II', escola: 'Uri Erechim - Campus II', motorista: 'João Zich', veiculo: 'Sprinter', status: "ativo" },
        { id: "3", nome: 'Rota Mantovani - Noturno', escola: 'Mantovani', motorista: 'João Zich', veiculo: 'Sprinter', status: "inativo" },
        { id: "4", nome: 'Rota JB - Noturno', escola: 'JB', motorista: 'João Zich', veiculo: 'Sprinter', status: "ativo" },
    ]

    return (
        <Container>
            <DataGrid columns={columns} rows={data}/>
            <RouteDialog open={openRouteDialog} onClose={handleCloseRouteDialog} />
        </Container>
    );
}