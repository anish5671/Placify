package com.placify.seeder;

import com.placify.question.Question;
import com.placify.question.Topic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

final class DsaPracticeCatalog {

    static final int TARGET_SIZE = 220;
    private static final String COMPANY_TAGS = "Amazon,Google,Microsoft,TCS";
    static final List<Module> MODULES = List.of(
            new Module("arrays", "Arrays & Hashing"),
            new Module("strings", "Strings & Backtracking"),
            new Module("two-pointers", "Two Pointers & Sliding Window"),
            new Module("stack-queue", "Stack & Queue"),
            new Module("linked-list", "Linked Lists"),
            new Module("binary-search", "Binary Search"),
            new Module("trees", "Trees & Tries"),
            new Module("heaps", "Heaps & Greedy"),
            new Module("dynamic-programming", "Dynamic Programming"),
            new Module("backtracking", "Backtracking & Recursion"),
            new Module("graphs", "Graphs")
    );

    private static final List<String> TWO_POINTER_TITLES = List.of(
            "3Sum", "Container With Most Water", "Trapping Rain Water", "Rotate Array",
            "Longest Repeating Character Replacement", "Permutation in String",
            "Find All Anagrams in a String", "Minimum Window Substring",
            "Minimum Size Subarray Sum", "Sort Colors", "Four Sum", "Move Zeroes",
            "Squares of a Sorted Array", "Sliding Window Maximum", "Max Consecutive Ones III"
    );
    private static final List<String> STRING_TITLES = List.of(
            "Palindrome Partitioning", "Letter Combinations of a Phone Number",
            "Word Search II", "Permutations II", "String Compression"
    );
    private static final List<String> STACK_TITLES = List.of(
            "Binary Tree Level Order Traversal", "Binary Tree Right Side View",
            "Binary Tree Zigzag Level Order Traversal", "Populating Next Right Pointers in Each Node",
            "Serialize and Deserialize Binary Tree", "Maximum Depth of Binary Tree",
            "Count Good Nodes in Binary Tree"
    );
    private static final List<String> TREE_BACKTRACKING_TITLES = List.of(
            "Construct Binary Tree from Preorder and Inorder Traversal",
            "Construct Binary Tree from Inorder and Postorder",
            "Path Sum II", "Flatten Binary Tree to Linked List"
    );
    private static final List<String> HEAP_TITLES = List.of(
            "Jump Game", "Jump Game II", "Best Time to Buy and Sell Stock with Cooldown",
            "Best Time to Buy and Sell Stock with Transaction Fee",
            "Best Time to Buy and Sell Stock III", "House Robber", "House Robber II",
            "Minimum Path Sum"
    );
    private static final List<String> BACKTRACKING_TITLES = List.of(
            "Combination Sum", "Target Sum", "Partition Equal Subset Sum", "Decode Ways"
    );
    private static final List<String> NEW_TWO_POINTER_TITLES = List.of(
            "Minimum Size Subarray Sum", "Sort Colors", "Four Sum", "Move Zeroes",
            "Squares of a Sorted Array", "Sliding Window Maximum", "Max Consecutive Ones III"
    );
    private static final List<String> NEW_STRING_TITLES = List.of("String Compression");
    private static final List<String> NEW_LINKED_LIST_TITLES = List.of(
            "Intersection of Two Linked Lists", "Palindrome Linked List",
            "Reverse Linked List II", "Rotate List"
    );
    private static final List<String> NEW_BINARY_SEARCH_TITLES = List.of(
            "Search Insert Position", "Find K Closest Elements", "Find the First Bad Version"
    );
    private static final Map<String, String> ADDITIONAL_MODULE_BY_TITLE = Map.ofEntries(
            Map.entry("Majority Element II", "arrays"),
            Map.entry("Merge Intervals", "arrays"),
            Map.entry("Next Permutation", "arrays"),
            Map.entry("Set Matrix Zeroes", "arrays"),
            Map.entry("Find the Missing and Repeating Number", "arrays"),
            Map.entry("Longest Common Prefix", "strings"),
            Map.entry("Find the Index of the First Occurrence in a String", "strings"),
            Map.entry("Isomorphic Strings", "strings"),
            Map.entry("Longest Substring with At Most K Distinct Characters", "strings"),
            Map.entry("Minimum Insertions to Make a String Palindrome", "strings"),
            Map.entry("3Sum Closest", "two-pointers"),
            Map.entry("Remove Duplicates from Sorted Array II", "two-pointers"),
            Map.entry("Fruit Into Baskets", "two-pointers"),
            Map.entry("Longest Subarray of 1s After Deleting One Element", "two-pointers"),
            Map.entry("Minimum Window Subsequence", "two-pointers"),
            Map.entry("Next Greater Element I", "stack-queue"),
            Map.entry("Online Stock Span", "stack-queue"),
            Map.entry("Implement Queue using Stacks", "stack-queue"),
            Map.entry("Implement Stack using Queues", "stack-queue"),
            Map.entry("Asteroid Collision", "stack-queue"),
            Map.entry("Odd Even Linked List", "linked-list"),
            Map.entry("Delete Node in a Linked List", "linked-list"),
            Map.entry("Flatten a Multilevel Doubly Linked List", "linked-list"),
            Map.entry("Swap Nodes in Pairs", "linked-list"),
            Map.entry("Remove Duplicates from Sorted List II", "linked-list"),
            Map.entry("Allocate Books", "binary-search"),
            Map.entry("Aggressive Cows", "binary-search"),
            Map.entry("Kth Element of Two Sorted Arrays", "binary-search"),
            Map.entry("Kth Missing Positive Number", "binary-search"),
            Map.entry("Painter's Partition Problem", "binary-search"),
            Map.entry("Binary Tree Boundary Traversal", "trees"),
            Map.entry("Vertical Order Traversal of a Binary Tree", "trees"),
            Map.entry("Check Completeness of a Binary Tree", "trees"),
            Map.entry("Construct BST from Preorder", "trees"),
            Map.entry("Implement Trie Prefix Tree", "trees"),
            Map.entry("Meeting Rooms II", "heaps"),
            Map.entry("Minimum Platforms", "heaps"),
            Map.entry("Fractional Knapsack", "heaps"),
            Map.entry("Reorganize String", "heaps"),
            Map.entry("Maximum Sum Combination", "heaps"),
            Map.entry("0/1 Knapsack", "dynamic-programming"),
            Map.entry("Rod Cutting", "dynamic-programming"),
            Map.entry("Minimum Cost to Cut a Stick", "dynamic-programming"),
            Map.entry("Maximum Sum Increasing Subsequence", "dynamic-programming"),
            Map.entry("Count Square Submatrices with All Ones", "dynamic-programming"),
            Map.entry("Rat in a Maze", "backtracking"),
            Map.entry("M-Coloring Problem", "backtracking"),
            Map.entry("Restore IP Addresses", "backtracking"),
            Map.entry("Matchsticks to Square", "backtracking"),
            Map.entry("Permutation Sequence", "backtracking"),
            Map.entry("Dijkstra's Algorithm", "graphs"),
            Map.entry("Bellman-Ford Algorithm", "graphs"),
            Map.entry("Bipartite Graph Check", "graphs"),
            Map.entry("Minimum Spanning Tree", "graphs"),
            Map.entry("Alien Dictionary", "graphs")
    );

    private DsaPracticeCatalog() {
    }

    static List<Topic> createModuleTopics() {
        return MODULES.stream()
                .map(module -> Topic.builder()
                        .name(module.name())
                        .slug("dsa-" + module.slug())
                        .category("DSA")
                        .description("DSA practice module: " + module.name())
                        .icon("Binary")
                        .build())
                .toList();
    }

    static List<Question> createQuestions(Map<String, Topic> moduleTopics) {
        return IntStream.range(0, PROBLEMS.size())
                .mapToObj(index -> {
                    Problem problem = PROBLEMS.get(index);
                    String moduleSlug = moduleFor(index, problem.title());
                    List<String> options = new ArrayList<>(List.of(
                            problem.approach(),
                            "Try every possible combination; this is usually exponential or quadratic.",
                            "Sort the input and scan it without preserving required order or duplicates.",
                            "Use a greedy choice without proving that it is optimal."
                    ));
                    int correctIndex = index % options.size();
                    Collections.rotate(options, correctIndex);

                    return Question.builder()
                            .title(problem.title())
                            .description(problem.statement())
                            .topic(moduleTopics.get(moduleSlug))
                            .category("DSA")
                            .difficulty(problem.difficulty())
                            .questionType("CODE_PRACTICE")
                            .optionA(options.get(0))
                            .optionB(options.get(1))
                            .optionC(options.get(2))
                            .optionD(options.get(3))
                            .correctOption(String.valueOf((char) ('A' + correctIndex)))
                            .explanation(problem.explanation())
                            .companyTags(COMPANY_TAGS)
                            .externalLink("https://leetcode.com/problemset/?search="
                                    + java.net.URLEncoder.encode(problem.title(), java.nio.charset.StandardCharsets.UTF_8))
                            .build();
                }).toList();
    }

