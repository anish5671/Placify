package com.preptrack.battle;

import java.util.*;

final class CodingBattleCatalog {

    private CodingBattleCatalog() {}

    record Spec(String slug, String title, String difficulty, String description,
                String inputFormat, String outputFormat, String constraints,
                String operation) {}

    static List<SeedProblem> problems() {
        List<Spec> specs = List.of(
                p("pair-sum", "Add Two Numbers", "EASY", "Read two signed integers and print their sum.", "PAIR", "sum"),
                p("pair-difference", "Find the Signed Difference", "EASY", "Read A and B and print A minus B.", "PAIR", "difference"),
                p("pair-product", "Multiply Two Numbers", "EASY", "Read two integers and print their product.", "PAIR", "product"),
                p("pair-maximum", "Larger of Two", "EASY", "Print the larger of two integers. If equal, print that value.", "PAIR", "maximum"),
                p("pair-minimum", "Smaller of Two", "EASY", "Print the smaller of two integers. If equal, print that value.", "PAIR", "minimum"),
                p("pair-distance", "Absolute Difference", "EASY", "Print the non-negative distance between two integers.", "PAIR", "distance"),
                p("pair-gcd", "Greatest Common Divisor", "EASY", "Print the greatest common divisor of two integers; define gcd(0, 0) as 0.", "PAIR", "gcd"),
                p("pair-lcm", "Least Common Multiple", "EASY", "Print the non-negative least common multiple; if either value is zero, print 0.", "PAIR", "lcm"),
                p("pair-parity", "Match the Parity", "EASY", "Print YES when both integers are even or both are odd, otherwise print NO.", "PAIR", "parity"),
                p("pair-order", "Compare Two Values", "EASY", "Print LESS, GREATER, or EQUAL according to the order of A and B.", "PAIR", "compare"),
                p("factorial", "Factorial Value", "EASY", "Compute N factorial, with 0! equal to 1.", "NUMBER", "factorial"),
                p("fibonacci", "Nth Fibonacci Number", "EASY", "Print F(N), where F(0)=0, F(1)=1, and each later term is the sum of the previous two.", "NUMBER", "fibonacci"),
                p("prime-check", "Prime Number Check", "EASY", "Determine whether N is prime. Values below 2 are not prime.", "NUMBER", "prime"),
                p("digit-sum", "Sum of Digits", "EASY", "Print the sum of the decimal digits of the non-negative integer N.", "NUMBER", "digit-sum"),
                p("digit-count", "Count Decimal Digits", "EASY", "Print the number of decimal digits in N. Zero has one digit.", "NUMBER", "digit-count"),
                p("reverse-number", "Reverse an Integer", "EASY", "Reverse the decimal digits of the non-negative integer N and drop leading zeroes in the result.", "NUMBER", "reverse-number"),
                p("integer-palindrome", "Integer Palindrome", "EASY", "Print YES if the non-negative integer reads the same forward and backward; otherwise print NO.", "NUMBER", "integer-palindrome"),
                p("power-of-two", "Power of Two", "EASY", "Print YES if N is a positive power of two, otherwise print NO.", "NUMBER", "power-of-two"),
                p("divisor-count", "Count Positive Divisors", "MEDIUM", "Print the number of positive divisors of N.", "NUMBER", "divisor-count"),
                p("decimal-binary", "Convert Decimal to Binary", "EASY", "Print the base-2 representation of the non-negative integer N without leading zeroes (except for zero itself).", "NUMBER", "binary"),
                p("array-sum", "Sum an Integer List", "EASY", "Print the sum of all N integers.", "ARRAY", "array-sum"),
                p("array-maximum", "Largest List Value", "EASY", "Print the largest value in the list.", "ARRAY", "array-max"),
                p("array-minimum", "Smallest List Value", "EASY", "Print the smallest value in the list.", "ARRAY", "array-min"),
                p("second-largest", "Second Distinct Maximum", "MEDIUM", "Print the second-largest distinct value. If it does not exist, print -1.", "ARRAY", "second-largest"),
                p("reverse-list", "Reverse a List", "EASY", "Print the list values in reverse order.", "ARRAY", "reverse-array"),
                p("count-even", "Count Even Values", "EASY", "Count values divisible by two, including zero and negative even values.", "ARRAY", "even-count"),
                p("move-zeroes", "Move Zeroes to the End", "EASY", "Preserve the relative order of non-zero values, then place all zeroes at the end.", "ARRAY", "move-zeroes"),
                p("maximum-subarray", "Best Contiguous Segment", "MEDIUM", "Find the largest possible sum of a non-empty contiguous segment.", "ARRAY", "max-subarray"),
                p("product-except-self", "Products Without Self", "MEDIUM", "For each position, print the product of all other values. Do not divide; inputs keep products within signed 64-bit range.", "ARRAY", "product-except-self"),
                p("increasing-run", "Longest Increasing Run", "MEDIUM", "Find the length of the longest contiguous strictly increasing run.", "ARRAY", "increasing-run"),
                p("target-count", "Count a Target Value", "EASY", "Given target T and a list, print how many list entries equal T.", "ARRAY_TARGET", "target-count"),
                p("target-first", "First Target Position", "EASY", "Print the zero-based index of the first occurrence of T, or -1 if T is absent.", "ARRAY_TARGET", "target-first"),
                p("rotate-right", "Rotate a List Right", "EASY", "Rotate the list to the right by K positions. K may exceed N.", "ARRAY_ROTATE", "rotate"),
                p("target-pairs", "Count Target-Sum Pairs", "MEDIUM", "Count index pairs i < j whose values sum to T. Equal-valued pairs at different indices count separately.", "ARRAY_TARGET", "target-pairs"),
                p("missing-zero", "Missing Value from Zero to N", "EASY", "The list contains N distinct values from 0 through N with one missing. Print the missing value.", "MISSING", "missing"),
                p("binary-search", "Binary Search Position", "EASY", "Search a sorted list for T and print its first zero-based index, or -1 if absent.", "SORTED_TARGET", "binary-search"),
                p("unique-count", "Count Distinct Values", "EASY", "Print the number of distinct integer values in the list.", "ARRAY", "unique-count"),
                p("dedupe-sorted", "Compress a Sorted List", "EASY", "Remove adjacent duplicates from the sorted list and print the remaining values in order.", "SORTED_ARRAY", "dedupe"),
                p("merge-sorted", "Merge Two Sorted Lists", "EASY", "Merge two non-decreasing lists into one non-decreasing list and print it.", "MERGE", "merge"),
                p("range-sum", "Answer Range Sums", "MEDIUM", "For each query L R, print the inclusive sum from index L through R. Indices are zero-based.", "RANGE", "range-sum"),
                p("reverse-string", "Reverse a Line", "EASY", "Print the input line with its characters in reverse order. Spaces are ordinary characters.", "STRING", "reverse-string"),
                p("string-palindrome", "Exact Text Palindrome", "EASY", "Print YES if the entire input line is a palindrome with exact case and spacing; otherwise print NO.", "STRING", "string-palindrome"),
                p("vowel-count", "Count Vowels in a Line", "EASY", "Count the English vowels a, e, i, o, u in either case.", "STRING", "vowels"),
                p("word-count", "Count Words", "EASY", "Count non-empty whitespace-separated words in the input line.", "STRING", "word-count"),
                p("reverse-words", "Reverse Word Order", "EASY", "Print the whitespace-separated words in reverse order, separated by one space.", "STRING", "reverse-words"),
                p("character-count", "Count a Character", "EASY", "Read a line and a character on the next line. Print the number of exact matches.", "STRING_TARGET", "character-count"),
                p("anagram-check", "Anagram Pair", "EASY", "Read two lines and print YES if they contain the same characters after ignoring whitespace and ASCII letter case; otherwise print NO.", "STRING_PAIR", "anagram"),
                p("longest-word", "Longest Word in a Line", "EASY", "Print the longest whitespace-separated word. If tied, print the earliest one; print an empty line when there are no words.", "STRING", "longest-word"),
                p("common-prefix", "Shared Starting Prefix", "MEDIUM", "Print the longest prefix shared by all N non-empty input words. If none is shared, print an empty line.", "STRING_LIST", "common-prefix"),
                p("balanced-brackets", "Balanced Brackets", "MEDIUM", "Check whether (), [], and {} are correctly nested and matched. Print YES or NO.", "STRING", "brackets")
        );

        return specs.stream().map(spec -> new SeedProblem(spec, casesFor(spec))).toList();
    }

