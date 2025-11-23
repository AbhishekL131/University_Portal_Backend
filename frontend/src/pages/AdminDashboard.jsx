import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../api/axios';
import { Users, BookOpen, GraduationCap, TrendingUp } from 'lucide-react';

const StatCard = ({ title, value, icon: Icon, color }) => (
    <div className="bg-white p-6 rounded-xl shadow-sm border border-gray-100">
        <div className="flex items-center justify-between">
            <div>
                <p className="text-sm text-gray-500 font-medium">{title}</p>
                <h3 className="text-2xl font-bold text-gray-900 mt-1">{value}</h3>
            </div>
            <div className={`p-3 rounded-lg ${color}`}>
                <Icon className="w-6 h-6 text-white" />
            </div>
        </div>
    </div>
);

const AdminDashboard = () => {
    const navigate = useNavigate();
    const [stats, setStats] = useState({
        faculty: 0,
        students: 0,
        courses: 0
    });

    useEffect(() => {
        const fetchStats = async () => {
            try {
                const response = await api.get('/Admin/stats');
                setStats(response.data);
            } catch (error) {
                console.error("Failed to fetch stats", error);
            }
        };
        fetchStats();
    }, []);

    return (
        <div className="space-y-6">
            <h1 className="text-2xl font-bold text-gray-900">Admin Dashboard</h1>

            <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                <StatCard
                    title="Total Faculty"
                    value={stats.faculty}
                    icon={Users}
                    color="bg-blue-500"
                />
                <StatCard
                    title="Total Students"
                    value={stats.students}
                    icon={GraduationCap}
                    color="bg-green-500"
                />
                <StatCard
                    title="Active Courses"
                    value={stats.courses}
                    icon={BookOpen}
                    color="bg-purple-500"
                />
            </div>

            <div className="bg-white rounded-xl shadow-sm border border-gray-100 p-6">
                <h2 className="text-lg font-semibold text-gray-900 mb-4">Quick Actions</h2>
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
                    <button
                        onClick={() => navigate('/admin/departments')}
                        className="p-4 text-left border rounded-lg hover:bg-gray-50 transition-colors"
                    >
                        <h3 className="font-medium text-orange-600">Manage Departments</h3>
                        <p className="text-sm text-gray-500 mt-1">Create and view departments</p>
                    </button>
                    <button
                        onClick={() => navigate('/admin/faculty')}
                        className="p-4 text-left border rounded-lg hover:bg-gray-50 transition-colors"
                    >
                        <h3 className="font-medium text-blue-600">Add New Faculty</h3>
                        <p className="text-sm text-gray-500 mt-1">Create a new faculty account</p>
                    </button>
                    <button
                        onClick={() => navigate('/admin/students')}
                        className="p-4 text-left border rounded-lg hover:bg-gray-50 transition-colors"
                    >
                        <h3 className="font-medium text-green-600">Register Student</h3>
                        <p className="text-sm text-gray-500 mt-1">Enroll a new student</p>
                    </button>
                    <button
                        onClick={() => navigate('/admin/courses')}
                        className="p-4 text-left border rounded-lg hover:bg-gray-50 transition-colors"
                    >
                        <h3 className="font-medium text-purple-600">Create Course</h3>
                        <p className="text-sm text-gray-500 mt-1">Add a new course to curriculum</p>
                    </button>
                </div>
            </div>
        </div>
    );
};

export default AdminDashboard;
