const roadmap = [
  {
    title: 'Understand the assessment pattern',
    topics: 'Assessment sections, question formats, time limits, and elimination stages',
    subtopics: 'MCQ strategy, coding environment, sectional timing, instructions, and test-day setup',
    practice: 'Take one untimed diagnostic across aptitude, technical MCQs, and coding; record a baseline by section.',
    tip: 'Use the current role-specific invitation or careers notice as the authority. Older campus patterns are only a guide.',
    difficulty: 'Easy',
  },
  {
    title: 'Quantitative aptitude',
    topics: 'Arithmetic, algebra, and data interpretation',
    subtopics: 'Percentages, ratios, averages, time/work, speed/distance, probability, permutations, and charts',
    practice: 'Solve 15 mixed questions in a timed set, then review every guess and slow calculation.',
    tip: 'Prioritize accuracy and quick estimation before learning shortcut formulas.',
    difficulty: 'Easy–Medium',
  },
  {
    title: 'Logical reasoning',
    topics: 'Deduction, arrangements, and analytical reasoning',
    subtopics: 'Sequences, syllogisms, directions, relations, seating, constraints, and scheduling puzzles',
    practice: 'Complete 10 mixed reasoning questions and explain the elimination logic for each answer.',
    tip: 'Skip a puzzle that is consuming time; return after securing the shorter questions.',
    difficulty: 'Easy–Medium',
  },
  {
    title: 'Verbal ability',
    topics: 'Reading comprehension and clear written communication',
    subtopics: 'Grammar, sentence correction, vocabulary in context, paragraph ordering, and concise summaries',
    practice: 'Read one short technical article and answer comprehension questions under a time limit.',
    tip: 'For communication rounds, practice speaking clearly at a natural pace rather than memorizing scripts.',
    difficulty: 'Easy–Medium',
  },
  {
    title: 'Programming fundamentals',
    topics: 'One interview language and code tracing',
    subtopics: 'Types, control flow, functions, arrays, strings, collections, recursion, and debugging',
    practice: 'Trace 10 short snippets and implement common string/array operations without library shortcuts.',
    tip: 'Choose a language you can explain confidently; check the target role’s stated language requirements.',
    difficulty: 'Easy–Medium',
  },
  {
    title: 'Data structures and algorithms',
    topics: 'Pattern-based problem solving, scaled to the selected role',
    subtopics: 'Arrays, strings, hashing, two pointers, windows, binary search, stacks, trees, graphs, and DP',
    practice: 'Solve problems from the selected role’s pattern priorities; explain complexity before coding.',
    tip: 'Entry roles benefit from reliable fundamentals; specialist and product roles need deeper tree, graph, and DP practice.',
    difficulty: 'Role-dependent',
  },
  {
    title: 'Core computer science',
    topics: 'OOP, DBMS, operating systems, and networks',
    subtopics: 'Design principles, SQL/normalization, transactions, processes/threads, memory, HTTP, and TCP/IP',
    practice: 'Answer five concept questions aloud and write two SQL queries involving joins or aggregation.',
    tip: 'Tie definitions to a concrete example; prioritize fundamentals that are relevant to the advertised role.',
    difficulty: 'Medium',
  },
  {
    title: 'Coding assessment practice',
    topics: 'Correctness, complexity, and implementation under time pressure',
    subtopics: 'Input/output, edge cases, constraints, test cases, debugging, and clean code',
    practice: 'Complete a timed set of two problems: one fundamentals problem and one role-appropriate stretch problem.',
    tip: 'Test empty, minimum, duplicate, and large inputs before submitting.',
    difficulty: 'Role-dependent',
  },
  {
    title: 'Technical interview',
    topics: 'Problem solving, CS fundamentals, and project depth',
    subtopics: 'Clarifying questions, trade-offs, complexity, project architecture, ownership, and debugging',
    practice: 'Run a mock interview: solve one problem aloud, then explain one project decision and its trade-offs.',
    tip: 'Communicate your approach while working; ask clarifying questions before committing to assumptions.',
    difficulty: 'Medium–Hard',
  },
  {
    title: 'HR and behavioral interview',
    topics: 'Motivation, communication, ownership, and teamwork',
    subtopics: 'Role fit, strengths, learning from setbacks, collaboration, and questions for the interviewer',
    practice: 'Prepare concise STAR examples for ownership, teamwork, conflict, and a difficult learning experience.',
    tip: 'Use truthful, specific examples and connect your motivation to the actual role.',
    difficulty: 'Easy–Medium',
  },
];

