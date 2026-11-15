import React, { useState, useEffect } from 'react';
import { 
  HelpCircle, 
  Search, 
  Bookmark, 
  CheckCircle2, 
  XCircle, 
  ExternalLink, 
  Filter, 
  RotateCcw,
  Sparkles,
  Layers
} from 'lucide-react';
import api from '../api/client';

export default function QuestionBank() {
  const [questions, setQuestions] = useState([]);
  const [topics, setTopics] = useState([]);
  const [loading, setLoading] = useState(true);

  // Filters
  const [category, setCategory] = useState('');
  const [difficulty, setDifficulty] = useState('');
  const [company, setCompany] = useState('');
  const [search, setSearch] = useState('');
  const [onlyBookmarked, setOnlyBookmarked] = useState(false);

  // User interactive answers state: { [qId]: selectedOption }
  const [userSelected, setUserSelected] = useState({});
  const [revealedSolutions, setRevealedSolutions] = useState({});

  useEffect(() => {
    fetchTopics();
    fetchQuestions();
  }, [category, difficulty, company]);

  const fetchTopics = async () => {
    try {
      const res = await api.get('/topics');
      if (res.data.success) {
        setTopics(res.data.data);
      }
    } catch (err) {
      console.error(err);
    }
  };

  const fetchQuestions = async () => {
    try {
      setLoading(true);
      const params = {};
      if (category) params.category = category;
      if (difficulty) params.difficulty = difficulty;
      if (company) params.company = company;
      if (search) params.search = search;

      const res = await api.get('/questions', { params });
      if (res.data.success) {
        setQuestions(res.data.data);
      }
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleSearchSubmit = (e) => {
    e.preventDefault();
    fetchQuestions();
  };

  const handleOptionClick = (questionId, optionLetter) => {
    setUserSelected(prev => ({ ...prev, [questionId]: optionLetter }));
    setRevealedSolutions(prev => ({ ...prev, [questionId]: true }));
  };

  const toggleBookmark = async (qId) => {
    try {
      const res = await api.post(`/questions/${qId}/bookmark`);
      const isBookmarked = res.data.data.isBookmarked;
      setQuestions(prev => prev.map(q => q.id === qId ? { ...q, isBookmarked } : q));
    } catch (err) {
      console.error(err);
    }
  };

  const toggleSolve = async (qId) => {
    try {
      const res = await api.post(`/questions/${qId}/solve`);
      const isSolved = res.data.data.isSolved;
      setQuestions(prev => prev.map(q => q.id === qId ? { ...q, isSolved } : q));
    } catch (err) {
      console.error(err);
    }
  };

  const filteredQuestions = questions.filter(q => {
    if (onlyBookmarked && !q.isBookmarked) return false;
    return true;
  });

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h2 className="text-2xl font-black text-white tracking-tight flex items-center gap-2">
            <HelpCircle className="w-6 h-6 text-indigo-400" />
            <span>Question Bank & Practice</span>
          </h2>
          <p className="text-xs sm:text-sm text-slate-400 mt-1">
            Topic-categorized interview questions across Aptitude, Java, DBMS, OS, Networks, SQL, and DSA
          </p>
        </div>

        {/* Search */}
        <form onSubmit={handleSearchSubmit} className="flex items-center gap-2">
          <div className="relative">
            <Search className="w-4 h-4 text-slate-400 absolute left-3.5 top-2.5" />
            <input
              type="text"
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              placeholder="Search question keyword..."
              className="pl-9 pr-4 py-2 rounded-xl bg-slate-900 border border-slate-800 text-xs sm:text-sm text-slate-100 placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-indigo-500/50 w-52 sm:w-64"
            />
          </div>
          <button
            type="submit"
            className="px-3.5 py-2 rounded-xl bg-indigo-600 hover:bg-indigo-500 text-white text-xs font-bold transition-all"
          >
            Search
          </button>
        </form>
      </div>

      {/* Filter Toolbar */}
      <div className="p-4 rounded-2xl bg-slate-900/90 border border-slate-800 flex flex-wrap items-center justify-between gap-4">
        <div className="flex flex-wrap items-center gap-2">
          {/* Category Dropdown */}
          <select
            value={category}
            onChange={(e) => setCategory(e.target.value)}
            className="px-3 py-1.5 rounded-xl bg-slate-950 border border-slate-800 text-xs font-semibold text-slate-200 focus:outline-none focus:ring-2 focus:ring-indigo-500/50"
          >
            <option value="">All Categories</option>
            <option value="APTITUDE">Aptitude</option>
            <option value="REASONING">Reasoning</option>
            <option value="VERBAL">Verbal</option>
            <option value="JAVA_OOPS">Java & OOPs</option>
            <option value="DBMS">DBMS & SQL</option>
            <option value="OS">Operating Systems</option>
            <option value="NETWORKS">Computer Networks</option>
            <option value="DSA">DSA</option>
          </select>

          {/* Difficulty Dropdown */}
          <select
            value={difficulty}
            onChange={(e) => setDifficulty(e.target.value)}
            className="px-3 py-1.5 rounded-xl bg-slate-950 border border-slate-800 text-xs font-semibold text-slate-200 focus:outline-none focus:ring-2 focus:ring-indigo-500/50"
          >
            <option value="">All Difficulties</option>
            <option value="EASY">Easy</option>
            <option value="MEDIUM">Medium</option>
            <option value="HARD">Hard</option>
          </select>

          {/* Company Tag Filter */}
          <select
            value={company}
            onChange={(e) => setCompany(e.target.value)}
            className="px-3 py-1.5 rounded-xl bg-slate-950 border border-slate-800 text-xs font-semibold text-slate-200 focus:outline-none focus:ring-2 focus:ring-indigo-500/50"
          >
            <option value="">All Companies</option>
            <option value="TCS">TCS</option>
            <option value="Amazon">Amazon</option>
            <option value="Infosys">Infosys</option>
            <option value="Accenture">Accenture</option>
            <option value="Google">Google</option>
            <option value="Microsoft">Microsoft</option>
            <option value="Wipro">Wipro</option>
            <option value="Cognizant">Cognizant</option>
          </select>

          {/* Bookmarks Toggle */}
          <button
            onClick={() => setOnlyBookmarked(!onlyBookmarked)}
            className={`px-3 py-1.5 rounded-xl text-xs font-bold transition-all flex items-center gap-1.5 ${
              onlyBookmarked
                ? 'bg-amber-500/20 text-amber-300 border border-amber-500/40'
                : 'bg-slate-950 text-slate-400 hover:text-slate-200 border border-slate-800'
            }`}
          >
            <Bookmark className="w-3.5 h-3.5 fill-current" />
            <span>Bookmarked Only</span>
          </button>
        </div>

        <div className="text-xs text-slate-400 font-medium">
          Showing <span className="text-white font-bold">{filteredQuestions.length}</span> Questions
        </div>
      </div>

      {/* Question Cards List */}
      {loading ? (
        <div className="flex flex-col items-center justify-center min-h-[40vh] gap-3">
          <div className="w-10 h-10 border-4 border-indigo-500/20 border-t-indigo-500 rounded-full animate-spin"></div>
          <p className="text-sm text-slate-400">Loading questions...</p>
        </div>
      ) : filteredQuestions.length === 0 ? (
        <div className="py-16 text-center rounded-3xl bg-slate-900/60 border border-dashed border-slate-800">
          <HelpCircle className="w-12 h-12 text-slate-600 mx-auto mb-3" />
          <h4 className="text-base font-bold text-slate-300">No Questions Found</h4>
          <p className="text-xs text-slate-500 mt-1">Try clearing filters or search terms.</p>
        </div>
      ) : (
        <div className="space-y-4">
          {filteredQuestions.map((q, idx) => {
            const hasChosen = userSelected[q.id];
            const isRevealed = revealedSolutions[q.id];
            const isCorrectChoice = hasChosen && hasChosen === q.correctOption;

            return (
              <div 
                key={q.id}
                className="p-6 rounded-3xl bg-slate-900/90 border border-slate-800 hover:border-slate-700/80 transition-all shadow-md space-y-4"
              >
                {/* Header row */}
                <div className="flex items-start justify-between gap-4">
                  <div className="space-y-1.5 flex-1">
                    <div className="flex flex-wrap items-center gap-2">
                      <span className="text-xs font-black text-indigo-400">#{idx + 1}</span>
                      <span className={`px-2 py-0.5 rounded text-[10px] font-black uppercase tracking-wider ${
                        q.difficulty === 'HARD' ? 'bg-rose-500/10 text-rose-400 border border-rose-500/20' :
                        q.difficulty === 'MEDIUM' ? 'bg-amber-500/10 text-amber-400 border border-amber-500/20' :
                        'bg-emerald-500/10 text-emerald-400 border border-emerald-500/20'
                      }`}>
                        {q.difficulty}
                      </span>
                      <span className="text-[10px] uppercase font-bold px-2 py-0.5 rounded bg-slate-800 text-slate-300">
                        {q.category}
                      </span>
                      {q.topicName && (
                        <span className="text-[10px] font-semibold px-2 py-0.5 rounded bg-indigo-950/40 text-indigo-300 border border-indigo-500/20">
                          {q.topicName}
                        </span>
                      )}
                    </div>

                    <h3 className="text-sm sm:text-base font-bold text-white leading-snug">
                      {q.title}
                    </h3>
                  </div>

                  {/* Bookmark & Solved Buttons */}
                  <div className="flex items-center gap-2 flex-shrink-0">
                    <button
                      onClick={() => toggleBookmark(q.id)}
                      className={`p-2 rounded-xl border transition-all ${
                        q.isBookmarked
                          ? 'bg-amber-500/20 text-amber-300 border-amber-500/40 shadow-sm'
                          : 'bg-slate-950 text-slate-400 hover:text-white border-slate-800'
                      }`}
                      title={q.isBookmarked ? 'Remove bookmark' : 'Bookmark question'}
                    >
                      <Bookmark className={`w-4 h-4 ${q.isBookmarked ? 'fill-current' : ''}`} />
                    </button>

                    <button
                      onClick={() => toggleSolve(q.id)}
                      className={`p-2 rounded-xl border transition-all ${
                        q.isSolved
                          ? 'bg-emerald-500/20 text-emerald-300 border-emerald-500/40 shadow-sm'
                          : 'bg-slate-950 text-slate-400 hover:text-white border-slate-800'
                      }`}
                      title={q.isSolved ? 'Marked as solved' : 'Mark as solved'}
                    >
                      <CheckCircle2 className={`w-4 h-4 ${q.isSolved ? 'fill-current' : ''}`} />
                    </button>
                  </div>
                </div>

                {/* Description / Code Snippet */}
                {q.description && (
                  <p className="text-xs sm:text-sm text-slate-300 leading-relaxed">
                    {q.description}
                  </p>
                )}

                {q.codeSnippet && (
                  <div className="p-3.5 rounded-xl bg-slate-950 border border-slate-800 font-mono text-xs text-indigo-300 overflow-x-auto whitespace-pre">
                    {q.codeSnippet}
                  </div>
                )}

                {/* Interactive Options (A, B, C, D) */}
                {q.optionA && (
                  <div className="grid grid-cols-1 sm:grid-cols-2 gap-2.5 pt-1">
                    {['A', 'B', 'C', 'D'].map((opt) => {
                      const text = q[`option${opt}`];
                      if (!text) return null;

                      const isSelected = hasChosen === opt;
                      const isCorrect = q.correctOption === opt;
                      
                      let optionClasses = 'bg-slate-950/70 border-slate-800/90 text-slate-300 hover:border-indigo-500/40';

                      if (isRevealed) {
                        if (isCorrect) {
                          optionClasses = 'bg-emerald-500/15 border-emerald-500/50 text-emerald-300 font-bold';
                        } else if (isSelected && !isCorrect) {
                          optionClasses = 'bg-rose-500/15 border-rose-500/50 text-rose-300 font-semibold';
                        }
                      } else if (isSelected) {
                        optionClasses = 'bg-indigo-600/20 border-indigo-500 text-indigo-300 font-bold';
                      }

                      return (
                        <button
                          key={opt}
                          type="button"
                          onClick={() => handleOptionClick(q.id, opt)}
                          className={`p-3 rounded-2xl border text-left text-xs transition-all flex items-start gap-2.5 ${optionClasses}`}
                        >
                          <span className={`w-5 h-5 rounded-lg flex items-center justify-center font-extrabold text-[11px] flex-shrink-0 ${
                            isRevealed && isCorrect ? 'bg-emerald-500 text-white' :
                            isRevealed && isSelected && !isCorrect ? 'bg-rose-500 text-white' :
                            'bg-slate-800 text-slate-300'
                          }`}>
                            {opt}
                          </span>
                          <span className="flex-1 mt-0.5 leading-relaxed">{text}</span>
                        </button>
                      );
                    })}
                  </div>
                )}

                {/* Footer: Tags, Reveal toggle, External Link */}
                <div className="flex flex-wrap items-center justify-between gap-3 pt-2 border-t border-slate-800/80">
                  {/* Company tags */}
                  <div className="flex flex-wrap items-center gap-1.5">
                    <span className="text-[10px] text-slate-400 font-semibold">Asked in:</span>
                    {q.companyTags?.split(',').map(tag => (
                      <span key={tag} className="px-2 py-0.5 rounded-md bg-slate-800 text-[10px] font-bold text-slate-300">
                        {tag.trim()}
                      </span>
                    ))}
                  </div>

                  <div className="flex items-center gap-3">
                    <button
                      onClick={() => setRevealedSolutions(prev => ({ ...prev, [q.id]: !prev[q.id] }))}
                      className="text-xs font-bold text-indigo-400 hover:text-indigo-300 transition-colors"
                    >
                      {isRevealed ? 'Hide Explanation' : 'View Correct Answer & Explanation'}
                    </button>

                    {q.externalLink && (
                      <a
                        href={q.externalLink}
                        target="_blank"
                        rel="noopener noreferrer"
                        className="text-xs text-slate-400 hover:text-slate-200 flex items-center gap-1 font-medium"
                      >
                        <span>LeetCode</span>
                        <ExternalLink className="w-3.5 h-3.5" />
                      </a>
                    )}
                  </div>
                </div>

                {/* Revealed Explanation Box */}
                {isRevealed && (
                  <div className="p-4 rounded-2xl bg-slate-950 border border-indigo-500/30 text-xs space-y-2 animate-fadeIn">
                    <div className="flex items-center gap-2">
                      <span className="text-emerald-400 font-extrabold flex items-center gap-1">
                        <CheckCircle2 className="w-4 h-4" /> Correct Answer: Option {q.correctOption}
                      </span>
                      {hasChosen && (
                        <span className={`text-[11px] font-bold ${isCorrectChoice ? 'text-emerald-400' : 'text-rose-400'}`}>
                          ({isCorrectChoice ? 'You answered correctly!' : `Your answer: Option ${hasChosen}`})
                        </span>
                      )}
                    </div>
                    <div className="text-slate-300 whitespace-pre-line leading-relaxed pt-1">
                      {q.explanation}
                    </div>
                  </div>
                )}
              </div>
            );
          })}
        </div>
      )}
    </div>
  );
}
