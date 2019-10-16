public abstract class AbstractProgram1 {
    public abstract boolean isStableMatching(Matching marriage);

    /**
     * Brute force solution to the Stable Marriage problem. Relies on the function
     * isStableMatching(Matching) to determine whether a candidate Matching is stable.
     *
     * @return A stable Matching.
     */
    public Matching stableMarriageBruteForce(Matching marriage) {
        int n = marriage.getStudentCount();
        int slots = marriage.totalInternshipSlots();

        Permutation p = new Permutation(n, slots);
        Matching matching;
        while ((matching = p.getNextMatching(marriage)) != null) {
            if (isStableMatching(matching)) {
                return matching;
            }
        }

        return new Matching(marriage);
    }

    public abstract Matching stableMarriageBruteForce_studentoptimal(Matching marriage);

    public abstract Matching stableMarriageGaleShapley_studentoptimal(Matching marriage);
}
