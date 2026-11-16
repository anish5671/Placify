package com.preptrack.seeder;

import com.preptrack.question.Question;
import com.preptrack.question.Topic;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

final class MockAssessmentCatalog {

    static final int APTITUDE_BANK_SIZE = 300;
    static final int CORE_BANK_SIZE = 150;
    static final int MOCK_SECTION_BANK_SIZE = 100;
    static final int MOCK_TEST_COUNT = 10;
    static final int QUESTIONS_PER_SECTION = 10;
    static final int QUESTIONS_PER_TEST = 20;
    static final List<String> CORE_CATEGORIES = List.of("JAVA_OOPS", "DBMS", "OS", "NETWORKS");

    private static final List<CoreFact> JAVA_FACTS = List.of(
            fact("Which keyword prevents a class from being subclassed?", "final", "A final class cannot be extended."),
            fact("What is method overloading in Java?", "Same method name with different parameter lists", "Overloading is resolved at compile time using the method signature."),
            fact("What is method overriding in Java?", "A subclass provides a new implementation of an inherited method", "Overriding replaces inherited behavior for a matching method signature."),
            fact("Which Java type supports multiple inheritance of type?", "An interface", "A class can implement multiple interfaces."),
            fact("Which collection stores unique elements without guaranteeing iteration order?", "HashSet", "HashSet uses hashing and does not guarantee element order."),
            fact("Which collection is backed by a resizable array?", "ArrayList", "ArrayList provides indexed access using a dynamically resized array."),
            fact("Which Java type is immutable and suitable for shared string literals?", "String", "String objects cannot be changed after they are created."),
            fact("What must equal objects return from hashCode()?", "The same hash code", "Equal objects must have equal hash codes for hash-based collections to work."),
            fact("When is a checked exception validated?", "At compile time", "The compiler requires checked exceptions to be caught or declared."),
            fact("What does try-with-resources close automatically?", "Resources that implement AutoCloseable", "The language closes declared resources when the try block exits."),
            fact("What does volatile guarantee for a shared variable?", "Visibility of writes between threads", "Volatile provides visibility but does not make compound operations atomic."),
            fact("What does synchronized provide for a critical section?", "Mutual exclusion and memory visibility", "Only one thread at a time can hold the same monitor."),
            fact("What does a static field belong to?", "The class", "A static member is associated with the class rather than each instance."),
            fact("What is the name of a Java constructor?", "The same name as its class", "A constructor has the class name and declares no return type."),
            fact("Can an abstract Java class be instantiated directly?", "No", "An abstract class may be incomplete and must be subclassed."),
            fact("What does this refer to inside an instance method?", "The current object", "The this reference identifies the current instance."),
            fact("What does super refer to in a subclass?", "The superclass", "The super keyword accesses superclass members and constructors."),
            fact("Where can a private member be accessed directly?", "Within the declaring class", "Private access is restricted to the declaring class."),
            fact("What does == compare for two object references?", "Whether the references identify the same object", "Use equals() to compare logical object equality when it is implemented."),
            fact("What does the JVM execute?", "Java bytecode", "The Java compiler produces bytecode that the JVM executes."),
            fact("Which Java distribution includes development tools such as javac?", "JDK", "The JDK includes the runtime and development tools."),
            fact("Which class provides a mutable string builder without synchronization?", "StringBuilder", "StringBuilder is mutable and is not synchronized."),
            fact("Which map is designed for concurrent access by multiple threads?", "ConcurrentHashMap", "ConcurrentHashMap supports thread-safe concurrent map operations."),
            fact("What is the return type of a Java constructor?", "A constructor has no return type", "Constructors are not methods and do not declare a return type."),
            fact("Which access modifier allows access from the same package and subclasses?", "protected", "Protected members are accessible within the package and through subclass access."),
            fact("Which method should be overridden when defining logical equality?", "equals(Object)", "The equals method defines logical equality; hashCode should also be consistent with it."),
            fact("Which Java construct guarantees cleanup when a resource leaves scope?", "try-with-resources", "Try-with-resources closes AutoCloseable resources when the block exits."),
            fact("What does an enum provide?", "A type-safe fixed set of named constants", "An enum defines a restricted set of instances known at compile time."),
            fact("What is a functional interface?", "An interface with exactly one abstract method", "A functional interface can be implemented with a lambda expression."),
            fact("What does a Java lambda expression represent?", "An implementation of a functional interface", "A lambda supplies the implementation of a functional interface's abstract method."),
            fact("What is the purpose of the static factory method valueOf on enums?", "Convert a matching name into an enum constant", "Enum.valueOf returns the constant whose declared name matches the supplied string."),
            fact("What does an unmodifiable collection prevent?", "Changes through that collection reference", "An unmodifiable view rejects mutation operations through the view."),
            fact("Which collection preserves insertion order while preventing duplicate elements?", "LinkedHashSet", "LinkedHashSet combines uniqueness with predictable insertion-order iteration."),
            fact("What does the Comparable interface define?", "A type's natural ordering", "Comparable provides compareTo for the type's natural ordering."),
            fact("What does a Java record primarily provide?", "A concise immutable data-carrier class", "Records generate common data-carrier members from their components."),
            fact("What is the purpose of the volatile keyword?", "Make writes visible across threads", "Volatile provides visibility guarantees but does not make compound actions atomic."),
            fact("Which executor service method submits a task and returns a Future?", "submit", "submit schedules a task and returns a Future representing its result."),
            fact("What does a generic type parameter provide?", "Compile-time type safety for reusable code", "Generics allow classes and methods to operate on types while checking usage at compile time.")
    );

