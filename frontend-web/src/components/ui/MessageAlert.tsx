import { Alert, Snackbar } from "@mui/material";

interface MessageAlertProps {
    open: boolean;
    message: string;
    severity: 'success' | 'error';
    onClose: () => void;
}

export default function MessageAlert({open, message, onClose, severity}: MessageAlertProps) {
    return (
        <Snackbar open={open} autoHideDuration={6000} onClose={onClose}>
            <Alert
                onClose={onClose}
                severity={severity}
                variant="filled"
                sx={{ width: '100%' }}
            >
            {message}
            </Alert>
        </Snackbar>
    )
}