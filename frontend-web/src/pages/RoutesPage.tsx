import { Grid } from "@mui/material";
import SideMenu from "../components/SideManu";

export default function RoutesPage() {
    return (
        <Grid container spacing={6}>
            <Grid>
                <SideMenu />
            </Grid>
            <Grid>
                <h1>Rotas</h1>
            </Grid>
        </Grid>
    );
}