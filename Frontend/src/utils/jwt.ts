// Utility to decode JWT token and extract claims
export const decodeJWT = (token: string): any => {
  try {
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(
      atob(base64)
        .split('')
        .map((c) => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
        .join('')
    );
    return JSON.parse(jsonPayload);
  } catch (error) {
    console.error('Error decoding JWT:', error);
    return null;
  }
};

export const getRolesFromToken = (token: string | null): string[] => {
  if (!token) return [];
  const decoded = decodeJWT(token);
  if (decoded && decoded.roles) {
    // Roles might be an array or a single value
    if (Array.isArray(decoded.roles)) {
      return decoded.roles;
    }
    return [decoded.roles];
  }
  return [];
};

export const hasRole = (token: string | null, role: string): boolean => {
  const roles = getRolesFromToken(token);
  return roles.includes(role) || roles.includes(`ROLE_${role}`);
};

export const isAdmin = (token: string | null): boolean => {
  return hasRole(token, 'ADMIN') || hasRole(token, 'ROLE_ADMIN');
};

