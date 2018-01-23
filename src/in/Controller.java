package in;
/**
 * @author linxi
 * www.leftvalue.top
 * in
 * Date 22/11/2017 7:50 PM
 */

import model.Request;
import net.JsoupNetTool;
import org.fusesource.jansi.Ansi;
import tools.JansiService;
import tools.SystemClipboardTools;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.fusesource.jansi.Ansi.*;

public class Controller {


    public static void main(String[] args) {
        try {
            String description = "Welcome to the world of Jurl :)\n" +
                    "You can paste curl command and enter to get Java Code(in jsoup)\n" +
                    "Other commands :\n" +
                    "V : view the result of the last request's response(if the response is not text,it will auto download to pwd );\n" +
                    "Q 或 exit : exit the application\n" +
                    "\t\t\tby leftvalue";
            System.out.println(ansi().fg(Ansi.Color.BLUE).bold().bg(Ansi.Color.WHITE).a(description).reset());
            JansiService.print(":)", Ansi.Color.YELLOW);
            JsoupNetTool jnt = null;
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                if (line.trim().isEmpty()) {
                    /**
                     * nothing to do
                     */
                } else if (testCommand("Q", line) || testCommand("EXIT", line)) {
                    JansiService.print("Have a nice day. Goodbye~", Ansi.Color.BLUE);
                    break;
                } else if (testCommand("V", line)) {
                    if (jnt != null) {
                        jnt.handle();
                    } else {
                        JansiService.print("No history request ,please paste curl commands and retry again", Ansi.Color.RED);
                    }
                } else {
                    if (testCommand("C", line)) {
                        line = SystemClipboardTools.get();
                        System.out.println(line + "\n");
                    }
                    try {
                        Request request = parse(line);
                        if (request == null) {
                            JansiService.print("Fail to parse the command,please check it~", Ansi.Color.RED);
                        } else {
                            jnt = new JsoupNetTool(request);
                            JansiService.print(jnt.write2ColorfulJAVA(), Ansi.Color.GREEN);
                            SystemClipboardTools.write(jnt.write2JAVA());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                JansiService.print(":)", Ansi.Color.YELLOW);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean testCommand(String command, String input) {
        return command.trim().toUpperCase().equals(input.trim().toUpperCase());
    }

    private static Pattern urlPattern = Pattern.compile("curl ['\\\"](.*?)['\\\"]");
    private static Pattern headPattern = Pattern.compile("-H ['\\\"](.*?)['\\\"]");
    private static Pattern dataPattern = Pattern.compile("--data ['\\\"](.*?)['\\\"]");
    private static Pattern data_bin_Pattern = Pattern.compile("--data-binary ['\\\"](.*?)['\\\"]");

    public static Request parse(String curl) {
        Request request = new Request();
        boolean success = false;
        /**
         * URL
         */
        Matcher url_m = urlPattern.matcher(curl);
        if (url_m.find()) {
            request.setUrl(url_m.group(1));
            success = true;
        }
        /**
         * -H
         */
        Matcher heads_m = headPattern.matcher(curl);
        while (heads_m.find()) {
            request.addHead(heads_m.group(1));
        }
        /**
         * --data
         */
        Matcher data_m = dataPattern.matcher(curl);
        if (data_m.find()) {
            request.setDatas(data_m.group(1));
        }

        /**
         * --data-binary
         */
        Matcher data_bin_m = data_bin_Pattern.matcher(curl);
        if (data_bin_m.find()) {
            request.setDatas(data_bin_m.group(1));
        }
        if (success == true) {
            return request;
        } else {
            return null;
        }
    }
}
