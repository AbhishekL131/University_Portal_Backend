import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../api/axios';
import { BookOpen, Users, Calendar, Clock, ArrowRight } from 'lucide-react';

const FacultyDashboard = () => {
    const [courses, setCourses] = useState([]);
    const [students, setStudents] = useState([]);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchData = async () => {
            try {
                const [coursesRes, studentsRes] = await Promise.all([
                    api.get('/Faculty/allcourses'),
                    api.get('/Faculty/allStudents')
                ]);
                setCourses(coursesRes.data);
                setStudents(studentsRes.data);
            } catch (error) {
                console.error("Failed to fetch dashboard data", error);
            } finally {
                setLoading(false);
            }
        };

        fetchData();
    }, []);

    return (
        <div className="space-y-8">
            <div className="flex items-center justify-between animate-fade-in">
                <div>
                    <h1 className="text-3xl font-bold text-white">Faculty Dashboard</h1>
                    <p className="text-slate-400 mt-1">Manage your courses and students</p>
                </div>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div
                    onClick={() => navigate('/faculty/courses')}
                    className="glass-card p-8 cursor-pointer hover:bg-white/20 transition-all duration-300 group animate-slide-up"
                    style={{ animationDelay: '0.1s' }}
                >
                    <div className="flex items-center justify-between mb-6">
                        <div className="p-4 bg-blue-500/20 rounded-2xl group-hover:scale-110 transition-transform duration-300">
                            <BookOpen className="w-8 h-8 text-blue-400" />
                        </div>
                        <div className="flex items-center text-blue-400 text-sm font-semibold opacity-0 group-hover:opacity-100 transition-opacity -translate-x-2 group-hover:translate-x-0 duration-300">
                            View All <ArrowRight className="w-4 h-4 ml-1" />
                        </div>
                    </div>
                    <div>
                        <h3 className="text-4xl font-bold text-white mb-2">{courses.length}</h3>
                        <p className="text-slate-400 font-medium">Assigned Courses</p>
                    </div>
                </div>

                <div
                    onClick={() => navigate('/faculty/students')}
                    className="glass-card p-8 cursor-pointer hover:bg-white/20 transition-all duration-300 group animate-slide-up"
                    style={{ animationDelay: '0.2s' }}
                >
                    <div className="flex items-center justify-between mb-6">
                        <div className="p-4 bg-emerald-500/20 rounded-2xl group-hover:scale-110 transition-transform duration-300">
                            <Users className="w-8 h-8 text-emerald-400" />
                        </div>
                        <div className="flex items-center text-emerald-400 text-sm font-semibold opacity-0 group-hover:opacity-100 transition-opacity -translate-x-2 group-hover:translate-x-0 duration-300">
                            View All <ArrowRight className="w-4 h-4 ml-1" />
                        </div>
                    </div>
                    <div>
                        <h3 className="text-4xl font-bold text-white mb-2">{students.length}</h3>
                        <p className="text-slate-400 font-medium">Total Students</p>
                    </div>
                </div>
            </div>

            <div className="glass-card overflow-hidden animate-slide-up" style={{ animationDelay: '0.3s' }}>
                <div className="p-6 border-b border-white/10 flex items-center justify-between">
                    <h2 className="text-xl font-bold text-white">Assigned Courses Overview</h2>
                    <button className="text-sm text-indigo-400 hover:text-indigo-300 transition-colors">View Schedule</button>
                </div>
                <div className="overflow-x-auto">
                    <table className="w-full text-left">
                        <thead className="bg-white/5">
                            <tr>
                                <th className="px-6 py-4 text-xs font-semibold text-slate-400 uppercase tracking-wider">Course Name</th>
                                <th className="px-6 py-4 text-xs font-semibold text-slate-400 uppercase tracking-wider">Credits</th>
                                <th className="px-6 py-4 text-xs font-semibold text-slate-400 uppercase tracking-wider">Department</th>
                                <th className="px-6 py-4 text-xs font-semibold text-slate-400 uppercase tracking-wider">Status</th>
                            </tr>
                        </thead>
                        <tbody className="divide-y divide-white/5">
                            {loading ? (
                                <tr>
                                    <td colSpan="4" className="px-6 py-8 text-center text-slate-400">
                                        <div className="flex items-center justify-center gap-2">
                                            <div className="w-2 h-2 bg-slate-400 rounded-full animate-bounce" />
                                            <div className="w-2 h-2 bg-slate-400 rounded-full animate-bounce delay-75" />
                                            <div className="w-2 h-2 bg-slate-400 rounded-full animate-bounce delay-150" />
                                        </div>
                                    </td>
                                </tr>
                            ) : courses.length === 0 ? (
                                <tr>
                                    <td colSpan="4" className="px-6 py-8 text-center text-slate-400">No courses assigned</td>
                                </tr>
                            ) : (
                                courses.map((course) => (
                                    <tr key={course.courseId} className="hover:bg-white/5 transition-colors">
                                        <td className="px-6 py-4">
                                            <div className="flex items-center gap-3">
                                                <div className="w-8 h-8 rounded-lg bg-indigo-500/20 flex items-center justify-center text-indigo-400 font-bold text-xs">
                                                    {course.courseName.substring(0, 2).toUpperCase()}
                                                </div>
                                                <span className="text-sm font-medium text-white">{course.courseName}</span>
                                            </div>
                                        </td>
                                        <td className="px-6 py-4 text-sm text-slate-300">
                                            <div className="flex items-center gap-2">
                                                <Clock className="w-4 h-4 text-slate-500" />
                                                {course.courseCredits} Credits
                                            </div>
                                        </td>
                                        <td className="px-6 py-4 text-sm text-slate-300">{course.deptId}</td>
                                        <td className="px-6 py-4">
                                            <span className="px-2 py-1 rounded-md bg-emerald-500/10 text-emerald-400 text-xs font-medium border border-emerald-500/20">
                                                Active
                                            </span>
                                        </td>
                                    </tr>
                                ))
                            )}
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    );
};

export default FacultyDashboard;
