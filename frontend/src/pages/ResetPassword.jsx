import React, { useState } from 'react';
import { Link, useSearchParams } from 'react-router-dom';
import { ArrowLeft, Compass, Lock } from 'lucide-react';
import api from '../api/client';

export default function ResetPassword() {
  const [searchParams] = useSearchParams();
  const token = searchParams.get('token') || '';
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [error, setError] = useState('');
  const [reset, setReset] = useState(false);
  const [submitting, setSubmitting] = useState(false);

  const handleSubmit = async (event) => {
    event.preventDefault();
    setError('');
    if (password !== confirmPassword) {
      setError('Passwords do not match.');
      return;
    }
    setSubmitting(true);
    try {
      const response = await api.post('/auth/reset-password', { token, password });
      if (!response.data.success) {
        setError(response.data.message || 'Unable to reset your password.');
        return;
      }
      setReset(true);
    } catch (requestError) {
      setError(requestError.response?.data?.message || requestError.message || 'Unable to reset your password.');
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="min-h-screen bg-slate-950 flex flex-col justify-center py-12 px-4 sm:px-6 lg:px-8 relative overflow-hidden">
      <div className="absolute top-0 left-1/2 -translate-x-1/2 w-[600px] h-[350px] bg-gradient-to-tr from-indigo-600/15 via-violet-600/15 to-transparent blur-3xl pointer-events-none" />
      <div className="sm:mx-auto sm:w-full sm:max-w-md relative z-10 text-center">
        <div className="inline-flex items-center justify-center w-14 h-14 rounded-2xl bg-gradient-to-tr from-indigo-600 via-indigo-500 to-violet-500 shadow-xl shadow-indigo-600/25 mb-4 text-white">
          <Compass className="w-8 h-8 animate-spin-slow" />
        </div>
        <h1 className="text-3xl font-extrabold text-white tracking-tight">Choose a new password</h1>
        <p className="mt-2 text-sm text-slate-400">Your password must be at least 6 characters.</p>
      </div>

      <div className="mt-8 sm:mx-auto sm:w-full sm:max-w-md relative z-10">
        <div className="bg-slate-900/90 backdrop-blur-xl py-8 px-6 sm:px-10 rounded-3xl border border-slate-800 shadow-2xl">
          {reset ? (
            <div className="p-4 rounded-xl bg-emerald-500/10 border border-emerald-500/30 text-emerald-300 text-sm" role="status">
              Your password has been updated. You can now sign in with your new password.
            </div>
          ) : !token ? (
            <div className="p-4 rounded-xl bg-rose-500/10 border border-rose-500/30 text-rose-300 text-sm" role="alert">
              This password reset link is invalid. Request a new one to continue.
            </div>
          ) : (
            <>
              {error && (
                <div className="mb-4 p-3.5 rounded-xl bg-rose-500/10 border border-rose-500/30 text-rose-400 text-xs font-medium" role="alert">
                  {error}
                </div>
              )}
              <form className="space-y-4" onSubmit={handleSubmit}>
                <div>
                  <label htmlFor="new-password" className="block text-xs font-semibold text-slate-300 mb-1.5">New password</label>
                  <div className="relative">
                    <Lock className="w-4 h-4 text-slate-400 absolute left-3.5 top-3" />
                    <input
                      id="new-password"
                      type="password"
                      required
                      minLength={6}
                      maxLength={100}
                      value={password}
                      onChange={(event) => setPassword(event.target.value)}
                      autoComplete="new-password"
                      className="w-full pl-10 pr-4 py-2.5 rounded-xl bg-slate-950/80 border border-slate-800 text-sm text-slate-100 focus:outline-none focus:ring-2 focus:ring-indigo-500/50 focus:border-indigo-500 transition-colors"
                    />
                  </div>
                </div>
                <div>
                  <label htmlFor="confirm-password" className="block text-xs font-semibold text-slate-300 mb-1.5">Confirm new password</label>
                  <div className="relative">
                    <Lock className="w-4 h-4 text-slate-400 absolute left-3.5 top-3" />
                    <input
                      id="confirm-password"
                      type="password"
                      required
                      minLength={6}
                      maxLength={100}
                      value={confirmPassword}
                      onChange={(event) => setConfirmPassword(event.target.value)}
                      autoComplete="new-password"
                      className="w-full pl-10 pr-4 py-2.5 rounded-xl bg-slate-950/80 border border-slate-800 text-sm text-slate-100 focus:outline-none focus:ring-2 focus:ring-indigo-500/50 focus:border-indigo-500 transition-colors"
                    />
                  </div>
                </div>
                <button
                  type="submit"
                  disabled={submitting}
                  className="w-full py-3 px-4 rounded-xl bg-gradient-to-r from-indigo-600 to-indigo-700 hover:from-indigo-500 hover:to-indigo-600 text-white text-sm font-bold shadow-lg shadow-indigo-600/25 transition-all disabled:opacity-50"
                >
                  {submitting ? 'Updating…' : 'Reset password'}
                </button>
              </form>
            </>
          )}
          <div className="mt-6 text-center text-xs">
            <Link to="/login" className="inline-flex items-center gap-1.5 font-semibold text-indigo-400 hover:text-indigo-300">
              <ArrowLeft className="w-3.5 h-3.5" /> Back to sign in
            </Link>
          </div>
        </div>
      </div>
    </div>
  );
}
