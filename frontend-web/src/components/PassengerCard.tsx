import { Avatar, Card, CardActions, CardHeader, IconButton, Typography } from "@mui/material";
import { red } from "@mui/material/colors";
import EditIcon from '@mui/icons-material/Edit';

export default function PassengerCard() {
    return (
        <Card sx={{ maxWidth: 345 }}>
            <CardHeader
                avatar={
                    <Avatar sx={{ bgcolor: red[500] }} aria-label="recipe">
                        R
                    </Avatar>
                }
            />
            <Typography variant="h6" component="div" sx={{ padding: 2 }}>
                Passenger Name
            </Typography>
            <CardActions disableSpacing>
                <IconButton aria-label="edit passenger">
                    <EditIcon />
                </IconButton>
            </CardActions>
        </Card>
    )
}