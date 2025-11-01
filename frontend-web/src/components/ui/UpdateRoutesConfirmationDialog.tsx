import { Button, Checkbox, Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle, FormControl, FormControlLabel } from "@mui/material";

interface UpdateRoutesConfirmationDialogProps {
    title: string;
    message: string;
    open: boolean;
    onClose: () => void;
    onConfirm: () => void;
    updateItineraries: boolean;
    setUpdateItineraries: (value: boolean) => void;
}

export default function UpdateRoutesConfirmationDialog({title, message, open, onClose, onConfirm, updateItineraries, setUpdateItineraries}: UpdateRoutesConfirmationDialogProps) {
    const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setUpdateItineraries(event.target.checked);
      };
    
    return (
        <Dialog open={open} onClose={onClose}>
            <DialogTitle>
                {title}
            </DialogTitle>
            <DialogContent>
                <DialogContentText>
                    {message}
                </DialogContentText>
                <FormControl>
                    <FormControlLabel
                        label="Atualizar itinerários agendados para essa rota"
                        control={<Checkbox checked={updateItineraries} onChange={handleChange} />}
                        labelPlacement="top">
                    </FormControlLabel>
                </FormControl>
            </DialogContent>
            <DialogActions>
                <Button variant="outlined" color="success" onClick={onConfirm}>Confirmar</Button>
                <Button variant="outlined" color="error" onClick={onClose}>Cancelar</Button>
            </DialogActions>
        </Dialog>
    )
}