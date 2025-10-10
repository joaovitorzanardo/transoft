import { Chip, Container } from "@mui/material";
import { DataGrid, GridActionsCellItem, type GridColDef, type GridRowId  } from '@mui/x-data-grid';
import EditIcon from '@mui/icons-material/Edit';
import { useNavigate } from "react-router";
import React from "react";
import { getVehicles } from "../../services/vehicle.service";

interface VehicleData {
    id: number;
    plateNumber: string;
    modelName: string;
    automakerName: string;
    modelYear: number;
    capacity: number;
    isActive: boolean;
}

interface AutomakerPresenter {
    automakerId: string;
    name: string;
}

interface VehicleModelPresenter {
    vehicleModelId: string;
    modelName: string;
    modelYear: number;
    automaker: AutomakerPresenter;
}

interface VehiclePresenter {
    vehicleId: string;
    plateNumber: string;
    capacity: number;
    isActive: boolean;
    vehicleModel: VehicleModelPresenter;
}

export function VehicleTable() {
    const navigate = useNavigate();

    const [data, setData] = React.useState<VehicleData[]>([]);
    const [loading, setLoading] = React.useState<boolean>(false);
    const [rowCount, setRowCount] = React.useState<number>(0);

    const [paginationModel, setPaginationModel] = React.useState({
        pageSize: 5,
        page: 0,
    });

    const navigateToVehicleInfo = (id: GridRowId) => () => {
        console.log(id)
        navigate(`/vehicles/${id}`);
    };

    React.useEffect(() => {
        async function getAllVehicles() {
            setLoading(true);
            const response = await getVehicles(paginationModel.page, paginationModel.pageSize)
            setLoading(false);

            if (response.status !== 200) {
                return;
            }

            setRowCount(response.data.count);
            setData(response.data.vehicles.map((vehicle: VehiclePresenter) => {
                return {
                    id: vehicle.vehicleId,
                    plateNumber: vehicle.plateNumber,
                    modelName: vehicle.vehicleModel.modelName,
                    modelYear: vehicle.vehicleModel.modelYear,
                    capacity: vehicle.capacity,
                    isActive: vehicle.isActive,
                    automakerName: vehicle.vehicleModel.automaker.name
                }
            }))
        }

        getAllVehicles();
    }, [paginationModel])

    const columns: GridColDef[] = [
        { field: 'plateNumber', headerName: 'Placa', width: 90 },
        { field: 'modelName', headerName: 'Veículo', width: 150 },
        { field: 'automakerName', headerName: 'Montadora', width: 150 },
        { field: 'modelYear', headerName: 'Ano', width: 150 },
        { field: 'capacity', headerName: 'Capacidade', width: 120 },
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
                        icon={<EditIcon />}
                        label="Edit"
                        className="textPrimary"
                        onClick={navigateToVehicleInfo(id)}
                        color="inherit"
                    />
                ]
            }
        }
    ];

    return (
        <Container>
            <DataGrid 
                columns={columns} 
                rows={data} 
                loading={loading}
                paginationModel={paginationModel}
                onPaginationModelChange={setPaginationModel}
                pageSizeOptions={[5, 15, 50]}
                rowCount={rowCount}
                paginationMode="server"
            />
        </Container>
    )
}