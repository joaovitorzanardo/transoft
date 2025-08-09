import { Grid } from "@mui/material";
import SideMenu from "../components/SideManu";

export default function ItinerariesPage() {
    return (
        <Grid container spacing={6}>
            <Grid>
                <SideMenu />
            </Grid>
            <Grid>
                <h1>Itinerários</h1>
            </Grid>
        </Grid>
    );
}