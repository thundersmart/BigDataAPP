import scrapy
from scrapy import Request
from zhihu.items import ZhihuItem
import scrapy.item
import json
from pymongo.mongo_client import MongoClient
from zhihu.spiders import scrapy_set


# 解析cookies
def get_cookie(cookie_line):
    cookies = {}
    for line in cookie_line.split(';'):
        key, value = line.split('=', 1)
        cookies[key] = value
    return cookies


class ZhihuSpider(scrapy.Spider):
    """docstring for Movie250Spider"""
    name = 'zhihu_answers_comments'
    allowed_domains = ["zhihu.com"]
    headers = {
        'User-Agent': "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36"
    }
    cookie_line = '_zap=66d3a24b-7071-4af7-94e3-1d83022d5dea; _za=7f2f1a3e-9058-4e06-ae7c-978387c449bd; d_c0="ACBAoTCaQQqPTsYv8cLwx0JQ_B25CYTiQx8=|1468981187"; _zap=2788fd21-223f-416d-9d7a-beb4d2e447a3; __utma=51854390.145560521.1443286957.1479437018.1479654147.8; __utmz=51854390.1479654147.8.8.utmcsr=baidu|utmccn=(organic)|utmcmd=organic; __utmv=51854390.100--|2=registration_date=20160815=1^3=entry_date=20160815=1; q_c1=e01babd6a64947928f727ab9723f6936|1487687994000|1475600020000; _xsrf=655d508a49d3f010c2072903f1ac8eba; nweb_qa=heifetz; aliyungf_tc=AQAAAPZFBhQdEQ4Aqxo+t2/+bk+u4BTX; capsion_ticket="2|1:0|10:1490172367|14:capsion_ticket|44:Nzc2ZDZmN2E5ZTlhNGRiMzhiNWNjM2ZmYTU5OWVkM2E=|b5eb04e6d2893cd8956c068c9e527169aa6fa82f83e6cc4360da8dc87bc8f9cd"; r_cap_id="MTQ2OWM0ZDA1YWQyNGZkOWI3OWExOGU5Zjc4M2VkZGQ=|1490172373|4d2e15dd9f220d7baef32b8bfd8599b0257c6de1"; cap_id="ODE0MDJmNDVmMWQ5NDdjNWIyNDA1MjdlZjNiNmVjYjI=|1490172373|9dc889e95823029a4f014e51b45d90c297ebbe0e"; l_cap_id="N2RlODBjZWY2OTI2NDIwNDkyN2M4NzEyNWUxMGFmNGQ=|1490172373|57a305120322bd5bbc0831b4b9285d93246d77d5"; z_c0=Mi4wQURDQV94SUZZd29BSUVDaE1KcEJDaGNBQUFCaEFsVk4zTWI1V0FBMHp4WHlCa0FaNDB4Y3NtYjNXTjk5SFNpempR|1490172381|e96f3c85cb87dce0b0c14e48044cd766a0577980'
    cookies = get_cookie(cookie_line)
    # 数据库读取的信息
    href = ''
    # 爬评论的尾部
    comments_target = '/comments'
    # 数据库连接
    Mongo_Conn = MongoClient('localhost', 27017)
    conn = Mongo_Conn['crwdata'][scrapy_set.table_name]

    def start_requests(self):
        self.get_href_content()
        yield Request(self.href + self.comments_target, headers=self.headers, cookies=self.cookies)

    def parse(self, response):
        data = json.loads(response.body.decode())
        content = data['data']
        for info in content:
            item = ZhihuItem()
            item['_id'] = info['created_time']
            item['comment'] = info['content']
            item['authorid'] = info['id']
            item['href'] = info['url']
            item['done'] = 1
            yield item

        # 翻页
        next_page = data['paging']
        if not next_page['is_end']:
            url = response.urljoin(next_page['next'])
            yield scrapy.Request(url, headers=self.headers, cookies=self.cookies)
        if next_page['is_end']:
            self.conn.update({'href': self.href}, {'$set': {'done': 1}})
            self.get_href_content()
            yield Request(self.href + self.comments_target, headers=self.headers, cookies=self.cookies)

    def get_href_content(self):
        content = self.conn.find_one({'done': 0})
        self.href = content['href']
