import { Box, Typography } from "@mui/material";

interface PageTitleProps {
    title: string;
    description: string;
}

export default function PageTitle({title, description}: PageTitleProps) {
    return (
        <Box>
            <Typography variant="h4" >{title}</Typography>
            <Typography variant="body1" >{description}</Typography>
        </Box>
    )
}