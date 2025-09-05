import { Button, Card, TextField, Typography, Link } from "@mui/material";
import { useNavigate } from "react-router";

export function LoginPage() {
    const navigate = useNavigate();

    const login = () => {
        navigate('/dashboard');
    }

    return (
        <Card variant="outlined" style={{display: 'flex', flexDirection: 'column', gap: '16px', padding: '16px', maxWidth: '400px', margin: 'auto', marginTop: '100px'}}>
            <Typography variant="h5" component="h2" align="center">Login</Typography>
            <TextField label="Email" variant="outlined"/>
            <TextField label="Senha" variant="outlined" type="password"/>
            <Button variant="contained" onClick={login}>Entrar</Button>
            <Link href="/register" align="center">Novo na Transoft? Crie sua conta aqui</Link>
        </Card>        
    );
}