import { useState, useEffect } from 'react';
import api from '../api/axios';
import { Building2, Plus, Trash2, Users, BookOpen, GraduationCap, UserCheck, ArrowLeft } from 'lucide-react';

const AdminDepartments = () => {
    const [activeTab, setActiveTab] = useState('list');
    const [departments, setDepartments] = useState([]);
    const [loading, setLoading] = useState(false);
    const [message, setMessage] = useState({ type: '', text: '' });
    const [selectedDept, setSelectedDept] = useState(null);
    const [deptDetails, setDeptDetails] = useState({
        hod: null,
        faculties: [],
        courses: [],
        students: []
    });
    const [allHods, setAllHods] = useState([]);

    const [formData, setFormData] = useState({
        deptId: '',
        departmentName: ''
    });

    useEffect(() => {
        if (activeTab === 'list') {
            fetchDepartments();
        } else if (activeTab === 'hods') {
            fetchAllHods();
        }
    }, [activeTab]);

    const fetchDepartments = async () => {
        setLoading(true);
        try {
            const response = await api.get('/department/all');
            setDepartments(response.data);
        } catch (error) {
            console.error("Failed to fetch departments", error);
            setMessage({ type: 'error', text: 'Failed to fetch departments' });
        } finally {
            setLoading(false);
        }
    };

    const fetchAllHods = async () => {
        setLoading(true);
        try {
            const response = await api.get('/department/getAllHod');
            setAllHods(response.data);
        } catch (error) {
            console.error("Failed to fetch HODs", error);
            // Don't show error if 404 (no HODs found)
            if (error.response && error.response.status !== 404) {
                setMessage({ type: 'error', text: 'Failed to fetch HODs' });
            } else {
                setAllHods([]);
            }
        } finally {
            setLoading(false);
        }
    };

    const fetchDeptDetails = async (deptId) => {
        setLoading(true);
        try {
            const [hodRes, facultiesRes, coursesRes, studentsRes] = await Promise.allSettled([
                api.get(`/department/getHod/${deptId}`),
                api.get(`/department/allfaculties/${deptId}`),
                api.get(`/department/allcourses/${deptId}`),
                api.get(`/department/allstudents/${deptId}`)
            ]);

            setDeptDetails({
                hod: hodRes.status === 'fulfilled' ? hodRes.value.data : null,
                faculties: facultiesRes.status === 'fulfilled' ? facultiesRes.value.data : [],
                courses: coursesRes.status === 'fulfilled' ? coursesRes.value.data : [],
                students: studentsRes.status === 'fulfilled' ? studentsRes.value.data : []
            });
        } catch (error) {
            console.error("Failed to fetch department details", error);
            setMessage({ type: 'error', text: 'Failed to fetch some department details' });
        } finally {
            setLoading(false);
        }
    };

    const handleCreate = async (e) => {
        e.preventDefault();
        setLoading(true);
        setMessage({ type: '', text: '' });
        try {
            await api.post('/department/create', formData);
            setMessage({ type: 'success', text: 'Department created successfully!' });
            setFormData({ deptId: '', departmentName: '' });
            fetchDepartments();
        } catch (error) {
            if (error.response && error.response.status === 409) {
                setMessage({ type: 'error', text: 'Department ID already exists!' });
            } else {
                setMessage({ type: 'error', text: 'Failed to create department.' });
            }
        } finally {
            setLoading(false);
        }
    };

    const handleDelete = async (deptId) => {
        if (!window.confirm(`Are you sure you want to delete department ${deptId}? This will delete ALL associated faculty, students, and courses!`)) {
            return;
        }
        setLoading(true);
        try {
            await api.delete(`/department/deleteDepartment/${deptId}`);
            setMessage({ type: 'success', text: 'Department deleted successfully' });
            fetchDepartments();
            if (selectedDept === deptId) {
                setSelectedDept(null);
                setActiveTab('list');
            }
        } catch (error) {
            setMessage({ type: 'error', text: 'Failed to delete department' });
        } finally {
            setLoading(false);
        }
    };

    const handleViewDetails = (deptId) => {
        setSelectedDept(deptId);
        setActiveTab('details');
        fetchDeptDetails(deptId);
    };

    return (
        <div className="space-y-6">
            <div className="flex justify-between items-center">
                <h1 className="text-2xl font-bold text-gray-900 flex items-center gap-2">
                    {activeTab === 'details' && (
                        <button
                            onClick={() => { setActiveTab('list'); setSelectedDept(null); }}
                            className="mr-2 p-1 hover:bg-gray-100 rounded-full"
                        >
                            <ArrowLeft className="w-6 h-6" />
                        </button>
                    )}
                    {activeTab === 'details' ? `Department: ${selectedDept}` : 'Department Management'}
                </h1>

                {activeTab !== 'details' && (
                    <div className="flex bg-gray-100 p-1 rounded-lg">
                        <button
                            onClick={() => setActiveTab('list')}
                            className={`px-4 py-2 rounded-md text-sm font-medium transition-colors ${activeTab === 'list' ? 'bg-white text-blue-600 shadow-sm' : 'text-gray-600'}`}
                        >
                            Departments
                        </button>
                        <button
                            onClick={() => setActiveTab('create')}
                            className={`px-4 py-2 rounded-md text-sm font-medium transition-colors ${activeTab === 'create' ? 'bg-white text-blue-600 shadow-sm' : 'text-gray-600'}`}
                        >
                            Create New
                        </button>
                        <button
                            onClick={() => setActiveTab('hods')}
                            className={`px-4 py-2 rounded-md text-sm font-medium transition-colors ${activeTab === 'hods' ? 'bg-white text-blue-600 shadow-sm' : 'text-gray-600'}`}
                        >
                            All HODs
                        </button>
                    </div>
                )}
            </div>

            {message.text && (
                <div className={`p-4 rounded-lg ${message.type === 'success' ? 'bg-green-50 text-green-700' : 'bg-red-50 text-red-700'}`}>
                    {message.text}
                </div>
            )}

            {activeTab === 'create' && (
                <div className="bg-white rounded-xl shadow-sm border border-gray-100 p-6 max-w-2xl">
                    <h2 className="text-lg font-semibold text-gray-900 mb-6 flex items-center gap-2">
                        <Plus className="w-5 h-5 text-blue-600" />
                        Create New Department
                    </h2>
                    <form onSubmit={handleCreate} className="space-y-4">
                        <div>
                            <label className="block text-sm font-medium text-gray-700 mb-1">Department ID</label>
                            <input
                                type="text"
                                required
                                placeholder="e.g., CSE, ECE, MECH"
                                className="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500 outline-none uppercase"
                                value={formData.deptId}
                                onChange={(e) => setFormData({ ...formData, deptId: e.target.value.toUpperCase() })}
                            />
                        </div>
                        <div>
                            <label className="block text-sm font-medium text-gray-700 mb-1">Department Name</label>
                            <input
                                type="text"
                                required
                                placeholder="e.g., Computer Science & Engineering"
                                className="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500 outline-none"
                                value={formData.departmentName}
                                onChange={(e) => setFormData({ ...formData, departmentName: e.target.value })}
                            />
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
            )}

            {activeTab === 'list' && (
                <div className="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
                    <table className="w-full text-left">
                        <thead className="bg-gray-50">
                            <tr>
                                <th className="px-6 py-3 text-xs font-medium text-gray-500 uppercase">ID</th>
                                <th className="px-6 py-3 text-xs font-medium text-gray-500 uppercase">Name</th>
                                <th className="px-6 py-3 text-xs font-medium text-gray-500 uppercase text-right">Actions</th>
                            </tr>
                        </thead>
                        <tbody className="divide-y divide-gray-100">
                            {departments.length === 0 ? (
                                <tr>
                                    <td colSpan="3" className="px-6 py-4 text-center text-gray-500">No departments found</td>
                                </tr>
                            ) : (
                                departments.map((dept) => (
                                    <tr key={dept.deptId} className="hover:bg-gray-50">
                                        <td className="px-6 py-4 text-sm font-medium text-gray-900">{dept.deptId}</td>
                                        <td className="px-6 py-4 text-sm text-gray-500">{dept.departmentName}</td>
                                        <td className="px-6 py-4 text-sm text-right space-x-2">
                                            <button
                                                onClick={() => handleViewDetails(dept.deptId)}
                                                className="text-blue-600 hover:text-blue-800 font-medium text-sm"
                                            >
                                                View Details
                                            </button>
                                            <button
                                                onClick={() => handleDelete(dept.deptId)}
                                                className="text-red-600 hover:text-red-800 p-1 rounded hover:bg-red-50"
                                                title="Delete Department"
                                            >
                                                <Trash2 className="w-4 h-4" />
                                            </button>
                                        </td>
                                    </tr>
                                ))
                            )}
                        </tbody>
                    </table>
                </div>
            )}

            {activeTab === 'hods' && (
                <div className="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
                    <div className="p-4 border-b border-gray-100">
                        <h2 className="text-lg font-semibold text-gray-900">All Heads of Departments</h2>
                    </div>
                    <table className="w-full text-left">
                        <thead className="bg-gray-50">
                            <tr>
                                <th className="px-6 py-3 text-xs font-medium text-gray-500 uppercase">Name</th>
                                <th className="px-6 py-3 text-xs font-medium text-gray-500 uppercase">Department</th>
                                <th className="px-6 py-3 text-xs font-medium text-gray-500 uppercase">Email</th>
                            </tr>
                        </thead>
                        <tbody className="divide-y divide-gray-100">
                            {allHods.length === 0 ? (
                                <tr>
                                    <td colSpan="3" className="px-6 py-4 text-center text-gray-500">No HODs assigned yet</td>
                                </tr>
                            ) : (
                                allHods.map((hod) => (
                                    <tr key={hod.facultyId} className="hover:bg-gray-50">
                                        <td className="px-6 py-4 text-sm font-medium text-gray-900">{hod.facultyName}</td>
                                        <td className="px-6 py-4 text-sm text-gray-500">{hod.deptId}</td>
                                        <td className="px-6 py-4 text-sm text-gray-500">{hod.email}</td>
                                    </tr>
                                ))
                            )}
                        </tbody>
                    </table>
                </div>
            )}

            {activeTab === 'details' && (
                <div className="space-y-6">
                    {/* HOD Section */}
                    <div className="bg-white p-6 rounded-xl shadow-sm border border-gray-100">
                        <h2 className="text-lg font-semibold text-gray-900 mb-4 flex items-center gap-2">
                            <UserCheck className="w-5 h-5 text-purple-600" />
                            Head of Department
                        </h2>
                        {deptDetails.hod ? (
                            <div className="flex items-center gap-4">
                                <div className="w-12 h-12 bg-purple-100 rounded-full flex items-center justify-center text-purple-600 font-bold text-xl">
                                    {deptDetails.hod.facultyName.charAt(0)}
                                </div>
                                <div>
                                    <h3 className="font-medium text-gray-900">{deptDetails.hod.facultyName}</h3>
                                    <p className="text-sm text-gray-500">{deptDetails.hod.email}</p>
                                </div>
                            </div>
                        ) : (
                            <p className="text-gray-500 italic">No HOD assigned</p>
                        )}
                    </div>

                    <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
                        {/* Faculties List */}
                        <div className="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
                            <div className="p-4 border-b border-gray-100 bg-blue-50 flex justify-between items-center">
                                <h3 className="font-semibold text-blue-900 flex items-center gap-2">
                                    <Users className="w-4 h-4" />
                                    Faculties ({deptDetails.faculties.length})
                                </h3>
                            </div>
                            <div className="max-h-96 overflow-y-auto">
                                {deptDetails.faculties.length === 0 ? (
                                    <p className="p-4 text-center text-gray-500 text-sm">No faculties found</p>
                                ) : (
                                    <ul className="divide-y divide-gray-100">
                                        {deptDetails.faculties.map(f => (
                                            <li key={f.facultyId} className="p-3 hover:bg-gray-50">
                                                <p className="text-sm font-medium text-gray-900">{f.facultyName}</p>
                                                <p className="text-xs text-gray-500">{f.email}</p>
                                            </li>
                                        ))}
                                    </ul>
                                )}
                            </div>
                        </div>

                        {/* Courses List */}
                        <div className="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
                            <div className="p-4 border-b border-gray-100 bg-green-50 flex justify-between items-center">
                                <h3 className="font-semibold text-green-900 flex items-center gap-2">
                                    <BookOpen className="w-4 h-4" />
                                    Courses ({deptDetails.courses.length})
                                </h3>
                            </div>
                            <div className="max-h-96 overflow-y-auto">
                                {deptDetails.courses.length === 0 ? (
                                    <p className="p-4 text-center text-gray-500 text-sm">No courses found</p>
                                ) : (
                                    <ul className="divide-y divide-gray-100">
                                        {deptDetails.courses.map(c => (
                                            <li key={c.courseId} className="p-3 hover:bg-gray-50">
                                                <p className="text-sm font-medium text-gray-900">{c.courseName}</p>
                                                <p className="text-xs text-gray-500">Credits: {c.courseCredits}</p>
                                            </li>
                                        ))}
                                    </ul>
                                )}
                            </div>
                        </div>

                        {/* Students List */}
                        <div className="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
                            <div className="p-4 border-b border-gray-100 bg-orange-50 flex justify-between items-center">
                                <h3 className="font-semibold text-orange-900 flex items-center gap-2">
                                    <GraduationCap className="w-4 h-4" />
                                    Students ({deptDetails.students.length})
                                </h3>
                            </div>
                            <div className="max-h-96 overflow-y-auto">
                                {deptDetails.students.length === 0 ? (
                                    <p className="p-4 text-center text-gray-500 text-sm">No students found</p>
                                ) : (
                                    <ul className="divide-y divide-gray-100">
                                        {deptDetails.students.map(s => (
                                            <li key={s.studentId} className="p-3 hover:bg-gray-50">
                                                <p className="text-sm font-medium text-gray-900">{s.name}</p>
                                                <p className="text-xs text-gray-500">{s.email}</p>
                                            </li>
                                        ))}
                                    </ul>
                                )}
                            </div>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
};

export default AdminDepartments;
