import { useState, useEffect } from 'react';
import api from '../api/axios';
import { BookOpen } from 'lucide-react';

const FacultyCourses = () => {
    const [courses, setCourses] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchCourses = async () => {
            try {
                const response = await api.get('/Faculty/allcourses');
                setCourses(response.data);
            } catch (error) {
                console.error("Failed to fetch courses", error);
            } finally {
                setLoading(false);
            }
        };

        fetchCourses();
    }, []);

    return (
        <div className="space-y-6">
            <h1 className="text-2xl font-bold text-gray-900 flex items-center gap-2">
                <BookOpen className="w-8 h-8 text-blue-600" />
                My Courses
            </h1>

            <div className="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
                <div className="overflow-x-auto">
                    <table className="w-full text-left">
                        <thead className="bg-gray-50">
                            <tr>
                                <th className="px-6 py-3 text-xs font-medium text-gray-500 uppercase">Course Name</th>
                                <th className="px-6 py-3 text-xs font-medium text-gray-500 uppercase">Credits</th>
                                <th className="px-6 py-3 text-xs font-medium text-gray-500 uppercase">Department</th>
                            </tr>
                        </thead>
                        <tbody className="divide-y divide-gray-100">
                            {loading ? (
                                <tr>
                                    <td colSpan="3" className="px-6 py-4 text-center text-gray-500">Loading...</td>
                                </tr>
                            ) : courses.length === 0 ? (
                                <tr>
                                    <td colSpan="3" className="px-6 py-4 text-center text-gray-500">No courses assigned</td>
                                </tr>
                            ) : (
                                courses.map((course) => (
                                    <tr key={course.courseId} className="hover:bg-gray-50">
                                        <td className="px-6 py-4 text-sm font-medium text-gray-900">{course.courseName}</td>
                                        <td className="px-6 py-4 text-sm text-gray-500">{course.courseCredits}</td>
                                        <td className="px-6 py-4 text-sm text-gray-500">{course.deptId}</td>
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

export default FacultyCourses;