    private static final List<CoreFact> DBMS_FACTS = List.of(
            fact("What is the main purpose of a primary key?", "Uniquely identify each row and disallow NULL", "A primary key uniquely identifies each row in a table."),
            fact("What does a foreign key enforce?", "Referential integrity between related tables", "A foreign key requires referenced values to match a key in the related table."),
            fact("Why is database normalization used?", "To reduce avoidable data duplication and update anomalies", "Normalization organizes data to reduce redundancy and modification anomalies."),
            fact("What does first normal form require for each field?", "Each field contains an atomic value", "First normal form disallows repeating groups and multi-valued fields."),
            fact("What does second normal form eliminate?", "Partial dependency on part of a candidate key", "Second normal form requires first normal form and removes partial dependencies."),
            fact("What does third normal form eliminate?", "Transitive dependency of non-key attributes on a key", "Third normal form removes transitive dependencies between non-key attributes."),
            fact("What does atomicity mean in ACID?", "A transaction happens completely or not at all", "Atomicity prevents a transaction from being partially applied."),
            fact("What does consistency mean in ACID?", "A transaction preserves database rules and constraints", "A committed transaction leaves the database in a valid state."),
            fact("What does isolation provide in ACID?", "Concurrent transactions behave without improper interference", "Isolation controls how concurrent transactions observe one another."),
            fact("What does durability mean in ACID?", "Committed changes survive a system failure", "Durability preserves committed data after a crash or restart."),
            fact("What is a common purpose of a database index?", "Speed up data lookup at the cost of storage and write work", "An index can reduce lookup time but must be maintained on writes."),
            fact("What does a LEFT JOIN return?", "Every left-table row and matching right-table rows", "Unmatched rows from the left table are retained with NULL right-side values."),
            fact("What does GROUP BY do in an aggregate query?", "Groups rows that share values in selected columns", "GROUP BY forms groups for aggregate calculations."),
            fact("Which clause filters groups after aggregation?", "HAVING", "HAVING filters grouped results, usually using aggregate expressions."),
            fact("Which clause filters rows before grouping?", "WHERE", "WHERE filters input rows before GROUP BY is applied."),
            fact("What does COUNT(*) count?", "Rows in the selected result set", "COUNT(*) counts rows regardless of NULL values in individual columns."),
            fact("What does DISTINCT do in a SELECT result?", "Removes duplicate result rows", "DISTINCT returns unique rows for the selected columns."),
            fact("What does COMMIT do?", "Makes the current transaction's changes permanent", "COMMIT successfully ends a transaction and persists its changes."),
            fact("What does ROLLBACK do to an uncommitted transaction?", "Undo its changes", "ROLLBACK discards changes made since the transaction began or its savepoint."),
            fact("What is the difference between UNION and UNION ALL?", "UNION removes duplicates; UNION ALL keeps them", "UNION applies duplicate elimination while UNION ALL preserves duplicate rows."),
            fact("What is a candidate key?", "A minimal set of attributes that uniquely identifies a row", "A candidate key is a minimal superkey."),
            fact("What is a database view?", "A named query presented as a virtual table", "A view presents query results as a table-like interface."),
            fact("What does a database schema describe?", "The structure of database objects and their relationships", "A schema defines tables, columns, constraints, and related objects."),
            fact("How should application code prevent SQL injection in parameterized queries?", "Bind user values as parameters instead of concatenating SQL", "Bound parameters keep input values separate from SQL syntax."),
            fact("What does ON DELETE CASCADE do for a foreign key?", "Deletes dependent rows when the referenced row is deleted", "Cascade behavior propagates the parent deletion to matching child rows."),
            fact("What is a composite key?", "A key formed from more than one column", "A composite key uses multiple attributes together to identify a row."),
            fact("What is a superkey?", "Any set of attributes that uniquely identifies a row", "A superkey uniquely identifies rows and may contain unnecessary attributes."),
            fact("What is a surrogate key?", "An artificial identifier with no business meaning", "A surrogate key is introduced to identify a row independently of business attributes."),
            fact("What does a UNIQUE constraint enforce?", "No duplicate non-NULL key values", "A UNIQUE constraint prevents repeated values in its constrained column set."),
            fact("What is a correlated subquery?", "A subquery that refers to columns from its outer query", "A correlated subquery depends on values from the surrounding query."),
            fact("What does an INNER JOIN return?", "Rows that satisfy the join condition in both tables", "An inner join includes only matching row pairs."),
            fact("What is a database transaction?", "A logical unit of database work", "A transaction groups operations that are committed or rolled back together."),
            fact("What is a dirty read?", "Reading data written by an uncommitted transaction", "A dirty read observes changes that may later be rolled back."),
            fact("What does a shared lock generally permit?", "Concurrent readers while preventing conflicting writes", "Shared locks allow compatible reads but conflict with exclusive modifications."),
            fact("What is a clustered index commonly used for?", "Organizing table rows by the index key order", "A clustered index determines or closely controls the physical row organization."),
            fact("What is a materialized view?", "A stored, refreshable result of a query", "Unlike a regular view, a materialized view persists query results."),
            fact("What does the SQL COALESCE function return?", "The first non-NULL expression", "COALESCE evaluates arguments from left to right and returns the first non-NULL value."),
            fact("What is a window function used for?", "Calculations across related rows without collapsing them", "Window functions compute values over a related row set while retaining each row.")
    );

