package model;

import org.jsoup.Connection;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author linxi
 * www.leftvalue.top
 * model
 * Date 22/11/2017 7:50 PM
 */
public class Request {
    private String url;//""
    private String user_agent;//-H 'User-Agent:
    private String referrer;// -H 'Referer:
    private HashMap<String, String> heads;//-H
    private HashMap<String, String> cookies;//-H 'Cookie:'
    private HashMap<String, String> datas;
    private Connection.Method method;

    public Request() {
        //nothing
        this.method = Connection.Method.GET;
        this.heads = new HashMap<>();
        this.cookies = new HashMap<>();
        this.datas = new HashMap<>();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
//System.out.println("[URL] " + url);
    }

    public String getUser_agent() {
        return user_agent;
    }

    public void setUser_agent(String user_agent) {
        this.user_agent = user_agent;
//        System.out.println("[User-Agent] " + user_agent);
    }

    public String getReferrer() {
        return referrer;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
//        System.out.println("[Referer] " + referrer);
    }

    public HashMap<String, String> getHeads() {
        return heads;
    }

    public void setHeads(HashMap<String, String> heads) {
        this.heads = heads;
    }

    public void addHead(String key, String value) {
        this.heads.put(key, value);
    }

    static Pattern pattern = Pattern.compile("^(.*): (.*)$");

    public void addHead(String key_value) {
        Matcher matcher = pattern.matcher(key_value);
        if (matcher.matches()) {
            String key = matcher.group(1);
            String value = matcher.group(2);
            if (key.equals("Cookie")) {
                setCookies(value);
            } else if (key.equals("User-Agent")) {
                setUser_agent(value);
            } else if (key.equals("Referer")) {
                setReferrer(value);
            } else {
//                System.out.println("[H] " + key + "=" + value);
                this.heads.put(key, value);
            }
        }

    }

    public HashMap<String, String> getCookies() {
        return cookies;
    }

    public void setCookies(HashMap<String, String> cookies) {
        this.cookies = cookies;
    }

    static Pattern cookiePattern = Pattern.compile("(.*)=(.*)");

    public void setCookies(String cookie_str) {
        String[] cookie_split = cookie_str.split("; ");
        for (String cookie : cookie_split) {
            Matcher matcher = cookiePattern.matcher(cookie);
            if (matcher.matches()) {
                String key = matcher.group(1);
                String value = matcher.group(2);
                //System.out.println("[Cookie] " + key + "=" + value);
                this.cookies.put(key, value);
            }
        }
    }

    public HashMap<String, String> getDatas() {
        return datas;
    }

    public void setDatas(HashMap<String, String> datas) {
        this.datas = datas;
    }

    public void setDatas(String datas_str) {
        String[] data_split = datas_str.split("&");
        for (String data : data_split) {
            Matcher matcher = cookiePattern.matcher(data);
            if (matcher.matches()) {
                String key = matcher.group(1);
                String value = matcher.group(2);
                //System.out.println("[Data] " + key + "=" + value);
                this.datas.put(key, value);
            }
        }
        this.method = Connection.Method.POST;
    }

    public Connection.Method getMethod() {
        return method;
    }

    public void setMethod(Connection.Method method) {
        this.method = method;
    }
}
