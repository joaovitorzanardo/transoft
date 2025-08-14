import { Container, Paper, Typography } from "@mui/material";

interface Props {
    title: string;
    value?: number;
}

export function StatsCard({ title, value }: Props) {
    return (
        <Paper sx={{ height: "100px", width: "200px", padding: 2}}>
            <Typography variant="h5" sx={{marginBottom: 1}}>{title}</Typography>
            <Container sx={{ display: "flex", width: "100%", justifyContent: "flex-end"}} >
                <Typography variant="h3">{value}</Typography>
            </Container>
        </Paper>
    );
}