    private static final List<CoreFact> OS_FACTS = List.of(
            fact("What is a process?", "A program in execution with its own process state", "A process is an executing program together with its managed resources."),
            fact("What is a thread?", "An execution unit that shares its process address space", "Threads in one process share memory while maintaining their own execution state."),
            fact("What happens during a context switch?", "The CPU saves one execution state and restores another", "The operating system saves and loads execution state to switch tasks."),
            fact("What is preemptive scheduling?", "The OS can interrupt a running task to schedule another", "A preemptive scheduler can take the CPU away from a running process."),
            fact("How does First-Come, First-Served scheduling choose a process?", "By arrival order", "FCFS schedules ready processes in the order they arrive."),
            fact("How does Round Robin scheduling share the CPU?", "It gives each ready process a time quantum in turn", "Round Robin cycles through ready tasks using bounded time slices."),
            fact("What is the selection rule in Shortest Job First scheduling?", "Choose the process with the shortest next CPU burst", "SJF prioritizes the shortest estimated CPU burst."),
            fact("What is a deadlock?", "A set of processes waits indefinitely for resources held by one another", "Deadlocked processes cannot proceed because of circular resource waits."),
            fact("Which is one of the four Coffman conditions for deadlock?", "Circular wait", "Circular wait is one of the necessary Coffman conditions for deadlock."),
            fact("What is a semaphore commonly used for?", "Coordinating access to shared resources", "Semaphore operations coordinate threads or processes using a counter."),
            fact("What distinguishes a mutex from a general counting semaphore?", "A mutex has ownership associated with its lock", "A mutex is normally released by the thread that acquired it."),
            fact("What does paging divide virtual memory into?", "Fixed-size pages mapped to physical frames", "Paging maps fixed-size virtual pages to physical memory frames."),
            fact("What is virtual memory?", "An abstraction that lets processes use a larger address space than physical RAM", "Virtual memory maps process addresses to physical memory and storage."),
            fact("What is a page fault?", "An access to a page that is not currently in physical memory", "The OS must resolve a page fault by locating or loading the page."),
            fact("Which page-replacement policy evicts the least recently used page?", "LRU", "LRU approximates locality by evicting the page unused for the longest time."),
            fact("What is thrashing?", "Excessive paging that leaves little time for useful execution", "Thrashing occurs when memory pressure causes continual page transfers."),
            fact("What is external fragmentation?", "Free memory exists but is split into noncontiguous regions", "External fragmentation prevents a large allocation despite sufficient total free space."),
            fact("What is a system call?", "A controlled interface for requesting an operating-system service", "Applications use system calls to request privileged OS operations."),
            fact("What is the kernel?", "The privileged core of the operating system", "The kernel manages core resources and executes privileged operations."),
            fact("What is restricted in user mode?", "Direct execution of privileged instructions", "User mode limits applications from directly performing privileged operations."),
            fact("What is a race condition?", "A result depends on the timing or interleaving of concurrent operations", "Unsynchronized access can make outcomes depend on execution order."),
            fact("What is starvation in scheduling?", "A process waits indefinitely because other work keeps being favored", "Starvation occurs when a task is repeatedly denied needed resources."),
            fact("How can aging reduce scheduling starvation?", "Gradually increase the priority of processes that wait", "Aging raises waiting tasks' priority over time."),
            fact("What is the producer-consumer problem?", "Coordinating producers and consumers sharing a bounded buffer", "Synchronization must prevent overflow, underflow, and unsafe concurrent access."),
            fact("What does an operating-system scheduler choose?", "Which ready task gets CPU time next", "The scheduler selects work from the ready queue for execution."),
            fact("What is internal fragmentation?", "Unused space inside an allocated memory block", "Internal fragmentation is wasted space within an allocated region."),
            fact("What is a critical section?", "Code that accesses shared state and needs mutual exclusion", "A critical section must be protected against unsafe concurrent access."),
            fact("What is a monitor in concurrent programming?", "A synchronization construct combining shared state and operations", "A monitor provides controlled access to shared state using mutual exclusion and condition synchronization."),
            fact("What is a zombie process in Unix-like systems?", "A terminated process whose exit status has not been collected", "A zombie has finished execution but remains in the process table until reaped."),
            fact("What is an orphan process?", "A process whose parent has terminated", "An orphan process is adopted by a system process after its parent exits."),
            fact("What does a page table map?", "Virtual pages to physical frames", "The page table translates virtual addresses into physical memory locations."),
            fact("What is copy-on-write?", "Sharing data until a process attempts to modify it", "Copy-on-write defers copying pages until a write requires a private copy."),
            fact("What is a bounded waiting condition in synchronization?", "A limit exists on how many times others enter before a waiting thread", "Bounded waiting prevents indefinite postponement when competing for a critical section."),
            fact("What is a livelock?", "Tasks keep changing state but make no useful progress", "In a livelock, active responses prevent tasks from advancing toward completion."),
            fact("What does a ready queue contain?", "Processes ready to run and waiting for CPU time", "The scheduler selects runnable processes from the ready queue."),
            fact("What is a kernel thread?", "A thread managed and scheduled by the operating-system kernel", "The kernel tracks and schedules kernel threads."),
            fact("What is a working set in virtual memory?", "The pages a process has recently used", "A working-set model estimates the pages needed to keep a process actively running."),
            fact("What does a TLB cache?", "Recent virtual-to-physical address translations", "The translation lookaside buffer speeds up address translation by caching page-table entries.")
    );

