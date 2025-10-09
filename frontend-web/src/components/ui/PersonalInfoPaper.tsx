import { Grid, Paper, TextField, Typography } from "@mui/material";
import { Controller, type Control } from "react-hook-form";

interface PersonalInfoPaperProps {
    control: Control<any>;
}

export default function PersonalInfoPaper({control}: PersonalInfoPaperProps) {
    return (
        <Paper sx={{ padding: 3 }} >
            <Typography variant="h5" >Informações Pessoais</Typography>
            <Grid container spacing={2} sx={{marginTop: 3}}>
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
    );
}