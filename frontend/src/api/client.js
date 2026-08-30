const API_BASE = import.meta.env.VITE_API_URL || "";

export async function apiRequest(path, options = {}) {
  const headers = {
    ...(options.body instanceof FormData ? {} : { "Content-Type": "application/json" }),
    ...(options.headers || {}),
  };

  const response = await fetch(`${API_BASE}${path}`, {
    ...options,
    headers,
  });

  const text = await response.text();
  let data = null;
  try {
    data = text ? JSON.parse(text) : null;
  } catch {
    throw new Error(text || "Request failed");
  }

  if (!response.ok) {
    const message = data?.error || data?.message || "Request failed";
    throw new Error(message);
  }

  return data;
}
