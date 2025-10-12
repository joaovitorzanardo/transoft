import { Box, Breadcrumbs, Button, FormControl, InputLabel, Link, MenuItem, Paper, Select, Stack, Typography } from "@mui/material"
import SideMenu from "../../components/SideManu";
import PhoneNumberInput from "../../components/ui/PhoneNumberInput";
import EnderecoPaper from "../../components/ui/EnderecoPaper";
import PersonalInfoPaper from "../../components/ui/PersonalInfoPaper";
import z from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import { Controller, useForm, type SubmitHandler } from "react-hook-form";
import React from "react";
import BlockIcon from '@mui/icons-material/Block';
import { getAllRoutes } from "../../services/route.service";
import type RouteSelectPresenter from "../../models/route/RouteSelectPresenter";
import { getPassengersById, savePassenger } from "../../services/passenger.service";
import type PassengerDto from "../../models/PassengerDto";
import type AddressDto from "../../models/address/AddressDto";
import ConfirmationDialog from "../../components/ui/ConfirmationDialog";
import MessageAlert from "../../components/ui/MessageAlert";
import { useParams } from "react-router";
import type PassengerPresenter from "../../models/PassengerPresenter";

const PassengerForm = z.object({
    name: z.string().nonempty({message: "O nome deve ser informado"}),
    email: z.email({message: "Formato do email inválido"}),
    ddd: z.string().nonempty({message: "O DDD deve ser informado"}),
    number: z.string().nonempty({message: "O número deve ser informado"}),
    routeId: z.string().nonempty({message: "A rota deve ser informada"}),
    cep: z.string().nonempty({message: "O CEP deve ser informado"})
                    .length(9, {message: "O CEP deve ter 8 caracteres"})
                    .regex(/^\d{5}-\d{3}$/, {message: "O CEP deve conter apenas números"}),
    estado: z.string().nonempty({message: "O estado deve ser informado"}),
    cidade: z.string().nonempty({message: "A cidade deve ser informada"}),
    bairro: z.string().nonempty({message: "O bairro deve ser informado"}),
    endereco: z.string().nonempty({message: "O endereço deve ser informado"}),
    numero: z.string().nonempty({message: "O número deve ser informado"}),
    complemento: z.string().optional()
});

type IFormInputs = z.infer<typeof PassengerForm>

type DialogType = 'save' | 'disable' | 'enable' | null;
type AlertState = { open: boolean; message: string; severity: 'success' | 'error' } | null;

