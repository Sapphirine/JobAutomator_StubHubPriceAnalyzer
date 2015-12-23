from pymongo import MongoClient
from subprocess import Popen
from bson.objectid import ObjectId
import os
import sys

#setup connection
client = MongoClient()
db = client['JobAutomator']
collection = db['Job']

for Job in collection.find({'_id':ObjectId(sys.argv[1])}):
	num = os.system(Job['Command'])
	collection.update_one({'_id':ObjectId(sys.argv[1])}, {'$set': {'Status': str(num)}})
	
if num == 0:
	for Job in collection.find({'Dependency':sys.argv[1]}):
		Popen("python Executor.py \"" + str(Job['_id']) +"\"",shell=True)
