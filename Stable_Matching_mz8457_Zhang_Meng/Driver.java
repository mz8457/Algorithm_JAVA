import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Driver {
    public static String filename;
    public static boolean testBruteForce_r;
    public static boolean testGS_r;

    public static void main(String[] args) throws Exception {
        parseArgs(args);

        Matching problem = parseMatchingProblem(filename);

        testRun(problem);
    }

    private static void usage() {
        System.err.println("usage: java Driver [-g] [-b] <filename>");
        System.err.println("\t-b\tTest brute force implementation");
        System.err.println("\t-gr\tTest Gale-Shapley student optimal implementation");
        System.exit(1);
    }

    public static void parseArgs(String[] args) {
        if (args.length == 0) {
            usage();
        }
        filename = "";
        testBruteForce_r = false;
        testGS_r = false;
        for (String s : args) {
            if (s.equals("-g")) {
                testGS_r = true;
            } else if (s.equals("-b")) {
                testBruteForce_r = true;
            } else if (!s.startsWith("-")) {
                filename = s;
            }
        }
    }

    public static Matching parseMatchingProblem(String inputFile) throws Exception {
        int m = 0;
        int n = 0; //num students
        ArrayList<ArrayList<Integer>> internshipWeights, studentPrefs;
        ArrayList<Integer> internshipSlots;
        ArrayList<Double> studentGPA;
        ArrayList<Integer> studentMonths;
        ArrayList<Integer> studentProjects;

        Scanner sc = new Scanner(new File(inputFile));
        String[] inputSizes = sc.nextLine().split(" ");

        m = Integer.parseInt(inputSizes[0]);
        n = Integer.parseInt(inputSizes[1]);
        internshipSlots = readSlotsList(sc, m);
        studentGPA = readGPAList(sc, n);
        studentMonths = readMonthsList(sc, n);
        studentProjects = readProjectList(sc , n);
        internshipWeights = readPreferenceLists(sc, m);
        studentPrefs = readPreferenceLists(sc, n);

        Matching problem = new Matching(m, n, internshipWeights, studentPrefs, internshipSlots, studentGPA, studentMonths, studentProjects);
        return problem;
    }


    public static ArrayList<Double> readGPAList(Scanner sc, int n) {
        ArrayList<Double> gpa = new ArrayList<Double>(0);

        String[] slots = sc.nextLine().split(" ");
        for (int i = 0; i < n; i++) {
            gpa.add(Double.parseDouble(slots[i]));
        }
        return gpa;

    }
    public static ArrayList<Integer> readMonthsList(Scanner sc, int n) {
        ArrayList<Integer> months = new ArrayList<Integer>(0);

        String[] slots = sc.nextLine().split(" ");
        for (int i = 0; i < n; i++) {
            months.add(Integer.parseInt(slots[i]));
        }
        return months;

    }
    public static ArrayList<Integer> readProjectList(Scanner sc, int n) {
        ArrayList<Integer> proj = new ArrayList<Integer>(0);

        String[] slots = sc.nextLine().split(" ");
        for (int i = 0; i < n; i++) {
            proj.add(Integer.parseInt(slots[i]));
        }
        return proj;

    }
    public static ArrayList<Integer> readSlotsList(Scanner sc, int m) {
        ArrayList<Integer> internshipSlots = new ArrayList<Integer>(0);

        String[] slots = sc.nextLine().split(" ");
        for (int i = 0; i < m; i++) {
            internshipSlots.add(Integer.parseInt(slots[i]));
        }

        return internshipSlots;
    }

    public static ArrayList<ArrayList<Integer>> readPreferenceLists(Scanner sc, int m) {
        ArrayList<ArrayList<Integer>> preferenceLists;
        preferenceLists = new ArrayList<ArrayList<Integer>>(0);

        for (int i = 0; i < m; i++) {
            String line = sc.nextLine();
            String[] preferences = line.split(" ");
            ArrayList<Integer> preferenceList = new ArrayList<Integer>(0);
            for (Integer j = 0; j < preferences.length; j++) {
                preferenceList.add(Integer.parseInt(preferences[j]));
            }
            preferenceLists.add(preferenceList);
        }

        return preferenceLists;
    }

    public static void testRun(Matching problem) {
        Program1 program = new Program1();
        boolean isStable;
        if (testGS_r) {
            Matching GSMatching = program.stableMarriageGaleShapley_studentoptimal(problem);
            System.out.println(GSMatching);
            isStable = program.isStableMatching(GSMatching);
            System.out.printf("%s: stable? %s\n", "Gale-Shapley Student Optimal", isStable);
            System.out.println();
        }
        if (testBruteForce_r) {
            Matching BFMatching = program.stableMarriageBruteForce_studentoptimal(problem);
            System.out.println(BFMatching);
            isStable = program.isStableMatching(BFMatching);
            System.out.printf("%s: stable? %s\n", "Brute Force Student Optimal", isStable);
            System.out.println();
        }
    }
}
