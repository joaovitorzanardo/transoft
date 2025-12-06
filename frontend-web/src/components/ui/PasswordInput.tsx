/* eslint-disable @typescript-eslint/no-explicit-any */
import { IconButton, TextField } from "@mui/material";
import Visibility from '@mui/icons-material/Visibility';
import VisibilityOff from '@mui/icons-material/VisibilityOff';
import React from "react";

interface PasswordInputProps {
    label: string;
    error: boolean;
    helperText: string | undefined;
    field: Record<string, any>;
}

export default function PasswordInput({ label, error, helperText, field }: PasswordInputProps) {
    const [showPassword, setShowPassword] = React.useState(true);
    const handleShowPassword = () => setShowPassword((show) => !show);

    return (
        <TextField
            label={label}
            error={error}
            helperText={helperText}
            variant="outlined"
            type={showPassword ? 'password' : 'text'}
            slotProps={{
                input: {
                    endAdornment:
                        <IconButton onClick={handleShowPassword} edge="end">
                            {showPassword ? <VisibilityOff /> : <Visibility />}
                        </IconButton>
                }
            }}
            {...field}
        />
    );
}