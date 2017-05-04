# -*- coding: utf-8 -*-
from pymongo.mongo_client import MongoClient
# Define your item pipelines here
#
# Don't forget to add your pipeline to the ITEM_PIPELINES setting
# See: http://doc.scrapy.org/en/latest/topics/item-pipeline.html


class VideoCommentPipeline(object):
    def __init__(self):
        self.Mongo_Conn = MongoClient('localhost', 27017)

    def process_item(self, item, spider):
        #手动修改，先上视频网站上查是否有该网络剧的回答，表名统一为V开头，例法医秦明的视频数据表名为V1001
        conn = self.Mongo_Conn['crwdata']['V1001']
        conn.insert(dict(item))
        return item
