import { useState } from 'react';
import api from '../api/axios';
import { UserPlus, Search, Trash2 } from 'lucide-react';

const AdminFaculty = () => {
    const [activeTab, setActiveTab] = useState('create');
    const [deptIdSearch, setDeptIdSearch] = useState('');
    const [faculties, setFaculties] = useState([]);
    const [loading, setLoading] = useState(false);
    const [message, setMessage] = useState({ type: '', text: '' });

    const [formData, setFormData] = useState({
        facultyId: '',
        userName: '',
        password: '',
        facultyName: '',
        email: '',
        deptId: ''
    });

    const handleInputChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleCreate = async (e) => {
        e.preventDefault();
        setLoading(true);
        setMessage({ type: '', text: '' });
        try {
            await api.post('/Admin/createFaculty', formData);
            setMessage({ type: 'success', text: 'Faculty created successfully!' });
            setFormData({ facultyId: '', userName: '', password: '', facultyName: '', email: '', deptId: '' });
        } catch (error) {
            setMessage({ type: 'error', text: 'Failed to create faculty. Check Department ID.' });
        } finally {
            setLoading(false);
        }
    };

    const handleSearch = async (e) => {
        e.preventDefault();
        if (!deptIdSearch) return;
        setLoading(true);
        try {
            const response = await api.get(`/department/allfaculties/${deptIdSearch}`);
            setFaculties(response.data);
        } catch (error) {
            setFaculties([]);
            setMessage({ type: 'error', text: 'No faculties found or invalid Department ID.' });
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="space-y-6">
            <div className="flex justify-between items-center">
                <h1 className="text-2xl font-bold text-gray-900">Faculty Management</h1>
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
                        <UserPlus className="w-5 h-5 text-blue-600" />
                        Add New Faculty
                    </h2>
                    <form onSubmit={handleCreate} className="space-y-4">
                        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-1">Full Name</label>
                                <input
                                    type="text"
                                    name="facultyName"
                                    required
                                    className="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500 outline-none"
                                    value={formData.facultyName}
                                    onChange={handleInputChange}
                                />
                            </div>
                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-1">Email</label>
                                <input
                                    type="email"
                                    name="email"
                                    required
                                    className="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500 outline-none"
                                    value={formData.email}
                                    onChange={handleInputChange}
                                />
                            </div>
                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-1">Username</label>
                                <input
                                    type="text"
                                    name="userName"
                                    required
                                    className="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500 outline-none"
                                    value={formData.userName}
                                    onChange={handleInputChange}
                                />
                            </div>
                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-1">Password</label>
                                <input
                                    type="password"
                                    name="password"
                                    required
                                    className="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500 outline-none"
                                    value={formData.password}
                                    onChange={handleInputChange}
                                />
                            </div>
                            <div className="md:col-span-2">
                                <label className="block text-sm font-medium text-gray-700 mb-1">Department ID</label>
                                <input
                                    type="text"
                                    name="deptId"
                                    required
                                    placeholder="e.g., CS, IT, MECH"
                                    className="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500 outline-none"
                                    value={formData.deptId}
                                    onChange={handleInputChange}
                                />
                            </div>
                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-1">Faculty ID</label>
                                <input
                                    type="text"
                                    name="facultyId"
                                    required
                                    placeholder="e.g., CSEF001"
                                    className="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500 outline-none"
                                    value={formData.facultyId}
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
                                {loading ? 'Creating...' : 'Create Faculty'}
                            </button>
                        </div>
                    </form>
                </div>
            ) : (
                <div className="space-y-6">
                    <div className="bg-white rounded-xl shadow-sm border border-gray-100 p-6">
                        <form onSubmit={handleSearch} className="flex gap-4">
                            <div className="flex-1">
                                <input
                                    type="text"
                                    placeholder="Enter Department ID to search (e.g., CS)"
                                    className="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500 outline-none"
                                    value={deptIdSearch}
                                    onChange={(e) => setDeptIdSearch(e.target.value)}
                                />
                            </div>
                            <button
                                type="submit"
                                disabled={loading}
                                className="bg-gray-900 hover:bg-gray-800 text-white px-6 py-2 rounded-lg font-medium transition-colors flex items-center gap-2"
                            >
                                <Search className="w-4 h-4" />
                                Search
                            </button>
                        </form>
                    </div>

                    {faculties.length > 0 && (
                        <div className="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
                            <table className="w-full text-left">
                                <thead className="bg-gray-50">
                                    <tr>
                                        <th className="px-6 py-3 text-xs font-medium text-gray-500 uppercase">ID</th>
                                        <th className="px-6 py-3 text-xs font-medium text-gray-500 uppercase">Name</th>
                                        <th className="px-6 py-3 text-xs font-medium text-gray-500 uppercase">Username</th>
                                        <th className="px-6 py-3 text-xs font-medium text-gray-500 uppercase">Email</th>
                                        <th className="px-6 py-3 text-xs font-medium text-gray-500 uppercase">Joined</th>
                                    </tr>
                                </thead>
                                <tbody className="divide-y divide-gray-100">
                                    {faculties.map((faculty) => (
                                        <tr key={faculty.facultyId} className="hover:bg-gray-50">
                                            <td className="px-6 py-4 text-sm font-medium text-gray-900">{faculty.facultyId}</td>
                                            <td className="px-6 py-4 text-sm font-medium text-gray-900">{faculty.facultyName}</td>
                                            <td className="px-6 py-4 text-sm text-gray-500">{faculty.userName}</td>
                                            <td className="px-6 py-4 text-sm text-gray-500">{faculty.email}</td>
                                            <td className="px-6 py-4 text-sm text-gray-500">
                                                {new Date(faculty.dateOfJoining).toLocaleDateString()}
                                            </td>
                                        </tr>
                                    ))}
                                </tbody>
                            </table>
                        </div>
                    )}
                </div>
            )}
        </div>
    );
};

export default AdminFaculty;
