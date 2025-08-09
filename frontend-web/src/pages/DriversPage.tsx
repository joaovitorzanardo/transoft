import { Grid } from "@mui/material";
import SideMenu from "../components/SideManu";

export default function DriversPage() {
    return (
        <Grid container spacing={6}>
            <Grid>
                <SideMenu />
            </Grid>
            <Grid>
                <h1>Motoristas</h1>
            </Grid>
        </Grid>
    );
}