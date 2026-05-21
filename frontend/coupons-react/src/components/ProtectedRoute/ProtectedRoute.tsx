import React from 'react';
import { useSelector } from 'react-redux';
import { Navigate } from 'react-router-dom';
import { AppState } from '../../redux/app-state';

interface ProtectedRouteProps {
    children: React.ReactNode;
}

export function ProtectedRoute({ children }: ProtectedRouteProps) {
    // Access Redux state to check if user is authenticated
    const store = useSelector((state: AppState) => state);
    const isAuthenticated = store.connectedUser && store.token;

    // If no auth, redirect to login (root page where LoginRegister modal is shown)
    if (!isAuthenticated) {
        return <Navigate to="/" replace />;
    }

    return <>{children}</>;
}

