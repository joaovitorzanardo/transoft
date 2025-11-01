import { Alert, Button, Card, Collapse, Divider, FormControl, IconButton, TextField, Typography } from "@mui/material";
import React from "react";
import { useForm, Controller, type SubmitHandler } from "react-hook-form";
import { Link, useNavigate } from "react-router";
import z from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import { register } from "../../services/register.service";
import type RegisterDto from "../../models/auth/RegisterDto";
import type CompanyDto from "../../models/CompanyDto";
import CloseIcon from '@mui/icons-material/Close';
import PasswordInput from "../../components/ui/PasswordInput";
import { IMaskMixin } from "react-imask";

const RegistrationForm = z.object({
    userName: z.string().nonempty({message: "O nome deve ser informado"}),
    userEmail: z.email({message: "Formato do email inválido"}),
    password: z.string().nonempty({message: "A senha deve ser informada"}),
    confirmPassword: z.string(),
    companyName: z.string().nonempty({message: "O nome da empresa deve ser informado"}),
    companyEmail: z.email({message: "Formato do email inválido"}),
    cnpj: z.string()
        .nonempty({message: "O cnpj deve ter ser informado"})
        .length(14, {message: "O cnpj deve ter 14 caracteres"})
        .regex(/^\d+$/, {message: "O cnpj deve conter apenas números"})
}).refine((data) => data.password === data.confirmPassword, {
    message: "As senhas não coincidem",
    path: ["confirmPassword"]
});

type IFormInputs = z.infer<typeof RegistrationForm>

const MaskedTextField = IMaskMixin(({ inputRef, ...props }) => (
    <TextField {...props} inputRef={inputRef} />
));

export default function RegisterPage() {
    const navigate = useNavigate();
    const [hasError, setHasError] = React.useState(false);
    const [errorMessage, setErrorMessage] = React.useState('');

    const { handleSubmit, control, formState: {isValid} } = useForm<IFormInputs>({
        defaultValues: {
            userName: '',
            userEmail: '',
            password: '',
            confirmPassword: '',
            companyName: '',
            companyEmail: '',
            cnpj: ''
        },
        resolver: zodResolver(RegistrationForm),
    });

    const onSubmit: SubmitHandler<IFormInputs> = async (data) => {        
        if (!isValid) {
            return;
        }

        const companyDto: CompanyDto = {
            name: data.companyName,
            email: data.companyEmail,
            cnpj: data.cnpj
        }

        const registerDto: RegisterDto = {
            name: data.userName,
            email: data.userEmail,
            password: data.password,
            company: companyDto,
        }

        try {
            await register(registerDto);
            navigate('/routes');
        } catch (error: any) {
            setHasError(true);
            setErrorMessage(error.response?.data.message)
        }
    }

    return (
        <>
            <Card variant="outlined" style={{display: 'flex', flexDirection: 'column', gap: '16px', padding: '16px', maxWidth: '400px', margin: 'auto', marginTop: '100px'}}>
                <FormControl onSubmit={handleSubmit(onSubmit)} style={{display: 'flex', flexDirection: 'column', gap: '16px'}} component="form">
                    <Typography variant="h5" component="h2" align="center">Criar Conta</Typography>
                    <Typography variant="subtitle2" align="center">Preencha os dados para começar</Typography>
                    <Controller
                        name="userName"
                        control={control}
                        render={({ field, fieldState }) => <TextField label="Nome" error={!!fieldState.error} helperText={fieldState.error?.message} variant="outlined" {...field}/>}
                    />
                    <Controller 
                        name="userEmail"
                        control={control}
                        render={({ field, fieldState }) => <TextField label="Email" error={!!fieldState.error} helperText={fieldState.error?.message} variant="outlined" {...field}/>}
                    />
                    <Controller 
                        name="password"
                        control={control}
                        render={({ field, fieldState }) => 
                        <PasswordInput label="Senha" error={!!fieldState.error} helperText={fieldState.error?.message} field={field}/>}
                    />
                    <Controller 
                        name="confirmPassword"
                        control={control}
                        render={({ field, fieldState }) => 
                            <PasswordInput label="Confirmar Senha" error={!!fieldState.error} helperText={fieldState.error?.message} field={field}/>}
                    />
                    <Controller 
                        name="companyName"
                        control={control}
                        render={({ field, fieldState }) => <TextField label="Nome da Empresa" error={!!fieldState.error} helperText={fieldState.error?.message} variant="outlined" {...field}/>}
                    />
                    <Controller 
                        name="companyEmail"
                        control={control}
                        render={({ field, fieldState }) => <TextField label="Email da Empresa" error={!!fieldState.error} helperText={fieldState.error?.message} variant="outlined" {...field}/>}
                    />
                    <Controller
                        name="cnpj"
                        control={control}
                        render={({ field, fieldState }) => (
                            <MaskedTextField 
                                mask="00.000.000/0000-00"
                                placeholder="00.000.000/0000-00"
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
                    <Button variant="contained" type="submit">Realizar Cadastro</Button>
                </FormControl>
                <Divider />
                <Link to="/login" style={{textAlign: 'center'}}>Já possui uma conta? Entre aqui</Link>
            </Card>
            <Collapse in={hasError}>
                <Alert severity="error" action={
                    <IconButton aria-label="close" color="inherit" size="small" onClick={() => { setHasError(false) }}>
                        <CloseIcon fontSize="inherit" />
                    </IconButton>
                }>
                    {errorMessage}
                </Alert>
            </Collapse>
        </>
    );
}