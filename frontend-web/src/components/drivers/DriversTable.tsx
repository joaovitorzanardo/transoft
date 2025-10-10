import { Chip, Container } from "@mui/material";
import { DataGrid, GridActionsCellItem, type GridColDef, type GridRowId } from "@mui/x-data-grid";
import EditIcon from '@mui/icons-material/Edit';
import { useNavigate } from "react-router";
import React from "react";
import { getDrivers } from "../../services/driver.service";

type Status = 'ativo' | 'inativo' | 'pendente';

interface DriverData {
    id: string;
    name: string;
    email: string;
    cnhNumber: string;
    cnhExpirationDate: string;
    phoneNumber: string;
    status: Status;
}
  
interface DriverPresenter {
    driverId: string;
    name: string;
    email: string;
    cnhNumber: string;
    cnhExpirationDate: string;
    phoneNumber: PhoneNumberDto;
    enabled: boolean;
    active: boolean;
}
  
interface PhoneNumberDto {
    ddd: string;
    number: string;
}

export default function DriversTable() {
    const navigate = useNavigate();

    const navigateToDriverInfo = (id: GridRowId) => () => {
        navigate(`/drivers/${id}`);
    };

    const [data, setData] = React.useState<DriverData[]>([]);
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
        async function getAllDrivers() {
            setLoading(true);
            const response = await getDrivers(paginationModel.page, paginationModel.pageSize)
            setLoading(false);

            if (response.status !== 200) {
                return;
            }

            setRowCount(response.data.count);
            setData(response.data.drivers.map((driver: DriverPresenter) => {
                return {
                    id: driver.driverId,
                    name: driver.name,
                    email: driver.email,
                    cnhNumber: driver.cnhNumber,
                    cnhExpirationDate: driver.cnhExpirationDate,
                    phoneNumber: `(${driver.phoneNumber.ddd}) ${driver.phoneNumber.number}`,
                    status: getStatus(driver.active, driver.enabled)
                }
            }))
        }

        getAllDrivers();
    }, [paginationModel])

    const columns: GridColDef[] = [
        { field: 'name', headerName: 'Nome', width: 90 },
        { field: 'email', headerName: 'Email', width: 150 },
        { field: 'phoneNumber', headerName: 'Telefone', width: 150 },
        { field: 'cnhNumber', headerName: 'CNH', width: 150 },
        { field: 'cnhExpirationDate', headerName: 'Validade', width: 120 },
        {
            field: 'status',
            headerName: 'Status',
            width: 120,
            renderCell: (params) => {
                const getChipProps = (status: Status) => {
                    switch(status) {
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
                        onClick={navigateToDriverInfo(id)}
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
    );
}