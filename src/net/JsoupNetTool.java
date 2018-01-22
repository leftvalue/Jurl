package net;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.Request;
import org.fusesource.jansi.Ansi;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import tools.Contype2FilenameExtension;
import tools.JansiService;

import static tools.JansiService.*;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.fusesource.jansi.Ansi.ansi;

/**
 * @author linxi
 * www.leftvalue.top
 * net
 * Date 22/11/2017 7:53 PM
 */
public class JsoupNetTool extends BaseNetTool {
    private Connection.Response response;

    public JsoupNetTool(Request request) {
        super(request);
    }

    /**
     * @return 执行local  request
     */
    public boolean execute() {
        try {
            Connection connection = Jsoup.
                    connect(request.getUrl());
            if (request.getUser_agent() != null) {
                connection = connection.userAgent(request.getUser_agent());
            }
            if (request.getReferrer() != null) {
                connection = connection.referrer(request.getReferrer());
            }
            if (!request.getCookies().isEmpty()) {
                connection = connection.cookies(request.getCookies());
            }
            if (!request.getHeads().isEmpty()) {
                for (Map.Entry<String, String> entry : request.getHeads().entrySet()) {
                    connection = connection.header(entry.getKey(), entry.getValue());
                }
            }
            if (!request.getDatas().isEmpty()) {
                connection = connection.data(request.getDatas());
            }
            this.response = connection.method(request.getMethod()).ignoreContentType(true).timeout(10000).execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * @return 能够执行的 Jsoup 连接语句 string
     */
    @Deprecated
    public String write2JAVA() {
        String str = "Connection.Response response = Jsoup.connect(\"" + request.getUrl() + "\")\n";
        if (request.getUser_agent() != null) {
            str += ".userAgent(\"" + request.getUser_agent() + "\")\n";
        }
        if (request.getReferrer() != null) {
            str += ".referrer(\"" + request.getReferrer() + "\")\n";
        }
        if (!request.getCookies().isEmpty()) {
            for (Map.Entry<String, String> entry : request.getCookies().entrySet()) {
                str += ".cookie(\"" + entry.getKey() + "\",\"" + entry.getValue() + "\")\n";
            }
        }
        if (!request.getHeads().isEmpty()) {
            for (Map.Entry<String, String> entry : request.getHeads().entrySet()) {
                str += ".header(\"" + entry.getKey() + "\",\"" + entry.getValue() + "\")\n";
            }
        }
        if (!request.getDatas().isEmpty()) {
            for (Map.Entry<String, String> entry : request.getDatas().entrySet()) {
                str += ".data(\"" + entry.getKey() + "\",\"" + entry.getValue() + "\")\n";
            }
        }
        str += ".method(" + (request.getMethod() == Connection.Method.POST ? "org.jsoup.Connection.Method.POST" : "org.jsoup.Connection.Method.GET")
                + ")\n.ignoreContentType(true)\n.timeout(10000)\n.execute();";
        return str;
    }

    private String yellow(String str) {
        return getColorString(str, Ansi.Color.YELLOW);
    }

    private String blue(String str) {
        return getColorString(str, Ansi.Color.BLUE);
    }

    public String write2ColorfulJAVA() {
        String str = yellow("Connection.Response response = Jsoup.connect(\"") + blue(request.getUrl()) + yellow("\")\n");
        if (request.getUser_agent() != null) {
            str += yellow(".userAgent(\"") + blue(request.getUser_agent()) + yellow("\")\n");
        }
        if (request.getReferrer() != null) {
            str += yellow(".referrer(\"") + blue(request.getReferrer()) + yellow("\")\n");
        }
        if (!request.getCookies().isEmpty()) {
            for (Map.Entry<String, String> entry : request.getCookies().entrySet()) {
                str += yellow(".cookie(\"") + blue(entry.getKey()) + yellow("\",\"") + blue(entry.getValue()) + yellow("\")\n");
            }
        }
        if (!request.getHeads().isEmpty()) {
            for (Map.Entry<String, String> entry : request.getHeads().entrySet()) {
                str += yellow(".header(\"") + blue(entry.getKey()) + yellow("\",\"") + blue(entry.getValue()) + yellow("\")\n");
            }
        }
        if (!request.getDatas().isEmpty()) {
            for (Map.Entry<String, String> entry : request.getDatas().entrySet()) {
                str += yellow(".data(\"") + blue(entry.getKey()) + yellow("\",\"") + blue(entry.getValue()) + yellow("\")\n");
            }
        }
        str += yellow(".method(") + blue(request.getMethod() == Connection.Method.POST ? "org.jsoup.Connection.Method.POST" : "org.jsoup.Connection.Method.GET")
                + yellow(")\n.ignoreContentType(") + blue("true") + yellow(")\n.timeout(") + blue("10000") + yellow(")\n.execute();");
        return str;
    }

    static final Pattern s_file_name_pattern = Pattern.compile("^https?://(?:.*?/)*(.*\\.(?:png|jpeg|gif|jpg|bmp|icon|mp3|mp4))");
    static final DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss");

    /**
     * @return 从 request 的 url 中找到文件名,如果不存在,则使用当前时间
     * PS: 返回文件名不包含 path
     */
    public String getFileName(String contentType) {
        Matcher matcher = s_file_name_pattern.matcher(this.request.getUrl());
        String name = "";
        if (matcher.find()) {
            name = matcher.group(1);
        } else {
            name = LocalDateTime.now().format(dtFormatter).concat(Contype2FilenameExtension.getExtension(contentType));
        }
        return name;
    }

    public String getBody() {
        String body = response.body();
//        if (body.contains("\\u")) {
//            System.out.println("检测到 body 含有 unicode,进行解码");
//            body = Decode.convert(body);
//        }
        return body;
    }

    private static final Pattern htmlTagPattern = Pattern.compile("<([^<>]*)>");

    /**
     * 发送请求并根据相应的类型进行相应处理
     *
     * @return
     */
    public boolean handle() {
        if (!execute()) {
            System.out.println("Fail to request.");
            return false;
        }
        String contentType = response.contentType();
        boolean flag = false;
        /**
         *  字符类型,包括 html 及 json 类型
         */
        if (contentType.contains("application/json")) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                Object obj = mapper.readValue(getBody(), Object.class);
                String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
                json = json.replaceAll("\\[", ansi().fg(Ansi.Color.CYAN).a("[").reset().toString());
                json = json.replaceAll("\\]", ansi().fg(Ansi.Color.CYAN).a("]").reset().toString());
                json = json.replaceAll("\\{", ansi().fg(Ansi.Color.YELLOW).a("{").reset().toString());
                json = json.replaceAll("\\}", ansi().fg(Ansi.Color.YELLOW).a("}").reset().toString());
                json = json.replaceAll(",", ansi().fg(Ansi.Color.GREEN).a(",").reset().toString());
                json = json.replaceAll(":", ansi().fg(Ansi.Color.RED).a(":").reset().toString());
                json = json.replace("\"", ansi().fg(Ansi.Color.BLUE).a("\"").reset().toString());
                System.out.println(json);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(getBody());
            }
        } else if (contentType.contains("text/html")) {
            String body = getBody();
            body = body.replaceAll("\\$", "RDS_CHAR_DOLLAR");// encode replacement;
            StringBuffer sb = new StringBuffer();
            Matcher me = htmlTagPattern.matcher(body);
            String leftquote = ansi().fg(Ansi.Color.YELLOW).a("<").reset().toString();
            String right = ansi().fg(Ansi.Color.YELLOW).a(">").reset().toString();
            while (me.find()) {
                String temp = leftquote + ansi().fg(Ansi.Color.BLUE).a(me.group(1)).reset().toString() + right;
                me.appendReplacement(sb, temp);
            }
            me.appendTail(sb);
            body = sb.toString().replaceAll("RDS_CHAR_DOLLAR", "\\$");
            System.out.println(sb.toString());
        } else if (contentType.contains("text/plain;")) {
            String body = getBody();
            JansiService.print(body, Ansi.Color.CYAN);
            flag = true;
        } else {
            String name = getFileName(contentType);
            String path = new File("").getAbsolutePath();
            String file_path = path + File.separator + name;
            flag = download2local(response, file_path);
        }
        return flag;
    }

    public boolean download2local(Connection.Response response, String path) {
        try {
            FileOutputStream out = (new FileOutputStream(new java.io.File(path)));
            out.write(response.bodyAsBytes());
            out.close();
            System.out.println("Download to " + path + " complete.");
            try {
                Desktop desktop = Desktop.getDesktop();
                desktop.open(new File(path));
            } catch (Exception e) {
                JansiService.print("Fail to download file " + e.getMessage() + ".", Ansi.Color.RED);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
