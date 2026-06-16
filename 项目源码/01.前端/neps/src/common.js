export function setSessionStorage(key, value) {
  sessionStorage.setItem(key, JSON.stringify(value));
}

export function getSessionStorage(key) {
  const raw = sessionStorage.getItem(key);
  return raw && raw !== 'null' ? JSON.parse(raw) : null;
}

export function removeSessionStorage(key) {
  sessionStorage.removeItem(key);
}