    private static Spec p(String slug, String title, String difficulty, String description, String family, String operation) {
        String input = switch (family) {
            case "PAIR" -> "One line containing two signed integers A and B.";
            case "NUMBER" -> "One line containing a non-negative integer N.";
            case "ARRAY", "SORTED_ARRAY" -> "An integer N followed by N space-separated integers. For sorted-list tasks, values are non-decreasing.";
            case "ARRAY_TARGET", "SORTED_TARGET" -> "N and target T, followed by N space-separated integers. For binary search, the list is sorted.";
            case "ARRAY_ROTATE" -> "N and K, followed by N space-separated integers.";
            case "MISSING" -> "N followed by N distinct integers chosen from 0 through N.";
            case "MERGE" -> "N and M, followed by the N values and then the M values; both lists are sorted.";
            case "RANGE" -> "N and Q, followed by N integers and then Q pairs L R.";
            case "STRING" -> "One complete input line, which may be empty or contain spaces.";
            case "STRING_TARGET" -> "A complete text line followed by a one-character target line.";
            case "STRING_PAIR" -> "Two complete input lines.";
            case "STRING_LIST" -> "N followed by N non-empty words made of letters.";
            default -> "";
        };
        String output = switch (family) {
            case "ARRAY", "SORTED_ARRAY", "ARRAY_ROTATE", "MERGE" ->
                    "Print the requested integer value(s), separated by one space; finish with a newline.";
            case "RANGE" -> "Print one integer answer per query, each on its own line.";
            case "STRING", "STRING_LIST" -> "Print the requested text on one line.";
            default -> "Print the requested value on one line.";
        };
        String constraints = operation.equals("brackets")
                ? "The line contains only (, ), [, ], {, and } and has at most 2,000 characters."
                : switch (family) {
            case "PAIR" -> "-1,000,000 <= A, B <= 1,000,000.";
            case "NUMBER" -> "0 <= N <= 1,000,000; factorial tests use N <= 20 and Fibonacci tests use N <= 40.";
            case "STRING", "STRING_TARGET", "STRING_PAIR" -> "Each line contains at most 2,000 printable ASCII characters.";
            case "STRING_LIST" -> "1 <= N <= 100; each word has 1 to 100 lowercase English letters.";
            case "RANGE" -> "1 <= N <= 1,000; 1 <= Q <= 1,000; each query satisfies 0 <= L <= R < N.";
            case "MERGE" -> "0 <= N, M <= 1,000; both input lists are non-decreasing.";
            default -> "1 <= N <= 1,000; list values are between -1,000,000 and 1,000,000.";
        };
        return new Spec(slug, title, difficulty, description, input, output, constraints, operation);
    }

