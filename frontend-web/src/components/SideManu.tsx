/* eslint-disable @typescript-eslint/no-unused-vars */
import { List, ListItem, ListItemButton, ListItemIcon, ListItemText, Paper, Typography, Button } from "@mui/material";

// import EqualizerIcon from '@mui/icons-material/Equalizer';
import ForkRightIcon from '@mui/icons-material/ForkRight';
import EmojiPeopleIcon from '@mui/icons-material/EmojiPeople';
import PersonIcon from '@mui/icons-material/Person';
import DirectionsBusIcon from '@mui/icons-material/DirectionsBus';
import SettingsIcon from '@mui/icons-material/Settings';
// import ArticleIcon from '@mui/icons-material/Article';
import PendingActionsIcon from '@mui/icons-material/PendingActions';
import { useNavigate } from "react-router";
import ConfirmationDialog from "./ui/ConfirmationDialog";
import React from "react";
import MessageAlert from "./ui/MessageAlert";
import { axiosInstance } from "../services/axios-instance";

interface MenuItem {
    name: string;
    icon: React.ReactNode;
    path: string;
}

type AlertState = { open: boolean; message: string; severity: 'success' | 'error' } | null;

export default function SideMenu() {
    const navigate = useNavigate();

    const [alert, setAlert] = React.useState<AlertState>(null);
    const [openDialog, setOpenDialog] = React.useState<boolean>(false);

    const items: MenuItem[] = [
        // {name: "Dashboard", icon: <EqualizerIcon/>, path: "/dashboard"}, 
        { name: "Rotas", icon: <ForkRightIcon />, path: "/routes" },
        { name: "Passageiros", icon: <EmojiPeopleIcon />, path: "/passengers" },
        { name: "Motoristas", icon: <PersonIcon />, path: "/drivers" },
        { name: "Veículos", icon: <DirectionsBusIcon />, path: "/vehicles" },
        { name: "Itinerarios", icon: <PendingActionsIcon />, path: "/itineraries" },
        { name: "Configurações", icon: <SettingsIcon />, path: "/configuration" },
        // {name: "Relatórios", icon: <ArticleIcon /> , path: "/reports"}
    ];

    return (
        <Paper elevation={3} sx={{ width: 250, height: '100vh', display: 'flex', flexDirection: 'column', justifyContent: 'space-between' }}>
            <div>
                <Typography variant="h5" sx={{ padding: 2, textAlign: 'center' }}>Transoft</Typography>
                <List>
                    {items.map((menuItem, index) => (
                        <ListItem key={index}>
                            <ListItemButton onClick={() => navigate(menuItem.path)}>
                                <ListItemIcon>
                                    {menuItem.icon}
                                </ListItemIcon>
                                <ListItemText primary={menuItem.name} />
                            </ListItemButton>
                        </ListItem>
                    ))}
                </List>
            </div>
            <ConfirmationDialog
                title="Confirmar Sair do Sistema"
                message="Tem certeza que deseja sair do sistema?"
                open={openDialog}
                onClose={() => setOpenDialog(false)}
                onConfirm={async () => {
                    try {
                        await axiosInstance.post("/logout");
                        navigate("/login");
                    } catch (error) {
                        setAlert({ open: true, message: 'Erro fazer o logout!', severity: 'error' });
                        setOpenDialog(false);
                    }
                }}
            />
            <Button
                variant="contained"
                color="error"
                sx={{ margin: 2 }}
                onClick={() => setOpenDialog(true)}
            >
                Sair
            </Button>
            {alert && (
                <MessageAlert
                    open={alert.open}
                    message={alert.message}
                    severity={alert.severity}
                    onClose={() => setAlert(null)}
                />
            )}
        </Paper>
    );
}