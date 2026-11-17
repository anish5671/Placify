import React, { useRef, useState } from 'react';
import { AlertCircle, CheckCircle2, Download, FileSearch, FileText, Lightbulb, LoaderCircle, ShieldCheck, Sparkles, Target, UploadCloud } from 'lucide-react';
import api from '../api/client';

const MAX_FILE_SIZE = 10 * 1024 * 1024;
const allowedTypes = ['application/pdf', 'application/vnd.openxmlformats-officedocument.wordprocessingml.document', 'application/octet-stream'];

const listValue = (value) => Array.isArray(value) ? value : [];
const scoreFrom = (result) => result?.deterministicScore?.overallScore ?? 0;

function ScoreBar({ label, value }) {
  const score = Math.max(0, Math.min(100, Number(value) || 0));
  return (
    <div className="space-y-1.5">
      <div className="flex justify-between text-xs">
        <span className="text-slate-300">{label}</span>
        <span className="font-bold text-indigo-300">{score}/100</span>
      </div>
      <div className="h-2 overflow-hidden rounded-full bg-slate-800">
        <div className="h-full rounded-full bg-gradient-to-r from-indigo-500 to-violet-500 transition-all" style={{ width: `${score}%` }} />
      </div>
    </div>
  );
}

function InsightList({ title, items, icon: Icon, tone = 'indigo' }) {
  const values = listValue(items);
  const iconTone = tone === 'emerald' ? 'text-emerald-400' : tone === 'amber' ? 'text-amber-400' : 'text-indigo-400';
  return (
    <section className="rounded-3xl border border-slate-800 bg-slate-900/90 p-6">
      <h3 className="mb-3 flex items-center gap-2 text-sm font-bold text-white">
        <Icon className={`h-4 w-4 ${iconTone}`} />
        {title}
      </h3>
      {values.length ? (
        <ul className="space-y-2 text-xs leading-relaxed text-slate-300">
          {values.map((item, index) => <li key={`${title}-${index}`} className="flex gap-2"><span className="text-indigo-400">•</span><span>{item}</span></li>)}
        </ul>
      ) : <p className="text-xs text-slate-500">No items were returned for this section.</p>}
    </section>
  );
}

