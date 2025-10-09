import { Button, Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle } from "@mui/material";

interface ConfirmationDialogProps {
    title: string;
    message: string;
    open: boolean;
    onClose: () => void;
    onConfirm: () => void;
}

export default function ConfirmationDialog({title, message, open, onClose, onConfirm}: ConfirmationDialogProps) {
    return (
        <Dialog open={open} onClose={onClose}>
            <DialogTitle>
                {title}
            </DialogTitle>
            <DialogContent>
                <DialogContentText>
                    {message}
                </DialogContentText>
            </DialogContent>
            <DialogActions>
                <Button variant="outlined" color="success" onClick={onConfirm}>Confirmar</Button>
                <Button variant="outlined" color="error" onClick={onClose}>Cancelar</Button>
            </DialogActions>
        </Dialog>
    )
}