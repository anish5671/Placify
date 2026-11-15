import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import ProtectedRoute from './components/ProtectedRoute';
import Layout from './components/Layout';

// Pages
import Login from './pages/Login';
import Register from './pages/Register';
import Dashboard from './pages/Dashboard';
import CompanyPrep from './pages/CompanyPrep';
import CompanyDetail from './pages/CompanyDetail';
import QuestionBank from './pages/QuestionBank';
import DsaPractice from './pages/DsaPractice';
import MockTests from './pages/MockTests';
import TestExam from './pages/TestExam';
import TestResult from './pages/TestResult';
import Profile from './pages/Profile';

export default function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          {/* Public Auth Routes */}
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />

          {/* Protected Routes with Standard Shell */}
          <Route
            element={
              <ProtectedRoute>
                <Layout />
              </ProtectedRoute>
            }
          >
            <Route path="/" element={<Navigate to="/dashboard" replace />} />
            <Route path="/dashboard" element={<Dashboard />} />
            <Route path="/companies" element={<CompanyPrep />} />
            <Route path="/companies/:id" element={<CompanyDetail />} />
            <Route path="/questions" element={<QuestionBank />} />
            <Route path="/dsa" element={<DsaPractice />} />
            <Route path="/tests" element={<MockTests />} />
            <Route path="/tests/:id/exam" element={<TestExam />} />
            <Route path="/tests/:attemptId/result" element={<TestResult />} />
            <Route path="/profile" element={<Profile />} />
          </Route>

          {/* Fallback */}
          <Route path="*" element={<Navigate to="/dashboard" replace />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}
