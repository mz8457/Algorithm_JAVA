import java.util.ArrayList;

/**
 * A Matching represents a candidate solution to the Stable Marriage problem. A Matching may or may
 * not be stable.
 */
public class Matching {
    /**
     * Number of internships.
     */
    private Integer m;

    /**
     * Number of students.
     */
    private Integer n;

    /**
     * A list containing each internship's preference list.
     */
    private ArrayList<ArrayList<Integer>> internship_preference;

    /**
     * A list containing each student's preference list.
     */
    private ArrayList<ArrayList<Integer>> student_preference;

    /**
     * Number of slots available in each internship.
     */
    private ArrayList<Integer> internship_slots;
    private ArrayList<ArrayList<Integer>> internship_weights;
    private ArrayList<Double> student_GPA;
    private ArrayList<Integer> student_months;
    private ArrayList<Integer> student_projects;
    /**
     * Matching information representing the index of internship a student is matched to, -1 if not
     * matched.
     *
     * <p>An empty matching is represented by a null value for this field.
     */
    private ArrayList<Integer> student_matching;

    public Matching(
            Integer m,
            Integer n,
            ArrayList<ArrayList<Integer>> internship_weights,
            ArrayList<ArrayList<Integer>> student_preference,
            ArrayList<Integer> internship_slots,
            ArrayList<Double> student_GPA,
            ArrayList<Integer> student_months,
            ArrayList<Integer> student_projects
    ) {
        this.m = m;
        this.n = n;
        this.internship_weights = internship_weights;
        this.student_preference = student_preference;
        this.internship_slots = internship_slots;
        this.student_matching = null;
        this.student_GPA = student_GPA;
        this.student_months = student_months;
        this.student_projects = student_projects;
        internship_preference = Program1.computeInternshipPreferences(
                getInternshipCount(),getStudentCount(),internship_weights,student_GPA,student_months,student_projects);
    }

    public Matching(
            Integer m,
            Integer n,
            ArrayList<ArrayList<Integer>> internship_weights,
            ArrayList<ArrayList<Integer>> internship_preference,
            ArrayList<ArrayList<Integer>> student_preference,
            ArrayList<Integer> internship_slots,
            ArrayList<Integer> student_matching,
            ArrayList<Double> student_GPA,
            ArrayList<Integer> student_months,
            ArrayList<Integer> student_projects) {
        this.m = m;
        this.n = n;
        this.internship_preference = internship_preference;
        this.internship_weights=internship_weights;
        this.student_preference = student_preference;
        this.internship_slots = internship_slots;
        this.student_matching = student_matching;
        this.student_GPA = student_GPA;
        this.student_months = student_months;
        this.student_projects = student_projects;

    }

    /**
     * Constructs a solution to the stable marriage problem, given the problem as a Matching. Take a
     * Matching which represents the problem data with no solution, and a student_matching which
     * solves the problem given in data.
     *
     * @param data              The given problem to solve.
     * @param student_matching The solution to the problem.
     */
    public Matching(Matching data, ArrayList<Integer> student_matching) {
        this(
                data.m,
                data.n,
                data.getInternshipWeights(),
                data.internship_preference,
                data.student_preference,
                data.internship_slots,
                student_matching,
                data.getStudentGPA(),
                data.getStudentMonths(),
                data.getStudentProjects());
    }

    /**
     * Creates a Matching from data which includes an empty solution.
     *
     * @param data The Matching containing the problem to solve.
     */
    public Matching(Matching data) {
        this(
                data.m,
                data.n,
                data.getInternshipWeights(),
                data.internship_preference,
                data.student_preference,
                data.internship_slots,
                new ArrayList<Integer>(0),
                data.getStudentGPA(),
                data.getStudentMonths(),
                data.getStudentProjects());
    }


    public void setStudentMatching(ArrayList<Integer> student_matching) {
        this.student_matching = student_matching;
    }

    public Integer getInternshipCount() {
        return m;
    }

    public Integer getStudentCount() {
        return n;
    }
    public void setInternshipPreference(ArrayList<ArrayList<Integer>> pref) {
        internship_preference = pref;

    }

    public ArrayList<ArrayList<Integer>> getInternshipWeights() {
        return internship_weights;
    }

    public ArrayList<ArrayList<Integer>> getInternshipPreference() {
        return internship_preference;
    }

    public ArrayList<ArrayList<Integer>> getStudentPreference() {
        return student_preference;
    }

    public ArrayList<Double> getStudentGPA(){
        return student_GPA;
    }

    public ArrayList<Integer> getStudentMonths(){
        return student_months;
    }

    public ArrayList<Integer> getStudentProjects(){
        return student_projects;
    }


    public ArrayList<Integer> getInternshipSlots() {
        return internship_slots;
    }

    public ArrayList<Integer> getStudentMatching() {
        return student_matching;
    }

    public int totalInternshipSlots() {
        int slots = 0;
        for (int i = 0; i < m; i++) {
            slots += internship_slots.get(i);
        }
        return slots;
    }

    public String getInputSizeString() {
        return String.format("m=%d n=%d\n", m, n);
    }

    public String getSolutionString() {
        if (student_matching == null) {
            return "";
        }

        StringBuilder s = new StringBuilder();
        for (int i = 0; i < student_matching.size(); i++) {
            String str = String.format("student %d internship %d", i, student_matching.get(i));
            s.append(str);
            if (i != student_matching.size() - 1) {
                s.append("\n");
            }
        }
        return s.toString();
    }



    public String toString() {
        return getInputSizeString() + getSolutionString();
    }
}
