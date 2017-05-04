import scrapy
from scrapy import Request
from zhihu.items import ZhihuItem
import scrapy.item
import json
import requests
from bs4 import BeautifulSoup
import re
from zhihu.spiders import scrapy_set


# 解析cookies
def get_cookie(cookie_line):
    cookies = {}
    for line in cookie_line.split(';'):
        key, value = line.split('=', 1)
        cookies[key] = value
    return cookies


class AnswersSpider(scrapy.Spider):
    """docstring for Movie250Spider"""
    name = 'zhihu_answers'
    allowed_domains = ["zhihu.com"]
    headers = {
        'User-Agent': "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36"
    }
    cookie_line = '_zap=66d3a24b-7071-4af7-94e3-1d83022d5dea; _za=7f2f1a3e-9058-4e06-ae7c-978387c449bd; d_c0="ACBAoTCaQQqPTsYv8cLwx0JQ_B25CYTiQx8=|1468981187"; _zap=2788fd21-223f-416d-9d7a-beb4d2e447a3; __utma=51854390.145560521.1443286957.1479437018.1479654147.8; __utmz=51854390.1479654147.8.8.utmcsr=baidu|utmccn=(organic)|utmcmd=organic; __utmv=51854390.100--|2=registration_date=20160815=1^3=entry_date=20160815=1; q_c1=e01babd6a64947928f727ab9723f6936|1487687994000|1475600020000; _xsrf=655d508a49d3f010c2072903f1ac8eba; nweb_qa=heifetz; aliyungf_tc=AQAAAPZFBhQdEQ4Aqxo+t2/+bk+u4BTX; capsion_ticket="2|1:0|10:1490172367|14:capsion_ticket|44:Nzc2ZDZmN2E5ZTlhNGRiMzhiNWNjM2ZmYTU5OWVkM2E=|b5eb04e6d2893cd8956c068c9e527169aa6fa82f83e6cc4360da8dc87bc8f9cd"; r_cap_id="MTQ2OWM0ZDA1YWQyNGZkOWI3OWExOGU5Zjc4M2VkZGQ=|1490172373|4d2e15dd9f220d7baef32b8bfd8599b0257c6de1"; cap_id="ODE0MDJmNDVmMWQ5NDdjNWIyNDA1MjdlZjNiNmVjYjI=|1490172373|9dc889e95823029a4f014e51b45d90c297ebbe0e"; l_cap_id="N2RlODBjZWY2OTI2NDIwNDkyN2M4NzEyNWUxMGFmNGQ=|1490172373|57a305120322bd5bbc0831b4b9285d93246d77d5"; z_c0=Mi4wQURDQV94SUZZd29BSUVDaE1KcEJDaGNBQUFCaEFsVk4zTWI1V0FBMHp4WHlCa0FaNDB4Y3NtYjNXTjk5SFNpempR|1490172381|e96f3c85cb87dce0b0c14e48044cd766a0577980'
    cookies = get_cookie(cookie_line)
    # 知乎问题头部
    head_url = 'https://www.zhihu.com/search?type=content&q='
    # 知乎api头部
    api_url = 'https://www.zhihu.com/api/v4/questions/'
    # 爬回答的尾部
    answers_target = '/answers?include=data%5B*%5D.is_normal%2Cis_sticky%2Ccollapsed_by%2Csuggest_edit%2Ccomment_count%2Ccollapsed_counts%2Creviewing_comments_count%2Ccan_comment%2Ccontent%2Ceditable_content%2Cvoteup_count%2Creshipment_settings%2Ccomment_permission%2Cmark_infos%2Ccreated_time%2Cupdated_time%2Crelationship.is_authorized%2Cis_author%2Cvoting%2Cis_thanked%2Cis_nothelp%2Cupvoted_followees%3Bdata%5B*%5D.author.is_blocking%2Cis_blocked%2Cis_followed%2Cvoteup_count%2Cmessage_thread_token%2Cbadge%5B%3F(type%3Dbest_answerer)%5D.topics&offset=3&limit=20&sort_by=default'

    def start_requests(self):
        # 中间插入问题的id号（初步考虑通过request和beautifulsoup获取，然后在用scrapy爬）
        end_url = self.get_pageurl(self.head_url + scrapy_set.target_name) + self.answers_target
        yield Request(self.api_url + end_url, headers=self.headers, cookies=self.cookies)

    def parse(self, response):
        data = json.loads(response.body.decode())
        content = data['data']
        for info in content:
            item = ZhihuItem()
            item['_id'] = info['created_time']
            #去除评论里面的格式标签和特别字符
            comment, number = re.subn(r'<(.+?)>', '', info['content'])
            item['comment'] = comment.replace('▼', '')
            item['authorid'] = info['id']
            item['href'] = info['url']
            item['done'] = 0
            yield item

        # 翻页
        next_page = data['paging']
        if not next_page['is_end']:
            url = response.urljoin(next_page['next'])
            yield scrapy.Request(url, headers=self.headers, cookies=self.cookies)

    def get_pageurl(self, url):
        href = ''
        title = ''
        while '剧' not in title:
            html = requests.get(url, headers=self.headers, cookies=self.cookies).content
            soup = BeautifulSoup(html)
            content = soup.find('div', attrs={'class': 'title'}).find('a')
            title = content.getText()
            href = content['href']
        result, number = re.subn(r'/(.+?)/', '', href)
        return result
