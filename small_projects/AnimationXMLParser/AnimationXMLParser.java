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

public class AnimationXMLParser{
    private static String firstPart = "<scene>\n<duration time=\"50.0\"/>\n"
        + "<integrator type=\"symplectic-euler\" dt=\"0.01\"/>\n"
        + "<maxsimfreq max=\"500.0\"/>\n";
    private static String finalLine = "</scene>";

    private static String parseParticle (String line, int dx, int dy, double radius, double vx){
        String[] elmts = line.split("\t");
        int x = Integer.parseInt(elmts[0])+dx;
        int y = Integer.parseInt(elmts[1])+dy;
        String particle = "<particle m=\"1.0\" px=\"-" + x
            + "\" py=\"-" + y + "\" vx=\"" + vx + "\" vy=\"0.0\"" +
            " fixed=\"0\" radius=\"" + radius + "\"/>\n";
        return particle;
    }

    private static String parseColor (String line, int index){
        String[] elmts = line.split("\t");
        double greyscale = Integer.parseInt(elmts[2])/255.0;
        String color = "<particlecolor i=\"" + index*2
            + "\" r=\"" + greyscale
            + "\" g=\"" + greyscale
            + "\" b=\"" + greyscale + "\"/>\n";
        return color;
    }

    private static String generateSpring (int index){
        int i = index*2;
        int i1 = i+1;
        String spring = "<edge i=\"" + i + "\" j=\"" + i1 + "\" radius=\"0.0001\"/>\n"
            + "<springforce edge=\"" + index + "\" k=\"10.0\" l0=\"10.0\" />\n";
        return spring;
    }

    private static String generateGrass(int particleIndex, double y){
        StringBuilder grass = new StringBuilder();
        grass.append("<particle m=\"1.0\" px=\"" + "-400"
                + "\" py=\"" + y + "\" vx=\"1000000.0\" vy=\"0.0\"" +
                " fixed=\"1\" radius=\"1\"/>\n");
        grass.append("<particlecolor i=\"" + particleIndex
                + "\" r=\"0.00392156862\" g=\"0.65098039215\" b=\"0.0666666666\"/>\n");
        grass.append("<particlepath i=\"" + particleIndex
                + "\"duration=\"100.0\" r=\"0.00392156862\" g=\"0.65098039215\" b=\"0.0666666666\"/>\n");

        return grass.toString();
    }

    private static String generateWaves (int particleIndex, int scalingX, int scalingY, int edgeIndex){
        double startingX = -120;
        double endingX = 200;
        double stepsize = 0.1;
        StringBuilder waves = new StringBuilder();
        while (startingX < endingX){
            double x = startingX * 10;
            double y = Math.sin(Math.toRadians(x))*10-scalingY;
            waves.append("<particle m=\"1.0\" px=\"" + startingX+scalingX
                    + "\" py=\"" + y + "\" vx=\"0.0\" vy=\"0.0\"" +
                    " fixed=\"0\" radius=\"1\"/>\n");
            waves.append("<particlecolor i=\"" + particleIndex
                    + "\" r=\"0.1098\" g=\"0.4196\" b=\"0.62745\"/>\n");
            if(startingX != -120){
                int pi = particleIndex-1;
                waves.append("<edge i=\"" + pi + "\" j=\"" + particleIndex + "\"/>\n");
                waves.append("<edgecolor i=\"" + edgeIndex + "\" r=\"0.1098\" g=\"0.4196\" b=\"0.62745\"/>\n");
            }
            startingX += stepsize;
            edgeIndex++;
            particleIndex++;
        }
        return waves.toString();
    }

    public static void main(String[] args){
        ArrayList<String> lines = new ArrayList<>();
        String inputfilename = args[0];
        String outputfilename = args[1];

        try (Stream<String> stream = Files.lines(Paths.get(inputfilename))) {
            stream.forEach(s -> lines.add(0, s));
        } catch (IOException e){
            e.printStackTrace();
        }

        Charset charset = Charset.forName("UTF-8");
        try {
            Path outputfile = Paths.get(outputfilename);
            try (BufferedWriter writer = Files.newBufferedWriter(outputfile, charset, StandardOpenOption.APPEND)) {
                writer.write(firstPart, 0, firstPart.length());
                int index=0;
                int dy = 40;
                double vx = 100;
                for (String line: lines){
                    String particle = parseParticle(line, 0, 0, 1, vx);
                    String particlecolor = parseColor(line, index);
                    String secondaryP = parseParticle(line, 0, dy, 0.01, vx);
                    String spring = generateSpring(index);
                    writer.write(particle, 0, particle.length());
                    writer.write(secondaryP, 0, secondaryP.length());
                    writer.write(particlecolor, 0, particlecolor.length());
                    writer.write(spring, 0, spring.length());
                    index++;
                }
                int scalingY = 60;
                int scalingX = 0;
                int particleIndex = 3137;
                int edgeIndex = 1569;
                int particlesPerWave = 3101;
                for (int i=0; i<7; i++){
                    String waves = generateWaves(particleIndex, scalingX, scalingY, edgeIndex);
                    particleIndex += particlesPerWave;
                    edgeIndex += particlesPerWave-1;
                    scalingX += 5;
                    scalingY += 10;
                    writer.write(waves, 0, waves.length());
                }
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
