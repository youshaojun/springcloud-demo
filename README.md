#### 基于SpringCloud全家桶搭建的demo  
##### 说明:
##### 服务启动顺序
1.eureka(注册中心)  
2.zipkin(链路追踪)  
3.provider(api服务提供者)  
4.consumer(api服务消费者)  
5.zuul(网关)  
6.dashboard(服务监控)  
##### eureka闭源使用consul代替(consul需要独立安装)
windows安装  
官网下载: https://www.consul.io/downloads.html  
管理员启动 consul agent -dev  
打开网址 http://localhost:8500  
zuul停止更新使用gateway代替  
#### ELK
##### 分布式日志收集ELK是三个开源软件的缩写，分别表示：Elasticsearch, Logstash, Kibana
##### ELK Stack 包含 Elasticsearch, Logstash, Kibana, Beats
##### 日志收集流程
Logback(日志收集) -----> Logstash(日志过滤) -----> Elasticsearch(日志索引) -----> Kibana(可视化)
##### Elasticsearch 搜索引擎
由于SpringBoot更新速度远比Elasticsearch慢, 所以使用最新版会有不兼容问题, 解决起来十分麻烦  
2019年9月27日09:23:15 当前最新Elasticsearch版本为7.3.2  
本次使用为Elasticsearch6.7.1版本  
windows安装  
官网下载: https://www.elastic.co/cn/downloads/elasticsearch  
安装head客户端插件: https://github.com/mobz/elasticsearch-head  
es5以上版本安装head需要安装node和grunt  
安装node: https://nodejs.org/en/download/  
安装grunt: 执行 npm install -g grunt-cli  
安装grunt卡顿, 使用淘宝镜像: 执行 npm config set registry https://registry.npm.taobao.org  
然后重新安装  
安装Ik分词器(注意与es版本一致): https://github.com/medcl/elasticsearch-analysis-ik/releases  
拼音分词插件: https://github.com/medcl/elasticsearch-analysis-pinyin/releases  
繁体字插件: https://github.com/medcl/elasticsearch-analysis-stconvert/releases  
拼音分词参数: https://blog.csdn.net/a1148233614/article/details/80280024  
拼音分词会影响中文分词, 通常会根据业务添加相关参数的拼音字段进行处理  
es相关度BM25算法, 计算包含查询词的文档, 根据词频,权重等打分, 适用于精确命中文档  
BM25算法缺陷: 无法处理词语的语义相关性  
##### Logstash 日志过滤
创建启动配置文件, 配置日志文件来源 input, 配置日志信息导入Elasticsearch output  
使用配置文件方式启动logstash.bat  
provider模块使用的日志来源为文件方式, 是通过logback收集的日志文件  
consumer模块使用mq收集日志  
也可以通过控制台输出日志信息导入Elasticsearch  
##### Kibana 数据可视化
直接启动服务, 配置文件默认连接 http://localhost:9200  
Discover模块展示日志信息  
#### 写在后面 
##### 注意: 生产环境与demo会有所区别
生产环境中：  
1.linux环境  
2.集群化部署  
3.分布式配置中心统一管理配置文件  
4.日志收集引入FileBeat, 并使用MQ缓冲(直接使用Logstash处理日志文件, 如果日志数据量很大,  
     会造成Logstash过载, 或网络阻塞导致日志丢失等)  
5.统一管理版本号  
### 未完待续 . . . . . .





