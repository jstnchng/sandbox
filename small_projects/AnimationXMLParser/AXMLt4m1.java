import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Calendar;
import java.io.BufferedWriter;
import java.text.SimpleDateFormat;
import java.nio.file.StandardOpenOption;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class AXMLt4m1{
    private static String firstPart = "<scene>\n<duration time=\"5.0\"/>\n"
        + "<integrator type=\"symplectic-euler\" dt=\"0.005\"/>\n"
        + "<maxsimfreq max=\"500.0\"/>\n";
        // + "<viewport cx=\"150.0\" cy=\"150.0\" size=\"150\"/>\n"
    private static String finalLine = "</scene>";

    public static String generateBox(){
        StringBuilder box = new StringBuilder();
        HashMap<Integer, double[]> colors = new HashMap<>();
        colors.put(0, new double[]{1,0,0});
        colors.put(1, new double[]{1, .498, 0});
        colors.put(2, new double[]{1,1,0});
        colors.put(3, new double[]{0,1,0});
        colors.put(4, new double[]{0,0,1});
        colors.put(5, new double[]{.294,0,.5098});
        colors.put(6, new double[]{.58,0,.8274});
        colors.put(7, new double[]{0,0,0});

        int px = 400;
        int px_h = 200;
        int py = 250;
        int py_h = 125;
        int particleIndex = 0;
        int radius = 10;
        int offset = 25;
        double r = colors.get(particleIndex)[0];
        double g = colors.get(particleIndex)[1];
        double b = colors.get(particleIndex)[2];
        box.append("<particle m=\"1.0\" px=\"" + "0"
                    + "\" py=\"" + "0" + "\" vx=\"0.0\" vy=\"0.0\"" +
                    " fixed=\"0\" radius=\"" + radius +"\"/>\n");
        box.append("<particlecolor i=\"" + particleIndex
                    + "\" r=\"" + r + "\" g=\"" + g + "\" b=\"" + b + "\"/>\n");
        particleIndex++;

        r = colors.get(particleIndex)[0];
        g = colors.get(particleIndex)[1];
        b = colors.get(particleIndex)[2];
        int py_bottom = offset;
        box.append("<particle m=\"1.0\" px=\"" + px_h
                    + "\" py=\"" + py_bottom + "\" vx=\"0.0\" vy=\"0.0\"" +
                    " fixed=\"0\" radius=\""+radius+"\"/>\n");
        box.append("<particlecolor i=\"" + particleIndex
                    + "\" r=\"" + r + "\" g=\"" + g + "\" b=\"" + b + "\"/>\n");
        particleIndex++;

        r = colors.get(particleIndex)[0];
        g = colors.get(particleIndex)[1];
        b = colors.get(particleIndex)[2];
        box.append("<particle m=\"1.0\" px=\"" + px
                    + "\" py=\"" + "0" + "\" vx=\"0.0\" vy=\"0.0\"" +
                    " fixed=\"0\" radius=\""+radius+"\"/>\n");
        box.append("<particlecolor i=\"" + particleIndex
                    + "\" r=\"" + r + "\" g=\"" + g + "\" b=\"" + b + "\"/>\n");
        particleIndex++;

        r = colors.get(particleIndex)[0];
        g = colors.get(particleIndex)[1];
        b = colors.get(particleIndex)[2];
        int px_right = px - offset;
        box.append("<particle m=\"1.0\" px=\"" + px_right
                    + "\" py=\"" + py_h + "\" vx=\"0.0\" vy=\"0.0\"" +
                    " fixed=\"0\" radius=\"" + radius + "\"/>\n");
        box.append("<particlecolor i=\"" + particleIndex
                    + "\" r=\"" + r + "\" g=\"" + g + "\" b=\"" + b + "\"/>\n");
        particleIndex++;

        r = colors.get(particleIndex)[0];
        g = colors.get(particleIndex)[1];
        b = colors.get(particleIndex)[2];
        box.append("<particle m=\"1.0\" px=\"" + px
                    + "\" py=\"" + py + "\" vx=\"0.0\" vy=\"0.0\"" +
                    " fixed=\"0\" radius=\""+radius+"\"/>\n");
        box.append("<particlecolor i=\"" + particleIndex
                    + "\" r=\"" + r + "\" g=\"" + g + "\" b=\"" + b + "\"/>\n");
        particleIndex++;

        r = colors.get(particleIndex)[0];
        g = colors.get(particleIndex)[1];
        b = colors.get(particleIndex)[2];
        int py_top = py - offset;
        box.append("<particle m=\"1.0\" px=\"" + px_h
                    + "\" py=\"" + py_top + "\" vx=\"0.0\" vy=\"0.0\"" +
                    " fixed=\"0\" radius=\""+radius+"\"/>\n");
        box.append("<particlecolor i=\"" + particleIndex
                    + "\" r=\"" + r + "\" g=\"" + g + "\" b=\"" + b + "\"/>\n");
        particleIndex++;

        r = colors.get(particleIndex)[0];
        g = colors.get(particleIndex)[1];
        b = colors.get(particleIndex)[2];
        box.append("<particle m=\"1.0\" px=\"" + "0"
                    + "\" py=\"" + py + "\" vx=\"0.0\" vy=\"0.0\"" +
                    " fixed=\"0\" radius=\""+radius+"\"/>\n");
        box.append("<particlecolor i=\"" + particleIndex
                    + "\" r=\"" + r + "\" g=\"" + g + "\" b=\"" + b + "\"/>\n");
        particleIndex++;

        r = colors.get(particleIndex)[0];
        g = colors.get(particleIndex)[1];
        b = colors.get(particleIndex)[2];
        int px_left = offset;
        box.append("<particle m=\"1.0\" px=\"" + px_left
                    + "\" py=\"" + py_h + "\" vx=\"0.0\" vy=\"0.0\"" +
                    " fixed=\"0\" radius=\""+radius+"\"/>\n");
        box.append("<particlecolor i=\"" + particleIndex
                    + "\" r=\"" + r + "\" g=\"" + g + "\" b=\"" + b + "\"/>\n");
        particleIndex++;

        for(int i=0; i<particleIndex-1;i++){
            int next = i+1;
            box.append("<edge i=\"" + i + "\" j=\"" + next + "\" radius=\"1\"/>\n");
            box.append("<elasticbodyspringforce i1=\"" + i + "\" i2=\"" + next + "\" alpha=\"100.0\" l0=\"0.6\"/>\n");
        }
        box.append("<edge i=\"" + "7" + "\" j=\"" + "0" + "\" radius=\"1\"/>\n");
        box.append("<elasticbodyspringforce i1=\"" + "7" + "\" i2=\"" + "0" + "\" alpha=\"100.0\" l0=\"0.6\"/>\n");

        for(int i=0; i<particleIndex-2; i=i+2){
            int next = i+1;
            int nextt = i+2;
            box.append("<elasticbodybendingforce i1=\"" + i + "\" i2=\"" + next + "\" i3=\"" + nextt + "\" alpha=\"0.01\" theta0=\"0.0\" />\n");
        }
        box.append("<elasticbodybendingforce i1=\"" + "6" + "\" i2=\"" + "7" + "\" i3=\"" + "0" + "\" alpha=\"0.01\" theta0=\"0.0\" />\n");
        return box.toString();
    }

    public static void main(String[] args){
        String outputfilename = args[0];
        Charset charset = Charset.forName("UTF-8");
        try {
            Path outputfile = Paths.get(outputfilename);
            try (BufferedWriter writer = Files.newBufferedWriter(outputfile, charset, StandardOpenOption.APPEND)) {
                writer.write(firstPart, 0, firstPart.length());
                String box = generateBox();
                writer.write(box,0,box.length());
                writer.write(finalLine, 0, finalLine.length());
            } catch (IOException e2) {
               e2.printStackTrace();
            }
        // } catch (IOException e1) {
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
}
