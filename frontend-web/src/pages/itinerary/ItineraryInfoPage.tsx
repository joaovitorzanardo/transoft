import { Box, Breadcrumbs, Button, FormControl, Grid, InputLabel, Link, MenuItem, Paper, Select, Stack, Typography, type SelectChangeEvent } from "@mui/material";
import SideMenu from "../../components/SideManu";
import { TimePicker } from "@mui/x-date-pickers/TimePicker";
import React from "react";
import CancelIcon from '@mui/icons-material/Cancel';
import type DriverPresenter from "../../models/driver/DriverPresenter";
import type VehiclePresenter from "../../models/vehicle/VehiclePresenter";
import { getAllDrivers } from "../../services/driver.service";
import { getAllVehicles } from "../../services/vehicle.service";
import { Controller, useForm, type SubmitHandler } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import z from "zod";
import type { Dayjs } from "dayjs";
import dayjs from "dayjs";
import { useParams } from "react-router";
import { cancelItinerary, getItineraryById, updateItinerary } from "../../services/itinerary.service";
import MessageAlert from "../../components/ui/MessageAlert";
import ConfirmationDialog from "../../components/ui/ConfirmationDialog";
import type ItineraryDto from "../../models/itinerary/ItineraryDto";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";

const dayjsSchema = z.custom<Dayjs | null>(
    (val) => val === null || dayjs.isDayjs(val),
    { message: "Horário inválido" }
).nullable();

const RouteForm = z.object({
    driverId: z.string().nonempty({message: "O motorista padrão deve ser informado"}),
    vehicleId: z.string().nonempty({message: "O veículo padrão deve ser informado"}),
    startTime: dayjsSchema,
    endTime: dayjsSchema,
});

type IFormInputs = z.infer<typeof RouteForm>

type DialogType = 'save' | 'disable' | null;
type AlertState = { open: boolean; message: string; severity: 'success' | 'error' } | null;