    private static List<TestInput> casesFor(Spec spec) {
        List<String> inputs = inputCases(spec.operation);
        if (inputs.size() != 10) throw new IllegalStateException("Every coding problem needs ten cases: " + spec.slug);
        List<TestInput> cases = new ArrayList<>();
        for (int i = 0; i < inputs.size(); i++) {
            cases.add(new TestInput(inputs.get(i), solve(spec.operation, inputs.get(i)), i >= 2));
        }
        return cases;
    }

    private static List<String> inputCases(String operation) {
        return switch (operation) {
            case "sum", "difference", "product", "maximum", "minimum", "distance", "gcd", "lcm", "parity", "compare" ->
                    List.of("7 3\n", "-4 9\n", "0 0\n", "-8 -12\n", "100 1\n",
                            "4 -4\n", "999 1000\n", "-1 1\n", "12345 6789\n", "42 58\n");
            case "factorial" -> nums(0, 1, 2, 3, 5, 7, 10, 12, 15, 20);
            case "fibonacci" -> nums(0, 1, 2, 3, 5, 10, 20, 30, 35, 40);
            case "prime" -> nums(0, 1, 2, 3, 4, 9, 17, 25, 97, 999);
            case "digit-sum", "digit-count", "reverse-number", "integer-palindrome", "binary" ->
                    nums(0, 1, 7, 10, 99, 1000, 12345, 900000, 10101, 999999);
            case "power-of-two" -> nums(0, 1, 2, 3, 8, 16, 31, 1024, 4096, 10000);
            case "divisor-count" -> nums(1, 2, 3, 4, 6, 9, 12, 25, 49, 100);
            case "array-sum", "array-max", "array-min", "second-largest", "reverse-array",
                    "even-count", "move-zeroes", "max-subarray", "product-except-self",
                    "increasing-run", "unique-count" -> List.of(
                    "1\n5\n", "4\n0 0 0 0\n", "5\n-1 3 -2 7 0\n", "6\n1 2 3 4 5 6\n",
                    "5\n9 8 7 6 5\n", "7\n-5 -2 -2 0 4 8 8\n", "3\n100 -100 0\n",
                    "8\n2 2 2 2 2 2 2 2\n", "4\n-9 -1 -4 -3\n", "5\n1 3 5 7 9\n");
            case "target-count", "target-first", "target-pairs" -> List.of(
                    "5 2\n1 2 2 3 2\n", "4 9\n9 9 9 9\n", "5 0\n-2 0 2 0 -2\n",
                    "1 7\n7\n", "6 -1\n-1 -1 0 1 -1 2\n", "5 4\n1 2 3 4 5\n",
                    "6 3\n3 3 3 3 3 3\n", "4 10\n-5 5 10 0\n", "5 1\n0 0 1 1 1\n",
                    "7 6\n1 2 3 4 5 6 7\n");
            case "rotate" -> List.of(
                    "1 0\n5\n", "4 1\n1 2 3 4\n", "5 2\n1 2 3 4 5\n", "3 3\n7 8 9\n",
                    "4 9\n-1 0 1 2\n", "5 0\n5 4 3 2 1\n", "6 7\n0 1 2 3 4 5\n",
                    "2 100\n-8 8\n", "3 1\n0 0 1\n", "5 4\n9 8 7 6 5\n");
            case "missing" -> List.of(
                    "1\n0\n", "1\n1\n", "2\n0 2\n", "2\n1 2\n", "3\n0 1 2\n",
                    "3\n0 2 3\n", "4\n1 2 3 4\n", "5\n0 1 2 3 5\n",
                    "7\n0 1 2 3 4 5 6\n", "6\n0 1 2 4 5 6\n");
            case "binary-search" -> List.of(
                    "1 5\n5\n", "4 9\n1 3 7 9\n", "5 0\n-5 -1 0 2 8\n", "1 7\n7\n",
                    "6 -1\n-4 -1 0 1 2 9\n", "5 4\n1 2 3 4 5\n", "6 3\n1 2 3 4 5 6\n",
                    "4 10\n-5 0 5 8\n", "5 1\n1 1 1 1 1\n", "7 6\n1 2 3 4 5 6 7\n");
            case "dedupe" -> List.of(
                    "1\n5\n", "4\n0 0 0 0\n", "5\n-2 -2 0 1 1\n", "6\n1 2 3 4 5 6\n",
                    "5\n-9 -4 -4 -4 0\n", "7\n-5 -2 -2 0 4 8 8\n", "3\n-1 0 1\n",
                    "8\n2 2 2 2 2 2 2 2\n", "4\n-9 -1 -1 3\n", "5\n1 3 5 7 9\n");
            case "merge" -> List.of(
                    "1 1\n1\n2\n", "0 2\n\n-1 3\n", "3 2\n-2 0 4\n-1 4\n",
                    "2 2\n1 1\n1 1\n", "4 0\n-9 -4 0 5\n\n", "0 0\n\n\n",
                    "3 3\n1 3 5\n2 4 6\n", "2 4\n-5 5\n-8 -3 0 9\n",
                    "1 4\n0\n0 0 0 1\n", "5 2\n1 2 3 4 5\n6 7\n");
            case "range-sum" -> List.of(
                    "1 1\n5\n0 0\n", "4 2\n1 2 3 4\n0 3\n1 2\n",
                    "5 3\n-2 0 2 4 6\n0 0\n1 3\n0 4\n", "3 2\n7 8 9\n1 1\n0 2\n",
                    "4 1\n-9 -4 0 5\n2 3\n", "5 2\n1 1 1 1 1\n0 4\n2 2\n",
                    "6 3\n-5 -2 0 4 8 10\n0 5\n2 4\n1 1\n", "2 1\n100 -100\n0 1\n",
                    "3 2\n0 0 1\n0 2\n2 2\n", "5 2\n9 8 7 6 5\n1 3\n3 4\n");
            case "reverse-string", "string-palindrome", "vowels", "word-count", "reverse-words", "longest-word", "brackets" ->
                    operation.equals("brackets")
                            ? List.of("\n", "()\n", "([])\n", "([)]\n", "{[()]}\n",
                                    "((\n", "([{}])\n", "}{\n", "[[]]\n", "())\n")
                            : List.of("\n", "a\n", "racecar\n", "Prep Track\n", "abca\n",
                                    "A man\n", "level\n", "12321\n", "one two three\n", "AaBb\n");
            case "character-count" -> List.of(
                    "\na\n", "a\na\n", "banana\na\n", "mississippi\ns\n", "Hello\nl\n",
                    "aaaa\nb\n", "123321\n2\n", "AaAa\na\n", "spaces here\n \n", "z\nz\n");
            case "anagram" -> List.of(
                    "\n\n", "a\na\n", "listen\nsilent\n", "Dormitory\nDirty room\n",
                    "abc\nabd\n", "aab\naba\n", "rail safety\nfairy tales\n", "A\n\n",
                    "conversation\nvoices rant on\n", "ab\nba \n");
            case "common-prefix" -> List.of(
                    "1\nalone\n", "3\nflower flow flight\n", "2\ndog race\n",
                    "3\ninterview internet internal\n", "4\nsame same same same\n",
                    "2\nx xyz\n", "3\ncar carpet carbon\n", "2\nalpha alphabet\n",
                    "3\napple apply application\n", "2\nzebra zen\n");
            default -> throw new IllegalArgumentException("Unknown operation " + operation);
        };
    }

