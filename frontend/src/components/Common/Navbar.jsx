import { useAuth } from '../../contexts/AuthContext';
import { Link } from 'react-router-dom';

const Navbar = () => {
  const { user, logout } = useAuth();

  return (
    <nav className="navbar navbar-expand-lg">
      <div className="container-fluid">
        <Link to="/" className="navbar-brand">
          <span style={{ fontWeight: '700', fontSize: '1.5rem' }}>StaffGuard</span>
        </Link>

        <div className="d-flex">
          {user?.roles?.includes('ADMIN') && (
            <Link to="/admin" className="nav-link">Admin Dashboard</Link>
          )}
          {user && <Link to="/profile" className="nav-link">Profile</Link>}
          {user && (
            <button className="btn btn-outline-light ms-2" onClick={logout}>
              Logout
            </button>
          )}
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
