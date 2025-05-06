import { useState, useEffect } from 'react';
import { fetchJson, sendJson } from '../../services/fetchJson';
import { Card, Form, Button, InputGroup, Alert } from 'react-bootstrap';
import { useAuth } from '../../contexts/AuthContext';

const UserProfile = () => {
  const { user } = useAuth();
  const [form, setForm]   = useState({ username: '', email: '', roles: [] });
  const [edit, setEdit]   = useState({ username: false, email: false });
  const [msg, setMsg]     = useState(null);

  useEffect(() => {
    if (!user) return;
    (async () => {
      try {
        const data = await fetchJson(`/api/users/${user.id}`);
        setForm({
          username: data.username,
          email:    data.email || '',
          roles:    data.roles
        });
      } catch (err) {
        console.error(err);
        setMsg({ variant:'danger', text: 'Failed to load profile.' });
      }
    })();
  }, [user]);

  const toggle = field => setEdit(e => ({ ...e, [field]: !e[field] }));

  const onSubmit = async e => {
    e.preventDefault();
    try {
      await sendJson(`/api/users/${user.id}`, {
        method: 'PATCH',
        body: {
          username: form.username,
          email: form.email
        }
      });
      setEdit({ username:false, email:false });
      setMsg({ variant:'success', text:'Profile updated.' });
    } catch (err) {
      console.error(err);
      setMsg({ variant:'danger', text:'Update failed.' });
    }
  };

  return (
      <Card className="shadow-lg">
        <Card.Header className="bg-primary text-white">
          <h3 className="mb-0">User Profile</h3>
        </Card.Header>
        <Card.Body>
          {msg && <Alert variant={msg.variant}>{msg.text}</Alert>}
          <Form onSubmit={onSubmit}>
            <Form.Group className="mb-3">
              <Form.Label>Username</Form.Label>
              <InputGroup>
                <Form.Control
                    value={form.username}
                    onChange={e=>setForm(f=>({ ...f, username:e.target.value }))}
                    disabled={!edit.username}
                    required
                />
                <Button variant={edit.username ? 'success':'outline-secondary'}
                        onClick={()=>toggle('username')}>
                  {edit.username ? 'Save':'Edit'}
                </Button>
              </InputGroup>
            </Form.Group>

            <Form.Group className="mb-3">
              <Form.Label>Email</Form.Label>
              <InputGroup>
                <Form.Control
                    type="email"
                    value={form.email}
                    onChange={e=>setForm(f=>({ ...f, email:e.target.value }))}
                    disabled={!edit.email}
                />
                <Button variant={edit.email ? 'success':'outline-secondary'}
                        onClick={()=>toggle('email')}>
                  {edit.email ? 'Save':'Edit'}
                </Button>
              </InputGroup>
            </Form.Group>

            <Form.Group className="mb-3">
              <Form.Label>Roles</Form.Label>
              <Form.Control plaintext readOnly
                            value={form.roles.map(r=>r.name.replace('ROLE_','')).join(', ')}
              />
            </Form.Group>

            <div className="d-grid">
              <Button type="submit" variant="primary" size="lg">
                Update Profile
              </Button>
            </div>
          </Form>
        </Card.Body>
      </Card>
  );
};

export default UserProfile;
