import { useSelector } from 'react-redux';
import { AppState } from '../../redux/app-state';
import Layout from '../layout/Layout';
import { Box, Container, Typography, Paper } from '@mui/material';
import { LoginRegister } from '../../Modalpopups/Login-register/LoginRegister';

export function HomePage() {
    const store = useSelector((state: AppState) => state);
    const isAuthenticated = store?.connectedUser && store?.token;

    if (isAuthenticated) {
        return <Layout />;
    }

    // Show login/register prompt for unauthenticated users
    return (
        <Box sx={{ display: 'flex', minHeight: '100vh', flexDirection: 'column', alignItems: 'center', justifyContent: 'center' }}>
            <Container maxWidth="sm">
                <Paper elevation={3} sx={{ p: 4, textAlign: 'center' }}>
                    <Typography variant="h4" component="h1" sx={{ mb: 2 }}>
                        Welcome to Coupons
                    </Typography>
                    <Typography variant="body1" sx={{ mb: 3 }}>
                        Please log in or register to view available coupons
                    </Typography>
                    <LoginRegister />
                </Paper>
            </Container>
        </Box>
    );
}

