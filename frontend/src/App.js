import logo from './logo.svg';
import './App.css';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import { AuthProvider } from './contexts/AuthContext';
import Login from './components/Auth/Login';
import AdminDashboard from './components/Dashboard/AdminDashboard';
import UserProfile from './components/Dashboard/UserProfile';
import Navbar from './components/Common/Navbar';
import ProtectedRoute from './components/Common/ProtectedRoute';

function App() {
  return (
    <Router>
      <AuthProvider>
        <Navbar />
        <div className="container">
          <Routes>
            {/* Landing Page */}
            <Route path="/" element={
              <div className="landing-page">
                <div className="text-center py-5">
                  <img
                    src="/staffguard.png"
                    alt="StaffGuard Logo"
                    className="mb-4"
                    style={{
                      width: 'min(10%)',
                      filter: 'drop-shadow(0 4px 6px rgba(0,0,0,0.1))'
                    }}
                  />
                </div>
                <div className="hero-section text-center py-5">
                  <h1 className="display-4 mb-4" style={{ color: 'var(--primary)' }}>
                    Welcome to StaffGuard
                  </h1>
                  <p className="lead mb-4" style={{ color: 'var(--secondary)' }}>
                    Modern Employee Management Solution
                  </p>
                  <div className="auth-options">
                    <Link to="/login" className="btn btn-primary btn-lg mx-2">
                      Employee Login
                    </Link>
                    <Link to="/login" className="btn btn-secondary btn-lg mx-2">
                      Admin Login
                    </Link>
                  </div>
                </div>
              </div>
            }/>

            {/* Login Route */}
            <Route path="/login" element={<Login />} />

            {/* Admin Dashboard - Protected */}
            <Route path="/admin/*" element={
              <ProtectedRoute roles={['ROLE_ADMIN']}>
                <AdminDashboard />
              </ProtectedRoute>
            }/>

            {/* Profile Page - Protected */}
            <Route path="/profile" element={
              <ProtectedRoute>
                <UserProfile />
              </ProtectedRoute>
            }/>
          </Routes>
        </div>
      </AuthProvider>
    </Router>
  );
}

export default App;
