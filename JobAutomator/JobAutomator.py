from pymongo import MongoClient
from datetime import datetime
import schedule
import time
from subprocess import Popen

#setup connection
client = MongoClient()
db = client['JobAutomator']
collection = db['Job']

#global minute
minute = datetime.now().minute

# Schedule library drifts if I use every().minute (adds a few seconds), 
# so instead I used every().second, then check to see if the minute has changed
# See call to schedule below
def update():
	global minute
	if minute == datetime.now().minute:
		return
	else:
		minute = datetime.now().minute
		findJobs()

def findJobs():
	weekday = datetime.now().weekday() + 40
	for Job in collection.find({'$and' : [
							{'$or' : [{'y':datetime.now().year},{'y':-1}]},
							{'$or' : [{'M':datetime.now().month},{'M':-1}]},
							{'$or' : [{'d':datetime.now().day},{'d':-1},{'d':weekday}]},
							{'$or' : [{'h':datetime.now().hour},{'h':-1}]},
							{'$or' : [{'m':datetime.now().minute},{'m':-1}]}
							]}):
		Popen("python Executor.py \"" + str(Job['_id']) +"\"",shell=True)
		print(str(Job['_id']))
		
		
#call update, which calls findJob every minute
schedule.every().second.do(update)

while 1:
	schedule.run_pending()
	time.sleep(1)