    private static List<String> nums(long... values) {
        return Arrays.stream(values).mapToObj(value -> value + "\n").toList();
    }

    private static String solve(String operation, String input) {
        return switch (operation) {
            case "sum", "difference", "product", "maximum", "minimum", "distance", "gcd", "lcm", "parity", "compare" ->
                    solvePair(operation, input);
            case "factorial", "fibonacci", "prime", "digit-sum", "digit-count", "reverse-number",
                    "integer-palindrome", "power-of-two", "divisor-count", "binary" -> solveNumber(operation, input);
            case "array-sum", "array-max", "array-min", "second-largest", "reverse-array",
                    "even-count", "move-zeroes", "max-subarray", "product-except-self",
                    "increasing-run", "unique-count" -> solveArray(operation, input);
            case "target-count", "target-first", "target-pairs" -> solveTarget(operation, input);
            case "rotate" -> solveRotate(input);
            case "missing" -> solveMissing(input);
            case "binary-search" -> solveBinarySearch(input);
            case "dedupe" -> solveDedupe(input);
            case "merge" -> solveMerge(input);
            case "range-sum" -> solveRanges(input);
            case "reverse-string", "string-palindrome", "vowels", "word-count", "reverse-words", "longest-word", "brackets" ->
                    solveString(operation, input);
            case "character-count" -> solveCharacterCount(input);
            case "anagram" -> solveAnagram(input);
            case "common-prefix" -> solveCommonPrefix(input);
            default -> throw new IllegalArgumentException("Unknown operation " + operation);
        };
    }