const companyGuides = {
  TCS: {
    roles: [
      { title: 'Ninja / entry-level', package: 'Ninja: 3.36 LPA (catalog figure; verify current offer)', tier: 'entry', focus: 'NQT foundation, basic coding, communication, and core fundamentals.' },
      { title: 'Digital', package: 'Digital: 7.0 LPA (catalog figure; verify current offer)', tier: 'advanced', focus: 'Stronger coding, algorithmic reasoning, and technical interview depth.' },
      { title: 'Prime', package: 'Prime: 9–11 LPA (catalog figure; verify current offer)', tier: 'specialist', focus: 'More demanding coding and deeper problem solving; confirm current track and compensation.' },
    ],
    patterns: ['Arrays and strings', 'Hash maps and sorting', 'Basic recursion and implementation', 'Advanced tracks: trees, graphs, and dynamic programming'],
    csFocus: 'OOP, SQL/DBMS, operating-system fundamentals, and project explanation.',
    official: 'https://www.tcs.com/careers',
  },
  ACCENTURE: {
    roles: [
      { title: 'Associate Software Engineer (ASE)', package: 'ASE: 4.5 LPA (catalog figure; verify current offer)', tier: 'entry', focus: 'Cognitive reasoning, communication, pseudocode, and straightforward coding.' },
      { title: 'Advanced ASE', package: 'Advanced ASE: 6.5 LPA (catalog figure; verify current offer)', tier: 'advanced', focus: 'More emphasis on coding fluency, implementation, and technical depth.' },
    ],
    patterns: ['Arrays and strings', 'Hashing and sorting', 'Basic searching and implementation', 'Stretch practice: recursion, trees, and dynamic programming'],
    csFocus: 'Pseudocode, OOP, SQL/DBMS, networking, cloud, and security basics.',
    official: 'https://www.accenture.com/careers',
  },
  INFOSYS: {
    roles: [
      { title: 'Systems Engineer', package: 'Systems Engineer: 3.6 LPA (catalog figure; verify current offer)', tier: 'entry', focus: 'Aptitude, reasoning, verbal ability, pseudocode, and programming basics.' },
      { title: 'Digital Specialist Engineer', package: 'DSE: 6.25 LPA (catalog figure; verify current offer)', tier: 'advanced', focus: 'Stronger coding and data-structure problem solving than a fundamentals-only track.' },
      { title: 'Specialist Programmer', package: 'SP: 9.5 LPA (catalog figure; verify current offer)', tier: 'specialist', focus: 'Algorithmic coding depth; prepare trees, graphs, and dynamic programming as stretch areas.' },
    ],
    patterns: ['Arrays, strings, and hashing', 'Sorting and binary search', 'Recursion and trees for specialist tracks', 'Dynamic programming and graphs as higher-difficulty practice'],
    csFocus: 'Pseudocode, OOP, DBMS/SQL, OS, and clear discussion of projects.',
    official: 'https://www.infosys.com/careers',
  },
  WIPRO: {
    roles: [
      { title: 'Project Engineer / Elite', package: 'Elite: 3.5 LPA (catalog figure; verify current offer)', tier: 'entry', focus: 'Aptitude, written communication, programming fundamentals, and basic coding.' },
      { title: 'Turbo', package: 'Turbo: 6.5 LPA (catalog figure; verify current offer)', tier: 'advanced', focus: 'More demanding coding fluency and stronger fundamentals; verify the current hiring track.' },
    ],
    patterns: ['Arrays and strings', 'Basic sorting and search', 'Hashing and simple recursion', 'Stretch practice: trees and dynamic programming'],
    csFocus: 'OOP, SQL, OS/networking basics, and projects relevant to the role.',
    official: 'https://careers.wipro.com',
  },
  COGNIZANT: {
    roles: [
      { title: 'GenC', package: 'GenC: 4.0 LPA (catalog figure; verify current offer)', tier: 'entry', focus: 'Aptitude, verbal ability, pseudocode, and programming fundamentals.' },
      { title: 'GenC Elevate', package: 'GenC Elevate: 5.5 LPA (catalog figure; verify current offer)', tier: 'advanced', focus: 'Add stronger coding, SQL, and applied technical problem solving.' },
      { title: 'GenC Next', package: 'GenC Next: 6.75 LPA (catalog figure; verify current offer)', tier: 'specialist', focus: 'Prepare for a higher coding bar and broader technical discussion; verify current criteria.' },
    ],
    patterns: ['Arrays, strings, and simple math', 'Hash maps and sorting', 'SQL and debugging', 'Stretch practice: recursion, trees, and dynamic programming'],
    csFocus: 'Pseudocode, Java/Python fundamentals, SQL, OOP, and basic web concepts.',
    official: 'https://careers.cognizant.com',
  },
  AMAZON: {
    roles: [
      { title: 'Software Development Engineer I', package: 'SDE I: 30–45 LPA total compensation (catalog figure; not a current offer quote)', tier: 'specialist', focus: 'Timed DSA, robust code, complexity analysis, and behavioral examples tied to Leadership Principles.' },
      { title: 'Cloud Support Associate', package: 'Compensation not specified in the company catalog; check the current listing.', tier: 'entry', focus: 'Role-specific troubleshooting, networking/Linux fundamentals, customer communication, and scripting.' },
    ],
    patterns: ['Hashing, arrays, and sliding windows', 'Trees, heaps, and graphs', 'Greedy and dynamic programming', 'Practice explaining complexity and edge cases aloud'],
    csFocus: 'SDE: data structures, OOP, OS/DBMS, and system design fundamentals. Cloud support: Linux, networking, and troubleshooting.',
    official: 'https://www.amazon.jobs',
  },
  GOOGLE: {
    roles: [
      { title: 'Software Engineer L3', package: 'L3: 35–55 LPA (catalog figure; not a current offer quote)', tier: 'specialist', focus: 'Algorithmic problem solving, clean implementation, complexity, and communication.' },
      { title: 'Site Reliability Engineer', package: 'Compensation not specified in the company catalog; check the current listing.', tier: 'specialist', focus: 'Coding plus operating systems, networking, reliability, and production troubleshooting.' },
    ],
    patterns: ['Arrays, strings, and hashing', 'Trees and graph traversal', 'Binary search and greedy reasoning', 'Dynamic programming; practice deriving rather than memorizing solutions'],
    csFocus: 'Algorithms, complexity, OS, networking, and role-appropriate design/reliability concepts.',
    official: 'https://careers.google.com',
  },
  MICROSOFT: {
    roles: [
      { title: 'Software Engineer', package: 'SDE-1: 30–42 LPA (catalog figure; not a current offer quote)', tier: 'specialist', focus: 'DSA, code quality, CS fundamentals, and collaborative technical discussion.' },
      { title: 'Cloud Solution Architect', package: 'Compensation not specified in the company catalog; check the current listing.', tier: 'advanced', focus: 'Cloud and systems fundamentals, solution communication, and role-specific technical depth.' },
    ],
    patterns: ['Arrays, strings, and hashing', 'Trees, linked lists, and recursion', 'Graphs and dynamic programming', 'Practice clean code, test cases, and explaining trade-offs'],
    csFocus: 'OOP, OS, DBMS, networking, and concurrency or design basics when relevant to the role.',
    official: 'https://careers.microsoft.com',
  },
};

