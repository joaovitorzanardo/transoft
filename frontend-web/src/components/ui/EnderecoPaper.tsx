import { Box, Grid, Paper, TextField, Typography } from "@mui/material";
import { Controller, type Control } from "react-hook-form";

interface EnderecoPaperProps {
    control: Control<any>;
}

export default function EnderecoPaper({control}: EnderecoPaperProps) {
    return (
        <Paper sx={{padding: 3, boxSizing: 'border-box'}}>
            <Typography variant="h5" >Endereço</Typography>
            <Grid container spacing={2} sx={{marginTop: 3}}>
                <Grid size={6}>
                    <Controller 
                        name="cep"
                        control={control}
                        render={({ field, fieldState }) => <TextField label="CEP" type="text" error={!!fieldState.error} helperText={fieldState.error?.message} variant="outlined" {...field} sx={{width: '100%'}}/>}
                    />                    
                </Grid>
                <Grid size={6}>
                    <Controller 
                        name="estado"
                        control={control}
                        render={({ field, fieldState }) => <TextField label="Estado" disabled type="text" error={!!fieldState.error} helperText={fieldState.error?.message} variant="outlined" {...field} sx={{width: '100%'}}/>}
                    />  
                </Grid>
            </Grid>
            <Box sx={{height: 20}}/>
            <Grid container spacing={2}>
                <Grid size={6}>
                    <Controller 
                        name="cidade"
                        control={control}
                        render={({ field, fieldState }) => <TextField label="Cidade/Município" disabled type="text" error={!!fieldState.error} helperText={fieldState.error?.message} variant="outlined" {...field} sx={{width: '100%'}}/>}
                    />
                </Grid>
                <Grid size={6}>
                    <Controller 
                        name="bairro"
                        control={control}
                        render={({ field, fieldState }) => <TextField label="Bairro" type="text" error={!!fieldState.error} helperText={fieldState.error?.message} variant="outlined" {...field} sx={{width: '100%'}}/>}
                    />
                </Grid>
            </Grid>
            <Box sx={{height: 20}}/>
            <Grid container spacing={2}>
                <Grid size={6}>
                    <Controller 
                        name="endereco"
                        control={control}
                        render={({ field, fieldState }) => <TextField label="Endereço" type="text" error={!!fieldState.error} helperText={fieldState.error?.message} variant="outlined" {...field} sx={{width: '100%'}}/>}
                    />
                </Grid>
                <Grid size={3}>
                    <Controller 
                        name="numero"
                        control={control}
                        render={({ field, fieldState }) => <TextField label="Número" type="number" error={!!fieldState.error} helperText={fieldState.error?.message} variant="outlined" {...field} sx={{width: '100%'}}/>}
                    />
                </Grid>
                <Grid size={3}>
                    <Controller 
                        name="complemento"
                        control={control}
                        render={({ field, fieldState }) => <TextField label="Complemento" type="number" error={!!fieldState.error} helperText={fieldState.error?.message} variant="outlined" {...field} sx={{width: '100%'}}/>}
                    />
                </Grid>
            </Grid>
        </Paper>
    );
}