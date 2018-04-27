package lsh.backend;

import java.util.ArrayList;
import java.util.Arrays;

public class Database {

    final public static int LS_STATIC = 0,
                            LS_GUEST  = 1,
                            NORMAL_EXIT = 0,
                            FIX_NO_FOUND = 1,
                            PREFERRED_WIFI = LS_GUEST;
    final public static String VERSION = "0.1.0";

    public static class Solution {

        public String name;
        public ArrayList<String> permissions;
        public ArrayList<String> commands;
        public String URL;

        public Solution(String name, ArrayList<String> permissions, ArrayList<String> commands, String URL) {
            this.name = name;
            this.permissions = permissions;
            this.commands = commands;
            this.URL = URL;
        }

    }

    public static class WifiConfig {
        public String name_;
        public ArrayList<String> content_;

        public WifiConfig(String name, ArrayList<String> content) {
            name_ = name;
            content_ = content;
        }
    }

    public enum MsgType {
        ERR,
        WARNING,
        INFO
    }


    //All static solutions are stored here (dynamic solutions will be pushed in by init() in main.cpp)
    public static ArrayList<Solution> solution_list = new ArrayList<>(
            Arrays.asList(
            //Solution 1
            new Solution(
                    "Fix Internet Connection",

                    Utils.sList("Disconnect you shortly"),
                    Utils.sList(
                            "netsh interface ipv4 reset",
                            "netsh interface ipv6 reset",
                            "netsh interface set interface Wi-Fi disable",
                            "netsh interface set interface Wi-Fi enable",
                            "timeout 3",
                            "netsh interface ipv4 add dnsservers \"Wi-Fi\" 8.8.8.8 index = 1",
                            "netsh interface ipv4 add dnsservers \"Wi-Fi\" 8.8.4.4 index = 2",
                            "ipconfig /flushdns"
                    ),
                    "fix_network"
            ),

            //Solution 2
            new Solution(
                    "Fix Office",
                    Utils.sList("Terminate all process of Microsoft Office."),
                    Utils.sList(
                            "taskill /f /im MSOUC.exe",
                            "C: && cd \"\\%localappdata\\%\\Microsoft\\Office\\16.0\"" +
                            " && rd /q /s OfficeFileCache & mkdir OfficeFileCache",
                            "C: && cd \"C:\\Program Files\\Microsoft Office\\root\\Office16\"" +
                            " && start MSOUC.exe"
                    ),
                    "fix_ms_office"
            ),

            //Solution 3
            new Solution(
                    "Fix Windows update",
                    Utils.sList("Wipe out all windows update history."),
                    Utils.sList(
                            "net stop wuauserv",
                            "C: && cd C:\\Windows && "
                                    + Utils.getRemoveFolderCMD("Software Distribution"),
                                    "net start wuauserv",
                                    "explorer ms-settings:windowsupdate-action"
                            ),
                    "fix_windows_update"
                ),

            //Solution 4
            new Solution(
                    "Fix L2TP VPN Authentication Problems",
                    Utils.sList("Need a manual reboot"),
                    Utils.sList(
                            "reg add HKEY_LOCAL_MACHINE\\SYSTEM\\CurrentControlSet\\Services\\PolicyAgent /v AssumeUDPEncapsulationContextOnSendRule /t REG_DWORD /d 2 /f"
                    ),
                    "fix_l2tp_auth"
            ),

            new Solution(
                "Fix LaSalle printer",
                    Utils.sList("Will need to connect to LaSalleSecure (You need to connect at least once before & might cause bluescreen)"),
                    Utils.sList(
                            "netsh wlan disconnect",
                            "timeout 2",
                            "netsh wlan connect name=\"LaSalleSecure\"",
                            "netsh interface ip set dnsservers name=Wi-Fi source=dhcp"
                    ),
                    "fix_printer"
            )
    ));

    final static ArrayList<String> chromeFolderList =
            Utils.sList("Cache",
                    "Local Storage",
                    "Pepper Data",
                    "Media Cache",
                    "GPUCache",
                    "Storage",
                    "Application Cache",
                    "File System",
                    "Service Worker\\CacheStorage",
                    "..\\ShaderCache");


