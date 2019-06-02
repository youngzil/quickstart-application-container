Tomcat生产部署：配置详解、启动参数调优及JVM参数优化


---------------------------------------------------------------------------------------------------------------------
https://www.cnblogs.com/kismetv/p/7228274.html
http://blog.chopmoon.com/favorites/231.html
https://juejin.im/post/5cdbb741e51d453af7192b98

Tomcat生产部署

整体关系
核心组件之间的整体关系，在上一部分有所介绍，这里总结一下：
Server元素在最顶层，代表整个Tomcat容器；一个Server元素中可以有一个或多个Service元素。
Service在Connector和Engine外面包了一层，把它们组装在一起，对外提供服务。一个Service可以包含多个Connector，但是只能包含一个Engine；Connector接收请求，Engine处理请求。
Engine、Host和Context都是容器，且 Engine包含Host，Host包含Context。每个Host组件代表Engine中的一个虚拟主机；每个Context组件代表在特定Host上运行的一个Web应用。

Tomcat请求流程：
1、对外接收请求是Connector连接器组件，可以支持不同协议，Connector组件中可设置端口，所以我们请求的时候需要输入端口号。可以把Connector当作接待员。
2、Connector组件接收到请求后，转发给处理Engine(catalina引擎)组件去处理。
3、根据请求的域名，分配到对应的Host主机。
4、在根据path分配context组件

JAVA_OPT  jvm启动参数，设置内存编码等
JAVA_HOME  jdk路径
CATALINA_HOME:   tomcat程序根目录
CATALINA_BASE:   应用部署目录，默认是$CATALINA_HOME
CATALINA_OUT:   应用日志输出目录，默认是$CATALINA_BASE/logs
CATALINA_TMPDIR:应用临时目录，默认为$CATALINA_BASE/temp


1、通过以上参数把Tomcat和web应用目录分离出来，即使Tomcat升级也跟web应用没有关系。
2、改造脚本，如下
3、war包或jar包是按照什么流程以及方式上传到生产环境的？这个可以使用自动化集成工具Jenkins。

用了网上的一个脚本改造了一下，命名为tomcat.sh
exp#!/bin/bash
export JAVA_OPTS="-Xms100m -Xmx200m"
export JAVA_HOME="/usr/java/jdk1.8.0_181"
export CATALINA_HOME="/usr/local/tomcat"
export CATALINA_BASE="`pwd`"

case $1 in
    start)
        $CATALINA_HOME/bin/catalina.sh start
        echo start success!!
        ;;
    stop)
        $CATALINA_HOME/bin/catalina.sh stop
        echo stop success!!
        ;;
    restart)
        $CATALINA_HOME/bin/catalina.sh stop
        echo stop success!!
        sleep 2
        $CATALINA_HOME/bin/catalina.sh start
        echo start success!!
        ;;
    version)
        $CATALINA_HOME/bin/catalina.sh version
        ;;
    configtest)
        $CATALINA_HOME/bin/catalina.sh configtest
        ;;
esac
exit 0



