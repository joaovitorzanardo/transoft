import { Box, Breadcrumbs, Button, FormControl, Grid, InputLabel, Link, MenuItem, Paper, Select, Stack, TextField, Typography, type SelectChangeEvent } from "@mui/material";
import SideMenu from "../../components/SideManu";
import React from "react";
import type Automaker from "../../models/vehicle/AutomakerPresenter";
import BlockIcon from '@mui/icons-material/Block';
import { getAutomakers } from "../../services/automaker.service";
import { getVehicleModelsByAutomaker } from "../../services/vehiclemodel.service";
import type VehicleModel from "../../models/vehicle/VehicleModel";
import ConfirmationDialog from "../../components/ui/ConfirmationDialog";
import z from "zod";
import { Controller, useForm, type SubmitHandler } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import type VehicleDto from "../../models/vehicle/VehicleDto";
import { disableVehicle, enableVehicle, getVehicleById, saveVehicle, updateVehicle } from "../../services/vehicle.service";
import { useParams } from "react-router";
import MessageAlert from "../../components/ui/MessageAlert";
import CheckIcon from '@mui/icons-material/Check';
import { IMaskMixin } from 'react-imask';
import type VehiclePresenter from "../../models/vehicle/VehiclePresenter";

const VehicleForm = z.object({
    plateNumber: z.string().nonempty({message: "A placa do veículo deve ser informada."}),
    capacity: z.string().nonempty({message: "A capacidade deve ser informada."}),
    vehicleModelId: z.string().nonempty({message: "O modelo do veículo deve ser selecionado."}),
    automakerId: z.string().optional()
});

type IFormInputs = z.infer<typeof VehicleForm>

type DialogType = 'save' | 'disable' | 'enable' | null;
type AlertState = { open: boolean; message: string; severity: 'success' | 'error' } | null;

const MaskedTextField = IMaskMixin(({ inputRef, ...props }) => (
    <TextField {...props} inputRef={inputRef} />
));

