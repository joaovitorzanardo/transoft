import { Chip, Container } from "@mui/material";
import { DataGrid, GridActionsCellItem, type GridColDef, type GridRowId } from "@mui/x-data-grid";
import FindInPageIcon from '@mui/icons-material/FindInPage';
import EditIcon from '@mui/icons-material/Edit';
import { useNavigate } from "react-router";
import React from "react";
import RouteDialog from "./RouteDialog";
import { getRoutes } from "../../services/route.service";
import type RoutePresenter from "../../models/route/RoutePresenter";

interface RouteData {
    id: string;
    name: string;
    schoolName: string;
    defaultDriverName: string;
    defaultVechileName: string;
    isActive: boolean;
}

export default function RouteTable() {
    const [selectedRouteId, setSelectedRouteId] = React.useState<string>('');
    const [openRouteDialog, setOpenRouteDialog] = React.useState(false);

    const handleOpenRouteDialog = (id: GridRowId) => () => {
        setSelectedRouteId(id.toString());
        setOpenRouteDialog(true)
    };

    const handleCloseRouteDialog = () => {
        setOpenRouteDialog(false)
    }

    const navigate = useNavigate();

    const navigateToRouteEditPage = (id: GridRowId) => () => {
        navigate(`/routes/${id}`);
    };

    const [data, setData] = React.useState<RouteData[]>([]);
    const [loading, setLoading] = React.useState<boolean>(false);
    const [rowCount, setRowCount] = React.useState<number>(0);

    const [paginationModel, setPaginationModel] = React.useState({
        pageSize: 5,
        page: 0,
    });

    React.useEffect(() => {
        async function getAllRoutes() {
            setLoading(true);
            const response = await getRoutes(paginationModel.page, paginationModel.pageSize)
            setLoading(false);

            if (response.status !== 200) {
                return;
            }

            setRowCount(response.data.count);
            setData(response.data.routes.map((route: RoutePresenter) => {
                return {
                    id: route.routeId,
                    name: route.name,
                    schoolName: route.school.name,
                    defaultDriverName: route.defaultDriver.name,
                    defaultVechileName: route.defaultVehicle.vehicleModel.modelName,
                    isActive: route.active,
                }
            }))
        }

        getAllRoutes();
    }, [paginationModel])

    const columns: GridColDef[] = [
        { field: 'name', headerName: 'Nome', width: 90 },
        { field: 'schoolName', headerName: 'Escola', width: 150 },
        { field: 'defaultDriverName', headerName: 'Motorista', width: 150 },
        { field: 'defaultVechileName', headerName: 'Veículo', width: 150 },
        {
            field: 'isActive',
            headerName: 'Status',
            width: 120,
            renderCell: (params) => {
                const getChipProps = (isActive: boolean) => {
                    if (isActive) {
                        return { color: 'success' as const, label: 'Ativo' };
                    }

                    return { color: 'error' as const, label: 'Inativo' };
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
                        onClick={handleOpenRouteDialog(id)}
                        color="inherit"
                    />,
                    <GridActionsCellItem
                        icon={<EditIcon />}
                        label="Edit"
                        className="textPrimary"
                        onClick={navigateToRouteEditPage(id)}
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
            <RouteDialog open={openRouteDialog} onClose={handleCloseRouteDialog} routeId={selectedRouteId} />
        </Container>
    );
}