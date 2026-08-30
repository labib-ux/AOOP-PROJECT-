import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../auth/AuthContext";

export default function Register() {
  const { register, homeForRole } = useAuth();
  const navigate = useNavigate();
  const [form, setForm] = useState({
    fullName: "",
    email: "",
    phone: "",
    password: "",
  });
  const [error, setError] = useState("");
  const [busy, setBusy] = useState(false);

  const update = (field) => (event) => setForm((prev) => ({ ...prev, [field]: event.target.value }));

  const onSubmit = async (event) => {
    event.preventDefault();
    setError("");
    setBusy(true);
    try {
      const data = await register(form);
      navigate(homeForRole(data.role));
    } catch (err) {
      setError(err.message);
    } finally {
      setBusy(false);
    }
  };

  return (
    <form className="card form" onSubmit={onSubmit}>
      <h1>Create citizen account</h1>
      <label>
        Full name
        <input value={form.fullName} onChange={update("fullName")} required />
      </label>
      <label>
        Email
        <input type="email" value={form.email} onChange={update("email")} required />
      </label>
      <label>
        Phone
        <input value={form.phone} onChange={update("phone")} required />
      </label>
      <label>
        Password
        <input type="password" value={form.password} onChange={update("password")} minLength={6} required />
      </label>
      {error && <p className="error">{error}</p>}
      <button className="button primary" type="submit" disabled={busy}>
        {busy ? "Creating..." : "Register"}
      </button>
      <p className="muted">
        Already registered? <Link to="/login">Login</Link>
      </p>
    </form>
  );
}
