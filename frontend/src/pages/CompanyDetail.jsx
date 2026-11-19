import React, { useEffect, useMemo, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import {
  ArrowLeft,
  Award,
  BarChart3,
  Bookmark,
  Briefcase,
  CheckCircle2,
  ChevronDown,
  Code2,
  ExternalLink,
  FileText,
  Filter,
  HelpCircle,
  Layers,
  Search,
  Sparkles,
  Target,
  Users,
} from 'lucide-react';
import api from '../api/client';
import { getCompanyGuide, getQuestionPattern, getRoadmap, getRoundPreparation } from '../data/companyPreparation';

const TABS = [
  { id: 'overview', label: 'Overview', icon: Building2Icon },
  { id: 'roles', label: 'Roles & packages', icon: Briefcase },
  { id: 'roadmap', label: 'Roadmap', icon: FileText },
  { id: 'hiring', label: 'Hiring process', icon: Users },
  { id: 'questions', label: 'Question bank', icon: HelpCircle },
  { id: 'dsa', label: 'DSA practice', icon: Code2 },
  { id: 'insights', label: 'Patterns & insights', icon: BarChart3 },
  { id: 'progress', label: 'My progress', icon: CheckCircle2 },
];

function Building2Icon(props) {
  return <Layers {...props} />;
}

function errorMessage(error, fallback) {
  return error.response?.data?.message || fallback;
}

function SectionCard({ title, icon: Icon, children, className = '' }) {
  return (
    <section className={`p-5 sm:p-6 rounded-3xl bg-slate-900/90 border border-slate-800 space-y-4 ${className}`}>
      <h3 className="text-base font-bold text-white flex items-center gap-2">
        {Icon && <Icon className="w-4 h-4 text-indigo-400" />}
        {title}
      </h3>
      {children}
    </section>
  );
}

function Notice({ children, tone = 'slate' }) {
  const tones = {
    slate: 'bg-slate-950/60 border-slate-800 text-slate-300',
    amber: 'bg-amber-500/10 border-amber-500/25 text-amber-200',
    rose: 'bg-rose-500/10 border-rose-500/30 text-rose-200',
  };
  return <div className={`p-3 rounded-xl border text-xs leading-relaxed ${tones[tone]}`}>{children}</div>;
}

export default function CompanyDetail() {
  const { id } = useParams();
  const [company, setCompany] = useState(null);
  const [questions, setQuestions] = useState([]);
  const [dsaQuestions, setDsaQuestions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [loadError, setLoadError] = useState('');
  const [questionError, setQuestionError] = useState('');
  const [dsaQuestionError, setDsaQuestionError] = useState('');
  const [actionError, setActionError] = useState('');
  const [activeTab, setActiveTab] = useState('overview');
  const [selectedRoleIndex, setSelectedRoleIndex] = useState(0);
  const [search, setSearch] = useState('');
  const [category, setCategory] = useState('ALL');
  const [topic, setTopic] = useState('ALL');
  const [difficulty, setDifficulty] = useState('ALL');
  const [questionType, setQuestionType] = useState('ALL');
  const [progressFilter, setProgressFilter] = useState('ALL');
  const [patternFilter, setPatternFilter] = useState('ALL');
  const [dsaSearch, setDsaSearch] = useState('');
  const [dsaTopic, setDsaTopic] = useState('ALL');
  const [dsaDifficulty, setDsaDifficulty] = useState('ALL');
  const [dsaProgress, setDsaProgress] = useState('ALL');
  const [dsaPattern, setDsaPattern] = useState('ALL');
  const [revealedSolutions, setRevealedSolutions] = useState({});
  const [roadmapProgress, setRoadmapProgress] = useState({});
  const [progressError, setProgressError] = useState('');

  const guide = getCompanyGuide(company?.code);
  const roles = guide?.roles || (company?.rolesOffered || 'Software Engineer')
    .split(',')
    .map((title) => ({ title: title.trim(), package: 'Compensation not specified in the company catalog; check the current listing.', tier: 'entry', focus: 'Build role-relevant fundamentals and verify the current hiring notice.' }));
  const role = roles[selectedRoleIndex] || roles[0];
  const roadmap = useMemo(() => getRoadmap(role), [role]);
  const topics = useMemo(
    () => [...new Set([...questions, ...dsaQuestions].map((question) => question.topicName).filter(Boolean))].sort(),
    [questions, dsaQuestions],
  );

  useEffect(() => {
    let active = true;
    const fetchCompanyData = async () => {
      setLoading(true);
      setLoadError('');
      setQuestionError('');
      setDsaQuestionError('');
      setActionError('');
      setCompany(null);
      setQuestions([]);
      setDsaQuestions([]);
      setActiveTab('overview');
      setSelectedRoleIndex(0);
      try {
        const companyResponse = await api.get(`/companies/${id}`);
        if (!companyResponse.data.success || !companyResponse.data.data) {
          throw new Error('The company service returned an unsuccessful response.');
        }
        if (!active) return;
        const currentCompany = companyResponse.data.data;
        setCompany(currentCompany);

        const [questionResult, dsaQuestionResult] = await Promise.allSettled([
          api.get(`/companies/${id}/questions`),
          api.get('/questions/dsa'),
        ]);
        if (!active) return;

        if (questionResult.status === 'fulfilled' && questionResult.value.data.success) {
          setQuestions(questionResult.value.data.data || []);
        } else {
          setQuestionError(questionResult.status === 'rejected'
            ? errorMessage(questionResult.reason, 'Unable to load company questions.')
            : 'The question service returned an unsuccessful response.');
        }

        if (dsaQuestionResult.status === 'fulfilled' && dsaQuestionResult.value.data.success) {
          setDsaQuestions(dsaQuestionResult.value.data.data || []);
        } else {
          setDsaQuestionError(dsaQuestionResult.status === 'rejected'
            ? errorMessage(dsaQuestionResult.reason, 'Unable to load DSA practice questions.')
            : 'The DSA question service returned an unsuccessful response.');
        }

        try {
          const stored = localStorage.getItem(`placify_company_roadmap_${currentCompany.code}`);
          const parsed = stored ? JSON.parse(stored) : {};
          setRoadmapProgress(parsed && typeof parsed === 'object' && !Array.isArray(parsed) ? parsed : {});
          setProgressError('');
        } catch {
          setRoadmapProgress({});
          setProgressError('Roadmap progress could not be loaded from this browser.');
        }
      } catch (error) {
        if (active) {
          setLoadError(errorMessage(error, error.message || 'Unable to load company details. Please try again.'));
        }
      } finally {
        if (active) setLoading(false);
      }
    };

    fetchCompanyData();
    return () => {
      active = false;
    };
  }, [id]);

  const filteredQuestions = useMemo(() => questions.filter((question) => {
    const query = search.trim().toLowerCase();
    const matchesSearch = !query || [question.title, question.description, question.topicName, question.category]
      .some((value) => value?.toLowerCase().includes(query));
    const matchesCategory = category === 'ALL' || question.category === category;
    const matchesTopic = topic === 'ALL' || question.topicName === topic;
    const matchesDifficulty = difficulty === 'ALL' || question.difficulty === difficulty;
    const matchesType = questionType === 'ALL' || question.questionType === questionType;
    const matchesProgress = progressFilter === 'ALL'
      || (progressFilter === 'SOLVED' && question.isSolved)
      || (progressFilter === 'UNSOLVED' && !question.isSolved)
      || (progressFilter === 'BOOKMARKED' && question.isBookmarked);
    const matchesPattern = patternFilter === 'ALL'
      || (question.category === 'DSA' && getQuestionPattern(question) === patternFilter);
    return matchesSearch && matchesCategory && matchesTopic && matchesDifficulty && matchesType && matchesProgress && matchesPattern;
  }), [questions, search, category, topic, difficulty, questionType, progressFilter, patternFilter]);

  const patternGroups = useMemo(() => {
    const groups = new Map();
    dsaQuestions.forEach((question) => {
      const name = getQuestionPattern(question);
      groups.set(name, [...(groups.get(name) || []), question]);
    });
    return [...groups.entries()].sort(([first], [second]) => first.localeCompare(second));
  }, [dsaQuestions]);

  const filteredDsaQuestions = useMemo(() => {
    const query = dsaSearch.trim().toLowerCase();
    return dsaQuestions.filter((question) => {
      const matchesSearch = !query || [question.title, question.description, question.topicName]
        .some((value) => value?.toLowerCase().includes(query));
      const matchesTopic = dsaTopic === 'ALL' || question.topicName === dsaTopic;
      const matchesDifficulty = dsaDifficulty === 'ALL' || question.difficulty === dsaDifficulty;
      const matchesProgress = dsaProgress === 'ALL'
        || (dsaProgress === 'SOLVED' && question.isSolved)
        || (dsaProgress === 'UNSOLVED' && !question.isSolved)
        || (dsaProgress === 'BOOKMARKED' && question.isBookmarked);
      const matchesPattern = dsaPattern === 'ALL' || getQuestionPattern(question) === dsaPattern;
      return matchesSearch && matchesTopic && matchesDifficulty && matchesProgress && matchesPattern;
    });
  }, [dsaQuestions, dsaSearch, dsaTopic, dsaDifficulty, dsaProgress, dsaPattern]);

  const solvedCount = questions.filter((question) => question.isSolved).length;
  const bookmarkedCount = questions.filter((question) => question.isBookmarked).length;
  const roadmapDoneCount = Object.values(roadmapProgress).filter(Boolean).length;
  const roadmapPercent = roadmap.length ? Math.round((roadmapDoneCount / roadmap.length) * 100) : 0;

  const toggleQuestionState = async (questionId, action, field) => {
    setActionError('');
    try {
      const response = await api.post(`/questions/${questionId}/${action}`);
      const nextState = response.data.data?.[field];
      const updateQuestion = (current) => current.map((question) => question.id === questionId
        ? { ...question, [field]: typeof nextState === 'boolean' ? nextState : !question[field] }
        : question);
      setQuestions(updateQuestion);
      setDsaQuestions(updateQuestion);
    } catch (error) {
      setActionError(errorMessage(error, `Unable to update ${field === 'isSolved' ? 'question progress' : 'bookmark'}. Please try again.`));
    }
  };

  const toggleRoadmapStep = (index) => {
    const updated = { ...roadmapProgress, [index]: !roadmapProgress[index] };
    setRoadmapProgress(updated);
    setProgressError('');
    try {
      localStorage.setItem(`placify_company_roadmap_${company.code}`, JSON.stringify(updated));
    } catch {
      setProgressError('Roadmap progress changed for this visit but could not be saved in this browser.');
    }
  };

  if (loading) {
    return (
      <div className="flex flex-col items-center justify-center min-h-[50vh] gap-3">
        <div className="w-10 h-10 border-4 border-indigo-500/20 border-t-indigo-500 rounded-full animate-spin" />
        <p className="text-sm text-slate-400">Loading company preparation guide...</p>
      </div>
    );
  }

  if (loadError || !company) {
    return (
      <div className="p-8 text-center space-y-4">
        <p role="alert" className="text-sm text-rose-200">{loadError || 'Company not found.'}</p>
        <Link to="/companies" className="inline-flex items-center gap-2 text-indigo-400 font-bold">
          <ArrowLeft className="w-4 h-4" /> Back to Companies
        </Link>
      </div>
    );
  }

  const hiringRounds = company.hiringProcess?.split('\n').filter(Boolean) || [];
  const categories = [...new Set(questions.map((question) => question.category).filter(Boolean))].sort();
  const questionTypes = [...new Set(questions.map((question) => question.questionType).filter(Boolean))].sort();
  const dsaTopics = [...new Set(dsaQuestions.map((question) => question.topicName).filter(Boolean))].sort();

  return (
    <div className="space-y-6">
      <Link to="/companies" className="inline-flex items-center gap-2 text-xs font-semibold text-slate-400 hover:text-slate-200 transition-colors">
        <ArrowLeft className="w-4 h-4" /> Back to Company Directory
      </Link>

      <header className="p-6 sm:p-8 rounded-3xl bg-slate-900 border border-slate-800 shadow-xl relative overflow-hidden">
        <div className="flex flex-col md:flex-row md:items-center justify-between gap-6 relative z-10">
          <div className="flex items-start sm:items-center gap-4">
            <div className="w-16 h-16 rounded-2xl bg-white p-2.5 flex items-center justify-center shadow-md flex-shrink-0">
              <img src={company.logoUrl} alt={`${company.name} logo`} className="max-h-full max-w-full object-contain" />
            </div>
            <div>
              <div className="flex flex-wrap items-center gap-2">
                <h1 className="text-2xl sm:text-3xl font-extrabold text-white tracking-tight">{company.name}</h1>
                <span className="px-2.5 py-0.5 rounded-full text-[10px] font-black uppercase bg-indigo-500/15 text-indigo-300 border border-indigo-500/30">
                  {company.difficultyLevel || 'Role-dependent'}
                </span>
              </div>
              <p className="text-xs sm:text-sm text-slate-400 mt-1 max-w-2xl leading-relaxed">{company.description}</p>
            </div>
          </div>
          <a href={guide?.official || company.websiteUrl} target="_blank" rel="noopener noreferrer"
            className="inline-flex items-center justify-center gap-2 px-4 py-2.5 rounded-xl bg-slate-800 hover:bg-slate-700 text-sm font-bold text-slate-200">
            Current careers page <ExternalLink className="w-4 h-4" />
          </a>
        </div>
        <div className="absolute right-0 top-0 w-80 h-80 bg-indigo-500/10 rounded-full blur-3xl pointer-events-none" />
      </header>

      <nav aria-label="Company preparation sections" className="flex gap-2 overflow-x-auto pb-2 border-b border-slate-800">
        {TABS.map(({ id: tabId, label, icon: Icon }) => (
          <button key={tabId} onClick={() => setActiveTab(tabId)} aria-current={activeTab === tabId ? 'page' : undefined}
            className={`whitespace-nowrap px-3 py-2.5 rounded-xl text-xs font-bold flex items-center gap-2 transition-colors ${
              activeTab === tabId ? 'bg-indigo-600 text-white' : 'text-slate-400 hover:text-slate-200 hover:bg-slate-800'
            }`}>
            <Icon className="w-4 h-4" /> {label}
          </button>
        ))}
      </nav>

      {activeTab === 'overview' && (
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-5">
          <div className="lg:col-span-2 space-y-5">
            <SectionCard title="Start with the role that matches your target" icon={Briefcase}>
              <RoleSelector roles={roles} selectedIndex={selectedRoleIndex} onChange={setSelectedRoleIndex} />
              <div className="p-4 rounded-2xl bg-slate-950/60 border border-slate-800 space-y-2">
                <p className="text-sm font-bold text-white">{role.title}</p>
                <p className="text-xs text-indigo-200">{role.focus}</p>
                <p className="text-xs text-slate-300">{role.package}</p>
                <p className="text-[11px] text-slate-500">Compensation shown here is inherited from the existing Placify company catalog, not independently verified or an official current offer. Confirm the latest role listing.</p>
              </div>
            </SectionCard>
            <SectionCard title="What to prioritize for this role" icon={Sparkles}>
              <div className="grid sm:grid-cols-2 gap-3">
                <FocusCard title="Coding patterns" value={role.tier === 'entry'
                  ? 'Arrays, strings, hashing, sorting, and basic search. Add recursion after fundamentals are reliable.'
                  : 'Add trees, graphs, heaps, greedy reasoning, and dynamic programming; explain complexity and edge cases.'} />
                <FocusCard title="Technical subjects" value={guide?.csFocus || company.syllabusOverview || 'Review the subjects named in the current role description.'} />
                <FocusCard title="Languages" value="No single language is assumed here. Use a language permitted by the current assessment and one you can debug confidently." />
                <FocusCard title="Preparation order" value="Start with the current assessment format, then coding fundamentals, role-relevant CS, mock interviews, and behavioral examples." />
              </div>
            </SectionCard>
          </div>
          <div className="space-y-5">
            <SectionCard title="Your preparation snapshot" icon={Target}>
              <Stat label="Tagged questions" value={questions.length} />
              <Stat label="Questions solved" value={`${solvedCount} / ${questions.length}`} />
              <Stat label="Bookmarked" value={bookmarkedCount} />
              <Stat label="Roadmap complete" value={`${roadmapPercent}%`} />
              <button onClick={() => setActiveTab('progress')} className="w-full py-2 rounded-xl bg-indigo-600 hover:bg-indigo-500 text-white text-xs font-bold">
                Open my progress
              </button>
            </SectionCard>
            <Notice tone="amber">
              Hiring patterns and eligibility can change by role, country, campus, and hiring cycle. The catalog summary is not an official or guaranteed current process; verify the active recruiter notice.
            </Notice>
          </div>
        </div>
      )}

      {activeTab === 'roles' && (
        <div className="space-y-5">
          <SectionCard title="Role-specific preparation tracks" icon={Award}>
            <p className="text-xs text-slate-400">Choose a track to tailor roadmap priorities. Role titles and catalog compensation are indicative; current job postings take precedence.</p>
            <RoleSelector roles={roles} selectedIndex={selectedRoleIndex} onChange={setSelectedRoleIndex} />
            <div className="grid md:grid-cols-2 gap-4">
              {roles.map((item, index) => (
                <button key={item.title} onClick={() => setSelectedRoleIndex(index)}
                  className={`text-left p-4 rounded-2xl border transition-colors ${
                    index === selectedRoleIndex ? 'bg-indigo-500/10 border-indigo-500/40' : 'bg-slate-950/50 border-slate-800 hover:border-slate-700'
                  }`}>
                  <div className="flex items-center justify-between gap-3">
                    <h3 className="text-sm font-bold text-white">{item.title}</h3>
                    <span className="text-[10px] uppercase tracking-wide text-indigo-300">{item.tier}</span>
                  </div>
                  <p className="text-xs text-slate-300 mt-2">{item.package}</p>
                  <p className="text-xs text-slate-400 mt-2">{item.focus}</p>
                </button>
              ))}
            </div>
          </SectionCard>
          <SectionCard title={`How preparation changes for ${role.title}`} icon={Code2}>
            <div className="grid md:grid-cols-2 gap-3">
              <FocusCard title="DSA expectations" value={role.tier === 'entry'
                ? 'Be fluent with arrays, strings, hashing, sorting, and basic complexity. Trees/graphs/DP are stretch preparation, not a claim about a fixed exam.'
                : 'Build on arrays/strings with trees, graph traversal, heaps, greedy methods, and dynamic programming. Practice deriving solutions and discussing trade-offs.'} />
              <FocusCard title="Assessment style" value={role.tier === 'entry'
                ? 'Balance timed aptitude or technical MCQs with clean implementation and strong communication.'
                : 'Spend more time on coding depth, edge-case analysis, and explaining decisions; confirm the actual interview loop for this opening.'} />
            </div>
          </SectionCard>
          <Notice tone="amber">No public, role-specific compensation source is attached to these catalog values. Do not treat them as verified current compensation or eligibility.</Notice>
        </div>
      )}

      {activeTab === 'roadmap' && (
        <div className="grid lg:grid-cols-3 gap-5">
          <div className="lg:col-span-2 space-y-4">
            <SectionCard title={`10-step plan · ${role.title}`} icon={FileText}>
              <RoleSelector roles={roles} selectedIndex={selectedRoleIndex} onChange={setSelectedRoleIndex} />
              <div className="space-y-3">
                {roadmap.map((step, index) => (
                  <article key={step.title} className="p-4 rounded-2xl bg-slate-950/60 border border-slate-800">
                    <div className="flex items-start gap-3">
                      <button onClick={() => toggleRoadmapStep(index)} aria-pressed={Boolean(roadmapProgress[index])}
                        aria-label={`${roadmapProgress[index] ? 'Mark incomplete' : 'Mark complete'}: ${step.title}`}
                        className={`mt-0.5 flex-shrink-0 ${roadmapProgress[index] ? 'text-emerald-400' : 'text-slate-600 hover:text-indigo-300'}`}>
                        <CheckCircle2 className="w-5 h-5" />
                      </button>
                      <div className="min-w-0 flex-1 space-y-2">
                        <div className="flex flex-wrap items-center gap-2">
                          <span className="text-[10px] font-black text-indigo-300">STEP {index + 1}</span>
                          <h3 className="text-sm font-bold text-white">{step.title}</h3>
                          <Badge tone={step.priority === 'High' ? 'indigo' : 'slate'}>{step.priority} priority</Badge>
                          <Badge tone="slate">{step.difficulty}</Badge>
                        </div>
                        <p className="text-xs text-slate-300"><strong className="text-slate-200">Topics:</strong> {step.topics}</p>
                        <p className="text-xs text-slate-400"><strong className="text-slate-300">Subtopics:</strong> {step.subtopics}</p>
                        <p className="text-xs text-slate-400"><strong className="text-slate-300">Practice:</strong> {step.practice}</p>
                        <p className="text-xs text-indigo-200"><strong>Tip:</strong> {step.tip}</p>
                      </div>
                    </div>
                  </article>
                ))}
              </div>
            </SectionCard>
          </div>
          <div className="space-y-5">
            <SectionCard title="Preparation progress" icon={Target}>
              <div className="flex justify-between text-xs text-slate-300"><span>Completed steps</span><strong>{roadmapDoneCount} / {roadmap.length}</strong></div>
              <div className="h-2 rounded-full bg-slate-800 overflow-hidden"><div className="h-full bg-indigo-500 transition-all" style={{ width: `${roadmapPercent}%` }} /></div>
              {progressError && <p role="status" className="text-xs text-amber-300">{progressError}</p>}
              <p className="text-xs text-slate-400">Roadmap checklist is saved in this browser. Solved-question progress is saved to your Placify account.</p>
            </SectionCard>
            <SectionCard title="Current syllabus summary" icon={Layers}>
              <p className="text-xs text-slate-300 leading-relaxed">{company.syllabusOverview || 'No syllabus summary is listed in the company catalog.'}</p>
              <p className="text-[11px] text-slate-500">Treat this as a topic checklist, not a current official syllabus.</p>
            </SectionCard>
          </div>
        </div>
      )}

      {activeTab === 'hiring' && (
        <div className="grid lg:grid-cols-3 gap-5">
          <div className="lg:col-span-2 space-y-5">
            <SectionCard title="Selection process in the current Placify catalog" icon={Users}>
              <Notice tone="amber">This sequence is an existing catalog summary, not verified current policy. Number of questions, duration, cutoffs, and negative marking are not asserted unless shown in the active company notice.</Notice>
              {hiringRounds.length ? (
                <div className="space-y-3">
                  {hiringRounds.map((roundText, index) => {
                    const separator = roundText.indexOf(':');
                    const title = separator >= 0 ? roundText.slice(0, separator) : `Stage ${index + 1}`;
                    const description = separator >= 0 ? roundText.slice(separator + 1).trim() : roundText;
                    return (
                      <article key={`${index}-${title}`} className="p-4 rounded-2xl bg-slate-950/60 border border-slate-800">
                        <div className="flex flex-wrap items-center gap-2 mb-2">
                          <span className="w-6 h-6 rounded-full bg-indigo-500/15 text-indigo-300 text-xs font-black flex items-center justify-center">{index + 1}</span>
                          <h3 className="text-sm font-bold text-white">{title}</h3>
                          <Badge tone="slate">Catalog summary</Badge>
                        </div>
                        <p className="text-xs text-slate-300 leading-relaxed">{description}</p>
                        <p className="text-xs text-indigo-200 mt-3"><strong>Preparation:</strong> {getRoundPreparation(index)}</p>
                      </article>
                    );
                  })}
                </div>
              ) : <EmptyState title="No selection stages listed" detail="Check the current company careers notice for the role you are targeting." />}
            </SectionCard>
          </div>
          <div className="space-y-5">
            <SectionCard title="Eligibility listed in catalog" icon={CheckCircle2}>
              <p className="text-xs leading-relaxed text-slate-300">{company.eligibilityCriteria || 'Eligibility is not listed. Check the current role-specific notice.'}</p>
              <p className="text-[11px] text-amber-200">Not independently verified; confirm academic, backlog, and degree requirements in the current notice.</p>
            </SectionCard>
            <SectionCard title="Common preparation mistakes" icon={Filter}>
              <ul className="list-disc pl-4 space-y-2 text-xs text-slate-300">
                <li>Preparing from a past pattern without checking the current role notice.</li>
                <li>Practicing only easy questions and not reviewing mistakes or edge cases.</li>
                <li>Memorizing answers instead of explaining reasoning and project decisions.</li>
                <li>Assuming a cutoff, question count, duration, or package from an unverified source.</li>
              </ul>
            </SectionCard>
          </div>
        </div>
      )}

      {activeTab === 'questions' && (
        <div className="space-y-4">
          <Notice tone="amber">These are questions tagged to this company in the Placify catalog. The current question schema has no source, year, or role evidence, so none are represented as confirmed company PYQs.</Notice>
          {questionError && <Notice tone="rose"><span role="alert">{questionError}</span></Notice>}
          {actionError && <Notice tone="rose"><span role="alert">{actionError}</span></Notice>}
          <div className="p-4 rounded-2xl bg-slate-900 border border-slate-800 grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-3">
            <label className="relative sm:col-span-2 lg:col-span-3">
              <span className="sr-only">Search company questions</span>
              <Search className="w-4 h-4 text-slate-500 absolute left-3 top-2.5" />
              <input value={search} onChange={(event) => setSearch(event.target.value)} placeholder="Search title, topic, or question text..."
                className="w-full pl-9 pr-3 py-2 rounded-xl bg-slate-950 border border-slate-800 text-xs text-white placeholder-slate-500 focus:outline-none focus:ring-2 focus:ring-indigo-500/40" />
            </label>
            <SelectFilter label="Category" value={category} onChange={setCategory} options={['ALL', ...categories]} />
            <SelectFilter label="Topic" value={topic} onChange={setTopic} options={['ALL', ...topics]} />
            <SelectFilter label="Difficulty" value={difficulty} onChange={setDifficulty} options={['ALL', 'EASY', 'MEDIUM', 'HARD']} />
            <SelectFilter label="Question format" value={questionType} onChange={setQuestionType} options={['ALL', ...questionTypes]} />
            <SelectFilter label="Progress" value={progressFilter} onChange={setProgressFilter} options={['ALL', 'SOLVED', 'UNSOLVED', 'BOOKMARKED']} />
            <SelectFilter label="DSA pattern" value={patternFilter} onChange={setPatternFilter} options={['ALL', ...patternGroups.map(([name]) => name)]} />
          </div>
          <p className="text-xs text-slate-400">Showing <strong className="text-white">{filteredQuestions.length}</strong> of {questions.length} tagged questions</p>
          {filteredQuestions.length ? (
            <div className="space-y-4">
              {filteredQuestions.map((question, index) => (
                <QuestionCard key={question.id} question={question} index={index}
                  revealed={Boolean(revealedSolutions[question.id])}
                  onReveal={() => setRevealedSolutions((current) => ({ ...current, [question.id]: !current[question.id] }))}
                  onBookmark={() => toggleQuestionState(question.id, 'bookmark', 'isBookmarked')}
                  onSolve={() => toggleQuestionState(question.id, 'solve', 'isSolved')} />
              ))}
            </div>
          ) : !questionError ? (
            <EmptyState title={questions.length ? 'No questions match these filters' : 'No tagged questions yet'}
              detail={questions.length ? 'Try broadening your search or clearing one of the filters.' : 'The company catalog does not currently have questions tagged for this company.'} />
          ) : null}
        </div>
      )}

      {activeTab === 'dsa' && (
        <div className="space-y-4">
          <Notice tone="amber">Practice problems from the general DSA catalog. Company tags, where present, are not evidence that a company asked a particular question.</Notice>
          {dsaQuestionError && <Notice tone="rose"><span role="alert">{dsaQuestionError}</span></Notice>}
          {actionError && <Notice tone="rose"><span role="alert">{actionError}</span></Notice>}
          <div className="p-4 rounded-2xl bg-slate-900 border border-slate-800 grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-3">
            <label className="relative sm:col-span-2 lg:col-span-3">
              <span className="sr-only">Search DSA practice questions</span>
              <Search className="w-4 h-4 text-slate-500 absolute left-3 top-2.5" />
              <input value={dsaSearch} onChange={(event) => setDsaSearch(event.target.value)} placeholder="Search title, topic, or question text..."
                className="w-full pl-9 pr-3 py-2 rounded-xl bg-slate-950 border border-slate-800 text-xs text-white placeholder-slate-500 focus:outline-none focus:ring-2 focus:ring-indigo-500/40" />
            </label>
            <SelectFilter label="Topic" value={dsaTopic} onChange={setDsaTopic} options={['ALL', ...dsaTopics]} />
            <SelectFilter label="Difficulty" value={dsaDifficulty} onChange={setDsaDifficulty} options={['ALL', 'EASY', 'MEDIUM', 'HARD']} />
            <SelectFilter label="Progress" value={dsaProgress} onChange={setDsaProgress} options={['ALL', 'SOLVED', 'UNSOLVED', 'BOOKMARKED']} />
            <SelectFilter label="DSA pattern" value={dsaPattern} onChange={setDsaPattern} options={['ALL', ...patternGroups.map(([name]) => name)]} />
          </div>
          <p className="text-xs text-slate-400">Showing <strong className="text-white">{filteredDsaQuestions.length}</strong> of {dsaQuestions.length} DSA practice questions</p>
          {filteredDsaQuestions.length ? (
            <div className="space-y-4">
              {filteredDsaQuestions.map((question, index) => (
                <QuestionCard key={question.id} question={question} index={index}
                  revealed={Boolean(revealedSolutions[question.id])}
                  onReveal={() => setRevealedSolutions((current) => ({ ...current, [question.id]: !current[question.id] }))}
                  onBookmark={() => toggleQuestionState(question.id, 'bookmark', 'isBookmarked')}
                  onSolve={() => toggleQuestionState(question.id, 'solve', 'isSolved')} />
              ))}
            </div>
          ) : !dsaQuestionError ? (
            <EmptyState title={dsaQuestions.length ? 'No questions match these filters' : 'No DSA practice questions available'}
              detail={dsaQuestions.length ? 'Try broadening your search or clearing one of the filters.' : 'The DSA practice catalog could not be found.'} />
          ) : null}
        </div>
      )}

      {activeTab === 'insights' && (
        <div className="grid lg:grid-cols-3 gap-5">
          <div className="lg:col-span-2 space-y-5">
            <SectionCard title="DSA practice patterns" icon={Code2}>
              <p className="text-xs text-slate-400">Pattern grouping is inferred from titles and topic labels in the general DSA practice catalog. It is not company-specific or evidence that a company asked these questions.</p>
              {patternGroups.length ? (
                <div className="grid sm:grid-cols-2 gap-3">
                  {patternGroups.map(([name, groupQuestions]) => (
                    <button key={name} onClick={() => { setDsaPattern(name); setActiveTab('dsa'); }}
                      className="text-left p-4 rounded-2xl bg-slate-950/60 border border-slate-800 hover:border-indigo-500/40">
                      <div className="flex justify-between gap-3"><span className="text-sm font-bold text-white">{name}</span><span className="text-xs text-indigo-300">{groupQuestions.length} catalog {groupQuestions.length === 1 ? 'item' : 'items'}</span></div>
                      <p className="text-[11px] text-slate-500 mt-2">Select to practice this DSA group</p>
                    </button>
                  ))}
                </div>
              ) : <EmptyState title="No DSA practice questions" detail="The general DSA practice catalog is currently empty." />}
            </SectionCard>
            <SectionCard title="Preparation priorities for this track" icon={Sparkles}>
              <RoleSelector roles={roles} selectedIndex={selectedRoleIndex} onChange={setSelectedRoleIndex} />
              <ul className="list-disc pl-4 space-y-2 text-xs text-slate-300">
                {(guide?.patterns || ['Arrays and strings', 'Hashing and sorting', 'Role-specific problem solving']).map((pattern) => <li key={pattern}>{pattern}</li>)}
              </ul>
              <p className="text-xs text-slate-400">These are practice priorities, not reported-frequency statistics or confirmed past questions.</p>
            </SectionCard>
          </div>
          <div className="space-y-5">
            <SectionCard title="Catalog coverage (not hiring frequency)" icon={BarChart3}>
              <Stat label="DSA practice questions" value={dsaQuestions.length} />
              {Object.entries(dsaQuestions.reduce((counts, question) => {
                const key = question.topicName || question.category || 'Uncategorized';
                counts[key] = (counts[key] || 0) + 1;
                return counts;
              }, {})).sort(([, first], [, second]) => second - first).slice(0, 6).map(([name, count]) => (
                <div key={name} className="flex justify-between gap-3 text-xs text-slate-300"><span>{name}</span><strong>{count}</strong></div>
              ))}
              <Notice>Question records do not include years or reliable sources. The app cannot calculate reported-question frequency or a historical time period.</Notice>
            </SectionCard>
            <SectionCard title="Interview subject focus" icon={Layers}>
              <p className="text-xs text-slate-300 leading-relaxed">{guide?.csFocus || company.syllabusOverview || 'Review core concepts relevant to the advertised role.'}</p>
            </SectionCard>
          </div>
        </div>
      )}

      {activeTab === 'progress' && (
        <div className="grid md:grid-cols-3 gap-5">
          <SectionCard title="Tagged question progress" icon={CheckCircle2}>
            <Stat label="Solved" value={`${solvedCount} / ${questions.length}`} />
            <Stat label="Remaining" value={questions.length - solvedCount} />
            <div className="h-2 rounded-full bg-slate-800 overflow-hidden"><div className="h-full bg-emerald-500" style={{ width: `${questions.length ? (solvedCount / questions.length) * 100 : 0}%` }} /></div>
            <button onClick={() => setActiveTab('questions')} className="w-full py-2 rounded-xl bg-indigo-600 hover:bg-indigo-500 text-white text-xs font-bold">Continue questions</button>
          </SectionCard>
          <SectionCard title="Bookmarks" icon={Bookmark}>
            <Stat label="Saved for later" value={bookmarkedCount} />
            <button onClick={() => { setProgressFilter('BOOKMARKED'); setActiveTab('questions'); }} className="w-full py-2 rounded-xl bg-slate-800 hover:bg-slate-700 text-white text-xs font-bold">Review bookmarks</button>
          </SectionCard>
          <SectionCard title="Roadmap checklist" icon={FileText}>
            <Stat label="Steps complete" value={`${roadmapDoneCount} / ${roadmap.length}`} />
            <div className="h-2 rounded-full bg-slate-800 overflow-hidden"><div className="h-full bg-indigo-500" style={{ width: `${roadmapPercent}%` }} /></div>
            {progressError && <p role="status" className="text-xs text-amber-300">{progressError}</p>}
            <button onClick={() => setActiveTab('roadmap')} className="w-full py-2 rounded-xl bg-slate-800 hover:bg-slate-700 text-white text-xs font-bold">Open roadmap</button>
          </SectionCard>
        </div>
      )}

    </div>
  );
}

function RoleSelector({ roles, selectedIndex, onChange }) {
  return (
    <label className="block">
      <span className="sr-only">Select job role</span>
      <span className="relative block">
        <select value={selectedIndex} onChange={(event) => onChange(Number(event.target.value))}
          className="w-full appearance-none px-3 py-2.5 pr-9 rounded-xl bg-slate-950 border border-slate-800 text-sm text-slate-100 focus:outline-none focus:ring-2 focus:ring-indigo-500/40">
          {roles.map((item, index) => <option key={item.title} value={index}>{item.title}</option>)}
        </select>
        <ChevronDown className="absolute right-3 top-3 w-4 h-4 text-slate-500 pointer-events-none" />
      </span>
    </label>
  );
}

function FocusCard({ title, value }) {
  return <div className="p-4 rounded-2xl bg-slate-950/60 border border-slate-800"><h4 className="text-xs font-bold text-indigo-300">{title}</h4><p className="text-xs text-slate-300 leading-relaxed mt-2">{value}</p></div>;
}

function Stat({ label, value }) {
  return <div className="flex items-center justify-between gap-3 text-xs text-slate-400"><span>{label}</span><strong className="text-white">{value}</strong></div>;
}

function Badge({ children, tone = 'slate' }) {
  const classes = tone === 'indigo' ? 'bg-indigo-500/10 text-indigo-300 border-indigo-500/25' : 'bg-slate-800 text-slate-300 border-slate-700';
  return <span className={`px-2 py-0.5 rounded-full border text-[10px] font-bold uppercase ${classes}`}>{children}</span>;
}

function SelectFilter({ label, value, onChange, options }) {
  return (
    <label className="flex items-center gap-2">
      <span className="text-[11px] text-slate-400 whitespace-nowrap">{label}</span>
      <select value={value} onChange={(event) => onChange(event.target.value)}
        className="min-w-0 flex-1 px-2.5 py-2 rounded-lg bg-slate-950 border border-slate-800 text-xs text-slate-200">
        {options.map((option) => <option key={option} value={option}>{option === 'ALL' ? `All ${label.toLowerCase()}` : option}</option>)}
      </select>
    </label>
  );
}

function QuestionCard({ question, index, revealed, onReveal, onBookmark, onSolve }) {
  const options = ['A', 'B', 'C', 'D'].map((letter) => [letter, question[`option${letter}`]]).filter(([, text]) => text);
  return (
    <article className="p-5 rounded-2xl bg-slate-900/90 border border-slate-800 space-y-3">
      <div className="flex items-start justify-between gap-3">
        <div className="space-y-2">
          <div className="flex flex-wrap items-center gap-2">
            <span className="text-xs font-bold text-indigo-400">Q{index + 1}</span>
            <Badge tone={question.difficulty === 'HARD' ? 'slate' : 'indigo'}>{question.difficulty}</Badge>
            <Badge>{question.category}</Badge>
            {question.topicName && <Badge>{question.topicName}</Badge>}
            <Badge>{question.questionType || 'Catalog question'}</Badge>
            {question.isSolved && <Badge tone="indigo">Solved</Badge>}
          </div>
          <h3 className="text-sm font-bold text-white">{question.title}</h3>
        </div>
        <div className="flex items-center gap-2 flex-shrink-0">
          <button onClick={onBookmark} aria-pressed={Boolean(question.isBookmarked)} aria-label={question.isBookmarked ? 'Remove bookmark' : 'Bookmark question'}
            className={`p-2 rounded-lg border ${question.isBookmarked ? 'text-amber-400 border-amber-500/30' : 'text-slate-400 border-slate-800 hover:text-slate-200'}`}>
            <Bookmark className="w-4 h-4" />
          </button>
          <button onClick={onSolve} aria-pressed={Boolean(question.isSolved)} aria-label={question.isSolved ? 'Mark question unsolved' : 'Mark question solved'}
            className={`p-2 rounded-lg border ${question.isSolved ? 'text-emerald-400 border-emerald-500/30' : 'text-slate-400 border-slate-800 hover:text-slate-200'}`}>
            <CheckCircle2 className="w-4 h-4" />
          </button>
        </div>
      </div>
      {question.description && <p className="text-xs text-slate-300 leading-relaxed whitespace-pre-line">{question.description}</p>}
      {options.length > 0 && <div className="grid sm:grid-cols-2 gap-2">{options.map(([letter, text]) => (
        <div key={letter} className={`p-2.5 rounded-xl text-xs border flex gap-2 ${revealed && question.correctOption === letter ? 'bg-emerald-500/10 border-emerald-500/40 text-emerald-200' : 'bg-slate-950/60 border-slate-800 text-slate-300'}`}>
          <strong>{letter}.</strong><span>{text}</span>
        </div>
      ))}</div>}
      {question.codeSnippet && <pre className="p-3 rounded-xl overflow-x-auto bg-slate-950 border border-slate-800 text-xs text-slate-300"><code>{question.codeSnippet}</code></pre>}
      <div className="pt-2 flex flex-wrap items-center justify-between gap-3 border-t border-slate-800">
        <button onClick={onReveal} aria-expanded={revealed} className="text-xs font-bold text-indigo-400 hover:text-indigo-300">
          {revealed ? 'Hide explanation' : 'Show explanation'}
        </button>
        {question.externalLink && <a href={question.externalLink} target="_blank" rel="noopener noreferrer" className="text-xs text-slate-400 hover:text-slate-200 inline-flex items-center gap-1">
          Open practice link <ExternalLink className="w-3.5 h-3.5" />
        </a>}
      </div>
      {revealed && <div className="p-4 rounded-xl bg-slate-950/80 border border-indigo-500/30 text-xs space-y-2">
        {question.correctOption && <p className="font-bold text-emerald-300">Correct answer: {question.correctOption}</p>}
        <p className="text-slate-300 whitespace-pre-line leading-relaxed">{question.explanation || question.hint || 'No explanation is available in this question record.'}</p>
        {question.constraints && <p className="text-slate-400"><strong>Constraints:</strong> {question.constraints}</p>}
        {question.sampleInput && <p className="text-slate-400"><strong>Sample input:</strong> {question.sampleInput}</p>}
        {question.sampleOutput && <p className="text-slate-400"><strong>Sample output:</strong> {question.sampleOutput}</p>}
      </div>}
    </article>
  );
}

function EmptyState({ title, detail, action }) {
  return <div className="py-12 text-center rounded-2xl bg-slate-900/60 border border-dashed border-slate-800">
    <HelpCircle className="w-9 h-9 text-slate-600 mx-auto mb-2" />
    <p className="text-sm font-semibold text-slate-300">{title}</p>
    <p className="text-xs text-slate-500 max-w-md mx-auto mt-1 mb-4">{detail}</p>
    {action}
  </div>;
}
