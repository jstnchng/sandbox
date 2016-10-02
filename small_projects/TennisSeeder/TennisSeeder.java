import java.util.*;

public class TennisSeeder{
    public static void seeder(int num_seeds){
        // create arraylist from num_seeds
        // until size of arraylist is 1
        // squash by taking leftmost and rightmost
        // parentheses on leftmost, add a comma, add the rightmost, add the parentheses on the rightmost

        List<String> presquash = new ArrayList<>();
        for(int i=1; i<num_seeds+1; i++){
            presquash.add(String.valueOf(i));
        }

        List<String> postsquash = new ArrayList<>();
        while(postsquash.size() != 1){
            postsquash = new ArrayList<>();
            while(!presquash.isEmpty()){
                String matches = "(" + presquash.remove(0) + "," + presquash.remove(presquash.size()-1) + ")";
                postsquash.add(matches);
            }
            presquash = postsquash;
        }
        System.out.println(postsquash.get(0));
    }

    public static void main(String[] args){
        seeder(2);
        seeder(4);
        seeder(8);
        seeder(16);
    }
}
