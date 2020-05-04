https://github.com/undertow-io/undertow
http://undertow.io



WildFly 8 包含了一个全新的Web服务器（Undertow），WildFly 8 默认的Web服务器为Undertow。一句话概括什么是Undertow - 高性能非阻塞 Web 服务器。

Undertow 主要有以下几个特点： 
1、轻量化 - Undertow 是一个Web 服务器，但它不像传统的Web 服务器有容器的概念，它由两个核心jar包组成，使用API加载一个Web应用可以使用小于10MB的内存
2、HTTP Upgrade 支持 - 设计WildFly时一个重要的考虑因素是在云环境中减少端口数量的需求。在云环境中，一个系统可能运行了几百个甚至几千个WildFly实例。基于HTTP使用HTTP Upgrade可以升级成多种协议，Undertow提供了复用这些协议的能力。
3、Web Socket 支持 - 对Web Socket的完全支持，用以满足Web应用现在面对巨大数量的客户端，以及对JSR-356规范的支持
4、Servlet 3.1 的支持 - Undertow支持Servlet 3.1，提供了一个机会来构建一个超越Servlet规范、对开发人员非常友好的系统。
5、可嵌套性 - Web 服务器不在需要容器，我们只需要通过API在J2SE代码下快速搭建Web服务 



相关链接及快速开始示例
Undertow 社区主页（http://undertow.io/）：包括Undertow相关的所有新闻，消息。
Undertow 源代码（https://github.com/undertow-io/）：包括所有Undertow相关的代码
Undertow 快速开始示例（https://github.com/kylinsoong/wildfly-architecture/tree/master/undertow/quickstart）：包括Undertow 快速开始示例，



参考
http://www.ibloger.net/article/2964.html
https://blog.csdn.net/fayeyiwang/article/details/54729523
https://blog.csdn.net/fayeyiwang/article/details/54729550


