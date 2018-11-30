一flume环境搭建

参考：centos7 部署flume-总结

二springboot2 中log4j2集成flume

1.log4j2 推送日志到flume 模式avro 配置

flume配置文件 flume-conf-avro.properties 如下：

\# agent配置

a1.sources = r1

a1.sinks = k1

a1.channels = c1

\# agent source 配置

a1.sources.r1.type = avro

a1.sources.r1.bind = 192.168.126.131

a1.sources.r1.port = 4444

\# agent  sink 配置

a1.sinks.k1.type = file_roll

a1.sinks.k1.sink.directory = /tmp/log/flume

a1.sinks.k1.sink.rollInterval = 3600

\# agent  channel 配置

a1.channels.c1.type = memory

a1.channels.c1.capacity = 1000

a1.channels.c1.transactionCapacity = 100

\# 关联 channel-source, channel-sink

a1.sources.r1.channels = c1

a1.sinks.k1.channel = c1

配置说明：

必须设置sources type 为avro

sinks类型设置为滚动文件，每1小时滚动1个文件，日志存储路径为/tmp/log/flume

配置具体可参考：[http://flume.apache.org/FlumeUserGuide.html](http://flume.apache.org/FlumeUserGuide.html)

2.flume启动agent脚本(/usr/local/flume路径下)

启动脚本flume-startup.sh 如下：

bin/flume-ng agent -n a1 -f /usr/local/flume/conf/flume-conf-avro.properties -Dflume.root.logger=INFO,console &

3.pom.xml 工程依赖包引入

<dependency>

​    <groupId>org.springframework.boot</groupId>

​    <artifactId>spring-boot-starter</artifactId>

​    <version>${springboot.version}</version>

​    <exclusions>

​        <exclusion>

​            <groupId>org.springframework.boot</groupId>

​            <artifactId>spring-boot-starter-logging</artifactId>

​        </exclusion>

​    </exclusions>

</dependency>

<!-- log4j2 依赖 -->

<dependency>

​    <groupId>org.springframework.boot</groupId>

​    <artifactId>spring-boot-starter-log4j2</artifactId>

​    <version>${springboot.version}</version>

</dependency>

<!-- flume集成关键核心包 版本不用填写避免版本冲突 -->

<dependency>

​    <groupId>org.apache.logging.log4j</groupId>

​    <artifactId>log4j-flume-ng</artifactId>

​    <!--<version>2.0.2</version>-->

</dependency>

4.配置文件application.properties添加log4j2 日志配置

logging.config=classpath:log4j2.xml

5.log4j2.xml 配置文件配置添加Flume

log4j2.xml配置文件如下：

<?xml version="1.0" encoding="UTF-8"?>

<!-- https://www.cnblogs.com/zhangzhen894095789/p/6640808.html -->

<configuration packages="org.apache.logging.log4j">

​    <appenders>

​        <Console name="Console" target="SYSTEM_OUT">

​            <ThresholdFilter level="trace" onMatch="ACCEPT" onMismatch="DENY" />

​            <PatternLayout pattern="%d{HH:mm:ss.SSS} %-5level %class{36} %L %M - %msg%xEx%n" />

​        </Console>

​        <!--文件会打印出所有信息，这个log每次运行程序会自动清空，由append属性决定-->

​        <File name="Test" fileName="logs/test.log" append="false">

​            <PatternLayout pattern="%d{HH:mm:ss.SSS} %-5level %class{36} %L %M - %msg%xEx%n" />

​        </File>

​        <RollingFile name="RollingFile" fileName="logs/app.log" filePattern="logs/$${date:yyyy-MM}/app-%d{MM-dd-yyyy}-%i.log">

​            <PatternLayout pattern="%d{yyyy-MM-dd 'at' HH:mm:ss z} %-5level %class{36} %L %M - %msg%xEx%n" />

​            <SizeBasedTriggeringPolicy size="50MB" />

​        </RollingFile>

<!--Flume 日志推送配置-->

​        <Flume name="FlumeMyAppender" compress="false" type="Avro">

​            <Agent host="192.168.126.131" port="4444"/>

​            <RFC5424Layout enterpriseNumber="18060" includeMDC="true" appName="MyApp"/>

​        </Flume>

​    </appenders>

​    <loggers>

​        <root level="INFO">

​            <appender-ref ref="Console" />

​            <appender-ref ref="RollingFile" />

<!--Flume 日志推送配置-->

​            <appender-ref ref="FlumeMyAppender"/>

​        </root>

​    </loggers>

</configuration>

配置说明：

name 自定义，appender-ref 使用时对应

compress 是否压缩，ture压缩后文件为二进制不容易查看，false不压缩，默认编码格式为UTF-8，可以直接查看

type 必须定义为Avro模式

Agent host flume服务端IP

Agent port flume服务端监听端口

appName flume 日志应用标签，标记后日志打印如下所示：

<131>1 2018-11-29T18:13:36.855+08:00 DESKTOP-SI98SU1 MyApp  39016 - - 我是error

<132>1 2018-11-29T18:13:36.856+08:00 DESKTOP-SI98SU1 MyApp 39016 - - 我是warn

<134>1 2018-11-29T18:13:36.858+08:00 DESKTOP-SI98SU1 MyApp 39016 - - 我是info

配置完成可以运行查看效果了（在 /tmp/log/flume下跟踪日志信息）

完整示例代码路径：[https://github.com/lingjiuhun/demoFlume](https://github.com/lingjiuhun/demoFlume)