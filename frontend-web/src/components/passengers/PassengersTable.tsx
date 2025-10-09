import { Chip, Container, IconButton, Tooltip } from "@mui/material";
import { DataGrid, type GridColDef  } from '@mui/x-data-grid';
import FindInPageIcon from '@mui/icons-material/FindInPage';
import EditIcon from '@mui/icons-material/Edit';
import { useNavigate } from "react-router";

interface PassengerData {
    id: number;
    nome: string;
    email: string;
    telefone: string;
    escola: string;
    endereco: string;
    status: React.ReactNode;
}

export default function PassengersTable() {
    const navigate = useNavigate();

    const navigateToPassengerInfo = () => {
        navigate('/passengers/edit');
    }

    const columns: GridColDef[] = [
        { field: 'nome', headerName: 'Nome', width: 90 },
        { field: 'email', headerName: 'Email', width: 150 },
        { field: 'telefone', headerName: 'Telefone', width: 150 },
        { field: 'escola', headerName: 'Escola', width: 150 },
        { field: 'endereco', headerName: 'Endereço', width: 120 },
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
                        case 'pendente':
                            return { color: 'warning' as const, label: 'Pendente' };
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
                                <EditIcon onClick={navigateToPassengerInfo}/>
                            </IconButton>
                        </Tooltip>
                    </>
                )
            }
        }
    ];

    const data: PassengerData[] = [
        { id: 1, nome: 'João Vitor', email: 'joao.zanardo@gmail.com', telefone: '(54) 99203-1028', escola: 'URI Erechim - Campus II', endereco: 'Av. Germano Hoffman', status: "ativo" },
        { id: 2, nome: 'Lucas Batista', email: 'lucas.batisa@gmail.com', telefone: '(54) 99203-1028', escola: 'URI Erechim - Campus II', endereco: 'Av. Germano Hoffman', status: "ativo" },
        { id: 3, nome: 'Pedro Henrique', email: 'pedro.piet@gmail.com', telefone: '(54) 99203-1028', escola: 'URI Erechim - Campus II', endereco: 'Av. Germano Hoffman', status: "inativo" },
        { id: 4, nome: 'Jair Bolsonaro', email: 'jair.bolso@gmail.com', telefone: '(54) 99203-1028', escola: 'URI Erechim - Campus II', endereco: 'Av. Germano Hoffman', status: "ativo" },
        { id: 5, nome: 'João Zich', email: 'joao.zich@gmail.com', telefone: '(54) 99203-1028', escola: 'URI Erechim - Campus II', endereco: 'Av. Germano Hoffman', status: "pendente" }
    ]

    return (
        <Container>
            <DataGrid columns={columns} rows={data}/>
        </Container>
    )
}