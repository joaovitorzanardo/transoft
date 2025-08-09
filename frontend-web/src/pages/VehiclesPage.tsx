import { Grid } from "@mui/material";
import SideMenu from "../components/SideManu";

export default function VehiclesPage() {
    return (
        <Grid container spacing={6}>
            <Grid>
                <SideMenu />
            </Grid>
            <Grid>
                <h1>Veículos</h1>
            </Grid>
        </Grid>
    );
}