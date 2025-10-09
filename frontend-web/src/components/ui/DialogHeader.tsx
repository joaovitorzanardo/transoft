import { Divider, IconButton, Stack, Typography } from "@mui/material";
import { GridCloseIcon } from "@mui/x-data-grid";

interface DialogHeaderProps {
    title: string;
    onClose: () => void;
}

export default function DialogHeader({title, onClose}: DialogHeaderProps) {
    return (
        <>
            <Stack direction="row" sx={{marginBottom: 1}} justifyContent="space-between">
                <Typography variant="h4">{title}</Typography>
                <IconButton>
                    <GridCloseIcon onClick={onClose} />
                </IconButton>
            </Stack>
            <Divider />
        </>
        
    );
}