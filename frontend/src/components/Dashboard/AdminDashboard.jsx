import { useState, useEffect } from 'react';
import { Table, Button, Modal, Form, Tabs, Tab } from 'react-bootstrap';
import api from '../../services/api';

const AdminDashboard = () => {
  const [users, setUsers] = useState([]); // Always an array
  const [employees, setEmployees] = useState([]); // Always an array
  const [showUserModal, setShowUserModal] = useState(false);
  const [editingUser, setEditingUser] = useState(null);
  const [newEmployee, setNewEmployee] = useState({
    name: '',
    email: '',
    department: '',
    salary: ''
  });
  const [activeTab, setActiveTab] = useState('users');

  // Helper to ensure the response is always an array:
  const normalizeResponse = (data) => {
    if (Array.isArray(data)) return data;
    if (data && typeof data === 'object') return [data];
    return [];
  };

  const fetchUsers = async () => {
    try {
      const res = await api.get('/api/users');
      console.log("Users API response:", res.data);
      setUsers(normalizeResponse(res.data));
    } catch (error) {
      console.error('Error fetching users:', error);
      setUsers([]);
    }
  };

  const fetchEmployees = async () => {
    try {
      const res = await api.get('/api/employees');
      console.log("Employees API response:", res.data);
      setEmployees(normalizeResponse(res.data));
    } catch (error) {
      console.error('Error fetching employees:', error);
      setEmployees([]);
    }
  };

  useEffect(() => {
    fetchUsers();
    fetchEmployees();
  }, []);

  const handleUserEdit = (user) => {
    setEditingUser(user);
    setShowUserModal(true);
  };

  const handleUserUpdate = async () => {
    try {
      await api.patch(`/api/users/${editingUser.id}`, editingUser);
      setShowUserModal(false);
      fetchUsers();
    } catch (error) {
      console.error('Error updating user:', error);
    }
  };

  const handleEmployeeCreate = async () => {
    try {
      await api.post('/api/employees', newEmployee);
      setNewEmployee({ name: '', email: '', department: '', salary: '' });
      fetchEmployees();
    } catch (error) {
      console.error('Error creating employee:', error);
    }
  };

  return (
    <div>
      <Tabs activeKey={activeTab} onSelect={(k) => setActiveTab(k)} className="mb-3">
        <Tab eventKey="users" title="User Management">
          <h3 style={{ color: 'var(--primary)' }}>User Management</h3>
          <Table striped hover>
            <thead>
              <tr>
                <th>ID</th>
                <th>Username</th>
                <th>Email</th>
                <th>Roles</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {users && users.length > 0 ? (
                users.map((u) => (
                  <tr key={u.id}>
                    <td>{u.id}</td>
                    <td>{u.username}</td>
                    <td>{u.email}</td>
                    <td>{u.roles ? u.roles.map(r => r.name).join(', ') : ''}</td>
                    <td>
                      <Button variant="warning" size="sm" onClick={() => handleUserEdit(u)}>
                        Edit
                      </Button>
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan="5">No users found.</td>
                </tr>
              )}
            </tbody>
          </Table>
          {/* Modal for editing user */}
          <Modal show={showUserModal} onHide={() => setShowUserModal(false)}>
            <Modal.Header closeButton>
              <Modal.Title>Edit User</Modal.Title>
            </Modal.Header>
            <Modal.Body>
              {editingUser && (
                <Form>
                  <Form.Group controlId="editUsername" className="mb-3">
                    <Form.Label>Username</Form.Label>
                    <Form.Control
                      type="text"
                      value={editingUser.username}
                      onChange={(e) =>
                        setEditingUser({ ...editingUser, username: e.target.value })
                      }
                    />
                  </Form.Group>
                  <Form.Group controlId="editEmail" className="mb-3">
                    <Form.Label>Email</Form.Label>
                    <Form.Control
                      type="email"
                      value={editingUser.email || ''}
                      onChange={(e) =>
                        setEditingUser({ ...editingUser, email: e.target.value })
                      }
                    />
                  </Form.Group>
                </Form>
              )}
            </Modal.Body>
            <Modal.Footer>
              <Button variant="secondary" onClick={() => setShowUserModal(false)}>
                Cancel
              </Button>
              <Button variant="primary" onClick={handleUserUpdate}>
                Save Changes
              </Button>
            </Modal.Footer>
          </Modal>
        </Tab>
        <Tab eventKey="employees" title="Employee Management">
          <h3 style={{ color: 'var(--primary)' }}>Employee Management</h3>
          <Form>
            <Form.Group controlId="empName" className="mb-3">
              <Form.Label>Name</Form.Label>
              <Form.Control
                type="text"
                value={newEmployee.name}
                onChange={(e) => setNewEmployee({ ...newEmployee, name: e.target.value })}
              />
            </Form.Group>
            <Form.Group controlId="empEmail" className="mb-3">
              <Form.Label>Email</Form.Label>
              <Form.Control
                type="email"
                value={newEmployee.email}
                onChange={(e) =>
                  setNewEmployee({ ...newEmployee, email: e.target.value })
                }
              />
            </Form.Group>
            <Form.Group controlId="empDepartment" className="mb-3">
              <Form.Label>Department</Form.Label>
              <Form.Control
                type="text"
                value={newEmployee.department}
                onChange={(e) =>
                  setNewEmployee({ ...newEmployee, department: e.target.value })
                }
              />
            </Form.Group>
            <Form.Group controlId="empSalary" className="mb-3">
              <Form.Label>Salary</Form.Label>
              <Form.Control
                type="text"
                value={newEmployee.salary}
                onChange={(e) =>
                  setNewEmployee({ ...newEmployee, salary: e.target.value })
                }
              />
            </Form.Group>
            <Button variant="success" onClick={handleEmployeeCreate}>
              Create Employee
            </Button>
          </Form>
          <hr />
          <h4>Existing Employees</h4>
          <Table striped hover>
            <thead>
              <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Email</th>
                <th>Department</th>
                <th>Salary</th>
              </tr>
            </thead>
            <tbody>
              {employees && employees.length > 0 ? (
                employees.map((emp) => (
                  <tr key={emp.id}>
                    <td>{emp.id}</td>
                    <td>{emp.name}</td>
                    <td>{emp.email}</td>
                    <td>{emp.department}</td>
                    <td>{emp.salary}</td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan="5">No employees found.</td>
                </tr>
              )}
            </tbody>
          </Table>
        </Tab>
      </Tabs>
    </div>
  );
};

export default AdminDashboard;
