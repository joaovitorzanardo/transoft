/* eslint-disable @typescript-eslint/no-explicit-any */
import React from 'react';
import { TextField, type TextFieldProps } from '@mui/material';
import { IMaskInput } from 'react-imask';

interface CustomProps {
    onChange: (event: { target: { name: string; value: string } }) => void;
    name: string;
    mask: string;
}

const TextMaskCustom = React.forwardRef<HTMLElement, CustomProps>(
    function TextMaskCustom(props, ref) {
        const { onChange, mask, ...other } = props;
        return (
            <IMaskInput
                {...other}
                mask={mask}
                inputRef={ref as any}
                onAccept={(value: any) => onChange({ target: { name: props.name, value } })}
                overwrite
            />
        );
    },
);


type MaskedTextFieldProps = TextFieldProps & {
    mask: string;
};

export const MaskedTextField = React.forwardRef<HTMLDivElement, MaskedTextFieldProps>(
    (props, ref) => {
        const { mask, ...textFieldProps } = props;

        return (
            <TextField
                {...textFieldProps}
                ref={ref}
                InputProps={{
                    inputComponent: TextMaskCustom as any,
                    ...textFieldProps.InputProps,
                }}
                inputProps={{
                    mask: mask,
                    ...textFieldProps.inputProps
                }}
            />
        );
    }
);