    private static String solvePair(String operation, String input) {
        Scanner scanner = new Scanner(input);
        long a = scanner.nextLong(), b = scanner.nextLong();
        return switch (operation) {
            case "sum" -> Long.toString(a + b);
            case "difference" -> Long.toString(a - b);
            case "product" -> Long.toString(a * b);
            case "maximum" -> Long.toString(Math.max(a, b));
            case "minimum" -> Long.toString(Math.min(a, b));
            case "distance" -> Long.toString(Math.abs(a - b));
            case "gcd" -> Long.toString(gcd(Math.abs(a), Math.abs(b)));
            case "lcm" -> Long.toString(a == 0 || b == 0 ? 0 : Math.abs((a / gcd(Math.abs(a), Math.abs(b))) * b));
            case "parity" -> Math.floorMod(a, 2) == Math.floorMod(b, 2) ? "YES" : "NO";
            default -> a < b ? "LESS" : (a > b ? "GREATER" : "EQUAL");
        };
    }

    private static long gcd(long a, long b) {
        while (b != 0) {
            long next = a % b;
            a = b;
            b = next;
        }
        return a;
    }

    private static String solveNumber(String operation, String input) {
        long n = Long.parseLong(input.trim());
        return switch (operation) {
            case "factorial" -> {
                long result = 1;
                for (int i = 2; i <= n; i++) result *= i;
                yield Long.toString(result);
            }
            case "fibonacci" -> {
                long a = 0, b = 1;
                for (int i = 0; i < n; i++) {
                    long next = a + b;
                    a = b;
                    b = next;
                }
                yield Long.toString(a);
            }
            case "prime" -> isPrime(n) ? "YES" : "NO";
            case "digit-sum" -> Long.toString(Long.toString(n).chars().map(c -> c - '0').sum());
            case "digit-count" -> Integer.toString(Long.toString(n).length());
            case "reverse-number" -> Long.toString(Long.parseLong(new StringBuilder(Long.toString(n)).reverse().toString()));
            case "integer-palindrome" -> {
                String text = Long.toString(n);
                yield text.contentEquals(new StringBuilder(text).reverse()) ? "YES" : "NO";
            }
            case "power-of-two" -> n > 0 && (n & (n - 1)) == 0 ? "YES" : "NO";
            case "divisor-count" -> {
                int count = 0;
                for (long i = 1; i * i <= n; i++) {
                    if (n % i == 0) count += i * i == n ? 1 : 2;
                }
                yield Integer.toString(count);
            }
            default -> Long.toBinaryString(n);
        };
    }

