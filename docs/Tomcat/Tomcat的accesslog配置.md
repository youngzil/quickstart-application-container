# tomcat配置
server.tomcat.max-threads=200
server.tomcat.max-http-post-size=20971520
server.tomcat.accept-count=1024
server.tomcat.uri-encoding=UTF-8
server.connection-timeout=20000

# tomcat accesslog配置
# Buffer output such that it is only flushed periodically.开启缓存
server.tomcat.accesslog.buffered=false
#server.tomcat.accesslog.buffered=true
#server.tomcat.accesslog.directory=dev null
server.tomcat.accesslog.directory=/Users/yangzl/center/logs
server.tomcat.accesslog.enabled=true
#server.tomcat.accesslog.enabled=false
# Date format to place in log file name.
server.tomcat.accesslog.file-date-format=.yyyy-MM-dd.HH
# Format pattern for access logs.
server.tomcat.accesslog.pattern="request-start@%{begin:yyyy-MM-dd HH:mm:ss.S}t" "%a (%{X-Forwarded-For}i) -> %A:%p" "%r" %{Content-Length}i process-req-time:%D "response-end@%{end:yyyy-MM-dd HH:mm:ss.S}t" status:%s %B %I "%{Referer}i" "%{User-Agent}i" traceId:%{X-Trace-Id}i
# Log file name prefix.
server.tomcat.accesslog.prefix=access_log
# Defer inclusion of the date stamp in the file name until rotate time.
server.tomcat.accesslog.rename-on-rotate=false
# Set request attributes for IP address, Hostname, protocol and port used for the request.
server.tomcat.accesslog.request-attributes-enabled=false
# Enable access log rotation.
server.tomcat.accesslog.rotate=true
# Log file name suffix.
server.tomcat.accesslog.suffix=.log





配置成这种：
server.tomcat.accesslog.pattern="request-start@%{begin:yyyy-MM-dd HH:mm:ss.S}t" "%a (%{X-Forwarded-For}i) -> %A:%p" "%r" %{Content-Length}i process-req-time:%D "response-end@%{end:yyyy-MM-dd HH:mm:ss.S}t" status:%s %B %I "%{Referer}i" "%{User-Agent}i" traceId:%{X-Trace-Id}i



打印的格式如下：

"request-start@2021-04-02 07:00:00.267" "10.1.23.166 (-) -> 10.1.25.60:8080" "GET /test/fetch?consumerId=10.1.23.166-998-CAAB9&appName
=wac-finance-sangreal&groupId=wc-finance-matrix-position-income&topic=wc.finance.matrix.position2&reset=1&fetchMax=500&fetchCnt=926186139&pollM
s=200&filterExpress= HTTP/1.1" - process-req-time:204 "response-end@2021-04-02 07:00:00.471" status:200 281 http-nio-8080-exec-110 "-" "Java/1.
8.0_121" traceId:627d664eedc52a7d




