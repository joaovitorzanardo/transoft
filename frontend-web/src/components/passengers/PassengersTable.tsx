import { Chip, Container } from "@mui/material";
import { DataGrid, GridActionsCellItem, type GridColDef, type GridRowId } from '@mui/x-data-grid';
import EditIcon from '@mui/icons-material/Edit';
import { useNavigate } from "react-router";
import React from "react";
import type { Status } from "../../models/Status";
import { getPassengers } from "../../services/passenger.service";
import type PassengerPresenter from "../../models/PassengerPresenter";

interface PassengerData {
    id: number;
    nome: string;
    email: string;
    phoneNumber: string;
    routeName: string;
    address: string;
    status: React.ReactNode;
}

export default function PassengersTable() {
    const navigate = useNavigate();

    const navigateToPassengerInfo = (id: GridRowId) => () => {
        navigate(`/passengers/${id}`);
    };

    const [data, setData] = React.useState<PassengerData[]>([]);
    const [loading, setLoading] = React.useState<boolean>(false);
    const [rowCount, setRowCount] = React.useState<number>(0);

    const [paginationModel, setPaginationModel] = React.useState({
        pageSize: 5,
        page: 0,
    });

    function getStatus(active: boolean, enabled: boolean): Status {
        if (!active) return 'pendente';
        if (enabled) return 'ativo';
        return 'inativo';
    }

    React.useEffect(() => {
        async function getAll() {
            setLoading(true);
            const response = await getPassengers(paginationModel.page, paginationModel.pageSize)
            setLoading(false);

            if (response.status !== 200) {
                return;
            }

            setRowCount(response.data.count);
            setData(response.data.passengers.map((passenger: PassengerPresenter) => {
                return {
                    id: passenger.passengerId,
                    name: passenger.name,
                    email: passenger.email,
                    phoneNumber: `(${passenger.phoneNumber.ddd}) ${passenger.phoneNumber.number}`,
                    routeName: passenger.routeName,
                    address: `${passenger.address.street}, ${passenger.address.city}, ${passenger.address.uf}, ${passenger.address.cep}, Brasil`,
                    status: getStatus(passenger.active, passenger.enabled)
                }
            }))
        }

        getAll();
    }, [paginationModel])

    const columns: GridColDef[] = [
        { field: 'name', headerName: 'Nome', width: 90 },
        { field: 'email', headerName: 'Email', width: 150 },
        { field: 'phoneNumber', headerName: 'Telefone', width: 150 },
        { field: 'routeName', headerName: 'Rota', width: 150 },
        { field: 'address', headerName: 'Endereço', width: 120 },
        {
            field: 'status',
            headerName: 'Status',
            width: 120,
            renderCell: (params) => {
                const getChipProps = (status: Status) => {
                    switch (status) {
                        case 'ativo':
                            return { color: 'success' as const, label: 'Ativo' };
                        case 'inativo':
                            return { color: 'error' as const, label: 'Inativo' };
                        case 'pendente':
                            return { color: 'warning' as const, label: 'Pendente' }
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
                        icon={<EditIcon />}
                        label="Edit"
                        className="textPrimary"
                        onClick={navigateToPassengerInfo(id)}
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
        </Container>
    )
}