    private static final List<CoreFact> NETWORK_FACTS = List.of(
            fact("At which OSI layer does IP provide addressing and routing?", "Network layer", "IP operates at the network layer."),
            fact("Which transport protocol provides reliable, ordered delivery?", "TCP", "TCP provides connection-oriented reliable byte-stream delivery."),
            fact("Which transport protocol sends connectionless datagrams?", "UDP", "UDP sends datagrams without establishing a transport connection."),
            fact("What does DNS primarily do?", "Maps domain names to IP addresses", "DNS resolves human-readable names to network records such as IP addresses."),
            fact("At which OSI layer does HTTP operate?", "Application layer", "HTTP is an application-layer protocol."),
            fact("What does HTTPS add to HTTP?", "TLS protection for the connection", "HTTPS uses TLS to provide confidentiality and authentication."),
            fact("What does Ethernet primarily define for a local network?", "Link-layer framing and media access", "Ethernet specifies link-layer communication on wired LANs."),
            fact("What does a router use to forward packets between networks?", "IP addressing and routing information", "Routers forward network-layer packets based on destination IP routes."),
            fact("What address does a typical Ethernet switch learn to forward frames?", "MAC address", "A switch learns source MAC addresses on its ports."),
            fact("What is a default gateway used for?", "Forwarding traffic to destinations outside the local subnet", "A host sends off-subnet traffic to its configured gateway."),
            fact("Which messages begin a TCP three-way handshake?", "SYN, SYN-ACK, ACK", "TCP establishes a connection through SYN, SYN-ACK, and ACK."),
            fact("What security properties can TLS provide?", "Encryption and peer authentication", "TLS protects data in transit and can authenticate the server."),
            fact("What does DHCP commonly assign to a host?", "IP configuration such as an address, gateway, and DNS server", "DHCP dynamically supplies network configuration to clients."),
            fact("What does a subnet mask identify?", "The network and host portions of an IP address", "The mask marks which address bits identify the subnet."),
            fact("How long is an IPv4 address?", "32 bits", "IPv4 addresses contain 32 bits."),
            fact("How long is an IPv6 address?", "128 bits", "IPv6 addresses contain 128 bits."),
            fact("At which layer is a MAC address used for local frame delivery?", "Data link layer", "MAC addresses identify interfaces for link-local frame delivery."),
            fact("What does Network Address Translation commonly change?", "IP address information in packet headers", "NAT translates address information between network address spaces."),
            fact("What does a TCP or UDP port identify?", "An application endpoint on a host", "Ports help deliver transport traffic to the correct application endpoint."),
            fact("How many layers are in the OSI reference model?", "Seven", "The OSI reference model has seven layers."),
            fact("What does HTTP status code 404 indicate?", "The requested resource was not found", "A 404 response means the server did not find the requested resource."),
            fact("What does HTTP status code 301 indicate?", "A permanent redirect", "A 301 response indicates that the resource has moved permanently."),
            fact("Which protocol is commonly used to send email between mail servers?", "SMTP", "SMTP is used for sending and relaying email."),
            fact("Which protocol supports synchronized access to mail stored on a server?", "IMAP", "IMAP keeps mail on the server and synchronizes client views."),
            fact("What does traceroute help reveal?", "The sequence of routing hops toward a destination", "Traceroute probes the path and reports intermediate routing hops."),
            fact("What does ARP resolve on an IPv4 local network?", "An IPv4 address to a link-layer MAC address", "ARP discovers the link-layer address associated with a local IPv4 address."),
            fact("Which DNS record identifies a mail server for a domain?", "MX", "An MX record specifies mail-exchange servers for a domain."),
            fact("What does a DNS CNAME record represent?", "An alias from one domain name to another", "A CNAME record points a name to its canonical target name."),
            fact("What is the purpose of a subnet?", "Divide an IP network into smaller logical networks", "Subnetting separates address space into manageable routing domains."),
            fact("What does the TCP receive window advertise?", "How much data the receiver can accept", "The receive window supports TCP flow control based on available receiver capacity."),
            fact("What is TCP congestion control intended to prevent?", "Overloading the network path", "Congestion control adapts sending behavior to network capacity."),
            fact("What does a firewall do?", "Apply rules to allow or block network traffic", "A firewall filters traffic according to configured security policy."),
            fact("What is a private IPv4 address used for?", "Addressing inside private networks without global routing", "Private address ranges are intended for internal networks and are not globally routed."),
            fact("What does a proxy server do?", "Relays requests between a client and another server", "A proxy acts as an intermediary for client requests."),
            fact("What is a CDN designed to do?", "Serve content from geographically distributed edge locations", "A content delivery network caches content near users to reduce latency."),
            fact("What does a socket identify?", "A communication endpoint defined by an address and transport port", "A socket combines network addressing with a transport endpoint."),
            fact("What is packet switching?", "Sending data as packets that share network links", "Packet switching lets traffic from multiple sources use shared network infrastructure."),
            fact("What does ICMP commonly carry?", "Network control and diagnostic messages", "ICMP reports network conditions and supports tools such as ping.")
    );

