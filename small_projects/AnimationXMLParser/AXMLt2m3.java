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

public class AXMLt2m3{
    private static String firstPart = "<scene>\n<duration time=\"10.0\"/>\n" + "<integrator type=\"forward-backward-euler\" dt=\"0.005\"/>\n"
        + "<collision type=\"penalty\" k=\"1000\" thickness=\"10.0\"/>\n"
        + "<collisiondetection type=\"contest\"/>\n"
        + "<maxsimfreq max=\"500.0\"/>\n";
    private static String finalLine = "<backgroundcolor r=\"0.0\" g=\"0.0\" b=\"0.0\"/>\n</scene>";
    // private static String firstPart = "<scene>\n<duration time=\"10.0\"/>\n"
        // + "<integrator type=\"explicit-euler\" dt=\"0.01\"/>\n"
        // + "<maxsimfreq max=\"500.0\"/>\n"
        // + "<collision type=\"simple\"/>\n";
    // private static String finalLine = "<backgroundcolor r=\"0.0\" g=\"0.0\" b=\"0.0\"/>\n</scene>";

    public static String generateBox(){
        StringBuilder box = new StringBuilder();
        String r="1.0", g="1.0", b="1.0";

        int px = 800;
        int py = 800;
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

    public static String generateBalls(int particleIndex, int edgeIndex, int radius, int stepsize){
        HashMap<Integer, double[]> colors = new HashMap<>();
        colors.put(0, new double[]{1,0,0});
        colors.put(1, new double[]{1, .498, 0});
        colors.put(2, new double[]{1,1,0});
        colors.put(3, new double[]{0,1,0});
        colors.put(4, new double[]{0,0,1});
        colors.put(5, new double[]{.294,0,.5098});
        colors.put(6, new double[]{.58,0,.8274});

        int x_center = 400;
        int y_center = 400;

        double degree = 0;
        boolean reverse = false;
        int colorindex = 0;
        StringBuilder balls = new StringBuilder();
        while (degree < 360){
            double x = x_center + radius * Math.cos(Math.toRadians(degree));
            double y = y_center + radius * Math.sin(Math.toRadians(degree));
            double vx = (x-x_center)/3;
            double vy = (y-y_center)/3;

            double r = Math.random();
            double g = Math.random();
            double b = Math.random();
            if(colorindex == 6){
                reverse = true;
            }
            if(colorindex == 0){
                reverse = false;
            }
            // double r = colors.get(colorindex)[0];
            // double g = colors.get(colorindex)[1];
            // double b = colors.get(colorindex)[2];
            balls.append("<particle m=\"1.0\" px=\"" + x
                    + "\" py=\"" + y + "\" vx=\"" + vx + "\" vy=\"" + vy + "\"" +
                    " fixed=\"0\" radius=\"2.0\"/>\n");
            balls.append("<particlecolor i=\"" + particleIndex
                    + "\" r=\"" + r + "\" g=\"" + g + "\" b=\"" + b + "\"/>\n");
            balls.append("<particlepath i=\"" + particleIndex + "\" duration=\"10.0\" r=\"" + r + "\" g=\"" + g + "\" b=\"" + b + "\"/>\n");

            particleIndex++;
            degree += stepsize;
            if (reverse){
                colorindex--;
            } else {
                colorindex++;
            }
        }
        return balls.toString();
    }

    public static String generateMultipleBalls(){
        int particleIndex = 4;
        int edgeIndex = 4;
        int radius = 10;
        int[] stepsizes = {1,2,3,4,5,6,8,9,10,12,15,18,20,24,30,36,40,45};
        StringBuilder balls = new StringBuilder();
        for(int i=stepsizes.length-1; i!=0; i--){
            balls.append(generateBalls(particleIndex, edgeIndex, radius, stepsizes[i]));
            particleIndex += 360/stepsizes[i];
            radius += 7;
        }
        return balls.toString();
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
                String balls = generateMultipleBalls();
                writer.write(balls, 0, balls.length());
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