    private static String moduleFor(int index, String title) {
        if (TWO_POINTER_TITLES.contains(title)) return "two-pointers";
        if (STRING_TITLES.contains(title)) return "strings";
        if (STACK_TITLES.contains(title)) return "stack-queue";
        if (TREE_BACKTRACKING_TITLES.contains(title)) return "backtracking";
        if (HEAP_TITLES.contains(title)) return "heaps";
        if (BACKTRACKING_TITLES.contains(title)) return "backtracking";
        if (NEW_LINKED_LIST_TITLES.contains(title)) return "linked-list";
        if (NEW_BINARY_SEARCH_TITLES.contains(title)) return "binary-search";
        if (NEW_STRING_TITLES.contains(title)) return "strings";
        if (NEW_TWO_POINTER_TITLES.contains(title)) return "two-pointers";
        String additionalModule = ADDITIONAL_MODULE_BY_TITLE.get(title);
        if (additionalModule != null) return additionalModule;

        if (index < 18) return "arrays";
        if (index < 31) return "strings";
        if (index < 39) return "stack-queue";
        if (index < 49) return "linked-list";
        if (index < 60) return "binary-search";
        if (index < 85) return "trees";
        if (index < 92) return "heaps";
        if (index < 117) return "dynamic-programming";
        if (index < 128) return "backtracking";
        return "graphs";
    }

    record Module(String slug, String name) {
    }

    private static Problem p(String title, String difficulty, String statement, String approach, String explanation) {
        return new Problem(title, difficulty, statement, approach, explanation);
    }