const roundPreparation = [
  'Check the latest role-specific notice for sections, eligibility, timing, and tool requirements. Treat older patterns as historical, not guaranteed.',
  'Practice timed mixed questions; review accuracy, pacing, and the concepts behind mistakes.',
  'For coding, clarify constraints, outline an approach, discuss complexity, then test edge cases.',
  'Prepare project walkthroughs and role-relevant fundamentals. Use examples to show ownership and clear communication.',
  'For behavioral discussions, prepare concise, truthful examples and adapt them to the question asked.',
];

export function getCompanyGuide(code) {
  return companyGuides[code] || null;
}

export function getRoadmap(role) {
  return roadmap.map((step, index) => {
    const isAdvanced = role?.tier === 'advanced' || role?.tier === 'specialist';
    const isSpecialist = role?.tier === 'specialist';
    let priority = 'Medium';

    if ([0, 4, 7, 8].includes(index)) priority = 'High';
    if ([1, 2, 3].includes(index) && role?.tier === 'entry') priority = 'High';
    if (index === 5 && isAdvanced) priority = 'High';
    if ([6, 8].includes(index) && isSpecialist) priority = 'High';
    if (index === 9 && role?.tier !== 'entry') priority = 'High';
    if (index === 7 && !isAdvanced) priority = 'Medium';

    return {
      ...step,
      priority,
      topics: index === 5 && role ? `${step.topics}. ${role.focus}` : step.topics,
    };
  });
}

export function getRoundPreparation(index) {
  return roundPreparation[Math.min(index, roundPreparation.length - 1)];
}

export function getQuestionPattern(question) {
  const text = `${question.title || ''} ${question.topicName || ''}`.toLowerCase();
  const patterns = [
    ['Sliding window', /sliding window|substring|subarray.*window/],
    ['Two pointers', /two.?pointer|pair sum|three sum|3sum/],
    ['Hashing', /hash|frequency|anagram|duplicate/],
    ['Binary search', /binary search|rotated array|search range/],
    ['Trees', /tree|ancestor|traversal/],
    ['Graphs', /graph|island|shortest path|bfs|dfs/],
    ['Dynamic programming', /dynamic programming|\bdp\b|subsequence|knapsack|coin change/],
    ['Stacks and queues', /stack|queue|next greater/],
    ['Arrays and strings', /array|string|prefix sum|subarray/],
  ];

  return patterns.find(([, pattern]) => pattern.test(text))?.[0] || question.topicName || 'Other';
}
