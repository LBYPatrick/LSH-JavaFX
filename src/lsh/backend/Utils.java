
package lsh.backend;

import java.util.Scanner;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;



public class Utils {

    static boolean visualizing = true,
                   DEBUG       = false;

    public static void setDebugMode(boolean value) {
        DEBUG = value;
    }

    public static void printf(String msg) {
        System.out.print(msg);
    }

    public static void setVisualizing(boolean value) {
        visualizing = value;
    }

    public static ArrayList<String> runCMD(String cmd) {

        cmd = "cmd /c " + cmd;
        Process pr;
        ArrayList<String> returnBuffer = new ArrayList<>();

        try {

        pr = Runtime.getRuntime().exec(cmd);
        BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));

        //Show debug Info
        if(DEBUG) printf("\nWith cmd \"" + cmd + "\"\n");

        String line=null;
        while((line=input.readLine()) != null) {

            if(line.length() > 0) {

                if(DEBUG) printf("\t" + line + "\n");

                returnBuffer.add(line);
            }
        }

    } catch(Exception e) {
        e.printStackTrace();
    }

    return returnBuffer;

}

    public static int getRAM() {

        double sum = 0;

        ArrayList<String> cmdBuffer = runCMD("wmic MEMORYCHIP get Capacity");

        for(int i = 1; i < cmdBuffer.size(); ++i) {
            //printf("Line " + Integer.toString(i) + ": " + cmdBuffer.get(i) + "\n");
            sum += (Long.parseLong(Utils.removeSpace(cmdBuffer.get(i))) / 1048576);
        }

        return (int)sum;
    }

    public static void percentBar(int current, int total) {

        if (!visualizing) return;

    final int barLength = 50;
    final int leftPercent = (int)(((double)current / (double)total) * barLength);
    final int rightPercent = barLength - leftPercent;
        String print_buffer = "\r[";

        for (int i = 0; i < leftPercent - 1; i++) print_buffer += "=";
        print_buffer += ">";
        for (int i = 0; i < rightPercent; i++) print_buffer += " ";

        print_buffer += "] " + Integer.toString(current) + "/" + Integer.toString(total);

        if (current == total) print_buffer += "\n";
        printf(print_buffer);
    }

    public static String getRemoveFolderCMD(String folder_name) {
        String returnBuffer = new String();

        returnBuffer += "rd /q /s \"";
        returnBuffer += folder_name;
        returnBuffer += "\" && mkdir \"";
        returnBuffer += folder_name;
        returnBuffer += "\" ";

        return returnBuffer;
    }

    public static void cleanScreen() {

        try
        {
            final String os = System.getProperty("os.name");

            if (os.contains("Windows"))
            {
                Runtime.getRuntime().exec("cls");
            }
            else
            {
                Runtime.getRuntime().exec("clear");
            }
        }
        catch (final Exception e)
        {
            //  Handle any exceptions.
        }
    }

    public static String removeSpace(String input) {

        String return_buffer = new String();

        for (char current_char : input.toCharArray()) {
            if (current_char != ' ') return_buffer += current_char;
        }

        return return_buffer;
    }

    public static boolean IsEqual(String left, String right, boolean is_strict) {
        if (is_strict) { return left.toUpperCase().equals(right.toUpperCase()); }
        if (!is_strict) { return left.toUpperCase().contains(right.toLowerCase());}
        return false;
    }

    public static String getWriteFileCMD(ArrayList<String> content, String filename) {

        String return_buffer = new String();
        boolean is_first_line = true;

        //Quit if nothing to write
        if (content.size() == 0) return new String();

        for (String current_line : content) {

            if (!is_first_line) { return_buffer += " & "; }
            else { is_first_line = false; }

            return_buffer += "echo ";

            //Iterate through every character and then add escape characters
            for (char current_char : current_line.toCharArray()) {
                if (current_char == '\\'
                        || current_char == '&'
                        || current_char == '|'
                        || current_char == '>'
                        || current_char == '<'
                        || current_char == '>'
                        || current_char == '^')
                    return_buffer += '^';
                return_buffer += current_char;
            }

            return_buffer += ">> " + filename;
        }
        return return_buffer;
    }

    public static void printSolutions(ArrayList<Database.Solution> solution_list) {

        int longest_left_length = 0;
        int current_left_size;
        int current_index = 0;

        String print_buffer = new String();

        for (Database.Solution current_solution : solution_list) {
            current_left_size = current_solution.URL.length() + Integer.toString(current_index).length() + 4;

            if (current_left_size > longest_left_length) longest_left_length = current_left_size;
            current_index++;
        }

        current_index = 0;

        for (Database.Solution current_solution : solution_list) {

            print_buffer += current_solution.URL + " or " + Integer.toString(current_index);

            for (int i = current_solution.URL.length() + Integer.toString(current_index).length() + 4;
                 i < longest_left_length;
                 ++i) {
                print_buffer += ' ';
            }

            print_buffer += " : " + current_solution.name + "\n";

            current_index++;
        }

        printf(print_buffer);

    }

    public static int search(ArrayList<String> list, String keyword) {
        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i).equals(keyword)) {
                return i;
            }
        }

        return -1;
    }

    public static void printMsg(String msg, Database.MsgType msgType) {
        switch (msgType) {
            case ERR :
                printf("[ERROR]" + msg + "\n");
                break;
            case WARNING :
                printf("[WARNING] " + msg + "\n");
                break;
            case INFO :
                printf("[INFO] + " + msg + "\n");
                break;
        }
    }

    public static boolean isEqual(String left, String right) {
        return IsEqual(left, right, true);
    }

    public static ArrayList<String> sList(String... content) {
        return new ArrayList<>(Arrays.asList(content));
    }
    public static void sleep(int mills) {
        try {
            Thread.sleep(mills);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String getInput() {
        Scanner scan = new Scanner(System.in);
        return scan.next();
    }
}
