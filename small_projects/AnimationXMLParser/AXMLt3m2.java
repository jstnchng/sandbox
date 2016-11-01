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

public class AXMLt3m2{
    private static String firstPart = "<scene>\n<duration time=\"5.0\"/>\n"
        + "<simtype type=\"rigid-body\"/>"
        + "<rigidbodyintegrator type=\"symplectic-euler\" dt=\"0.005\"/>\n"
        + "<maxsimfreq max=\"500000.0\"/>\n"
        // + "<viewport cx=\"150.0\" cy=\"150.0\" size=\"150\"/>\n"
        + "<rigidbodycollisionhandling detection=\"all-pairs\" response=\"velocity-projection\"/>\n";
    private static String finalLine = "</scene>";

    public static String generateBox(){
        StringBuilder box = new StringBuilder();
        String r="1.0", g="1.0", b="1.0";

        int px = 300;
        int py = 300;
        int particleIndex = 0;
        int radius = 10;
        box.append("<particle m=\"1.0\" px=\"" + "0"
                    + "\" py=\"" + py + "\" vx=\"0.0\" vy=\"0.0\"" +
                    " fixed=\"1\" radius=\"" + radius + "\"/>\n");
        box.append("<particlecolor i=\"" + particleIndex
                    + "\" r=\"" + r + "\" g=\"" + g + "\" b=\"" + b + "\"/>\n");
        particleIndex++;
        box.append("<particle m=\"1.0\" px=\"" + "0"
                    + "\" py=\"" + "0" + "\" vx=\"0.0\" vy=\"0.0\"" +
                    " fixed=\"1\" radius=\"" + radius +"\"/>\n");
        box.append("<particlecolor i=\"" + particleIndex
                    + "\" r=\"" + r + "\" g=\"" + g + "\" b=\"" + b + "\"/>\n");
        particleIndex++;
        box.append("<particle m=\"1.0\" px=\"" + px
                    + "\" py=\"" + "0" + "\" vx=\"0.0\" vy=\"0.0\"" +
                    " fixed=\"1\" radius=\""+radius+"\"/>\n");
        box.append("<particlecolor i=\"" + particleIndex
                    + "\" r=\"" + r + "\" g=\"" + g + "\" b=\"" + b + "\"/>\n");
        particleIndex++;
        box.append("<particle m=\"1.0\" px=\"" + px
                    + "\" py=\"" + py + "\" vx=\"0.0\" vy=\"0.0\"" +
                    " fixed=\"1\" radius=\""+radius+"\"/>\n");
        box.append("<particlecolor i=\"" + particleIndex
                    + "\" r=\"" + r + "\" g=\"" + g + "\" b=\"" + b + "\"/>\n");
        particleIndex++;
        for(int i=0; i<particleIndex-1;i++){
            int next = i+1;
            box.append("<edge i=\"" + i + "\" j=\"" + next + "\" radius=\"10\"/>\n");
        }
        box.append("<edge i=\"" + "3" + "\" j=\"" + "0" + "\" radius=\"10\"/>\n");
        return box.toString();
    }

    public static String generateBalls(int x_offset, double vx, int i_offset){
        int x_center = 150;
        int y_center = 150;
        double vy = 0;

        double degree = 0;
        double radius = 50;
        int stepsize = 18;
        StringBuilder balls = new StringBuilder();
        while (degree < 360){
            double x = x_center + radius * Math.cos(Math.toRadians(degree)) - x_offset;
            double y = y_center + radius * Math.sin(Math.toRadians(degree));

            balls.append("<rigidbodyvertex x=\"" + x + "\" y=\"" + y + "\" m=\"1\"/>\n");
            degree += stepsize;
        }
        StringBuilder p = new StringBuilder();
        for(int i=0; i<(360/stepsize); i++){
            int newI = i + i_offset;
            p.append("p=\"" + newI + "\" ");
        }
        balls.append("<rigidbody " + p.toString() + "vx=\"" + vx + "\" vy=\"" + vy + "\" omega=\"0.0\" r=\"5\"/>\n");
        return balls.toString();
    }

    public static String generateSprings(){
        StringBuilder springs = new StringBuilder();
        springs.append("<rigidbodyspringforce  i=\"-1\" pix=\"150.0\" piy=\"275\" j=\"0\" pjx=\"0\" pjy=\"50\" k=\"1000.0\" l0=\"25\"/>\n");
        springs.append("<rigidbodyspringforce  i=\"-1\" pix=\"150.0\" piy=\"25\" j=\"0\" pjx=\"0\" pjy=\"-50\" k=\"1000.0\" l0=\"25\"/>\n");
        springs.append("<rigidbodyspringforce  i=\"-1\" pix=\"-150\" piy=\"275\" j=\"2\" pjx=\"0\" pjy=\"50.0\" k=\"1000.0\" l0=\"25\"/>\n");
        springs.append("<rigidbodyspringforce  i=\"-1\" pix=\"-150\" piy=\"25\" j=\"2\" pjx=\"0\" pjy=\"-50.0\" k=\"1000.0\" l0=\"25\"/>\n");
        springs.append("<rigidbodyspringcolor spring=\"0\" r=\"0.7529\" g=\"0.7529\" b=\"0.7529\"/>");
        springs.append("<rigidbodyspringcolor spring=\"1\" r=\"0.7529\" g=\"0.7529\" b=\"0.7529\"/>");
        springs.append("<rigidbodyspringcolor spring=\"2\" r=\"0.7529\" g=\"0.7529\" b=\"0.7529\"/>");
        springs.append("<rigidbodyspringcolor spring=\"3\" r=\"0.7529\" g=\"0.7529\" b=\"0.7529\"/>");
        return springs.toString();
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
                String balls = generateBalls(0,0,0);
                writer.write(balls, 0, balls.length());
                balls = generateBalls(150,700,20);
                writer.write(balls, 0, balls.length());
                balls = generateBalls(300,0,40);
                writer.write(balls, 0, balls.length());
                String springs = generateSprings();
                writer.write(springs, 0, springs.length());
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
