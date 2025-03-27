import { useAuth } from '../../contexts/AuthContext';
import { useLocation, Navigate } from 'react-router-dom';
import Spinner from 'react-bootstrap/Spinner';

const ProtectedRoute = ({ roles, children }) => {
  const { user, loading } = useAuth();
  const location = useLocation();

  if (loading) {
    return <div className="text-center mt-5"><Spinner animation="border" /></div>;
  }

  if (!user) {
    console.warn("Unauthorized access - Redirecting to login");
    return <Navigate to="/login" state={{ from: location }} replace />;
  }

  if (roles && !user.roles.some(role => roles.includes(role.name))) {
    console.warn(`Access denied for user: ${user.username} (Roles: ${user.roles.map(r => r.name).join(", ")})`);
    return <Navigate to="/" replace />;
  }

  return children;
};

export default ProtectedRoute;
