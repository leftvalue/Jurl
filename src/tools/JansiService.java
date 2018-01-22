package tools;

import org.fusesource.jansi.Ansi;

import static org.fusesource.jansi.Ansi.ansi;

/**
 * @author linxi
 * www.leftvalue.top
 * tools
 * 用于控制台的多彩显示
 * Date 22/01/2018 9:19 PM
 */
public class JansiService {
    /**
     * 输出但是不翻页
     *
     * @param str
     * @param color
     */
    public static void print(String str, Ansi.Color color) {
        System.out.println(ansi().fg(color).a(str).reset());
    }

    /**
     * 翻页后输出
     *
     * @param str
     * @param color
     */
    public static void printScreen(String str, Ansi.Color color) {
        System.out.println(ansi().eraseScreen().fg(color).a(str).reset());
    }

}
