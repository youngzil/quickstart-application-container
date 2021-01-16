- [WildFly介绍](#WildFly介绍)
- [启动和停止WildFly](#启动和停止WildFly)



---------------------------------------------------------------------------------------------------------------------

## WildFly介绍

WildFly，以前称为JBoss Application Server，由红帽 （Red Hat）开发，是另一个功能齐全且经过认证的应用服务器。

WildFly，前身是JBoss AS，从V8开始为区别于JBoss EAP，更名为WildFly. 由红帽 （Red Hat）开发，是另一个功能齐全且经过认证的应用服务器.
Wildfly是一个开源的基于JavaEE的轻量级应用服务器。可以在任何商业应用中免费使用。
WildFly是一个灵活的、轻量的、强大管理能力的应用程序服务器。Wildfly是一个管理EJB的容器和服务器，但JBoss核心服务不包括支持servlet/JSP的WEB容器，一般与Tomcat或Jetty绑定使用。

作为将JBoss Application Server项目重命名为WildFly的一部分，jboss-as git repo已移至http://github.com/wildfly/wildfly。

WildFly 8 包含了一个全新的Web服务器（Undertow），WildFly 8 默认的Web服务器为Undertow。一句话概括什么是Undertow - 高性能非阻塞 Web 服务器。



https://wildfly.org/
https://docs.wildfly.org/
https://github.com/wildfly/wildfly
https://github.com/wildfly/quickstart
https://developer.jboss.org/en/wildfly



http://www.jboss.org/
http://jbossas.jboss.org/downloads
https://github.com/jbossas/jboss-as


https://www.redhat.com/en/technologies/jboss-middleware/business-rules
https://developers.redhat.com/products/brms/download/?referrer=jbd


https://www.oschina.net/p/jboss+brms
https://www.oschina.net/p/jboss+as



---------------------------------------------------------------------------------------------------------------------

## 启动和停止WildFly

成功构建后切换到bin目录
$ cd build/target/wildfly-[version]/bin

以域模式启动服务器
./domain.sh

以独立模式启动服务器
./standalone.sh

要停止服务器，请按Ctrl + C，或使用管理控制台
./jboss-cli.sh --connect command=:shutdown


有关如何启动和停止WildFly的更多信息，请参见WildFly文档中的“入门指南”。




cd /Users/yangzl/mysoft/wildfly-20.0.1.Final/bin
sh standalone.sh
启动后打开：有控制台等管理功能
http://localhost:8080/

要打开控制台
执行sh add-user.sh添加用户

可以在上面的页面上打开，也可以打开下面链接
http://127.0.0.1:9990/console
http://127.0.0.1:9990/management


---------------------------------------------------------------------------------------------------------------------

