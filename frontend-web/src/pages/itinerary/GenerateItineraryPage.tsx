import { Box, Breadcrumbs, Button, FormControl, InputLabel, Link, MenuItem, Paper, Select, Stack, Typography, type SelectChangeEvent } from "@mui/material";
import SideMenu from "../../components/SideManu";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import { DatePicker } from "@mui/x-date-pickers/DatePicker";
import { DemoContainer } from "@mui/x-date-pickers/internals/demo";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import React from "react";
import { getAllRoutes } from "../../services/route.service";
import type RouteSelectPresenter from "../../models/route/RouteSelectPresenter";
import { Controller, useForm, type SubmitHandler } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import z from "zod";
import MessageAlert from "../../components/ui/MessageAlert";
import ConfirmationDialog from "../../components/ui/ConfirmationDialog";
import type ItineraryDto from "../../models/ItineraryDto";
import { generateItineraries } from "../../services/itinerary.service";
import type { Dayjs } from "dayjs";
import dayjs from "dayjs";

const dayjsSchema = z.custom<Dayjs>(
    (val) => dayjs.isDayjs(val),
    { message: "Data inválida" }
).nullable();

const GenerateItineraryForm = z.object({
    routeId: z.string().nonempty({message: "A rota deve ser informada"}),
    startDate: dayjsSchema,
    endDate: dayjsSchema,
});

type IFormInputs = z.infer<typeof GenerateItineraryForm>;

type AlertState = { open: boolean; message: string; severity: 'success' | 'error' } | null;

export default function GenerateItineraryPage() {
    const [routes, setRoutes] = React.useState<RouteSelectPresenter[]>([]);

    const [openDialog, setOpenDialog] = React.useState<boolean>(false);
    const [alert, setAlert] = React.useState<AlertState>(null);
    const [loading, setLoading] = React.useState(false);
    
    React.useEffect(() => {
        async function getAll() {
            const response = await getAllRoutes();
            setRoutes(response.data);
        }

        getAll();
    }, []);

    const { handleSubmit, control, trigger, reset } = useForm<IFormInputs>({
        defaultValues: {
            routeId: '',
            startDate: null,
            endDate: null,
        },
        resolver: zodResolver(GenerateItineraryForm),
    });

    const onSubmit: SubmitHandler<IFormInputs> = async (data) => {
            setOpenDialog(false);
            setLoading(true);
    
            const itineraryDto: ItineraryDto = {
                routeId: data.routeId,
                dateInterval: {
                    startDate: data.startDate === null ? new Date() : data.startDate.toDate(),
                    endDate: data.endDate === null ? new Date() : data.endDate.toDate()
                }
            }
                
            try {
                const response = await generateItineraries(itineraryDto);
                if (response.status === 201) {
                    setAlert({ open: true, message: 'Itinerários gerados com sucesso!', severity: 'success' });
                    reset();
                } else {
                    setAlert({ open: true, message: 'Erro ao gerar itinerários!', severity: 'error' });
                }
            } catch(error) {
                console.log(error);
                setAlert({ open: true, message: 'Erro ao gerar itinerários!', severity: 'error' });
            } finally {
                setLoading(false);
            }
        }

    const handleOpenDialog = async () => {
        const isValid = await trigger();
        if (isValid) {
            setOpenDialog(true);
        }
    };

    const config = {
        title: 'Confirmar geração de Itinerários',
        message: 'Tem certeza que deseja confirmar a geração de itinerários?',
        onConfirm: () => handleSubmit(onSubmit)()
    };

    return (
        <Stack direction="row" sx={{ backgroundColor: '#F7F9FA'}}>
            <SideMenu />
            <Stack sx={{ padding: '3rem'}}>
                <Breadcrumbs aria-label="breadcrumb">
                    <Link underline="hover" color="inherit" href="/itineraries">Itinerários</Link>
                    <Typography sx={{ color: 'text.primary' }}>Gerar</Typography>
                </Breadcrumbs>
                <Box sx={{height: 10}}/>
                <Stack direction="row" sx={{display: 'flex', justifyContent: 'space-between', alignItems: 'center'}} >
                    <Typography variant="h4" >Gerar Itinerários</Typography>
                </Stack>
                <Box sx={{height: 20}}/>
                <FormControl component="form">
                    <Paper sx={{ padding: 3 }}>
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
                        <Controller
                            name="startDate"
                            control={control}
                            render={({ field, fieldState }) => (
                                <LocalizationProvider dateAdapter={AdapterDayjs}>
                                    <DatePicker 
                                        sx={{width: '100%', marginTop: 3}}
                                        label="Data Inicial" 
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
                        <Controller
                            name="endDate"
                            control={control}
                            render={({ field, fieldState }) => (
                                <LocalizationProvider dateAdapter={AdapterDayjs}>
                                    <DatePicker 
                                        sx={{width: '100%', marginTop: 3}}
                                        label="Data Final" 
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
                    </Paper>
                    <Box sx={{height: 30}}/>
                    <Stack direction="row" justifyContent="flex-start">
                        <Button 
                            variant="contained" 
                            color="primary" 
                            onClick={() => handleOpenDialog()}
                            loading={loading} 
                            loadingPosition="start"
                        >
                            Gerar
                        </Button>
                    </Stack>
                    {config && (
                        <ConfirmationDialog
                            title={config.title}
                            message={config.message}
                            open={openDialog} 
                            onClose={() => setOpenDialog(false)}
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