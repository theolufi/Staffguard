import { useState, useEffect } from 'react';
import { Form, Button, Alert } from 'react-bootstrap';
import { useAuth } from '../../contexts/AuthContext';
import { useNavigate } from 'react-router-dom';

const Login = () => {
  const { login, user } = useAuth(); // Ensure we get user from context
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const userData = await login({
        username: e.target.username.value,
        password: e.target.password.value
      });

      console.log("Logged-in User:", userData); // DEBUG: Print user object
    } catch (err) {
      setError("Invalid credentials");
    }
  };

  // Redirect AFTER the user state has updated
  useEffect(() => {
    if (user) {
      console.log("User Detected in State:", user);
      if (user.roles.some(role => role.name === "ROLE_ADMIN")) {
        console.log("Navigating to /admin");
        navigate("/admin");
      } else {
        console.log("Navigating to /profile");
        navigate("/profile");
      }
    }
  }, [user, navigate]); // Runs AFTER user state updates

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
