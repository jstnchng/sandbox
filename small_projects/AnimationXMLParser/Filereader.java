public class Filereader{
    // take in some input, 1 argument
    // read through line by line, if there is no #include then just print it out
    // if there is an include then take the 2nd argument (String split by space)
    // run the method again with the new input

    public void readFile(String f){
        BufferedReader reader = Files.newBufferedReader(Paths.get(inputfilename));
        String line = null;
        while ( (line=reader.readline() != null)){
            if(line.indexOf("#include") == -1){
                System.out.println(line);
            } else {
                String[] newFile = line.split(" ");
                readFile(newFile[1]);
            }
        }
    }

    public static void main(String[] args){
        ArrayList<String> lines = new ArrayList<>();
        String inputfilename = args[0];
        readFile(inputfilename);
    }
}
