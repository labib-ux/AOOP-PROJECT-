export default function PlaceholderPage({ title, detail }) {
  return (
    <section className="card">
      <h1>{title}</h1>
      <p className="muted">{detail}</p>
      <p className="badge">Coming in a later phase</p>
    </section>
  );
}
