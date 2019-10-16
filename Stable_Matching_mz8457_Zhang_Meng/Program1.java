/*
 * Name: <MengZhang>
 * EID: <mz8457>
 */

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.HashMap;

/**
 * Your solution goes in this class.
 * 
 * Please do not modify the other files we have provided for you, as we will use
 * our own versions of those files when grading your project. You are
 * responsible for ensuring that your solution works with the original version
 * of all the other files we have provided for you.
 * 
 * That said, please feel free to add additional files and classes to your
 * solution, as you see fit. We will use ALL of your additional files when
 * grading your solution.
 */
public class Program1 extends AbstractProgram1 {
    private int getPreferenceIndex(ArrayList<Integer> preferenceList, int entity) {
        int index = -1;
        for (int i = 0; i < preferenceList.size(); i++) {
            if (preferenceList.get(i) == entity) {
                index = i;
                break;
            }
        }
        return index;
    }
    /**
     * Compute the preference lists for each internship, given weights and student metrics.
     * Return a ArrayList<ArrayList<Integer>> prefs, where prefs.get(i) is the ordered list of preferred students for
     * internship i, with length studentCount.
     */
    public static ArrayList<ArrayList<Integer>> computeInternshipPreferences(int internshipCount, int studentCount,
                                                                      ArrayList<ArrayList<Integer>>internship_weights,
                                                                      ArrayList<Double> student_GPA,
                                                                      ArrayList<Integer> student_months,
                                                                      ArrayList<Integer> student_projects){
        ArrayList<ArrayList<Integer>> internshipPref = new ArrayList<>(internshipCount);
        for(int i=0; i<internshipCount; i++) {
        		ArrayList<Integer> intern_stu_pref = new ArrayList<>(studentCount);
        		HashMap<Integer, Double> stu_score = new HashMap<>();
        		for(int j=0; j<studentCount; j++) {
        			double score = student_GPA.get(j)*internship_weights.get(i).get(0)+student_months.get(j)*internship_weights.get(i).get(1)+student_projects.get(i)*internship_weights.get(i).get(2);
        			stu_score.put(j, score);
        		}
        		ArrayList<HashMap.Entry<Integer, Double>> stu_score_list = new ArrayList<HashMap.Entry<Integer, Double>>(stu_score.entrySet());
        		Collections.sort(stu_score_list, new Comparator<HashMap.Entry<Integer, Double>>() {
        	           @Override
        	           public int compare(HashMap.Entry<Integer, Double> o1, HashMap.Entry<Integer, Double> o2) {
        	               return o2.getValue().compareTo(o1.getValue());
        	           }
        	       });
        		for (int k = 0; k < stu_score_list.size(); k++) {
                    intern_stu_pref.add(stu_score_list.get(k).getKey());
                }      
        		internshipPref.add(intern_stu_pref);
        }

        return internshipPref;
    }
    private static Double computeInternshipStudentScore(double studentGPA, int studentExp, int studentProjects, int
                                                        weightGPA, int weightExp, int weightProjects){
        return studentGPA*weightGPA+studentExp*weightExp+studentProjects*weightProjects;
    }

    /**
     * Determines whether a candidate Matching represents a solution to the Stable Marriage problem.
     * Study the description of a Matching in the project documentation to help you with this.
     */
    public boolean isStableMatching(Matching marriage) {
        /* TODO implement this function */
    		int n = marriage.getStudentCount();
    		int m = marriage.getInternshipCount();
    		ArrayList<Integer> s_matching = marriage.getStudentMatching();
    		ArrayList<ArrayList<Integer>> s_preference = marriage.getStudentPreference();
    		ArrayList<Integer> slot = marriage.getInternshipSlots();
    		ArrayList<ArrayList<Integer>> intern_preference = marriage.getInternshipPreference();
    		HashMap<Integer, Integer> accepted_slot = new HashMap<>();
    		for(int i=0; i<s_matching.size(); i++) {
    			if(s_matching.get(i) != -1 && accepted_slot.containsKey(s_matching.get(i))) {
    				accepted_slot.put(s_matching.get(i), accepted_slot.get(s_matching.get(i))+1);
    			}else if(s_matching.get(i) != -1 && !accepted_slot.containsKey(s_matching.get(i))){
    				accepted_slot.put(s_matching.get(i), accepted_slot.getOrDefault(s_matching.get(i),0)+1);
    			}
    		}
    		for(int i=0; i<m; i++) {
    			if(!accepted_slot.containsKey(i) && slot.get(i)!=0 || accepted_slot.containsKey(i) && accepted_slot.get(i) != slot.get(i) ) {
    				return false;
    			}
    			
    		}
    		HashMap<Integer, Integer> stu_matching = new HashMap<Integer, Integer>();
    		for(int i=0; i<s_matching.size(); i++) {
    			stu_matching.put(i, s_matching.get(i));
    		}
    		for(int i=0; i<n; i++) {
    			int intern_now = stu_matching.get(i);
    			if(intern_now == -1) {
    				for(int j=0; j<m; j++) {
    					for(int k=intern_preference.get(j).indexOf(i)+1; k<intern_preference.get(j).size(); k++) {
    						if(stu_matching.containsKey(intern_preference.get(j).get(k)) && stu_matching.get(intern_preference.get(j).get(k)) == j) {
    							return false;
    						}
    					}
    				}
    			}else {
    				for(int p=0; p<s_preference.get(i).indexOf(intern_now); p++) {
    					if(stu_matching.containsValue(s_preference.get(i).get(p))) {
    						for(int key: stu_matching.keySet()) {
    							if(stu_matching.get(key) == s_preference.get(i).get(p)) {
    								int key_rank_in_internpre = intern_preference.get(s_preference.get(i).get(p)).indexOf(key);
    								if(key_rank_in_internpre > intern_preference.get(s_preference.get(i).get(p)).indexOf(i)) {
    									return false;
    								}
    							}
    						}
    					}
    				}
    			}
    		}
    		return true;	
        
    } 

