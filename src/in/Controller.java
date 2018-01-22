package in;
/**
 * @author linxi
 * www.leftvalue.top
 * in
 * Date 22/11/2017 7:50 PM
 */

import model.Request;
import net.JsoupNetTool;
import tools.JansiService;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.fusesource.jansi.Ansi.*;

public class Controller {

    public static void main(String[] args) {
        try {
            String description = "欢迎来到 Jurl 的世界 :)\n" +
                    "粘贴 curl 指令并回车以得到 java 代码\n" +
                    "其他命令 :\n" +
                    "V : 预览上次网络请求的 response(如果请求得到文件,则直接下载文件到 pwd 下);\n" +
                    "Q 或 exit : 退出程序\n" +
                    "\t\t\tby leftvalue";
            System.out.println(ansi().fg(Color.BLUE).bold().bg(Color.WHITE).a(description).reset());
            JansiService.print(":)", Color.YELLOW);
            JsoupNetTool jnt = null;
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                if (line.trim().isEmpty()) {
                    /**
                     * nothing to do
                     */
                } else if (testCommand("Q", line) || testCommand("EXIT", line)) {
                    JansiService.print("Have a nice day. Goodbye~", Color.BLUE);
                    break;
                } else if (testCommand("V", line)) {
                    if (jnt != null) {
                        jnt.handle();
                    } else {
                        JansiService.print("无历史 curl 记录,请输入 curl 后预览", Color.RED);
                    }
                } else {
                    try {
                        Request request = parse(line);
                        if (request == null) {
                            JansiService.print("命令解析失败,请检查重试~", Color.RED);
                            System.out.println("");
                        } else {
                            jnt = new JsoupNetTool(request);
                            JansiService.print(jnt.write2JAVA(), Color.GREEN);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                JansiService.print(":)", Color.YELLOW);
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
