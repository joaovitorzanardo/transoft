import { Chip, Container, IconButton, Tooltip } from "@mui/material";
import { DataGrid, type GridColDef  } from '@mui/x-data-grid';
import FindInPageIcon from '@mui/icons-material/FindInPage';
import EditIcon from '@mui/icons-material/Edit';

interface VehicleData {
    id: number;
    placa: string;
    veiculo: string;
    montadora: string;
    ano: number;
    capacidade: number;
    status: React.ReactNode;
}

export function VehicleTable() {

    const columns: GridColDef[] = [
        { field: 'placa', headerName: 'Placa', width: 90 },
        { field: 'veiculo', headerName: 'Veículo', width: 150 },
        { field: 'montadora', headerName: 'Montadora', width: 150 },
        { field: 'ano', headerName: 'Ano', width: 150 },
        { field: 'capacidade', headerName: 'Capacidade', width: 120 },
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
                        case 'manutencao':
                            return { color: 'warning' as const, label: 'Manutenção' };
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
                                <FindInPageIcon />
                            </IconButton>
                        </Tooltip>
                        <Tooltip title="Editar">
                            <IconButton>
                                <EditIcon />
                            </IconButton>
                        </Tooltip>
                    </>
                )
            }
        }
    ];

    const data: VehicleData[] = [
        { id: 1, placa: 'ABC-1234', veiculo: 'Fusca', montadora: 'Volkswagen', ano: 1975, capacidade: 4, status: "ativo" },
        { id: 2, placa: 'XYZ-5678', veiculo: 'Civic', montadora: 'Honda', ano: 2020, capacidade: 5, status: "ativo" },
        { id: 3, placa: 'LMN-9101', veiculo: 'Onix', montadora: 'Chevrolet', ano: 2019, capacidade: 5, status: "inativo" },
        { id: 4, placa: 'OPQ-2345', veiculo: 'Corolla', montadora: 'Toyota', ano: 2021, capacidade: 5, status: "ativo" },
        { id: 5, placa: 'RST-6789', veiculo: 'Palio', montadora: 'Fiat', ano: 2018, capacidade: 5, status: "manutencao" }
    ]

    return (
        <Container>
            <DataGrid columns={columns} rows={data}/>
        </Container>
    )
}