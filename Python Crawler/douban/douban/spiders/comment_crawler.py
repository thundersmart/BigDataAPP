import scrapy
from scrapy import Request
from douban.items import DoubanItem
import scrapy.item
from pymongo.mongo_client import MongoClient
from datetime import datetime


# 获取链接以及定义id作为表名
def get_href_content():
    Mongo_Conn = MongoClient('localhost', 27017)
    conn = Mongo_Conn['crwdata']['name_list']
    content = conn.find_one({'done': 0})
    href = content['href']
    _id = content['_id']
    table_name = 'E' + str(_id)
    return _id, href, table_name


# cookie_str转换成字典
def get_cookies(cookie_line):
    cookies = {}
    for line in cookie_line.split(';'):
        key, value = line.split('=', 1)  # 1代表只分一次，得到两个数据
        cookies[key] = value
    return cookies


class CommentSpider(scrapy.Spider):
    """docstring for Movie250Spider"""

    name = 'comment'
    allowed_domains = ["douban.com"]
    cookie_line = 'bid=p1J2_Lwkp5M; ll="118281"; ps=y; ap=1; _vwo_uuid_v2=F976B7C47BBEB9D68A293C74C643CA52|b79b4a2ebd20a41a88683db26d0a123f; _ga=GA1.2.392887690.1489644501; _pk_ref.100001.4cf6=%5B%22%22%2C%22%22%2C1490839415%2C%22https%3A%2F%2Fwww.douban.com%2Faccounts%2Flogin%3Fredir%3Dhttps%253A%252F%252Fmovie.douban.com%252Fsubject%252F26298935%252Fcomments%253Fstart%253D33673%2526limit%253D20%2526sort%253Dnew_score%2526status%253DP%22%5D; __utmt=1; ue="1092717097@qq.com"; dbcl2="158586785:b2eWOh/AaIM"; ck=ShmL; push_noty_num=0; push_doumail_num=0; __utma=30149280.392887690.1489644501.1490758150.1490839418.10; __utmb=30149280.1.10.1490839418; __utmc=30149280; __utmz=30149280.1490758150.9.6.utmcsr=baidu|utmccn=(organic)|utmcmd=organic; __utmv=30149280.15858; __utma=223695111.392887690.1489644501.1490243462.1490839430.6; __utmb=223695111.0.10.1490839430; __utmc=223695111; __utmz=223695111.1490839430.6.6.utmcsr=douban.com|utmccn=(referral)|utmcmd=referral|utmcct=/accounts/login; _pk_id.100001.4cf6=6edf0d3e7b47224d.1489480588.8.1490839436.1490243485.; _pk_ses.100001.4cf6=*'
    headers = {
        'User-Agent': "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36",
    }
    cookie = get_cookies(cookie_line)
    _id, href, table_name = get_href_content()

    def start_requests(self):
        yield Request(self.href + 'comments?status=P', headers=self.headers, cookies=self.cookie)
        #yield Request('https://movie.douban.com/subject/26363830/comments?start=24362&limit=20&sort=new_score&status=P',
        #              headers=self.headers, cookies=self.cookie)

    def parse(self, response):
        for info in response.xpath('//div[@class="comment-item"]'):
            item = DoubanItem()
            item['_id'] = info.xpath('@data-cid')[0].extract()
            #排除名字为空的情况
            name = info.xpath('div[@class="comment"]/h3/span[@class="comment-info"]/a[@class]/text()')
            if name:
                item['name'] = name[0].extract()
            else:
                continue
            #排除不评分的情况
            rating = info.xpath('div[@class="comment"]/h3/span[@class="comment-info"]/span[@class]/@title')[0]
            if rating:
                item['rating'] = rating.extract()
            else:
                continue
            item['comment'] = info.xpath('div[@class="comment"]/p[@class]/text()')[0].extract().strip()
            item['update_time'] = datetime.now()
            yield item

        # 翻页
        next_page = response.xpath('//a[@class="next"]/@href')
        if next_page:
            url = response.urljoin(self.href + 'comments' + next_page[0].extract())
            yield scrapy.Request(url, headers=self.headers, cookies=self.cookie)
