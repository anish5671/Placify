import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { 
  Building2, 
  Search, 
  Briefcase, 
  HelpCircle, 
  ArrowRight, 
  Sparkles,
  Award,
  Layers
} from 'lucide-react';
import api from '../api/client';

export default function CompanyPrep() {
  const [companies, setCompanies] = useState([]);
  const [loading, setLoading] = useState(true);
  const [loadError, setLoadError] = useState('');
  const [search, setSearch] = useState('');
  const [difficultyFilter, setDifficultyFilter] = useState('ALL');

  useEffect(() => {
    fetchCompanies();
  }, []);

  const fetchCompanies = async () => {
    try {
      setLoading(true);
      const res = await api.get('/companies');
      if (res.data.success) {
        setCompanies(res.data.data);
        setLoadError('');
      } else {
        setCompanies([]);
        setLoadError('The company service returned an unsuccessful response.');
      }
    } catch (err) {
      console.error('Failed to load companies', err);
      setCompanies([]);
      setLoadError(err.response?.data?.message || 'Unable to load companies. Check that the backend is running, then try again.');
    } finally {
      setLoading(false);
    }
  };

  const filteredCompanies = companies.filter((c) => {
    const matchesSearch = c.name.toLowerCase().includes(search.toLowerCase()) || 
                          c.code.toLowerCase().includes(search.toLowerCase());
    const matchesDiff = difficultyFilter === 'ALL' || c.difficultyLevel === difficultyFilter;
    return matchesSearch && matchesDiff;
  });

  return (
    <div className="space-y-6">
      {/* Page Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h2 className="text-2xl font-black text-white tracking-tight flex items-center gap-2">
            <Building2 className="w-6 h-6 text-indigo-400" />
            <span>Company Preparation Hub</span>
          </h2>
          <p className="text-xs sm:text-sm text-slate-400 mt-1">
            Company-specific roadmaps, eligibility criteria, hiring patterns & curated question banks
          </p>
        </div>

        {/* Search & Filter Controls */}
        <div className="flex flex-wrap items-center gap-3">
          <div className="relative">
            <Search className="w-4 h-4 text-slate-400 absolute left-3.5 top-2.5" />
            <input
              type="text"
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              placeholder="Search companies..."
              className="pl-9 pr-4 py-2 rounded-xl bg-slate-900 border border-slate-800 text-xs sm:text-sm text-slate-100 placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-indigo-500/50 w-48 sm:w-60"
            />
          </div>

          <select
            value={difficultyFilter}
            onChange={(e) => setDifficultyFilter(e.target.value)}
            className="px-3 py-2 rounded-xl bg-slate-900 border border-slate-800 text-xs sm:text-sm text-slate-200 focus:outline-none focus:ring-2 focus:ring-indigo-500/50"
          >
            <option value="ALL">All Difficulties</option>
            <option value="EASY_MEDIUM">Easy / Medium</option>
            <option value="MEDIUM">Medium</option>
            <option value="HARD">Hard (Tier 1)</option>
          </select>
        </div>
      </div>

      {/* Company Grid */}
      {loading ? (
        <div className="flex flex-col items-center justify-center min-h-[40vh] gap-3">
          <div className="w-10 h-10 border-4 border-indigo-500/20 border-t-indigo-500 rounded-full animate-spin"></div>
          <p className="text-sm text-slate-400">Loading companies & roadmaps...</p>
        </div>
      ) : loadError ? (
        <div role="alert" className="p-6 rounded-2xl bg-rose-500/10 border border-rose-500/30 text-sm text-rose-200">
          <p>{loadError}</p>
          <button onClick={fetchCompanies} className="mt-3 px-3 py-1.5 rounded-lg bg-rose-500/20 hover:bg-rose-500/30 font-bold">
            Try again
          </button>
        </div>
      ) : filteredCompanies.length === 0 ? (
        <div className="py-12 text-center rounded-2xl bg-slate-900/60 border border-dashed border-slate-800">
          <Building2 className="w-10 h-10 text-slate-600 mx-auto mb-2" />
          <p className="text-sm font-semibold text-slate-300">No companies match your search</p>
          <p className="text-xs text-slate-500 mt-1">Try another company name or difficulty.</p>
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-5">
          {filteredCompanies.map((company) => (
            <div
              key={company.id}
              className="p-6 rounded-3xl bg-slate-900/90 border border-slate-800 hover:border-indigo-500/40 transition-all shadow-lg hover:shadow-indigo-500/10 group flex flex-col justify-between"
            >
              <div>
                {/* Header: Logo, Name & Badge */}
                <div className="flex items-start justify-between gap-3 mb-4">
                  <div className="flex items-center gap-3">
                    <div className="w-12 h-12 rounded-2xl bg-white p-2 flex items-center justify-center shadow-md">
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
                      <h3 className="text-base font-bold text-white group-hover:text-indigo-400 transition-colors">
                        {company.name}
                      </h3>
                      <span className="text-[11px] font-semibold text-slate-400 uppercase tracking-wider">
                        {company.code}
                      </span>
                    </div>
                  </div>

                  <span className={`px-2 py-0.5 rounded-full text-[10px] font-extrabold uppercase ${
                    company.difficultyLevel === 'HARD'
                      ? 'bg-rose-500/10 text-rose-400 border border-rose-500/30'
                      : company.difficultyLevel === 'MEDIUM'
                      ? 'bg-amber-500/10 text-amber-400 border border-amber-500/30'
                      : 'bg-emerald-500/10 text-emerald-400 border border-emerald-500/30'
                  }`}>
                    {company.difficultyLevel}
                  </span>
                </div>

                {/* Description */}
                <p className="text-xs text-slate-300 line-clamp-2 mb-4 leading-relaxed">
                  {company.description}
                </p>

                {/* Package & Roles */}
                <div className="space-y-2 mb-4 p-3 rounded-2xl bg-slate-950/60 border border-slate-800/80">
                  <div className="text-[11px] text-slate-400 flex items-center gap-1.5">
                    <Award className="w-3.5 h-3.5 text-indigo-400" />
                    <span className="font-semibold text-slate-200">Package:</span>
                    <span className="text-indigo-300 font-bold">{company.packageInfo || 'Competitive'}</span>
                  </div>
                  <div className="text-[11px] text-slate-400 flex items-center gap-1.5">
                    <Briefcase className="w-3.5 h-3.5 text-slate-400" />
                    <span className="font-semibold text-slate-200">Roles:</span>
                    <span className="text-slate-300 truncate">{company.rolesOffered || 'Software Engineer'}</span>
                  </div>
                </div>

                {/* Stats */}
                <div className="flex items-center justify-between text-xs text-slate-400 border-t border-slate-800/80 pt-3 mb-4">
                  <span className="flex items-center gap-1 font-medium">
                    <HelpCircle className="w-3.5 h-3.5 text-indigo-400" />
                    <strong className="text-white">{company.questionCount}</strong> Questions
                  </span>
                </div>
              </div>

              {/* Action Button */}
              <Link
                to={`/companies/${company.id}`}
                className="w-full py-2.5 px-4 rounded-xl bg-slate-800 group-hover:bg-indigo-600 text-slate-200 group-hover:text-white text-xs font-bold transition-all flex items-center justify-center gap-2 shadow-sm"
              >
                <span>View Full Roadmap & Prep</span>
                <ArrowRight className="w-3.5 h-3.5 group-hover:translate-x-1 transition-transform" />
              </Link>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
