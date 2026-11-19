import React, { useState, useEffect, useRef } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { 
  Clock, 
  ChevronLeft, 
  ChevronRight, 
  CheckCircle2, 
  Flag, 
  RotateCcw, 
  Send, 
  AlertTriangle,
  HelpCircle,
  X
} from 'lucide-react';
import api from '../api/client';

export default function TestExam() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [testData, setTestData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const [currentIndex, setCurrentIndex] = useState(0);

  // State maps by questionId:
  // answers: { [qId]: 'A' | 'B' | 'C' | 'D' }
  const [answers, setAnswers] = useState({});
  // markedForReview: { [qId]: boolean }
  const [markedForReview, setMarkedForReview] = useState({});
  // visited: { [qId]: boolean }
  const [visited, setVisited] = useState({});
  const [visibleHints, setVisibleHints] = useState({});

  // Timer in seconds
  const [timeLeft, setTimeLeft] = useState(1800); // 30 mins default
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [showConfirmModal, setShowConfirmModal] = useState(false);

  const startTimeRef = useRef(Date.now());
  const answersRef = useRef({});
  const markedForReviewRef = useRef({});
  const isSubmittingRef = useRef(false);

  useEffect(() => {
    fetchTest();
  }, [id]);

  const fetchTest = async () => {
    try {
      setLoading(true);
      const res = await api.get(`/tests/${id}/start`);
      if (res.data.success && res.data.data) {
        const data = res.data.data;
        setTestData(data);
        startTimeRef.current = Date.now();
        answersRef.current = {};
        markedForReviewRef.current = {};
        isSubmittingRef.current = false;
        setCurrentIndex(0);
        setAnswers({});
        setMarkedForReview({});
        setVisibleHints({});
        const durationSec = (data.durationMinutes || 30) * 60;
        setTimeLeft(durationSec);
        // Mark first question as visited
        if (data.questions && data.questions.length > 0) {
          setVisited({ [data.questions[0].questionId]: true });
        } else {
          setVisited({});
        }
      }
    } catch (err) {
      setError('Failed to start test. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  // Timer Countdown Effect
  useEffect(() => {
    if (loading || !testData) return;

    const timer = setInterval(() => {
      setTimeLeft((prev) => {
        if (prev <= 1) {
          clearInterval(timer);
          handleAutoSubmit();
          return 0;
        }
        return prev - 1;
      });
    }, 1000);

    return () => clearInterval(timer);
  }, [loading, testData]);

  const handleAutoSubmit = () => {
    submitExam();
  };

  const handleSelectOption = (qId, option) => {
    const updatedAnswers = { ...answersRef.current, [qId]: option };
    answersRef.current = updatedAnswers;
    setAnswers(updatedAnswers);
  };

  const handleClearOption = (qId) => {
    const updatedAnswers = { ...answersRef.current };
    delete updatedAnswers[qId];
    answersRef.current = updatedAnswers;
    setAnswers(updatedAnswers);
  };

  const handleToggleReview = (qId) => {
    const updatedMarked = { ...markedForReviewRef.current, [qId]: !markedForReviewRef.current[qId] };
    markedForReviewRef.current = updatedMarked;
    setMarkedForReview(updatedMarked);
  };

  const goToQuestion = (index) => {
    if (!testData?.questions) return;
    if (index >= 0 && index < testData.questions.length) {
      setCurrentIndex(index);
      const targetQId = testData.questions[index].questionId;
      setVisited(prev => ({ ...prev, [targetQId]: true }));
    }
  };

  const submitExam = async () => {
    if (isSubmittingRef.current || !testData) return;
    isSubmittingRef.current = true;
    setIsSubmitting(true);
    setShowConfirmModal(false);

    try {
      const timeSpentSeconds = Math.max(1, Math.round((Date.now() - startTimeRef.current) / 1000));
      
      const payloadAnswers = testData.questions.map(q => ({
        questionId: q.questionId,
        selectedOption: answersRef.current[q.questionId] || null,
        isMarkedForReview: !!markedForReviewRef.current[q.questionId],
      }));

      const res = await api.post(`/tests/${testData.testId}/submit`, {
        timeTakenSeconds: timeSpentSeconds,
        answers: payloadAnswers,
      });

      if (res.data.success && res.data.data) {
        const attemptId = res.data.data.attemptId;
        navigate(`/tests/${attemptId}/result`, { replace: true });
      }
    } catch (err) {
      console.error('Submission failed', err);
      alert('Failed to submit test. Please check network connection.');
      isSubmittingRef.current = false;
      setIsSubmitting(false);
    }
  };

  if (loading) {
    return (
      <div className="flex flex-col items-center justify-center min-h-[60vh] gap-3">
        <div className="w-10 h-10 border-4 border-indigo-500/20 border-t-indigo-500 rounded-full animate-spin"></div>
        <p className="text-sm text-slate-400">Setting up your secure exam room...</p>
      </div>
    );
  }

  if (error || !testData) {
    return (
      <div className="p-8 text-center text-slate-400">
        {error || 'Test not found'}
      </div>
    );
  }

  const currentQ = testData.questions[currentIndex];
  const qId = currentQ?.questionId;

  // Format Timer: MM:SS
  const minutes = Math.floor(timeLeft / 60);
  const seconds = timeLeft % 60;
  const isTimeCritical = timeLeft < 300; // < 5 mins

  // Question counts for palette
  let answeredCount = 0;
  let markedCount = 0;
  testData.questions.forEach(q => {
    if (answers[q.questionId]) answeredCount++;
    if (markedForReview[q.questionId]) markedCount++;
  });
  const unattemptedCount = testData.questions.length - answeredCount;

  return (
    <div className="space-y-4 max-w-7xl mx-auto pb-12">
      {/* Exam Sticky Top Bar */}
      <div className="sticky top-16 z-20 p-4 rounded-2xl bg-slate-900/95 backdrop-blur-md border border-slate-800 shadow-xl flex flex-wrap items-center justify-between gap-4">
        <div>
          <span className="text-[10px] uppercase font-bold text-indigo-400 tracking-wider">
            {testData.companyName} • {testData.category}
          </span>
          <h2 className="text-sm sm:text-base font-extrabold text-white leading-tight">
            {testData.title}
          </h2>
        </div>

        {/* Live Countdown Timer & Submit CTA */}
        <div className="flex items-center gap-3">
          <div className={`flex items-center gap-2 px-3.5 py-1.5 rounded-xl border font-mono text-sm font-bold shadow-inner ${
            isTimeCritical
              ? 'bg-rose-500/15 border-rose-500/40 text-rose-400 animate-pulse'
              : 'bg-slate-950 border-slate-800 text-indigo-300'
          }`}>
            <Clock className="w-4 h-4" />
            <span>
              {String(minutes).padStart(2, '0')}:{String(seconds).padStart(2, '0')}
            </span>
          </div>

          <button
            onClick={() => setShowConfirmModal(true)}
            className="px-4 py-2 rounded-xl bg-gradient-to-r from-emerald-600 to-teal-600 hover:from-emerald-500 hover:to-teal-500 text-white font-bold text-xs flex items-center gap-1.5 shadow-md shadow-emerald-600/20 transition-all hover:scale-[1.02]"
          >
            <Send className="w-3.5 h-3.5" />
            <span>Submit Exam</span>
          </button>
        </div>
      </div>

      {/* Main Exam Room Split Workspace */}
      <div className="grid grid-cols-1 lg:grid-cols-4 gap-6 items-start">
        {/* Left 3 Cols: Active Question Workspace */}
        <div className="lg:col-span-3 space-y-4">
          <div className="p-6 sm:p-8 rounded-3xl bg-slate-900 border border-slate-800 shadow-xl space-y-5 min-h-[500px] flex flex-col justify-between">
            <div>
              {/* Question metadata badge */}
              <div className="flex items-center justify-between border-b border-slate-800/80 pb-4 mb-4">
                <div className="flex items-center gap-2">
                  <span className="w-7 h-7 rounded-lg bg-indigo-600/20 border border-indigo-500/30 text-indigo-400 flex items-center justify-center font-extrabold text-xs">
                    {currentIndex + 1}
                  </span>
                  <span className="text-xs font-bold text-slate-300">
                    Question {currentIndex + 1} of {testData.questions.length}
                  </span>
                  <span className="text-[10px] uppercase font-bold px-2 py-0.5 rounded bg-slate-950 text-slate-400 border border-slate-800">
                    {currentQ.category}
                  </span>
                </div>

                <div className="text-xs font-semibold text-slate-400">
                  Marks: <span className="text-white font-bold">+{currentQ.marks}</span>
                </div>
              </div>

              {/* Problem Title & Description */}
              <div className="space-y-3">
                <h3 className="text-base sm:text-lg font-bold text-white leading-relaxed">
                  {currentQ.title}
                </h3>

                {currentQ.description && (
                  <p className="text-xs sm:text-sm text-slate-300 leading-relaxed bg-slate-950/60 p-4 rounded-2xl border border-slate-800/80">
                    {currentQ.description}
                  </p>
                )}

                {currentQ.codeSnippet && (
                  <pre className="p-4 rounded-2xl bg-slate-950 border border-slate-800 font-mono text-xs text-indigo-300 overflow-x-auto whitespace-pre leading-relaxed">
                    {currentQ.codeSnippet}
                  </pre>
                )}
              </div>

              {currentQ.hint && (
                <div className="pt-4">
                  <button
                    type="button"
                    onClick={() => setVisibleHints(prev => ({ ...prev, [qId]: !prev[qId] }))}
                    className="px-3.5 py-2 rounded-xl text-xs font-bold text-amber-300 bg-amber-500/10 border border-amber-500/30 hover:bg-amber-500/15 transition-colors"
                  >
                    {visibleHints[qId] ? 'Hide Hint' : 'Show Hint'}
                  </button>
                  {visibleHints[qId] && (
                    <div className="mt-3 p-4 rounded-2xl bg-amber-950/30 border border-amber-500/30 text-xs text-slate-200 leading-relaxed">
                      <span className="text-amber-300 font-extrabold uppercase tracking-wider text-[10px] block mb-1">Hint</span>
                      {currentQ.hint}
                    </div>
                  )}
                </div>
              )}

              {/* Options Selection */}
              <div className="space-y-2.5 pt-4">
                {['A', 'B', 'C', 'D'].map((opt) => {
                  const text = currentQ[`option${opt}`];
                  if (!text) return null;
                  const isSelected = answers[qId] === opt;

                  return (
                    <button
                      key={opt}
                      type="button"
                      onClick={() => handleSelectOption(qId, opt)}
                      className={`w-full p-4 rounded-2xl border text-left text-xs sm:text-sm transition-all flex items-start gap-3 ${
                        isSelected
                          ? 'bg-indigo-600/20 border-indigo-500 text-white font-semibold shadow-inner'
                          : 'bg-slate-950/60 border-slate-800 hover:border-slate-700 text-slate-300'
                      }`}
                    >
                      <span className={`w-6 h-6 rounded-lg flex items-center justify-center font-bold text-xs flex-shrink-0 transition-colors ${
                        isSelected ? 'bg-indigo-600 text-white' : 'bg-slate-800 text-slate-400'
                      }`}>
                        {opt}
                      </span>
                      <span className="flex-1 mt-0.5 leading-relaxed">{text}</span>
                    </button>
                  );
                })}
              </div>
            </div>

            {/* Bottom Actions Bar */}
            <div className="pt-6 border-t border-slate-800 flex flex-wrap items-center justify-between gap-3">
              <div className="flex items-center gap-2">
                <button
                  type="button"
                  onClick={() => handleToggleReview(qId)}
                  className={`px-3.5 py-2 rounded-xl text-xs font-bold transition-all flex items-center gap-1.5 ${
                    markedForReview[qId]
                      ? 'bg-amber-500/20 text-amber-300 border border-amber-500/40'
                      : 'bg-slate-950 text-slate-400 hover:text-slate-200 border border-slate-800'
                  }`}
                >
                  <Flag className="w-3.5 h-3.5" />
                  <span>{markedForReview[qId] ? 'Marked for Review' : 'Mark for Review'}</span>
                </button>

                {answers[qId] && (
                  <button
                    type="button"
                    onClick={() => handleClearOption(qId)}
                    className="px-3.5 py-2 rounded-xl text-xs font-semibold text-slate-400 hover:text-slate-200 bg-slate-950 border border-slate-800 flex items-center gap-1.5 transition-colors"
                  >
                    <RotateCcw className="w-3.5 h-3.5" />
                    <span>Clear Choice</span>
                  </button>
                )}
              </div>

              <div className="flex items-center gap-2">
                <button
                  type="button"
                  onClick={() => goToQuestion(currentIndex - 1)}
                  disabled={currentIndex === 0}
                  className="px-4 py-2 rounded-xl text-xs font-bold text-slate-300 bg-slate-800 hover:bg-slate-700 disabled:opacity-40 disabled:pointer-events-none flex items-center gap-1 transition-all"
                >
                  <ChevronLeft className="w-4 h-4" />
                  <span>Previous</span>
                </button>

                <button
                  type="button"
                  onClick={() => goToQuestion(currentIndex + 1)}
                  disabled={currentIndex === testData.questions.length - 1}
                  className="px-4 py-2 rounded-xl text-xs font-bold text-white bg-indigo-600 hover:bg-indigo-500 disabled:opacity-40 disabled:pointer-events-none flex items-center gap-1 transition-all"
                >
                  <span>Next</span>
                  <ChevronRight className="w-4 h-4" />
                </button>
              </div>
            </div>
          </div>
        </div>

        {/* Right 1 Col: Interactive Question Palette */}
        <div className="p-6 rounded-3xl bg-slate-900 border border-slate-800 shadow-xl space-y-5">
          <div>
            <h4 className="text-sm font-bold text-white mb-1">Question Palette</h4>
            <p className="text-[11px] text-slate-400">Click any question number to jump</p>
          </div>

          {/* Palette Color Legend */}
          <div className="grid grid-cols-2 gap-2 text-[10px] text-slate-300 p-3 rounded-2xl bg-slate-950 border border-slate-800/80">
            <div className="flex items-center gap-1.5">
              <span className="w-3 h-3 rounded-full bg-emerald-500" />
              <span>Answered ({answeredCount})</span>
            </div>
            <div className="flex items-center gap-1.5">
              <span className="w-3 h-3 rounded-full bg-amber-500" />
              <span>Review ({markedCount})</span>
            </div>
            <div className="flex items-center gap-1.5">
              <span className="w-3 h-3 rounded-full bg-indigo-500" />
              <span>Ans & Review</span>
            </div>
            <div className="flex items-center gap-1.5">
              <span className="w-3 h-3 rounded-full bg-slate-700" />
              <span>Unattempted ({unattemptedCount})</span>
            </div>
          </div>

          {/* Palette Buttons Grid */}
          <div className="grid grid-cols-4 sm:grid-cols-5 gap-2 max-h-[300px] overflow-y-auto pr-1">
            {testData.questions.map((q, idx) => {
              const qIdItem = q.questionId;
              const hasAns = !!answers[qIdItem];
              const isMarked = !!markedForReview[qIdItem];
              const isCurrent = idx === currentIndex;
              const isVis = !!visited[qIdItem];

              let btnColor = 'bg-slate-800 text-slate-400 border-slate-700'; // Not visited

              if (hasAns && isMarked) {
                btnColor = 'bg-indigo-600 text-white border-indigo-400';
              } else if (isMarked) {
                btnColor = 'bg-amber-500 text-white border-amber-300';
              } else if (hasAns) {
                btnColor = 'bg-emerald-500 text-white border-emerald-400';
              } else if (isVis) {
                btnColor = 'bg-slate-700 text-slate-200 border-slate-600';
              }

              return (
                <button
                  key={qIdItem}
                  type="button"
                  onClick={() => goToQuestion(idx)}
                  className={`w-9 h-9 rounded-xl font-black text-xs border transition-all flex items-center justify-center ${btnColor} ${
                    isCurrent ? 'ring-2 ring-white ring-offset-2 ring-offset-slate-900 scale-105' : 'hover:scale-105'
                  }`}
                >
                  {idx + 1}
                </button>
              );
            })}
          </div>

          <button
            onClick={() => setShowConfirmModal(true)}
            className="w-full py-3 rounded-2xl bg-emerald-600 hover:bg-emerald-500 text-white font-extrabold text-xs shadow-lg shadow-emerald-600/20 transition-all flex items-center justify-center gap-2"
          >
            <Send className="w-4 h-4" />
            <span>Finish & Submit Test</span>
          </button>
        </div>
      </div>

      {/* Confirmation Submit Modal */}
      {showConfirmModal && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-slate-950/80 backdrop-blur-md">
          <div className="bg-slate-900 border border-slate-800 rounded-3xl max-w-md w-full p-6 shadow-2xl space-y-4 animate-scaleIn">
            <div className="flex items-start justify-between">
              <div className="flex items-center gap-2.5">
                <div className="w-10 h-10 rounded-2xl bg-amber-500/10 border border-amber-500/20 text-amber-400 flex items-center justify-center">
                  <AlertTriangle className="w-5 h-5" />
                </div>
                <div>
                  <h3 className="text-base font-bold text-white">Submit Assessment?</h3>
                  <p className="text-xs text-slate-400">Review your attempt status below</p>
                </div>
              </div>
              <button
                onClick={() => setShowConfirmModal(false)}
                className="p-1 rounded-lg text-slate-400 hover:text-slate-200"
              >
                <X className="w-5 h-5" />
              </button>
            </div>

            {/* Attempt summary counts */}
            <div className="grid grid-cols-3 gap-2.5 p-3.5 rounded-2xl bg-slate-950 border border-slate-800 text-center">
              <div>
                <span className="text-[10px] text-slate-400 uppercase font-bold block">Answered</span>
                <span className="text-emerald-400 font-extrabold text-base">{answeredCount}</span>
              </div>
              <div>
                <span className="text-[10px] text-slate-400 uppercase font-bold block">Review</span>
                <span className="text-amber-400 font-extrabold text-base">{markedCount}</span>
              </div>
              <div>
                <span className="text-[10px] text-slate-400 uppercase font-bold block">Unattempted</span>
                <span className="text-rose-400 font-extrabold text-base">{unattemptedCount}</span>
              </div>
            </div>

            <p className="text-xs text-slate-300 leading-relaxed">
              Once submitted, your answers will be securely evaluated on the backend and your comprehensive analysis report will be generated.
            </p>

            <div className="flex items-center justify-end gap-3 pt-2">
              <button
                type="button"
                onClick={() => setShowConfirmModal(false)}
                className="px-4 py-2 rounded-xl text-xs font-semibold text-slate-400 hover:text-slate-200"
              >
                Continue Exam
              </button>
              <button
                type="button"
                onClick={submitExam}
                disabled={isSubmitting}
                className="px-5 py-2.5 rounded-xl bg-emerald-600 hover:bg-emerald-500 text-white font-bold text-xs shadow-md shadow-emerald-600/20 flex items-center gap-2"
              >
                {isSubmitting ? (
                  <div className="w-4 h-4 border-2 border-white/20 border-t-white rounded-full animate-spin" />
                ) : (
                  <>
                    <Send className="w-4 h-4" />
                    <span>Confirm & Submit</span>
                  </>
                )}
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