    private static final List<Problem> PROBLEMS = List.of(
            p("Best Time to Buy and Sell Stock", "EASY", "Find the largest profit from one buy followed by one later sale; return zero when no profitable sale exists.", "Track the minimum price seen so far and the best difference; O(n) time and O(1) space.", "Scan once, updating the running minimum before evaluating each sale. This enforces buy-before-sell and gives O(n) time and O(1) space."),
            p("Contains Duplicate", "EASY", "Determine whether an integer array contains any value more than once.", "Insert values into a HashSet and stop when an insertion is not new; O(n) expected time.", "A set records values already visited, so the first repeated value answers the question. Expected O(n) time and O(n) space."),
            p("Product of Array Except Self", "MEDIUM", "Return an array where each position contains the product of every input value except the value at that position, without division.", "Use prefix products and multiply by a reverse suffix-product pass; O(n) time.", "Store the product to the left of each index, then sweep right-to-left multiplying the suffix. O(n) time; output storage is not counted as auxiliary space."),
            p("Maximum Subarray", "MEDIUM", "Find the contiguous subarray with the greatest sum and return that sum.", "Kadane's algorithm keeps the best sum ending at the current position; O(n) time.", "At each value, either extend the current subarray or start a new one, then update the global maximum. O(n) time and O(1) space."),
            p("Maximum Product Subarray", "MEDIUM", "Find the contiguous subarray whose product is greatest and return that product.", "Track both maximum and minimum products ending here because a negative value swaps them; O(n) time.", "Keep current high and low products; for each number compare it with both products multiplied by that number. O(n) time and O(1) space."),
            p("Find Minimum in Rotated Sorted Array", "MEDIUM", "Find the smallest value in a strictly increasing array that may have been rotated.", "Binary-search the unsorted half by comparing the middle value with the right endpoint; O(log n).", "If the middle exceeds the right endpoint, the minimum is to its right; otherwise it is at the middle or left. O(log n) time."),
            p("3Sum", "MEDIUM", "Return all unique triples of values whose sum is zero.", "Sort, fix one value, then use two pointers while skipping duplicates; O(n^2) time.", "Sorting enables a two-pointer search for each fixed value. Skip equal values at all three positions so each triple appears once. O(n^2) time."),
            p("Container With Most Water", "MEDIUM", "Choose two vertical lines that hold the greatest possible amount of water.", "Start at both ends and move the shorter line inward; O(n) time.", "Area is limited by the shorter boundary. Moving the taller boundary cannot improve area while width shrinks, so advance the shorter one. O(n) time."),
            p("Trapping Rain Water", "HARD", "Compute how much water remains between the bars of an elevation map after rain.", "Use two pointers and left/right maximum boundaries, processing the side with the smaller maximum; O(n) time.", "The smaller boundary determines the water level at that side, so it can be finalized while moving inward. O(n) time and O(1) extra space."),
            p("Remove Duplicates from Sorted Array", "EASY", "Remove repeated values in-place from a sorted array and return the number of unique values.", "Use a write pointer for the next distinct value while scanning once; O(n) time.", "Because equal values are adjacent, copy each new value to the next write position. O(n) time and O(1) extra space."),
            p("Merge Sorted Array", "EASY", "Merge two sorted arrays in-place into the first array, which has enough trailing capacity.", "Fill from the end using two pointers so unread values are not overwritten; O(m+n) time.", "The largest remaining value belongs at the last open slot. Work backward until the second array is consumed. O(m+n) time."),
            p("Rotate Array", "MEDIUM", "Rotate an array to the right by k positions in-place.", "Reverse the whole array, then reverse the two resulting sections; O(n) time and O(1) space.", "Reduce k modulo the length, reverse all values, then reverse each section to obtain the requested rotation."),
            p("Single Number", "EASY", "Every value occurs twice except one; find the value that occurs once.", "XOR all values; paired values cancel, leaving the unique value; O(n) time and O(1) space.", "XOR is associative, x XOR x is zero, and x XOR zero is x. A single pass therefore isolates the unpaired value."),
            p("Majority Element", "EASY", "Find the value that appears more than half the time in an array.", "Use the Boyer-Moore majority vote candidate and verify it if a majority is not guaranteed.", "Pairing each candidate occurrence with a different value cannot eliminate a true majority. The surviving candidate is the majority; O(n) time and O(1) space."),
            p("Missing Number", "EASY", "An array contains distinct values from zero through n with one missing; find it.", "XOR the expected range with all array values so matching values cancel; O(n) time.", "Alternatively use the arithmetic-series sum. XOR avoids overflow and takes O(n) time with O(1) space."),
            p("First Missing Positive", "HARD", "Find the smallest positive integer absent from an unsorted array using constant extra space.", "Place each value x in index x-1 when x is within 1..n, then find the first mismatch; O(n) time.", "Index placement makes each valid positive value mark its own slot. The first unfilled slot identifies the answer; O(n) time and O(1) extra space."),
            p("Subarray Sum Equals K", "MEDIUM", "Count contiguous subarrays whose values sum to k.", "Use prefix sums and a frequency map of earlier sums; O(n) expected time.", "For current prefix sum s, each earlier prefix s-k forms a valid subarray. Count those prefixes before recording s."),
            p("Longest Consecutive Sequence", "MEDIUM", "Find the length of the longest run of consecutive integers in an unsorted array.", "Put values in a set and expand only from values with no predecessor; O(n) expected time.", "Starting only at sequence beginnings ensures each value is traversed a constant number of times. Expected O(n) time and O(n) space."),
            p("Valid Anagram", "EASY", "Determine whether two strings contain the same characters with the same frequencies.", "Count characters in one pass over each string; O(n) time.", "Increment counts for the first string and decrement for the second; all counts must finish at zero."),
            p("Group Anagrams", "MEDIUM", "Group words that are anagrams of one another.", "Use a canonical key such as sorted characters or a 26-count frequency signature; O(n k log k) with sorting.", "Words with the same canonical character signature belong to one map bucket. Sorting each word costs O(k log k)."),
            p("Longest Repeating Character Replacement", "MEDIUM", "Find the longest substring that can be made of one repeated letter using at most k replacements.", "Maintain a sliding window and its most frequent character; shrink when window size minus that frequency exceeds k.", "The window is feasible when non-dominant characters fit within the replacement budget. O(n) time with a fixed alphabet."),
            p("Permutation in String", "MEDIUM", "Check whether one string contains a permutation of another as a contiguous substring.", "Slide a fixed-size window and compare character-frequency counts; O(n) time for a fixed alphabet.", "A permutation has the same frequency vector. Update counts as the window advances rather than rebuilding them."),
            p("Find All Anagrams in a String", "MEDIUM", "Return every start index where a substring is an anagram of a pattern.", "Use a fixed-length sliding window and update character counts incrementally; O(n) time.", "Compare the current window's frequencies with the pattern frequencies, updating only the characters that enter and leave."),
            p("Minimum Window Substring", "HARD", "Find the shortest substring containing every character of a target string, including multiplicities.", "Expand a sliding window until valid, then contract it while tracking required counts; O(n) time.", "Maintain how many target characters are still missing. Once zero, move the left boundary to minimize the valid window."),
            p("Valid Palindrome", "EASY", "Check whether a string is a palindrome after ignoring non-alphanumeric characters and case.", "Compare normalized characters with two pointers from opposite ends; O(n) time and O(1) space.", "Skip characters outside the accepted set, lowercase the rest, and stop on the first unequal pair."),
            p("Longest Palindromic Substring", "MEDIUM", "Return the longest palindromic substring in a given string.", "Expand around each odd and even center; O(n^2) time and O(1) space.", "Every palindrome has a center. Expanding each possible center finds the longest while using constant extra space."),
            p("Palindromic Substrings", "MEDIUM", "Count all palindromic substrings in a string.", "Expand around every odd and even center and count each successful expansion; O(n^2) time.", "Each center expansion discovers exactly the palindromes centered there, including single-character substrings."),
            p("Encode and Decode Strings", "MEDIUM", "Design reversible encoding for a list of strings that may contain delimiter characters.", "Prefix each string with its length and a separator, then read exactly that many characters when decoding.", "Length-prefix framing is unambiguous even when content contains separators or is empty; total encoding and decoding time is linear."),
            p("String to Integer (atoi)", "MEDIUM", "Parse a signed decimal integer from a string while handling whitespace, invalid suffixes, and overflow.", "Skip leading spaces, parse sign and digits, and clamp during accumulation before overflow occurs; O(n).", "Accumulate digits only while they are valid and check against integer bounds before multiplying by ten."),
            p("Integer to Roman", "MEDIUM", "Convert a positive integer to its Roman numeral representation.", "Greedily subtract values from a descending table that includes subtractive pairs such as IV and IX.", "The canonical descending value table encodes all subtractive forms; repeatedly append each symbol while its value fits."),
            p("Roman to Integer", "EASY", "Convert a Roman numeral to its integer value.", "Scan symbols and subtract a value when it is smaller than the next symbol; otherwise add it.", "A smaller symbol before a larger one is subtractive; all other symbols contribute additively. O(n) time."),
            p("Daily Temperatures", "MEDIUM", "For each day, find how many days pass until a warmer temperature, or zero if none follows.", "Use a monotonic decreasing stack of unresolved indices; O(n) time.", "A warmer day resolves all colder indices at the stack top, and each index is pushed and popped once."),
            p("Valid Parentheses", "EASY", "Determine whether brackets are correctly matched and properly nested.", "Push opening brackets and require each closing bracket to match the stack top; O(n) time.", "The most recent unmatched opening bracket must close first, which is exactly stack order."),
            p("Min Stack", "MEDIUM", "Design a stack supporting push, pop, top, and minimum retrieval in constant time.", "Store each value with the minimum so far, or maintain a second stack of minima; O(1) per operation.", "Recording the minimum at each stack depth makes the current minimum available without scanning."),
            p("Evaluate Reverse Polish Notation", "MEDIUM", "Evaluate an arithmetic expression written in postfix notation.", "Push operands and apply each operator to the top two stack values; O(n) time.", "Postfix order places operands before their operator. Pop right then left to preserve subtraction and division order."),
            p("Generate Parentheses", "MEDIUM", "Generate every well-formed string containing n pairs of parentheses.", "Backtrack, adding an opening bracket while openings remain and a closing bracket only when it stays balanced.", "The invariant closings never exceed openings and openings never exceed n; only valid prefixes are explored."),
            p("Largest Rectangle in Histogram", "HARD", "Find the largest rectangle that can fit under a histogram.", "Use a monotonic increasing stack and compute widths when a shorter bar closes a range; O(n) time.", "When a bar is popped, the current index is its first smaller right boundary and the new stack top is its left boundary."),
            p("Car Fleet", "MEDIUM", "Count groups of cars that reach a destination together without passing one another.", "Sort cars by position and compare arrival times from nearest to farthest; O(n log n).", "A car joins the fleet ahead if its arrival time is no greater; otherwise it forms a new fleet."),
            p("Simplify Path", "MEDIUM", "Normalize an absolute Unix-style path by resolving . and .. components.", "Split into components and use a stack, popping for .. when possible; O(n) time.", "Ignore empty and current-directory components, pop for parent-directory components, then join the remaining names."),
            p("Reverse Nodes in k-Group", "HARD", "Reverse nodes in fixed-size groups in a linked list, leaving a short final group unchanged.", "Locate each complete group, reverse its pointers, and connect its boundaries; O(n) time and O(1) extra space.", "Do not reverse until k nodes are confirmed. Keep the prior group's tail to reconnect each reversed segment."),
            p("Merge Two Sorted Lists", "EASY", "Merge two sorted singly linked lists into one sorted list.", "Use a sentinel node and repeatedly link the smaller current node; O(n+m) time.", "A sentinel simplifies the head case; attach the remaining list when either input is exhausted."),
            p("Linked List Cycle", "EASY", "Determine whether a singly linked list contains a cycle.", "Use Floyd's slow and fast pointers; they meet if a cycle exists; O(n) time and O(1) space.", "Inside a cycle, the fast pointer gains one node per step on the slow pointer and eventually catches it."),
            p("Linked List Cycle II", "MEDIUM", "Return the node where a linked-list cycle begins, or null when there is no cycle.", "After Floyd pointers meet, move one pointer to the head and advance both one step until they meet again.", "The distance equations for the meeting point show that head-to-entry equals meeting-point-to-entry modulo the cycle length."),
            p("Reorder List", "MEDIUM", "Reorder a list as first, last, second, second-last, continuing inward.", "Find the middle, reverse the second half, then weave both halves together; O(n) time.", "The three linear passes split, reverse, and interleave the list without allocating another list."),
            p("Remove Nth Node From End of List", "MEDIUM", "Remove the nth node from the end of a singly linked list.", "Advance a fast pointer n nodes, then move fast and slow together from a sentinel; O(n) time.", "When fast reaches the end, slow is immediately before the node to remove."),
            p("Add Two Numbers", "MEDIUM", "Add two non-negative integers represented by reverse-order linked lists.", "Walk both lists with a carry, creating one output digit per position; O(max(n,m)) time.", "At each position add available digits and carry, store sum modulo ten, and carry the quotient."),
            p("Copy List with Random Pointer", "MEDIUM", "Deep-copy a list whose nodes also point to arbitrary nodes in the same list.", "Map each original node to its clone, then connect next and random pointers in a second pass; O(n).", "The map preserves identity relationships so each random pointer can be redirected to the matching clone."),
            p("Find the Duplicate Number", "MEDIUM", "Find the repeated value in an array of n+1 values from 1 through n without modifying the array.", "Treat values as pointers and use Floyd cycle detection; O(n) time and O(1) space.", "The duplicate creates a cycle in the index-linked sequence. Floyd's entry-finding phase returns the duplicate."),
            p("LRU Cache", "MEDIUM", "Design a least-recently-used cache with constant-time get and put operations.", "Combine a HashMap with a doubly linked list ordered from least to most recently used.", "The map locates nodes in O(1); list unlink/relink operations update recency and evict the least recent node."),
            p("Binary Search", "EASY", "Find a target in a sorted array and return its index, or -1 if absent.", "Use low and high bounds and halve the search interval each step; O(log n) time.", "Maintain an inclusive search interval and discard the half that cannot contain the target."),
            p("Search a 2D Matrix", "MEDIUM", "Search a matrix whose rows are sorted and whose first value exceeds the previous row's last value.", "Treat the matrix as one sorted array and binary-search virtual indices; O(log(mn)).", "Map flat index mid to row mid/columns and compare its value with the target."),
            p("Koko Eating Bananas", "MEDIUM", "Find the minimum integer eating speed that finishes all piles within h hours.", "Binary-search the speed and test feasibility using the sum of ceiling pile/speed; O(n log maxPile).", "Feasibility is monotonic: any speed that finishes on time implies every faster speed also does."),
            p("Time Based Key-Value Store", "MEDIUM", "Store values with timestamps and retrieve the latest value at or before a requested time.", "Keep timestamp-sorted entries per key and binary-search the floor timestamp.", "Per-key append order is chronological; upper-bound search finds the last eligible entry."),
            p("Median of Two Sorted Arrays", "HARD", "Find the median of two sorted arrays in logarithmic time.", "Binary-search a partition in the shorter array so left partitions contain half the values.", "Adjust the partition until both left boundary values are no greater than the opposite right boundaries; then compute the median."),
            p("Search in a Sorted Array of Unknown Size", "MEDIUM", "Find a target using an accessor that returns a sentinel outside the array bounds.", "Grow an upper bound exponentially, then binary-search the bounded range; O(log n).", "Exponential expansion finds a range containing the target or end, and binary search completes the lookup."),
            p("Find First and Last Position of Element", "MEDIUM", "Return the first and last indices of a target in a sorted array.", "Run lower-bound and upper-bound binary searches; O(log n) time.", "Two boundary searches find the first value not less than target and first value greater than target."),
            p("Search a 2D Matrix II", "MEDIUM", "Search a matrix sorted ascending across each row and column.", "Start at the top-right and eliminate one row or column per comparison; O(m+n).", "A value above target eliminates its column; a value below target eliminates its row."),
            p("Find Peak Element", "MEDIUM", "Find an index whose value is greater than its neighboring values.", "Binary-search the slope: move toward the larger neighbor; O(log n).", "A rising slope guarantees a peak to the right, while a falling slope guarantees one at or left of mid."),
            p("Capacity to Ship Packages Within D Days", "MEDIUM", "Find the smallest ship capacity that delivers ordered packages within a day limit.", "Binary-search capacity and greedily count required days; O(n log(sum(weights))).", "The required day count decreases monotonically as capacity increases, enabling binary search."),
            p("Split Array Largest Sum", "HARD", "Split an array into at most k contiguous parts while minimizing the largest part sum.", "Binary-search the maximum allowed sum and greedily count parts needed; O(n log(sum)).", "If a bound needs more than k parts it is too small; otherwise it is feasible."),
            p("Construct Binary Tree from Preorder and Inorder Traversal", "MEDIUM", "Rebuild a binary tree from its preorder and inorder traversal arrays.", "Use preorder to select each root and an index map to divide the inorder range; O(n).", "Preorder visits a subtree root first; its inorder position separates left and right subtrees."),
            p("Invert Binary Tree", "EASY", "Swap the left and right children of every node in a binary tree.", "Traverse the tree with DFS or BFS and swap each node's children; O(n) time.", "Every node is visited once and its two child links are exchanged."),
            p("Maximum Depth of Binary Tree", "EASY", "Compute the maximum number of nodes on a root-to-leaf path.", "Use DFS: one plus the larger child depth; O(n) time.", "The empty tree has depth zero; each node adds one to the deeper subtree."),
            p("Diameter of Binary Tree", "EASY", "Find the length in edges of the longest path between any two tree nodes.", "Postorder DFS returns subtree height and updates the best left-height plus right-height; O(n).", "Any path through a node combines its deepest left and right branches; track the maximum during height computation."),
            p("Balanced Binary Tree", "EASY", "Determine whether every node's left and right subtree heights differ by at most one.", "Postorder DFS returns height or an imbalance sentinel; O(n) time.", "Propagate failure immediately when a child is unbalanced or height difference exceeds one."),
            p("Same Tree", "EASY", "Check whether two binary trees have identical structure and node values.", "Recursively compare corresponding nodes and their children; O(n) time.", "Matching trees require equal null structure and equal values at every paired node."),
            p("Subtree of Another Tree", "EASY", "Determine whether one tree appears as a rooted subtree of another.", "Try structural equality at each node of the larger tree; O(mn) worst case.", "At each candidate root, compare shape and values recursively; otherwise search both child subtrees."),
            p("Lowest Common Ancestor of a BST", "MEDIUM", "Find the lowest common ancestor of two values in a binary search tree.", "Walk down while both values lie on the same side; the first split is the answer; O(h).", "BST ordering places both nodes in one subtree until their paths diverge at the LCA."),
            p("Binary Tree Level Order Traversal", "MEDIUM", "Return node values grouped by depth from top to bottom.", "Use BFS with a queue and process the queue size at the start of each level; O(n).", "The queue contains nodes in level order; batching its current size separates levels."),
            p("Binary Tree Right Side View", "MEDIUM", "Return the value visible at each depth when viewing a tree from the right.", "Run level-order BFS and record the last node of each level; O(n).", "The last queued node in a level is its rightmost visible node."),
            p("Count Good Nodes in Binary Tree", "MEDIUM", "Count nodes whose value is at least every value on the root-to-node path.", "DFS while carrying the maximum value seen on the path; O(n).", "A node qualifies when it meets the carried maximum; pass the updated maximum to its children."),
            p("Validate Binary Search Tree", "MEDIUM", "Determine whether a binary tree obeys strict BST ordering at every node.", "DFS with exclusive lower and upper value bounds; O(n) time.", "Each node must respect all ancestor constraints, not only its immediate children."),
            p("Kth Smallest Element in a BST", "MEDIUM", "Find the kth smallest value in a binary search tree.", "Perform inorder traversal and stop after visiting k nodes; O(h+k).", "Inorder traversal of a BST visits values in sorted order."),
            p("Construct Binary Tree from Inorder and Postorder", "MEDIUM", "Rebuild a binary tree from its inorder and postorder traversals.", "Use the last postorder value as root and split inorder with an index map; O(n).", "Consume postorder backward, building the right subtree before the left, around the root's inorder position."),
            p("Binary Tree Maximum Path Sum", "HARD", "Find the greatest sum of values on any path between two nodes.", "Postorder DFS returns the best downward gain and updates a global sum using both child gains; O(n).", "Clamp negative child gains to zero for a path through the current node; return only one branch upward."),
            p("Serialize and Deserialize Binary Tree", "HARD", "Convert a binary tree to a string and reconstruct the exact tree from that string.", "Use preorder traversal with explicit null markers and consume tokens in the same order.", "Null markers preserve tree shape; deterministic preorder makes reconstruction unambiguous."),
            p("Binary Tree Zigzag Level Order Traversal", "MEDIUM", "Return each tree level alternating left-to-right and right-to-left.", "BFS by level and reverse or indexed-write every other level; O(n).", "The traversal order per level is adjusted while retaining normal queue expansion."),
            p("Path Sum", "EASY", "Check whether a root-to-leaf path has a specified sum.", "DFS subtracting each visited value and test zero at leaves; O(n).", "Only a leaf completes a valid root-to-leaf path, so check the remaining sum there."),
            p("Path Sum II", "MEDIUM", "Return all root-to-leaf paths whose values add to a target.", "Backtrack a path list and remaining sum; O(n) traversal plus output size.", "Append on descent, record matching leaves, and remove on return to restore the path."),
            p("Flatten Binary Tree to Linked List", "MEDIUM", "Transform a binary tree in-place into preorder right-child links.", "Use reverse preorder traversal or splice each left subtree between a node and its right subtree.", "Preorder's next node follows the left subtree when present; reconnect the original right subtree after the left subtree's rightmost node."),
            p("Populating Next Right Pointers in Each Node", "MEDIUM", "Connect each node's next pointer to its adjacent node on the same level.", "Traverse each level and link children using existing next pointers; O(n) time and O(1) extra space.", "For a perfect tree, each parent connects its left-to-right child and its right child to the next parent's left child."),
            p("Unique Binary Search Trees", "MEDIUM", "Count structurally distinct BSTs that can store values 1 through n.", "Use Catalan dynamic programming: choose each root and multiply left/right subtree counts; O(n^2).", "For each root split, combine the number of possible left and right trees and sum all products."),
            p("Implement Trie (Prefix Tree)", "MEDIUM", "Design a trie supporting insertion, exact search, and prefix search.", "Store child links per character and a terminal marker; operations take O(word length).", "Each character follows one edge; the terminal flag distinguishes a stored word from a prefix."),
            p("Design Add and Search Words Data Structure", "MEDIUM", "Support word insertion and searches where a dot matches any one character.", "Use a trie; for a wildcard, recursively explore every child at that depth.", "Literal characters follow one edge; wildcard branches across children until the word length is consumed."),
            p("Word Search", "MEDIUM", "Find whether a word can be traced through adjacent non-repeating cells in a character grid.", "Backtrack from matching cells, marking a cell visited for the current path; O(mn*4^L).", "Explore four neighbors, restoring the cell after recursion so other paths can use it."),
            p("Top K Frequent Elements", "MEDIUM", "Return the k values that occur most often in an integer array.", "Count frequencies and use a size-k heap or frequency buckets; O(n log k) with a heap.", "A min-heap of size k discards less frequent values while retaining the highest counts."),
            p("Kth Largest Element in an Array", "MEDIUM", "Find the kth largest value without sorting the entire array.", "Use quickselect around a pivot or a size-k min-heap; average O(n) for quickselect.", "Partitioning places the target rank in its final side, allowing the other side to be skipped."),
            p("Find Median from Data Stream", "HARD", "Support adding numbers and retrieving the median at any time.", "Maintain a max-heap for the lower half and min-heap for the upper half; O(log n) add and O(1) median.", "Balance heap sizes and keep every lower-half value no greater than every upper-half value."),
            p("Merge K Sorted Lists", "HARD", "Merge k sorted linked lists into one sorted linked list.", "Use a min-heap containing the current head of each list; O(N log k).", "Repeatedly append the smallest available node and insert its successor into the heap."),
            p("Task Scheduler", "MEDIUM", "Schedule tasks with a cooldown between identical task types and minimize total intervals.", "Use the maximum task frequency to form cooldown frames, or simulate with a max-heap and queue.", "The most frequent task determines the minimum frame length; other tasks fill idle slots when possible."),
            p("IPO", "HARD", "Select up to k projects to maximize capital, with each project requiring a minimum capital.", "Sort by required capital and push affordable profits into a max-heap; select the best each round.", "Only currently affordable projects are eligible; adding the largest available profit maximizes next-round capital."),
            p("Find K Pairs with Smallest Sums", "MEDIUM", "Return k pairs, one value from each sorted array, with the smallest pair sums.", "Use a min-heap over sorted matrix frontier positions; O(k log k).", "Seed the heap with the first-row candidates, then advance the second-array index for each popped pair."),
            p("Longest Increasing Subsequence", "MEDIUM", "Find the length of the longest strictly increasing subsequence.", "Maintain minimal tail values and binary-search each new value; O(n log n).", "The tails array stores the smallest possible ending value for each subsequence length."),
            p("Longest Common Subsequence", "MEDIUM", "Find the length of the longest subsequence shared by two strings.", "Use a two-dimensional DP over string prefixes, matching characters diagonally; O(mn).", "A matching pair extends the previous diagonal; otherwise take the best result excluding either final character."),
            p("Word Break", "MEDIUM", "Determine whether a string can be segmented into dictionary words.", "Dynamic programming marks reachable prefix positions using dictionary matches; O(n^2) typical.", "A position is reachable if an earlier reachable boundary leaves a dictionary word ending there."),
            p("Combination Sum", "MEDIUM", "Return unique combinations of reusable candidates that sum to a target.", "Backtrack with a start index and remaining target to avoid permutation duplicates.", "Reuse the current index for unlimited copies; advance it when exploring later candidates."),
            p("House Robber", "MEDIUM", "Maximize the sum of non-adjacent values in a linear sequence.", "Keep the best total with and without the current value; O(n) time and O(1) space.", "For each house choose between skipping it and adding its value to the best total two positions back."),
            p("House Robber II", "MEDIUM", "Maximize non-adjacent values arranged in a circle.", "Take the maximum of linear robberies excluding the first or excluding the last; O(n).", "The first and last cannot both be selected, so these two linear cases cover every valid solution."),
            p("Decode Ways", "MEDIUM", "Count ways to decode a digit string where 1 through 26 map to letters.", "DP using one-digit and valid two-digit transitions; O(n) time and O(1) space.", "A zero cannot decode alone; add the prior one- or two-position counts only when their codes are valid."),
            p("Unique Paths", "MEDIUM", "Count paths from the top-left to bottom-right of a grid when only right and down moves are allowed.", "Use one-dimensional DP where each cell combines the value above and left; O(mn).", "Each cell's path count is the sum of paths arriving from its top and left neighbors."),
            p("Jump Game", "MEDIUM", "Determine whether the final array index is reachable when each value gives a maximum jump length.", "Track the farthest reachable index in one greedy scan; O(n).", "If the scan passes the farthest reachable position, the end is unreachable; otherwise extend the frontier."),
            p("Jump Game II", "MEDIUM", "Find the minimum jumps needed to reach the final index.", "Greedily expand the current jump range and count a jump when its boundary is reached; O(n).", "Each range represents positions reachable with the same number of jumps; choose the next farthest boundary."),
            p("Partition Equal Subset Sum", "MEDIUM", "Determine whether an array can be split into two subsets with equal sums.", "Use descending 0/1 subset-sum DP up to half the total; O(n*sum).", "Each value updates reachable sums backward so it is used at most once; target is half the total."),
            p("Target Sum", "MEDIUM", "Count sign assignments that make an array sum to a target.", "Transform to a subset-sum count and use one-dimensional frequency DP.", "Positive and negative partitions imply a subset target of (sum+target)/2; count ways with descending updates."),
            p("Interleaving String", "MEDIUM", "Check whether a string is formed by interleaving two strings while preserving each one's order.", "Use prefix DP over positions in both source strings; O(mn) time.", "A destination prefix is reachable from either source when its next character matches."),
            p("Longest Palindromic Subsequence", "MEDIUM", "Find the length of the longest palindromic subsequence in a string.", "Interval DP: matching endpoints add two; otherwise discard one endpoint; O(n^2).", "Fill shorter intervals first so both subproblems are already known."),
            p("Edit Distance", "MEDIUM", "Find the minimum insertions, deletions, and substitutions to transform one string into another.", "DP over string prefixes with replace, insert, and delete transitions; O(mn).", "Equal characters copy the diagonal state; differing characters cost one plus the minimum of three operations."),
            p("Best Time to Buy and Sell Stock with Cooldown", "MEDIUM", "Maximize stock profit when selling requires one cooldown day before buying again.", "DP with hold, sold, and rest states; O(n) time and O(1) space.", "A buy transitions from rest, a sell from hold, and a sold state becomes rest after the cooldown."),
            p("Coin Change II", "MEDIUM", "Count combinations of coins that make an amount, treating different coin orders as the same combination.", "For each coin, update amount DP in ascending order; O(coins*amount).", "Processing one denomination at a time counts combinations rather than permutations."),
            p("Regular Expression Matching", "HARD", "Match a string against a pattern containing . and * where * repeats the preceding token.", "Use DP over text and pattern prefixes, handling zero or repeated occurrences for *; O(mn).", "For star, either consume zero preceding tokens or consume one matching text character and stay on the pattern."),
            p("Burst Balloons", "HARD", "Choose an order to burst balloons to maximize coins earned from neighboring values at each burst.", "Interval DP chooses the last balloon burst inside each interval; O(n^3).", "When the final balloon in an interval is chosen, its boundary neighbors are fixed, splitting the remaining work."),
            p("Palindromic Substrings Count", "MEDIUM", "Count every contiguous substring that reads identically from either direction.", "Expand around all character and between-character centers; O(n^2) time.", "Each successful symmetric expansion adds one palindrome and then checks the next wider pair."),
            p("Minimum Path Sum", "MEDIUM", "Find the minimum sum path from the top-left to bottom-right of a non-negative grid using right and down moves.", "Dynamic programming each cell from the smaller of its top and left neighbors; O(mn).", "Each cell's optimal cost is its value plus the cheaper reachable predecessor."),
            p("Triangle Minimum Path Sum", "MEDIUM", "Find the minimum top-to-bottom path sum in a triangle, moving to adjacent positions on the next row.", "Update a one-dimensional DP bottom-up; O(n^2) time and O(n) space.", "Each entry adds its value to the smaller of its two children in the next row."),
            p("Maximal Square", "MEDIUM", "Find the area of the largest square containing only ones in a binary matrix.", "DP each one-cell from top, left, and diagonal minimum plus one; O(mn).", "A square can extend only as far as the smallest neighboring square permits."),
            p("Best Time to Buy and Sell Stock with Transaction Fee", "MEDIUM", "Maximize profit with as many stock transactions as desired and a fixed fee per sale.", "Track best cash and holding states per day; O(n) time and O(1) space.", "Update cash by selling the held share minus fee, and holding by buying from prior cash."),
            p("Best Time to Buy and Sell Stock III", "HARD", "Maximize profit with at most two stock transactions.", "Track best states after each buy and sell, or use left/right maximum profit passes; O(n).", "The state sequence buy, sell, buy, sell captures the best profit under a two-transaction limit."),
            p("N-Queens", "HARD", "Place n queens on an n by n board so none attack another, and return all valid boards.", "Backtrack row by row while tracking used columns and diagonals; O(n!) search.", "A placement is valid when its column and two diagonal keys are unused; undo each placement on return."),
            p("Subsets", "MEDIUM", "Return every subset of a set of distinct integers.", "Backtrack include/exclude choices or use a bitmask; O(n*2^n) output time.", "Each value independently belongs or does not belong to a subset."),
            p("Subsets II", "MEDIUM", "Return every unique subset when the input may contain duplicates.", "Sort values and skip equal choices at the same recursion depth.", "Sorting makes duplicates adjacent; skipping them at one depth prevents duplicate subset branches."),
            p("Permutations", "MEDIUM", "Return every ordering of distinct input values.", "Backtrack with a used-value marker; O(n*n!) including output.", "Choose one unused value at each depth and undo the choice after exploring its branch."),
            p("Permutations II", "MEDIUM", "Return every unique ordering of values that may contain duplicates.", "Sort values and skip a duplicate when its previous equal value is not currently used.", "This rule ensures equal values are chosen in a consistent order, avoiding duplicate permutations."),
            p("Combinations", "MEDIUM", "Return every size-k combination selected from values 1 through n.", "Backtrack with an increasing start value and prune when too few values remain.", "Increasing choices prevent permutation duplicates; pruning stops branches that cannot reach size k."),
            p("Word Search II", "HARD", "Find all dictionary words that can be traced through adjacent cells in a character grid.", "Build a trie of words and backtrack over the board, pruning paths absent from the trie.", "Trie prefixes let the search stop early; mark cells for the current path and restore them afterward."),
            p("Sudoku Solver", "HARD", "Fill an incomplete 9 by 9 Sudoku grid so each row, column, and box contains digits 1 through 9 once.", "Backtrack over empty cells using row, column, and box candidate sets; choose the most constrained cell first.", "Try only legal digits and undo failed placements; selecting a cell with few candidates reduces branching."),
            p("Combination Sum II", "MEDIUM", "Find unique combinations summing to a target when each candidate may be used once and duplicates exist.", "Sort and backtrack, skipping equal candidates at the same depth.", "Use each sorted index once; same-depth duplicate skipping prevents repeated combinations."),
            p("Palindrome Partitioning", "MEDIUM", "Split a string into substrings so every part is a palindrome, returning all valid partitions.", "Backtrack over end positions and recurse only when the chosen substring is palindromic.", "Each valid palindrome prefix leads to a recursive partition of the remaining suffix."),
            p("Letter Combinations of a Phone Number", "MEDIUM", "Return all letter strings represented by a sequence of digits from 2 through 9.", "Backtrack through the mapped letters for each digit; O(4^n) worst case.", "Append one mapped letter per digit, then restore the prefix after exploring each choice."),
            p("Number of Islands II", "HARD", "After each land addition to a water grid, report the current number of islands.", "Use disjoint-set union and merge the new cell with adjacent land; near O(1) amortized per operation.", "Each new land cell creates one component; each successful union with a neighboring component reduces the count."),
            p("Clone Graph", "MEDIUM", "Create a deep copy of a connected graph with arbitrary neighbor relationships.", "Traverse with DFS or BFS and map each original node to its clone; O(V+E).", "Create a clone on first visit and use the map to connect cloned neighbor edges."),
            p("Course Schedule", "MEDIUM", "Determine whether all courses can be completed given prerequisite pairs.", "Topologically process indegree-zero nodes or use DFS cycle detection; O(V+E).", "A directed prerequisite cycle prevents a valid ordering; Kahn's algorithm detects it when fewer than V nodes are processed."),
            p("Course Schedule II", "MEDIUM", "Return a valid order for completing courses, or an empty result if prerequisites form a cycle.", "Use Kahn's topological sort with an indegree queue; O(V+E).", "Append each zero-indegree course and reduce its dependents' indegrees; success requires processing every vertex."),
            p("Pacific Atlantic Water Flow", "MEDIUM", "Find grid cells from which water can flow to both the Pacific and Atlantic borders.", "Run reverse DFS/BFS from each ocean's borders, moving to equal-or-higher cells, then intersect visited sets.", "Reverse traversal follows possible incoming flow; cells reached from both oceans satisfy the condition."),
            p("Graph Valid Tree", "MEDIUM", "Determine whether an undirected graph is connected and contains no cycle.", "Check edge count n-1 and run DFS/BFS or union-find for connectivity; O(V+E).", "A tree has exactly n-1 edges and one connected component; these conditions exclude cycles."),
            p("Number of Connected Components", "MEDIUM", "Count connected components in an undirected graph.", "Use DFS/BFS from every unvisited vertex or union-find; O(V+E).", "Each traversal from an unvisited vertex discovers exactly one component."),
            p("Redundant Connection", "MEDIUM", "Find the edge that first creates a cycle in an undirected graph that began as a tree.", "Use union-find; the edge whose endpoints already share a root is redundant.", "Each valid tree edge joins separate components; a failed union identifies the cycle-forming edge."),
            p("Word Ladder", "HARD", "Find the shortest number of one-letter transformations from a start word to a target using a dictionary.", "Use BFS over words connected by single-character changes; O(N*L*alphabet) typical.", "All transformations have equal cost, so BFS discovers the shortest sequence first."),
            p("Network Delay Time", "MEDIUM", "Find how long it takes for a signal from one node to reach every node in a weighted directed graph.", "Run Dijkstra's algorithm from the source with a min-priority queue; O((V+E)log V).", "Finalize the shortest known distance when a node is removed with its current minimum distance."),
            p("Cheapest Flights Within K Stops", "MEDIUM", "Find the cheapest route between two cities using at most k intermediate stops.", "Use bounded-edge relaxation or layered Bellman-Ford for k+1 rounds.", "Each round extends paths by at most one flight; copy prior distances so a round cannot use multiple extra edges."),
            p("Min Cost to Connect All Points", "MEDIUM", "Connect all points with minimum total Manhattan-distance cable length.", "Use Prim's minimum spanning tree algorithm; O(n^2) for the dense complete graph.", "Repeatedly add the cheapest edge from the connected set to an unconnected point."),
            p("Reconstruct Itinerary", "HARD", "Use every directed airline ticket exactly once to build the lexicographically smallest itinerary from a required start.", "Run Hierholzer's algorithm with each airport's destinations in lexical order.", "Depth-first edge consumption and reverse postorder produce an Eulerian route; ordered adjacency selects the lexical route."),
            p("Accounts Merge", "MEDIUM", "Merge accounts that share an email address and return sorted emails per person.", "Union accounts by shared email, then group emails by representative; O(N alpha(N)+sorting).", "An email-to-owner map joins overlapping accounts; collect each connected component and sort its emails."),
            p("Minimum Size Subarray Sum", "MEDIUM", "Given positive integers and a target, find the shortest contiguous subarray whose sum is at least the target.", "Use a sliding window, expanding the right edge and shrinking the left edge while the sum meets the target.", "Positive values make the window sum monotonic as either edge moves. Each element enters and leaves once, giving O(n) time."),
            p("Sort Colors", "MEDIUM", "Sort an array containing only 0, 1, and 2 in-place without using a library sort.", "Use three-way partitioning with low, current, and high pointers; O(n) time and O(1) space.", "Move zeros before low and twos after high; advance current only after handling its value."),
            p("Four Sum", "MEDIUM", "Return unique quadruplets whose values sum to a target.", "Sort, fix two values, then use two pointers and skip duplicates; O(n^3) time.", "Sorting enables pruning and duplicate skipping; the inner two-pointer scan finds each distinct pair for the fixed values."),
            p("Move Zeroes", "EASY", "Move all zeroes to the end of an array while preserving the order of nonzero values.", "Compact nonzero values with a write pointer, then fill the remaining positions with zeroes.", "The write pointer preserves encounter order and uses O(1) extra space."),
            p("Squares of a Sorted Array", "EASY", "Return the squares of a sorted array in sorted order, including negative values.", "Compare absolute values at both ends and fill the output from right to left; O(n) time.", "The largest square must come from one of the endpoints because the input is sorted."),
            p("Sliding Window Maximum", "HARD", "For every window of size k, return its maximum value.", "Maintain a decreasing deque of indices, removing expired indices and smaller trailing values; O(n) time.", "The deque front is always the current maximum; each index is added and removed at most once."),
            p("Max Consecutive Ones III", "MEDIUM", "Given a binary array and k, find the longest contiguous segment of ones after flipping at most k zeroes.", "Use a sliding window that shrinks whenever it contains more than k zeroes; O(n) time.", "The window is valid when its zero count is at most k. Each index enters and leaves once."),
            p("String Compression", "MEDIUM", "Compress consecutive repeated characters in-place as a character followed by its count, returning the new length.", "Use separate read and write pointers to count each run and write its character and count digits.", "Each run is read once and the write pointer never overtakes unread input, so the algorithm uses O(1) extra space."),
            p("Intersection of Two Linked Lists", "EASY", "Return the node at which two singly linked lists intersect, or null if they do not.", "Use two pointers that switch to the other list's head after reaching the end; O(m+n) time.", "Both pointers traverse the same combined distance, aligning their remaining path lengths without extra storage."),
            p("Palindrome Linked List", "EASY", "Determine whether a singly linked list reads the same forward and backward using O(1) extra space.", "Find the midpoint, reverse the second half, and compare corresponding nodes.", "The midpoint split produces equal-length halves (or one extra node in front); reversing permits an in-place comparison."),
            p("Reverse Linked List II", "MEDIUM", "Reverse nodes between positions left and right in a singly linked list without changing other nodes.", "Use a sentinel and repeatedly move the node after the sublist head to the front of the sublist.", "Head-insertion reverses the selected segment in-place and correctly handles left equal to one."),
            p("Rotate List", "MEDIUM", "Rotate a linked list to the right by k places.", "Connect the tail to the head to form a cycle, then break it at the new tail after reducing k modulo the list length.", "The new tail is length-k-1 steps from the old head. Handle empty lists and zero effective rotations before forming the cycle."),
            p("Search Insert Position", "EASY", "Return the index where a target belongs in a sorted array, whether or not it is present.", "Binary-search for the lower bound: the first index whose value is not less than the target.", "The lower-bound invariant narrows the insertion point in O(log n) time."),
            p("Find K Closest Elements", "MEDIUM", "Return the k values closest to x in a sorted array, breaking ties toward the smaller value.", "Binary-search the left edge of a length-k window; compare the values just outside each candidate window.", "The best window is contiguous in a sorted array, and the comparison eliminates half the possible starts each step."),
            p("Find the First Bad Version", "EASY", "Find the first bad version when versions are ordered and every version after it is also bad.", "Binary-search the first true result of the monotonic good/bad predicate.", "The predicate changes from false to true once, so lower-bound binary search finds the transition in O(log n) calls."),
            p("Majority Element II", "MEDIUM", "Return all values that occur more than one third of the time in an integer array.", "Use the extended Boyer-Moore vote algorithm to track at most two candidates, then verify their counts.", "At most two values can exceed n/3 occurrences. Candidate elimination finds them in O(n) time and O(1) extra space."),
            p("Merge Intervals", "MEDIUM", "Combine all overlapping intervals and return the resulting non-overlapping ranges.", "Sort intervals by start and extend the last output interval whenever the next one overlaps.", "After sorting, an interval can overlap only the most recently merged range. Time is O(n log n)."),
            p("Next Permutation", "MEDIUM", "Rearrange a sequence into its next lexicographically greater permutation, wrapping to the smallest order if needed.", "Find the rightmost ascending pivot, swap with its next larger suffix value, then reverse the suffix.", "The suffix is non-increasing; reversing it after the pivot swap gives the smallest possible larger arrangement."),
            p("Set Matrix Zeroes", "MEDIUM", "If a matrix cell is zero, set its entire row and column to zero in-place.", "Use the first row and column as marker storage, while separately remembering whether either originally contained zero.", "Markers preserve the original zero locations without a second matrix, giving O(1) auxiliary space."),
            p("Find the Missing and Repeating Number", "MEDIUM", "An array should contain every value from 1 through n once, but one value is missing and another repeats; identify both.", "Use a frequency array, or derive the two values from the sum and squared-sum differences.", "The expected sum and square sum provide two equations for the missing and repeated values; verify bounds and handle large arithmetic safely."),
            p("Longest Common Prefix", "EASY", "Find the longest prefix shared by every string in a list.", "Compare characters across strings from the first position until a mismatch or the shortest string ends.", "The first mismatch ends the common prefix; the scan takes O(total characters) time."),
            p("Find the Index of the First Occurrence in a String", "EASY", "Return the first position where a pattern occurs in a text, or -1 if it does not occur.", "Use the Knuth-Morris-Pratt prefix function to search in O(n+m) time.", "The prefix table reuses matched pattern information after a mismatch instead of restarting comparisons."),
            p("Isomorphic Strings", "EASY", "Determine whether characters in one string can be consistently replaced one-to-one to form another string.", "Track forward and reverse character mappings while scanning both strings.", "Both mappings are required to prevent two source characters from mapping to the same target character."),
            p("Longest Substring with At Most K Distinct Characters", "MEDIUM", "Find the length of the longest substring containing no more than k distinct characters.", "Maintain character frequencies in a sliding window and shrink until its distinct count is at most k.", "Each character enters and leaves the window at most once, so the scan is O(n)."),
            p("Minimum Insertions to Make a String Palindrome", "HARD", "Find the minimum number of character insertions needed to make a string a palindrome.", "Compute the longest palindromic subsequence; the answer is string length minus that subsequence length.", "Characters in the longest palindromic subsequence can remain in place; insertions are needed only for the rest."),
            p("3Sum Closest", "MEDIUM", "Find three values whose sum is closest to a target and return that sum.", "Sort, fix one value, then move two pointers according to whether the current sum is below or above target.", "For each fixed value, the two-pointer scan examines promising sums in O(n); total time is O(n^2)."),
            p("Remove Duplicates from Sorted Array II", "MEDIUM", "Remove duplicates in-place from a sorted array while allowing each value to appear at most twice.", "Use a write pointer and accept a value only when it differs from the value two kept positions earlier.", "Sorted order groups duplicates together; comparing with the last two kept values enforces the limit in O(n) time."),
            p("Fruit Into Baskets", "MEDIUM", "Find the longest contiguous segment containing at most two distinct fruit types.", "Use a sliding window with counts and shrink it whenever a third type enters.", "The frequency map keeps the window valid; every index advances at most twice for O(n) time."),
            p("Longest Subarray of 1s After Deleting One Element", "MEDIUM", "Find the longest run of ones obtainable after deleting exactly one element from a binary array.", "Use a sliding window containing at most one zero and count its length minus one.", "The one-zero window represents the best segment after deleting its zero, or one element when the window has none."),
            p("Minimum Window Subsequence", "HARD", "Find the shortest substring of a source that contains a target as a subsequence in order.", "Scan forward to complete the target, then move backward to shrink each valid window.", "Greedy forward and backward passes locate a minimal window for each completion without storing all subsequences."),
            p("Next Greater Element I", "EASY", "For each value in one array, find the first greater value to its right in a second array.", "Use a decreasing monotonic stack over the second array and store each value's next greater result.", "When a larger value arrives, it resolves all smaller values at the stack top in linear total time."),
            p("Online Stock Span", "MEDIUM", "For each new stock price, report the number of consecutive prior days with price no greater than today's.", "Maintain a decreasing stack of price and accumulated span pairs, popping all smaller or equal prices.", "Popped spans combine with today's span, so each price is pushed and popped once."),
            p("Implement Queue using Stacks", "EASY", "Implement FIFO queue operations using only stack operations.", "Use an input stack for pushes and transfer to an output stack only when the output stack is empty.", "Each item moves between stacks at most once, giving amortized O(1) operations."),
            p("Implement Stack using Queues", "EASY", "Implement LIFO stack operations using queue operations.", "After adding an item, rotate the prior queue contents behind it so the newest item is at the front.", "Keeping the newest item at the front makes pop and top constant time; push is linear."),
            p("Asteroid Collision", "MEDIUM", "Simulate asteroids moving in opposite directions; colliding asteroids destroy the smaller one, or both if equal.", "Use a stack and resolve collisions only when a right-moving asteroid meets a left-moving one.", "The stack stores surviving asteroids in order; each asteroid is pushed once and removed at most once."),
            p("Odd Even Linked List", "MEDIUM", "Group nodes at odd indices followed by nodes at even indices while preserving each group order.", "Maintain separate odd and even chains and connect the odd tail to the even head.", "Rewiring two running tails preserves relative order and uses O(1) extra space."),
            p("Delete Node in a Linked List", "MEDIUM", "Delete a given non-tail node when the list head is not provided.", "Copy the next node's value into the given node and bypass the next node.", "The following node supplies the value and link to retain; the operation is impossible for a tail node."),
            p("Flatten a Multilevel Doubly Linked List", "MEDIUM", "Flatten a doubly linked list whose nodes may have child lists into one level in depth-first order.", "Traverse nodes and splice each child list between its parent and the parent's former successor.", "Save and reconnect both next and prev links at each splice; a stack can defer the original successor."),
            p("Swap Nodes in Pairs", "MEDIUM", "Swap each adjacent pair of nodes in a singly linked list without changing node values.", "Use a sentinel and rewire each complete pair before advancing to the next pair.", "The sentinel handles swapping at the head; an unmatched final node remains in place."),
            p("Remove Duplicates from Sorted List II", "MEDIUM", "Remove every value that occurs more than once in a sorted linked list, keeping only values that appeared once.", "Use a sentinel and skip each entire run when the current value repeats.", "Sorted order makes each duplicate run contiguous, so one pass can unlink all repeated nodes."),
            p("Allocate Books", "HARD", "Allocate contiguous book pages among students so the maximum pages assigned to anyone is minimized.", "Binary-search the maximum allowed pages and greedily count the students needed for that bound.", "Feasibility is monotonic as the page limit increases; reject when students exceed the available count."),
            p("Aggressive Cows", "HARD", "Place a fixed number of cows in stalls to maximize the minimum distance between any two placed cows.", "Sort stall positions and binary-search the distance, greedily placing each next cow as early as possible.", "If a distance is feasible, every smaller distance is feasible too, enabling binary search."),
            p("Kth Element of Two Sorted Arrays", "HARD", "Find the kth smallest value across two sorted arrays without merging them.", "Binary-search a partition in the shorter array so exactly k elements lie on the left.", "A valid partition has each left boundary no greater than the opposite right boundary; it is found in O(log min(n,m))."),
            p("Kth Missing Positive Number", "EASY", "Find the kth positive integer missing from a strictly increasing array of positive values.", "Binary-search the first index where the number of missing values before it reaches k.", "At index i, the count of missing positives is array[i]-i-1, a monotonic function."),
            p("Painter's Partition Problem", "HARD", "Assign contiguous boards to painters in order while minimizing the maximum total length assigned to one painter.", "Binary-search the maximum workload and greedily count how many painters are required.", "The number of required painters decreases as the workload limit grows, making feasibility monotonic."),
            p("Binary Tree Boundary Traversal", "MEDIUM", "Return a binary tree's boundary in anti-clockwise order without repeating nodes.", "Collect the root, non-leaf left boundary, all leaves, then the reversed non-leaf right boundary.", "Separating the four traversal parts and excluding leaves from side boundaries prevents duplicates."),
            p("Vertical Order Traversal of a Binary Tree", "HARD", "Group tree nodes by vertical column, sorting ties by row and then value.", "Traverse with row and column coordinates, store nodes by coordinate, and sort ties before emitting columns.", "Coordinates determine column and depth; tie sorting enforces the required deterministic order."),
            p("Check Completeness of a Binary Tree", "MEDIUM", "Determine whether every level of a binary tree is full except possibly the last, which must be filled left to right.", "Run level-order traversal and reject any non-null node appearing after the first missing child slot.", "Completeness requires all later queue positions to be empty once a gap has appeared."),
            p("Construct BST from Preorder", "MEDIUM", "Build a binary search tree from its preorder traversal.", "Recursively consume preorder values within exclusive lower and upper bounds.", "Each subtree is a contiguous preorder segment bounded by the BST ordering constraints."),
            p("Implement Trie Prefix Tree", "MEDIUM", "Implement a trie supporting insertion, exact-word search, and prefix search.", "Store child links per character and mark terminal nodes for complete words.", "Each operation follows one path of length at most the input word length."),
            p("Meeting Rooms II", "MEDIUM", "Find the minimum number of rooms required to hold all meeting intervals.", "Sort start times and use a min-heap of current meeting end times.", "Reuse a room when its earliest meeting ends before the next begins; heap size is the active room count."),
            p("Minimum Platforms", "MEDIUM", "Find the minimum number of train platforms needed so no arriving train waits.", "Sort arrival and departure times and sweep both lists, incrementing on arrival and decrementing on departure.", "The maximum simultaneous trains equals the required platforms; process equal times consistently with interval conventions."),
            p("Fractional Knapsack", "MEDIUM", "Maximize value in a capacity-limited knapsack when fractions of items may be taken.", "Sort items by value-to-weight ratio and take each greedily until capacity is filled.", "With divisible items, each unit of capacity should go to the highest remaining value density."),
            p("Reorganize String", "MEDIUM", "Rearrange characters so no identical characters are adjacent, or report that it is impossible.", "Use a max-heap of character counts, temporarily holding the previously used character out of the heap.", "Always selecting the most frequent available character avoids stranding a larger remaining count."),
            p("Maximum Sum Combination", "MEDIUM", "Find the k largest pair sums formed by choosing one value from each of two arrays.", "Sort both arrays and explore index pairs from the largest corner with a max-heap and visited set.", "Each popped pair reveals its next two neighbors; the visited set prevents duplicate index pairs."),
            p("0/1 Knapsack", "MEDIUM", "Choose items with given weights and values to maximize value without exceeding capacity; each item can be used at most once.", "Use capacity DP updated in descending order for each item.", "Descending updates prevent an item from being reused in the same iteration; complexity is O(nW)."),
            p("Rod Cutting", "MEDIUM", "Find the maximum revenue obtainable by cutting a rod into pieces with given prices by length.", "Use unbounded knapsack DP over rod lengths and try each first-cut size.", "Every length can be formed by a first piece plus an optimal remainder; the recurrence permits repeated piece lengths."),
            p("Minimum Cost to Cut a Stick", "HARD", "Choose an order for specified cuts on a stick to minimize the total cost, where each cut costs the current segment length.", "Sort cut positions and use interval DP to try each cut as the first cut within an interval.", "After a cut, the left and right intervals become independent; the segment's length is paid once."),
            p("Maximum Sum Increasing Subsequence", "MEDIUM", "Find the maximum possible sum of a strictly increasing subsequence.", "Use dynamic programming where each value extends the best-sum subsequence ending at a smaller prior value.", "For each index, compare its standalone value with extensions from all smaller previous values; O(n^2) time."),
            p("Count Square Submatrices with All Ones", "MEDIUM", "Count every square submatrix containing only ones.", "For each one-cell, store one plus the minimum square size ending above, left, and diagonally above-left.", "The DP value is the side length of the largest all-one square ending at that cell; sum all values."),
            p("Rat in a Maze", "MEDIUM", "List paths for a rat to travel from the top-left to bottom-right of a blocked grid without revisiting cells.", "Backtrack over allowed neighboring cells, marking the current path and undoing marks on return.", "The visited/path state prevents cycles; backtracking explores each valid route."),
            p("M-Coloring Problem", "MEDIUM", "Determine whether an undirected graph can be colored with at most m colors so adjacent vertices differ.", "Assign colors recursively, checking each candidate against already colored neighbors.", "Backtracking abandons a color choice as soon as it conflicts and tries the next available color."),
            p("Restore IP Addresses", "MEDIUM", "Insert three dots into a digit string to form all valid IPv4 addresses.", "Backtrack over segment lengths one through three, rejecting leading zeroes and values above 255.", "Exactly four valid segments must consume the entire input; pruning invalid prefixes limits the search."),
            p("Matchsticks to Square", "MEDIUM", "Determine whether matchsticks with given lengths can be arranged into four equal-length sides without breaking any stick.", "Sort lengths descending and backtrack by placing each stick on a side whose current length does not exceed the target.", "Prune symmetric side choices and reject when total length is not divisible by four; each stick is assigned exactly once."),
            p("Permutation Sequence", "HARD", "Return the kth permutation of the numbers 1 through n in lexicographic order.", "Use factorial number-system blocks to select each next value directly from the remaining sorted values.", "Each leading digit owns (n-1)! permutations; division and remainder identify the correct block and offset."),
            p("Dijkstra's Algorithm", "MEDIUM", "Find shortest distances from one source in a weighted graph with non-negative edge weights.", "Use a min-priority queue and relax outgoing edges whenever a shorter distance is found.", "Non-negative weights ensure a node's smallest popped distance is final; complexity is O((V+E)log V)."),
            p("Bellman-Ford Algorithm", "MEDIUM", "Find shortest paths from a source when edge weights may be negative, and detect reachable negative cycles.", "Relax every edge up to V-1 rounds, stopping early if no distance changes; perform one extra cycle check.", "Any shortest simple path uses at most V-1 edges; a further improvement indicates a reachable negative cycle."),
            p("Bipartite Graph Check", "MEDIUM", "Determine whether an undirected graph can be divided into two sets with every edge crossing between them.", "Color each component with two colors using BFS or DFS and reject same-color adjacent vertices.", "A valid two-coloring exists exactly when the graph contains no odd-length cycle."),
            p("Minimum Spanning Tree", "MEDIUM", "Connect every vertex of a connected weighted undirected graph with minimum total edge weight and no cycles.", "Use Kruskal's algorithm: sort edges and add each edge that joins different union-find components.", "The cut property makes the lightest eligible edge safe; union-find skips cycle-forming edges."),
            p("Alien Dictionary", "HARD", "Infer a valid character order from a sorted list of words in an unknown alphabet, or detect inconsistency.", "Compare adjacent words to create the first differing-character edge, then topologically sort the graph.", "Only the first mismatch constrains order; a longer word before its exact prefix is invalid.")
    );


    private record Problem(String title, String difficulty, String statement, String approach, String explanation) {
    }
}
