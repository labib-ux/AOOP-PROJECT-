import { Link } from "react-router-dom";

export default function Home() {
  return (
    <section className="hero">
      <p className="eyebrow">Ward-level civic complaints for Bangladesh</p>
      <h1>Report broken roads, waterlogging, and dead streetlights — then track them in public.</h1>
      <p className="lede">
        Nagorik Seba sits below ministry GRS systems. Citizens pin issues on a map; wards assign, resolve,
        and get scored in the open.
      </p>
      <div className="actions">
        <Link className="button primary" to="/register">
          Register as citizen
        </Link>
        <Link className="button" to="/map">
          View public map
        </Link>
      </div>
    </section>
  );
}
