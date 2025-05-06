import { useAuth } from '../../contexts/AuthContext';
import { useLocation, Navigate } from 'react-router-dom';
import Spinner from 'react-bootstrap/Spinner';

const ProtectedRoute = ({ roles, children }) => {
  const { user, loading } = useAuth();
  const location = useLocation();

  if (loading) {
    return (
        <div className="text-center mt-5">
          <Spinner animation="border" />
        </div>
    );
  }

  if (!user) {
    console.warn("Unauthorized access - Redirecting to login");
    return <Navigate to="/login" state={{ from: location }} replace />;
  }

  // roles is an array of strings, e.g. ["ROLE_ADMIN"]
  if (
      Array.isArray(roles) &&
      !user.roles.some(userRole => roles.includes(userRole))
  ) {
    console.warn(`Access denied for user: ${user.username}`);
    return <Navigate to="/" replace />;
  }

  return children;
};

export default ProtectedRoute;
