/* eslint-disable @typescript-eslint/no-explicit-any */
import { Box, Breadcrumbs, Button, FormControl, FormGroup, Grid, InputLabel, Link, MenuItem, Paper, Select, Stack, TextField, Typography } from "@mui/material";
import SideMenu from "../../components/SideManu";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import { TimePicker } from '@mui/x-date-pickers/TimePicker';
import BlockIcon from '@mui/icons-material/Block';
import { Controller, useForm, type SubmitHandler } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import z from "zod";
import dayjs, { Dayjs } from "dayjs";
import ControlledCheckbox from "../../components/ui/ControlledCheckbox";
import React from "react";
import type SchoolPresenter from "../../models/SchoolPresenter";
import type DriverPresenter from "../../models/driver/DriverPresenter";
import type VehiclePresenter from "../../models/vehicle/VehiclePresenter";
import { getAllSchools } from "../../services/school.service";
import { getAllEnabledDrivers } from "../../services/driver.service";
import { getAllActiveVehicles } from "../../services/vehicle.service";
import MessageAlert from "../../components/ui/MessageAlert";
import ConfirmationDialog from "../../components/ui/ConfirmationDialog";
import { disableRoute, enableRoute, getRouteById, saveRoute, updateRoute } from "../../services/route.service";
import type RouteDto from "../../models/route/RouteDto";
import { useParams } from "react-router";
import type RouteUpdateDto from "../../models/route/RouteUpdateDto";
import CheckIcon from '@mui/icons-material/Check';
import UpdateRoutesConfirmationDialog from "../../components/ui/UpdateRoutesConfirmationDialog";

const dayjsSchema = z.custom<Dayjs | null>(
    (val) => val === null || dayjs.isDayjs(val),
    { message: "Horário inválido" }
).nullable();

const RouteForm = z.object({
    name: z.string().nonempty({ message: "O nome deve ser informado" }),
    schoolId: z.string({ message: "A escola deve ser informada" }),
    defaultDriverId: z.string().nonempty({ message: "O motorista padrão deve ser informado" }),
    defaultVehicleId: z.string().nonempty({ message: "O veículo padrão deve ser informado" }),
    dtStartTime: dayjsSchema,
    dtEndTime: dayjsSchema,
    rtStartTime: dayjsSchema,
    rtEndTime: dayjsSchema,
    monday: z.boolean().nonoptional(),
    tuesday: z.boolean().nonoptional(),
    wednesday: z.boolean().nonoptional(),
    thursday: z.boolean().nonoptional(),
    friday: z.boolean().nonoptional(),
});

type IFormInputs = z.infer<typeof RouteForm>

type DialogType = 'save' | 'disable' | 'enable' | 'update' | null;
type AlertState = { open: boolean; message: string; severity: 'success' | 'error' } | null;

