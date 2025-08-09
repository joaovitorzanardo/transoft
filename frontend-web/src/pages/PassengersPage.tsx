import { Grid } from "@mui/material";
import SideMenu from "../components/SideManu";

export default function PassengersPage() {
    return (
        <Grid container spacing={6}>
            <Grid>
                <SideMenu />
            </Grid>
            <Grid>
                <h1>Passageiros</h1>
            </Grid>
        </Grid>
    );
}