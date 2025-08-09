import { Grid } from "@mui/material";
import SideMenu from "../components/SideManu";

export default function ConfigurationPage() {
    return (
        <Grid container spacing={6}>
            <Grid>
                <SideMenu />
            </Grid>
            <Grid>
                <h1>Configuração</h1>
            </Grid>
        </Grid>
    );
}