export default function RouteInfoPage() {
    const { routeId } = useParams();

    const [schools, setSchools] = React.useState<SchoolPresenter[]>([]);
    const [drivers, setDrivers] = React.useState<DriverPresenter[]>([]);
    const [vehicles, setVehicles] = React.useState<VehiclePresenter[]>([]);

    const [openDialog, setOpenDialog] = React.useState<DialogType>(null);
    const [alert, setAlert] = React.useState<AlertState>(null);
    const [loading, setLoading] = React.useState(false);
    const [updateItineraries, setUpdateItineraries] = React.useState(false);

    const [isEnabled, setIsEnabled] = React.useState(false);

    React.useEffect(() => {
        async function getSchools() {
            const response = await getAllSchools();
            setSchools(response.data);
        }
        getSchools();
    }, []);

    React.useEffect(() => {
        async function getDrivers() {
            const response = await getAllEnabledDrivers();
            setDrivers(response.data);
        }

        getDrivers();
    }, []);

    React.useEffect(() => {
        async function getVehicles() {
            const response = await getAllActiveVehicles();
            setVehicles(response.data);
        }
        getVehicles();
    }, []);

    const { handleSubmit, control, trigger, setValue, reset } = useForm<IFormInputs>({
        defaultValues: {
            name: '',
            schoolId: '',
            defaultDriverId: '',
            defaultVehicleId: '',
            dtStartTime: null,
            dtEndTime: null,
            rtStartTime: null,
            rtEndTime: null,
            monday: false,
            tuesday: false,
            wednesday: false,
            thursday: false,
            friday: false
        },
        resolver: zodResolver(RouteForm),
        mode: 'onSubmit',
        reValidateMode: 'onSubmit',
    });

    React.useEffect(() => {
        async function getById() {
            if (routeId === undefined || routeId === 'edit') {
                return;
            }

            const response = await getRouteById(routeId);

            if (response.status !== 200) {
                return;
            }

            const routeData = response.data;
            setIsEnabled(routeData.active);

            setValue('name', routeData.name);
            setValue('schoolId', routeData.school.schoolId);
            setValue('defaultDriverId', routeData.defaultDriver.driverId);
            setValue('defaultVehicleId', routeData.defaultVehicle.vehicleId);
            setValue('monday', routeData.daysOfWeek.monday);
            setValue('tuesday', routeData.daysOfWeek.tuesday);
            setValue('wednesday', routeData.daysOfWeek.wednesday);
            setValue('thursday', routeData.daysOfWeek.thursday);
            setValue('friday', routeData.daysOfWeek.friday);
            setValue('dtStartTime', routeData.departureTrip.startTime ? dayjs(routeData.departureTrip.startTime, 'HH:mm') : null);
            setValue('dtEndTime', routeData.departureTrip.endTime ? dayjs(routeData.departureTrip.endTime, 'HH:mm') : null);
            setValue('rtStartTime', routeData.returnTrip.startTime ? dayjs(routeData.returnTrip.startTime, 'HH:mm') : null);
            setValue('rtEndTime', routeData.returnTrip.endTime ? dayjs(routeData.returnTrip.endTime, 'HH:mm') : null);
        }

        getById();
    }, [routeId, setValue]);

    const onSubmit: SubmitHandler<IFormInputs> = async (data) => {
        setOpenDialog(null);
        setLoading(true);

        try {
            let response = null;
            if (routeId && routeId !== 'edit') {
                const routeUpdateDto: RouteUpdateDto = {
                    name: data.name,
                    defaultDriverId: data.defaultDriverId,
                    defaultVehicleId: data.defaultVehicleId,
                    departureTrip: {
                        startTime: data.dtStartTime ? data.dtStartTime.format('HH:mm') : '',
                        endTime: data.dtEndTime ? data.dtEndTime.format('HH:mm') : '',
                    },
                    returnTrip: {
                        startTime: data.rtStartTime ? data.rtStartTime.format('HH:mm') : '',
                        endTime: data.rtEndTime ? data.rtEndTime.format('HH:mm') : '',
                    },
                    daysOfWeek: {
                        monday: data.monday,
                        tuesday: data.tuesday,
                        wednesday: data.wednesday,
                        thursday: data.thursday,
                        friday: data.friday
                    },
                    updateItineraries: updateItineraries
                };

                response = await updateRoute(routeId, routeUpdateDto);
            } else {
                const routeDto: RouteDto = {
                    name: data.name,
                    schoolId: data.schoolId,
                    defaultDriverId: data.defaultDriverId,
                    defaultVehicleId: data.defaultVehicleId,
                    departureTrip: {
                        startTime: data.dtStartTime ? data.dtStartTime.format('HH:mm') : '',
                        endTime: data.dtEndTime ? data.dtEndTime.format('HH:mm') : '',
                    },
                    returnTrip: {
                        startTime: data.rtStartTime ? data.rtStartTime.format('HH:mm') : '',
                        endTime: data.rtEndTime ? data.rtEndTime.format('HH:mm') : '',
                    },
                    daysOfWeek: {
                        monday: data.monday,
                        tuesday: data.tuesday,
                        wednesday: data.wednesday,
                        thursday: data.thursday,
                        friday: data.friday
                    }
                };

                response = await saveRoute(routeDto);
            }

            if (response.status === 201) {
                setAlert({ open: true, message: 'Motorista salvo com sucesso!', severity: 'success' });
                reset();
            } else if (response.status === 200) {
                setAlert({ open: true, message: 'Motorista atualizado com sucesso!', severity: 'success' });
                reset();
            }
        } catch (error: any) {
            setAlert({ open: true, message: error.response?.data.message, severity: 'error' });
        } finally {
            setLoading(false);
        }
    }

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

    const enable = async () => {
        setOpenDialog(null);
        try {
            if (routeId === undefined) {
                return;
            }
            await enableRoute(routeId);
            setIsEnabled(true);
            setAlert({ open: true, message: 'Rota habilitada com sucesso!', severity: 'success' });
        } catch (error: any) {
            setAlert({ open: true, message: error.response?.data.message, severity: 'error' });
        }
    }

    const disable = async () => {
        setOpenDialog(null);
        try {
            if (routeId === undefined) {
                return;
            }
            await disableRoute(routeId);
            setIsEnabled(false);
            setAlert({ open: true, message: 'Rota desabilitada com sucesso!', severity: 'success' });
        } catch (error: any) {
            setAlert({ open: true, message: error.response?.data.message, severity: 'error' });
        }
    }

    const dialogConfig = {
        save: {
            title: 'Confirmar Cadastro de Rota',
            message: 'Tem certeza que deseja confirmar o cadastro desse rota?',
            onConfirm: () => handleSubmit(onSubmit)()
        },
        update: {
            title: 'Confirmar Atualização da Rota',
            message: 'Tem certeza que deseja atualizar as informações dessa rota?',
            onConfirm: () => handleSubmit(onSubmit)()
        },
        disable: {
            title: 'Confirmar Desabilitar Rota',
            message: 'Tem certeza que deseja desabilitar esse rota?',
            onConfirm: () => disable()
        },
        enable: {
            title: 'Confirmar Habilitar Rota',
            message: 'Tem certeza que deseja habilitar esse rota?',
            onConfirm: () => enable()
        }
    };

    const config = openDialog ? dialogConfig[openDialog] : null;

    return (
        <Stack direction="row" sx={{ backgroundColor: '#F7F9FA', height: '100vh', overflow: 'hidden' }}>
            <SideMenu />
            <Stack sx={{ padding: '3rem', flex: 1, overflowY: 'auto' }}>
                <Breadcrumbs aria-label="breadcrumb">
                    <Link underline="hover" color="inherit" href="/routes">Rotas</Link>
                    <Typography sx={{ color: 'text.primary' }}>Cadastro</Typography>
                </Breadcrumbs>
                <Stack direction="row" sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginTop: 1 }} >
                    <Typography variant="h4" >Cadastrar Rota</Typography>
                </Stack>
                <FormControl component="form">
                    <Paper sx={{ padding: 3, marginTop: 2 }} >
                        <Typography variant="h5" >Informações Gerais</Typography>
                        <Controller
                            name="name"
                            control={control}
                            render={({ field, fieldState }) => <TextField label="Nome" error={!!fieldState.error} helperText={fieldState.error?.message} variant="outlined" {...field} sx={{ width: '100%', marginTop: 3 }} />}
                        />
                        <Grid container spacing={2} sx={{ marginTop: 3 }}>
                            <Grid size={6}>
                                <Controller
                                    name="schoolId"
                                    control={control}
                                    render={({ field, fieldState }) => (
                                        <FormControl sx={{ width: '100%' }}>
                                            <InputLabel id="school-label">Escola</InputLabel>
                                            <Select
                                                {...field}
                                                sx={{ width: '100%' }}
                                                label="Escola"
                                                error={!!fieldState.error}
                                            >
                                                <MenuItem value="">Nenhum</MenuItem>
                                                {schools.map((school) => (
                                                    <MenuItem value={school.schoolId}>
                                                        {school.name}
                                                    </MenuItem>
                                                ))}
                                            </Select>
                                        </FormControl>
                                    )}
                                />
                            </Grid>
                            <Grid size={6}>
                                <Controller
                                    name="defaultDriverId"
                                    control={control}
                                    render={({ field, fieldState }) => (
                                        <FormControl sx={{ width: '100%' }}>
                                            <InputLabel id="defaultDriver-label">Motorista Padrão</InputLabel>
                                            <Select
                                                {...field}
                                                sx={{ width: '100%' }}
                                                label="Motorista Padrão"
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
                        </Grid>
                        <Controller
                            name="defaultVehicleId"
                            control={control}
                            render={({ field, fieldState }) => (
                                <FormControl sx={{ width: '100%', marginTop: 3 }}>
                                    <InputLabel id="defaultVehicle-label">Veículo Padrão</InputLabel>
                                    <Select
                                        {...field}
                                        sx={{ width: '100%' }}
                                        label="Veículo Padrão"
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
                    </Paper>
                    <Box sx={{ height: 20 }} />
                    <Paper sx={{ padding: 3 }} >
                        <Typography variant="h5" >Viagem de Ida</Typography>
                        <LocalizationProvider dateAdapter={AdapterDayjs}>
                            <Stack direction="row" spacing={2} sx={{ marginTop: 3 }}>
                                <Controller
                                    name="dtStartTime"
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
                                    name="dtEndTime"
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
                    <Box sx={{ height: 20 }} />
                    <Paper sx={{ padding: 3 }} >
                        <Typography variant="h5" >Viagem de Volta</Typography>
                        <LocalizationProvider dateAdapter={AdapterDayjs}>
                            <Stack direction="row" spacing={2} sx={{ marginTop: 3 }}>
                                <Controller
                                    name="rtStartTime"
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
                                    name="rtEndTime"
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
                    <Box sx={{ height: 20 }} />
                    <Paper sx={{ padding: 3 }} >
                        <Typography variant="h5" >Dias da Semana</Typography>
                        <FormControl component="fieldset" sx={{ marginTop: 3 }}>
                            <FormGroup row>
                                <ControlledCheckbox
                                    control={control}
                                    name="monday"
                                    label="Segunda"
                                />
                                <ControlledCheckbox
                                    control={control}
                                    name="tuesday"
                                    label="Terça"
                                />
                                <ControlledCheckbox
                                    control={control}
                                    name="wednesday"
                                    label="Quarta"
                                />
                                <ControlledCheckbox
                                    control={control}
                                    name="thursday"
                                    label="Quinta"
                                />
                                <ControlledCheckbox
                                    control={control}
                                    name="friday"
                                    label="Sexta"
                                />
                            </FormGroup>
                        </FormControl>
                    </Paper>
                    <Box sx={{ height: 20 }} />
                    <Stack direction="row" justifyContent="flex-start" spacing={2}>
                        <Button
                            variant="contained"
                            color="primary"
                            onClick={() => routeId !== 'edit' ? handleOpenDialog('update') : handleOpenDialog('save')}
                            loading={loading}
                            loadingPosition="start"
                        >
                            Salvar
                        </Button>
                        {
                            routeId !== 'edit' && (
                                isEnabled ?
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
                    {config && openDialog !== 'update' && (
                        <ConfirmationDialog
                            title={config.title}
                            message={config.message}
                            open={openDialog !== null}
                            onClose={() => setOpenDialog(null)}
                            onConfirm={config.onConfirm}
                        />
                    )}
                    {config && openDialog === 'update' && (
                        <UpdateRoutesConfirmationDialog
                            title={config.title}
                            message={config.message}
                            open={openDialog !== null}
                            onClose={() => setOpenDialog(null)}
                            onConfirm={config.onConfirm}
                            updateItineraries={updateItineraries}
                            setUpdateItineraries={setUpdateItineraries}
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