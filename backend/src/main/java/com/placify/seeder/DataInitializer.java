package com.placify.seeder;

import com.placify.company.Company;
import com.placify.company.CompanyRepository;
import com.placify.question.Question;
import com.placify.question.QuestionRepository;
import com.placify.question.Topic;
import com.placify.question.TopicRepository;
import com.placify.test.Test;
import com.placify.test.TestQuestion;
import com.placify.test.TestQuestionRepository;
import com.placify.test.TestRepository;
import com.placify.user.User;
import com.placify.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final TopicRepository topicRepository;
    private final QuestionRepository questionRepository;
    private final TestRepository testRepository;
    private final TestQuestionRepository testQuestionRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.count() == 0) {
            seedDemoUser();
        }

        if (topicRepository.count() == 0) {
            Map<String, Topic> topics = seedTopics();
            Map<String, Company> companies = seedCompanies();
            List<Question> questions = seedQuestions(topics);
            seedMockTests(companies, questions);
            log.info("Placify Data Seeding Completed Successfully!");
        }

        seedMissingDsaQuestions();
        syncDsaMockTest();
        seedAptitudeAndCoreQuestionBanks();
        syncAptitudeCoreMockTests();
    }

    private void seedDemoUser() {
        User demoUser = User.builder()
                .username("demo")
                .email("demo@placify.com")
                .password(passwordEncoder.encode("password123"))
                .fullName("Anish Sharma")
                .targetCompany("Amazon")
                .targetRole("Software Development Engineer (SDE-1)")
                .graduationYear(2025)
                .role("ROLE_USER")
                .build();
        userRepository.save(demoUser);
        log.info("Demo user seeded: demo / password123");
    }

    private Map<String, Topic> seedTopics() {
        List<Topic> topicList = List.of(
                Topic.builder().name("Quantitative Aptitude").slug("quant").category("APTITUDE").description("Number system, percentages, ratios, profit & loss, time and work").icon("Calculator").build(),
                Topic.builder().name("Logical Reasoning").slug("reasoning").category("REASONING").description("Puzzles, syllogisms, blood relations, seating arrangement").icon("Brain").build(),
                Topic.builder().name("Verbal Ability").slug("verbal").category("VERBAL").description("Reading comprehension, sentence correction, vocabulary").icon("BookOpen").build(),
                Topic.builder().name("Java & OOPs").slug("java-oops").category("JAVA_OOPS").description("Encapsulation, inheritance, polymorphism, multithreading, collections").icon("Code2").build(),
                Topic.builder().name("Database Management Systems").slug("dbms").category("DBMS").description("ACID properties, indexing, normalization, transactions").icon("Database").build(),
                Topic.builder().name("SQL Queries & Design").slug("sql").category("DBMS").description("Joins, subqueries, group by, aggregations, window functions").icon("Server").build(),
                Topic.builder().name("Operating Systems").slug("os").category("OS").description("Process scheduling, multithreading, deadlocks, memory management").icon("Cpu").build(),
                Topic.builder().name("Computer Networks").slug("networks").category("NETWORKS").description("OSI model, TCP/IP, HTTP/S, DNS, routing protocols").icon("Network").build(),
                Topic.builder().name("Data Structures & Algorithms").slug("dsa").category("DSA").description("Arrays, strings, linked lists, trees, graphs, greedy and dynamic programming").icon("Binary").build()
        );

        Map<String, Topic> map = new HashMap<>();
        for (Topic t : topicRepository.saveAll(topicList)) {
            map.put(t.getSlug(), t);
        }
        return map;
    }

    private Map<String, Company> seedCompanies() {
        List<Company> list = List.of(
                Company.builder()
                        .name("Tata Consultancy Services")
                        .code("TCS")
                        .logoUrl("https://upload.wikimedia.org/wikipedia/commons/b/b1/Tata_Consultancy_Services_Logo.svg")
                        .description("TCS is a global leader in IT services, consulting, and business solutions. Hiring is primarily conducted via the National Qualifier Test (NQT).")
                        .hiringProcess("Round 1: TCS NQT (Part A: Foundation Section - Numerical, Verbal, Reasoning; Part B: Advanced Section - Advanced Quant, Reasoning & Coding 2 problems)\nRound 2: Technical Interview (Core CS, Project, Language fundamentals)\nRound 3: Managerial & HR Interview")
                        .eligibilityCriteria("60% or 6.0 CGPA throughout 10th, 12th, Diploma, UG, and PG. Maximum 1 active backlog allowed at time of test. Gap in education up to 24 months allowed.")
                        .syllabusOverview("Numerical Ability, Verbal Ability, Reasoning Ability, Advanced Quantitative & Reasoning, Hands-on Coding in C/C++/Java/Python.")
                        .difficultyLevel("MEDIUM")
                        .websiteUrl("https://www.tcs.com/careers")
                        .packageInfo("Ninja: 3.36 LPA | Digital: 7.0 LPA | Prime: 9-11 LPA")
                        .rolesOffered("Assistant System Engineer, Systems Engineer, Prime Software Engineer")
                        .build(),

                Company.builder()
                        .name("Amazon")
                        .code("AMAZON")
                        .logoUrl("https://upload.wikimedia.org/wikipedia/commons/a/a9/Amazon_logo.svg")
                        .description("Amazon focuses heavily on Data Structures, Algorithms, System Architecture, and adherence to their 16 Leadership Principles.")
                        .hiringProcess("Round 1: Online Assessment (OA1: Debugging/Aptitude, OA2: 2 Coding Problems + Work Simulation, OA3: Work Style Assessment)\nRound 2-4: Virtual Onsite Technical Rounds (DSA, Problem Solving, LP questions)\nRound 5: Bar Raiser Round (High bar technical & behavioral)")
                        .eligibilityCriteria("B.Tech/M.Tech in CS/IT or related branches. Minimum 6.5 CGPA. Strong problem solving foundation.")
                        .syllabusOverview("Trees, Graphs, DP, Heap, Greedy, HashMaps, Low Level Design (LLD), OS/DBMS internals, Leadership Principles.")
                        .difficultyLevel("HARD")
                        .websiteUrl("https://www.amazon.jobs")
                        .packageInfo("SDE-1: 30 - 45 LPA (Base + Stocks + Bonus)")
                        .rolesOffered("Software Development Engineer I (SDE-1), Cloud Support Associate")
                        .build(),

                Company.builder()
                        .name("Infosys")
                        .code("INFOSYS")
                        .logoUrl("https://upload.wikimedia.org/wikipedia/commons/9/95/Infosys_logo.svg")
                        .description("Infosys conducts hiring through campus placement, InfyTQ certification, and HackWithInfy hackathon for specialist and digital roles.")
                        .hiringProcess("Round 1: Online Assessment (Reasoning Ability, Mathematical Ability, Verbal Ability, Pseudocode, Puzzle Solving)\nRound 2: Technical + HR Interview")
                        .eligibilityCriteria("Minimum 60% or 6.0 CGPA in 10th, 12th, and graduation. No active backlogs allowed.")
                        .syllabusOverview("Logical Reasoning, Quantitative Aptitude, English Comprehension, Pseudocode analysis, Data structures basics.")
                        .difficultyLevel("MEDIUM")
                        .websiteUrl("https://www.infosys.com/careers")
                        .packageInfo("Systems Engineer: 3.6 LPA | DSE: 6.25 LPA | Specialist Programmer: 9.5 LPA")
                        .rolesOffered("System Engineer, Digital Specialist Engineer, Specialist Programmer")
                        .build(),

                Company.builder()
                        .name("Accenture")
                        .code("ACCENTURE")
                        .logoUrl("https://upload.wikimedia.org/wikipedia/commons/c/cd/Accenture.svg")
                        .description("Accenture recruits through a staged elimination assessment evaluating cognitive, technical, coding, and communication skills.")
                        .hiringProcess("Stage 1: Cognitive & Technical Assessment (90 mins, 90 MCQs - Critical Thinking, Abstract Reasoning, English, Pseudocode, Common Applications, Networking & Cloud)\nStage 2: Coding Assessment (45 mins, 2 questions - C/C++/Java/Python)\nStage 3: Communication Assessment (Pronunciation, Fluency, Listening)\nStage 4: Technical & HR Interview")
                        .eligibilityCriteria("Full-time degree with 65% or 6.5 CGPA. Maximum 1 year education gap.")
                        .syllabusOverview("Analytical Reasoning, English, Pseudocode logic, MS Office & Cloud basics, Security & Networks, Coding (Strings, Arrays, Math).")
                        .difficultyLevel("EASY_MEDIUM")
                        .websiteUrl("https://www.accenture.com/careers")
                        .packageInfo("Associate Software Engineer: 4.5 LPA | Advanced ASE: 6.5 LPA")
                        .rolesOffered("Associate Software Engineer (ASE), Advanced ASE")
                        .build(),

                Company.builder()
                        .name("Google")
                        .code("GOOGLE")
                        .logoUrl("https://upload.wikimedia.org/wikipedia/commons/2/2f/Google_2015_logo.svg")
                        .description("Google hires through Google Online Challenge (GOC) and referral applications, evaluating algorithmic mastery, scalable coding, and Googleyness.")
                        .hiringProcess("Round 1: Google Online Challenge (2 algorithmic coding problems)\nRound 2: Phone Technical Screen (45 mins live coding on Google Docs/CoderPad)\nRound 3-5: Onsite Coding Interviews (Data structures, algorithms, complexity trade-offs)\nRound 6: Googleyness & Leadership Interview")
                        .eligibilityCriteria("Bachelor's or Master's in Computer Science or related STEM field. Exceptional algorithmic thinking.")
                        .syllabusOverview("Graph algorithms, Dynamic Programming, Segment Trees, Trie, Binary Search on Answer, Complexity Analysis, Clean Code.")
                        .difficultyLevel("HARD")
                        .websiteUrl("https://careers.google.com")
                        .packageInfo("Software Engineer L3: 35 - 55 LPA")
                        .rolesOffered("Software Engineer L3, Site Reliability Engineer")
                        .build(),

                Company.builder()
                        .name("Wipro")
                        .code("WIPRO")
                        .logoUrl("https://upload.wikimedia.org/wikipedia/commons/a/a0/Wipro_Logo_RGB.png")
                        .description("Wipro hires through Elite National Talent Hunt (NTH) and Turbo hiring channels for engineering graduates across India.")
                        .hiringProcess("Round 1: Online Assessment (Aptitude test: Logical, Quantitative, English; Written Communication Test; Online Programming Test)\nRound 2: Business Discussion / Technical & HR Interview")
                        .eligibilityCriteria("60% or 6.0 CGPA throughout 10th, 12th, and graduation. 3-year gap max allowed.")
                        .syllabusOverview("Quantitative Aptitude, Logical Ability, Verbal English, Essay Writing, Basic Coding.")
                        .difficultyLevel("EASY_MEDIUM")
                        .websiteUrl("https://careers.wipro.com")
                        .packageInfo("Project Engineer (Elite): 3.5 LPA | Turbo: 6.5 LPA")
                        .rolesOffered("Project Engineer, Turbo Engineer")
                        .build(),

                Company.builder()
                        .name("Microsoft")
                        .code("MICROSOFT")
                        .logoUrl("https://upload.wikimedia.org/wikipedia/commons/9/96/Microsoft_logo_%282012%29.svg")
                        .description("Microsoft evaluates solid CS fundamentals, problem solving on LeetCode Medium/Hard topics, and object-oriented design.")
                        .hiringProcess("Round 1: Codility Online Assessment (3 coding challenges)\nRound 2-4: Technical Interviews (DSA, Tree/Graph traversals, Dynamic Programming, System Design basics)\nRound 5: AA (As Appropriate / Hiring Manager) Round")
                        .eligibilityCriteria("B.Tech/M.Tech with strong foundational knowledge of OS, DBMS, OOP, and DSA.")
                        .syllabusOverview("Trees, Graphs, Recursion, DP, OOP design principles, Concurrency basics, Memory management.")
                        .difficultyLevel("HARD")
                        .websiteUrl("https://careers.microsoft.com")
                        .packageInfo("SDE-1: 30 - 42 LPA")
                        .rolesOffered("Software Engineer, Cloud Solution Architect")
                        .build(),

                Company.builder()
                        .name("Cognizant")
                        .code("COGNIZANT")
                        .logoUrl("https://upload.wikimedia.org/wikipedia/commons/2/29/Cognizant_logo_2022.svg")
                        .description("Cognizant recruits for GenC, GenC Elevate, and GenC Next tracks through comprehensive aptitude, pseudocode, and coding exams.")
                        .hiringProcess("Round 1: GenC Assessment (Quantitative, Analytical, Verbal, Pseudocode)\nRound 2: Skill-based Technical Assessment (SQL, Web, Java/Python)\nRound 3: Technical & HR Discussion")
                        .eligibilityCriteria("60% or 6.0 CGPA with no active backlogs.")
                        .syllabusOverview("Quantitative aptitude, logical deductions, grammar and vocabulary, pseudocode debugging, SQL queries.")
                        .difficultyLevel("EASY_MEDIUM")
                        .websiteUrl("https://careers.cognizant.com")
                        .packageInfo("GenC: 4.0 LPA | GenC Elevate: 5.5 LPA | GenC Next: 6.75 LPA")
                        .rolesOffered("Programmer Analyst Trainee, Associate Software Engineer")
                        .build()
        );

        Map<String, Company> map = new HashMap<>();
        for (Company c : companyRepository.saveAll(list)) {
            map.put(c.getCode(), c);
        }
        return map;
    }

    private List<Question> seedQuestions(Map<String, Topic> topics) {
        List<Question> qList = new ArrayList<>();

        Topic quant = topics.get("quant");
        Topic reasoning = topics.get("reasoning");
        Topic verbal = topics.get("verbal");
        Topic javaOops = topics.get("java-oops");
        Topic dbms = topics.get("dbms");
        Topic sql = topics.get("sql");
        Topic os = topics.get("os");
        Topic networks = topics.get("networks");
        Topic dsa = topics.get("dsa");

        // 1. QUANTITATIVE APTITUDE
        qList.add(Question.builder()
                .title("A train running at 54 km/hr crosses a pole in 20 seconds. What is the length of the train?")
                .description("Calculate the distance traveled by the train while crossing a stationary pole at a constant speed.")
                .topic(quant).category("APTITUDE").difficulty("EASY")
                .optionA("250 meters").optionB("300 meters").optionC("350 meters").optionD("200 meters")
                .correctOption("B")
                .explanation("Speed = 54 km/hr = 54 * (5/18) m/s = 15 m/s.\nDistance = Speed * Time = 15 m/s * 20 s = 300 meters. Hence, the length of the train is 300 meters.")
                .companyTags("TCS,Infosys,Wipro,Accenture")
                .build());

        qList.add(Question.builder()
                .title("A and B can do a work in 12 days, B and C in 15 days, and C and A in 20 days. If A, B, and C work together, they will complete the work in:")
                .description("Classic Time and Work problem frequently asked in campus placements.")
                .topic(quant).category("APTITUDE").difficulty("MEDIUM")
                .optionA("5 days").optionB("7.5 days").optionC("10 days").optionD("15.5 days")
                .correctOption("C")
                .explanation("1 day work of (A+B) = 1/12, (B+C) = 1/15, (C+A) = 1/20.\nAdding all: 2(A+B+C) = 1/12 + 1/15 + 1/20 = (5 + 4 + 3)/60 = 12/60 = 1/5.\nTherefore, 1 day work of (A+B+C) = 1/10.\nHence, together they will finish the work in 10 days.")
                .companyTags("TCS,Cognizant,Accenture,Infosys")
                .build());

        qList.add(Question.builder()
                .title("A shopkeeper marks an article at 25% above cost price and allows a discount of 10%. What is his profit percentage?")
                .description("Profit, Loss, and Marked Price calculation.")
                .topic(quant).category("APTITUDE").difficulty("EASY")
                .optionA("12.5%").optionB("15%").optionC("10%").optionD("14.5%")
                .correctOption("A")
                .explanation("Let Cost Price (CP) = 100.\nMarked Price (MP) = 125.\nDiscount = 10% of 125 = 12.5.\nSelling Price (SP) = 125 - 12.5 = 112.5.\nProfit = SP - CP = 112.5 - 100 = 12.5% profit.")
                .companyTags("Infosys,Wipro,TCS")
                .build());

        qList.add(Question.builder()
                .title("Two numbers are in the ratio 3 : 5. If their LCM is 120, find their HCF.")
                .description("HCF and LCM properties of natural numbers.")
                .topic(quant).category("APTITUDE").difficulty("EASY")
                .optionA("6").optionB("8").optionC("10").optionD("12")
                .correctOption("B")
                .explanation("Let the numbers be 3x and 5x, where x is their HCF.\nLCM of 3x and 5x is 15x.\nGiven 15x = 120 => x = 8.\nTherefore, HCF = 8.")
                .companyTags("Accenture,Cognizant,Wipro")
                .build());

        // 2. LOGICAL REASONING
        qList.add(Question.builder()
                .title("Pointing to a photograph, Rohit said, 'She is the daughter of my grandfather's only son.' How is Rohit related to the girl in the photograph?")
                .description("Blood relations logical deduction.")
                .topic(reasoning).category("REASONING").difficulty("EASY")
                .optionA("Father").optionB("Brother").optionC("Cousin").optionD("Uncle")
                .correctOption("B")
                .explanation("Grandfather's only son = Rohit's father.\nDaughter of Rohit's father = Rohit's sister.\nTherefore, Rohit is the brother of the girl.")
                .companyTags("TCS,Infosys,Accenture")
                .build());

        qList.add(Question.builder()
                .title("Find the next number in the series: 3, 7, 15, 31, 63, ?")
                .description("Number series pattern identification.")
                .topic(reasoning).category("REASONING").difficulty("EASY")
                .optionA("127").optionB("125").optionC("129").optionD("120")
                .correctOption("A")
                .explanation("Pattern is (previous * 2) + 1:\n3 * 2 + 1 = 7\n7 * 2 + 1 = 15\n15 * 2 + 1 = 31\n31 * 2 + 1 = 63\n63 * 2 + 1 = 127.")
                .companyTags("TCS,Wipro,Infosys,Cognizant")
                .build());

        qList.add(Question.builder()
                .title("In a certain code, 'COMPUTER' is written as 'RFUVQNPC'. How is 'MEDICINE' written in that code?")
                .description("Coding and Decoding letter reversal pattern.")
                .topic(reasoning).category("REASONING").difficulty("MEDIUM")
                .optionA("EOJDJEFM").optionB("EOJDEJFM").optionC("MFEJDJOE").optionD("EOJDJFEM")
                .correctOption("A")
                .explanation("First and last letters are swapped (C and R become R and C). The intermediate letters are moved +1 and reversed.\nMEDICINE -> First 'E', Last 'M'. Intermediate letters: E(+1)=F, D(+1)=E, I(+1)=J, C(+1)=D, I(+1)=J, N(+1)=O. Reversing them gives EOJDJEFM.")
                .companyTags("Infosys,Accenture,TCS")
                .build());

        // 3. VERBAL ABILITY
        qList.add(Question.builder()
                .title("Choose the correct synonym of the word: PRAGMATIC")
                .description("Vocabulary test: word meanings.")
                .topic(verbal).category("VERBAL").difficulty("EASY")
                .optionA("Theoretical").optionB("Idealistic").optionC("Practical").optionD("Arrogant")
                .correctOption("C")
                .explanation("Pragmatic means dealing with things sensibly and realistically in a way that is based on practical rather than theoretical considerations.")
                .companyTags("TCS,Infosys,Accenture,Amazon")
                .build());

        qList.add(Question.builder()
                .title("Identify the grammatically correct sentence:")
                .description("Sentence structure and subject-verb agreement.")
                .topic(verbal).category("VERBAL").difficulty("MEDIUM")
                .optionA("Neither the teacher nor the students was present in the auditorium.")
                .optionB("Neither the teacher nor the students were present in the auditorium.")
                .optionC("Neither the teacher or the students was present in the auditorium.")
                .optionD("Neither the teacher nor the students has been present in the auditorium.")
                .correctOption("B")
                .explanation("When using 'neither... nor...', the verb agrees with the closer subject. Here, 'students' is plural and closest to the verb, so the plural verb 'were' is correct.")
                .companyTags("TCS,Accenture,Wipro")
                .build());

        // 4. JAVA & OOPS
        qList.add(Question.builder()
                .title("Why is String immutable in Java?")
                .description("Deep dive into Java String pool, security, and multi-threading.")
                .topic(javaOops).category("JAVA_OOPS").difficulty("MEDIUM")
                .optionA("To prevent subclassing of String class only.")
                .optionB("For security, caching in the String Constant Pool, thread safety, and hashcode caching.")
                .optionC("Because Java does not support pointers.")
                .optionD("To reduce heap memory allocation to zero.")
                .correctOption("B")
                .explanation("Java Strings are immutable for several key reasons: 1) String Constant Pool allows sharing instances to save memory; 2) Thread safety without synchronization; 3) Security (Strings hold sensitive data like database URLs, passwords, sockets); 4) Caches the hashCode upon creation so it is fast in HashMaps.")
                .companyTags("Amazon,Google,Microsoft,TCS,Infosys")
                .build());

        qList.add(Question.builder()
                .title("What happens if both equals() and hashCode() are not overridden in a class used as a key in HashMap?")
                .description("HashMap internal contract between equals and hashCode.")
                .topic(javaOops).category("JAVA_OOPS").difficulty("MEDIUM")
                .optionA("A compile-time error is thrown.")
                .optionB("The key will be stored using default Object equals/hashCode (memory address), causing lookups for equal objects to fail to find the entry.")
                .optionC("HashMap automatically overrides them at runtime using reflection.")
                .optionD("NullPointerException is thrown when inserting into HashMap.")
                .correctOption("B")
                .explanation("Object class provides default hashCode() based on memory address and equals() based on reference equality (==). Two logically identical objects will have different hashcodes and fail the equals check, meaning map.get(new Key(...)) returns null.")
                .companyTags("Amazon,Microsoft,Google,Infosys")
                .build());

        qList.add(Question.builder()
                .title("What is the difference between Comparable and Comparator in Java?")
                .description("Core Java sorting interfaces.")
                .topic(javaOops).category("JAVA_OOPS").difficulty("EASY")
                .optionA("Comparable provides multiple sorting sequences; Comparator provides single sorting.")
                .optionB("Comparable uses compareTo() and modifies the original class; Comparator uses compare() and enables custom, multiple sorting orders outside the class.")
                .optionC("Comparable is in java.util; Comparator is in java.lang.")
                .optionD("There is no functional difference; they are aliases.")
                .correctOption("B")
                .explanation("Comparable defines natural ordering via compareTo(T o) within the class (java.lang). Comparator allows defining multiple custom sorting strategies via compare(T o1, T o2) in separate classes or lambdas (java.util).")
                .companyTags("TCS,Infosys,Accenture,Amazon")
                .build());

        qList.add(Question.builder()
                .title("Can you override a private or static method in Java?")
                .description("Polymorphism and method hiding rules in Java.")
                .topic(javaOops).category("JAVA_OOPS").difficulty("MEDIUM")
                .optionA("Yes, both can be overridden normally.")
                .optionB("Private methods can be overridden, but static methods cannot.")
                .optionC("No, private methods are not visible to subclasses, and static methods are resolved at compile-time (method hiding, not overriding).")
                .optionD("Only if the subclass uses the @Override annotation.")
                .correctOption("C")
                .explanation("Method overriding is based on dynamic dispatch at runtime. Static methods belong to the class and are resolved at compile-time based on reference type (Method Hiding). Private methods are not inherited or accessible outside the class.")
                .companyTags("Amazon,Microsoft,TCS,Cognizant")
                .build());

        // 5. DBMS & SQL
        qList.add(Question.builder()
                .title("What is the difference between WHERE and HAVING clause in SQL?")
                .description("SQL filtering and aggregation order of execution.")
                .topic(sql).category("DBMS").difficulty("EASY")
                .optionA("WHERE is used with GROUP BY; HAVING cannot be used with GROUP BY.")
                .optionB("WHERE filters individual rows before grouping and cannot use aggregate functions; HAVING filters grouped rows after aggregation.")
                .optionC("HAVING is faster than WHERE clause.")
                .optionD("WHERE can only be used in SELECT; HAVING can be used in UPDATE and DELETE.")
                .correctOption("B")
                .explanation("WHERE filters records before any grouping occurs and cannot accept aggregate functions like COUNT(), AVG(), or SUM(). HAVING is applied to summarized group records created by GROUP BY.")
                .companyTags("TCS,Infosys,Accenture,Amazon")
                .build());

        qList.add(Question.builder()
                .title("Which normal form eliminates partial functional dependency?")
                .description("Database Normalization: 1NF, 2NF, 3NF, BCNF.")
                .topic(dbms).category("DBMS").difficulty("MEDIUM")
                .optionA("First Normal Form (1NF)")
                .optionB("Second Normal Form (2NF)")
                .optionC("Third Normal Form (3NF)")
                .optionD("Boyce-Codd Normal Form (BCNF)")
                .correctOption("B")
                .explanation("2NF requires the table to be in 1NF and that no non-prime attribute is partially dependent on any candidate key (eliminates partial dependency). 3NF eliminates transitive dependencies.")
                .companyTags("TCS,Infosys,Amazon,Cognizant")
                .build());

        qList.add(Question.builder()
                .title("Explain the ACID properties in relational database management systems:")
                .description("Transaction management fundamentals.")
                .topic(dbms).category("DBMS").difficulty("EASY")
                .optionA("Accuracy, Consistency, Integrity, Durability")
                .optionB("Atomicity, Consistency, Isolation, Durability")
                .optionC("Aggregation, Concurrency, Indexing, Distribution")
                .optionD("Asynchronous, Clustered, Integrated, Decoupled")
                .correctOption("B")
                .explanation("Atomicity: All-or-nothing execution. Consistency: Database moves from one valid state to another. Isolation: Concurrent transactions do not interfere. Durability: Committed data is permanently saved even during power failure.")
                .companyTags("Amazon,Google,Microsoft,TCS,Infosys")
                .build());

        qList.add(Question.builder()
                .title("Which SQL query finds the 3rd highest salary of an employee?")
                .description("Nth highest salary query using subqueries or DENSE_RANK().")
                .topic(sql).category("DBMS").difficulty("MEDIUM")
                .optionA("SELECT salary FROM employees ORDER BY salary DESC LIMIT 3;")
                .optionB("SELECT DISTINCT salary FROM employees e1 WHERE 3 = (SELECT COUNT(DISTINCT salary) FROM employees e2 WHERE e2.salary >= e1.salary);")
                .optionC("SELECT MAX(salary) FROM employees WHERE salary < (SELECT MAX(salary) FROM employees);")
                .optionD("SELECT salary FROM employees GROUP BY salary HAVING COUNT(*) = 3;")
                .correctOption("B")
                .explanation("This correlated subquery counts how many distinct salaries are greater than or equal to e1.salary. When that count equals 3, e1.salary is the 3rd highest salary.")
                .companyTags("Amazon,TCS,Infosys,Accenture")
                .build());

        // 6. OPERATING SYSTEMS
        qList.add(Question.builder()
                .title("What are the four necessary conditions for a Deadlock to occur?")
                .description("Coffman conditions in Operating Systems.")
                .topic(os).category("OS").difficulty("MEDIUM")
                .optionA("Mutual Exclusion, Hold and Wait, No Preemption, Circular Wait")
                .optionB("Concurrency, Swapping, Thrashing, Aging")
                .optionC("Starvation, Semaphores, Critical Section, Mutex")
                .optionD("Segmentation, Paging, Fragmentation, Compaction")
                .correctOption("A")
                .explanation("According to Coffman (1971), all four conditions must hold simultaneously for a deadlock to exist: 1) Mutual Exclusion; 2) Hold and Wait; 3) No Preemption; 4) Circular Wait.")
                .companyTags("Amazon,Microsoft,Google,TCS,Infosys")
                .build());

        qList.add(Question.builder()
                .title("What is Thrashing in an operating system?")
                .description("Virtual memory and paging performance degradation.")
                .topic(os).category("OS").difficulty("MEDIUM")
                .optionA("A state where CPU spends more time swapping pages in and out of memory than executing actual process instructions.")
                .optionB("When multiple processes terminate abnormally due to segmentation fault.")
                .optionC("When the disk read/write head crashes physically.")
                .optionD("The process of defragmenting physical RAM.")
                .correctOption("A")
                .explanation("Thrashing occurs when the working set of active processes exceeds physical memory. The OS continuously experiences page faults, causing high disk I/O and near-zero CPU utilization.")
                .companyTags("Amazon,Microsoft,TCS,Cognizant")
                .build());

        qList.add(Question.builder()
                .title("What is the primary difference between a Process and a Thread?")
                .description("Process vs Thread memory space and scheduling.")
                .topic(os).category("OS").difficulty("EASY")
                .optionA("Processes share memory address space; threads have separate address space.")
                .optionB("A process is an execution unit with its own address space; threads share the process's code, data, and resources but have their own stack and registers.")
                .optionC("Threads cannot be scheduled by the OS kernel.")
                .optionD("Creating a process is faster and lighter than creating a thread.")
                .correctOption("B")
                .explanation("A process is an independent program in execution with isolated memory. Threads (lightweight processes) exist within a process and share memory and file descriptors, making context switching between threads much faster.")
                .companyTags("Amazon,Google,Microsoft,Infosys,TCS")
                .build());

        // 7. COMPUTER NETWORKS
        qList.add(Question.builder()
                .title("What happens under the hood during the TCP 3-Way Handshake?")
                .description("Transport layer connection establishment protocol.")
                .topic(networks).category("NETWORKS").difficulty("MEDIUM")
                .optionA("Client sends PING, Server sends PONG, Client sends OK.")
                .optionB("Client sends SYN (seq=x), Server responds with SYN-ACK (seq=y, ack=x+1), Client responds with ACK (ack=y+1).")
                .optionC("Client sends FIN, Server sends FIN-ACK, Client sends RST.")
                .optionD("Server sends SYN, Client responds with ACK, Connection is established.")
                .correctOption("B")
                .explanation("To establish a reliable TCP socket connection: 1) Client sends SYN packet with initial sequence number x; 2) Server replies with SYN-ACK with its own sequence number y and acknowledging x+1; 3) Client sends ACK for y+1. The connection transitions to ESTABLISHED.")
                .companyTags("Amazon,Google,Microsoft,Infosys")
                .build());

        qList.add(Question.builder()
                .title("Which OSI layer is responsible for end-to-end communication and port addressing?")
                .description("OSI 7-layer architecture.")
                .topic(networks).category("NETWORKS").difficulty("EASY")
                .optionA("Network Layer (Layer 3)").optionB("Transport Layer (Layer 4)").optionC("Data Link Layer (Layer 2)").optionD("Session Layer (Layer 5)")
                .correctOption("B")
                .explanation("The Transport Layer (Layer 4 - TCP, UDP) provides process-to-process delivery using port numbers and handles segmentation, flow control, and error recovery.")
                .companyTags("TCS,Infosys,Wipro,Accenture")
                .build());

        qList.add(Question.builder()
                .title("What is the difference between HTTP and HTTPS?")
                .description("Web protocol security with TLS/SSL encryption.")
                .topic(networks).category("NETWORKS").difficulty("EASY")
                .optionA("HTTP uses port 443; HTTPS uses port 80.")
                .optionB("HTTPS encrypts all traffic using TLS/SSL over port 443, preventing eavesdropping and tampering; HTTP transmits plain text over port 80.")
                .optionC("HTTPS is a binary protocol; HTTP is text only.")
                .optionD("HTTPS does not support cookies or sessions.")
                .correctOption("B")
                .explanation("HTTPS runs HTTP over Transport Layer Security (TLS), ensuring confidentiality through symmetric/asymmetric encryption, data integrity via hashing, and authentication via digital certificates.")
                .companyTags("TCS,Accenture,Infosys,Amazon")
                .build());

        // 8. DATA STRUCTURES & ALGORITHMS (Practice & MCQ)
        qList.add(Question.builder()
                .title("Two Sum: Find two numbers in an array that add up to a target")
                .description("Given an array of integers nums and an integer target, return indices of the two numbers such that they add up to target. You may assume that each input would have exactly one solution, and you may not use the same element twice.")
                .topic(dsa).category("DSA").difficulty("EASY")
                .questionType("CODE_PRACTICE")
                .optionA("O(N^2) using nested loops")
                .optionB("O(N) using a Hash Map storing value-to-index pairs")
                .optionC("O(N log N) using binary search without space")
                .optionD("O(1) using bit manipulation")
                .correctOption("B")
                .explanation("The optimal approach uses a single-pass HashMap. For each number x, check if (target - x) is already in the map. If yes, return current index and map.get(target - x). Time Complexity: O(N), Space Complexity: O(N).")
                .companyTags("Amazon,Google,Microsoft,TCS,Infosys")
                .externalLink("https://leetcode.com/problems/two-sum/")
                .constraints("2 <= nums.length <= 10^4\n-10^9 <= nums[i] <= 10^9\n-10^9 <= target <= 10^9")
                .sampleInput("nums = [2,7,11,15], target = 9")
                .sampleOutput("[0,1]")
                .codeSnippet("class Solution {\n    public int[] twoSum(int[] nums, int target) {\n        Map<Integer, Integer> map = new HashMap<>();\n        for (int i = 0; i < nums.length; i++) {\n            int complement = target - nums[i];\n            if (map.containsKey(complement)) {\n                return new int[] { map.get(complement), i };\n            }\n            map.put(nums[i], i);\n        }\n        return new int[]{};\n    }\n}")
                .build());

        qList.add(Question.builder()
                .title("Longest Substring Without Repeating Characters")
                .description("Given a string s, find the length of the longest substring without duplicate characters.")
                .topic(dsa).category("DSA").difficulty("MEDIUM")
                .questionType("CODE_PRACTICE")
                .optionA("O(N^3) checking all substrings")
                .optionB("O(N) using sliding window with two pointers and a character index map")
                .optionC("O(N log N) using divide and conquer")
                .optionD("O(2^N) using recursion")
                .correctOption("B")
                .explanation("Sliding Window Pattern: Maintain left and right pointers. Track the last seen index of each character. When a duplicate character is seen at right, update left = Math.max(left, map.get(char) + 1). Result is max(maxLen, right - left + 1). Time: O(N), Space: O(min(m, n)).")
                .companyTags("Amazon,Google,Microsoft")
                .externalLink("https://leetcode.com/problems/longest-substring-without-repeating-characters/")
                .constraints("0 <= s.length <= 5 * 10^4\ns consists of English letters, digits, symbols and spaces.")
                .sampleInput("s = \"abcabcbb\"")
                .sampleOutput("3 (The answer is \"abc\", with the length of 3)")
                .codeSnippet("class Solution {\n    public int lengthOfLongestSubstring(String s) {\n        int n = s.length(), maxLen = 0;\n        Map<Character, Integer> map = new HashMap<>();\n        for (int r = 0, l = 0; r < n; r++) {\n            char c = s.charAt(r);\n            if (map.containsKey(c)) {\n                l = Math.max(l, map.get(c) + 1);\n            }\n            map.put(c, r);\n            maxLen = Math.max(maxLen, r - l + 1);\n        }\n        return maxLen;\n    }\n}")
                .build());

        qList.add(Question.builder()
                .title("Reverse a Linked List")
                .description("Given the head of a singly linked list, reverse the list, and return the reversed list.")
                .topic(dsa).category("DSA").difficulty("EASY")
                .questionType("CODE_PRACTICE")
                .optionA("O(N) time and O(N) auxiliary space using recursion or stack")
                .optionB("O(N) time and O(1) space iteratively using three pointers (prev, curr, next)")
                .optionC("O(N^2) time swapping values")
                .optionD("Cannot be reversed in linear time")
                .correctOption("B")
                .explanation("Initialize prev = null, curr = head. In a loop while curr != null: save next = curr.next; set curr.next = prev; advance prev = curr; curr = next. Return prev as new head. Time: O(N), Space: O(1).")
                .companyTags("Amazon,Microsoft,TCS,Infosys")
                .externalLink("https://leetcode.com/problems/reverse-linked-list/")
                .constraints("The number of nodes in the list is the range [0, 5000].\n-5000 <= Node.val <= 5000")
                .sampleInput("head = [1,2,3,4,5]")
                .sampleOutput("[5,4,3,2,1]")
                .codeSnippet("class Solution {\n    public ListNode reverseList(ListNode head) {\n        ListNode prev = null;\n        ListNode curr = head;\n        while (curr != null) {\n            ListNode next = curr.next;\n            curr.next = prev;\n            prev = curr;\n            curr = next;\n        }\n        return prev;\n    }\n}")
                .build());

        qList.add(Question.builder()
                .title("Binary Search: Search in a Rotated Sorted Array")
                .description("Given the array nums after the possible rotation and an integer target, return the index of target if it is in nums, or -1 if it is not in nums. You must write an algorithm with O(log n) runtime complexity.")
                .topic(dsa).category("DSA").difficulty("MEDIUM")
                .questionType("CODE_PRACTICE")
                .optionA("Linear scan in O(N)")
                .optionB("Modified Binary Search identifying which half (left or right) is normally sorted in O(log N)")
                .optionC("Sorting the array first in O(N log N)")
                .optionD("Hashing in O(N) space")
                .correctOption("B")
                .explanation("At least one half of a rotated sorted array is always sorted. Check if nums[mid] == target. If nums[low] <= nums[mid], the left half is sorted: check if target lies between nums[low] and nums[mid]. Otherwise, right half is sorted: check if target lies between nums[mid] and nums[high]. Time: O(log N), Space: O(1).")
                .companyTags("Amazon,Google,Microsoft")
                .externalLink("https://leetcode.com/problems/search-in-rotated-sorted-array/")
                .constraints("1 <= nums.length <= 5000\n-10^4 <= nums[i] <= 10^4\nAll values of nums are unique.")
                .sampleInput("nums = [4,5,6,7,0,1,2], target = 0")
                .sampleOutput("4")
                .build());

        qList.add(Question.builder()
                .title("Lowest Common Ancestor of a Binary Tree")
                .description("Given a binary tree, find the lowest common ancestor (LCA) of two given nodes in the tree.")
                .topic(dsa).category("DSA").difficulty("MEDIUM")
                .questionType("CODE_PRACTICE")
                .optionA("O(N^2) inspecting heights of each node")
                .optionB("O(N) post-order DFS traversal returning non-null when node p or q is discovered")
                .optionC("Breadth first search storing parents with O(N!) space")
                .optionD("Topological sort on DAG")
                .correctOption("B")
                .explanation("Post-order DFS: If root == null or root == p or root == q, return root. Recursively search left and right subtrees. If both left and right return non-null, root is the LCA. If only one returns non-null, propagate that node up. Time: O(N), Space: O(H).")
                .companyTags("Amazon,Microsoft,Google")
                .externalLink("https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/")
                .constraints("The number of nodes in the tree is in the range [2, 10^5].\nAll Node.val are unique.\np != q")
                .sampleInput("root = [3,5,1,6,2,0,8,null,null,7,4], p = 5, q = 1")
                .sampleOutput("3")
                .build());

        qList.add(Question.builder()
                .title("Climbing Stairs (Dynamic Programming)")
                .description("You are climbing a staircase. It takes n steps to reach the top. Each time you can either climb 1 or 2 steps. In how many distinct ways can you climb to the top?")
                .topic(dsa).category("DSA").difficulty("EASY")
                .questionType("CODE_PRACTICE")
                .optionA("O(2^N) exponential recursion")
                .optionB("O(N) time and O(1) space using Fibonacci state transition: dp[i] = dp[i-1] + dp[i-2]")
                .optionC("O(N log N) using divide and conquer")
                .optionD("O(N^2) dynamic programming table")
                .correctOption("B")
                .explanation("To reach step n, you can either step from (n-1) or (n-2). Ways(n) = Ways(n-1) + Ways(n-2). This is identical to the Fibonacci sequence! We only need two variables (prev1 and prev2) to compute in O(N) time and O(1) space.")
                .companyTags("Amazon,Google,TCS,Infosys")
                .externalLink("https://leetcode.com/problems/climbing-stairs/")
                .constraints("1 <= n <= 45")
                .sampleInput("n = 3")
                .sampleOutput("3 (1+1+1, 1+2, 2+1)")
                .build());

        qList.add(Question.builder()
                .title("Coin Change: Minimum coins to make up an amount")
                .description("You are given an integer array coins representing coins of different denominations and an integer amount representing a total amount of money. Return the fewest number of coins that you need to make up that amount.")
                .topic(dsa).category("DSA").difficulty("MEDIUM")
                .questionType("CODE_PRACTICE")
                .optionA("Greedy algorithm picking largest coin first always gives optimal solution.")
                .optionB("Dynamic Programming: dp[i] = min(dp[i], dp[i - coin] + 1) in O(amount * len(coins)) time.")
                .optionC("Binary Search on answer")
                .optionD("Depth First Search without memoization")
                .correctOption("B")
                .explanation("Greedy fails for arbitrary coin systems (e.g., coins [1, 3, 4], amount 6: greedy picks 4+1+1 = 3 coins, but optimal is 3+3 = 2 coins). DP bottom-up table dp[0..amount] initialized to infinity, dp[0]=0, finds minimum coins efficiently in O(amount * n) time.")
                .companyTags("Amazon,Google,Microsoft")
                .externalLink("https://leetcode.com/problems/coin-change/")
                .constraints("1 <= coins.length <= 12\n1 <= coins[i] <= 2^31 - 1\n0 <= amount <= 10^4")
                .sampleInput("coins = [1,2,5], amount = 11")
                .sampleOutput("3 (11 = 5 + 5 + 1)")
                .build());

        qList.add(Question.builder()
                .title("Number of Islands (Graph BFS/DFS)")
                .description("Given an m x n 2D binary grid grid which represents a map of '1's (land) and '0's (water), return the number of islands. An island is surrounded by water and is formed by connecting adjacent lands horizontally or vertically.")
                .topic(dsa).category("DSA").difficulty("MEDIUM")
                .questionType("CODE_PRACTICE")
                .optionA("O(M * N) using DFS or BFS to traverse connected components of '1's and marking visited cells.")
                .optionB("O((M * N)^2) using Dijkstra's shortest path")
                .optionC("O(M + N) using two pointers")
                .optionD("Greedy counting of '1' cells")
                .correctOption("A")
                .explanation("Iterate through every cell in the grid. When a '1' is encountered, increment islandCount and trigger a DFS/BFS traversal that sinks/marks all connected adjacent '1's to '0' (or visited). Time Complexity: O(M * N), Space: O(M * N) for recursion stack.")
                .companyTags("Amazon,Google,Microsoft")
                .externalLink("https://leetcode.com/problems/number-of-islands/")
                .constraints("m == grid.length\nn == grid[i].length\n1 <= m, n <= 300\ngrid[i][j] is '0' or '1'.")
                .sampleInput("grid = [[\"1\",\"1\",\"0\",\"0\",\"0\"],[\"1\",\"1\",\"0\",\"0\",\"0\"],[\"0\",\"0\",\"1\",\"0\",\"0\"],[\"0\",\"0\",\"0\",\"1\",\"1\"]]")
                .sampleOutput("3")
                .build());

        qList.addAll(DsaPracticeCatalog.createQuestions(dsa));
        return questionRepository.saveAll(qList);
    }

    private void seedMissingDsaQuestions() {
        long existingCount = questionRepository.countByCategoryIgnoreCase("DSA");
        if (existingCount >= DsaPracticeCatalog.TARGET_SIZE) {
            return;
        }

        Topic topic = topicRepository.findBySlug("dsa").orElse(null);
        if (topic == null) {
            log.warn("Skipping DSA catalog seeding because the DSA topic has not been initialized.");
            return;
        }

        Set<String> existingTitles = new HashSet<>();
        questionRepository.findByCategoryIgnoreCase("DSA").stream()
                .map(Question::getTitle)
                .forEach(existingTitles::add);

        List<Question> missingQuestions = DsaPracticeCatalog.createQuestions(topic).stream()
                .filter(question -> !existingTitles.contains(question.getTitle()))
                .limit(DsaPracticeCatalog.TARGET_SIZE - existingCount)
                .toList();

        if (!missingQuestions.isEmpty()) {
            questionRepository.saveAll(missingQuestions);
            log.info("Seeded {} DSA practice questions; total DSA question count is now {}.",
                    missingQuestions.size(), existingCount + missingQuestions.size());
        }
    }

    private void syncDsaMockTest() {
        Test dsaTest = testRepository.findBySlug("dsa-sprint").orElse(null);
        if (dsaTest == null) {
            dsaTest = testRepository.save(Test.builder()
                    .title("DSA Problem Solving & Complexity Sprint")
                    .slug("dsa-sprint")
                    .description("A timed assessment sampling Arrays, Strings, Linked Lists, Trees, Graphs, and Dynamic Programming.")
                    .category("DSA")
                    .durationMinutes(30)
                    .totalQuestions(15)
                    .totalMarks(30)
                    .passingMarks(18)
                    .difficulty("HARD")
                    .build());
        }

        List<TestQuestion> existingQuestions =
                testQuestionRepository.findByTestIdOrderByQuestionOrderAsc(dsaTest.getId());
        if (existingQuestions.size() > 15) {
            testQuestionRepository.deleteAll(existingQuestions.subList(15, existingQuestions.size()));
            existingQuestions = new ArrayList<>(existingQuestions.subList(0, 15));
        }

        Set<Long> assignedQuestionIds = new HashSet<>();
        existingQuestions.forEach(testQuestion -> assignedQuestionIds.add(testQuestion.getQuestion().getId()));

        List<Question> dsaQuestions = questionRepository.findByCategoryIgnoreCase("DSA");
        int nextOrder = existingQuestions.stream()
                .mapToInt(TestQuestion::getQuestionOrder)
                .max()
                .orElse(0) + 1;
        List<TestQuestion> additions = new ArrayList<>();
        for (Question question : dsaQuestions) {
            if (existingQuestions.size() + additions.size() >= 15) {
                break;
            }
            if (assignedQuestionIds.add(question.getId())) {
                additions.add(TestQuestion.builder()
                        .test(dsaTest)
                        .question(question)
                        .questionOrder(nextOrder++)
                        .marks(2)
                        .build());
            }
        }

        if (!additions.isEmpty()) {
            testQuestionRepository.saveAll(additions);
        }

        dsaTest.setTotalQuestions(Math.min(15, existingQuestions.size() + additions.size()));
        dsaTest.setTotalMarks(dsaTest.getTotalQuestions() * 2);
        dsaTest.setPassingMarks(Math.min(18, dsaTest.getTotalMarks()));
        testRepository.save(dsaTest);
        log.info("DSA mock test is configured with {} DSA questions.", dsaTest.getTotalQuestions());
    }

    private void seedAptitudeAndCoreQuestionBanks() {
        Topic aptitudeTopic = topicRepository.findBySlug("quant")
                .orElseThrow(() -> new IllegalStateException("The quantitative aptitude topic is required for mock question seeding."));

        List<Question> aptitudeQuestions = questionRepository.findByCategoryIgnoreCase("APTITUDE");
        if (aptitudeQuestions.size() < MockAssessmentCatalog.APTITUDE_BANK_SIZE) {
            Set<String> existingTitles = aptitudeQuestions.stream()
                    .map(Question::getTitle)
                    .collect(java.util.stream.Collectors.toSet());
            List<Question> additions = MockAssessmentCatalog.createAptitudeQuestions(
                            aptitudeTopic, 1, MockAssessmentCatalog.APTITUDE_BANK_SIZE)
                    .stream()
                    .filter(question -> !existingTitles.contains(question.getTitle()))
                    .limit(MockAssessmentCatalog.APTITUDE_BANK_SIZE - aptitudeQuestions.size())
                    .toList();
            questionRepository.saveAll(additions);
            log.info("Seeded {} aptitude questions.", additions.size());
        } else if (aptitudeQuestions.size() > MockAssessmentCatalog.APTITUDE_BANK_SIZE) {
            log.warn("Found {} aptitude questions; the requested bank size is {}.",
                    aptitudeQuestions.size(), MockAssessmentCatalog.APTITUDE_BANK_SIZE);
        }

        long coreQuestionCount = MockAssessmentCatalog.CORE_CATEGORIES.stream()
                .mapToLong(questionRepository::countByCategoryIgnoreCase)
                .sum();
        if (coreQuestionCount < MockAssessmentCatalog.CORE_BANK_SIZE) {
            Map<String, Topic> coreTopics = Map.of(
                    "JAVA_OOPS", topicRepository.findBySlug("java-oops")
                            .orElseThrow(() -> new IllegalStateException("The Java & OOP topic is required for mock question seeding.")),
                    "DBMS", topicRepository.findBySlug("dbms")
                            .orElseThrow(() -> new IllegalStateException("The DBMS topic is required for mock question seeding.")),
                    "OS", topicRepository.findBySlug("os")
                            .orElseThrow(() -> new IllegalStateException("The operating systems topic is required for mock question seeding.")),
                    "NETWORKS", topicRepository.findBySlug("networks")
                            .orElseThrow(() -> new IllegalStateException("The computer networks topic is required for mock question seeding."))
            );
            Set<String> existingTitles = MockAssessmentCatalog.CORE_CATEGORIES.stream()
                    .map(questionRepository::findByCategoryIgnoreCase)
                    .flatMap(List::stream)
                    .map(Question::getTitle)
                    .collect(java.util.stream.Collectors.toSet());
            List<Question> additions = MockAssessmentCatalog.createCoreQuestions(
                            coreTopics, 1, MockAssessmentCatalog.CORE_BANK_SIZE)
                    .stream()
                    .filter(question -> !existingTitles.contains(question.getTitle()))
                    .limit(MockAssessmentCatalog.CORE_BANK_SIZE - (int) coreQuestionCount)
                    .toList();
            questionRepository.saveAll(additions);
            log.info("Seeded {} CSE core questions.", additions.size());
        } else if (coreQuestionCount > MockAssessmentCatalog.CORE_BANK_SIZE) {
            log.warn("Found {} CSE core questions; the requested bank size is {}.",
                    coreQuestionCount, MockAssessmentCatalog.CORE_BANK_SIZE);
        }
    }

    private void syncAptitudeCoreMockTests() {
        List<Question> aptitudeQuestions = questionRepository.findByCategoryIgnoreCase("APTITUDE").stream()
                .sorted(Comparator.comparing(Question::getId))
                .toList();
        List<List<Question>> coreQuestionsByCategory = MockAssessmentCatalog.CORE_CATEGORIES.stream()
                .map(questionRepository::findByCategoryIgnoreCase)
                .map(questions -> questions.stream().sorted(Comparator.comparing(Question::getId)).toList())
                .toList();

        if (aptitudeQuestions.size() < MockAssessmentCatalog.MOCK_SECTION_BANK_SIZE
                || coreQuestionsByCategory.stream().mapToInt(List::size).sum() < MockAssessmentCatalog.MOCK_SECTION_BANK_SIZE) {
            throw new IllegalStateException("Cannot create the ten Aptitude + CSE mock tests without 100 questions in each bank.");
        }

        List<Question> coreQuestions = new ArrayList<>(MockAssessmentCatalog.MOCK_SECTION_BANK_SIZE);
        for (int questionIndex = 0; coreQuestions.size() < MockAssessmentCatalog.MOCK_SECTION_BANK_SIZE; questionIndex++) {
            for (List<Question> categoryQuestions : coreQuestionsByCategory) {
                if (questionIndex < categoryQuestions.size()) {
                    coreQuestions.add(categoryQuestions.get(questionIndex));
                }
                if (coreQuestions.size() == MockAssessmentCatalog.MOCK_SECTION_BANK_SIZE) {
                    break;
                }
            }
        }

        for (int testNumber = 1; testNumber <= MockAssessmentCatalog.MOCK_TEST_COUNT; testNumber++) {
            String slug = "aptitude-cse-mock-" + testNumber;
            Test test = testRepository.findBySlug(slug).orElseGet(() -> Test.builder().slug(slug).build());
            test.setTitle(String.format(Locale.ROOT, "Placify Mock Test %02d", testNumber));
            test.setDescription("A timed placement assessment with 10 Aptitude and 10 CSE Core questions. Hints are available during the test; solutions are shown after submission.");
            test.setCompany(null);
            test.setCategory("PLACEMENT_MOCK");
            test.setDurationMinutes(30);
            test.setTotalQuestions(MockAssessmentCatalog.QUESTIONS_PER_TEST);
            test.setTotalMarks(MockAssessmentCatalog.QUESTIONS_PER_TEST * 2);
            test.setPassingMarks(24);
            test.setDifficulty(testNumber <= 3 ? "EASY" : testNumber <= 7 ? "MEDIUM" : "HARD");
            test.setActive(true);
            test = testRepository.save(test);

            List<TestQuestion> testQuestions = new ArrayList<>(MockAssessmentCatalog.QUESTIONS_PER_TEST);
            int aptitudeStart = (testNumber - 1) * MockAssessmentCatalog.QUESTIONS_PER_SECTION;
            int coreStart = (testNumber - 1) * MockAssessmentCatalog.QUESTIONS_PER_SECTION;
            for (int questionIndex = 0; questionIndex < MockAssessmentCatalog.QUESTIONS_PER_SECTION; questionIndex++) {
                testQuestions.add(TestQuestion.builder()
                        .test(test)
                        .question(aptitudeQuestions.get(aptitudeStart + questionIndex))
                        .questionOrder(questionIndex * 2 + 1)
                        .marks(2)
                        .build());
                testQuestions.add(TestQuestion.builder()
                        .test(test)
                        .question(coreQuestions.get(coreStart + questionIndex))
                        .questionOrder(questionIndex * 2 + 2)
                        .marks(2)
                        .build());
            }

            List<TestQuestion> existingQuestions =
                    testQuestionRepository.findByTestIdOrderByQuestionOrderAsc(test.getId());
            boolean assignmentsMatch = existingQuestions.size() == testQuestions.size();
            if (assignmentsMatch) {
                for (int questionIndex = 0; questionIndex < testQuestions.size(); questionIndex++) {
                    TestQuestion existing = existingQuestions.get(questionIndex);
                    TestQuestion desired = testQuestions.get(questionIndex);
                    if (!existing.getQuestion().getId().equals(desired.getQuestion().getId())
                            || existing.getQuestionOrder() != desired.getQuestionOrder()
                            || existing.getMarks() != desired.getMarks()) {
                        assignmentsMatch = false;
                        break;
                    }
                }
            }

            if (!assignmentsMatch) {
                testQuestionRepository.deleteByTestId(test.getId());
                testQuestionRepository.flush();
                testQuestionRepository.saveAll(testQuestions);
            }
        }
        log.info("Configured {} placement mock tests with 10 aptitude and 10 CSE core questions each.",
                MockAssessmentCatalog.MOCK_TEST_COUNT);
    }

    private void seedMockTests(Map<String, Company> companies, List<Question> questions) {
        Company tcs = companies.get("TCS");
        Company amazon = companies.get("AMAZON");
        Company infosys = companies.get("INFOSYS");

        Test tcsTest = Test.builder()
                .title("TCS NQT National Qualifier Comprehensive Mock Test")
                .slug("tcs-nqt-mock")
                .description("Official pattern simulated assessment covering Numerical Ability, Logical Reasoning, Verbal English, and Core CS Fundamentals.")
                .company(tcs)
                .category("COMPANY_MOCK")
                .durationMinutes(30)
                .totalQuestions(12)
                .totalMarks(24)
                .passingMarks(14)
                .difficulty("MEDIUM")
                .build();

        Test amazonTest = Test.builder()
                .title("Amazon SDE Technical Aptitude & Core CS Assessment")
                .slug("amazon-sde-cs-mock")
                .description("High-bar assessment focusing on Data Structures, Algorithms, OS, DBMS, Networks, and OOP Architecture principles.")
                .company(amazon)
                .category("COMPANY_MOCK")
                .durationMinutes(35)
                .totalQuestions(12)
                .totalMarks(24)
                .passingMarks(16)
                .difficulty("HARD")
                .build();

        Test infyTest = Test.builder()
                .title("Infosys HackWithInfy & Specialist Programmer Mock")
                .slug("infosys-mock")
                .description("Curated test covering advanced Java & OOPs, SQL queries, pseudocode logic, and algorithmic reasoning.")
                .company(infosys)
                .category("COMPANY_MOCK")
                .durationMinutes(25)
                .totalQuestions(10)
                .totalMarks(20)
                .passingMarks(12)
                .difficulty("MEDIUM")
                .build();

        Test csCoreTest = Test.builder()
                .title("Computer Science Core Sprint: OS, DBMS & Networks")
                .slug("cs-core-sprint")
                .description("Mastery mock test evaluating essential CS subjects tested across product and service company interviews.")
                .company(null)
                .category("TECHNICAL_CS")
                .durationMinutes(25)
                .totalQuestions(10)
                .totalMarks(20)
                .passingMarks(12)
                .difficulty("MEDIUM")
                .build();

        Test dsaTest = Test.builder()
                .title("DSA Problem Solving & Complexity Sprint")
                .slug("dsa-sprint")
                .description("Speed and conceptual challenge on Arrays, Binary Search, Linked Lists, Trees, and Dynamic Programming.")
                .company(null)
                .category("DSA")
                .durationMinutes(30)
                .totalQuestions(15)
                .totalMarks(30)
                .passingMarks(18)
                .difficulty("HARD")
                .build();

        testRepository.saveAll(List.of(tcsTest, amazonTest, infyTest, csCoreTest, dsaTest));

        // Create TestQuestions associations
        List<TestQuestion> tqList = new ArrayList<>();

        // Add questions to TCS Test

        // Add questions to TCS Test
        int order = 1;
        for (Question q : questions) {
            if (order <= 12) {
                tqList.add(TestQuestion.builder().test(tcsTest).question(q).questionOrder(order++).marks(2).build());
            }
        }

        // Add questions to Amazon Test (prefer DSA, OS, DBMS, Networks)
        order = 1;
        for (Question q : questions) {
            if (q.getCategory().equals("DSA") || q.getCategory().equals("OS") || q.getCategory().equals("DBMS") || q.getCategory().equals("JAVA_OOPS") || q.getCategory().equals("NETWORKS")) {
                if (order <= 12) {
                    tqList.add(TestQuestion.builder().test(amazonTest).question(q).questionOrder(order++).marks(2).build());
                }
            }
        }

        // Add questions to Infosys Test
        order = 1;
        for (Question q : questions) {
            if (q.getCategory().equals("JAVA_OOPS") || q.getCategory().equals("APTITUDE") || q.getCategory().equals("REASONING") || q.getCategory().equals("DBMS")) {
                if (order <= 10) {
                    tqList.add(TestQuestion.builder().test(infyTest).question(q).questionOrder(order++).marks(2).build());
                }
            }
        }

        // Add to CS Core Test
        order = 1;
        for (Question q : questions) {
            if (q.getCategory().equals("OS") || q.getCategory().equals("DBMS") || q.getCategory().equals("NETWORKS")) {
                if (order <= 10) {
                    tqList.add(TestQuestion.builder().test(csCoreTest).question(q).questionOrder(order++).marks(2).build());
                }
            }
        }

        // Add to DSA Test
        order = 1;
        for (Question q : questions) {
            if (q.getCategory().equals("DSA")) {
                if (order <= 15) {
                    tqList.add(TestQuestion.builder().test(dsaTest).question(q).questionOrder(order++).marks(2).build());
                }
            }
        }

        testQuestionRepository.saveAll(tqList);
    }
}