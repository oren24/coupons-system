import {createTheme} from '@mui/material/styles';

// Lightweight theme to standardize spacing, palette and typography.
const theme = createTheme({
    palette: {
        primary: {
            main: '#1976d2'
        },
        secondary: {
            main: '#f50057'
        }
    },
    spacing: 8,
    typography: {
        h5: {
            fontWeight: 600
        }
    }
});

export default theme;

