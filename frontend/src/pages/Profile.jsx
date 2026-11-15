import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { 
  User, 
  Mail, 
  Building2, 
  Briefcase, 
  GraduationCap, 
  Edit3, 
  Award, 
  CheckCircle2, 
  Bookmark, 
  Clock, 
  Target,
  X,
  Save,
  RotateCcw
} from 'lucide-react';
import api from '../api/client';
import { useAuth } from '../context/AuthContext';

export default function Profile() {
  const { user, updateUser } = useAuth();
  const [profile, setProfile] = useState(null);
  const [attempts, setAttempts] = useState([]);
  const [bookmarks, setBookmarks] = useState([]);
  const [loading, setLoading] = useState(true);

  // Edit Modal
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);
  const [editForm, setEditForm] = useState({
    fullName: '',
    targetCompany: '',
    targetRole: '',
    graduationYear: 2025,
  });
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    fetchUserData();
  }, []);

  const fetchUserData = async () => {
    try {
      setLoading(true);
      const [profRes, attRes, bmarkRes] = await Promise.all([
        api.get('/user/profile'),
        api.get('/results/my-attempts'),
        api.get('/questions/bookmarked'),
      ]);

      if (profRes.data.success) {
        setProfile(profRes.data.data);
        setEditForm({
          fullName: profRes.data.data.fullName || '',
          targetCompany: profRes.data.data.targetCompany || '',
          targetRole: profRes.data.data.targetRole || '',
          graduationYear: profRes.data.data.graduationYear || 2025,
        });
      }
      if (attRes.data.success) setAttempts(attRes.data.data);
      if (bmarkRes.data.success) setBookmarks(bmarkRes.data.data);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleUpdateProfile = async (e) => {
    e.preventDefault();
    setSaving(true);
    try {
      const res = await api.put('/user/profile', {
        ...editForm,
        graduationYear: parseInt(editForm.graduationYear) || 2025,
      });

      if (res.data.success) {
        setProfile(res.data.data);
        updateUser(res.data.data);
        setIsEditModalOpen(false);
      }
    } catch (err) {
      console.error(err);
      alert('Failed to update profile');
    } finally {
      setSaving(false);
    }
  };

  if (loading) {
    return (
      <div className="flex flex-col items-center justify-center min-h-[60vh] gap-3">
        <div className="w-10 h-10 border-4 border-indigo-500/20 border-t-indigo-500 rounded-full animate-spin"></div>
        <p className="text-sm text-slate-400">Loading candidate profile...</p>
      </div>
    );
  }

  const getInitials = (name) => {
    if (!name) return 'PT';
    return name.split(' ').map(n => n[0]).join('').substring(0, 2).toUpperCase();
  };

  return (
    <div className="space-y-6 max-w-6xl mx-auto pb-12">
      {/* Profile Card Header */}
      <div className="p-6 sm:p-8 rounded-3xl bg-slate-900 border border-slate-800 shadow-xl relative overflow-hidden">
        <div className="relative z-10 flex flex-col md:flex-row md:items-center justify-between gap-6">
          <div className="flex items-start sm:items-center gap-5">
            <div className="w-20 h-20 rounded-2xl bg-gradient-to-tr from-indigo-500 to-violet-500 flex items-center justify-center font-black text-white text-2xl shadow-xl shadow-indigo-600/25 flex-shrink-0">
              {getInitials(profile?.fullName)}
            </div>
            <div className="space-y-1">
              <div className="flex items-center gap-2">
                <h2 className="text-2xl font-extrabold text-white tracking-tight">
                  {profile?.fullName || profile?.username}
                </h2>
                <span className="text-[10px] uppercase font-bold px-2 py-0.5 rounded bg-indigo-500/20 text-indigo-400 border border-indigo-500/30">
                  {profile?.role || 'CANDIDATE'}
                </span>
              </div>
              <p className="text-xs text-slate-400 flex items-center gap-2">
                <Mail className="w-3.5 h-3.5" />
                <span>{profile?.email}</span>
                <span>•</span>
                <span>@{profile?.username}</span>
              </p>
              <div className="flex flex-wrap items-center gap-3 pt-2 text-xs text-slate-300">
                <span className="flex items-center gap-1.5 bg-slate-950/70 px-2.5 py-1 rounded-lg border border-slate-800">
                  <Building2 className="w-3.5 h-3.5 text-indigo-400" />
                  Target: <strong className="text-white">{profile?.targetCompany || 'General'}</strong>
                </span>
                <span className="flex items-center gap-1.5 bg-slate-950/70 px-2.5 py-1 rounded-lg border border-slate-800">
                  <Briefcase className="w-3.5 h-3.5 text-slate-400" />
                  Role: <strong className="text-white">{profile?.targetRole || 'Software Engineer'}</strong>
                </span>
                <span className="flex items-center gap-1.5 bg-slate-950/70 px-2.5 py-1 rounded-lg border border-slate-800">
                  <GraduationCap className="w-3.5 h-3.5 text-emerald-400" />
                  Batch: <strong className="text-white">{profile?.graduationYear || 2025}</strong>
                </span>
              </div>
            </div>
          </div>

          <button
            onClick={() => setIsEditModalOpen(true)}
            className="px-4 py-2.5 rounded-xl bg-slate-800 hover:bg-slate-700 text-slate-200 text-xs font-bold transition-all flex items-center justify-center gap-2 border border-slate-700 shadow-sm"
          >
            <Edit3 className="w-4 h-4 text-indigo-400" />
            <span>Edit Profile Details</span>
          </button>
        </div>

        {/* Ambient Glow */}
        <div className="absolute right-0 top-0 w-80 h-80 bg-indigo-500/10 rounded-full blur-3xl pointer-events-none" />
      </div>

      {/* Test History Log */}
      <div className="p-6 rounded-3xl bg-slate-900 border border-slate-800 space-y-4">
        <div className="flex items-center justify-between">
          <div>
            <h3 className="text-base font-bold text-white flex items-center gap-2">
              <Clock className="w-4 h-4 text-indigo-400" />
              <span>Assessment History & Results Log</span>
            </h3>
            <p className="text-xs text-slate-400">All tests taken with full score records</p>
          </div>
          <Link to="/tests" className="text-xs font-bold text-indigo-400 hover:text-indigo-300">
            Take Another Test →
          </Link>
        </div>

        {attempts.length > 0 ? (
          <div className="overflow-x-auto">
            <table className="w-full text-left text-xs">
              <thead className="bg-slate-950 text-slate-400 uppercase tracking-wider text-[10px]">
                <tr>
                  <th className="py-3 px-4 rounded-l-xl">Test Name</th>
                  <th className="py-3 px-4">Category</th>
                  <th className="py-3 px-4">Score</th>
                  <th className="py-3 px-4">Accuracy</th>
                  <th className="py-3 px-4">Status</th>
                  <th className="py-3 px-4">Date</th>
                  <th className="py-3 px-4 text-right rounded-r-xl">Action</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-800/60 text-slate-200">
                {attempts.map(att => (
                  <tr key={att.attemptId} className="hover:bg-slate-800/30 transition-colors">
                    <td className="py-3.5 px-4 font-bold text-white">{att.testTitle}</td>
                    <td className="py-3.5 px-4 text-slate-300">{att.category}</td>
                    <td className="py-3.5 px-4 font-extrabold text-white">{att.score} / {att.totalMarks}</td>
                    <td className="py-3.5 px-4 text-indigo-400 font-bold">{att.percentage}%</td>
                    <td className="py-3.5 px-4">
                      <span className={`px-2 py-0.5 rounded-full text-[10px] font-black uppercase ${
                        att.passed ? 'bg-emerald-500/10 text-emerald-400 border border-emerald-500/20' : 'bg-rose-500/10 text-rose-400 border border-rose-500/20'
                      }`}>
                        {att.passed ? 'PASSED' : 'NEEDS PRACTICE'}
                      </span>
                    </td>
                    <td className="py-3.5 px-4 text-slate-400">
                      {new Date(att.completedAt).toLocaleDateString()}
                    </td>
                    <td className="py-3.5 px-4 text-right">
                      <Link
                        to={`/tests/${att.attemptId}/result`}
                        className="px-3 py-1.5 rounded-lg bg-indigo-600/20 hover:bg-indigo-600/30 text-indigo-300 border border-indigo-500/30 text-xs font-semibold"
                      >
                        Review Result
                      </Link>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        ) : (
          <div className="py-8 text-center text-xs text-slate-500">
            No test history yet. Attempt a mock test to track your performance history here.
          </div>
        )}
      </div>

      {/* Bookmarked Questions */}
      <div className="p-6 rounded-3xl bg-slate-900 border border-slate-800 space-y-4">
        <div>
          <h3 className="text-base font-bold text-white flex items-center gap-2">
            <Bookmark className="w-4 h-4 text-amber-400 fill-amber-400" />
            <span>Saved Questions ({bookmarks.length})</span>
          </h3>
          <p className="text-xs text-slate-400">Questions you marked for revision</p>
        </div>

        {bookmarks.length > 0 ? (
          <div className="space-y-3">
            {bookmarks.map(q => (
              <div key={q.id} className="p-4 rounded-2xl bg-slate-950/70 border border-slate-800 flex items-center justify-between gap-4">
                <div className="space-y-1">
                  <div className="flex items-center gap-2">
                    <span className="text-[10px] uppercase font-bold px-2 py-0.5 rounded bg-slate-800 text-slate-300">
                      {q.category}
                    </span>
                    <span className={`text-[10px] uppercase font-bold px-2 py-0.5 rounded ${
                      q.difficulty === 'HARD' ? 'bg-rose-500/10 text-rose-400' :
                      q.difficulty === 'MEDIUM' ? 'bg-amber-500/10 text-amber-400' :
                      'bg-emerald-500/10 text-emerald-400'
                    }`}>
                      {q.difficulty}
                    </span>
                  </div>
                  <h4 className="text-xs sm:text-sm font-bold text-white">{q.title}</h4>
                </div>

                <Link
                  to="/questions"
                  className="px-3 py-1.5 rounded-xl bg-slate-800 hover:bg-indigo-600 text-slate-200 hover:text-white text-xs font-bold transition-all flex-shrink-0"
                >
                  Practice
                </Link>
              </div>
            ))}
          </div>
        ) : (
          <div className="py-8 text-center text-xs text-slate-500">
            No bookmarked questions yet. Click the bookmark icon in the Question Bank to save problems for quick revision.
          </div>
        )}
      </div>

      {/* Edit Profile Modal */}
      {isEditModalOpen && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-slate-950/80 backdrop-blur-md">
          <div className="bg-slate-900 border border-slate-800 rounded-3xl max-w-md w-full p-6 shadow-2xl space-y-4 animate-scaleIn">
            <div className="flex items-center justify-between border-b border-slate-800 pb-3">
              <h3 className="text-base font-bold text-white">Edit Profile & Target</h3>
              <button onClick={() => setIsEditModalOpen(false)} className="text-slate-400 hover:text-white">
                <X className="w-5 h-5" />
              </button>
            </div>

            <form onSubmit={handleUpdateProfile} className="space-y-4 text-xs">
              <div>
                <label className="block text-slate-300 font-semibold mb-1">Full Name</label>
                <input
                  type="text"
                  required
                  value={editForm.fullName}
                  onChange={(e) => setEditForm({ ...editForm, fullName: e.target.value })}
                  className="w-full px-3 py-2 rounded-xl bg-slate-950 border border-slate-800 text-white focus:outline-none focus:ring-2 focus:ring-indigo-500/50"
                />
              </div>

              <div>
                <label className="block text-slate-300 font-semibold mb-1">Target Company</label>
                <select
                  value={editForm.targetCompany}
                  onChange={(e) => setEditForm({ ...editForm, targetCompany: e.target.value })}
                  className="w-full px-3 py-2 rounded-xl bg-slate-950 border border-slate-800 text-white focus:outline-none focus:ring-2 focus:ring-indigo-500/50"
                >
                  <option value="TCS">TCS</option>
                  <option value="Amazon">Amazon</option>
                  <option value="Infosys">Infosys</option>
                  <option value="Accenture">Accenture</option>
                  <option value="Google">Google</option>
                  <option value="Microsoft">Microsoft</option>
                  <option value="Wipro">Wipro</option>
                  <option value="Cognizant">Cognizant</option>
                </select>
              </div>

              <div>
                <label className="block text-slate-300 font-semibold mb-1">Target Role</label>
                <input
                  type="text"
                  value={editForm.targetRole}
                  onChange={(e) => setEditForm({ ...editForm, targetRole: e.target.value })}
                  className="w-full px-3 py-2 rounded-xl bg-slate-950 border border-slate-800 text-white focus:outline-none focus:ring-2 focus:ring-indigo-500/50"
                />
              </div>

              <div>
                <label className="block text-slate-300 font-semibold mb-1">Graduation Year</label>
                <input
                  type="number"
                  value={editForm.graduationYear}
                  onChange={(e) => setEditForm({ ...editForm, graduationYear: e.target.value })}
                  className="w-full px-3 py-2 rounded-xl bg-slate-950 border border-slate-800 text-white focus:outline-none focus:ring-2 focus:ring-indigo-500/50"
                />
              </div>

              <div className="flex justify-end gap-3 pt-3 border-t border-slate-800">
                <button
                  type="button"
                  onClick={() => setIsEditModalOpen(false)}
                  className="px-4 py-2 rounded-xl text-slate-400 hover:text-white"
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  disabled={saving}
                  className="px-5 py-2.5 rounded-xl bg-indigo-600 hover:bg-indigo-500 text-white font-bold flex items-center gap-1.5 shadow-md shadow-indigo-600/20"
                >
                  <Save className="w-3.5 h-3.5" />
                  <span>{saving ? 'Saving...' : 'Save Changes'}</span>
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
