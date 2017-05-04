# -*- coding: utf-8 -*-

# Define your item pipelines here
#
# Don't forget to add your pipeline to the ITEM_PIPELINES setting
# See: http://doc.scrapy.org/en/latest/topics/item-pipeline.html
from pymongo.mongo_client import MongoClient
from douban.spiders.comment_crawler import CommentSpider


class DoubanPipeline(object):
    def __init__(self):
        self.Mongo_Conn = MongoClient('localhost', 27017)
        self.table_name = CommentSpider.table_name

    def process_item(self, item, spider):
        conn = self.Mongo_Conn['crwdata'][self.table_name]
        conn.insert(dict(item))
        return item
