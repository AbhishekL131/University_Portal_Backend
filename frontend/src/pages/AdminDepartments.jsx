import { useState, useEffect } from 'react';
import api from '../api/axios';
import { Building2, Plus, Trash2 } from 'lucide-react';

const AdminDepartments = () => {
    const [activeTab, setActiveTab] = useState('create');
    const [departments, setDepartments] = useState([]); // Note: There isn't a direct "list all departments" API in the controller I saw, but I'll check if I can use something else or if I need to add one.
    // Wait, DepartmentController doesn't have getAllDepartments!
    // It has getHod, allfaculties, allcourses, allstudents.
    // But no "getAllDepartments".
    // I need to add that to the backend too.

    const [loading, setLoading] = useState(false);
    const [message, setMessage] = useState({ type: '', text: '' });

    useEffect(() => {
        if (activeTab === 'list') {
            const fetchDepartments = async () => {
                try {
                    const response = await api.get('/department/all');
                    setDepartments(response.data);
                } catch (error) {
                    console.error("Failed to fetch departments", error);
                    setMessage({ type: 'error', text: 'Failed to load departments. Ensure backend is running and updated.' });
                }
            };
            fetchDepartments();
        }
    }, [activeTab]);

    const [formData, setFormData] = useState({
        deptId: '',
        deptName: ''
    });

    const handleInputChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleCreate = async (e) => {
        e.preventDefault();
        setLoading(true);
        setMessage({ type: '', text: '' });
        try {
            await api.post('/department/create', formData);
            setMessage({ type: 'success', text: 'Department created successfully!' });
            setFormData({ deptId: '', deptName: '' });
        } catch (error) {
            if (error.response && error.response.status === 409) {
                setMessage({ type: 'error', text: 'Department ID already exists.' });
            } else {
                setMessage({ type: 'error', text: 'Failed to create department.' });
            }
        } finally {
            setLoading(false);
        }
    };

    const handleDelete = async (deptId) => {
        if (!window.confirm(`Are you sure you want to delete department "${deptId}"? This will also delete all associated faculty, students, and courses.`)) return;

        try {
            await api.delete(`/department/deleteDepartment/${deptId}`);
            setMessage({ type: 'success', text: 'Department deleted successfully' });
            setDepartments(departments.filter(d => d.deptId !== deptId));
        } catch (error) {
            console.error("Failed to delete department", error);
            setMessage({ type: 'error', text: 'Failed to delete department' });
        }
    };

    return (
        <div className="space-y-6">
            <div className="flex justify-between items-center">
                <h1 className="text-2xl font-bold text-gray-900">Department Management</h1>
                <div className="flex bg-gray-100 p-1 rounded-lg">
                    <button
                        onClick={() => setActiveTab('create')}
                        className={`px-4 py-2 rounded-md text-sm font-medium transition-colors ${activeTab === 'create' ? 'bg-white text-blue-600 shadow-sm' : 'text-gray-600'
                            }`}
                    >
                        Create New
                    </button>
                    <button
                        onClick={() => setActiveTab('list')}
                        className={`px-4 py-2 rounded-md text-sm font-medium transition-colors ${activeTab === 'list' ? 'bg-white text-blue-600 shadow-sm' : 'text-gray-600'
                            }`}
                    >
                        View List
                    </button>
                </div>
            </div>

            {message.text && (
                <div className={`p-4 rounded-lg ${message.type === 'success' ? 'bg-green-50 text-green-700' : 'bg-red-50 text-red-700'}`}>
                    {message.text}
                </div>
            )}

            {activeTab === 'create' ? (
                <div className="bg-white rounded-xl shadow-sm border border-gray-100 p-6 max-w-2xl">
                    <h2 className="text-lg font-semibold text-gray-900 mb-6 flex items-center gap-2">
                        <Plus className="w-5 h-5 text-blue-600" />
                        Add New Department
                    </h2>
                    <form onSubmit={handleCreate} className="space-y-4">
                        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-1">Department ID</label>
                                <input
                                    type="text"
                                    name="deptId"
                                    required
                                    placeholder="e.g., CS"
                                    className="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500 outline-none"
                                    value={formData.deptId}
                                    onChange={handleInputChange}
                                />
                            </div>
                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-1">Department Name</label>
                                <input
                                    type="text"
                                    name="deptName"
                                    required
                                    placeholder="e.g., Computer Science"
                                    className="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500 outline-none"
                                    value={formData.deptName}
                                    onChange={handleInputChange}
                                />
                            </div>

                        </div>
                        <div className="pt-4">
                            <button
                                type="submit"
                                disabled={loading}
                                className="w-full bg-blue-600 hover:bg-blue-700 text-white font-semibold py-2.5 rounded-lg transition-colors disabled:opacity-70"
                            >
                                {loading ? 'Creating...' : 'Create Department'}
                            </button>
                        </div>
                    </form>
                </div>
            ) : (
                <div className="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
                    {loading ? (
                        <div className="p-6 text-center text-gray-500">Loading departments...</div>
                    ) : departments.length === 0 ? (
                        <div className="p-6 text-center text-gray-500">No departments found.</div>
                    ) : (
                        <table className="w-full text-left">
                            <thead className="bg-gray-50">
                                <tr>
                                    <th className="px-6 py-3 text-xs font-medium text-gray-500 uppercase">ID</th>
                                    <th className="px-6 py-3 text-xs font-medium text-gray-500 uppercase">Name</th>
                                    <th className="px-6 py-3 text-xs font-medium text-gray-500 uppercase">Actions</th>
                                </tr>
                            </thead>
                            <tbody className="divide-y divide-gray-100">
                                {departments.map((dept) => (
                                    <tr key={dept.deptId} className="hover:bg-gray-50">
                                        <td className="px-6 py-4 text-sm font-medium text-gray-900">{dept.deptId}</td>
                                        <td className="px-6 py-4 text-sm text-gray-900">{dept.deptName}</td>
                                        <td className="px-6 py-4 text-sm">
                                            <button
                                                onClick={() => handleDelete(dept.deptId)}
                                                className="text-red-600 hover:text-red-900 p-2 hover:bg-red-50 rounded-full transition-colors"
                                                title="Delete Department"
                                            >
                                                <Trash2 className="w-4 h-4" />
                                            </button>
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    )}
                </div>
            )}
        </div>
    );
};

export default AdminDepartments;
