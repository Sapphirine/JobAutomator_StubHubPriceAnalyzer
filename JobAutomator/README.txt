Title: Job Automator
Author: Jake G. Wood
Date: 9/12/15

Table of Contents:
-----------------
1) Overview
2) Requirements
3) Running
4) Improvements

1) Overview
-----------
This project is a command line level job automator with a web based front end. It has the common process automator features of being able to run a job at a specific (1-minute resolution) or by dependency on another job.

The stack is:
html w/ socket.io - Front end
nodejs - server
mongodb - database
python - backend scripts to run jobs in db

2) Requirements
---------------
The user will need to have python (I built this in 2.7.6) with pymongo, node with socket.io, express and mongodb libraries and mongodb (I'm using 3.0.6).

3) Running
----------
By default, the server connects to mongodb on the default (27071) port, and listens for http requests at 28081. The backend python job execution scripts and the front end are totally independent, so feel free to run either with or without the other.

4) Improvements
---------------
I built this to suit my needs, so there are definitely some things to consider before it's applied more generally, such as:
	-There is no sanitization of inputs, neither at front end input nor at input into database
	-The front end is obviously not visually appealing whatsoever
	-I haven't included the ability to repeat jobs by intervals (every 15 minutes, etc.)
