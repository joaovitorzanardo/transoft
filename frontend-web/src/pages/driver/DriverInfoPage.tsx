import { Box, Breadcrumbs, Button, FormControl, Grid, Link, Paper, Stack, TextField, Typography } from "@mui/material";
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

const DriverForm = z.object({
    name: z.string().nonempty({message: "O nome deve ser informado"}),
    email: z.email({message: "Formato do email inválido"}),
    ddd: z.number().positive({message: "O DDD deve ser um número positivo"}),
    number: z.string().nonempty({message: "O número deve ser informado"}),
    cnhNumber: z.string()
            .nonempty({message: "A CNH deve ter ser informada"})
            .length(11, {message: "A CNH deve ter 11 caracteres"})
            .regex(/^\d+$/, {message: "A CNH deve conter apenas números"}),
    cnhExpirationDate: z.date()
});

type IFormInputs = z.infer<typeof DriverForm>

export default function DriverInfoPage() {
    const { handleSubmit, control, formState: {isValid} } = useForm<IFormInputs>({
        defaultValues: {
            name: '',
            email: '',
            ddd: 54,
            number: '',
            cnhNumber: '',
            cnhExpirationDate: new Date()
        },
        resolver: zodResolver(DriverForm),
    });

    const onSubmit: SubmitHandler<IFormInputs> = (data) => {        
        if (!isValid) {
            return;
        }

        console.log(data);
    }    

    return (
        <Stack direction="row" sx={{ backgroundColor: '#F7F9FA'}}>
            <SideMenu />
            <Stack sx={{ padding: '3rem'}}>
                <Breadcrumbs aria-label="breadcrumb">
                    <Link underline="hover" color="inherit" href="/drivers">Motoristas</Link>
                    <Typography sx={{ color: 'text.primary' }}>Cadastro</Typography>
                </Breadcrumbs>
                <Box sx={{height: 10}}/>
                <Stack direction="row" sx={{display: 'flex', justifyContent: 'space-between', alignItems: 'center'}} >
                    <Typography variant="h4" >Cadastrar Motorista</Typography>
                </Stack>
                <Box sx={{height: 20}}/>
                <FormControl onSubmit={handleSubmit(onSubmit)} component="form">
                    <PersonalInfoPaper control={control}/>
                    <Box sx={{height: 30}}/>
                    <PhoneNumberInput control={control}/>
                    <Box sx={{height: 30}}/>
                    <Paper sx={{padding: 3, boxSizing: 'border-box'}}>
                        <Typography variant="h5" >CNH</Typography>
                        <Grid container spacing={2} sx={{marginTop: 3}}>
                            <Grid size={6}>
                                <Controller
                                    name="cnhNumber"
                                    control={control}
                                    render={({ field, fieldState }) => <TextField label="CNH" type="number" error={!!fieldState.error} helperText={fieldState.error?.message} variant="outlined" {...field} sx={{width: '100%'}}/>}
                                />
                            </Grid>
                            <Grid size={6}>
                                <LocalizationProvider dateAdapter={AdapterDayjs}>
                                    <DatePicker label="Validade" sx={{width: '100%'}}/>
                                </LocalizationProvider>
                            </Grid>
                        </Grid>
                    </Paper>
                    <Box sx={{height: 30}}/>
                    <Stack direction="row" justifyContent="flex-start" spacing={2}>
                        <Button variant="contained" color="primary">Salvar</Button>
                        <Button variant="outlined" color="error" startIcon={<BlockIcon />}>Desabilitar</Button>
                    </Stack>
                </FormControl>
            </Stack>
        </Stack>
    );
}