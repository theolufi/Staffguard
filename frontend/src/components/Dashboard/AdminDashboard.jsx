import { useState, useEffect } from 'react';
import { fetchJson, sendJson } from '../../services/fetchJson';
import { Button, Table, Modal } from 'react-bootstrap';
import UserForm from './UserForm';

const AdminDashboard = () => {
  const [users, setUsers] = useState([]);
  const [error, setError] = useState();
  const [showForm, setShowForm] = useState(false);
  const [editingUser, setEditingUser] = useState(null);

  useEffect(() => {
    load();
  }, []);

  async function load() {
    try {
      const data = await fetchJson('/api/users');
      setUsers(data);
    } catch (e) {
      setError(e.message);
    }
  }

  const handleAdd = () => {
    setEditingUser(null);
    setShowForm(true);
  };

  const handleEdit = (u) => {
    setEditingUser(u);
    setShowForm(true);
  };

  const handleDelete = async (u) => {
    if (!window.confirm(`Delete ${u.username}?`)) return;
    try {
      await sendJson(`/api/users/${u.id}`, { method: 'DELETE' });
      await load();
    } catch (e) {
      setError(e.message);
    }
  };

  const handleSave = async (u) => {
    try {
      if (u.id) {
        await sendJson(`/api/users/${u.id}`, { method: 'PUT', body: u });
      } else {
        await sendJson('/api/users', { method: 'POST', body: u });
      }
      setShowForm(false);
      await load();
    } catch (e) {
      setError(e.message);
    }
  };

  return (
      <div>
        <h3>User Management</h3>
        {error && <div className="alert alert-danger">{error}</div>}
        <Button onClick={handleAdd} className="mb-3">
          Add User
        </Button>
        <Table striped hover>
          <thead>
          <tr>
            <th>ID</th><th>Username</th><th>Email</th><th>Roles</th><th>Actions</th>
          </tr>
          </thead>
          <tbody>
          {users.map(u => (
              <tr key={u.id}>
                <td>{u.id}</td>
                <td>{u.username}</td>
                <td>{u.email || '-'}</td>
                <td>{u.roles.map(r=>r.name).join(', ')}</td>
                <td>
                  <Button size="sm" onClick={()=>handleEdit(u)}>Edit</Button>{' '}
                  <Button size="sm" variant="danger" onClick={()=>handleDelete(u)}>
                    Delete
                  </Button>
                </td>
              </tr>
          ))}
          </tbody>
        </Table>

        <Modal show={showForm} onHide={()=>setShowForm(false)}>
          <Modal.Header closeButton>
            <Modal.Title>{editingUser ? 'Edit User' : 'New User'}</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <UserForm
                user={editingUser}
                onCancel={()=>setShowForm(false)}
                onSave={handleSave}
            />
          </Modal.Body>
        </Modal>
      </div>
  );
};

export default AdminDashboard;