export default function ResumeAnalyzer() {
  const inputRef = useRef(null);
  const [file, setFile] = useState(null);
  const [jobDescription, setJobDescription] = useState('');
  const [result, setResult] = useState(null);
  const [dragging, setDragging] = useState(false);
  const [uploadProgress, setUploadProgress] = useState(0);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  const chooseFile = (candidate) => {
    setError('');
    setSuccess('');
    if (!candidate) return;
    const extension = candidate.name.split('.').pop()?.toLowerCase();
    if (!['pdf', 'docx'].includes(extension) || !allowedTypes.includes(candidate.type)) {
      setError('Choose a valid PDF or DOCX resume.');
      return;
    }
    if (candidate.size > MAX_FILE_SIZE) {
      setError('The resume must be smaller than 10 MB.');
      return;
    }
    setFile(candidate);
    setResult(null);
  };

  const analyze = async (event) => {
    event.preventDefault();
    if (!file) {
      setError('Upload a resume before starting analysis.');
      return;
    }
    setLoading(true);
    setError('');
    setSuccess('');
    setUploadProgress(0);
    const formData = new FormData();
    formData.append('file', file);
    if (jobDescription.trim()) formData.append('jobDescription', jobDescription.trim());
    try {
      const response = await api.post('/resume/analyze', formData, {
        headers: { 'Content-Type': 'multipart/form-data' },
        onUploadProgress: (progressEvent) => {
          if (progressEvent.total) setUploadProgress(Math.round((progressEvent.loaded * 100) / progressEvent.total));
        },
      });
      if (!response.data.success) throw new Error(response.data.message || 'Analysis failed');
      setResult(response.data.data);
      setSuccess('Resume analyzed successfully. Your uploaded document was not stored.');
    } catch (requestError) {
      setError(requestError.response?.data?.message || requestError.message || 'Could not analyze this resume.');
    } finally {
      setLoading(false);
    }
  };

  const downloadReport = async () => {
    if (!result?.analysisId) {
      setError('This analysis did not return a downloadable report id.');
      return;
    }
    try {
      const response = await api.get(`/resume/${result.analysisId}/report`, { responseType: 'blob' });
      const url = URL.createObjectURL(response.data);
      const link = document.createElement('a');
      link.href = url;
      link.download = 'resume-analysis.pdf';
      link.click();
      URL.revokeObjectURL(url);
    } catch {
      setError('Could not download the report.');
    }
  };

  const skills = result?.skills || {};
  const jobMatch = result?.jobMatch;
  const sectionScores = result?.deterministicScore?.sectionScores || {};

  return (
    <div className="space-y-6 pb-12">
      <div className="relative overflow-hidden rounded-3xl border border-indigo-500/20 bg-gradient-to-r from-indigo-950/80 via-slate-900 to-violet-950/60 p-6 sm:p-8">
        <div className="relative z-10 max-w-3xl space-y-2">
          <div className="inline-flex items-center gap-2 rounded-full border border-indigo-500/20 bg-indigo-500/10 px-3 py-1 text-xs font-semibold text-indigo-300"><Sparkles className="h-3.5 w-3.5" /> AI-powered career feedback</div>
          <h1 className="text-2xl font-extrabold tracking-tight text-white sm:text-3xl">Resume Analyzer</h1>
          <p className="text-sm leading-relaxed text-slate-300">Upload a resume to receive evidence-based scoring, ATS guidance, skills analysis, and optional job matching. Gemini never receives invented profile data.</p>
        </div>
        <FileSearch className="absolute -bottom-8 right-8 h-44 w-44 text-indigo-400/10" />
      </div>

      <form onSubmit={analyze} className="grid gap-6 lg:grid-cols-2">
        <div className="rounded-3xl border border-slate-800 bg-slate-900/90 p-6">
          <h2 className="mb-1 text-base font-bold text-white">Upload your resume</h2>
          <p className="mb-4 text-xs text-slate-400">PDF or DOCX, up to 10 MB. Files are validated and processed in memory.</p>
          <div
            onDragOver={(event) => { event.preventDefault(); setDragging(true); }}
            onDragLeave={() => setDragging(false)}
            onDrop={(event) => { event.preventDefault(); setDragging(false); chooseFile(event.dataTransfer.files[0]); }}
            onClick={() => inputRef.current?.click()}
            className={`cursor-pointer rounded-2xl border border-dashed p-8 text-center transition-colors ${dragging ? 'border-indigo-400 bg-indigo-500/10' : 'border-slate-700 bg-slate-950/60 hover:border-indigo-500/60'}`}
          >
            <input ref={inputRef} type="file" accept=".pdf,.docx,application/pdf,application/vnd.openxmlformats-officedocument.wordprocessingml.document" className="hidden" onChange={(event) => chooseFile(event.target.files[0])} />
            {file ? <FileText className="mx-auto mb-3 h-10 w-10 text-emerald-400" /> : <UploadCloud className="mx-auto mb-3 h-10 w-10 text-indigo-400" />}
            <p className="text-sm font-bold text-white">{file ? file.name : 'Drop your resume here'}</p>
            <p className="mt-1 text-xs text-slate-500">{file ? `${(file.size / 1024 / 1024).toFixed(2)} MB` : 'or click to browse files'}</p>
          </div>
          {loading && <div className="mt-4 space-y-1.5"><div className="flex justify-between text-xs text-slate-400"><span>Uploading and analyzing...</span><span>{uploadProgress}%</span></div><div className="h-2 overflow-hidden rounded-full bg-slate-800"><div className="h-full rounded-full bg-indigo-500 transition-all" style={{ width: `${Math.max(uploadProgress, 5)}%` }} /></div></div>}
          <button disabled={loading || !file} className="mt-4 flex w-full items-center justify-center gap-2 rounded-xl bg-indigo-600 px-4 py-3 text-sm font-bold text-white shadow-lg shadow-indigo-600/20 transition hover:bg-indigo-500 disabled:cursor-not-allowed disabled:opacity-50">
            {loading ? <LoaderCircle className="h-4 w-4 animate-spin" /> : <ShieldCheck className="h-4 w-4" />} {loading ? 'Analyzing securely...' : 'Analyze Resume'}
          </button>
        </div>

        <div className="rounded-3xl border border-slate-800 bg-slate-900/90 p-6">
          <h2 className="mb-1 text-base font-bold text-white">Optional job description</h2>
          <p className="mb-4 text-xs text-slate-400">Paste a role description to compare only skills actually evidenced in your resume.</p>
          <textarea value={jobDescription} onChange={(event) => setJobDescription(event.target.value)} rows={10} maxLength={12000} placeholder="Paste the job description here..." className="w-full resize-none rounded-2xl border border-slate-800 bg-slate-950/70 p-4 text-xs leading-relaxed text-white outline-none focus:border-indigo-500" />
          <p className="mt-2 text-right text-[11px] text-slate-500">{jobDescription.length}/12000</p>
        </div>
      </form>

      {error && <div className="flex items-center gap-2 rounded-2xl border border-rose-500/30 bg-rose-500/10 p-4 text-xs text-rose-300"><AlertCircle className="h-4 w-4 shrink-0" />{error}</div>}
      {success && <div className="flex items-center gap-2 rounded-2xl border border-emerald-500/30 bg-emerald-500/10 p-4 text-xs text-emerald-300"><CheckCircle2 className="h-4 w-4 shrink-0" />{success}</div>}

      {!result && !loading && <div className="rounded-3xl border border-dashed border-slate-800 bg-slate-950/40 py-14 text-center"><Target className="mx-auto mb-3 h-10 w-10 text-slate-700" /><p className="text-sm font-semibold text-slate-400">Your analysis dashboard will appear here</p><p className="mt-1 text-xs text-slate-600">Upload a resume to get started.</p></div>}

      {result && <div className="space-y-6">
        <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-4">
          <div className="rounded-3xl border border-indigo-500/30 bg-gradient-to-br from-indigo-950/80 to-slate-900 p-6"><p className="text-xs font-semibold uppercase tracking-wider text-indigo-300">Overall score</p><p className="mt-2 text-5xl font-black text-white">{scoreFrom(result)}<span className="text-xl text-slate-500">/100</span></p><p className="mt-2 text-xs text-slate-400">Estimated, not an employer guarantee</p></div>
          <div className="rounded-3xl border border-slate-800 bg-slate-900/90 p-6"><p className="text-xs font-semibold uppercase tracking-wider text-slate-400">ATS compatibility</p><p className="mt-2 text-4xl font-black text-white">{result.atsAnalysis?.score ?? Math.round(((sectionScores.atsCompatibility || 0) * 100) / 12)}<span className="text-xl text-slate-500">/100</span></p><p className="mt-2 text-xs text-slate-500">Based on headings and parsing risks</p></div>
          <div className="rounded-3xl border border-slate-800 bg-slate-900/90 p-6"><p className="text-xs font-semibold uppercase tracking-wider text-slate-400">Skills found</p><p className="mt-2 text-4xl font-black text-white">{Object.values(skills).flat().length}</p><p className="mt-2 text-xs text-slate-500">Grouped from resume evidence</p></div>
          <div className="rounded-3xl border border-slate-800 bg-slate-900/90 p-6"><p className="text-xs font-semibold uppercase tracking-wider text-slate-400">Job match</p><p className="mt-2 text-4xl font-black text-white">{jobMatch ? `${jobMatch.matchPercentage}%` : '—'}</p><p className="mt-2 text-xs text-slate-500">{jobMatch ? 'Against the pasted description' : 'No job description added'}</p></div>
        </div>

        <div className="grid gap-6 lg:grid-cols-2">
          <section className="rounded-3xl border border-slate-800 bg-slate-900/90 p-6"><h2 className="mb-5 text-base font-bold text-white">Score breakdown</h2><div className="space-y-4">{Object.entries(sectionScores).map(([label, value]) => <ScoreBar key={label} label={label.replace(/([A-Z])/g, ' $1')} value={value * (value <= 20 ? 5 : 1)} />)}</div><p className="mt-5 text-[11px] leading-relaxed text-slate-500">{result.deterministicScore?.methodology}</p></section>
          <section className="rounded-3xl border border-slate-800 bg-slate-900/90 p-6"><h2 className="mb-4 text-base font-bold text-white">Skills breakdown</h2><div className="grid gap-3 sm:grid-cols-2">{Object.entries(skills).map(([category, values]) => <div key={category} className="rounded-2xl bg-slate-950/60 p-3"><p className="mb-2 text-xs font-bold capitalize text-indigo-300">{category.replace(/([A-Z])/g, ' $1')}</p><div className="flex flex-wrap gap-1.5">{listValue(values).map((skill) => <span key={skill} className="rounded-lg border border-slate-700 bg-slate-800 px-2 py-1 text-[11px] text-slate-300">{skill}</span>)}</div></div>)}</div></section>
        </div>

        {jobMatch && <section className="rounded-3xl border border-indigo-500/20 bg-indigo-950/20 p-6"><h2 className="mb-4 flex items-center gap-2 text-base font-bold text-white"><Target className="h-4 w-4 text-indigo-400" /> Job description match</h2><div className="grid gap-4 md:grid-cols-3"><div><p className="mb-2 text-xs font-semibold text-emerald-300">Matching skills</p><div className="flex flex-wrap gap-1.5">{listValue(jobMatch.matchingSkills).map((skill) => <span key={skill} className="rounded-lg bg-emerald-500/10 px-2 py-1 text-[11px] text-emerald-300">{skill}</span>)}</div></div><div><p className="mb-2 text-xs font-semibold text-rose-300">Missing skills</p><div className="flex flex-wrap gap-1.5">{listValue(jobMatch.missingSkills).map((skill) => <span key={skill} className="rounded-lg bg-rose-500/10 px-2 py-1 text-[11px] text-rose-300">{skill}</span>)}</div></div><div><p className="mb-2 text-xs font-semibold text-slate-300">Recommendations</p><ul className="space-y-1 text-[11px] text-slate-400">{listValue(jobMatch.recommendations).map((item) => <li key={item}>• {item}</li>)}</ul></div></div></section>}

        <div className="grid gap-6 lg:grid-cols-2"><InsightList title="Resume strengths" items={result.strengths} icon={CheckCircle2} tone="emerald" /><InsightList title="Improvement recommendations" items={[...listValue(result.weaknesses), ...listValue(result.improvementSuggestions), ...listValue(result.experienceSuggestions)]} icon={Lightbulb} tone="amber" /></div>
        <div className="flex flex-wrap justify-end gap-3"><button onClick={downloadReport} className="inline-flex items-center gap-2 rounded-xl border border-slate-700 bg-slate-800 px-4 py-2.5 text-xs font-bold text-slate-200 hover:bg-slate-700"><Download className="h-4 w-4" /> Download PDF report</button></div>
      </div>}
    </div>
  );
}
