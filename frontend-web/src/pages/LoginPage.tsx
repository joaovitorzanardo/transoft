import { Button, Card, TextField, Typography, Link, FormControl, Divider, Collapse, Alert, IconButton } from "@mui/material";
import { useNavigate } from "react-router";
import z from "zod";
import { useForm, Controller, type SubmitHandler } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import PasswordInput from "../components/ui/PasswordInput";
import { useAuth } from "../auth/AuthProvider";
import React from "react";
import MessageAlert from "../components/ui/MessageAlert";

const LoginForm = z.object({
    email: z.email({message: "Formato do email inválido"}),
    password: z.string().nonempty({message: "A senha deve ser informada"})
})

type IFormInputs = z.infer<typeof LoginForm>;
type AlertState = { open: boolean; message: string; severity: 'success' | 'error' } | null;

export function LoginPage() {
    const navigate = useNavigate();
    const [alert, setAlert] = React.useState<AlertState>(null);
    const { token, handleLogin } = useAuth();

    if (token) {
        navigate('/routes');
        return;
    }

    const { handleSubmit, control, formState: {isValid} } = useForm<IFormInputs>({
        defaultValues: {
            email: '',
            password: ''
        },
        resolver: zodResolver(LoginForm),
    });

    const onSubmit: SubmitHandler<IFormInputs> = async (data) => {
        if (!isValid) {
            return;
        }

        try {
            await handleLogin(data.email, data.password);
            navigate('/routes');
        } catch (error: any) {
            setAlert({ open: true, message: error.response?.data.message, severity: 'error' });
        }
    }

    return (
        <>
            <Card variant="outlined" style={{display: 'flex', flexDirection: 'column', gap: '16px', padding: '16px', maxWidth: '400px', margin: 'auto', marginTop: '100px'}}>
                <FormControl onSubmit={handleSubmit(onSubmit)} style={{display: 'flex', flexDirection: 'column', gap: '16px'}} component="form">
                    <Typography variant="h5" component="h2" align="center">Login</Typography>
                    <Controller
                        name="email"
                        control={control}
                        render={({field, fieldState}) => <TextField label="Email" variant="outlined" error={!!fieldState.error} helperText={fieldState.error?.message} {...field}/>
                    }/>
                    <Controller 
                            name="password"
                            control={control}
                            render={({ field, fieldState }) => 
                                <PasswordInput label="Senha" error={!!fieldState.error} helperText={fieldState.error?.message} field={field}/>}
                    />
                    <Button variant="contained" type="submit">Entrar</Button>
                </FormControl>
                <Divider />
                <Link href="/register" align="center">Novo na Transoft? Crie sua conta aqui</Link>
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