export default function ItineraryInfoPage() {
    const { itineraryId } = useParams();

    const [drivers, setDrivers] = React.useState<DriverPresenter[]>([]);
    const [vehicles, setVehicles] = React.useState<VehiclePresenter[]>([]);

    const [openDialog, setOpenDialog] = React.useState<DialogType>(null);
    const [alert, setAlert] = React.useState<AlertState>(null);
    const [loading, setLoading] = React.useState(false);

    React.useEffect(() => {
            async function getDrivers() {
                const response = await getAllDrivers();
                setDrivers(response.data);
            }
            
            getDrivers();
        }, []);
    
    React.useEffect(() => {
        async function getVehicles() {
            const response = await getAllVehicles();
            setVehicles(response.data);
        }
        getVehicles();
    }, []);

    const { handleSubmit, control, trigger, setValue, reset } = useForm<IFormInputs>({
        defaultValues: {
            driverId: '',
            vehicleId: '',
            startTime: null,
            endTime: null
        },
        resolver: zodResolver(RouteForm),
    });

    React.useEffect(() => {
        async function getById() {
            if (itineraryId === undefined || itineraryId === 'edit') {
                return;
            }

            const response = await getItineraryById(itineraryId);

            if (response.status !== 200) {
                return;
            }

            const itineraryData = response.data;
            
            setValue('driverId', itineraryData.driver.driverId);
            setValue('vehicleId', itineraryData.vehicle.vehicleId);
            setValue('startTime', itineraryData.startTime ? dayjs(itineraryData.startTime, 'HH:mm') : null);
            setValue('endTime', itineraryData.endTime ? dayjs(itineraryData.endTime, 'HH:mm') : null);
        }

        getById();
    }, [itineraryId, setValue]);

    const onSubmit: SubmitHandler<IFormInputs> = async (data) => {        
        setOpenDialog(null);
        setLoading(true);
        
        const itineraryDto: ItineraryDto = {
            driverId: data.driverId,
            vehicleId: data.vehicleId,
            startTime: data.startTime ? data.startTime.format('HH:mm') : null,
            endTime: data.endTime ? data.endTime.format('HH:mm') : null,
        };

        try {
            if (itineraryId === undefined) {
                return;
            }
            const response = await updateItinerary(itineraryId, itineraryDto);
            if (response.status === 200) {
                setAlert({ open: true, message: 'Itinerário salvo com sucesso!', severity: 'success' });
                reset();
            } else {
                setAlert({ open: true, message: 'Erro ao salvar itinerário!', severity: 'error' });
            }
        } catch(error) {
            console.log(error);
            setAlert({ open: true, message: 'Erro ao salvar itinerário!', severity: 'error' });
        } finally {
            setLoading(false);
        }
    }

    const cancel = async () => {
        setOpenDialog(null);
        setLoading(true);
        if (itineraryId === undefined) {
            return;
        }
        try {
            if (itineraryId === undefined) {
                return;
            }
            const response = await cancelItinerary(itineraryId);
            if (response.status === 200) {
                setAlert({ open: true, message: 'Itinerário cancelado com sucesso!', severity: 'success' });
                reset();
            } else {
                setAlert({ open: true, message: 'Erro ao cancelar itinerário!', severity: 'error' });
            }
        } catch(error) {
            console.log(error);
            setAlert({ open: true, message: 'Erro ao cancelar itinerário!', severity: 'error' });
        } finally {
            setLoading(false);
        }
    }

    const dialogConfig = {
        save: {
            title: 'Confirmar Atualizar Itinerário',
            message: 'Tem certeza que deseja atualizar esse itinerário?',
            onConfirm: () => handleSubmit(onSubmit)()
        },
        disable: {
            title: 'Confirmar Cancelar Itinerário',
            message: 'Tem certeza que deseja cancelar esse itinerário?',
            onConfirm: () => cancel()
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
                    <Link underline="hover" color="inherit" href="/itineraries">Itinerários</Link>
                    <Typography sx={{ color: 'text.primary' }}>Editar</Typography>
                </Breadcrumbs>
                <Box sx={{height: 10}}/>
                <Stack direction="row" sx={{display: 'flex', justifyContent: 'space-between', alignItems: 'center'}} >
                    <Typography variant="h4" >Editar Itinerário</Typography>
                </Stack>
                <Box sx={{height: 20}}/>
                <FormControl component="form">
                    <Paper sx={{ padding: 3 }}>
                        <Typography variant="h5" >Informações Gerais</Typography>
                        <Grid container spacing={2} sx={{marginTop: 3}}>
                            <Grid size={6}>
                                <Controller
                                        name="driverId"
                                        control={control}
                                        render={({ field, fieldState }) => (
                                            <FormControl sx={{width: '100%'}}>
                                                <InputLabel id="driverId-label">Motorista Padrão</InputLabel>
                                                <Select 
                                                    {...field}
                                                    sx={{width: '100%'}}
                                                    label="Motorista"
                                                    error={!!fieldState.error}
                                                >
                                                    <MenuItem value="">Nenhum</MenuItem>
                                                    {drivers.map((driver) => (
                                                        <MenuItem value={driver.driverId}>
                                                            {driver.name}
                                                        </MenuItem> 
                                                    ))}
                                                </Select>
                                            </FormControl>
                                        )}
                                    />
                            </Grid>
                            <Grid size={6}>
                                <Controller
                                    name="vehicleId"
                                    control={control}
                                    render={({ field, fieldState }) => (
                                        <FormControl sx={{width: '100%'}}>
                                            <InputLabel id="vehicleId-label">Veículo Padrão</InputLabel>
                                            <Select 
                                                {...field}
                                                sx={{width: '100%'}}
                                                label="Veículo"
                                                error={!!fieldState.error}
                                            >
                                                <MenuItem value="">Nenhum</MenuItem>
                                                {vehicles.map((vehicle) => (
                                                    <MenuItem value={vehicle.vehicleId}>
                                                        {vehicle.plateNumber} {vehicle.vehicleModel.modelName} - {vehicle.vehicleModel.modelYear}
                                                    </MenuItem> 
                                                ))}
                                            </Select>
                                        </FormControl>
                                    )}
                                />
                            </Grid>
                        </Grid>
                    </Paper>
                    <Box sx={{height: 30}}/>
                    <Paper sx={{ padding: 3 }}>
                        <Typography variant="h5" >Horário</Typography>
                        <LocalizationProvider dateAdapter={AdapterDayjs}>
                            <Stack direction="row" spacing={2} sx={{marginTop: 3}}>
                                <Controller
                                    name="startTime"
                                    control={control}
                                    render={({ field, fieldState }) => (
                                        <TimePicker
                                            label="Início"
                                            value={field.value}
                                            onChange={(newValue) => field.onChange(newValue)}
                                            slotProps={{
                                                textField: {
                                                    error: !!fieldState.error,
                                                    helperText: fieldState.error?.message,
                                                },
                                            }}
                                        />
                                    )}
                                />
                                <Controller
                                    name="endTime"
                                    control={control}
                                    render={({ field, fieldState }) => (
                                        <TimePicker
                                            label="Fim"
                                            value={field.value}
                                            onChange={(newValue) => field.onChange(newValue)}
                                            slotProps={{
                                                textField: {
                                                    error: !!fieldState.error,
                                                    helperText: fieldState.error?.message,
                                                },
                                            }}
                                        />
                                    )}
                                />
                            </Stack>
                        </LocalizationProvider>                        
                    </Paper>
                    <Box sx={{height: 20}}/>
                    <Stack direction="row" justifyContent="flex-start" spacing={2}>
                        <Button 
                            variant="contained" 
                            color="primary" 
                            onClick={() => handleOpenDialog('save')} 
                            loading={loading} 
                            loadingPosition="start"
                        >
                            Atualizar
                        </Button>
                        <Button 
                            variant="outlined" 
                            color="error" 
                            onClick={() => handleOpenDialog('disable')}
                            loading={loading} 
                            loadingPosition="start"
                            startIcon={<CancelIcon />}
                        >
                            Cancelar
                        </Button>
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
    )
}