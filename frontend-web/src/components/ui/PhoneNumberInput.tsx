import { Paper, Stack, TextField, Typography } from "@mui/material";
import { Controller, type Control } from "react-hook-form";

interface PhoneNumberInputProps {
    control: Control<any>;
}

export default function PhoneNumberInput({control}: PhoneNumberInputProps) {
    return (
        <Paper sx={{padding: 3, boxSizing: 'border-box'}}>
            <Typography variant="h5" >Telefone</Typography>
            <Stack direction="row" spacing={2} sx={{marginTop: 3}}>
                <TextField label="Código do país" type="text"defaultValue="+55" disabled/>
                <Controller
                    name="ddd"
                    control={control}
                    render={({ field, fieldState }) => <TextField label="DDD" type="number" error={!!fieldState.error} helperText={fieldState.error?.message} variant="outlined" {...field} />}
                />
                <Controller
                    name="number"
                    control={control}
                    render={({ field, fieldState }) => <TextField label="Número" type="text" error={!!fieldState.error} helperText={fieldState.error?.message} variant="outlined" {...field} />}
                />
            </Stack>
        </Paper>
    );
}