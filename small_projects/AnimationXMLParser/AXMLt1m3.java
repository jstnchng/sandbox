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

public class AXMLt1m3{
    private static String firstPart = "<scene>\n<duration time=\"50.0\"/>\n"
        + "<integrator type=\"linearized-implicit-euler\" dt=\"0.01\"/>\n"
        + "<maxsimfreq max=\"500.0\"/>\n";
    private static String finalLine = "<backgroundcolor r=\"0.0\" g=\"0.0\" b=\"0.0\"/>\n</scene>";

    public static String generateO(int particleIndex, int scalingX, int scalingY, int edgeIndex){
        HashMap<Integer, double[]> colors = new HashMap<>();
        colors.put(0, new double[]{1,0,0});
        colors.put(1, new double[]{1, .498, 0});
        colors.put(2, new double[]{1,1,0});
        colors.put(3, new double[]{0,1,0});
        colors.put(4, new double[]{0,0,1});
        colors.put(5, new double[]{.294,0,.5098});
        colors.put(6, new double[]{.58,0,.8274});

        int startingEdge = 0;
        double startingX = 0;
        double endingX = 90;
        double stepsize = 3;
        boolean reverse = false;
        int colorindex =0;
        StringBuilder osc = new StringBuilder();
        while (startingX < endingX){
            double x = startingX * 8;
            double y1 = Math.sin(Math.toRadians(x))*18-scalingY;
            y1 += 0.1;
            double y2 = -y1;

            if(colorindex == 6){
                reverse = true;
            }
            if(colorindex == 0){
                reverse = false;
            }

            double r = colors.get(colorindex)[0];
            double g = colors.get(colorindex)[1];
            double b = colors.get(colorindex)[2];
            osc.append("<particle m=\"1.0\" px=\"" + startingX+scalingX
                    + "\" py=\"" + y1 + "\" vx=\"0.0\" vy=\"0.0\"" +
                    " fixed=\"0\" radius=\"0.1\"/>\n");
            osc.append("<particlecolor i=\"" + particleIndex
                    + "\" r=\"" + r + "\" g=\"" + g + "\" b=\"" + b + "\"/>\n");
            particleIndex++;
            osc.append("<particle m=\"1.0\" px=\"" + startingX+scalingX
                    + "\" py=\"" + y2 + "\" vx=\"0.0\" vy=\"0.0\"" +
                    " fixed=\"0\" radius=\"0.1\"/>\n");
            osc.append("<particlecolor i=\"" + particleIndex
                    + "\" r=\"" + r + "\" g=\"" + g + "\" b=\"" + b + "\"/>\n");
            int prev = particleIndex-1;
            osc.append("<edge i=\"" + prev + "\" j=\"" + particleIndex + "\" radius=\"1\"/>\n"
                + "<springforce edge=\"" + edgeIndex + "\" k=\"40.0\" l0=\"7.0\" />\n"
                + "<edgecolor i=\"" + edgeIndex + "\" r=\"" + r + "\" g=\"" + g + "\" b=\"" + b + "\"/>\n");

            edgeIndex++;
            startingX += stepsize;
            particleIndex++;
            if (reverse){
                colorindex--;
            } else {
                colorindex++;
            }
        }
        return osc.toString();
    }

    public static void main(String[] args){
        String outputfilename = args[0];
        Charset charset = Charset.forName("UTF-8");
        try {
            Path outputfile = Paths.get(outputfilename);
            try (BufferedWriter writer = Files.newBufferedWriter(outputfile, charset, StandardOpenOption.APPEND)) {
                writer.write(firstPart, 0, firstPart.length());
                String oscillator = generateO(0,0,0,0);
                writer.write(oscillator, 0, oscillator.length());
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
