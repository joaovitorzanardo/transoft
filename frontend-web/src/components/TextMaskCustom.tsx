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
                onAccept={(value: any) => {
                    onChange({ target: { name: props.name, value } });
                }}
                overwrite
            />
        );
    },
);

type MaskedTextFieldProps = Omit<TextFieldProps, 'InputProps'> & {
    mask: string;
    InputProps?: TextFieldProps['InputProps'];
};

export const MaskedTextField = React.forwardRef<HTMLDivElement, MaskedTextFieldProps>(
    (props, ref) => {
        const { mask, InputProps, inputProps, onChange, value, ...textFieldProps } = props;

        return (
            <TextField
                {...textFieldProps}
                ref={ref}
                value={value}
                onChange={onChange}
                slotProps={{
                    input: {
                        inputComponent: TextMaskCustom as any,
                        ...InputProps,
                    },
                    htmlInput: {
                        mask: mask,
                        ...inputProps
                    }
                }}
            />
        );
    }
);