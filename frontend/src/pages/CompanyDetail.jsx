import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import { 
  Building2, 
  ArrowLeft, 
  Briefcase, 
  Award, 
  CheckCircle2, 
  FileText, 
  HelpCircle, 
  Target, 
  Play, 
  Bookmark, 
  Code2, 
  Clock, 
  ExternalLink,
  ChevronRight,
  Sparkles
} from 'lucide-react';
import api from '../api/client';

export default function CompanyDetail() {
  const { id } = useParams();
  const [company, setCompany] = useState(null);
  const [questions, setQuestions] = useState([]);
  const [tests, setTests] = useState([]);
  const [loading, setLoading] = useState(true);
  const [activeTab, setActiveTab] = useState('roadmap'); // 'roadmap', 'questions', 'tests', 'questionsheet', 'dsasheet'
  const [questionCategory, setQuestionCategory] = useState('ALL');
  const [revealedSolutions, setRevealedSolutions] = useState({});

  useEffect(() => {
    fetchCompanyData();
  }, [id]);

  const fetchCompanyData = async () => {
    try {
      setLoading(true);
      const [compRes, qRes, tRes] = await Promise.all([
        api.get(`/companies/${id}`),
        api.get(`/companies/${id}/questions`),
        api.get(`/companies/${id}/tests`),
      ]);

      if (compRes.data.success) setCompany(compRes.data.data);
      if (qRes.data.success) setQuestions(qRes.data.data);
      if (tRes.data.success) setTests(tRes.data.data);
    } catch (err) {
      console.error('Failed to load company details', err);
    } finally {
      setLoading(false);
    }
  };

  const toggleSolution = (qId) => {
    setRevealedSolutions(prev => ({ ...prev, [qId]: !prev[qId] }));
  };

  const handleBookmark = async (qId) => {
    try {
      await api.post(`/questions/${qId}/bookmark`);
      // Update state locally
      setQuestions(prev => prev.map(q => q.id === qId ? { ...q, isBookmarked: !q.isBookmarked } : q));
    } catch (err) {
      console.error(err);
    }
  };

  const handleSolve = async (qId) => {
    try {
      await api.post(`/questions/${qId}/solve`);
      setQuestions(prev => prev.map(q => q.id === qId ? { ...q, isSolved: !q.isSolved } : q));
    } catch (err) {
      console.error(err);
    }
  };

  if (loading) {
    return (
      <div className="flex flex-col items-center justify-center min-h-[50vh] gap-3">
        <div className="w-10 h-10 border-4 border-indigo-500/20 border-t-indigo-500 rounded-full animate-spin"></div>
        <p className="text-sm text-slate-400">Loading {company?.name || 'Company'} details...</p>
      </div>
    );
  }

  if (!company) {
    return (
      <div className="p-8 text-center text-slate-400">
        Company not found. <Link to="/companies" className="text-indigo-400 font-bold">Back to Companies</Link>
      </div>
    );
  }

  // Filter questions by selected tab category
  const filteredQuestions = questions.filter(q => {
    if (questionCategory === 'ALL') return true;
    if (questionCategory === 'APTITUDE') return q.category === 'APTITUDE' || q.category === 'REASONING' || q.category === 'VERBAL';
    if (questionCategory === 'CORE_CS') return q.category === 'JAVA_OOPS' || q.category === 'DBMS' || q.category === 'OS' || q.category === 'NETWORKS';
    if (questionCategory === 'DSA') return q.category === 'DSA';
    return true;
  });

  // Parse hiring process stages if formatted with newlines
  const hiringRounds = company.hiringProcess ? company.hiringProcess.split('\n').filter(Boolean) : [];

  return (
    <div className="space-y-6">
      {/* Back button */}
      <div>
        <Link
          to="/companies"
          className="inline-flex items-center gap-2 text-xs font-semibold text-slate-400 hover:text-white transition-colors"
        >
          <ArrowLeft className="w-4 h-4" />
          <span>Back to Company Directory</span>
        </Link>
      </div>

      {/* Company Header Banner */}
      <div className="p-6 sm:p-8 rounded-3xl bg-slate-900 border border-slate-800 shadow-xl relative overflow-hidden">
        <div className="flex flex-col md:flex-row md:items-center justify-between gap-6 relative z-10">
          <div className="flex items-start sm:items-center gap-4">
            <div className="w-16 h-16 rounded-2xl bg-white p-2.5 flex items-center justify-center shadow-md flex-shrink-0">
              <img
                src={company.logoUrl}
                alt={company.name}
                className="max-h-full max-w-full object-contain"
                onError={(e) => {
                  e.target.onerror = null;
                  e.target.src = 'https://upload.wikimedia.org/wikipedia/commons/a/ac/No_image_available.svg';
                }}
              />
            </div>
            <div>
              <div className="flex items-center gap-2">
                <h2 className="text-2xl sm:text-3xl font-extrabold text-white tracking-tight">{company.name}</h2>
                <span className={`px-2.5 py-0.5 rounded-full text-[10px] font-black uppercase ${
                  company.difficultyLevel === 'HARD' ? 'bg-rose-500/20 text-rose-300 border border-rose-500/30' :
                  company.difficultyLevel === 'MEDIUM' ? 'bg-amber-500/20 text-amber-300 border border-amber-500/30' :
                  'bg-emerald-500/20 text-emerald-300 border border-emerald-500/30'
                }`}>
                  {company.difficultyLevel}
                </span>
              </div>
              <p className="text-xs sm:text-sm text-slate-400 mt-1 max-w-2xl leading-relaxed">
                {company.description}
              </p>
            </div>
          </div>

          {/* Quick Info Pills */}
          <div className="flex flex-col sm:flex-row md:flex-col gap-2 flex-shrink-0">
            <div className="px-3.5 py-2 rounded-xl bg-slate-950/70 border border-slate-800 text-xs">
              <span className="text-slate-400 block text-[10px] uppercase font-bold">Package Offered</span>
              <span className="text-indigo-400 font-extrabold">{company.packageInfo || 'Competitive'}</span>
            </div>
            <div className="px-3.5 py-2 rounded-xl bg-slate-950/70 border border-slate-800 text-xs">
              <span className="text-slate-400 block text-[10px] uppercase font-bold">Target Roles</span>
              <span className="text-slate-200 font-semibold">{company.rolesOffered || 'Software Engineer'}</span>
            </div>
          </div>
        </div>

        {/* Ambient Glow */}
        <div className="absolute right-0 top-0 w-80 h-80 bg-indigo-500/10 rounded-full blur-3xl pointer-events-none" />
      </div>

      {/* Tab Navigation */}
      <div className="flex border-b border-slate-800 space-x-2 text-sm font-semibold">
        <button
          onClick={() => setActiveTab('roadmap')}
          className={`py-3 px-4 border-b-2 transition-all flex items-center gap-2 ${
            activeTab === 'roadmap'
              ? 'border-indigo-500 text-indigo-400 font-bold'
              : 'border-transparent text-slate-400 hover:text-slate-200'
          }`}
        >
          <FileText className="w-4 h-4" />
          <span>Hiring Roadmap & Syllabus</span>
        </button>

        <button
          onClick={() => setActiveTab('questions')}
          className={`py-3 px-4 border-b-2 transition-all flex items-center gap-2 ${
            activeTab === 'questions'
              ? 'border-indigo-500 text-indigo-400 font-bold'
              : 'border-transparent text-slate-400 hover:text-slate-200'
          }`}
        >
          <HelpCircle className="w-4 h-4" />
          <span>Company Question Bank ({questions.length})</span>
        </button>

        <button
          onClick={() => setActiveTab('tests')}
          className={`py-3 px-4 border-b-2 transition-all flex items-center gap-2 ${
            activeTab === 'tests'
              ? 'border-indigo-500 text-indigo-400 font-bold'
              : 'border-transparent text-slate-400 hover:text-slate-200'
          }`}
        >
          <Target className="w-4 h-4" />
          <span>Mock Tests ({tests.length})</span>
        </button>
      </div>

      {/* Tab 1: Roadmap & Syllabus */}
      {activeTab === 'roadmap' && (
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          {/* Left 2 Cols: Recruitment Stages */}
          <div className="lg:col-span-2 space-y-6">
            {/* Step-by-Step Recruitment Process */}
            <div className="p-6 rounded-3xl bg-slate-900/90 border border-slate-800 space-y-4">
              <h3 className="text-base font-bold text-white flex items-center gap-2">
                <Sparkles className="w-4 h-4 text-indigo-400" />
                <span>Recruitment Stages & Selection Process</span>
              </h3>
              
              <div className="relative pl-6 space-y-6 before:content-[''] before:absolute before:left-2 before:top-2 before:bottom-2 before:w-0.5 before:bg-indigo-500/30">
                {hiringRounds.map((roundText, index) => {
                  const parts = roundText.split(':');
                  const title = parts[0];
                  const desc = parts.slice(1).join(':');

                  return (
                    <div key={index} className="relative group">
                      <div className="absolute -left-[27px] top-1 w-5 h-5 rounded-full bg-slate-900 border-2 border-indigo-500 flex items-center justify-center text-[10px] font-bold text-indigo-400">
                        {index + 1}
                      </div>
                      <div className="p-4 rounded-2xl bg-slate-950/60 border border-slate-800 group-hover:border-indigo-500/30 transition-all">
                        <h4 className="text-sm font-bold text-indigo-300">{title}</h4>
                        <p className="text-xs text-slate-300 mt-1 leading-relaxed">
                          {desc || roundText}
                        </p>
                      </div>
                    </div>
                  );
                })}
              </div>
            </div>

            {/* Exam Syllabus Breakdown */}
            <div className="p-6 rounded-3xl bg-slate-900/90 border border-slate-800 space-y-3">
              <h3 className="text-base font-bold text-white flex items-center gap-2">
                <FileText className="w-4 h-4 text-indigo-400" />
                <span>Test Syllabus Overview</span>
              </h3>
              <p className="text-xs sm:text-sm text-slate-300 leading-relaxed bg-slate-950/60 p-4 rounded-2xl border border-slate-800">
                {company.syllabusOverview}
              </p>
            </div>
          </div>

          {/* Right Col: Eligibility & Mock CTA */}
          <div className="space-y-6">
            <div className="p-6 rounded-3xl bg-slate-900/90 border border-slate-800 space-y-3">
              <h3 className="text-sm font-bold text-white flex items-center gap-2">
                <CheckCircle2 className="w-4 h-4 text-emerald-400" />
                <span>Eligibility Criteria</span>
              </h3>
              <div className="p-4 rounded-2xl bg-slate-950/60 border border-slate-800 text-xs text-slate-300 leading-relaxed">
                {company.eligibilityCriteria}
              </div>
            </div>

            {/* Quick Mock CTA Card */}
            <div className="p-6 rounded-3xl bg-gradient-to-br from-indigo-950/80 to-slate-900 border border-indigo-500/30 space-y-4">
              <div className="w-10 h-10 rounded-2xl bg-indigo-500/20 text-indigo-300 flex items-center justify-center">
                <Target className="w-5 h-5" />
              </div>
              <div>
                <h4 className="text-sm font-bold text-white">Simulated Company Mock Assessment</h4>
                <p className="text-xs text-slate-300 mt-1">
                  Experience {company.name}'s actual assessment pattern with timed scoring and detailed post-test solution analysis.
                </p>
              </div>
              <button
                onClick={() => setActiveTab('tests')}
                className="w-full py-2.5 px-4 rounded-xl bg-indigo-600 hover:bg-indigo-500 text-white font-bold text-xs flex items-center justify-center gap-2 shadow-lg shadow-indigo-600/20 transition-all"
              >
                <Play className="w-3.5 h-3.5 fill-white" />
                <span>View {company.name} Mock Tests</span>
              </button>
            </div>
          </div>
        </div>
      )}

      {/* Tab 2: Company Questions */}
      {activeTab === 'questions' && (
        <div className="space-y-4">
          {/* Category Filter Pills */}
          <div className="flex flex-wrap gap-2">
            {[
              { id: 'ALL', label: 'All Questions' },
              { id: 'APTITUDE', label: 'Aptitude & Reasoning' },
              { id: 'CORE_CS', label: 'Technical & CS Core' },
              { id: 'DSA', label: 'Coding / DSA' },
            ].map(tab => (
              <button
                key={tab.id}
                onClick={() => setQuestionCategory(tab.id)}
                className={`px-3 py-1.5 rounded-xl text-xs font-bold transition-all ${
                  questionCategory === tab.id
                    ? 'bg-indigo-600 text-white shadow-md shadow-indigo-600/20'
                    : 'bg-slate-900 text-slate-400 hover:text-slate-200 border border-slate-800'
                }`}
              >
                {tab.label}
              </button>
            ))}
          </div>

          {/* Question List */}
          <div className="space-y-4">
            {filteredQuestions.map((q, idx) => (
              <div key={q.id} className="p-5 rounded-2xl bg-slate-900/90 border border-slate-800 space-y-3">
                <div className="flex items-start justify-between gap-3">
                  <div className="space-y-1">
                    <div className="flex items-center gap-2">
                      <span className="text-xs font-bold text-indigo-400">Q{idx + 1}.</span>
                      <span className={`px-2 py-0.5 rounded text-[10px] font-extrabold uppercase ${
                        q.difficulty === 'HARD' ? 'bg-rose-500/10 text-rose-400' :
                        q.difficulty === 'MEDIUM' ? 'bg-amber-500/10 text-amber-400' :
                        'bg-emerald-500/10 text-emerald-400'
                      }`}>
                        {q.difficulty}
                      </span>
                      <span className="text-[10px] uppercase font-bold px-2 py-0.5 rounded bg-slate-800 text-slate-300">
                        {q.category}
                      </span>
                    </div>
                    <h4 className="text-sm font-bold text-white">{q.title}</h4>
                  </div>

                  <div className="flex items-center gap-2">
                    <button
                      onClick={() => handleBookmark(q.id)}
                      className={`p-1.5 rounded-lg border transition-colors ${
                        q.isBookmarked
                          ? 'bg-amber-500/10 text-amber-400 border-amber-500/30'
                          : 'text-slate-400 hover:text-slate-200 border-slate-800'
                      }`}
                      title="Bookmark"
                    >
                      <Bookmark className="w-4 h-4" />
                    </button>
                    <button
                      onClick={() => handleSolve(q.id)}
                      className={`p-1.5 rounded-lg border transition-colors ${
                        q.isSolved
                          ? 'bg-emerald-500/10 text-emerald-400 border-emerald-500/30'
                          : 'text-slate-400 hover:text-slate-200 border-slate-800'
                      }`}
                      title="Mark as Solved"
                    >
                      <CheckCircle2 className="w-4 h-4" />
                    </button>
                  </div>
                </div>

                {q.description && (
                  <p className="text-xs text-slate-300 leading-relaxed">{q.description}</p>
                )}

                {/* Options if MCQ */}
                {q.optionA && (
                  <div className="grid grid-cols-1 sm:grid-cols-2 gap-2 pt-2">
                    {['A', 'B', 'C', 'D'].map(opt => {
                      const optText = q[`option${opt}`];
                      if (!optText) return null;
                      const isCorrect = revealedSolutions[q.id] && q.correctOption === opt;
                      return (
                        <div
                          key={opt}
                          className={`p-2.5 rounded-xl text-xs border transition-colors flex items-center gap-2 ${
                            isCorrect 
                              ? 'bg-emerald-500/10 border-emerald-500/40 text-emerald-300 font-semibold'
                              : 'bg-slate-950/60 border-slate-800/80 text-slate-300'
                          }`}
                        >
                          <span className="w-5 h-5 rounded-md bg-slate-800 flex items-center justify-center font-bold text-[10px]">
                            {opt}
                          </span>
                          <span>{optText}</span>
                        </div>
                      );
                    })}
                  </div>
                )}

                {/* Solution Reveal Toggle */}
                <div className="pt-2 flex items-center justify-between border-t border-slate-800/80">
                  <button
                    onClick={() => toggleSolution(q.id)}
                    className="text-xs font-bold text-indigo-400 hover:text-indigo-300 transition-colors"
                  >
                    {revealedSolutions[q.id] ? 'Hide Answer & Explanation' : 'View Correct Answer & Explanation'}
                  </button>

                  {q.externalLink && (
                    <a
                      href={q.externalLink}
                      target="_blank"
                      rel="noopener noreferrer"
                      className="text-xs text-slate-400 hover:text-slate-200 flex items-center gap-1 font-medium"
                    >
                      <span>Practice on LeetCode</span>
                      <ExternalLink className="w-3.5 h-3.5" />
                    </a>
                  )}
                </div>

                {/* Solution Drawer */}
                {revealedSolutions[q.id] && (
                  <div className="p-4 rounded-xl bg-slate-950/80 border border-indigo-500/30 text-xs space-y-2 mt-2">
                    <div className="text-emerald-400 font-bold flex items-center gap-1.5">
                      <CheckCircle2 className="w-4 h-4" />
                      <span>Correct Answer: Option {q.correctOption}</span>
                    </div>
                    <div className="text-slate-300 whitespace-pre-line leading-relaxed">
                      {q.explanation}
                    </div>
                  </div>
                )}
              </div>
            ))}
          </div>
        </div>
      )}

      {/* Tab 3: Company Mock Tests */}
      {activeTab === 'tests' && (
        <div className="space-y-4">
          {tests.length > 0 ? (
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              {tests.map(test => (
                <div key={test.id} className="p-6 rounded-3xl bg-slate-900 border border-slate-800 space-y-4 flex flex-col justify-between">
                  <div>
                    <div className="flex items-center justify-between gap-2 mb-2">
                      <span className="text-[10px] font-black uppercase px-2 py-0.5 rounded bg-indigo-500/10 text-indigo-400 border border-indigo-500/20">
                        {test.category}
                      </span>
                      <span className="text-xs font-semibold text-slate-400">
                        {test.difficulty}
                      </span>
                    </div>
                    <h3 className="text-base font-bold text-white">{test.title}</h3>
                    <p className="text-xs text-slate-300 mt-1 leading-relaxed">{test.description}</p>
                    
                    <div className="flex items-center gap-4 text-xs text-slate-400 mt-4 pt-3 border-t border-slate-800/80">
                      <span className="flex items-center gap-1">
                        <Clock className="w-3.5 h-3.5 text-indigo-400" /> {test.durationMinutes} Mins
                      </span>
                      <span className="flex items-center gap-1">
                        <HelpCircle className="w-3.5 h-3.5 text-indigo-400" /> {test.totalQuestions} Questions
                      </span>
                      <span className="flex items-center gap-1">
                        <Award className="w-3.5 h-3.5 text-indigo-400" /> {test.totalMarks} Marks
                      </span>
                    </div>
                  </div>

                  <Link
                    to={`/tests/${test.id}`}
                    className="w-full py-2.5 px-4 rounded-xl bg-indigo-600 hover:bg-indigo-500 text-white font-bold text-xs flex items-center justify-center gap-2 shadow-lg shadow-indigo-600/20 transition-all"
                  >
                    <Play className="w-3.5 h-3.5 fill-white" />
                    <span>Start Mock Exam</span>
                  </Link>
                </div>
              ))}
            </div>
          ) : (
            <div className="py-12 text-center rounded-2xl bg-slate-900/60 border border-dashed border-slate-800">
              <Target className="w-10 h-10 text-slate-600 mx-auto mb-2" />
              <p className="text-sm font-semibold text-slate-300">Specific Company Test Scheduled Soon</p>
              <p className="text-xs text-slate-500 max-w-sm mx-auto mt-1 mb-4">
                You can still take the TCS NQT Comprehensive Mock or Core CS Sprint tests.
              </p>
              <Link
                to="/tests"
                className="inline-flex items-center gap-2 px-4 py-2 rounded-xl bg-indigo-600 hover:bg-indigo-500 text-white text-xs font-bold"
              >
                <span>Browse All Mock Tests</span>
              </Link>
            </div>
          )}
        </div>
      )}
    </div>
  );
}