    /**
     * Determines a solution to the Stable Marriage problem from the given input set. Study the
     * project description to understand the variables which represent the input to your solution.
     */
    @Override
    public Matching stableMarriageGaleShapley_studentoptimal(Matching marriage) {
        /* TODO implement this function */
    		int n = marriage.getStudentCount();
    		int m = marriage.getInternshipCount();
    		ArrayList<Integer> gs_matching = new ArrayList<>();
    		HashMap<Integer, Integer> h_gs_matching = new HashMap<>();
    		ArrayList<Integer> waiting_list = new ArrayList<>();
    		ArrayList<ArrayList<Integer>> stu_pro_intern = new ArrayList<ArrayList<Integer>>(n);
    		ArrayList<ArrayList<Integer>> s_pref = marriage.getStudentPreference();
    		ArrayList<Integer> slot = marriage.getInternshipSlots();
    		ArrayList<ArrayList<Integer>> intern_pref = marriage.getInternshipPreference();
    		int stu = 0;
    		while(stu < n) {
    			stu_pro_intern.add(new ArrayList<Integer>());
    			for(int j=0; j<m; j++) {
    				int cur_intern = s_pref.get(stu).get(j);
    				stu_pro_intern.get(stu).add(cur_intern);
    				int match_intern_count = 0;
    				for(int key: h_gs_matching.keySet()) {
    					if(h_gs_matching.get(key) == cur_intern) {
    						match_intern_count++;
    					}
    				}
    				if(!h_gs_matching.containsValue(cur_intern) && slot.get(cur_intern)!=0 || h_gs_matching.containsValue(cur_intern) && slot.get(cur_intern) != match_intern_count) {
    					h_gs_matching.put(stu, cur_intern);
    					break;
    				}else if(h_gs_matching.containsValue(cur_intern) && slot.get(cur_intern) == match_intern_count){
    					int stu_rank = intern_pref.get(cur_intern).indexOf(stu);
    					int low_pref_stu_rank = 0;
    					int low_pref_stu = intern_pref.get(cur_intern).get(0);
    					for(int key: h_gs_matching.keySet()) {
        					if(h_gs_matching.get(key) == cur_intern) {
        						if(intern_pref.get(cur_intern).indexOf(key) > low_pref_stu_rank) {
        							low_pref_stu_rank = intern_pref.get(cur_intern).indexOf(key);
        							low_pref_stu = key;
        						}
        					}
        				}
    					if(stu_rank < low_pref_stu_rank) {
    						h_gs_matching.put(stu, cur_intern);
    						h_gs_matching.remove(low_pref_stu);
    						waiting_list.add(low_pref_stu);
    						break;
    					}
    				}
    			}
    			stu++;
    		}
    		while(!waiting_list.isEmpty()) {
    			for(int i=0; i<waiting_list.size(); i++) {
        			if(stu_pro_intern.get(waiting_list.get(i)).size() == m) {
        				waiting_list.remove(i);
        			}
        		}
    			for(int i=0; i<waiting_list.size();i++) {
    					for(int j=stu_pro_intern.get(waiting_list.get(i)).size(); j<m; j++) {
    						int cur_intern = s_pref.get(waiting_list.get(i)).get(j);
    	    					stu_pro_intern.get(waiting_list.get(i)).add(cur_intern);
    	    					int match_intern_count = 0;
    	        				for(int key: h_gs_matching.keySet()) {
    	        					if(h_gs_matching.get(key) == cur_intern) {
    	        						match_intern_count++;
    	        					}
    	        				}
    	        				if(!h_gs_matching.containsValue(cur_intern) && slot.get(cur_intern)!=0 || h_gs_matching.containsValue(cur_intern) && slot.get(cur_intern) != match_intern_count) {
    	        					h_gs_matching.put(waiting_list.get(i), cur_intern);
    	        					waiting_list.remove(i);
    	        					break;
    	        				}else if(h_gs_matching.containsValue(cur_intern) && slot.get(cur_intern) == match_intern_count){
    	        					int stu_rank = intern_pref.get(cur_intern).indexOf(waiting_list.get(i));
    	        					int low_pref_stu_rank = 0;
    	        					int low_pref_stu = intern_pref.get(cur_intern).get(0);
    	        					for(int key: h_gs_matching.keySet()) {
    	            					if(h_gs_matching.get(key) == cur_intern) {
    	            						if(intern_pref.get(cur_intern).indexOf(key) > low_pref_stu_rank) {
    	            							low_pref_stu_rank = intern_pref.get(cur_intern).indexOf(key);
    	            							low_pref_stu = key;
    	            						}
    	            					}
    	            				}
    	        					if(stu_rank < low_pref_stu_rank) {
    	        						h_gs_matching.put(waiting_list.get(i), cur_intern);
    	        						h_gs_matching.remove(low_pref_stu);
    	        						waiting_list.add(low_pref_stu);
    	        						waiting_list.remove(i);
    	        						break;
    	        					}
    	        				}

    					}
    			}
    		} 
    		for(int i=0; i<n; i++) {
    			if(h_gs_matching.containsKey(i)) {
    				gs_matching.add(h_gs_matching.get(i));
    			}else {
    				gs_matching.add(-1);
    			}
    		}
    		marriage.setStudentMatching(gs_matching);
    		return marriage;
    		
    }

