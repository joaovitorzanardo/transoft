import { Chip, Container } from "@mui/material";
import { DataGrid, GridActionsCellItem, type GridColDef, type GridRowId } from "@mui/x-data-grid";
import FindInPageIcon from '@mui/icons-material/FindInPage';
import EditIcon from '@mui/icons-material/Edit';
import { useNavigate } from "react-router";
import React from "react";
import ItineraryDialog from "./ItineraryDialog";
import { getItineraries } from "../../services/itinerary.service";
import type ItineraryPresenter from "../../models/ItineraryPresenter";

interface ItineraryData {
    id: string;
    routeName: string;
    date: string
    driverName: string;
    vehiclePlate: string;
    type: string;
    status: string;
}

export default function ItineraryTable() {
    const navigate = useNavigate();

    const navigateToItineraryEditPage = (id: GridRowId) => () => {
        navigate(`/itineraries/${id}`);
    };

    const [data, setData] = React.useState<ItineraryData[]>([]);
    const [loading, setLoading] = React.useState<boolean>(false);
    const [rowCount, setRowCount] = React.useState<number>(0);

    const [paginationModel, setPaginationModel] = React.useState({
        pageSize: 5,
        page: 0,
    });

    const [openInfoDialog, setOpenInfoDialog] = React.useState(false)

    const handleOpenDialog = () => {
        setOpenInfoDialog(true)
    }

    const handleCloseDialog = () => {
        setOpenInfoDialog(false)
    }

    React.useEffect(() => {
        async function getAll() {
            setLoading(true);
            const response = await getItineraries(paginationModel.page, paginationModel.pageSize)
            setLoading(false);

            if (response.status !== 200) {
                return;
            }

            setRowCount(response.data.count);
            setData(response.data.itineraries.map((itinerary: ItineraryPresenter) => {
                return {
                    id: itinerary.itineraryId,
                    routeName: itinerary.route.name,
                    date: itinerary.date.toString(),
                    driverName: itinerary.driver.name,
                    vehiclePlate: itinerary.vehicle.plateNumber,
                    type: itinerary.type,
                    status: itinerary.status
                }
            })) 
        }

        getAll();
    }, [paginationModel])

    const columns: GridColDef[] = [
        { field: 'routeName', headerName: 'Rota', width: 90 },
        { field: 'date', headerName: 'Data', width: 150 },
        { field: 'driverName', headerName: 'Motorista', width: 150 },
        { field: 'vehiclePlate', headerName: 'Veículo', width: 150 },
        { field: 'type', headerName: 'Tipo', width: 120 },
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
            type: 'actions',
            headerName: 'Ações',
            width: 120,
            getActions: ({ id }) => {
                return [
                    <GridActionsCellItem
                        icon={<FindInPageIcon />}
                        label="Ver Detalhes"
                        className="textPrimary"
                        onClick={handleOpenDialog}
                        color="inherit"
                    />,
                    <GridActionsCellItem
                        icon={<EditIcon />}
                        label="Edit"
                        className="textPrimary"
                        onClick={navigateToItineraryEditPage(id)}
                        color="inherit"
                    />
                ]
            }
        }
    ];

    return (
        <Container>
            <DataGrid columns={columns} 
                rows={data}
                loading={loading}
                paginationModel={paginationModel}
                onPaginationModelChange={setPaginationModel}
                pageSizeOptions={[5, 15, 50]}
                rowCount={rowCount}
                paginationMode="server"
            />
            <ItineraryDialog open={openInfoDialog} onClose={handleCloseDialog}/>
        </Container>
    )
}