    private MockAssessmentCatalog() {
    }

    static List<Question> createAptitudeQuestions(Topic topic, int firstNumber, int count) {
        List<Question> questions = new ArrayList<>(count);
        for (int number = firstNumber; number < firstNumber + count; number++) {
            questions.add(createAptitudeQuestion(topic, number));
        }
        return questions;
    }

    static List<Question> createCoreQuestions(Map<String, Topic> topics, int firstNumber, int count) {
        List<Question> questions = new ArrayList<>(count);
        for (int number = firstNumber; number < firstNumber + count; number++) {
            String category = CORE_CATEGORIES.get((number - 1) % CORE_CATEGORIES.size());
            List<CoreFact> facts = factsFor(category);
            CoreFact fact = facts.get(((number - 1) / CORE_CATEGORIES.size()) % facts.size());
            List<String> distractors = new ArrayList<>();
            for (int offset = 1; distractors.size() < 3; offset++) {
                String candidate = facts.get((facts.indexOf(fact) + offset) % facts.size()).answer();
                if (!candidate.equals(fact.answer()) && !distractors.contains(candidate)) {
                    distractors.add(candidate);
                }
            }
            questions.add(buildQuestion(
                    String.format(Locale.ROOT, "Core CS %03d: %s", number, fact.prompt()),
                    "CSE core concept practice.",
                    topics.get(category),
                    category,
                    fact.answer(),
                    distractors,
                    fact.explanation(),
                    hintFor(category),
                    number
            ));
        }
        return questions;
    }

