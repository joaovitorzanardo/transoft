/* eslint-disable @typescript-eslint/no-explicit-any */
import { Box, Breadcrumbs, Button, FormControl, Grid, Link, Paper, Stack, Typography } from "@mui/material";
import SideMenu from "../../components/SideManu";
import PersonalInfoPaper from "../../components/ui/PersonalInfoPaper";
import PhoneNumberInput from "../../components/ui/PhoneNumberInput";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import { DatePicker } from "@mui/x-date-pickers/DatePicker";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import z from "zod";
import { Controller, useForm, type SubmitHandler } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import BlockIcon from '@mui/icons-material/Block';
import React from "react";
import { disableDriver, enableDriver, getDriverById, saveDriver, updateDriver } from "../../services/driver.service";
import type DriverDto from "../../models/driver/DriverDto";
import MessageAlert from "../../components/ui/MessageAlert";
import ConfirmationDialog from "../../components/ui/ConfirmationDialog";
import dayjs, { Dayjs } from "dayjs";
import { useParams } from "react-router";
import CheckIcon from '@mui/icons-material/Check';
import { MaskedTextField } from "../../components/TextMaskCustom";

const DriverForm = z.object({
    name: z.string().nonempty({ message: "O nome deve ser informado" }),
    email: z.email({ message: "Formato do email inválido" }),
    ddd: z.string().nonempty({ message: "O DDD deve ser informado" }),
    number: z.string().nonempty({ message: "O número deve ser informado" }),
    cnhNumber: z.string()
        .nonempty({ message: "A CNH deve ter ser informada" })
        .length(11, { message: "A CNH deve ter 11 caracteres" })
        .regex(/^\d+$/, { message: "A CNH deve conter apenas números" }),
    cnhExpirationDate: z.custom<Dayjs>(
        (val) => dayjs.isDayjs(val),
        { message: "Data inválida" }
    ).nullable()
}).refine(
    (data) => data.cnhExpirationDate !== null,
    {
        message: "A data de validade é obrigatória",
        path: ["cnhExpirationDate"]
    }
);

type IFormInputs = z.infer<typeof DriverForm>

type DialogType = 'save' | 'disable' | 'enable' | null;
type AlertState = { open: boolean; message: string; severity: 'success' | 'error' } | null;

