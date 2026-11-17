import React, { useCallback, useEffect, useMemo, useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { ArrowLeft, CheckCircle2, Clock3, Copy, LoaderCircle, Play, Send, Swords, Users } from 'lucide-react';
import api from '../api/client';

const STARTERS = {
  Python: `import sys

def solve():
    data = sys.stdin.read().split()
    # Parse the input format and print the required output.

if __name__ == "__main__":
    solve()
`,
  Java: `import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        // Parse the input format and print the required output.
    }
}
`,
  'C++': `#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    // Parse the input format and print the required output.
    return 0;
}
`,
};

function getApiError(error) {
  return error?.response?.data?.message || error?.message || 'Something went wrong. Please try again.';
}

function formatTime(seconds) {
  const safe = Math.max(0, seconds);
  return `${String(Math.floor(safe / 60)).padStart(2, '0')}:${String(safe % 60).padStart(2, '0')}`;
}

function Example({ example }) {
  return (
    <div className="rounded-xl border border-slate-800 bg-slate-950/70 p-3">
      <h3 className="mb-2 text-xs font-bold uppercase tracking-wider text-indigo-300">Example {example.number}</h3>
      <div className="grid gap-3 sm:grid-cols-2">
        <div>
          <div className="mb-1 text-[11px] uppercase text-slate-500">Input</div>
          <pre className="overflow-x-auto whitespace-pre-wrap rounded-lg bg-slate-900 p-2 text-xs text-slate-200">{example.input || '(empty input)'}</pre>
        </div>
        <div>
          <div className="mb-1 text-[11px] uppercase text-slate-500">Output</div>
          <pre className="overflow-x-auto whitespace-pre-wrap rounded-lg bg-slate-900 p-2 text-xs text-emerald-300">{example.output || '(empty output)'}</pre>
        </div>
      </div>
    </div>
  );
}

export default function CodingBattle() {
  const { code: routeCode } = useParams();
  const navigate = useNavigate();
  const [problems, setProblems] = useState([]);
  const [selectedProblem, setSelectedProblem] = useState('');
  const [duration, setDuration] = useState(30);
  const [joinCode, setJoinCode] = useState('');
  const [room, setRoom] = useState(null);
  const [language, setLanguage] = useState('Python');
  const [sourceByLanguage, setSourceByLanguage] = useState(STARTERS);
  const [execution, setExecution] = useState(null);
  const [busy, setBusy] = useState('');
  const [error, setError] = useState('');
  const [clock, setClock] = useState(Date.now());
  const [serverClockOffset, setServerClockOffset] = useState(0);
  const [copyLabel, setCopyLabel] = useState('Copy invite link');

  const activeCode = routeCode?.toUpperCase();
  const sourceCode = sourceByLanguage[language];
  const participantCount = Number(Boolean(room?.host)) + Number(Boolean(room?.opponent));
  const secondsLeft = useMemo(() => {
    if (!room?.endsAt) return null;
    return Math.ceil((new Date(room.endsAt).getTime() - (clock + serverClockOffset)) / 1000);
  }, [room, clock, serverClockOffset]);

  const loadRoom = useCallback(async (silent = false) => {
    if (!activeCode) return;
    if (!silent) setError('');
    try {
      const response = await api.get(`/battles/rooms/${activeCode}`);
      setRoom(response.data.data);
      setServerClockOffset(new Date(response.data.data.serverNow).getTime() - Date.now());
    } catch (requestError) {
      if (!silent) setError(getApiError(requestError));
    }
  }, [activeCode]);

  useEffect(() => {
    api.get('/battles/problems')
      .then(({ data }) => {
        const items = data.data || [];
        setProblems(items);
        if (items.length) setSelectedProblem(String(items[0].id));
      })
      .catch((requestError) => setError(getApiError(requestError)));
  }, []);

  useEffect(() => {
    if (!activeCode) {
      setRoom(null);
      setExecution(null);
      return undefined;
    }
    loadRoom();
    const timer = window.setInterval(() => loadRoom(true), 2500);
    return () => window.clearInterval(timer);
  }, [activeCode, loadRoom, room?.status]);

  useEffect(() => {
    const timer = window.setInterval(() => setClock(Date.now()), 500);
    return () => window.clearInterval(timer);
  }, []);

  const createRoom = async (event) => {
    event.preventDefault();
    setBusy('create');
    setError('');
    try {
      const { data } = await api.post('/battles/rooms', {
        problemId: Number(selectedProblem),
        durationMinutes: Number(duration),
      });
      navigate(`/battle/${data.data.code}`);
    } catch (requestError) {
      setError(getApiError(requestError));
    } finally {
      setBusy('');
    }
  };

  const joinRoom = async (event) => {
    event.preventDefault();
    setBusy('join');
    setError('');
    try {
      const { data } = await api.post('/battles/rooms/join', { code: joinCode.trim().toUpperCase() });
      navigate(`/battle/${data.data.code}`);
    } catch (requestError) {
      setError(getApiError(requestError));
    } finally {
      setBusy('');
    }
  };

  const executeCode = async (action) => {
    setBusy(action);
    setError('');
    setExecution(null);
    try {
      const { data } = await api.post(`/battles/rooms/${activeCode}/${action.toLowerCase()}`, {
        sourceCode,
        language: language === 'Python' ? 'python' : language === 'C++' ? 'cpp' : 'java',
      });
      setExecution(data.data);
      if (data.data.room) setRoom(data.data.room);
    } catch (requestError) {
      setError(getApiError(requestError));
    } finally {
      setBusy('');
    }
  };

  const copyInvite = async () => {
    await navigator.clipboard.writeText(`${window.location.origin}/battle/${room.code}`);
    setCopyLabel('Invite link copied');
    window.setTimeout(() => setCopyLabel('Copy invite link'), 1800);
  };

  const updateSource = (value) => setSourceByLanguage((current) => ({ ...current, [language]: value }));

  if (!activeCode) {
    return (
      <div className="mx-auto max-w-5xl space-y-6">
        <div className="flex items-center gap-3">
          <div className="rounded-xl bg-indigo-500/15 p-3 text-indigo-300"><Swords className="h-6 w-6" /></div>
          <div>
            <p className="text-sm font-semibold text-indigo-300">1v1 coding arena</p>
            <h1 className="text-2xl font-black text-white sm:text-3xl">Coding Battle</h1>
          </div>
        </div>
        <p className="max-w-3xl text-sm leading-6 text-slate-400">
          Create a private room, choose a problem and time limit, then share the invite link or room code with a friend.
          Both players receive the same prompt and server-timed match.
        </p>
        {error && <div className="rounded-xl border border-rose-500/30 bg-rose-500/10 p-3 text-sm text-rose-200">{error}</div>}
        <div className="grid gap-5 lg:grid-cols-2">
          <form onSubmit={createRoom} className="space-y-5 rounded-2xl border border-slate-800 bg-slate-900/80 p-5 sm:p-6">
            <div>
              <h2 className="text-lg font-bold text-white">Create a room</h2>
              <p className="mt-1 text-sm text-slate-400">You will be the host. The clock starts when your opponent joins.</p>
            </div>
            <label className="block text-sm font-medium text-slate-300">
              Problem
              <select value={selectedProblem} onChange={(event) => setSelectedProblem(event.target.value)} required
                className="mt-2 w-full rounded-xl border border-slate-700 bg-slate-950 px-3 py-3 text-white outline-none focus:border-indigo-500">
                {problems.map((problem) => <option key={problem.id} value={problem.id}>{problem.title} · {problem.difficulty}</option>)}
              </select>
            </label>
            <label className="block text-sm font-medium text-slate-300">
              Time limit
              <div className="mt-2 flex items-center gap-3">
                <input type="number" min="5" max="120" step="5" value={duration}
                  onChange={(event) => setDuration(event.target.value)}
                  className="w-28 rounded-xl border border-slate-700 bg-slate-950 px-3 py-3 text-white outline-none focus:border-indigo-500" />
                <span className="text-slate-400">minutes (5–120)</span>
              </div>
            </label>
            <button disabled={busy !== '' || !problems.length}
              className="flex w-full items-center justify-center gap-2 rounded-xl bg-indigo-600 px-4 py-3 font-bold text-white transition hover:bg-indigo-500 disabled:cursor-not-allowed disabled:opacity-50">
              {busy === 'create' ? <LoaderCircle className="h-4 w-4 animate-spin" /> : <Swords className="h-4 w-4" />}
              Create private battle
            </button>
          </form>

          <form onSubmit={joinRoom} className="space-y-5 rounded-2xl border border-slate-800 bg-slate-900/80 p-5 sm:p-6">
            <div>
              <h2 className="text-lg font-bold text-white">Join a friend</h2>
              <p className="mt-1 text-sm text-slate-400">Enter the 8-character room code shared by the host.</p>
            </div>
            <label className="block text-sm font-medium text-slate-300">
              Room code
              <input value={joinCode} onChange={(event) => setJoinCode(event.target.value.toUpperCase())}
                minLength={6} maxLength={10} required autoComplete="off" placeholder="E.G. 4K7M9Q2R"
                className="mt-2 w-full rounded-xl border border-slate-700 bg-slate-950 px-3 py-3 font-mono uppercase tracking-[0.2em] text-white outline-none placeholder:text-slate-600 focus:border-indigo-500" />
            </label>
            <button disabled={busy !== '' || joinCode.trim().length < 6}
              className="flex w-full items-center justify-center gap-2 rounded-xl border border-slate-700 bg-slate-800 px-4 py-3 font-bold text-white transition hover:bg-slate-700 disabled:cursor-not-allowed disabled:opacity-50">
              {busy === 'join' ? <LoaderCircle className="h-4 w-4 animate-spin" /> : <Users className="h-4 w-4" />}
              Join battle
            </button>
            <div className="rounded-xl border border-amber-500/20 bg-amber-500/5 p-3 text-xs leading-5 text-amber-100/80">
              Code is submitted to the external Judge0 CE execution service for sandboxed evaluation. Its free public tier may rate-limit requests.
            </div>
          </form>
        </div>
      </div>
    );
  }

  if (!room) {
    return (
      <div className="mx-auto max-w-3xl space-y-4">
        <Link to="/battle" className="inline-flex items-center gap-2 text-sm text-indigo-300 hover:text-indigo-200"><ArrowLeft className="h-4 w-4" /> Back to battles</Link>
        {error && <div className="rounded-xl border border-rose-500/30 bg-rose-500/10 p-4 text-sm text-rose-200">{error}</div>}
        <div className="flex items-center gap-3 rounded-2xl border border-slate-800 bg-slate-900 p-5 text-slate-300">
          <LoaderCircle className="h-5 w-5 animate-spin text-indigo-300" /> Loading battle room…
        </div>
      </div>
    );
  }

  const terminal = ['FINISHED', 'EXPIRED'].includes(room.status);
  const winnerMessage = room.status === 'FINISHED'
    ? (room.winner?.id === room.host?.id ? `${room.host.fullName || room.host.username} won the battle!`
      : `${room.opponent?.fullName || room.opponent?.username} won the battle!`)
    : room.status === 'EXPIRED' ? 'Time is up. This battle has ended.' : '';

  return (
    <div className="mx-auto max-w-[1500px] space-y-4">
      <div className="flex flex-col gap-3 rounded-2xl border border-slate-800 bg-slate-900/90 p-4 sm:flex-row sm:items-center sm:justify-between">
        <div className="flex flex-wrap items-center gap-3">
          <Link to="/battle" className="rounded-lg p-2 text-slate-400 hover:bg-slate-800 hover:text-white" aria-label="Back to battles"><ArrowLeft className="h-4 w-4" /></Link>
          <div>
            <div className="text-xs font-semibold uppercase tracking-wider text-indigo-300">Room {room.code}</div>
            <div className="text-lg font-bold text-white">{room.problem.title}</div>
          </div>
          {room.currentUserRole === 'HOST' && room.status === 'WAITING' && (
            <button onClick={copyInvite} className="inline-flex items-center gap-2 rounded-lg border border-slate-700 px-3 py-2 text-xs font-semibold text-slate-300 hover:bg-slate-800">
              <Copy className="h-3.5 w-3.5" /> {copyLabel}
            </button>
          )}
        </div>
        <div className="flex flex-wrap items-center gap-3">
          <div className="flex items-center gap-2 rounded-xl bg-slate-950 px-3 py-2 text-sm text-slate-300">
            <Users className="h-4 w-4 text-indigo-300" /> {room.host.username} <span className="text-slate-600">vs</span> {room.opponent?.username || 'Waiting…'}
          </div>
          {room.status === 'ACTIVE' && secondsLeft !== null && (
            <div className={`flex items-center gap-2 rounded-xl px-3 py-2 font-mono text-lg font-bold ${secondsLeft <= 60 ? 'bg-rose-500/15 text-rose-300' : 'bg-slate-950 text-white'}`}>
              <Clock3 className="h-4 w-4" /> {formatTime(secondsLeft)}
            </div>
          )}
          <span className={`rounded-full px-3 py-1.5 text-xs font-bold ${room.status === 'ACTIVE' ? 'bg-emerald-500/10 text-emerald-300' : room.status === 'WAITING' ? 'bg-amber-500/10 text-amber-300' : 'bg-slate-700 text-slate-300'}`}>
            {room.status === 'WAITING' ? 'Waiting for opponent' : room.status}
          </span>
        </div>
      </div>

      {room.status === 'WAITING' && room.currentUserRole === 'HOST' && (
        <div className="rounded-2xl border border-amber-500/20 bg-amber-500/5 p-4 text-sm text-amber-100/90">
          Share room code <strong className="font-mono tracking-widest text-white">{room.code}</strong> with your friend. The shared server timer starts as soon as they join.
        </div>
      )}
      {room.status === 'WAITING' && room.currentUserRole === 'OPPONENT' && (
        <div className="rounded-2xl border border-amber-500/20 bg-amber-500/5 p-4 text-sm text-amber-100/90">Waiting for the host to start the match by joining this room.</div>
      )}
      {terminal && (
        <div className={`rounded-2xl border p-4 text-sm font-semibold ${room.status === 'FINISHED' ? 'border-emerald-500/30 bg-emerald-500/10 text-emerald-200' : 'border-slate-700 bg-slate-800 text-slate-200'}`}>
          {room.status === 'FINISHED' && <CheckCircle2 className="mr-2 inline h-4 w-4" />}{winnerMessage}
        </div>
      )}
      {error && <div className="rounded-xl border border-rose-500/30 bg-rose-500/10 p-3 text-sm text-rose-200">{error}</div>}

      <div className="grid items-start gap-4 xl:grid-cols-[minmax(0,0.9fr)_minmax(0,1.1fr)]">
        <section className="space-y-4 rounded-2xl border border-slate-800 bg-slate-900/80 p-4 sm:p-5">
          <div className="flex items-center gap-2">
            <span className="rounded-full bg-indigo-500/15 px-2.5 py-1 text-xs font-bold text-indigo-300">{room.problem.difficulty}</span>
            <span className="text-xs text-slate-500">Problem #{room.problem.id}</span>
          </div>
          <p className="whitespace-pre-line text-sm leading-6 text-slate-200">{room.problem.description}</p>
          <div className="space-y-3">
            <div><h3 className="mb-1 text-xs font-bold uppercase tracking-wider text-slate-400">Input format</h3><p className="text-sm leading-5 text-slate-300">{room.problem.inputFormat}</p></div>
            <div><h3 className="mb-1 text-xs font-bold uppercase tracking-wider text-slate-400">Output format</h3><p className="text-sm leading-5 text-slate-300">{room.problem.outputFormat}</p></div>
            <div><h3 className="mb-1 text-xs font-bold uppercase tracking-wider text-slate-400">Constraints</h3><p className="whitespace-pre-line text-sm leading-5 text-slate-300">{room.problem.constraints}</p></div>
          </div>
          <div className="space-y-3">
            <h3 className="text-xs font-bold uppercase tracking-wider text-slate-400">Visible examples</h3>
            {room.problem.examples.map((example) => <Example key={example.number} example={example} />)}
          </div>
          <p className="text-xs leading-5 text-slate-500">Run checks the two visible examples. Submit checks private edge cases; hidden inputs and reference outputs are never sent to the browser.</p>
        </section>

        <section className="space-y-3">
          <div className="overflow-hidden rounded-2xl border border-slate-800 bg-[#0b1120]">
            <div className="flex flex-wrap items-center justify-between gap-3 border-b border-slate-800 px-3 py-3 sm:px-4">
              <label className="flex items-center gap-2 text-xs font-semibold uppercase tracking-wider text-slate-400">
                Language
                <select value={language} onChange={(event) => setLanguage(event.target.value)}
                  className="rounded-lg border border-slate-700 bg-slate-900 px-3 py-2 text-sm normal-case tracking-normal text-white outline-none focus:border-indigo-500">
                  {Object.keys(STARTERS).map((name) => <option key={name}>{name}</option>)}
                </select>
              </label>
              <div className="flex gap-2">
                <button onClick={() => executeCode('RUN')} disabled={busy !== '' || room.status !== 'ACTIVE'}
                  className="inline-flex items-center gap-2 rounded-lg border border-slate-700 px-3 py-2 text-sm font-bold text-slate-200 hover:bg-slate-800 disabled:opacity-40">
                  {busy === 'RUN' ? <LoaderCircle className="h-4 w-4 animate-spin" /> : <Play className="h-4 w-4" />} Run
                </button>
                <button onClick={() => executeCode('SUBMIT')} disabled={busy !== '' || room.status !== 'ACTIVE'}
                  className="inline-flex items-center gap-2 rounded-lg bg-indigo-600 px-3 py-2 text-sm font-bold text-white hover:bg-indigo-500 disabled:opacity-40">
                  {busy === 'SUBMIT' ? <LoaderCircle className="h-4 w-4 animate-spin" /> : <Send className="h-4 w-4" />} Submit
                </button>
              </div>
            </div>
            <textarea value={sourceCode} onChange={(event) => updateSource(event.target.value)} spellCheck="false"
              aria-label={`${language} code editor`} disabled={room.status !== 'ACTIVE'}
              className="min-h-[400px] w-full resize-y bg-transparent p-4 font-mono text-[13px] leading-6 text-slate-100 outline-none placeholder:text-slate-600 disabled:opacity-60 sm:min-h-[520px]"
              placeholder="Write your solution here…" />
          </div>

          {execution && (
            <div className="space-y-3 rounded-2xl border border-slate-800 bg-slate-900/80 p-4">
              <div className="flex flex-wrap items-center justify-between gap-2">
                <h2 className="font-bold text-white">{execution.action}: {execution.verdict}</h2>
                <span className={`rounded-full px-2.5 py-1 text-xs font-bold ${execution.accepted ? 'bg-emerald-500/10 text-emerald-300' : 'bg-rose-500/10 text-rose-300'}`}>
                  {execution.passedCount}/{execution.totalCount} passed
                </span>
              </div>
              <p className="text-sm text-slate-400">{execution.message}</p>
              {execution.results?.map((result) => (
                <div key={result.exampleNumber} className="rounded-xl border border-slate-800 bg-slate-950 p-3">
                  <div className={`mb-2 text-xs font-bold ${result.passed ? 'text-emerald-300' : 'text-rose-300'}`}>
                    Example {result.exampleNumber}: {result.verdict}
                  </div>
                  {!result.passed && <pre className="whitespace-pre-wrap break-words text-xs text-rose-200">Output: {result.actualOutput || '(empty)'}</pre>}
                  {!result.passed && <pre className="mt-1 whitespace-pre-wrap break-words text-xs text-slate-400">Expected: {result.expectedOutput || '(empty)'}</pre>}
                </div>
              ))}
            </div>
          )}
          <div className="rounded-xl border border-slate-800 bg-slate-900/50 px-4 py-3 text-xs leading-5 text-slate-500">
            Execution is sandboxed by Judge0 CE—not run by the Placify application server. Public Judge0 instances can be busy or rate-limited.
          </div>
          <div className="sr-only" aria-live="polite">{participantCount} players in room</div>
        </section>
      </div>
    </div>
  );
}
