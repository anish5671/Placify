import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import { 
  Award, 
  CheckCircle2, 
  XCircle, 
  MinusCircle, 
  Clock, 
  ArrowRight, 
  RotateCcw, 
  LayoutDashboard, 
  HelpCircle,
  Sparkles,
  BarChart2,
  FileText
} from 'lucide-react';
import api from '../api/client';

export default function TestResult() {
  const { attemptId } = useParams();
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(true);
  const [filterMode, setFilterMode] = useState('ALL'); // 'ALL', 'CORRECT', 'INCORRECT', 'UNATTEMPTED'

  useEffect(() => {
    fetchResult();
  }, [attemptId]);

  const fetchResult = async () => {
    try {
      setLoading(true);
      const res = await api.get(`/results/${attemptId}`);
      if (res.data.success) {
        setResult(res.data.data);
      }
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <div className="flex flex-col items-center justify-center min-h-[60vh] gap-3">
        <div className="w-10 h-10 border-4 border-indigo-500/20 border-t-indigo-500 rounded-full animate-spin"></div>
        <p className="text-sm text-slate-400">Evaluating answers and computing score analysis...</p>
      </div>
    );
  }

  if (!result) {
    return (
      <div className="p-8 text-center text-slate-400">
        Result report not found. <Link to="/dashboard" className="text-indigo-400 font-bold">Return to Dashboard</Link>
      </div>
    );
  }

  const minutes = Math.floor(result.timeTakenSeconds / 60);
  const seconds = result.timeTakenSeconds % 60;
  const isPassed = result.isPassed;

  const attemptedCount = result.correctCount + result.incorrectCount;
  const accuracyRate = attemptedCount > 0 ? Math.round((result.correctCount / attemptedCount) * 100) : 0;

  const filteredDetails = result.questionDetails?.filter(q => {
    if (filterMode === 'CORRECT') return q.isCorrect;
    if (filterMode === 'INCORRECT') return !q.isCorrect && q.selectedOption !== null;
    if (filterMode === 'UNATTEMPTED') return q.selectedOption === null;
    return true;
  }) || [];

  return (
    <div className="space-y-6 max-w-5xl mx-auto pb-12">
      {/* Top Banner & Score Overview */}
      <div className={`p-6 sm:p-8 rounded-3xl border shadow-2xl relative overflow-hidden ${
        isPassed 
          ? 'bg-gradient-to-br from-emerald-950/60 via-slate-900 to-indigo-950/70 border-emerald-500/30'
          : 'bg-gradient-to-br from-slate-900 via-slate-900 to-rose-950/40 border-rose-500/30'
      }`}>
        <div className="relative z-10 flex flex-col md:flex-row md:items-center justify-between gap-6">
          <div className="space-y-2">
            <div className="flex items-center gap-2">
              <span className={`px-2.5 py-0.5 rounded-full text-[10px] font-black uppercase tracking-wider ${
                isPassed ? 'bg-emerald-500/20 text-emerald-300 border border-emerald-500/30' : 'bg-rose-500/20 text-rose-300 border border-rose-500/30'
              }`}>
                {isPassed ? 'PASSED ASSESSMENT' : 'NEEDS IMPROVEMENT'}
              </span>
              <span className="text-xs text-slate-400 font-semibold">{result.companyName}</span>
            </div>
            <h2 className="text-2xl sm:text-3xl font-extrabold text-white tracking-tight">
              {result.testTitle}
            </h2>
            <p className="text-xs sm:text-sm text-slate-300">
              Assessment completed on {new Date(result.completedAt).toLocaleString()}
            </p>
          </div>

          {/* Big Score Dial */}
          <div className="flex items-center gap-6 p-4 rounded-2xl bg-slate-950/70 border border-slate-800 flex-shrink-0">
            <div className="text-right">
              <div className="text-[10px] uppercase font-bold text-slate-400">Your Score</div>
              <div className="text-3xl sm:text-4xl font-black text-white tracking-tight">
                {result.score} <span className="text-lg text-slate-400 font-semibold">/ {result.totalMarks}</span>
              </div>
              <div className="text-xs font-bold text-indigo-400 mt-0.5">{result.percentage}% Marks</div>
            </div>
            <div className={`w-14 h-14 rounded-2xl flex items-center justify-center font-black text-lg ${
              isPassed ? 'bg-emerald-500/20 text-emerald-300 border border-emerald-500/40' : 'bg-rose-500/20 text-rose-300 border border-rose-500/40'
            }`}>
              <Award className="w-8 h-8" />
            </div>
          </div>
        </div>
      </div>

      {/* 4 Stat Metric Cards */}
      <div className="grid grid-cols-2 sm:grid-cols-4 gap-4">
        <div className="p-4 rounded-2xl bg-slate-900 border border-slate-800 flex items-center gap-3">
          <div className="w-10 h-10 rounded-xl bg-emerald-500/10 border border-emerald-500/20 text-emerald-400 flex items-center justify-center">
            <CheckCircle2 className="w-5 h-5" />
          </div>
          <div>
            <span className="text-[10px] uppercase font-bold text-slate-400 block">Correct</span>
            <span className="text-lg font-black text-white">{result.correctCount} Qs</span>
          </div>
        </div>

        <div className="p-4 rounded-2xl bg-slate-900 border border-slate-800 flex items-center gap-3">
          <div className="w-10 h-10 rounded-xl bg-rose-500/10 border border-rose-500/20 text-rose-400 flex items-center justify-center">
            <XCircle className="w-5 h-5" />
          </div>
          <div>
            <span className="text-[10px] uppercase font-bold text-slate-400 block">Incorrect</span>
            <span className="text-lg font-black text-white">{result.incorrectCount} Qs</span>
          </div>
        </div>

        <div className="p-4 rounded-2xl bg-slate-900 border border-slate-800 flex items-center gap-3">
          <div className="w-10 h-10 rounded-xl bg-slate-800 border border-slate-700 text-slate-400 flex items-center justify-center">
            <MinusCircle className="w-5 h-5" />
          </div>
          <div>
            <span className="text-[10px] uppercase font-bold text-slate-400 block">Unattempted</span>
            <span className="text-lg font-black text-white">{result.unattemptedCount} Qs</span>
          </div>
        </div>

        <div className="p-4 rounded-2xl bg-slate-900 border border-slate-800 flex items-center gap-3">
          <div className="w-10 h-10 rounded-xl bg-indigo-500/10 border border-indigo-500/20 text-indigo-400 flex items-center justify-center">
            <Clock className="w-5 h-5" />
          </div>
          <div>
            <span className="text-[10px] uppercase font-bold text-slate-400 block">Time Spent</span>
            <span className="text-lg font-black text-white">{minutes}m {seconds}s</span>
          </div>
        </div>
      </div>

      {/* Action Buttons */}
      <div className="flex flex-wrap items-center justify-between gap-3 p-4 rounded-2xl bg-slate-900/60 border border-slate-800">
        <div className="flex items-center gap-2">
          <Link
            to="/tests"
            className="px-4 py-2 rounded-xl bg-slate-800 hover:bg-slate-700 text-slate-200 text-xs font-bold transition-all flex items-center gap-1.5"
          >
            <RotateCcw className="w-3.5 h-3.5" />
            <span>Retake Another Mock</span>
          </Link>
          <Link
            to="/dashboard"
            className="px-4 py-2 rounded-xl bg-indigo-600 hover:bg-indigo-500 text-white text-xs font-bold transition-all flex items-center gap-1.5"
          >
            <LayoutDashboard className="w-3.5 h-3.5" />
            <span>Go to Dashboard</span>
          </Link>
        </div>

        {/* Filter Pills for Review */}
        <div className="flex items-center gap-1.5">
          {[
            { id: 'ALL', label: `All (${result.questionDetails?.length || 0})` },
            { id: 'CORRECT', label: `Correct (${result.correctCount})` },
            { id: 'INCORRECT', label: `Incorrect (${result.incorrectCount})` },
            { id: 'UNATTEMPTED', label: `Unattempted (${result.unattemptedCount})` },
          ].map(f => (
            <button
              key={f.id}
              onClick={() => setFilterMode(f.id)}
              className={`px-3 py-1.5 rounded-xl text-xs font-bold transition-all ${
                filterMode === f.id
                  ? 'bg-indigo-600 text-white shadow-sm'
                  : 'bg-slate-950 text-slate-400 hover:text-slate-200 border border-slate-800'
              }`}
            >
              {f.label}
            </button>
          ))}
        </div>
      </div>

      {/* Question-By-Question Detailed Solutions Review */}
      <div className="space-y-4">
        <h3 className="text-base font-bold text-white flex items-center gap-2">
          <FileText className="w-5 h-5 text-indigo-400" />
          <span>Detailed Question Analysis & Explanations</span>
        </h3>

        {filteredDetails.map((q) => {
          const isCorrect = q.isCorrect;
          const isUnattempted = q.selectedOption === null;

          return (
            <div
              key={q.questionId}
              className={`p-6 rounded-3xl bg-slate-900 border transition-all space-y-4 ${
                isCorrect 
                  ? 'border-emerald-500/30 bg-gradient-to-b from-emerald-950/10 to-slate-900' 
                  : isUnattempted
                  ? 'border-slate-800'
                  : 'border-rose-500/30 bg-gradient-to-b from-rose-950/10 to-slate-900'
              }`}
            >
              {/* Question Header */}
              <div className="flex items-start justify-between gap-4">
                <div className="space-y-1">
                  <div className="flex items-center gap-2">
                    <span className="text-xs font-bold text-indigo-400">Q{q.questionOrder}.</span>
                    <span className={`px-2 py-0.5 rounded-full text-[10px] font-extrabold uppercase flex items-center gap-1 ${
                      isCorrect 
                        ? 'bg-emerald-500/10 text-emerald-400 border border-emerald-500/20' 
                        : isUnattempted
                        ? 'bg-slate-800 text-slate-400'
                        : 'bg-rose-500/10 text-rose-400 border border-rose-500/20'
                    }`}>
                      {isCorrect ? <CheckCircle2 className="w-3 h-3" /> : isUnattempted ? <MinusCircle className="w-3 h-3" /> : <XCircle className="w-3 h-3" />}
                      <span>{isCorrect ? 'CORRECT' : isUnattempted ? 'UNATTEMPTED' : 'INCORRECT'}</span>
                    </span>
                    <span className="text-[10px] text-slate-400 uppercase font-semibold">
                      Marks: {q.marksObtained} / {q.maxMarks}
                    </span>
                  </div>
                  <h4 className="text-sm sm:text-base font-bold text-white leading-snug">
                    {q.title}
                  </h4>
                </div>
              </div>

              {q.description && (
                <p className="text-xs text-slate-300 leading-relaxed bg-slate-950/60 p-3.5 rounded-2xl border border-slate-800/80">
                  {q.description}
                </p>
              )}

              {/* Options Breakdown with Highlights */}
              <div className="grid grid-cols-1 sm:grid-cols-2 gap-2.5">
                {['A', 'B', 'C', 'D'].map(opt => {
                  const text = q[`option${opt}`];
                  if (!text) return null;

                  const isThisCorrect = q.correctOption === opt;
                  const isUserPick = q.selectedOption === opt;

                  let optClass = 'bg-slate-950/60 border-slate-800/80 text-slate-300';
                  if (isThisCorrect) {
                    optClass = 'bg-emerald-500/20 border-emerald-500/50 text-emerald-300 font-bold';
                  } else if (isUserPick && !isThisCorrect) {
                    optClass = 'bg-rose-500/20 border-rose-500/50 text-rose-300 font-semibold';
                  }

                  return (
                    <div
                      key={opt}
                      className={`p-3 rounded-2xl border text-xs flex items-start gap-2.5 ${optClass}`}
                    >
                      <span className={`w-5 h-5 rounded-lg flex items-center justify-center font-bold text-[10px] flex-shrink-0 ${
                        isThisCorrect ? 'bg-emerald-500 text-white' :
                        isUserPick ? 'bg-rose-500 text-white' :
                        'bg-slate-800 text-slate-400'
                      }`}>
                        {opt}
                      </span>
                      <div className="flex-1">
                        <div>{text}</div>
                        {isThisCorrect && (
                          <span className="text-[10px] font-bold text-emerald-400 block mt-0.5">✓ Correct Answer</span>
                        )}
                        {isUserPick && !isThisCorrect && (
                          <span className="text-[10px] font-bold text-rose-400 block mt-0.5">✗ Your Choice</span>
                        )}
                      </div>
                    </div>
                  );
                })}
              </div>

              {/* Full Conceptual Explanation */}
              <div className="p-4 rounded-2xl bg-slate-950 border border-indigo-500/20 text-xs space-y-1.5">
                <span className="text-indigo-400 font-bold uppercase tracking-wider text-[10px] block">
                  Concept & Solution Explanation:
                </span>
                <p className="text-slate-300 whitespace-pre-line leading-relaxed">
                  {q.explanation}
                </p>
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
}
