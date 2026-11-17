import React, { useRef, useState } from 'react';
import {
  AlertCircle,
  CheckCircle2,
  FileSearch,
  FileText,
  LoaderCircle,
  ShieldCheck,
  Sparkles,
  Target,
  UploadCloud,
} from 'lucide-react';
import api from '../api/client';

const MAX_FILE_SIZE = 10 * 1024 * 1024;
const SCORE_LABELS = {
  skills: 'Skills section',
  experience: 'Experience section',
  projects: 'Projects section',
  education: 'Education section',
  contactInformation: 'Contact details',
  certifications: 'Certifications',
  contentDepth: 'Content depth',
};

export default function ResumeAnalyzer() {
  const fileInput = useRef(null);
  const [file, setFile] = useState(null);
  const [jobDescription, setJobDescription] = useState('');
  const [result, setResult] = useState(null);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const selectFile = (candidate) => {
    setError('');
    setResult(null);
    if (!candidate) return;
    const extension = candidate.name.split('.').pop()?.toLowerCase();
    if (!['pdf', 'docx'].includes(extension)) {
      setFile(null);
      setError('Choose a PDF or DOCX resume.');
      return;
    }
    if (candidate.size > MAX_FILE_SIZE) {
      setFile(null);
      setError('The resume must be 10 MB or smaller.');
      return;
    }
    setFile(candidate);
  };

  const analyze = async (event) => {
    event.preventDefault();
    if (!file) {
      setError('Choose your resume before starting the analysis.');
      return;
    }

    const formData = new FormData();
    formData.append('file', file);
    if (jobDescription.trim()) formData.append('jobDescription', jobDescription.trim());

    setLoading(true);
    setError('');
    setResult(null);
    try {
      const response = await api.post('/resume/analyze', formData);
      if (!response.data.success) {
        throw new Error(response.data.message || 'Resume analysis failed.');
      }
      setResult(response.data.data);
    } catch (requestError) {
      setError(requestError.response?.data?.message || requestError.message || 'Could not analyze this resume.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="space-y-6">
      <header className="relative overflow-hidden rounded-3xl border border-indigo-500/20 bg-gradient-to-r from-indigo-950 via-slate-900 to-violet-950 p-6 sm:p-8">
        <div className="relative z-10 max-w-3xl">
          <div className="mb-3 inline-flex items-center gap-2 rounded-full border border-indigo-400/20 bg-indigo-400/10 px-3 py-1 text-xs font-semibold text-indigo-200">
            <Sparkles className="h-3.5 w-3.5" />
            Resume feedback and role matching
          </div>
          <h1 className="text-2xl font-extrabold tracking-tight text-white sm:text-3xl">Resume Analyzer</h1>
          <p className="mt-2 text-sm leading-6 text-slate-300">
            Get a transparent checklist score, skill-based role suggestions, and optional Gemini feedback.
            Job matches are estimates, not hiring guarantees.
          </p>
        </div>
        <FileSearch className="absolute -bottom-5 right-6 h-36 w-36 text-white/5" />
      </header>

      <form onSubmit={analyze} className="grid gap-5 lg:grid-cols-2">
        <section className="rounded-2xl border border-slate-800 bg-slate-900 p-5 sm:p-6">
          <h2 className="flex items-center gap-2 font-bold text-white">
            <UploadCloud className="h-5 w-5 text-indigo-300" />
            Upload your resume
          </h2>
          <p className="mt-1 text-xs text-slate-400">PDF or DOCX, up to 10 MB. Scanned-image PDFs are not supported.</p>
          <input
            ref={fileInput}
            className="sr-only"
            type="file"
            accept=".pdf,.docx,application/pdf,application/vnd.openxmlformats-officedocument.wordprocessingml.document"
            onChange={(event) => selectFile(event.target.files?.[0])}
          />
          <button
            type="button"
            onClick={() => fileInput.current?.click()}
            className="mt-5 flex min-h-36 w-full flex-col items-center justify-center rounded-2xl border border-dashed border-slate-700 bg-slate-950/60 px-4 text-center hover:border-indigo-400/60"
          >
            <FileText className="mb-3 h-8 w-8 text-emerald-400" />
            <span className="max-w-full truncate text-sm font-bold text-white">
              {file?.name || 'Choose a PDF or DOCX file'}
            </span>
            <span className="mt-1 text-xs text-slate-500">
              {file ? `${(file.size / (1024 * 1024)).toFixed(2)} MB` : 'Your file is processed in memory'}
            </span>
          </button>
          <button
            type="submit"
            disabled={loading || !file}
            className="mt-4 flex w-full items-center justify-center gap-2 rounded-xl bg-indigo-600 px-4 py-3 text-sm font-bold text-white shadow-lg shadow-indigo-950/40 transition hover:bg-indigo-500 disabled:cursor-not-allowed disabled:opacity-50"
          >
            {loading ? <LoaderCircle className="h-4 w-4 animate-spin" /> : <ShieldCheck className="h-4 w-4" />}
            {loading ? 'Analyzing resume…' : 'Analyze Resume'}
          </button>
        </section>

        <section className="rounded-2xl border border-slate-800 bg-slate-900 p-5 sm:p-6">
          <label htmlFor="job-description" className="font-bold text-white">Optional job description</label>
          <p className="mt-1 text-xs text-slate-400">Compare resume evidence with skills mentioned in a specific role.</p>
          <textarea
            id="job-description"
            value={jobDescription}
            onChange={(event) => setJobDescription(event.target.value)}
            maxLength={12000}
            placeholder="Paste the job description here…"
            className="mt-4 min-h-52 w-full resize-y rounded-xl border border-slate-800 bg-slate-950/70 p-4 text-sm text-slate-200 placeholder:text-slate-600 focus:border-indigo-500 focus:outline-none focus:ring-2 focus:ring-indigo-500/20"
          />
          <div className="mt-1 text-right text-xs text-slate-500">{jobDescription.length}/12000</div>
        </section>
      </form>

      {error && (
        <div role="alert" className="flex items-start gap-2 rounded-xl border border-rose-500/30 bg-rose-500/10 p-4 text-sm text-rose-300">
          <AlertCircle className="mt-0.5 h-4 w-4 shrink-0" />
          <span>{error}</span>
        </div>
      )}

      {result && (
        <section className="space-y-5" aria-live="polite">
          <div className="grid gap-5 lg:grid-cols-[0.8fr_1.2fr]">
            <div className="rounded-2xl border border-slate-800 bg-slate-900 p-5 sm:p-6">
              <p className="text-sm font-semibold text-slate-300">Resume checklist score</p>
              <div className="mt-2 flex items-end gap-2">
                <span className="text-5xl font-black text-white">{result.overallScore}</span>
                <span className="pb-1 text-slate-500">/ 100</span>
              </div>
              <p className="mt-2 text-xs leading-5 text-slate-500">
                This score measures detected resume sections and content depth; it is not an ATS vendor score.
              </p>
              <div className="mt-5 space-y-3">
                {Object.entries(result.sectionScores || {}).map(([key, score]) => (
                  <div key={key}>
                    <div className="mb-1 flex justify-between text-xs">
                      <span className="text-slate-400">{SCORE_LABELS[key] || key}</span>
                      <span className="font-semibold text-slate-300">{score}</span>
                    </div>
                    <div className="h-1.5 overflow-hidden rounded-full bg-slate-800">
                      <div className="h-full rounded-full bg-indigo-500" style={{ width: `${score}%` }} />
                    </div>
                  </div>
                ))}
              </div>
              {result.missingSections?.length > 0 && (
                <p className="mt-4 text-xs leading-5 text-amber-300">
                  Sections not detected: {result.missingSections.join(', ')}
                </p>
              )}
            </div>

            <div className="rounded-2xl border border-slate-800 bg-slate-900 p-5 sm:p-6">
              <h2 className="flex items-center gap-2 font-bold text-white">
                <Target className="h-5 w-5 text-indigo-300" />
                Suggested job roles
              </h2>
              {result.detectedSkills?.length > 0 ? (
                <div className="mt-3 flex flex-wrap gap-2">
                  {result.detectedSkills.map((skill) => (
                    <span key={skill} className="rounded-full border border-slate-700 bg-slate-950 px-2.5 py-1 text-xs text-slate-300">{skill}</span>
                  ))}
                </div>
              ) : (
                <p className="mt-3 text-sm text-slate-400">No skills from the current matching catalog were detected. Add a clear skills section or relevant tools to your resume.</p>
              )}
              <div className="mt-5 space-y-3">
                {result.predictedRoles?.map((role) => (
                  <article key={role.role} className="rounded-xl border border-slate-800 bg-slate-950/60 p-4">
                    <div className="flex items-center justify-between gap-3">
                      <h3 className="font-semibold text-white">{role.role}</h3>
                      <span className="text-sm font-bold text-indigo-300">{role.matchPercentage}%</span>
                    </div>
                    <p className="mt-1 text-xs leading-5 text-slate-400">
                      Evidence match: {role.matchingSkills.join(', ')}
                      {role.missingSkills.length > 0 ? ` · Other common skills: ${role.missingSkills.join(', ')}` : ''}
                    </p>
                  </article>
                ))}
              </div>
            </div>
          </div>

          {result.jobDescriptionMatch && (
            <div className="rounded-2xl border border-slate-800 bg-slate-900 p-5 sm:p-6">
              <div className="flex flex-wrap items-center justify-between gap-3">
                <h2 className="font-bold text-white">Job description match</h2>
                <span className="rounded-full bg-indigo-500/15 px-3 py-1 text-sm font-bold text-indigo-200">
                  {result.jobDescriptionMatch.matchPercentage}%
                </span>
              </div>
              <p className="mt-2 text-sm text-slate-400">
                Matched: {result.jobDescriptionMatch.matchingSkills.join(', ') || 'No catalog skills detected'}
              </p>
              {result.jobDescriptionMatch.missingSkills.length > 0 && (
                <p className="mt-2 text-sm text-amber-300">
                  Not found in resume: {result.jobDescriptionMatch.missingSkills.join(', ')}
                </p>
              )}
              <ul className="mt-3 list-inside list-disc space-y-1 text-xs text-slate-400">
                {result.jobDescriptionMatch.recommendations.map((recommendation) => <li key={recommendation}>{recommendation}</li>)}
              </ul>
            </div>
          )}

          <div className="rounded-2xl border border-slate-800 bg-slate-900 p-5 sm:p-6">
            <h2 className="flex items-center gap-2 font-bold text-white">
              {result.aiFeedback?.status === 'AVAILABLE'
                ? <Sparkles className="h-5 w-5 text-amber-300" />
                : <AlertCircle className="h-5 w-5 text-amber-300" />}
              AI feedback
            </h2>
            {result.aiFeedback?.status === 'AVAILABLE' ? (
              <>
                <p className="mt-3 text-sm leading-6 text-slate-300">{result.aiFeedback.summary}</p>
                <div className="mt-4 grid gap-5 sm:grid-cols-2">
                  <FeedbackList title="Strengths" items={result.aiFeedback.strengths} />
                  <FeedbackList title="Suggestions" items={result.aiFeedback.improvementSuggestions} />
                </div>
                <p className="mt-4 text-xs text-slate-500">{result.aiFeedback.message}</p>
              </>
            ) : (
              <p className="mt-3 text-sm leading-6 text-amber-200">
                {result.aiFeedback?.message || 'AI feedback is currently unavailable. Local results are shown above.'}
              </p>
            )}
          </div>

          <div className="flex items-start gap-2 rounded-xl border border-emerald-500/20 bg-emerald-500/5 p-4 text-xs leading-5 text-slate-400">
            <CheckCircle2 className="mt-0.5 h-4 w-4 shrink-0 text-emerald-400" />
            <span>{result.privacyNotice}</span>
          </div>
        </section>
      )}
    </div>
  );
}

function FeedbackList({ title, items }) {
  return (
    <div>
      <h3 className="text-sm font-semibold text-slate-200">{title}</h3>
      {items?.length ? (
        <ul className="mt-2 list-inside list-disc space-y-1.5 text-sm leading-5 text-slate-400">
          {items.map((item, index) => <li key={`${title}-${index}`}>{item}</li>)}
        </ul>
      ) : <p className="mt-2 text-sm text-slate-500">No items returned.</p>}
    </div>
  );
}