    private static Question createAptitudeQuestion(Topic topic, int number) {
        int variant = number - 1;
        int variation = variant / 10;
        int type = variant % 10;
        String title;
        String explanation;
        String hint;
        String answer;
        List<String> distractors;

        switch (type) {
            case 0 -> {
                int base = 100 * (variation + 3);
                int percentage = 15 + (variation % 6) * 5;
                int value = base * percentage / 100;
                title = String.format(Locale.ROOT, "What is %d%% of %d?", percentage, base);
                explanation = String.format(Locale.ROOT, "%d%% of %d = (%d / 100) x %d = %d.", percentage, base, percentage, base, value);
                hint = "Convert the percentage to a fraction over 100, then multiply by the given value.";
                answer = Integer.toString(value);
                distractors = numberDistractors(value);
            }
            case 1 -> {
                int firstPart = 2 + variation % 5;
                int secondPart = 3 + variation % 4;
                int multiplier = variation + 4;
                int total = (firstPart + secondPart) * multiplier;
                int share = firstPart * multiplier;
                title = String.format(Locale.ROOT, "A total of %d is divided in the ratio %d:%d. What is the first share?", total, firstPart, secondPart);
                explanation = String.format(Locale.ROOT, "The total number of ratio parts is %d + %d = %d. Each part is %d / %d = %d, so the first share is %d x %d = %d.", firstPart, secondPart, firstPart + secondPart, total, firstPart + secondPart, multiplier, firstPart, multiplier, share);
                hint = "Add the ratio parts first, then multiply one part by the first ratio term.";
                answer = Integer.toString(share);
                distractors = numberDistractors(share);
            }
            case 2 -> {
                int cost = 200 * (variation + 5);
                int profitPercent = 10 + (variation % 6) * 5;
                int sellingPrice = cost * (100 + profitPercent) / 100;
                title = String.format(Locale.ROOT, "An item costs %d and is sold at a %d%% profit. What is its selling price?", cost, profitPercent);
                explanation = String.format(Locale.ROOT, "Selling price = cost x (100 + profit%%) / 100 = %d x %d / 100 = %d.", cost, 100 + profitPercent, sellingPrice);
                hint = "A profit is added to the cost price; multiply the cost by (100 + profit percentage) / 100.";
                answer = Integer.toString(sellingPrice);
                distractors = numberDistractors(sellingPrice);
            }
            case 3 -> {
                int first = 12 + variation * 2;
                int average = first + 3;
                title = String.format(Locale.ROOT, "What is the average of %d, %d, %d, and %d?", first, first + 2, first + 4, first + 6);
                explanation = String.format(Locale.ROOT, "The sum is %d + %d + %d + %d = %d. Dividing by 4 gives an average of %d.", first, first + 2, first + 4, first + 6, 4 * average, average);
                hint = "Add all four values and divide the sum by four.";
                answer = Integer.toString(average);
                distractors = numberDistractors(average);
            }
            case 4 -> {
                int speed = 36 + variation * 9;
                int time = 2 + variation;
                int distance = speed * time;
                title = String.format(Locale.ROOT, "A vehicle travels at %d km/h for %d hours. How far does it travel?", speed, time);
                explanation = String.format(Locale.ROOT, "Distance = speed x time = %d km/h x %d h = %d km.", speed, time, distance);
                hint = "Use distance = speed x time, keeping the time unit in hours.";
                answer = Integer.toString(distance);
                distractors = numberDistractors(distance);
            }
            case 5 -> {
                int principal = 1000 * (variation + 1);
                int rate = 6 + variation;
                int time = 2 + variation;
                int interest = principal * rate * time / 100;
                title = String.format(Locale.ROOT, "Find the simple interest on %d at %d%% per year for %d years.", principal, rate, time);
                explanation = String.format(Locale.ROOT, "Simple interest = P x R x T / 100 = %d x %d x %d / 100 = %d.", principal, rate, time, interest);
                hint = "Apply simple interest = principal x annual rate x time / 100.";
                answer = Integer.toString(interest);
                distractors = numberDistractors(interest);
            }
            case 6 -> {
                int principal = 10000 * (variation + 1);
                int years = 1 + variation % 3;
                int amount = BigDecimal.valueOf(principal)
                        .multiply(BigDecimal.valueOf(1.1).pow(years))
                        .setScale(0, RoundingMode.HALF_UP).intValueExact();
                int interest = amount - principal;
                title = String.format(Locale.ROOT, "What compound interest is earned on %d at 10%% annually for %d year(s)?", principal, years);
                explanation = String.format(Locale.ROOT, "Amount = P x (1 + R/100)^T = %d x (1.1)^%d = %d. Interest = amount - principal = %d.", principal, years, amount, interest);
                hint = "Calculate the compound amount with P(1 + rate)^time, then subtract the principal.";
                answer = Integer.toString(interest);
                distractors = numberDistractors(interest);
            }
            case 7 -> {
                int red = 2 + variation;
                int blue = 5 + variation;
                int total = red + blue;
                title = String.format(Locale.ROOT, "A bag contains %d red and %d blue balls. What is the probability of drawing a red ball?", red, blue);
                answer = fraction(red, total);
                explanation = String.format(Locale.ROOT, "Probability = favorable outcomes / total outcomes = %d / %d = %s.", red, total, answer);
                hint = "Probability is the number of favorable outcomes divided by the total number of equally likely outcomes.";
                distractors = List.of(fraction(red + 1, total), fraction(red, total + 1), fraction(blue, total));
            }
            case 8 -> {
                int firstWorkerDays = 6 + variation * 3;
                int secondWorkerDays = firstWorkerDays * 2;
                int daysTogether = firstWorkerDays * secondWorkerDays / (firstWorkerDays + secondWorkerDays);
                title = String.format(Locale.ROOT, "Worker A completes a job in %d days and worker B in %d days. How many days do they need working together?", firstWorkerDays, secondWorkerDays);
                explanation = String.format(Locale.ROOT, "Their combined daily rate is 1/%d + 1/%d = %d/%d of the job. The time is the reciprocal, %d days.", firstWorkerDays, secondWorkerDays, firstWorkerDays + secondWorkerDays, firstWorkerDays * secondWorkerDays, daysTogether);
                hint = "Add the workers' one-day work rates, then take the reciprocal of the combined rate.";
                answer = Integer.toString(daysTogether);
                distractors = numberDistractors(daysTogether);
            }
            default -> {
                int younger = 18 + variation;
                int older = 2 * younger + 4;
                int difference = older - younger;
                title = String.format(Locale.ROOT, "An older sibling is twice as old as a younger sibling plus 4 years. If the younger sibling is %d, what is their age difference?", younger);
                explanation = String.format(Locale.ROOT, "Older age = 2 x %d + 4 = %d. The difference is %d - %d = %d years.", younger, older, older, younger, difference);
                hint = "Find the older age from the relationship, then subtract the younger age.";
                answer = Integer.toString(difference);
                distractors = numberDistractors(difference);
            }
        }

        return buildQuestion(
                String.format(Locale.ROOT, "Aptitude %03d: %s", number, title),
                "Quantitative aptitude practice.",
                topic,
                "APTITUDE",
                answer,
                distractors,
                explanation,
                hint,
                number
        );
    }

