import { InputAdornment, TextField } from "@mui/material";
import { GridSearchIcon } from "@mui/x-data-grid";

interface SearchInputProps {
    title: string;
}

export function SearchInput({ title }: SearchInputProps) {
    return (
        <TextField id="outlined-basic" placeholder={title} variant="outlined" slotProps={{
            input: {
                startAdornment: (
                <InputAdornment position="start">
                    <GridSearchIcon />
                </InputAdornment>
                ),
            },
            }}/>
    );
}