    final static ArrayList<String> msEdgeFolderList =
            Utils.sList(
                    "Microsoft Edge\\Cache",
                    "#!001\\MicrosoftEdge\\Cache",
                    "#!002\\MicrosoftEdge\\Cache",
                    "#!005\\MicrosoftEdge\\Cache",
                    "#!121\\MicrosoftEdge\\Cache",
                    "Temp",
                    "#!001\\MicrosoftEdge\\User\\Default\\AppCache",
                    "#!002\\MicrosoftEdge\\User\\Default\\AppCache",
                    "#!005\\MicrosoftEdge\\User\\Default\\AppCache",
                    "#!121\\MicrosoftEdge\\User\\Default\\AppCache"
            );

    final static ArrayList<WifiConfig> wifiConfig = new ArrayList<>(
            Arrays.asList(
                    new WifiConfig("LaSalleStatic",
                            Utils.sList(
                                    "<?xml version=\"1.0\"?>",
                                            "<WLANProfile xmlns=\"http://www.microsoft.com/networking/WLAN/profile/v1\">",
                                            "\t<name>LaSalleStatic</name>",
                                            "\t<SSIDConfig>",
                                            "\t\t<SSID>",
                                            "\t\t\t<hex>4C6153616C6C65537461746963</hex>",
                                            "\t\t\t<name>LaSalleStatic</name>",
                                            "\t\t</SSID>",
                                            "\t</SSIDConfig>",
                                            "\t<connectionType>ESS</connectionType>",
                                            "\t<connectionMode>manual</connectionMode>",
                                            "\t<MSM>",
                                            "\t\t<security>",
                                            "\t\t\t<authEncryption>",
                                            "\t\t\t\t<authentication>WPA2PSK</authentication>",
                                            "\t\t\t\t<encryption>AES</encryption>",
                                            "\t\t\t\t<useOneX>false</useOneX>",
                                            "\t\t\t</authEncryption>",
                                            "\t\t\t<sharedKey>",
                                            "\t\t\t\t<keyType>passPhrase</keyType>",
                                            "\t\t\t\t<protected>false</protected>",
                                            "\t\t\t\t<keyMaterial>e7B5kG9637WR302</keyMaterial>",
                                            "\t\t\t</sharedKey>",
                                            "\t\t</security>",
                                            "\t</MSM>",
                                            "\t<MacRandomization xmlns=\"http://www.microsoft.com/networking/WLAN/profile/v3\">",
                                            "\t\t<enableRandomization>false</enableRandomization>",
                                            "\t\t<randomizationSeed>3468873475</randomizationSeed>",
                                            "\t</MacRandomization>",
                                            "</WLANProfile>")),

                    new WifiConfig("LaSalleGuest", Utils.sList(
                            "<?xml version=\"1.0\"?>",
                            "<WLANProfile xmlns=\"http://www.microsoft.com/networking/WLAN/profile/v1\">",
                            "\t<name>LaSalleGuest</name>",
                            "\t<SSIDConfig>",
                            "\t\t<SSID>",
                            "\t\t\t<hex>4C6153616C6C654775657374</hex>",
                            "\t\t\t<name>LaSalleGuest</name>",
                            "\t\t</SSID>",
                            "\t</SSIDConfig>",
                            "\t<connectionType>ESS</connectionType>",
                            "\t<connectionMode>auto</connectionMode>",
                            "\t<MSM>",
                            "\t\t<security>",
                            "\t\t\t<authEncryption>",
                            "\t\t\t\t<authentication>open</authentication>",
                            "\t\t\t\t<encryption>none</encryption>",
                            "\t\t\t\t<useOneX>false</useOneX>",
                            "\t\t\t</authEncryption>",
                            "\t\t</security>",
                            "\t</MSM>",
                            "\t<MacRandomization xmlns=\"http://www.microsoft.com/networking/WLAN/profile/v3\">",
                            "\t\t<enableRandomization>false</enableRandomization>",
                            "\t\t<randomizationSeed>2861552956</randomizationSeed>",
                            "\t</MacRandomization>",
                            "</WLANProfile>"
                    ))));

}