    private static boolean isPrime(long n) {
        if (n < 2) return false;
        for (long i = 2; i * i <= n; i++) if (n % i == 0) return false;
        return true;
    }

    private static String solveArray(String operation, String input) {
        int[] values = readArray(input);
        return switch (operation) {
            case "array-sum" -> Long.toString(Arrays.stream(values).asLongStream().sum());
            case "array-max" -> Integer.toString(Arrays.stream(values).max().orElse(0));
            case "array-min" -> Integer.toString(Arrays.stream(values).min().orElse(0));
            case "second-largest" -> {
                int[] distinct = Arrays.stream(values).distinct().sorted().toArray();
                yield distinct.length < 2 ? "-1" : Integer.toString(distinct[distinct.length - 2]);
            }
            case "reverse-array" -> join(reverse(values));
            case "even-count" -> Long.toString(Arrays.stream(values).filter(value -> value % 2 == 0).count());
            case "move-zeroes" -> {
                List<Integer> result = new ArrayList<>();
                int zeros = 0;
                for (int value : values) if (value == 0) zeros++; else result.add(value);
                while (zeros-- > 0) result.add(0);
                yield join(result);
            }
            case "max-subarray" -> {
                long best = values[0], current = values[0];
                for (int i = 1; i < values.length; i++) {
                    current = Math.max(values[i], current + values[i]);
                    best = Math.max(best, current);
                }
                yield Long.toString(best);
            }
            case "product-except-self" -> {
                long[] output = new long[values.length];
                for (int i = 0; i < values.length; i++) {
                    long product = 1;
                    for (int j = 0; j < values.length; j++) if (i != j) product *= values[j];
                    output[i] = product;
                }
                yield join(output);
            }
            case "increasing-run" -> {
                int best = 1, current = 1;
                for (int i = 1; i < values.length; i++) {
                    current = values[i] > values[i - 1] ? current + 1 : 1;
                    best = Math.max(best, current);
                }
                yield Integer.toString(best);
            }
            default -> Long.toString(Arrays.stream(values).distinct().count());
        };
    }