    private ArrayList<Matching> getAllStableMarriages(Matching marriage) {
        ArrayList<Matching> marriages = new ArrayList<>();
        int n = marriage.getStudentCount();
        int slots = marriage.totalInternshipSlots();

        Permutation p = new Permutation(n, slots);
        Matching matching;
        while ((matching = p.getNextMatching(marriage)) != null) {
            if (isStableMatching(matching)) {
                marriages.add(matching);
            }
        }

        return marriages;
    }

    @Override
    public Matching stableMarriageBruteForce_studentoptimal(Matching marriage) {
        ArrayList<Matching> allStableMarriages = getAllStableMarriages(marriage);
        Matching studentOptimal = null;
        int n = marriage.getStudentCount();
        int m = marriage.getInternshipCount();
        System.out.println("student" + n + "internship" + m);
        ArrayList<ArrayList<Integer>> student_preference = marriage.getStudentPreference();

        //Construct an inverse list for constant access time
        ArrayList<ArrayList<Integer>> inverse_student_preference = new ArrayList<ArrayList<Integer>>(0) ;
        for (Integer i=0; i<n; i++) {
            ArrayList<Integer> inverse_preference_list = new ArrayList<Integer>(m) ;
            for (Integer j=0; j<m; j++)
                inverse_preference_list.add(-1) ;
            ArrayList<Integer> preference_list = student_preference.get(i) ;

            for (int j=0; j<m; j++) {
                inverse_preference_list.set(preference_list.get(j), j) ;
            }
            inverse_student_preference.add(inverse_preference_list) ;
        }

        // bestStudentMatching stores the rank of the best Internship each student matched to
        // over all stable matchings
        int[] bestStudentMatching = new int[marriage.getStudentCount()];
        Arrays.fill(bestStudentMatching, -1);
        for (Matching mar : allStableMarriages) {
            ArrayList<Integer> student_matching = mar.getStudentMatching();
            for (int i = 0; i < student_matching.size(); i++) {
                if (student_matching.get(i) != -1 && (bestStudentMatching[i] == -1 ||
                        inverse_student_preference.get(i).get(student_matching.get(i)) < bestStudentMatching[i])) {
                    bestStudentMatching[i] = inverse_student_preference.get(i).get(student_matching.get(i));
                    studentOptimal = mar;
                }
            }
        }

        return studentOptimal;
    }
}
