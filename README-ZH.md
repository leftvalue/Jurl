# [![](/imgs/jurl.png)](https://github.com/leftvalue/Jurl)
![](	https://img.shields.io/github/license/leftvalue/Jurl.svg) 
![](https://img.shields.io/badge/Power_by-leftvalue-orange.svg)
[![](https://img.shields.io/badge/download-it-blue.svg)](https://github.com/leftvalue/Jurl/blob/master/out/Jurl.jar)
## Description
[Jurl](https://github.com/leftvalue/Jurl/) 是一个基于 java 的命令行工具,通过粘贴从 firefox/chrome 等浏览器dev-tools 复制的 curl 网络请求,解析为 java 代码,方便定向网络爬虫的快速生成,同时为 cmd没有 curl 功能的 windows 使用者提供 一个备选方案(非 curl 功能的 全集,已在日程中)✈️

[English Readme](/README.md)
## Feature

- [x] 轻松将 curl 命令解析为 java 代码
- [x] windows 系统的 curl 备选
- [x] 提供对网络请求的 response 的简单格式化与文本高亮(HTML/JSON),图片类型直接下载并调用系统默认查看程序打开

## Synopsis

对于大多数浏览器在你访问某个站点的时候,dev-tools 都可以记录发送的每一条网络请求.当我平时因为一些事情需要写一些简单的定向爬虫的时候,需要快速的将某些特定请求写为 java 代码嵌入整个项目中.
但是有的请求很复杂,各种 headers cookies 和 datas 让我着实头疼,所以我写了这个小工具.
因为我平时经常使用 jsoup 作为java 的网络请求工具类库,所以我也将 jsoup 作为这个项目的基本网络请求类库.Jurl 使用 jsoup 来发送网络请求,同时导出的 java 代码也是使用 jsoup 的.如果你想要在项目中使用自动生成的 java 代码,你只需要在项目中引入 jsoup 即可(我使用的是 jsoup-1.10.3);
你需要做的只是,下载[Jurl.jar](/out/Jurl.jar)并运行它
```shell
java -jar Jurl.jar
```
打开你的浏览器的控制台,在 network 中找到需要重发的网络请求,右击 `Copy->Copy as curl`,然后粘贴到 Jurl 的命令窗口,即可解析粘贴的 curl 命令得到 java 代码.

* 输入 `V` 命令以预览请求的 response,如果是图片,将会自动调用系统默认查看程序打开.
* `C` 命令会自动读取系统剪切板的文字并当做 curl 命令解析.
* **PS:** 当解析完成输出结果后,会自动拷贝结果到系统剪切板
## Screen Capture
![](/imgs/0.png)
![](/imgs/1.png)
![](/imgs/2.png)

## 待完善
- [ ] httpClient HttpURLConnection等不同网络请求库的 java 代码的导出
- [ ] JSON以及 HTML 代码的更用户友好性的格式化与高亮
