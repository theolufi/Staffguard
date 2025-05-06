import { useState, useEffect } from 'react';
import { Form, Button, Alert } from 'react-bootstrap';
import { useAuth } from '../../contexts/AuthContext';
import { useNavigate } from 'react-router-dom';

const Login = () => {
  const { login, user } = useAuth();
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    try {
      const userData = await login({
        username: e.target.username.value,
        password: e.target.password.value
      });
      console.log("Logged-in User:", userData);
    } catch (err) {
      setError("Invalid credentials");
    }
  };

  useEffect(() => {
    if (!user) return;

    console.log("User Detected in State:", user);

    // ==== FIXED: roles is an array of strings, not objects ====
    const isAdmin = user.roles?.some(role => role === "ROLE_ADMIN");

    if (isAdmin) {
      console.log("Navigating to /admin");
      navigate("/admin", { replace: true });
    } else {
      console.log("Navigating to /profile");
      navigate("/profile", { replace: true });
    }
  }, [user, navigate]);

  return (
      <div className="login-container">
        <h2 className="text-center mb-4" style={{ color: 'var(--primary)' }}>
          StaffGuard Login
        </h2>
        <Form onSubmit={handleSubmit}>
          {error && <Alert variant="danger">{error}</Alert>}
          <Form.Group className="mb-3" controlId="username">
            <Form.Label>Username</Form.Label>
            <Form.Control type="text" required placeholder="Enter your username" />
          </Form.Group>
          <Form.Group className="mb-4" controlId="password">
            <Form.Label>Password</Form.Label>
            <Form.Control type="password" required placeholder="Enter your password" />
          </Form.Group>
          <div className="d-grid">
            <Button variant="primary" type="submit" size="lg">
              Sign In
            </Button>
          </div>
        </Form>
      </div>
  );
};

export default Login;
