import { Grid } from "@mui/material";
import SideMenu from "../components/SideManu";

export default function ReportsPage() {
    return (
        <Grid container spacing={6}>
            <Grid>
                <SideMenu />
            </Grid>
            <Grid>
                <h1>Relatórios</h1>
            </Grid>
        </Grid>
    );
}