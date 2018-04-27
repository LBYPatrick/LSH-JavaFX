package lsh.backend;

import java.util.ArrayList;

public class Main {

    public static void init() {
        final int memeory_mb = Utils.getRAM();
        final int min_pagefile_size = memeory_mb / 2;
        final int max_pagefile_size = memeory_mb * 2;

        String chrome_clean_cmd = "taskkill /f /im chrome.exe "
                + "&& C: "
                + "&& cd \"%localappdata%\\Google\\Chrome\\User Data\\Default\"";
        String ms_edge_clean_cmd = "taskkill /f /im MicrosoftEdge.exe"
                + "&& C:"
                + "&& cd \"%localappdata%\\Packages\\Microsoft.MicrosoftEdge_8wekyb3d8bbwe\\AC\"";

        //Process folder list for fix_storage
        for (String temp : Database.chromeFolderList) {
            chrome_clean_cmd += " && " + Utils.getRemoveFolderCMD(temp);
        }

        for (String temp : Database.msEdgeFolderList) {
            ms_edge_clean_cmd += " && " + Utils.getRemoveFolderCMD(temp);
        }

        //========================
        //Push solutions in
        //========================
        Database.solution_list.add(new Database.Solution(
                "Improve Computer Performance",
                Utils.sList(
                        "Might Occupy more space on the system drive",
                        "Disable Windows Defender"
                ),
                Utils.sList(
                        "wmic computersystem set AutomaticManagedPageFile=false",
                        "wmic pagefileset where name=\"C:\\\\pagefile.sys\" delete",
                        "wmic pagefileset create name=\"C:\\pagefile.sys\"",
                        "wmic pagefileset where name=\"C:\\\\pagefile.sys\" set InitialSize="
                                + Integer.toString(min_pagefile_size)
                                + ", MaximumSize="
                                + Integer.toString(max_pagefile_size),
                        "reg add \"HKEY_LOCAL_MACHINE\\SOFTWARE\\Policies\\Microsoft\\Windows Defender\" /v DisableAntiSpyware /t REG_DWORD /d 1 /f"
                ),
                "fix_performance"
        ));


        Database.solution_list.add(new Database.Solution(
                "Free Disk Space",
                Utils.sList(
                        "Will force Quit Microsoft Edge, Internet Explorer and Google Chrome"
                ),
                Utils.sList(
                        //For /c/windows/temp
                        "C: && cd %localappdata% && rd /q /s temp  && mkdir temp ",

                        //For Google Chrome
                        chrome_clean_cmd,

                        //For Microsoft Edge
                        ms_edge_clean_cmd,

                        //For Internet Explorer
                        "C: && cd \"%localappdata%\\Microsoft\\Windows\\INetCache\""
                        + " && rd /q /s \"IE\" \"Low\\IE\" "
                        + " && mkdir \"IE\" \"Low\\IE\" ",

                        //For Live Kernel Reports
                        "C: && cd \"C:\\Windows\\LiveKernelReports\" && del *.dmp ",

                        //For SoftwareOptimization
                        "C: && cd C:\\Windows\\ && rd /q /s DeliveryOptimization && mkdir DeliveryOptimization",

                        //For Measured Boot
                        "C: && cd C:\\Windows\\Logs"
                                + " && rd /q /s MeasuredBoot "
                                + " && mkdir MeasuredBoot ",

                        //For OneNote
                        "taskkill /f /im onenote.exe"
                                + " && taskkill /f /im onenotem.exe"
                                + " && C: && cd \"%localappdata%\\Microsoft\\OneNote\\16.0\""
                                + " && rd /q /s Backup "
                                + " && mkdir Backup ",

                        //For hibernation file
                        "powercfg -h -size 25 "
                ),
                "fix_storage"
        ));

        Database.solution_list.add(new Database.Solution(
                "Fix Bluescreen death",
                Utils.sList(
                        "Will disconnect you from the internet for 2 seconds during the process",
                        "Needs you to reboot your computer manually to work perfectly."
                ),
                Utils.sList(
                        "wmic pagefileset set name=\"C:\\pagefile.sys\",InitialSize="
                                + Integer.toString(min_pagefile_size)
                                + " MaximumSize="
                                + Integer.toString(max_pagefile_size),
                        "powercfg -h -size 100"
                ),
                "fix_blue_screen"
        ));


        //Look for fix_network and add things in
        for (Database.Solution current_solution :Database.solution_list){
            if (current_solution.URL == "fix_network" || current_solution.URL == "fix_blue_screen") {

                //CMD for cleaning wifi_config.xml if present
                current_solution.commands.add("type nul > wifi_config.xml");

                current_solution.commands.add(Utils.getWriteFileCMD
                (Database.wifiConfig.get(Database.PREFERRED_WIFI).content_, "wifi_config.xml"));

                //connect to Specified Wi-Fi
                current_solution.commands.add("netsh wlan delete profile name=\"" + Database.wifiConfig.get(Database.PREFERRED_WIFI).name_ + "\"");
                current_solution.commands.add("netsh wlan add profile \".\\wifi_config.xml\"");
                current_solution.commands.add("del wifi_config.xml");
                current_solution.commands.add("netsh wlan disconnect");
                current_solution.commands.add("netsh wlan connect name=\"" + Database.wifiConfig.get(Database.PREFERRED_WIFI).name_ + "\"");

                //clean up
            }
        }
    }


    public static void main(String [] args) {

        //Core.setDebugMode(true);
        final int argc = args.length + 1;
        ArrayList<String> solution_urls = new ArrayList<>();

        init();

        //Console interface
        if (argc == 1) {
            Utils.setVisualizing(true);
            CLI.welcome();
            return;
        }

        //LSHelper CLI
        else if (argc > 1) {
            Utils.setVisualizing (false);

            if (Utils.search(Utils.sList("--help", "-h", "/?"), args[0]) !=-1){
                Utils.printf("LSHelper " + Database.VERSION + " by @LBYPatrick and @Tylerliu\n\n" +
                        "Usage: ls-helper <URL_OR_NUMBER>\n\n");
                Utils.printSolutions (Database.solution_list);
                return;
            } else{

                for (Database.Solution current_solution : Database.solution_list) {
                    solution_urls.add(current_solution.URL);
                }

                int result = Utils.search (solution_urls, args[0]);

                if (result != -1) {
                    Core.RunFix (result);
                } else {
                    result = Core.getFunctionNumber (args[0], true);

                    if (result != -1) {
                        Core.RunFix(result);
                    } else {
                        Utils.printMsg ("Unknown Function \"" + args[0] + "\"", Database.MsgType.ERR);
                        return;
                    }
                }
            }
        }
        return;
    }
}
