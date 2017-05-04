# -*- coding: utf-8 -*-

# Define here the models for your scraped items
#
# See documentation in:
# http://doc.scrapy.org/en/latest/topics/items.html

import scrapy


class DoubanItem(scrapy.Item):
    # define the fields for your item here like:
    # name = scrapy.Field()
    _id = scrapy.Field()
    name = scrapy.Field()
    href = scrapy.Field()
    #设一个值确定该链接下的评论是否爬过 0-代表没有爬过 1-代表爬过
    done = scrapy.Field()
    #评论item
    rating = scrapy.Field()
    comment = scrapy.Field()
    update_time = scrapy.Field()
