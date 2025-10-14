import { List, ListItem, ListItemButton, ListItemIcon, ListItemText, Paper, Typography } from "@mui/material";

// import EqualizerIcon from '@mui/icons-material/Equalizer';
import ForkRightIcon from '@mui/icons-material/ForkRight';
import EmojiPeopleIcon from '@mui/icons-material/EmojiPeople';
import PersonIcon from '@mui/icons-material/Person';
import DirectionsBusIcon from '@mui/icons-material/DirectionsBus';
import SettingsIcon from '@mui/icons-material/Settings';
// import ArticleIcon from '@mui/icons-material/Article';
import PendingActionsIcon from '@mui/icons-material/PendingActions';
import { useNavigate } from "react-router";

interface MenuItem {
    name: string;
    icon: React.ReactNode;
    path: string;
}

export default function SideMenu() {
    const navigate = useNavigate();

    const items: MenuItem[] = [
        // {name: "Dashboard", icon: <EqualizerIcon/>, path: "/dashboard"}, 
        {name: "Rotas", icon: <ForkRightIcon />, path: "/routes"}, 
        {name: "Passageiros", icon: <EmojiPeopleIcon /> , path: "/passengers"},
        {name: "Motoristas", icon: <PersonIcon /> , path: "/drivers"}, 
        {name: "Veículos", icon: <DirectionsBusIcon /> , path: "/vehicles"},
        {name: "Itinerarios", icon: <PendingActionsIcon /> , path: "/itineraries"}, 
        {name: "Configurações", icon: <SettingsIcon /> , path: "/configuration"}, 
        // {name: "Relatórios", icon: <ArticleIcon /> , path: "/reports"}
    ];

    return (
        <Paper elevation={3} sx={{ width: 250, height: '100vh' }}>
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
        </Paper>
    );
}