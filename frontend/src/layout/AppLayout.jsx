import { Link, NavLink, Outlet } from "react-router-dom";
import { useAuth } from "../auth/AuthContext";

export default function AppLayout() {
  const { isAuthenticated, auth, logout } = useAuth();

  return (
    <div className="app-shell">
      <header className="topbar">
        <Link to="/" className="brand">
          Nagorik Seba
        </Link>
        <nav>
          <NavLink to="/">Home</NavLink>
          <NavLink to="/map">Public map</NavLink>
          {isAuthenticated && auth.role === "CITIZEN" && (
            <>
              <NavLink to="/citizen/dashboard">Dashboard</NavLink>
              <NavLink to="/citizen/complaint/new">New complaint</NavLink>
            </>
          )}
          {isAuthenticated && auth.role !== "CITIZEN" && (
            <>
              <NavLink to="/authority/dashboard">Dashboard</NavLink>
              <NavLink to="/authority/complaints">Complaints</NavLink>
            </>
          )}
          {!isAuthenticated ? (
            <>
              <NavLink to="/login">Login</NavLink>
              <NavLink to="/register">Register</NavLink>
            </>
          ) : (
            <button type="button" className="link-button" onClick={logout}>
              Logout ({auth.fullName})
            </button>
          )}
        </nav>
      </header>
      <main className="page">
        <Outlet />
      </main>
    </div>
  );
}
