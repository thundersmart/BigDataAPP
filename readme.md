这是一个基于Python爬虫和jieba分词处理的网剧评分APP，项目分为三个部分：爬虫程序、服务端、APP客户端，数据存储系统基于mongodb
目录内容
Android APP——android客户端程序，编写环境为android studio
Data——数据库数据
Python Crawler——爬虫程序，基于scrapy分布式爬虫，分别douban（豆瓣内容获取）、video_comment（网剧播放网站的评论获取）、zhihu（知乎评论内容获取）
Web Servlet——发布数据接口的服务端