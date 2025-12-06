/* eslint-disable @typescript-eslint/no-explicit-any */
import { Button, Card, Divider, FormControl, TextField, Typography } from "@mui/material";
import React from "react";
import { useForm, Controller, type SubmitHandler } from "react-hook-form";
import { Link, useNavigate } from "react-router";
import z from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import { register } from "../../services/register.service";
import PasswordInput from "../../components/ui/PasswordInput";
import MessageAlert from "../../components/ui/MessageAlert";
import { login } from "../../services/login.service";
import type CompanyRegistrationDto from "../../models/CompanyRegistrationDto";
import { MaskedTextField } from "../../components/TextMaskCustom";

const RegistrationForm = z.object({
    name: z.string().nonempty({ message: "O nome deve ser informado" }),
    email: z.email({ message: "Formato do email inválido" }),
    cnpj: z.string().nonempty({ message: "O cnpj deve ter ser informado" }),
    password: z.string().nonempty({ message: "A senha deve ser informada" }),
    confirmPassword: z.string()
}).refine((data) => data.password === data.confirmPassword, {
    message: "As senhas não coincidem",
    path: ["confirmPassword"]
});

type IFormInputs = z.infer<typeof RegistrationForm>

type AlertState = { open: boolean; message: string; severity: 'success' | 'error' } | null;

export default function RegisterPage() {
    const navigate = useNavigate();
    const [alert, setAlert] = React.useState<AlertState>(null);

    const { handleSubmit, control, formState: { isValid } } = useForm<IFormInputs>({
        defaultValues: {
            name: '',
            email: '',
            cnpj: '',
            password: '',
            confirmPassword: ''
        },
        resolver: zodResolver(RegistrationForm),
    });

    const onSubmit: SubmitHandler<IFormInputs> = async (data) => {
        if (!isValid) {
            return;
        }

        const companyRegistrationDto: CompanyRegistrationDto = {
            name: data.name,
            email: data.email,
            cnpj: data.cnpj,
            password: data.password
        }

        try {
            await register(companyRegistrationDto);
            await login({ email: data.email, password: data.password });
            navigate('/routes');
        } catch (error: any) {
            setAlert({ open: true, message: error.response?.data.message, severity: 'error' });
        }
    }

    return (
        <>
            <Card variant="outlined" style={{ display: 'flex', flexDirection: 'column', gap: '16px', padding: '16px', maxWidth: '400px', margin: 'auto', marginTop: '100px' }}>
                <FormControl onSubmit={handleSubmit(onSubmit)} style={{ display: 'flex', flexDirection: 'column', gap: '16px' }} component="form">
                    <Typography variant="h5" component="h2" align="center">Criar Conta</Typography>
                    <Typography variant="subtitle2" align="center">Preencha os dados para começar</Typography>
                    <Controller
                        name="name"
                        control={control}
                        render={({ field, fieldState }) => <TextField label="Nome da Empresa" error={!!fieldState.error} helperText={fieldState.error?.message} variant="outlined" {...field} />}
                    />
                    <Controller
                        name="email"
                        control={control}
                        render={({ field, fieldState }) => <TextField label="Email" error={!!fieldState.error} helperText={fieldState.error?.message} variant="outlined" {...field} />}
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
                                sx={{ width: '100%' }}
                            />
                        )}
                    />
                    <Controller
                        name="password"
                        control={control}
                        render={({ field, fieldState }) =>
                            <PasswordInput label="Senha" error={!!fieldState.error} helperText={fieldState.error?.message} field={field} />}
                    />
                    <Controller
                        name="confirmPassword"
                        control={control}
                        render={({ field, fieldState }) =>
                            <PasswordInput label="Confirmar Senha" error={!!fieldState.error} helperText={fieldState.error?.message} field={field} />}
                    />
                    <Button variant="contained" type="submit">Realizar Cadastro</Button>
                </FormControl>
                <Divider />
                <Link to="/login" style={{ textAlign: 'center' }}>Já possui uma conta? Entre aqui</Link>
            </Card>
            {alert && (
                <MessageAlert
                    open={alert.open}
                    message={alert.message}
                    severity={alert.severity}
                    onClose={() => setAlert(null)}
                />
            )}
        </>
    );
}