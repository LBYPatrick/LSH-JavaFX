package lsh.backend;

import java.util.ArrayList;
import java.util.Scanner;

public class CLI {

    static void printf(String message) {
        System.out.printf(message);
    }

    static void ListOptions() {
        for(int i = 0; i < Database.solution_list.size(); ++i) {
            printf("\t" + Integer.toString(i+1) + " " + Database.solution_list.get(i).name + "\n");
        }
    }

    static String getMessage() {
        Scanner scan = new Scanner(System.in);
        return scan.next();
    }

    static void welcome() {

        String cmd_input_buffer;
        int    option_number;

        Utils.cleanScreen();
        printf("\nWelcome to LSHelper!\n\n");
        ListOptions();
        printf("\nTo exit the program, type \"exit\" or \"quit\"\n");
        printf("\nYour option: ");

        cmd_input_buffer = getMessage();

        if (Utils.search(Utils.sList("quit","exit","eof","bye"),cmd_input_buffer) != -1) return;

        option_number = Core.getFunctionNumber(cmd_input_buffer);

        if(option_number == -1) {
            printf("Invalid function, please try again...");
            Utils.sleep(1300);
            welcome();
        }
        else {
            Utils.cleanScreen();

            printf("Before you run this...\n\n");
            Core.printPermissionList(option_number);
            printf("\nDo you wish to proceed?<Y\\N>:");

            cmd_input_buffer = getMessage();

            if (Utils.IsEqual(Utils.removeSpace(cmd_input_buffer), "Y",true)) {

                Utils.cleanScreen();
                Utils.printf("Working...\n");
                Core.RunFix(option_number);
                welcome();
            }
		else {
                welcome();
            }
        }
    }

}