    private static Question buildQuestion(
            String title,
            String description,
            Topic topic,
            String category,
            String answer,
            List<String> distractors,
            String explanation,
            String hint,
            int number
    ) {
        List<String> options = new ArrayList<>(4);
        int correctIndex = (number - 1) % 4;
        int distractorIndex = 0;
        for (int optionIndex = 0; optionIndex < 4; optionIndex++) {
            options.add(optionIndex == correctIndex ? answer : distractors.get(distractorIndex++));
        }

        String difficulty = switch ((number - 1) % 3) {
            case 0 -> "EASY";
            case 1 -> "MEDIUM";
            default -> "HARD";
        };

        return Question.builder()
                .title(title)
                .description(description)
                .topic(topic)
                .category(category)
                .difficulty(difficulty)
                .questionType("MCQ")
                .optionA(options.get(0))
                .optionB(options.get(1))
                .optionC(options.get(2))
                .optionD(options.get(3))
                .correctOption(String.valueOf((char) ('A' + correctIndex)))
                .explanation(explanation)
                .hint(hint)
                .build();
    }

    private static List<String> numberDistractors(int answer) {
        return List.of(Integer.toString(answer - 1), Integer.toString(answer + 1), Integer.toString(answer + 2));
    }

    private static String fraction(int numerator, int denominator) {
        int divisor = gcd(numerator, denominator);
        return (numerator / divisor) + "/" + (denominator / divisor);
    }

    private static int gcd(int first, int second) {
        int a = Math.abs(first);
        int b = Math.abs(second);
        while (b != 0) {
            int remainder = a % b;
            a = b;
            b = remainder;
        }
        return a;
    }

    private static String hintFor(String category) {
        return switch (category) {
            case "JAVA_OOPS" -> "Recall the Java language rule or API contract being tested and compare the exact terms.";
            case "DBMS" -> "Identify the database concept involved and distinguish it from related SQL commands or constraints.";
            case "OS" -> "Focus on the resource or execution-state change described in the question.";
            default -> "Map the behavior to the relevant network layer, protocol, or addressing mechanism.";
        };
    }

    private static List<CoreFact> factsFor(String category) {
        return switch (category) {
            case "JAVA_OOPS" -> JAVA_FACTS;
            case "DBMS" -> DBMS_FACTS;
            case "OS" -> OS_FACTS;
            default -> NETWORK_FACTS;
        };
    }

    private static CoreFact fact(String prompt, String answer, String explanation) {
        return new CoreFact(prompt, answer, explanation);
    }

    private record CoreFact(String prompt, String answer, String explanation) {
    }
}
