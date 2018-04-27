package lsh.backend;

public class Core {

    static boolean DEBUG = false;

    static public class Status {

        public Status() {}

        public int current = 0 , total = 100;

    }

    static public void setDebugMode(boolean value) {
        DEBUG = value;
        Utils.setDebugMode(value);
    }

    static public boolean RunFix(String function) {

        int function_number;
        boolean isFunctionFound = false;

        function = Utils.removeSpace(function);

        function_number = getFunctionNumber(function);
        if (function_number == -1) return false;
        else return RunFix(function_number);
    }

    public synchronized static boolean RunFix(int functionNumber, Status statusBuffer) {

        final int solutionLength = Database.solution_list.get(functionNumber).commands.size();

        statusBuffer.total = solutionLength;

        String currentCommand;

        for(int i = 0; i < solutionLength; ++i) {

            statusBuffer.current = i + 1;

            //Post Status (When visualizing is set to true in Utils)
            Utils.percentBar(i + 1,  solutionLength);

            currentCommand = Database.solution_list.get(functionNumber).commands.get(i);

            //Use Java's Thread Sleep instead of "timeout" in cmd because it does not work in Java for some reason
            if(currentCommand.contains("timeout")) {
                String timeReadBuffer = new String();

                for(int n = currentCommand.indexOf(" ") + 1; n < currentCommand.length(); ++n) {
                    timeReadBuffer += currentCommand.charAt(n);
                }

                Utils.sleep(Integer.parseInt(timeReadBuffer)*1000);
                continue;
            }

            else {Utils.runCMD(currentCommand);}
        }
        return true;
    }

    public static boolean RunFix(int functionNumber)   {
        return RunFix(functionNumber,new Status());
    }

    public static void printPermissionList(int solutionNumber) {
	final int startPosition = 0;
	final int endPosition = Database.solution_list.get(solutionNumber).permissions.size() - 1;
        for (int i = 0; i <= endPosition; ++i) {
            Utils.printf("\t" + Integer.toString(i+1) + ". " + Database.solution_list.get(solutionNumber).permissions.get(i) + "\n");
        }
    }

    public static int getFunctionNumber(String input,boolean is_from_cli) {

        try {
            final int returnValue = Integer.parseInt(input) - (is_from_cli? 0 : 1);
            return returnValue;
        } catch (Exception e) {
            for(int i = 0; i < Database.solution_list.size(); ++ i) {
                if(Utils.IsEqual(input, Database.solution_list.get(i).URL, false)) {
                    return i;
                }
            }
            return -1;
        }
    }

    public static int getFunctionNumber(String input) {
        return getFunctionNumber(input,false);
    }

}
