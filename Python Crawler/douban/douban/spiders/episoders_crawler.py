import scrapy
from scrapy import Request
from douban.items import DoubanItem
import scrapy.item
import requests
from bs4 import BeautifulSoup


class EpisodersSpider(scrapy.Spider):
    """docstring for Movie250Spider"""
    name = 'episodes'
    allowed_domains = ["douban.com"]
    headers = {
        'User-Agent': "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36",
    }
    episoders_id = 1001
    index = 0
    href = []
    table_name = 'name_list'

    def start_requests(self):
        print('hello world!')
        # self.getgenre('https://movie.douban.com/tag/%E7%BD%91%E7%BB%9C%E5%89%A7')
        # yield Request(self.href[self.index], headers=self.headers)

    def parse(self, response):
        for info in response.xpath('//div[@id="info"]'):
            print(info)
            # 除去没有评论和评论少的这些不值得获取的网络剧
            # status = info.xpath(
            #     'tr[@class="item"]/td[@valign="top"]/div[@class="pl2"]/div[@class="star clearfix"]/span[@class="pl"]/text()')[
            #     0].extract()
            # if status == '(尚未上映)' or status == '(评价人数不足)':
            #     continue
            #
            # item = DoubanItem()
            # item['_id'] = self.episoders_id
            # item['name'] = info.xpath('tr[@class="item"]/td[@width="100"]/a[@class="nbg"]/@title')[0].extract()
            # self.href = info.xpath('tr[@class="item"]/td[@width="100"]/a[@class="nbg"]/@href')[0].extract()
            # item['href'] = self.href
            # item['done'] = 0
            # self.episoders_id += 1
            # yield item

        # 翻页
        # if next_page:
        #     url = response.urljoin(next_page[0].extract())
        #     yield scrapy.Request(url, headers=self.headers)

    def getgenre(self, url):
        html = requests.get(url, headers=self.headers).content
        soup = BeautifulSoup(html)
        episoders_list_soup = soup.find('div', attrs={'class': 'article'}).find('div', attrs={'class': ''})
        for episoders_li in episoders_list_soup.find_all('table', attrs={'width': '100%', 'class': ''}):
            # 判断是否还没上映的或评价人数太少，不能给出客观评价的，这些数据不抓取
            status = episoders_li.find('span', attrs={'class': 'pl'}).getText().strip()
            if (status == '(尚未上映)' or status == '(评价人数不足)'):
                continue
            detail = episoders_li.find('td', attrs={'width': '100'})
            episoders_listdata = detail.find('a', attrs={'class': 'nbg'})
            self.href.append(episoders_listdata['href'])
        # 跳下一页
        next_page = soup.find('span', attrs={'class': 'next'}).find('a')
        if next_page:
            self.getgenre(next_page['href'])
