import scrapy
from scrapy import Request
from video_comment.items import VideoCommentItem
import scrapy.item
import json
from video_comment.spiders import scrapy_set


class SouhuSpider(scrapy.Spider):
    """docstring for Movie250Spider"""
    name = 'souhu'
    allowed_domains = ["souhu.com"]
    headers = {
        'User-Agent': "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36"
    }
    head_url = 'http://changyan.sohu.com/api/2/topic/comments?'
    page = 'page_no='
    # 总页数
    page_sum = 0
    # 当前页数
    page_no = 1

    def start_requests(self):
        href = self.head_url + self.page + str(self.page_no) + '&' + scrapy_set.client_id + '&' + scrapy_set.topic_id
        yield Request(href, headers=self.headers, dont_filter=True)

    def parse(self, response):
        data = json.loads(response.body.decode())
        content = data['comments']
        for info in content:
            item = VideoCommentItem()
            item['_id'] = info['create_time']
            item['comment'] = info['content']
            item['authorid'] = info['user_id']
            item['done'] = 1
            yield item
        self.page_no += 1

        # 翻页
        if self.page_no == 2:  # 首次取页数
            self.page_sum = int(int(data['cmt_sum']) / 31)
        if self.page_no < self.page_sum:
            href = self.head_url + self.page + str(
                self.page_no) + '&' + scrapy_set.client_id + '&' + scrapy_set.topic_id
            url = response.urljoin(href)
            yield scrapy.Request(url, headers=self.headers, dont_filter=True)
