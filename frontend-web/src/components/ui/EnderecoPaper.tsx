import { Box, Grid, Paper, TextField, Typography } from "@mui/material";
import { Controller, type Control, type UseFormSetValue } from "react-hook-form";
import { IMaskMixin } from "react-imask";
import { getAddressInfo } from "../../services/address.service";
import React from "react";

interface EnderecoPaperProps {
    control: Control<any>;
    setValue: UseFormSetValue<any>;
}

const MaskedTextField = IMaskMixin(({ inputRef, ...props }) => (
    <TextField {...props} inputRef={inputRef} />
));

export default function EnderecoPaper({control, setValue}: EnderecoPaperProps) {
    const [previousCep, setPreviousCep] = React.useState<string>('');

    const handleOnBlurCep = async (e: React.FocusEvent<HTMLInputElement>) => {
        const cep = e.target.value;
        console.log(cep)
        if (previousCep === cep || cep.trim() === '') {
            setPreviousCep(cep);
            return;
        }

        try {
            const response = await getAddressInfo(cep);
            const addressData = response.data;
            setValue('endereco', addressData.street);
            setValue('cidade', addressData.city);
            setValue('estado', addressData.state);
            setValue('bairro', addressData.neighborhood);
            console.log(addressData)
        } catch(error) {
            console.log(error)
        } finally {
            setPreviousCep(cep);
        }
    }

    return (
        <Paper sx={{padding: 3, boxSizing: 'border-box'}}>
            <Typography variant="h5" >Endereço</Typography>
            <Grid container spacing={2} sx={{marginTop: 3}}>
                <Grid size={6}>
                    <Controller
                        name="cep"
                        control={control}
                        render={({ field, fieldState }) => (
                            <MaskedTextField 
                                mask="00000-000"
                                placeholder="99700-000"
                                {...field}
                                label="CEP" 
                                type="text" 
                                onBlur={handleOnBlurCep}
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
                        render={({ field, fieldState }) => <TextField label="Bairro" disabled type="text" error={!!fieldState.error} helperText={fieldState.error?.message} variant="outlined" {...field} sx={{width: '100%'}}/>}
                    />
                </Grid>
            </Grid>
            <Box sx={{height: 20}}/>
            <Grid container spacing={2}>
                <Grid size={6}>
                    <Controller 
                        name="endereco"
                        control={control}
                        render={({ field, fieldState }) => <TextField label="Endereço" disabled type="text" error={!!fieldState.error} helperText={fieldState.error?.message} variant="outlined" {...field} sx={{width: '100%'}}/>}
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
                        render={({ field, fieldState }) => <TextField label="Complemento" type="text" error={!!fieldState.error} helperText={fieldState.error?.message} variant="outlined" {...field} sx={{width: '100%'}}/>}
                    />
                </Grid>
            </Grid>
        </Paper>
    );
}