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

public class AXMLt3m1{

    private static String firstPart = "<scene>\n<duration time=\"10.0\"/>\n"
        + "<simtype type=\"rigid-body\"/>"
        + "<rigidbodyintegrator type=\"symplectic-euler\" dt=\"0.005\"/>\n"
        + "<maxsimfreq max=\"500000.0\"/>\n"
        + "<viewport cx=\"0.0\" cy=\"-2.0\" size=\"50\"/>\n";
    // private static String finalLine = "<backgroundcolor r=\"0.0\" g=\"0.0\" b=\"0.0\"/>\n</scene>";
    private static String finalLine = "</scene>";

    public static String generateFigure(){
        StringBuilder figure = new StringBuilder();
        String r="1.0", g="1.0", b="1.0";

        int vx = 0;
        int vy = 0;
        //head left bottom point
        int px = 25;
        int py = 20;
        figure.append("<rigidbodyvertex x=\"" + px + "\" y=\"" + py + "\" m=\"1\"/>\n");
        //head right bottom
        px = 35;
        py = 20;
        figure.append("<rigidbodyvertex x=\"" + px + "\" y=\"" + py + "\" m=\"1\"/>\n");
        //head right top
        px = 35;
        py = 30;
        figure.append("<rigidbodyvertex x=\"" + px + "\" y=\"" + py + "\" m=\"1\"/>\n");
        //head left top
        px = 25;
        py = 30;
        figure.append("<rigidbodyvertex x=\"" + px + "\" y=\"" + py + "\" m=\"1\"/>\n");
        figure.append("<rigidbody p=\"0\" p=\"1\" p=\"2\" p=\"3\" vx=\"" + vx + "\" vy=\"" + vy + "\" omega=\"0.0\" r=\"0.1\"/>\n");
        //body
        px = 30;
        py = 20;
        figure.append("<rigidbodyvertex x=\"" + px + "\" y=\"" + py + "\" m=\"1\"/>\n");
        py = 0;
        figure.append("<rigidbodyvertex x=\"" + px + "\" y=\"" + py + "\" m=\"1\"/>\n");
        figure.append("<rigidbody p=\"4\" p=\"5\" vx=\"" + vx + "\" vy=\"" + vy + "\" omega=\"0.0\" r=\"0.1\"/>\n");
        //left arm
        py = 17;
        px = 10;
        figure.append("<rigidbodyvertex x=\"" + px + "\" y=\"" + py + "\" m=\"1\"/>\n");
        figure.append("<rigidbody p=\"4\" p=\"6\" vx=\"" + vx + "\" vy=\"" + vy + "\" omega=\"0.0\" r=\"0.1\"/>\n");
        //right arm
        px = 50;
        figure.append("<rigidbodyvertex x=\"" + px + "\" y=\"" + py + "\" m=\"1\"/>\n");
        figure.append("<rigidbody p=\"4\" p=\"7\" vx=\"" + vx + "\" vy=\"" + vy + "\" omega=\"0.0\" r=\"0.1\"/>\n");
        //left leg
        py = -5;
        px = 15;
        figure.append("<rigidbodyvertex x=\"" + px + "\" y=\"" + py + "\" m=\"1\"/>\n");
        figure.append("<rigidbody p=\"5\" p=\"8\" vx=\"" + vx + "\" vy=\"" + vy + "\" omega=\"0.0\" r=\"0.1\"/>\n");
        //right leg
        px = 45;
        figure.append("<rigidbodyvertex x=\"" + px + "\" y=\"" + py + "\" m=\"1\"/>\n");
        figure.append("<rigidbody p=\"5\" p=\"9\" vx=\"" + vx + "\" vy=\"" + vy + "\" omega=\"0.0\" r=\"0.1\"/>\n");

        figure.append("<rigidbodyspringforce  i=\"0\" pix=\"0.0\" piy=\"-5\" j=\"1\" pjx=\"0.0\" pjy=\"10.0\" k=\"1900000.0\" l0=\"0.0\"/>\n");
        figure.append("<rigidbodyspringforce  i=\"2\" pix=\"10.0\" piy=\"1.5\" j=\"1\" pjx=\"0.0\" pjy=\"10.0\" k=\"1900000.0\" l0=\"0.0\"/>\n");
        figure.append("<rigidbodyspringforce  i=\"3\" pix=\"-10.0\" piy=\"1.5\" j=\"1\" pjx=\"0.0\" pjy=\"10.0\" k=\"1900000.0\" l0=\"0.0\"/>\n");
        figure.append("<rigidbodyspringforce  i=\"4\" pix=\"7.5\" piy=\"2.5\" j=\"1\" pjx=\"0.0\" pjy=\"-10.0\" k=\"1900000.0\" l0=\"0.0\"/>\n");
        figure.append("<rigidbodyspringforce  i=\"5\" pix=\"-7.5\" piy=\"2.5\" j=\"1\" pjx=\"0.0\" pjy=\"-10.0\" k=\"1900000.0\" l0=\"0.0\"/>\n");

        figure.append("<rigidbodygravityforce fx=\"0.0\" fy=\"-1.0\"/>\n");
        return figure.toString();
    }

    public static void main(String[] args){
        String outputfilename = args[0];
        Charset charset = Charset.forName("UTF-8");
        try {
            Path outputfile = Paths.get(outputfilename);
            try (BufferedWriter writer = Files.newBufferedWriter(outputfile, charset, StandardOpenOption.APPEND)) {
                writer.write(firstPart, 0, firstPart.length());
                String figure = generateFigure();
                writer.write(figure,0,figure.length());
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