    private static int[] readArray(String input) {
        Scanner scanner = new Scanner(input);
        int n = scanner.nextInt();
        int[] values = new int[n];
        for (int i = 0; i < n; i++) values[i] = scanner.nextInt();
        return values;
    }

    private static int[] reverse(int[] input) {
        int[] result = input.clone();
        for (int left = 0, right = result.length - 1; left < right; left++, right--) {
            int value = result[left];
            result[left] = result[right];
            result[right] = value;
        }
        return result;
    }

    private static String join(int[] values) {
        return Arrays.stream(values).mapToObj(Integer::toString).reduce((a, b) -> a + " " + b).orElse("");
    }

    private static String join(long[] values) {
        return Arrays.stream(values).mapToObj(Long::toString).reduce((a, b) -> a + " " + b).orElse("");
    }

    private static String join(List<Integer> values) {
        return values.stream().map(Object::toString).reduce((a, b) -> a + " " + b).orElse("");
    }

    private static String solveTarget(String operation, String input) {
        Scanner scanner = new Scanner(input);
        int n = scanner.nextInt(), target = scanner.nextInt();
        int[] values = new int[n];
        for (int i = 0; i < n; i++) values[i] = scanner.nextInt();
        if (operation.equals("target-count")) {
            return Long.toString(Arrays.stream(values).filter(value -> value == target).count());
        }
        if (operation.equals("target-first")) {
            for (int i = 0; i < n; i++) if (values[i] == target) return Integer.toString(i);
            return "-1";
        }
        int pairs = 0;
        for (int i = 0; i < n; i++) for (int j = i + 1; j < n; j++) {
            if (values[i] + values[j] == target) pairs++;
        }
        return Integer.toString(pairs);
    }

    private static String solveRotate(String input) {
        Scanner scanner = new Scanner(input);
        int n = scanner.nextInt(), k = scanner.nextInt();
        int[] values = new int[n];
        for (int i = 0; i < n; i++) values[i] = scanner.nextInt();
        int shift = k % n;
        int[] rotated = new int[n];
        for (int i = 0; i < n; i++) rotated[(i + shift) % n] = values[i];
        return join(rotated);
    }

    private static String solveMissing(String input) {
        int[] values = readArray(input);
        long expected = (long) values.length * (values.length + 1) / 2;
        return Long.toString(expected - Arrays.stream(values).asLongStream().sum());
    }

    private static String solveBinarySearch(String input) {
        Scanner scanner = new Scanner(input);
        int n = scanner.nextInt(), target = scanner.nextInt();
        int[] values = new int[n];
        for (int i = 0; i < n; i++) values[i] = scanner.nextInt();
        int left = 0, right = n;
        while (left < right) {
            int middle = left + (right - left) / 2;
            if (values[middle] < target) left = middle + 1;
            else right = middle;
        }
        return left == n || values[left] != target ? "-1" : Integer.toString(left);
    }

    private static String solveDedupe(String input) {
        int[] values = readArray(input);
        List<Integer> result = new ArrayList<>();
        for (int value : values) if (result.isEmpty() || result.get(result.size() - 1) != value) result.add(value);
        return join(result);
    }

    private static String solveMerge(String input) {
        Scanner scanner = new Scanner(input);
        int n = scanner.nextInt(), m = scanner.nextInt();
        int[] values = new int[n + m];
        for (int i = 0; i < n; i++) values[i] = scanner.nextInt();
        for (int i = n; i < n + m; i++) values[i] = scanner.nextInt();
        Arrays.sort(values);
        return join(values);
    }

