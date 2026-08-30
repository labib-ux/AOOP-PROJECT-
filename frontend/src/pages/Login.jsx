import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../auth/AuthContext";

export default function Login() {
  const { login, homeForRole } = useAuth();
  const navigate = useNavigate();
  const [emailOrPhone, setEmailOrPhone] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [busy, setBusy] = useState(false);

  const onSubmit = async (event) => {
    event.preventDefault();
    setError("");
    setBusy(true);
    try {
      const data = await login(emailOrPhone, password);
      navigate(homeForRole(data.role));
    } catch (err) {
      setError(err.message);
    } finally {
      setBusy(false);
    }
  };

  return (
    <form className="card form" onSubmit={onSubmit}>
      <h1>Login</h1>
      <label>
        Email or phone
        <input value={emailOrPhone} onChange={(e) => setEmailOrPhone(e.target.value)} required />
      </label>
      <label>
        Password
        <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} required />
      </label>
      {error && <p className="error">{error}</p>}
      <button className="button primary" type="submit" disabled={busy}>
        {busy ? "Signing in..." : "Sign in"}
      </button>
      <p className="muted">
        No account? <Link to="/register">Register</Link>
      </p>
    </form>
  );
}
