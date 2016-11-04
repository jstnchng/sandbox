import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.lang.Math;
import java.util.Arrays;
import java.util.ArrayList;

public class Editor {
    private static final int portNumber = 5005;
    //current x and y in terms of pixels
    private static int currentX = 0;
    private static int currentY = 0;
    //maximum size x and y in terms of pixels
    private static int maxX = 0;
    private static int maxY = 0;
    //stores all the characters in the document
    private static char[][] document = null;
    //size of char width & height
    private static final int charX = 8;
    private static final int charY = 14;

    //prints for debugging purposes
    public static void printDocument(){
        System.out.println("current document: ");
        for(int j=0; j<document.length; j++){
            System.out.print("[ ");
            for(int i=0; i<document[0].length; i++){
                System.out.print(document[j][i] + " ");
            }
            System.out.println("");
        }
    }

    //string splits the event, calls functions to handle mouse/key/resize
    public static String processEvent(String event){
        String[] eventParts = event.split(",");
        if(eventParts[0].contains("mouse")){
            int x = Integer.parseInt(eventParts[1]);
            int y = Integer.parseInt(eventParts[2]);
            return processMouseAction(eventParts[0], x, y);
        } else if (eventParts[0].contains("key")){
            return processKeyAction(eventParts[0], eventParts[1]);
        } else {
            int x = Integer.parseInt(eventParts[1]);
            int y = Integer.parseInt(eventParts[2]);
            return processResizeAction(x, y);
        }
    }

    //rounds x and y to nearest pixel x and y divisible by charX and charY respectively
    public static void getNearestGrid(int x, int y){
        currentX = x - (x%charX);
        currentY = y - (y%charY);
    }

    //creates new document with resize, initialize some variables
    public static String processResizeAction(int x, int y){
        int newX = (x+charX-1)/charX;
        int newY = (y+charY-1)/charY;
        maxX = newX*charX;
        maxY = newY*charY;
        if(document == null){
            document = new char[newY][newX];
        }
        System.out.println("new document dimensions: (" + document.length + ", " + document[0].length + ")");
        return null;
    }

    //so far only processes clicks. a mouseup is a click, since a mouseup is always preceded by some mousedown
    //highlighting is a mousedown at x,y and a mouseup at a different x,y
    public static String processMouseAction(String action, int x, int y){
        if(action.equals("mouseup")){
            getNearestGrid(x,y);
        }
        System.out.println("current X is: " + currentX);
        System.out.println("current Y is: " + currentY);
        return null;
    }

    //handles pressing a key
    public static String processKeyAction(String action, String charOrCode){
        //use keydown here instead of keyup so can hold keys, e.g. hold e for eeeeeeeeee
        if(action.equals("keydown")){
            System.out.println("At x,y: (" + currentX + "," + currentY + "), char is: " + charOrCode);
            int len = charOrCode.length();
            //if len is 1 then its just a regular char, not special code
            if(len == 1){
                int oldX = currentX;
                currentX += len * charX;
                char newChar = charOrCode.charAt(0);
                int shiftX = oldX/charX;
                int shiftY = currentY/charY;
                int nextBlankSpace = shiftX;
                //looks for next available blank space, shifts all chars over and inserts the newly typed one
                while(document[shiftY][nextBlankSpace] != (char) 0){
                    nextBlankSpace++;
                }
                while(nextBlankSpace != shiftX){
                    document[shiftY][nextBlankSpace] = document[shiftY][nextBlankSpace-1];
                    nextBlankSpace--;
                    document[shiftY][nextBlankSpace] = (char) 0;
                }
                document[shiftY][shiftX] = newChar;
                return renderDocument();
            } else {
                return processSpecialKeyAction(action, charOrCode);
            }
        }
        return null;
    }

    // for codes
    public static String processSpecialKeyAction(String action, String charOrCode){
        if(charOrCode.equals("Return")){
            updateDocument("Return");
            int oldY = currentY;
            currentY += charY;
            currentX = 0;
            return renderDocument();
        } else if(charOrCode.equals("Tab")){
            updateDocument("Tab");
            int oldX = currentX;
            currentX += 4 * charX;
            currentX = currentX%maxX;
            return renderDocument();
        } else if(charOrCode.equals("space")){
            updateDocument("space");
            int oldX = currentX;
            currentX += charX;
            return renderDocument();
        } else if (charOrCode.equals("Up")) {
            currentY -= charY;
            return null;
        } else if (charOrCode.equals("Down")) {
            currentY += charY;
            return null;
        } else if (charOrCode.equals("Right")) {
            currentX += charX;
            return null;
        } else if (charOrCode.equals("Left")) {
            currentX -= charX;
            return null;
        } else {
            return charOrCode;
        }
    }

    public static void updateDocument(String action){
        int scaledY = currentY/charY;
        int scaledX = currentX/charX;
        // printDocument();
        if(action.equals("Return")){
            // moves everything down, up to and not including current row
            for(int i=document.length-2; i>scaledY; i--){
                document[i+1] = Arrays.copyOf(document[i], document[i].length);
                Arrays.fill(document[i], (char) 0);
            }
            // moves everything to the right of the return 1 line down
            for(int j=scaledX; j<document[0].length; j++){
                document[scaledY+1][j-scaledX] = document[scaledY][j];
                document[scaledY][j] = (char) 0;
            }
        }
        if(action.equals("Tab") || action.equals("space")){
            int len = action.equals("Tab") ? 4 : 1;
            int index = scaledX + len;
            for(int j=scaledX; j<document[0].length; j++){
                System.out.println("index: " + index);
                //if nothing there then can ignore
                if(document[scaledY][j] == (char) 0){
                    continue;
                }
                //straightforward shift stuff over
                if(index < document[0].length){
                    document[scaledY][index] = document[scaledY][j];
                    document[scaledY][j] = (char) 0;
                } else {
                    //this is the case where tab overflows, resets index all the way left and moves everything down
                    index -= document[0].length;
                    for(int i=document.length-2; i>scaledY; i--){
                        document[i+1] = Arrays.copyOf(document[i], document[i].length);
                        Arrays.fill(document[i], (char) 0);
                    }
                    document[scaledY+1][index] = document[scaledY][j];
                    document[scaledY][j] = (char) 0;
                }
                index++;
            }
        }
        // printDocument();
    }

    public static String renderDocument(){
        //re-renders by applying white rectangle then rerendering the document
        StringBuilder cmd = new StringBuilder("rect,0,0," + maxX + "," + maxY +",#FFFFFF\n");
        for(int i=0; i<document.length; i++){
            for(int j=0; j<document[0].length; j++){
                int y = i*charY;
                int x = j*charX;
                char txt = document[i][j];
                if(txt != (char) 0){
                    cmd.append("text," + x + "," + y + ",#000000," + txt + "\n");
                }
            }
            // System.out.println("");
        }
        return cmd.toString();
    }

    public static void main(String args[]) throws IOException {
        System.out.println("Creating a server socket on port " + portNumber);
        Socket socket = new Socket("localhost", portNumber);
        try(
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        ) {
            String event;
            while( (event = in.readLine()) != null){
                System.out.println("received this event: " + event);
                String cmd = processEvent(event);
                if(cmd != null && !cmd.isEmpty()){
                    //split in case there are multiple commands (with a re-render)
                    String[] cmds = cmd.split("\n");
                    for(String command : cmds){
                        System.out.println("cmd send to server: " + command);
                        out.println(command);
                    }
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
