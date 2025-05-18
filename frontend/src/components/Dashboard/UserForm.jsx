import { useState } from 'react';
import { Form, Button } from 'react-bootstrap';

export default function UserForm({ user, onCancel, onSave }) {
    const [form, setForm] = useState({
        id: user?.id,
        username: user?.username || '',
        email: user?.email || '',
        password: '',
        roles: user?.roles?.map(r=>r.name) || []
    });

    const roleOptions = ['ROLE_ADMIN','ROLE_STAFF','ROLE_EMPLOYEE'];

    const toggleRole = (r) =>
        setForm(f => ({
            ...f,
            roles: f.roles.includes(r)
                ? f.roles.filter(x=>x!==r)
                : [...f.roles, r]
        }));

    const submit = (e) => {
        e.preventDefault();
        onSave(form);
    };

    return (
        <Form onSubmit={submit}>
            <Form.Group className="mb-2">
                <Form.Label>Username</Form.Label>
                <Form.Control
                    value={form.username}
                    onChange={e=>setForm({...form,username:e.target.value})}
                    required
                />
            </Form.Group>
            <Form.Group className="mb-2">
                <Form.Label>Email</Form.Label>
                <Form.Control
                    type="email"
                    value={form.email}
                    onChange={e=>setForm({...form,email:e.target.value})}
                />
            </Form.Group>
            {!user && (
                <Form.Group className="mb-2">
                    <Form.Label>Password</Form.Label>
                    <Form.Control
                        type="password"
                        value={form.password}
                        onChange={e=>setForm({...form,password:e.target.value})}
                        required
                    />
                </Form.Group>
            )}
            <Form.Group className="mb-2">
                <Form.Label>Roles</Form.Label>
                {roleOptions.map(r => (
                    <Form.Check
                        key={r}
                        type="checkbox"
                        label={r.replace('ROLE_','')}
                        checked={form.roles.includes(r)}
                        onChange={()=>toggleRole(r)}
                    />
                ))}
            </Form.Group>
            <div className="text-end">
                <Button variant="secondary" onClick={onCancel}>Cancel</Button>{' '}
                <Button variant="primary" type="submit">Save</Button>
            </div>
        </Form>
    );
}
