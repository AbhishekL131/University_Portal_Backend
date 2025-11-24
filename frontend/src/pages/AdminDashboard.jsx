import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../api/axios';
import { Users, BookOpen, GraduationCap, TrendingUp, Plus, ArrowRight } from 'lucide-react';

const StatCard = ({ title, value, icon: Icon, color, delay }) => (
    <div
        className="glass-card p-6 hover:bg-white/20 transition-all duration-300 animate-slide-up group"
        style={{ animationDelay: delay }}
    >
        <div className="flex items-center justify-between mb-4">
            <div className={`p-3 rounded-xl ${color} bg-opacity-20 text-white group-hover:scale-110 transition-transform duration-300`}>
                <Icon className="w-6 h-6" />
            </div>
            <span className="flex items-center text-xs font-medium text-emerald-400 bg-emerald-400/10 px-2 py-1 rounded-lg">
                <TrendingUp className="w-3 h-3 mr-1" /> +12%
            </span>
        </div>
        <div>
            <h3 className="text-3xl font-bold text-white mb-1">{value}</h3>
            <p className="text-sm text-slate-400 font-medium">{title}</p>
        </div>
    </div>
);

const QuickAction = ({ title, desc, color, onClick, delay }) => (
    <button
        onClick={onClick}
        className="glass-card p-6 text-left hover:bg-white/20 transition-all duration-300 group animate-slide-up w-full"
        style={{ animationDelay: delay }}
    >
        <div className={`w-10 h-10 rounded-xl ${color} flex items-center justify-center mb-4 group-hover:scale-110 transition-transform duration-300 shadow-lg`}>
            <Plus className="w-5 h-5 text-white" />
        </div>
        <h3 className="font-bold text-white text-lg mb-1 group-hover:text-indigo-300 transition-colors">{title}</h3>
        <p className="text-sm text-slate-400 mb-4">{desc}</p>
        <div className="flex items-center text-xs font-semibold text-indigo-400 uppercase tracking-wider group-hover:translate-x-1 transition-transform">
            Action <ArrowRight className="w-3 h-3 ml-1" />
        </div>
    </button>
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
        <div className="space-y-8">
            <div className="flex items-center justify-between animate-fade-in">
                <div>
                    <h1 className="text-3xl font-bold text-white">Dashboard Overview</h1>
                    <p className="text-slate-400 mt-1">Welcome back, Admin</p>
                </div>
                <div className="text-sm text-slate-400 bg-white/5 px-4 py-2 rounded-lg border border-white/10">
                    {new Date().toLocaleDateString('en-US', { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' })}
                </div>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                <StatCard
                    title="Total Faculty"
                    value={stats.faculty}
                    icon={Users}
                    color="bg-blue-500"
                    delay="0.1s"
                />
                <StatCard
                    title="Total Students"
                    value={stats.students}
                    icon={GraduationCap}
                    color="bg-emerald-500"
                    delay="0.2s"
                />
                <StatCard
                    title="Active Courses"
                    value={stats.courses}
                    icon={BookOpen}
                    color="bg-violet-500"
                    delay="0.3s"
                />
            </div>

            <div className="space-y-4">
                <h2 className="text-xl font-bold text-white animate-fade-in" style={{ animationDelay: '0.4s' }}>Quick Actions</h2>
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
                    <QuickAction
                        title="Manage Depts"
                        desc="Create & view departments"
                        color="bg-orange-500"
                        onClick={() => navigate('/admin/departments')}
                        delay="0.5s"
                    />
                    <QuickAction
                        title="Add Faculty"
                        desc="Create faculty account"
                        color="bg-blue-500"
                        onClick={() => navigate('/admin/faculty')}
                        delay="0.6s"
                    />
                    <QuickAction
                        title="Register Student"
                        desc="Enroll a new student"
                        color="bg-emerald-500"
                        onClick={() => navigate('/admin/students')}
                        delay="0.7s"
                    />
                    <QuickAction
                        title="Create Course"
                        desc="Add course to curriculum"
                        color="bg-violet-500"
                        onClick={() => navigate('/admin/courses')}
                        delay="0.8s"
                    />
                </div>
            </div>
        </div>
    );
};

export default AdminDashboard;
