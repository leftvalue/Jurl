# ![![Jurl][logo]][Jurl]
   [logo]: https://mrdickplus.github.io/Jurl/jurl.png "Jurl"
## Description
[Jurl][Jurl] is a command line tool to help you get java code (which use Jsoup to send http/https request) and quickly send the request as curl
## Feature
-[x] Easy to covert curl commands into java.
-[x] Help windows users who don't have curl in cmd.
-[x] Simply format and highlight Html & Json for reading friendly
## Synopsis

Mostly browsers have dev-tools that you can easily see all network history when you visit a website.When I write spider ,I need to use serveral special request and resend it to get my useful information(data).

But I am mad at writing java code to build my spider again and again when the request has many headers 、datas as well as cookies which I don't know which is useful or which is not.So I create the tool.

Because I usually use the java library 'jsoup' to send request,so I choose it as the base network request tool in the project.Jurl use jsoup to send request,and,the java code it auto creates also use jsoup.So if you wanna use the auto-created java code,you will need resolve the dependence of jsoup(I use jsoup-1.10.3).

What you need to do is,download the Jurl.jar and run it :
```shell
java -jar Jurl.jar
```
Open your browser (such as Chrome or Firefox and so on) and then open the dev-tools.After you visit a website and want to use a request,just Copy-\>Copy as curl,and paste it in the jurl terminal.It will give you the java code.
You can also enter V to view the response of last request.If the request got an response that is not text,jurl will download it to the current workspace and open it using the default app of your desktop.
## Example Usage
