import { Navigate, Route, Routes } from "react-router-dom";
import ProtectedRoute from "./auth/ProtectedRoute";
import AppLayout from "./layout/AppLayout";
import Home from "./pages/Home";
import Login from "./pages/Login";
import Register from "./pages/Register";
import PlaceholderPage from "./pages/PlaceholderPage";

export default function App() {
  return (
    <Routes>
      <Route element={<AppLayout />}>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route
          path="/map"
          element={<PlaceholderPage title="Public heatmap" detail="Leaflet + OpenStreetMap view of all complaints. No login required in later phases." />}
        />
        <Route
          path="/citizen/dashboard"
          element={
            <ProtectedRoute roles={["CITIZEN"]}>
              <PlaceholderPage title="Citizen dashboard" detail="Your complaints and live timelines will appear here." />
            </ProtectedRoute>
          }
        />
        <Route
          path="/citizen/complaint/new"
          element={
            <ProtectedRoute roles={["CITIZEN"]}>
              <PlaceholderPage title="Submit complaint" detail="Category, description, geo-tagged photo, and auto ward detection." />
            </ProtectedRoute>
          }
        />
        <Route
          path="/citizen/complaint/:id"
          element={
            <ProtectedRoute roles={["CITIZEN"]}>
              <PlaceholderPage title="Complaint detail" detail="Status timeline, rating, and reopen will live on this page." />
            </ProtectedRoute>
          }
        />
        <Route
          path="/authority/dashboard"
          element={
            <ProtectedRoute roles={["WARD_COUNCILOR", "DEPT_OFFICER", "ADMIN"]}>
              <PlaceholderPage title="Authority dashboard" detail="Ward stats, SLA alerts, and performance analytics." />
            </ProtectedRoute>
          }
        />
        <Route
          path="/authority/complaints"
          element={
            <ProtectedRoute roles={["WARD_COUNCILOR", "DEPT_OFFICER", "ADMIN"]}>
              <PlaceholderPage title="Manage complaints" detail="Verify, assign, start work, resolve, and upload proof." />
            </ProtectedRoute>
          }
        />
        <Route path="*" element={<Navigate to="/" replace />} />
      </Route>
    </Routes>
  );
}