export default function VehicleInfoPage() {
    const { vehicleId } = useParams();

    const [vehicle, setVehicle] = React.useState<VehiclePresenter>();
    const [automakers, setAutomakers] = React.useState<Automaker[]>([]);
    const [vehicleModels, setVehicleModels] = React.useState<VehicleModel[]>([]);
    const [openDialog, setOpenDialog] = React.useState<DialogType>(null);
    const [alert, setAlert] = React.useState<AlertState>(null);
    const [loading, setLoading] = React.useState(false);
    const [active, setActive] = React.useState(false);

    const { handleSubmit, control, trigger, setValue, watch, reset } = useForm<IFormInputs>({
        defaultValues: {
            plateNumber: '',
            capacity: '',
            vehicleModelId: '',
            automakerId: ''
        },
        resolver: zodResolver(VehicleForm),
    });

    const selectedAutomaker = watch('automakerId');

    React.useEffect(() => {
        async function getById() {
            if (vehicleId === undefined || vehicleId === 'edit') {
                return;
            }

            const response = await getVehicleById(vehicleId);

            if (response.status !== 200) {
                return;
            }

            const vehicleData = response.data;
            setActive(vehicleData.isActive);
            setVehicle(vehicleData);
            
            setValue('automakerId', vehicleData.vehicleModel.automaker.automakerId);
            setValue('vehicleModelId', vehicleData.vehicleModel.vehicleModelId);
            setValue('plateNumber', vehicleData.plateNumber);
            setValue('capacity', vehicleData.capacity.toString());
        }

        getById();
    }, [vehicleId, setValue, active]);

    React.useEffect(() => {
        async function getAuto() {
            const response = await getAutomakers();
            setAutomakers(response.data);
        }
        getAuto();
    }, []);

    React.useEffect(() => {
        async function getVehicles() {
            if (!selectedAutomaker) {
                setVehicleModels([]);
                setValue('vehicleModelId', '');
                return;
            }

            const response = await getVehicleModelsByAutomaker(selectedAutomaker);
            setVehicleModels(response.data);
        }

        getVehicles();
    }, [selectedAutomaker, setValue]);

    const onSubmit: SubmitHandler<IFormInputs> = async (data) => {
        setOpenDialog(null);
        setLoading(true);
        
        const vehicleDto: VehicleDto = {
            plateNumber: data.plateNumber,
            capacity: Number(data.capacity),
            vehicleModelId: data.vehicleModelId
        };

        try {
            let response = null;
            
            if (vehicleId && vehicleId !== 'edit') {
                response = await updateVehicle(vehicleId, vehicleDto);
            } else {
                response = await saveVehicle(vehicleDto);
            }

            if (response.status === 201) {
                setAlert({ open: true, message: 'Veículo salvo com sucesso!', severity: 'success' });
                reset();
            } else if (response.status === 200) {
                setAlert({ open: true, message: 'Veículo atualizado com sucesso!', severity: 'success' });    
                reset();
            }
        } catch (error: any) {
            setAlert({ open: true, message: error.response?.data.message, severity: 'error' });
        } finally {
            setLoading(false);
        }
    };

    const handleDisable = async () => {
        setOpenDialog(null);
        if (!vehicle) return;

        setLoading(true);
        try {
            const response = await disableVehicle(vehicle.vehicleId);
            if (response.status === 200) {
                setActive(false);
                setAlert({ open: true, message: 'Veículo desabilitado com sucesso!', severity: 'success' });
            }
        } catch(error: any) {
            setAlert({ open: true, message: error.response?.data.message, severity: 'error' });
        } finally {
            setLoading(false);
        }
    };

    const handleEnable = async () => {
        setOpenDialog(null);
        if (!vehicle) return;

        setLoading(true);
        try {
            const response = await enableVehicle(vehicle.vehicleId);
            if (response.status === 200) {
                setActive(true);
                setAlert({ open: true, message: 'Veículo habilitado com sucesso!', severity: 'success' });
            } else {
                setAlert({ open: true, message: 'Erro ao habilitado veículo!', severity: 'error' });
            }
        } catch(error) {
            setAlert({ open: true, message: 'Erro ao habilitado veículo!', severity: 'error' });
        } finally {
            setLoading(false);
        }
    };

    const dialogConfig = {
        save: {
            title: 'Confirmar Cadastro de Veículo',
            message: 'Tem certeza que deseja confirmar o cadastro desse veículo?',
            onConfirm: () => handleSubmit(onSubmit)()
        },
        disable: {
            title: 'Confirmar Desabilitar Veículo',
            message: 'Tem certeza que deseja desabilitar esse veículo?',
            onConfirm: handleDisable
        },
        enable: {
            title: 'Confirmar Habilitar Veículo',
            message: 'Tem certeza que deseja habilitar esse veículo?',
            onConfirm: handleEnable
        }
    };

    const config = openDialog ? dialogConfig[openDialog] : null;

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

    return (
        <Stack direction="row" sx={{ backgroundColor: '#F7F9FA'}}>
            <SideMenu />
            <Stack sx={{ padding: '3rem'}}>
                <Breadcrumbs aria-label="breadcrumb">
                    <Link underline="hover" color="inherit" href="/vehicles">Veículos</Link>
                    <Typography sx={{ color: 'text.primary' }}>Cadastro</Typography>
                </Breadcrumbs>
                <Box sx={{height: 10}}/>
                <Stack direction="row" sx={{display: 'flex', justifyContent: 'space-between', alignItems: 'center'}} >
                    <Typography variant="h4" >Cadastrar Veículo</Typography>
                </Stack>
                <Box sx={{height: 20}}/>
                <FormControl>
                    <Paper sx={{ padding: 3 }} >
                        <Typography variant="h5" >Informações do Veículo</Typography>
                        <Stack sx={{marginTop: 3}}>
                            <Controller
                                name="plateNumber"
                                control={control}
                                render={({ field, fieldState }) => (
                                    <MaskedTextField 
                                        mask="aaa-0a00"
                                        placeholder="BRA-0S19"
                                        {...field}
                                        label="Placa" 
                                        type="text" 
                                        error={!!fieldState.error} 
                                        helperText={fieldState.error?.message}
                                        variant="outlined" 
                                        sx={{width: '100%'}}
                                    />
                                )}
                            />
                        </Stack>
                        <Grid container spacing={2} sx={{marginTop: 3}}>
                            <Grid size={6}>
                                <Controller
                                    name="automakerId"
                                    control={control}
                                    render={({ field }) => (
                                        <FormControl sx={{width: '100%'}}>
                                            <InputLabel id="montadora-label">Montadora</InputLabel>
                                            <Select 
                                                {...field}
                                                sx={{width: '100%'}}
                                                labelId="montadora-label"
                                                label="Montadora"
                                            >
                                                <MenuItem value="">Nenhum</MenuItem>
                                                {automakers.map((automaker) => (
                                                    <MenuItem key={automaker.automakerId} value={automaker.automakerId}>
                                                        {automaker.name}
                                                    </MenuItem> 
                                                ))}
                                            </Select>
                                        </FormControl>
                                    )}
                                />
                            </Grid>
                            <Grid size={6}>
                                <Controller
                                    name="vehicleModelId"
                                    control={control}
                                    render={({ field, fieldState }) => (
                                        <FormControl sx={{width: '100%'}}>
                                            <InputLabel id="modelo-label">Modelo</InputLabel>
                                            <Select 
                                                {...field}
                                                sx={{width: '100%'}}
                                                label="Modelo"
                                                error={!!fieldState.error}
                                            >
                                                <MenuItem value="">Nenhum</MenuItem>
                                                {vehicleModels.map((vehicleModel) => (
                                                    <MenuItem key={vehicleModel.vehicleModelId} value={vehicleModel.vehicleModelId}>
                                                        {vehicleModel.modelName}
                                                    </MenuItem> 
                                                ))}
                                            </Select>
                                        </FormControl>
                                    )}
                                />
                            </Grid>
                        </Grid>
                        <Box sx={{height: 20}}/>
                        <Stack>
                            <Controller
                                name="capacity"
                                control={control}
                                render={({ field, fieldState }) => 
                                    <TextField 
                                        label="Capacidade" 
                                        type="number" 
                                        error={!!fieldState.error} 
                                        helperText={fieldState.error?.message} 
                                        variant="outlined" 
                                        {...field} 
                                        sx={{width: '100%'}}
                                    />
                                }
                            />
                        </Stack>
                    </Paper>
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
                        {
                            vehicleId !== 'edit' && (
                                vehicle?.isActive ? 
                                <Button 
                                    variant="outlined" 
                                    color="error" 
                                    startIcon={<BlockIcon />} 
                                    onClick={() => handleOpenDialog('disable')} 
                                    loading={loading} 
                                    loadingPosition="start"
                                >
                                    Desabilitar
                                </Button> : <Button 
                                    variant="outlined" 
                                    color="success" 
                                    startIcon={<CheckIcon />} 
                                    onClick={() => handleOpenDialog('enable')} 
                                    loading={loading} 
                                    loadingPosition="start"
                                >
                                    Habilitar
                                </Button>
                            )                                
                        }
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