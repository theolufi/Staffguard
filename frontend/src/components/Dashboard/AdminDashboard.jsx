import { useState, useEffect } from 'react';
import { fetchJson } from '../../services/fetchJson';
import { Button, Table } from 'react-bootstrap';

const AdminDashboard = () => {
  const [users, setUsers]   = useState([]);
  const [error, setError]   = useState(null);

  useEffect(() => {
    (async () => {
      try {
        const data = await fetchJson('/api/users');
        setUsers(Array.isArray(data) ? data : [data]);
      } catch (err) {
        console.error(err);
        setError(err.message);
      }
    })();
  }, []);

  if (error) {
    return <div className="alert alert-danger">Error loading users: {error}</div>;
  }

  return (
      <div>
        <h3>User Management</h3>
        <Button variant="success" className="mb-3" disabled>
          <i className="bi bi-plus-lg"></i> Add User (TBD)
        </Button>
        <Table striped hover>
          <thead>
          <tr>
            <th>ID</th><th>Username</th><th>Email</th><th>Roles</th>
          </tr>
          </thead>
          <tbody>
          {users.map(u => (
              <tr key={u.id}>
                <td>{u.id}</td>
                <td>{u.username}</td>
                <td>{u.email || '-'}</td>
                <td>{u.roles.map(r=>r.name.replace('ROLE_','')).join(', ')}</td>
              </tr>
          ))}
          </tbody>
        </Table>
      </div>
  );
};

export default AdminDashboard;
