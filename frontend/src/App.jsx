import { Routes, Route, Navigate } from 'react-router-dom';
import Login from './pages/Login';
import AdminDashboard from './pages/AdminDashboard';
import AdminDepartments from './pages/AdminDepartments';
import AdminFaculty from './pages/AdminFaculty';
import AdminStudents from './pages/AdminStudents';
import AdminCourses from './pages/AdminCourses';
import FacultyDashboard from './pages/FacultyDashboard';
import FacultyCourses from './pages/FacultyCourses';
import FacultyStudents from './pages/FacultyStudents';
import Layout from './components/Layout';
import { useAuth } from './context/AuthContext';

const PrivateRoute = ({ children, allowedRole }) => {
  const { token, role } = useAuth();

  if (!token) {
    return <Navigate to="/login" />;
  }

  if (allowedRole && role !== allowedRole) {
    return <Navigate to={role === 'Admin' ? '/admin' : '/faculty'} />;
  }

  return children;
};

function App() {
  return (
    <Routes>
      <Route path="/login" element={<Login />} />
      <Route path="/" element={<Navigate to="/login" />} />

      {/* Admin Routes */}
      <Route path="/admin/*" element={
        <PrivateRoute allowedRole="Admin">
          <Layout />
        </PrivateRoute>
      }>
        <Route index element={<AdminDashboard />} />
        <Route path="departments" element={<AdminDepartments />} />
        <Route path="faculty" element={<AdminFaculty />} />
        <Route path="students" element={<AdminStudents />} />
        <Route path="courses" element={<AdminCourses />} />
      </Route>

      {/* Faculty Routes */}
      <Route path="/faculty/*" element={
        <PrivateRoute allowedRole="Faculty">
          <Layout />
        </PrivateRoute>
      }>
        <Route index element={<FacultyDashboard />} />
        <Route path="courses" element={<FacultyCourses />} />
        <Route path="students" element={<FacultyStudents />} />
      </Route>
    </Routes>
  );
}

export default App;