export default function DriverInfoPage() {
    const { driverId } = useParams();

    const [openDialog, setOpenDialog] = React.useState<DialogType>(null);
    const [alert, setAlert] = React.useState<AlertState>(null);
    const [loading, setLoading] = React.useState(false);
    const [isEnabled, setIsEnabled] = React.useState(false);

    const { handleSubmit, control, trigger, reset, setValue } = useForm<IFormInputs>({
        defaultValues: {
            name: '',
            email: '',
            ddd: '',
            number: '',
            cnhNumber: '',
            cnhExpirationDate: null
        },
        resolver: zodResolver(DriverForm),
        mode: 'onSubmit',
        reValidateMode: 'onSubmit',
    });

    React.useEffect(() => {
        async function getById() {
            if (driverId === undefined || driverId === 'edit') {
                return;
            }

            const response = await getDriverById(driverId);

            if (response.status !== 200) {
                return;
            }

            const driverData = response.data;
            setIsEnabled(driverData.enabled);

            setValue('name', driverData.name);
            setValue('email', driverData.email);
            setValue('ddd', driverData.phoneNumber.ddd);
            setValue('number', driverData.phoneNumber.number);
            setValue('cnhNumber', driverData.cnhNumber);
            setValue('cnhExpirationDate', dayjs(driverData.cnhExpirationDate, 'DD/MM/YYYY'));
        }

        getById();
    }, [driverId, setValue]);

    const onSubmit: SubmitHandler<IFormInputs> = async (data) => {
        setOpenDialog(null);
        setLoading(true);

        const driverDto: DriverDto = {
            name: data.name,
            email: data.email,
            cnhNumber: data.cnhNumber,
            cnhExpirationDate: data.cnhExpirationDate === null ? new Date() : data.cnhExpirationDate.toDate(),
            phoneNumber: {
                ddd: data.ddd,
                number: data.number
            }
        };

        try {
            let response = null;

            if (driverId && driverId !== 'edit') {
                response = await updateDriver(driverId, driverDto);
            } else {
                response = await saveDriver(driverDto);
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

    const dialogConfig = {
        save: {
            title: 'Confirmar Cadastro de Motorista',
            message: 'Tem certeza que deseja confirmar o cadastro desse motorista?',
            onConfirm: () => handleSubmit(onSubmit)()
        },
        disable: {
            title: 'Confirmar Desabilitar Motorista',
            message: 'Tem certeza que deseja desabilitar esse motorista?',
            onConfirm: () => disable()
        },
        enable: {
            title: 'Confirmar Habilitar Motorista',
            message: 'Tem certeza que deseja habilitar esse motorista?',
            onConfirm: () => enable()
        }
    };

    const disable = async () => {
        setOpenDialog(null);
        try {
            if (driverId === undefined) {
                return;
            }
            await disableDriver(driverId);
            setIsEnabled(false);
            setAlert({ open: true, message: 'Motorista desabilitado com sucesso!', severity: 'success' });
        } catch (error: any) {
            setAlert({ open: true, message: error.response?.data.message, severity: 'error' });
        }
    }

    const enable = async () => {
        setOpenDialog(null);
        try {
            if (driverId === undefined) {
                return;
            }
            await enableDriver(driverId);
            setIsEnabled(true);
            setAlert({ open: true, message: 'Motorista habilitado com sucesso!', severity: 'success' });
        } catch (error: any) {
            setAlert({ open: true, message: error.response?.data.message, severity: 'error' });
        }
    }

    const config = openDialog ? dialogConfig[openDialog] : null;

    return (
        <Stack direction="row" sx={{ backgroundColor: '#F7F9FA' }}>
            <SideMenu />
            <Stack sx={{ padding: '3rem' }}>
                <Breadcrumbs aria-label="breadcrumb">
                    <Link underline="hover" color="inherit" href="/drivers">Motoristas</Link>
                    <Typography sx={{ color: 'text.primary' }}>Cadastro</Typography>
                </Breadcrumbs>
                <Box sx={{ height: 10 }} />
                <Stack direction="row" sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }} >
                    <Typography variant="h4" >Cadastrar Motorista</Typography>
                </Stack>
                <Box sx={{ height: 20 }} />
                <FormControl onSubmit={handleSubmit(onSubmit)} component="form">
                    <PersonalInfoPaper control={control} />
                    <Box sx={{ height: 30 }} />
                    <PhoneNumberInput control={control} />
                    <Box sx={{ height: 30 }} />
                    <Paper sx={{ padding: 3, boxSizing: 'border-box' }}>
                        <Typography variant="h5" >CNH</Typography>
                        <Grid container spacing={2} sx={{ marginTop: 3 }}>
                            <Grid size={6}>
                                <Controller
                                    name="cnhNumber"
                                    control={control}
                                    render={({ field, fieldState }) => (
                                        <MaskedTextField
                                            mask="00000000000"
                                            placeholder="12345678900"
                                            {...field}
                                            label="Número Registro"
                                            type="text"
                                            error={!!fieldState.error}
                                            helperText={fieldState.error?.message}
                                            variant="outlined"
                                            sx={{ width: '100%' }}
                                        />
                                    )}
                                />
                            </Grid>
                            <Grid size={6}>
                                <Controller
                                    name="cnhExpirationDate"
                                    control={control}
                                    render={({ field, fieldState }) => (
                                        <LocalizationProvider dateAdapter={AdapterDayjs}>
                                            <DatePicker
                                                label="Validade"
                                                format="DD/MM/YYYY"
                                                value={field.value ? dayjs(field.value) : null}
                                                onChange={(newValue) => field.onChange(newValue)}
                                                slotProps={{
                                                    textField: {
                                                        error: !!fieldState.error,
                                                        helperText: fieldState.error?.message,
                                                        fullWidth: true,
                                                    }
                                                }}
                                            />
                                        </LocalizationProvider>
                                    )}
                                />
                            </Grid>
                        </Grid>
                    </Paper>
                    <Box sx={{ height: 30 }} />
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
                            driverId !== 'edit' && (
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