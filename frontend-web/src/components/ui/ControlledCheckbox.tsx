import { Checkbox, FormControlLabel } from "@mui/material";
import { Controller, type Control } from "react-hook-form";

interface ControlledCheckboxProps {
    control: Control<any>,
    name: string;
    label: string;
}

export default function ControlledCheckbox({control, name, label}: ControlledCheckboxProps) {
    return (
        <Controller
            name={name}
            control={control}
            render={({ field }) => (
                <FormControlLabel
                    control={
                        <Checkbox 
                            checked={field.value}
                            onChange={field.onChange}
                        />
                    }
                    label={label}
                />
            )}
        />
    )
} 