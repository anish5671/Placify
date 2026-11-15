import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { 
  CheckCircle2, 
  Target, 
  Award, 
  TrendingUp, 
  ArrowRight, 
  Clock, 
  Building2, 
  Bookmark, 
  Code2, 
  BookOpen, 
  Play, 
  Sparkles,
  BarChart3,
  Calendar
} from 'lucide-react';
import api from '../api/client';
import { useAuth } from '../context/AuthContext';

export default function Dashboard() {
  const { user } = useAuth();
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    fetchDashboardStats();
  }, []);

  const fetchDashboardStats = async () => {
    try {
      setLoading(true);
      const res = await api.get('/progress/dashboard');
      if (res.data.success) {
        setStats(res.data.data);
      }
    } catch (err) {
      setError('Failed to load dashboard metrics');
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <div className="flex flex-col items-center justify-center min-h-[60vh] gap-3">
        <div className="w-10 h-10 border-4 border-indigo-500/20 border-t-indigo-500 rounded-full animate-spin"></div>
        <p className="text-sm text-slate-400">Loading your prep statistics...</p>
      </div>
    );
  }

  const overallReadiness = stats?.overallReadinessPercentage || 0;

  return (
    <div className="space-y-6">
      {/* Welcome Banner */}
      <div className="relative overflow-hidden rounded-3xl bg-gradient-to-r from-indigo-900/80 via-slate-900 to-indigo-950/70 border border-indigo-500/20 p-6 sm:p-8 shadow-xl">
        <div className="relative z-10 flex flex-col md:flex-row md:items-center justify-between gap-6">
          <div className="space-y-2 max-w-xl">
            <div className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-indigo-500/10 border border-indigo-500/20 text-indigo-400 text-xs font-semibold">
              <Sparkles className="w-3.5 h-3.5" />
              <span>Target: {user?.targetCompany || 'Top Tech'} • {user?.targetRole || 'Software Engineer'}</span>
            </div>
            <h2 className="text-2xl sm:text-3xl font-extrabold text-white tracking-tight">
              Welcome back, {user?.fullName || user?.username}! 🎯
            </h2>
            <p className="text-slate-300 text-sm leading-relaxed">
              Track your mock test performance, solve company-tagged DSA questions, and monitor your overall readiness to land your offer.
            </p>
          </div>

          <div className="flex flex-wrap items-center gap-3">
            <Link
              to="/tests"
              className="px-5 py-3 rounded-2xl bg-indigo-600 hover:bg-indigo-500 text-white font-bold text-sm shadow-lg shadow-indigo-600/30 flex items-center gap-2 transition-all hover:scale-[1.02]"
            >
              <Play className="w-4 h-4 fill-white" />
              <span>Take Mock Test</span>
            </Link>
            <Link
              to="/dsa"
              className="px-5 py-3 rounded-2xl bg-slate-800 hover:bg-slate-700/80 text-slate-200 font-semibold text-sm border border-slate-700 transition-all flex items-center gap-2"
            >
              <Code2 className="w-4 h-4 text-indigo-400" />
              <span>Solve DSA Sheets</span>
            </Link>
          </div>
        </div>

        {/* Subtle decorative glow */}
        <div className="absolute right-0 top-0 w-96 h-96 bg-indigo-500/10 rounded-full blur-3xl pointer-events-none" />
      </div>

      {/* 4 Key Metric Cards */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
        {/* Total Questions Solved */}
        <div className="p-5 rounded-2xl bg-slate-900/90 border border-slate-800/80 shadow-sm flex flex-col justify-between">
          <div className="flex items-center justify-between mb-2">
            <span className="text-xs font-semibold text-slate-400 uppercase tracking-wider">Questions Solved</span>
            <div className="w-9 h-9 rounded-xl bg-emerald-500/10 border border-emerald-500/20 text-emerald-400 flex items-center justify-center">
              <CheckCircle2 className="w-5 h-5" />
            </div>
          </div>
          <div>
            <div className="flex items-baseline gap-2">
              <span className="text-2xl sm:text-3xl font-extrabold text-white">
                {stats?.totalQuestionsSolved || 0}
              </span>
              <span className="text-xs text-slate-400">
                / {stats?.totalQuestionsAvailable || 0} Total
              </span>
            </div>
            <div className="w-full bg-slate-800 rounded-full h-1.5 mt-3 overflow-hidden">
              <div 
                className="bg-emerald-500 h-1.5 rounded-full transition-all duration-500" 
                style={{ 
                  width: `${Math.min(100, ((stats?.totalQuestionsSolved || 0) / (stats?.totalQuestionsAvailable || 1)) * 100)}%` 
                }}
              />
            </div>
          </div>
        </div>

        {/* Tests Attempted */}
        <div className="p-5 rounded-2xl bg-slate-900/90 border border-slate-800/80 shadow-sm flex flex-col justify-between">
          <div className="flex items-center justify-between mb-2">
            <span className="text-xs font-semibold text-slate-400 uppercase tracking-wider">Tests Attempted</span>
            <div className="w-9 h-9 rounded-xl bg-indigo-500/10 border border-indigo-500/20 text-indigo-400 flex items-center justify-center">
              <Target className="w-5 h-5" />
            </div>
          </div>
          <div>
            <div className="flex items-baseline gap-2">
              <span className="text-2xl sm:text-3xl font-extrabold text-white">
                {stats?.totalTestsAttempted || 0}
              </span>
              <span className="text-xs text-slate-400">Mock Assessments</span>
            </div>
            <p className="text-[11px] text-indigo-400 mt-2 font-medium flex items-center gap-1">
              <span>View full test history below</span>
            </p>
          </div>
        </div>

        {/* Average Test Score */}
        <div className="p-5 rounded-2xl bg-slate-900/90 border border-slate-800/80 shadow-sm flex flex-col justify-between">
          <div className="flex items-center justify-between mb-2">
            <span className="text-xs font-semibold text-slate-400 uppercase tracking-wider">Average Test Score</span>
            <div className="w-9 h-9 rounded-xl bg-amber-500/10 border border-amber-500/20 text-amber-400 flex items-center justify-center">
              <Award className="w-5 h-5" />
            </div>
          </div>
          <div>
            <div className="flex items-baseline gap-2">
              <span className="text-2xl sm:text-3xl font-extrabold text-white">
                {stats?.averageTestScore || 0}%
              </span>
              <span className="text-xs text-slate-400">Accuracy Score</span>
            </div>
            <div className="w-full bg-slate-800 rounded-full h-1.5 mt-3 overflow-hidden">
              <div 
                className="bg-amber-500 h-1.5 rounded-full transition-all duration-500" 
                style={{ width: `${Math.min(100, stats?.averageTestScore || 0)}%` }}
              />
            </div>
          </div>
        </div>

        {/* Overall Readiness */}
        <div className="p-5 rounded-2xl bg-gradient-to-br from-indigo-950/70 to-slate-900 border border-indigo-500/30 shadow-sm flex flex-col justify-between">
          <div className="flex items-center justify-between mb-2">
            <span className="text-xs font-semibold text-indigo-300 uppercase tracking-wider">Overall Readiness</span>
            <div className="w-9 h-9 rounded-xl bg-indigo-500/20 text-indigo-300 flex items-center justify-center">
              <TrendingUp className="w-5 h-5" />
            </div>
          </div>
          <div>
            <div className="flex items-baseline gap-2">
              <span className="text-2xl sm:text-3xl font-extrabold text-white">
                {overallReadiness}%
              </span>
              <span className="text-xs text-indigo-400 font-semibold">
                {overallReadiness >= 75 ? '🔥 High Readiness' : overallReadiness >= 40 ? '⚡ Progressing' : '🌱 Starting Up'}
              </span>
            </div>
            <div className="w-full bg-slate-800 rounded-full h-1.5 mt-3 overflow-hidden">
              <div 
                className="bg-gradient-to-r from-indigo-500 to-violet-500 h-1.5 rounded-full transition-all duration-500" 
                style={{ width: `${overallReadiness}%` }}
              />
            </div>
          </div>
        </div>
      </div>

      {/* Grid: Topic-wise Performance Charts & Quick Roadmap */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Left: Topic Performance Bars */}
        <div className="lg:col-span-2 p-6 rounded-3xl bg-slate-900/90 border border-slate-800 space-y-4">
          <div className="flex items-center justify-between">
            <div>
              <h3 className="text-base font-bold text-white flex items-center gap-2">
                <BarChart3 className="w-4 h-4 text-indigo-400" />
                <span>Topic-Wise Mastery</span>
              </h3>
              <p className="text-xs text-slate-400">Questions solved across core technical & aptitude domains</p>
            </div>
            <Link to="/questions" className="text-xs font-semibold text-indigo-400 hover:text-indigo-300 flex items-center gap-1">
              <span>Explore Bank</span>
              <ArrowRight className="w-3.5 h-3.5" />
            </Link>
          </div>

          <div className="grid grid-cols-1 sm:grid-cols-2 gap-3 pt-2">
            {stats?.categoryStats?.map((cat) => (
              <div key={cat.category} className="p-3.5 rounded-2xl bg-slate-950/60 border border-slate-800/80 flex flex-col justify-between">
                <div className="flex items-center justify-between text-xs mb-2">
                  <span className="font-semibold text-slate-200">{cat.displayName}</span>
                  <span className="text-indigo-400 font-bold">{cat.solved} / {cat.total}</span>
                </div>
                <div className="space-y-1">
                  <div className="w-full bg-slate-800 rounded-full h-2 overflow-hidden">
                    <div 
                      className="bg-gradient-to-r from-indigo-500 to-violet-500 h-2 rounded-full transition-all duration-500"
                      style={{ width: `${cat.percentage}%` }}
                    />
                  </div>
                  <div className="flex justify-between text-[10px] text-slate-400">
                    <span>Mastery</span>
                    <span>{cat.percentage}%</span>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>

        {/* Right: Company Roadmaps Promo */}
        <div className="p-6 rounded-3xl bg-gradient-to-b from-indigo-950/40 via-slate-900 to-slate-900 border border-indigo-500/20 flex flex-col justify-between space-y-4">
          <div>
            <div className="w-10 h-10 rounded-2xl bg-indigo-500/10 border border-indigo-500/30 text-indigo-400 flex items-center justify-center mb-3">
              <Building2 className="w-5 h-5" />
            </div>
            <h3 className="text-base font-bold text-white mb-1">Company-Specific Roadmaps</h3>
            <p className="text-xs text-slate-300 leading-relaxed">
              Prepare company by company. Discover recruitment rounds, exam patterns, packages, and company-tagged questions for:
            </p>
            <div className="flex flex-wrap gap-2 mt-4">
              {['TCS', 'Amazon', 'Infosys', 'Accenture', 'Google', 'Microsoft'].map(c => (
                <span key={c} className="px-2.5 py-1 rounded-lg bg-slate-800 text-[11px] font-bold text-slate-300 border border-slate-700">
                  {c}
                </span>
              ))}
            </div>
          </div>

          <Link
            to="/companies"
            className="w-full py-2.5 px-4 rounded-xl bg-indigo-600/90 hover:bg-indigo-600 text-white text-xs font-bold text-center shadow-md shadow-indigo-600/20 transition-all flex items-center justify-center gap-1.5"
          >
            <span>Explore Company Roadmaps</span>
            <ArrowRight className="w-3.5 h-3.5" />
          </Link>
        </div>
      </div>

      {/* Recent Mock Test History */}
      <div className="p-6 rounded-3xl bg-slate-900/90 border border-slate-800 space-y-4">
        <div className="flex items-center justify-between">
          <div>
            <h3 className="text-base font-bold text-white flex items-center gap-2">
              <Clock className="w-4 h-4 text-indigo-400" />
              <span>Recent Test Attempts</span>
            </h3>
            <p className="text-xs text-slate-400">Review your past performance and analyze answers</p>
          </div>
          <Link to="/tests" className="text-xs font-semibold text-indigo-400 hover:text-indigo-300 flex items-center gap-1">
            <span>Browse All Tests</span>
            <ArrowRight className="w-3.5 h-3.5" />
          </Link>
        </div>

        {stats?.recentAttempts && stats.recentAttempts.length > 0 ? (
          <div className="overflow-x-auto">
            <table className="w-full text-left text-xs">
              <thead className="bg-slate-950/60 text-slate-400 uppercase tracking-wider text-[10px]">
                <tr>
                  <th className="py-3 px-4 rounded-l-xl">Assessment Name</th>
                  <th className="py-3 px-4">Company</th>
                  <th className="py-3 px-4">Score</th>
                  <th className="py-3 px-4">Accuracy</th>
                  <th className="py-3 px-4">Time Taken</th>
                  <th className="py-3 px-4">Status</th>
                  <th className="py-3 px-4 text-right rounded-r-xl">Action</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-800/60 text-slate-200">
                {stats.recentAttempts.map((att) => {
                  const minutes = Math.floor(att.timeTakenSeconds / 60);
                  const seconds = att.timeTakenSeconds % 60;
                  return (
                    <tr key={att.attemptId} className="hover:bg-slate-800/30 transition-colors">
                      <td className="py-3.5 px-4 font-semibold text-white">
                        {att.testTitle}
                      </td>
                      <td className="py-3.5 px-4 text-slate-300">
                        {att.companyName}
                      </td>
                      <td className="py-3.5 px-4 font-bold text-white">
                        {att.score} / {att.totalMarks}
                      </td>
                      <td className="py-3.5 px-4 font-semibold text-indigo-400">
                        {att.percentage}%
                      </td>
                      <td className="py-3.5 px-4 text-slate-400">
                        {minutes}m {seconds}s
                      </td>
                      <td className="py-3.5 px-4">
                        <span className={`px-2 py-0.5 rounded-full text-[10px] font-bold ${
                          att.passed 
                            ? 'bg-emerald-500/10 text-emerald-400 border border-emerald-500/20' 
                            : 'bg-rose-500/10 text-rose-400 border border-rose-500/20'
                        }`}>
                          {att.passed ? 'PASSED' : 'NEEDS PRACTICE'}
                        </span>
                      </td>
                      <td className="py-3.5 px-4 text-right">
                        <Link
                          to={`/tests/${att.attemptId}/result`}
                          className="px-3 py-1.5 rounded-lg bg-indigo-600/20 hover:bg-indigo-600/30 text-indigo-300 border border-indigo-500/30 text-xs font-semibold transition-all inline-block"
                        >
                          View Analysis
                        </Link>
                      </td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          </div>
        ) : (
          <div className="py-12 text-center rounded-2xl bg-slate-950/40 border border-dashed border-slate-800">
            <Target className="w-10 h-10 text-slate-600 mx-auto mb-2" />
            <p className="text-sm font-semibold text-slate-300">No Mock Tests Attempted Yet</p>
            <p className="text-xs text-slate-500 max-w-sm mx-auto mt-1 mb-4">
              Take your first simulated company assessment to benchmark your speed, accuracy, and readiness.
            </p>
            <Link
              to="/tests"
              className="inline-flex items-center gap-2 px-4 py-2 rounded-xl bg-indigo-600 hover:bg-indigo-500 text-white text-xs font-bold shadow-md shadow-indigo-600/20"
            >
              <span>Take Your First Test</span>
              <ArrowRight className="w-3.5 h-3.5" />
            </Link>
          </div>
        )}
      </div>
    </div>
  );
}
