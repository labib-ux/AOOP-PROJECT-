import { Navigate } from "react-router-dom";
import { useAuth } from "./AuthContext";

export default function ProtectedRoute({ children, roles }) {
  const { isAuthenticated, auth } = useAuth();

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  if (roles && !roles.includes(auth.role)) {
    return <Navigate to={auth.role === "CITIZEN" ? "/citizen/dashboard" : "/authority/dashboard"} replace />;
  }

  return children;
}
