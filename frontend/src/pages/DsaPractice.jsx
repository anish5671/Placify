import React, { useState, useEffect } from 'react';
import { 
  Code2, 
  ExternalLink, 
  CheckCircle2, 
  Bookmark, 
  Eye, 
  Sparkles, 
  Layers, 
  X
} from 'lucide-react';
import api from '../api/client';

const MODULES = [
  { slug: 'arrays', name: 'Arrays & Hashing' },
  { slug: 'strings', name: 'Strings & Backtracking' },
  { slug: 'two-pointers', name: 'Two Pointers & Sliding Window' },
  { slug: 'stack-queue', name: 'Stack & Queue' },
  { slug: 'linked-list', name: 'Linked Lists' },
  { slug: 'binary-search', name: 'Binary Search' },
  { slug: 'trees', name: 'Trees & Tries' },
  { slug: 'heaps', name: 'Heaps & Greedy' },
  { slug: 'dynamic-programming', name: 'Dynamic Programming' },
  { slug: 'backtracking', name: 'Backtracking & Recursion' },
  { slug: 'graphs', name: 'Graphs' },
];

const getPracticeUrl = (problem) => (
  problem.externalLink
  || `https://leetcode.com/problemset/?search=${encodeURIComponent(problem.title)}`
);

export default function DsaPractice() {
  const [questions, setQuestions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [selectedTopic, setSelectedTopic] = useState(null);
  const [activeModalProblem, setActiveModalProblem] = useState(null);

  useEffect(() => {
    fetchDsaQuestions();
  }, []);

  const fetchDsaQuestions = async () => {
    try {
      setLoading(true);
      const res = await api.get('/questions/dsa');
      if (res.data.success) {
        setQuestions(res.data.data);
      }
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleToggleSolve = async (qId) => {
    try {
      const res = await api.post(`/questions/${qId}/solve`);
      const isSolved = res.data.data.isSolved;
      setQuestions(prev => prev.map(q => q.id === qId ? { ...q, isSolved } : q));
      setActiveModalProblem(prev => (
        prev?.id === qId ? { ...prev, isSolved } : prev
      ));
    } catch (err) {
      console.error(err);
    }
  };

  const handleToggleBookmark = async (qId) => {
    try {
      const res = await api.post(`/questions/${qId}/bookmark`);
      const isBookmarked = res.data.data.isBookmarked;
      setQuestions(prev => prev.map(q => q.id === qId ? { ...q, isBookmarked } : q));
      setActiveModalProblem(prev => (
        prev?.id === qId ? { ...prev, isBookmarked } : prev
      ));
    } catch (err) {
      console.error(err);
    }
  };

  const selectedModule = MODULES.find(module => module.slug === selectedTopic);
  const filteredProblems = questions.filter(q => q.topicName === selectedModule?.name);

  const totalSolved = questions.filter(q => q.isSolved).length;
  const progressPercent = questions.length > 0 ? Math.round((totalSolved / questions.length) * 100) : 0;

  return (
    <div className="space-y-6">
      {/* Header & Overview Card */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h2 className="text-2xl font-black text-white tracking-tight flex items-center gap-2">
            <Code2 className="w-6 h-6 text-indigo-400" />
            <span>DSA Practice Sheets</span>
          </h2>
          <p className="text-xs sm:text-sm text-slate-400 mt-1">
            11 focused modules with 20 important pattern-based problems in each, curated from LeetCode, GFG, and Striver practice patterns
          </p>
        </div>

        {/* Global DSA Progress Badge */}
        <div className="p-3.5 px-5 rounded-2xl bg-slate-900 border border-slate-800 flex items-center gap-4">
          <div>
            <div className="text-xs text-slate-400 font-semibold">DSA Completion</div>
            <div className="text-lg font-black text-white">{totalSolved} / {questions.length} Solved</div>
          </div>
          <div className="w-12 h-12 rounded-full bg-slate-950 border-2 border-indigo-500/40 flex items-center justify-center font-bold text-xs text-indigo-400">
            {progressPercent}%
          </div>
        </div>
      </div>

      {loading ? (
        <div className="flex flex-col items-center justify-center min-h-[40vh] gap-3">
          <div className="w-10 h-10 border-4 border-indigo-500/20 border-t-indigo-500 rounded-full animate-spin"></div>
          <p className="text-sm text-slate-400">Loading DSA practice sheet...</p>
        </div>
      ) : !selectedModule ? (
        <div className="grid grid-cols-1 sm:grid-cols-2 xl:grid-cols-3 gap-3">
          {MODULES.map(module => {
            const moduleQuestions = questions.filter(q => q.topicName === module.name);
            const solved = moduleQuestions.filter(q => q.isSolved).length;
            return (
              <button
                key={module.slug}
                onClick={() => setSelectedTopic(module.slug)}
                className="text-left p-5 rounded-2xl bg-slate-900/90 border border-slate-800 hover:border-indigo-500/50 hover:bg-slate-800/70 transition-colors"
              >
                <div className="flex items-start justify-between gap-3">
                  <div className="flex items-center gap-3">
                    <div className="p-2 rounded-xl bg-indigo-500/10 text-indigo-300">
                      <Layers className="w-5 h-5" />
                    </div>
                    <h3 className="text-sm font-bold text-white">{module.name}</h3>
                  </div>
                  <span className="text-xs font-bold text-indigo-300">
                    {moduleQuestions.length} questions
                  </span>
                </div>
                <div className="mt-4 text-xs text-slate-400">{solved} solved</div>
                <div className="h-1.5 mt-2 rounded-full bg-slate-950 overflow-hidden">
                  <div
                    className="h-full bg-indigo-500"
                    style={{ width: `${moduleQuestions.length ? (solved / moduleQuestions.length) * 100 : 0}%` }}
                  />
                </div>
              </button>
            );
          })}
        </div>
      ) : (
        <div>
          <div className="flex flex-wrap items-center justify-between gap-3 mb-3">
            <button
              onClick={() => setSelectedTopic(null)}
              className="text-xs font-bold text-indigo-300 hover:text-indigo-200"
            >
              ← All modules
            </button>
            <span className="text-xs text-slate-400">
              {filteredProblems.length} questions · {filteredProblems.filter(q => q.isSolved).length} solved
            </span>
          </div>
          <div className="overflow-hidden rounded-3xl bg-slate-900/90 border border-slate-800 shadow-xl">
          <div className="overflow-x-auto">
            <table className="w-full text-left text-xs">
              <thead className="bg-slate-950/80 text-slate-400 uppercase tracking-wider text-[10px]">
                <tr>
                  <th className="py-3.5 px-4 w-12 text-center">Status</th>
                  <th className="py-3.5 px-4">Problem & Pattern</th>
                  <th className="py-3.5 px-4 text-center">Reference</th>
                  <th className="py-3.5 px-4 text-right">Actions</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-800/60 text-slate-200">
                {filteredProblems.map(p => (
                  <tr key={p.id} className="hover:bg-slate-800/40 transition-colors">
                    {/* Status Checkbox */}
                    <td className="py-4 px-4 text-center">
                      <button
                        onClick={() => handleToggleSolve(p.id)}
                        className={`w-6 h-6 rounded-lg border flex items-center justify-center transition-all ${
                          p.isSolved
                            ? 'bg-emerald-500 text-white border-emerald-500 shadow-sm shadow-emerald-500/20'
                            : 'border-slate-700 bg-slate-950 hover:border-slate-500 text-transparent'
                        }`}
                        title={p.isSolved ? 'Mark as Unsolved' : 'Mark as Solved'}
                      >
                        <CheckCircle2 className="w-4 h-4 fill-current" />
                      </button>
                    </td>

                    {/* Problem Name & Description */}
                    <td className="py-4 px-4 font-medium">
                      <div className="text-sm font-bold text-white hover:text-indigo-400 cursor-pointer transition-colors" onClick={() => setActiveModalProblem(p)}>
                        {p.title}
                      </div>
                      <div className="text-xs text-slate-400 line-clamp-1 mt-0.5">
                        {p.description}
                      </div>
                    </td>
                    <td className="py-4 px-4 text-center">
                      <a
                        href={getPracticeUrl(p)}
                        target="_blank"
                        rel="noopener noreferrer"
                        className="inline-flex items-center gap-1 px-2.5 py-1 rounded-lg bg-slate-950 hover:bg-slate-800 text-indigo-400 border border-slate-800 text-[11px] font-semibold transition-colors"
                        aria-label={`Solve ${p.title} on LeetCode`}
                      >
                        <span>Search problem</span>
                        <ExternalLink className="w-3 h-3" />
                      </a>
                    </td>

                    {/* Actions: View Solution Modal + Bookmark */}
                    <td className="py-4 px-4 text-right">
                      <div className="flex items-center justify-end gap-2">
                        <button
                          onClick={() => handleToggleBookmark(p.id)}
                          className={`p-1.5 rounded-lg border transition-colors ${
                            p.isBookmarked
                              ? 'bg-amber-500/20 text-amber-300 border-amber-500/40'
                              : 'text-slate-400 hover:text-slate-200 border-slate-800'
                          }`}
                          title="Bookmark Problem"
                        >
                          <Bookmark className={`w-3.5 h-3.5 ${p.isBookmarked ? 'fill-current' : ''}`} />
                        </button>

                        <button
                          onClick={() => setActiveModalProblem(p)}
                          className="px-3 py-1.5 rounded-lg bg-indigo-600/20 hover:bg-indigo-600/30 text-indigo-300 border border-indigo-500/30 font-bold text-xs flex items-center gap-1.5 transition-all"
                        >
                          <Eye className="w-3.5 h-3.5" />
                          <span>Approach</span>
                        </button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
        </div>
      )}

      {/* Problem Solution & Approach Modal */}
      {activeModalProblem && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-slate-950/80 backdrop-blur-md">
          <div className="bg-slate-900 border border-slate-800 rounded-3xl max-w-3xl w-full max-h-[85vh] overflow-hidden flex flex-col shadow-2xl">
            {/* Modal Header */}
            <div className="p-6 border-b border-slate-800 flex items-start justify-between gap-4">
              <div>
                <div className="text-[10px] text-indigo-300 font-semibold mb-1">
                  {activeModalProblem.topicName}
                </div>
                <h3 className="text-lg font-bold text-white">{activeModalProblem.title}</h3>
              </div>

              <button
                onClick={() => setActiveModalProblem(null)}
                className="p-2 rounded-xl text-slate-400 hover:text-slate-200 hover:bg-slate-800 transition-colors"
              >
                <X className="w-5 h-5" />
              </button>
            </div>

            {/* Modal Body */}
            <div className="p-6 overflow-y-auto space-y-5 text-xs text-slate-300">
              {/* Problem Description */}
              <div>
                <h4 className="font-bold text-white uppercase tracking-wider text-[11px] mb-1.5">Problem Statement</h4>
                <p className="leading-relaxed bg-slate-950/60 p-4 rounded-2xl border border-slate-800">
                  {activeModalProblem.description}
                </p>
              </div>

              {/* Sample Input / Output & Constraints */}
              {(activeModalProblem.sampleInput || activeModalProblem.constraints) && (
                <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                  {activeModalProblem.sampleInput && (
                    <div className="p-3.5 rounded-2xl bg-slate-950/60 border border-slate-800">
                      <span className="text-[10px] uppercase font-bold text-slate-400 block mb-1">Example Input & Output</span>
                      <div className="font-mono text-slate-200">{activeModalProblem.sampleInput}</div>
                      <div className="font-mono text-emerald-400 mt-1">Output: {activeModalProblem.sampleOutput}</div>
                    </div>
                  )}

                  {activeModalProblem.constraints && (
                    <div className="p-3.5 rounded-2xl bg-slate-950/60 border border-slate-800">
                      <span className="text-[10px] uppercase font-bold text-slate-400 block mb-1">Constraints</span>
                      <pre className="font-mono text-slate-300 whitespace-pre-line text-[11px]">{activeModalProblem.constraints}</pre>
                    </div>
                  )}
                </div>
              )}

              {/* Java Implementation Code Snippet */}
              {activeModalProblem.codeSnippet && (
                <div>
                  <h4 className="font-bold text-white uppercase tracking-wider text-[11px] mb-1.5">Java Solution Code</h4>
                  <pre className="p-4 rounded-2xl bg-slate-950 border border-slate-800 font-mono text-indigo-300 overflow-x-auto leading-relaxed">
                    {activeModalProblem.codeSnippet}
                  </pre>
                </div>
              )}

              {/* Optimal Approach & Complexity Breakdown */}
              <div className="p-4 rounded-2xl bg-indigo-950/30 border border-indigo-500/20 space-y-2">
                <h4 className="font-bold text-indigo-300 uppercase tracking-wider text-[11px] flex items-center gap-1.5">
                  <Sparkles className="w-4 h-4 text-indigo-400" /> Optimal Approach & Explanation
                </h4>
                <p className="whitespace-pre-line leading-relaxed text-slate-200">
                  {activeModalProblem.explanation}
                </p>
              </div>
            </div>

            {/* Modal Footer */}
            <div className="p-4 border-t border-slate-800 flex items-center justify-between bg-slate-950/40">
              <button
                onClick={() => handleToggleSolve(activeModalProblem.id)}
                className={`px-4 py-2 rounded-xl text-xs font-bold transition-all flex items-center gap-2 ${
                  activeModalProblem.isSolved
                    ? 'bg-emerald-500/20 text-emerald-300 border border-emerald-500/40'
                    : 'bg-slate-800 text-slate-200 hover:bg-slate-700'
                }`}
              >
                <CheckCircle2 className="w-4 h-4" />
                <span>{activeModalProblem.isSolved ? 'Solved' : 'Mark as Solved'}</span>
              </button>

              <a
                href={getPracticeUrl(activeModalProblem)}
                target="_blank"
                rel="noopener noreferrer"
                className="px-4 py-2 rounded-xl bg-indigo-600 hover:bg-indigo-500 text-white text-xs font-bold flex items-center gap-1.5 shadow-md shadow-indigo-600/20 transition-all"
              >
                <span>Search on LeetCode</span>
                <ExternalLink className="w-3.5 h-3.5" />
              </a>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}