export default function PassengerInfoPage() {
    const { passengerId } = useParams();
    
    const [passenger, setPassenger] = React.useState<PassengerPresenter>();
    const [openDialog, setOpenDialog] = React.useState<DialogType>(null);
    const [alert, setAlert] = React.useState<AlertState>(null);
    const [loading, setLoading] = React.useState(false);

    const [routes, setRoutes] = React.useState<RouteSelectPresenter[]>([]);

    const { handleSubmit, control, setValue, trigger, reset, watch } = useForm<IFormInputs>({
        defaultValues: {
            name: '',
            email: '',
            ddd: '',
            number: '',
            routeId: '',
            cep: '',
            estado: '',
            cidade: '',
            bairro: '',
            endereco: '',
            numero: '',
            complemento: ''
        },
        resolver: zodResolver(PassengerForm),
    });

    React.useEffect(() => {
        async function getById() {
            if (passengerId === undefined || passengerId === 'edit') {
                return;
            }

            const response = await getPassengersById(passengerId);

            if (response.status !== 200) {
                return;
            }

            const passengerData = response.data;
            //setActive(vehicleData.isActive);
            setPassenger(passengerData);
            
            setValue('name', passengerData.name);
            setValue('email', passengerData.email);
            setValue('ddd', passengerData.phoneNumber.ddd);
            setValue('number', passengerData.phoneNumber.number);
            setValue('routeId', passengerData.routeId);
            setValue('cep', passengerData.address.cep);
            setValue('estado', passengerData.address.uf);
            setValue('cidade', passengerData.address.city);
            setValue('bairro', passengerData.address.district);
            setValue('endereco', passengerData.address.street);
            setValue('numero', passengerData.address.number);
            setValue('complemento', passengerData.address.complement);
        }

        getById();
    }, [passengerId, setValue]);

    const handleOpenDialog = async (type: DialogType) => {
        if (type === 'save') {
            const isValid = await trigger();
            if (isValid) {
                setOpenDialog(type);
            }
        } else {
            setOpenDialog(type);
        }
    };

    const onSubmit: SubmitHandler<IFormInputs> = async (data) => {        
        setOpenDialog(null);
        setLoading(true);

        const address: AddressDto = {
            cep: data.cep,
            street: data.endereco,
            district: data.bairro,
            number: Number(data.numero),
            city: data.cidade,
            uf: data.estado,
            complement: data.complemento
        }
        
        const passengerDto: PassengerDto = {
            name: data.name,
            email: data.email,
            routeId: data.routeId,
            phoneNumber: {
                ddd: data.ddd,
                number: data.number
            },
            address: address
        };

        try {
            const response = await savePassenger(passengerDto);
            if (response.status === 201) {
                setAlert({ open: true, message: 'Passageiro salvo com sucesso!', severity: 'success' });
                reset();
            } else {
                setAlert({ open: true, message: 'Erro ao salvar passageiro!', severity: 'error' });
            }
        } catch(error) {
            console.log(error);
            setAlert({ open: true, message: 'Erro ao salvar passageiro!', severity: 'error' });
        } finally {
            setLoading(false);
        }
    }

    React.useEffect(() => {
        async function getAll() {
            const response = await getAllRoutes();
            setRoutes(response.data);
        }

        getAll();
    }, []);
    
    const dialogConfig = {
        save: {
            title: 'Confirmar Cadastro de Passageiro',
            message: 'Tem certeza que deseja confirmar o cadastro desse passageiro?',
            onConfirm: () => handleSubmit(onSubmit)()
        },
        disable: {
            title: 'Confirmar Desabilitar Passageiro',
            message: 'Tem certeza que deseja desabilitar esse passageiro?',
            onConfirm: () => console.log('Passageiro Desabilitado')
        },
        enable: {
            title: 'Confirmar Habilitar Passageiro',
            message: 'Tem certeza que deseja habilitar esse passageiro?',
            onConfirm: () => console.log('Passageiro Desabilitado')
        }
    };

    const config = openDialog ? dialogConfig[openDialog] : null;

    return (
        <Stack direction="row" sx={{ backgroundColor: '#F7F9FA'}}>
            <SideMenu />
            <Stack sx={{ padding: '3rem'}}>
                <Breadcrumbs aria-label="breadcrumb">
                    <Link underline="hover" color="inherit" href="/passengers">Passageiros</Link>
                    <Typography sx={{ color: 'text.primary' }}>Cadastro</Typography>
                </Breadcrumbs>
                <Box sx={{height: 10}}/>
                <Stack direction="row" sx={{display: 'flex', justifyContent: 'space-between', alignItems: 'center'}} >
                    <Typography variant="h4" >Cadastrar Passageiro</Typography>
                </Stack>
                <Box sx={{height: 20}}/>
                <FormControl onSubmit={handleSubmit(onSubmit)} component="form">
                    <PersonalInfoPaper control={control}/>
                    <Box sx={{height: 30}}/>
                    <PhoneNumberInput control={control}/>
                    <Box sx={{height: 30}}/>
                    <Paper sx={{ padding: 3 }}>
                        <Typography variant="h5" >Rota</Typography>
                        <Controller
                            name="routeId"
                            control={control}
                            render={({ field, fieldState }) => (
                                <FormControl sx={{width: '100%', marginTop: 3}}>
                                    <InputLabel id="rota-label">Rota</InputLabel>
                                    <Select 
                                        {...field}
                                        sx={{width: '100%'}}
                                        label="Rota"
                                        error={!!fieldState.error}
                                    >
                                        <MenuItem value="">Nenhum</MenuItem>
                                        {routes.map((route) => (
                                            <MenuItem value={route.routeId}>
                                                {route.name}
                                            </MenuItem> 
                                        ))}
                                    </Select>
                                </FormControl>
                            )}
                        />
                    </Paper>
                    <Box sx={{height: 30}}/>
                    <EnderecoPaper control={control} setValue={setValue}/>
                    <Box sx={{height: 30}}/>
                    <Stack direction="row" justifyContent="flex-start" spacing={2}>
                        <Button 
                            variant="contained" 
                            color="primary" 
                            onClick={() => handleOpenDialog('save')} 
                            loading={loading} 
                            loadingPosition="start"
                        >
                            Salvar
                        </Button>
                        <Button variant="outlined" color="error" startIcon={<BlockIcon />}>Desabilitar</Button>
                    </Stack>
                    {config && (
                        <ConfirmationDialog
                            title={config.title}
                            message={config.message}
                            open={openDialog !== null} 
                            onClose={() => setOpenDialog(null)}
                            onConfirm={config.onConfirm}
                        />
                    )}
                    {alert && (
                        <MessageAlert 
                            open={alert.open}
                            message={alert.message}
                            severity={alert.severity}
                            onClose={() => setAlert(null)}
                        />
                    )}
                </FormControl>
            </Stack>
        </Stack>
    );
}