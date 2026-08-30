import { createContext, useContext, useMemo, useState } from "react";
import { apiRequest } from "../api/client";

const AUTH_KEY = "nagorik-seba-auth";
const AuthContext = createContext(null);

function loadStoredAuth() {
  try {
    const raw = localStorage.getItem(AUTH_KEY);
    return raw ? JSON.parse(raw) : null;
  } catch {
    return null;
  }
}

export function AuthProvider({ children }) {
  const [auth, setAuth] = useState(loadStoredAuth);

  const persist = (next) => {
    setAuth(next);
    if (next) {
      localStorage.setItem(AUTH_KEY, JSON.stringify(next));
    } else {
      localStorage.removeItem(AUTH_KEY);
    }
  };

  const login = async (emailOrPhone, password) => {
    const data = await apiRequest("/api/auth/login", {
      method: "POST",
      body: JSON.stringify({ emailOrPhone, password }),
    });
    persist(data);
    return data;
  };

  const register = async (payload) => {
    const data = await apiRequest("/api/auth/register", {
      method: "POST",
      body: JSON.stringify(payload),
    });
    persist(data);
    return data;
  };

  const logout = () => persist(null);

  const homeForRole = (role) => {
    if (role === "CITIZEN") {
      return "/citizen/dashboard";
    }
    return "/authority/dashboard";
  };

  const value = useMemo(
    () => ({
      auth,
      isAuthenticated: Boolean(auth?.accessToken),
      login,
      register,
      logout,
      homeForRole,
      authHeader: auth?.accessToken ? { Authorization: `Bearer ${auth.accessToken}` } : {},
    }),
    [auth]
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) {
    throw new Error("useAuth must be used within AuthProvider");
  }
  return ctx;
}
