var express = require('express');
var app = express();
var http = require('http').Server(app);
var io = require('socket.io')(http);
var jadp = require('./JADBOperations');

app.use(express.static('.'));

app.get('/',function(req,res)
	{
	res.redirect('JobAutomatorInterface.html');
	});

app.get('/JobAutomatorInterface.html',function(req,res)
    {
    res.redirect('JobAutomatorInterface.html');
	});

io.on('connection',function(socket)
	{
	console.log('connected');
	
	socket.on('deleteJob',function(id)
		{
		//console.log('Delete clicked');
		jadp.delete(io,id);
		});
	
	socket.on('addClick',function(newJob)
		{
		//console.log('Add clicked');
		jadp.add(io,newJob);
		});
	
	socket.on('searchClick',function(searchTerm)
        {
        //console.log('Search clicked');
		jadp.search(io,searchTerm);
        });
	});


http.listen(28081,function()
	{
	console.log('Listening');
	});


