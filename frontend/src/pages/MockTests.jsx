import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { 
  CheckSquare, 
  Clock, 
  HelpCircle, 
  Award, 
  Play, 
  Building2, 
  ShieldAlert, 
  Info,
  X,
  Target
} from 'lucide-react';
import api from '../api/client';

export default function MockTests() {
  const [tests, setTests] = useState([]);
  const [loading, setLoading] = useState(true);
  const [selectedCategory, setSelectedCategory] = useState('ALL');
  const [instructionModalTest, setInstructionModalTest] = useState(null);

  const navigate = useNavigate();

  useEffect(() => {
    fetchTests();
  }, []);

  const fetchTests = async () => {
    try {
      setLoading(true);
      const res = await api.get('/tests');
      if (res.data.success) {
        setTests(res.data.data);
      }
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleStartExam = (testId) => {
    setInstructionModalTest(null);
    navigate(`/tests/${testId}/exam`);
  };

  const filteredTests = tests.filter(t => {
    if (selectedCategory === 'ALL') return true;
    return t.category === selectedCategory;
  });

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h2 className="text-2xl font-black text-white tracking-tight flex items-center gap-2">
            <CheckSquare className="w-6 h-6 text-indigo-400" />
            <span>Mock Test Assessment System</span>
          </h2>
          <p className="text-xs sm:text-sm text-slate-400 mt-1">
            Simulated company online assessments with live countdown timers, question palettes, and backend scoring
          </p>
        </div>

        {/* Category Pills */}
        <div className="flex flex-wrap items-center gap-2">
          {[
            { id: 'ALL', name: 'All Tests' },
            { id: 'COMPANY_MOCK', name: 'Company Mocks' },
            { id: 'TECHNICAL_CS', name: 'Core CS Sprint' },
            { id: 'DSA', name: 'DSA Challenge' }
          ].map(cat => (
            <button
              key={cat.id}
              onClick={() => setSelectedCategory(cat.id)}
              className={`px-3 py-1.5 rounded-xl text-xs font-bold transition-all ${
                selectedCategory === cat.id
                  ? 'bg-indigo-600 text-white shadow-md shadow-indigo-600/20'
                  : 'bg-slate-900 text-slate-400 hover:text-slate-200 border border-slate-800'
              }`}
            >
              {cat.name}
            </button>
          ))}
        </div>
      </div>

      {/* Test Catalog Grid */}
      {loading ? (
        <div className="flex flex-col items-center justify-center min-h-[40vh] gap-3">
          <div className="w-10 h-10 border-4 border-indigo-500/20 border-t-indigo-500 rounded-full animate-spin"></div>
          <p className="text-sm text-slate-400">Loading mock tests...</p>
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-5">
          {filteredTests.map((test) => (
            <div
              key={test.id}
              className="p-6 rounded-3xl bg-slate-900/90 border border-slate-800 hover:border-indigo-500/40 transition-all shadow-xl flex flex-col justify-between group"
            >
              <div className="space-y-4">
                {/* Header Badge */}
                <div className="flex items-center justify-between">
                  <span className="text-[10px] font-black uppercase px-2.5 py-0.5 rounded-full bg-indigo-500/10 text-indigo-400 border border-indigo-500/20">
                    {test.category}
                  </span>
                  <span className={`text-[10px] font-black uppercase px-2 py-0.5 rounded ${
                    test.difficulty === 'HARD' ? 'bg-rose-500/10 text-rose-400' :
                    test.difficulty === 'MEDIUM' ? 'bg-amber-500/10 text-amber-400' :
                    'bg-emerald-500/10 text-emerald-400'
                  }`}>
                    {test.difficulty}
                  </span>
                </div>

                {/* Title & Description */}
                <div>
                  <h3 className="text-base font-bold text-white group-hover:text-indigo-400 transition-colors">
                    {test.title}
                  </h3>
                  <p className="text-xs text-slate-300 mt-1.5 line-clamp-2 leading-relaxed">
                    {test.description}
                  </p>
                </div>

                {/* Company Tag if any */}
                {test.companyName && (
                  <div className="flex items-center gap-2 text-xs text-slate-400 bg-slate-950/60 p-2.5 rounded-xl border border-slate-800/80">
                    <Building2 className="w-4 h-4 text-indigo-400" />
                    <span className="font-semibold text-slate-300">{test.companyName}</span>
                  </div>
                )}

                {/* Test Parameters */}
                <div className="grid grid-cols-3 gap-2 pt-2 border-t border-slate-800/80 text-center">
                  <div className="p-2 rounded-xl bg-slate-950/50">
                    <Clock className="w-3.5 h-3.5 text-indigo-400 mx-auto mb-1" />
                    <span className="text-[10px] text-slate-400 uppercase block font-semibold">Duration</span>
                    <span className="text-xs font-bold text-white">{test.durationMinutes}m</span>
                  </div>
                  <div className="p-2 rounded-xl bg-slate-950/50">
                    <HelpCircle className="w-3.5 h-3.5 text-indigo-400 mx-auto mb-1" />
                    <span className="text-[10px] text-slate-400 uppercase block font-semibold">Questions</span>
                    <span className="text-xs font-bold text-white">{test.totalQuestions}</span>
                  </div>
                  <div className="p-2 rounded-xl bg-slate-950/50">
                    <Award className="w-3.5 h-3.5 text-indigo-400 mx-auto mb-1" />
                    <span className="text-[10px] text-slate-400 uppercase block font-semibold">Marks</span>
                    <span className="text-xs font-bold text-white">{test.totalMarks}</span>
                  </div>
                </div>
              </div>

              {/* Start Test Button */}
              <button
                onClick={() => setInstructionModalTest(test)}
                className="mt-5 w-full py-2.5 px-4 rounded-xl bg-gradient-to-r from-indigo-600 to-indigo-700 hover:from-indigo-500 hover:to-indigo-600 text-white font-bold text-xs flex items-center justify-center gap-2 shadow-lg shadow-indigo-600/20 transition-all hover:scale-[1.01]"
              >
                <Play className="w-3.5 h-3.5 fill-white" />
                <span>Start Mock Test</span>
              </button>
            </div>
          ))}
        </div>
      )}

      {/* Pre-Test Instructions Modal */}
      {instructionModalTest && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-slate-950/80 backdrop-blur-md">
          <div className="bg-slate-900 border border-slate-800 rounded-3xl max-w-lg w-full overflow-hidden shadow-2xl animate-scaleIn">
            <div className="p-6 border-b border-slate-800 flex items-start justify-between">
              <div>
                <span className="text-[10px] font-black uppercase text-indigo-400 px-2 py-0.5 rounded bg-indigo-500/10">
                  Assessment Instructions
                </span>
                <h3 className="text-lg font-bold text-white mt-1">{instructionModalTest.title}</h3>
              </div>
              <button
                onClick={() => setInstructionModalTest(null)}
                className="p-1.5 rounded-lg text-slate-400 hover:text-white hover:bg-slate-800"
              >
                <X className="w-5 h-5" />
              </button>
            </div>

            <div className="p-6 space-y-4 text-xs text-slate-300">
              <div className="grid grid-cols-3 gap-3 p-3.5 rounded-2xl bg-slate-950 border border-slate-800 text-center">
                <div>
                  <span className="text-slate-400 block text-[10px] uppercase font-bold">Time Limit</span>
                  <span className="text-white font-extrabold text-sm">{instructionModalTest.durationMinutes} Minutes</span>
                </div>
                <div>
                  <span className="text-slate-400 block text-[10px] uppercase font-bold">Total Questions</span>
                  <span className="text-white font-extrabold text-sm">{instructionModalTest.totalQuestions}</span>
                </div>
                <div>
                  <span className="text-slate-400 block text-[10px] uppercase font-bold">Passing Marks</span>
                  <span className="text-emerald-400 font-extrabold text-sm">{instructionModalTest.passingMarks}</span>
                </div>
              </div>

              <div className="space-y-2">
                <h4 className="font-bold text-white uppercase tracking-wider text-[11px]">Important Exam Rules:</h4>
                <ul className="space-y-2 pl-4 list-disc text-slate-300 leading-relaxed">
                  <li>The test runs on a countdown timer. When the timer hits 0:00, your test is automatically submitted.</li>
                  <li>Use the Question Palette on the right to jump directly to any question.</li>
                  <li>You can mark questions for review and return to them anytime before submission.</li>
                  <li>Each correct answer earns 2 marks. There is no negative marking.</li>
                  <li>Correct answers and detailed explanations are calculated by the backend and revealed only after test submission.</li>
                </ul>
              </div>
            </div>

            <div className="p-4 border-t border-slate-800 flex items-center justify-end gap-3 bg-slate-950/40">
              <button
                onClick={() => setInstructionModalTest(null)}
                className="px-4 py-2 rounded-xl text-xs font-semibold text-slate-400 hover:text-white"
              >
                Cancel
              </button>
              <button
                onClick={() => handleStartExam(instructionModalTest.id)}
                className="px-5 py-2.5 rounded-xl bg-indigo-600 hover:bg-indigo-500 text-white font-bold text-xs shadow-md shadow-indigo-600/20 flex items-center gap-2"
              >
                <Play className="w-3.5 h-3.5 fill-white" />
                <span>Begin Test Now</span>
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
