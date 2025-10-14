import { Box, Button, Grid, Paper, Stack, TextField, Typography } from "@mui/material";
import SideMenu from "../components/SideManu";
import PageTitle from "../components/ui/PageTitle";
import { Controller, useForm, type SubmitHandler } from "react-hook-form";
import z from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import { IMaskMixin } from "react-imask";
import React from "react";
import { getCompany, updateCompany } from "../services/configuration.service";
import type CompanyDto from "../models/CompanyDto";
import MessageAlert from "../components/ui/MessageAlert";
import ConfirmationDialog from "../components/ui/ConfirmationDialog";

const ConfigurationForm = z.object({
    cnpj: z.string().nonempty({message: "O cnpj deve ser informado"}),
    name: z.string().nonempty({message: "O nome deve ser informado"}),
    email: z.email({message: "Formato do email inválido"}),
});

type IFormInputs = z.infer<typeof ConfigurationForm>

const MaskedTextField = IMaskMixin(({ inputRef, ...props }) => (
    <TextField {...props} inputRef={inputRef} />
));

type AlertState = { open: boolean; message: string; severity: 'success' | 'error' } | null;

export default function ConfigurationPage() {

    const [openDialog, setOpenDialog] = React.useState<boolean>(false);
    const [alert, setAlert] = React.useState<AlertState>(null);
    const [loading, setLoading] = React.useState(false);

    const { handleSubmit, control, trigger, setValue } = useForm<IFormInputs>({
        defaultValues: {
            cnpj: '',
            name: '',
            email: '',
        },
        resolver: zodResolver(ConfigurationForm),
    });

    React.useEffect(() => {
        async function get() {
            const apiToken = sessionStorage.getItem("apiToken")
            if (apiToken === undefined || apiToken === '') {
                return;
            }

            const response = await getCompany();

            if (response.status !== 200) {
                return;
            }

            const companyData = response.data;
            
            setValue('cnpj', companyData.cnpj);
            setValue('name', companyData.name);
            setValue('email', companyData.email);
        }

        get();
    }, [setValue]);

    const onSubmit: SubmitHandler<IFormInputs> = async (data) => {        
        setOpenDialog(false);
        setLoading(true);
        
        const companyDto: CompanyDto = {
            cnpj: data.cnpj,
            name: data.name,
            email: data.email,
        };

        try {
            const response = await updateCompany(companyDto);
            if (response.status === 200) {
                setAlert({ open: true, message: 'Dados da empresa atualizados com sucesso!', severity: 'success' });
            } else {
                setAlert({ open: true, message: 'Erro ao atualizar dados da empresa!', severity: 'error' });
            }
        } catch(error) {
            console.log(error);
            setAlert({ open: true, message: 'Erro ao atualizar dados da empresa!', severity: 'error' });
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

    return (
        <Stack direction="row" sx={{ backgroundColor: '#F7F9FA'}}>
            <SideMenu />
            <Stack sx={{ paddingLeft: 5, paddingTop: 5 }}>
                <Stack direction="row" sx={{display: 'flex', justifyContent: 'space-between', alignItems: 'center'}} >
                    <PageTitle title="Configurações" description="Alterar a configuração do sistema" />
                </Stack>
                <Box sx={{height: 30}}/>
                <Paper sx={{ padding: 3 }} >
                    <Typography variant="h5" >Informações da Empresa</Typography>
                    <Grid container spacing={2} sx={{marginTop: 3}}>
                        <Grid size={6}>
                            <Controller
                                name="cnpj"
                                control={control}
                                render={({ field, fieldState }) => (
                                    <MaskedTextField 
                                        mask="00.000.000/0000-00"
                                        placeholder="00.000.000/0000-00"
                                        disabled
                                        {...field}
                                        label="CNPJ" 
                                        type="text" 
                                        error={!!fieldState.error} 
                                        helperText={fieldState.error?.message}
                                        variant="outlined" 
                                        sx={{width: '100%'}}
                                    />
                                )}
                            />
                        </Grid>
                        <Grid size={6}>
                            <Controller
                                name="name"
                                control={control}
                                render={({ field, fieldState }) => <TextField label="Nome" error={!!fieldState.error} helperText={fieldState.error?.message} variant="outlined" {...field} sx={{width: '100%'}}/>}
                            />
                        </Grid>
                        <Grid size={6}>
                            <Controller
                                name="email"
                                control={control}
                                render={({ field, fieldState }) => <TextField label="Email" error={!!fieldState.error} helperText={fieldState.error?.message} variant="outlined" {...field} sx={{width: '100%'}}/>}
                            />
                        </Grid>
                    </Grid>
                </Paper>
                <Box sx={{height: 30}}/>
                <Stack direction="row" justifyContent="flex-start" spacing={2}>
                    <Button 
                        variant="contained" 
                        color="primary" 
                        onClick={() => handleOpenDialog()}
                        loading={loading} 
                        loadingPosition="start"
                    >
                        Atualizar
                    </Button>
                </Stack>
                <ConfirmationDialog
                    title="Confirmar Atualizar Dados da Empresa"
                    message="Tem certeza que deseja atualizar os dados da empresa?"
                    open={openDialog} 
                    onClose={() => setOpenDialog(false)}
                    onConfirm={() => handleSubmit(onSubmit)()}
                />
                {alert && (
                    <MessageAlert 
                        open={alert.open}
                        message={alert.message}
                        severity={alert.severity}
                        onClose={() => setAlert(null)}
                    />
                )}
            </Stack>
        </Stack>
    );
}