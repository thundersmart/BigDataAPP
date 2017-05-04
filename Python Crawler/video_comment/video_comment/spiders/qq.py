import scrapy
from scrapy import Request
from video_comment.items import VideoCommentItem
import scrapy.item
import json
from video_comment.spiders import scrapy_set


class IqiyiSpider(scrapy.Spider):
    """docstring for Movie250Spider"""
    name = 'qq'
    allowed_domains = ["qq.com"]
    headers = {
        'User-Agent': "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36"
    }
    head_url = 'http://coral.qq.com/article/'
    commentid = '/comment?commentid='
    # 当前页数
    comment_no = 0

    def start_requests(self):
        href = self.head_url + scrapy_set.videoid + self.commentid + str(self.comment_no)
        yield Request(href, headers=self.headers, dont_filter=True)

    def parse(self, response):
        data = json.loads(response.body.decode())
        content = data['data']
        for info in content['commentid']:
            item = VideoCommentItem()
            item['_id'] = info['id']
            item['comment'] = info['content']
            item['authorid'] = info['userid']
            item['done'] = 1
            yield item

        # 翻页
        if content['last']:
            self.comment_no = content['last']
            href = self.head_url + scrapy_set.videoid + self.commentid + str(self.comment_no)
            url = response.urljoin(href)
            yield scrapy.Request(url, headers=self.headers, dont_filter=True)