    private static String solveRanges(String input) {
        Scanner scanner = new Scanner(input);
        int n = scanner.nextInt(), q = scanner.nextInt();
        int[] values = new int[n];
        for (int i = 0; i < n; i++) values[i] = scanner.nextInt();
        List<String> answers = new ArrayList<>();
        while (q-- > 0) {
            int left = scanner.nextInt(), right = scanner.nextInt();
            long sum = 0;
            for (int i = left; i <= right; i++) sum += values[i];
            answers.add(Long.toString(sum));
        }
        return String.join("\n", answers);
    }

    private static String solveString(String operation, String input) {
        String text = input.endsWith("\n") ? input.substring(0, input.length() - 1) : input;
        return switch (operation) {
            case "reverse-string" -> new StringBuilder(text).reverse().toString();
            case "string-palindrome" -> text.contentEquals(new StringBuilder(text).reverse()) ? "YES" : "NO";
            case "vowels" -> Long.toString(text.chars().filter(c -> "aeiouAEIOU".indexOf(c) >= 0).count());
            case "word-count" -> Integer.toString(words(text).length);
            case "reverse-words" -> {
                String[] words = words(text);
                Collections.reverse(Arrays.asList(words));
                yield String.join(" ", words);
            }
            case "longest-word" -> {
                String best = "";
                for (String word : words(text)) if (word.length() > best.length()) best = word;
                yield best;
            }
            default -> {
                Deque<Character> stack = new ArrayDeque<>();
                boolean balanced = true;
                for (char c : text.toCharArray()) {
                    if (c == '(' || c == '[' || c == '{') stack.push(c);
                    else {
                        if (stack.isEmpty()) { balanced = false; break; }
                        char open = stack.pop();
                        if ((c == ')' && open != '(') || (c == ']' && open != '[') || (c == '}' && open != '{')) {
                            balanced = false;
                            break;
                        }
                    }
                }
                yield balanced && stack.isEmpty() ? "YES" : "NO";
            }
        };
    }

    private static String[] words(String text) {
        return text.isBlank() ? new String[0] : text.trim().split("\\s+");
    }

    private static String solveCharacterCount(String input) {
        String[] lines = input.split("\\n", -1);
        String text = lines[0];
        String target = lines.length > 1 ? lines[1] : "";
        char character = target.isEmpty() ? '\0' : target.charAt(0);
        return Long.toString(text.chars().filter(c -> c == character).count());
    }

    private static String solveAnagram(String input) {
        String[] lines = input.split("\\n", -1);
        String first = lines[0].replaceAll("\\s", "").toLowerCase(Locale.ROOT);
        String second = (lines.length > 1 ? lines[1] : "").replaceAll("\\s", "").toLowerCase(Locale.ROOT);
        char[] a = first.toCharArray(), b = second.toCharArray();
        Arrays.sort(a);
        Arrays.sort(b);
        return Arrays.equals(a, b) ? "YES" : "NO";
    }

    private static String solveCommonPrefix(String input) {
        Scanner scanner = new Scanner(input);
        int n = scanner.nextInt();
        String[] words = new String[n];
        for (int i = 0; i < n; i++) words[i] = scanner.next();
        String prefix = words[0];
        for (int i = 1; i < words.length; i++) {
            while (!words[i].startsWith(prefix)) prefix = prefix.substring(0, prefix.length() - 1);
        }
        return prefix;
    }

    record TestInput(String input, String output, boolean hidden) {}

    record SeedProblem(Spec spec, List<TestInput> cases) {
        CodingProblem toEntity() {
            return CodingProblem.builder()
                    .slug(spec.slug()).title(spec.title()).difficulty(spec.difficulty())
                    .description(spec.description()).inputFormat(spec.inputFormat())
                    .outputFormat(spec.outputFormat()).constraints(spec.constraints())
                    .build();
        }
    }
}
