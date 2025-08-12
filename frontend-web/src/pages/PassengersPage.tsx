import { Grid } from "@mui/material";
import SideMenu from "../components/SideManu";
import PassengerCard from "../components/PassengerCard";

export default function PassengersPage() {
    return (
        <Grid container spacing={6}>
            <Grid>
                <SideMenu />
            </Grid>
            <Grid>
                <h1>Passageiros</h1>
                <PassengerCard />
            </Grid>
        </Grid>
    );
}