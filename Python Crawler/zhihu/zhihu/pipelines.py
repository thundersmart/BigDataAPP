# -*- coding: utf-8 -*-
from pymongo.mongo_client import MongoClient
from zhihu.spiders import scrapy_set

# Define your item pipelines here
#
# Don't forget to add your pipeline to the ITEM_PIPELINES setting
# See: http://doc.scrapy.org/en/latest/topics/item-pipeline.html


class ZhihuPipeline(object):
    def __init__(self):
        self.Mongo_Conn = MongoClient('localhost', 27017)

    def process_item(self, item, spider):
        conn = self.Mongo_Conn['crwdata'][scrapy_set.table_name]
        conn.insert(dict(item))
        return item
