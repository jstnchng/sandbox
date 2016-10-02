import java.util.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Filereader{
    // take in some input, 1 argument
    // read through line by line, if there is no #include then just print it out
    // if there is an include then take the 2nd argument (String split by space)
    // run the method again with the new input
    public static HashSet<String> filesRead = new HashSet<>();

    public static void readFile(String f){
        filesRead.add(f);
        try( BufferedReader reader = Files.newBufferedReader(Paths.get(f))) {
            String line = null;
            while ( (line=reader.readLine()) != null){
                if(line.indexOf("#include") == -1){
                    System.out.println(line);
                } else {
                    String[] newFile = line.split(" ");
                    if(newFile[1].equals(f) || filesRead.contains(newFile[1])){
                        throw new IllegalArgumentException("recursively called on same file or called in a loop!!");
                    } else {
                        readFile(newFile[1]);
                    }
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        ArrayList<String> lines = new ArrayList<>();
        String inputfilename = args[0];
        readFile(inputfilename);
    }
}
