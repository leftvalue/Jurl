package net;

import model.Request;
import org.fusesource.jansi.Ansi;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import tools.Contype2FilenameExtension;
import tools.Decode;
import tools.JansiService;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        if (body.contains("\\u")) {
            System.out.println("检测到 body 含有 unicode,进行解码");
            body = Decode.convert(body);
        }
        return body;
    }

    /**
     * 发送请求并根据相应的类型进行相应处理
     *
     * @return
     */
    public boolean handle() {
        if (!execute()) {
            System.out.println("请求失败");
            return false;
        }
        String contentType = response.contentType();
        boolean flag = false;
        /**
         *  字符类型,包括 html 及 json 类型
         */
        if (contentType.contains("application/json") || contentType.contains("text/html") || contentType.contains("text/plain;")) {
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
            System.out.println("下载到 " + path + " 完成");
            try {
                Desktop desktop = Desktop.getDesktop();
                desktop.open(new File(path));
            } catch (Exception e) {
                JansiService.print("预览下载文件失败 " + e.getMessage(), Ansi.Color.RED);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
