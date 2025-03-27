// UserProfile.jsx
import { useState, useEffect } from 'react';
import { Form, Button } from 'react-bootstrap';
import { useAuth } from '../../contexts/AuthContext';
import api from '../../services/api.js';

const UserProfile = () => {
  const { user } = useAuth();
  const [formData, setFormData] = useState({});

  useEffect(() => {
    if(user) {
      api.get(`/api/users/${user.id}`)
        .then(res => setFormData(res.data));
    }
  }, [user]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    await api.patch(`/api/users/${user.id}`, formData);
  };

  return (
    <Form onSubmit={handleSubmit}>
      <Form.Group controlId="email">
        <Form.Label>Email</Form.Label>
        <Form.Control
          type="email"
          value={formData.email || ''}
          onChange={e => setFormData({...formData, email: e.target.value})}
        />
      </Form.Group>
      {/* Add other fields */}
      <Button type="submit">Update</Button>
    </Form>
  );
};

export default UserProfile;