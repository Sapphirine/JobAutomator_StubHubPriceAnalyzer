var connectionString = 'mongodb://localhost:27017/JobAutomator';
var activeCollection = 'Job';
var mc = require('mongodb').MongoClient;
var ops = require('mongodb');

module.exports = 

{

search: function(io,searchTerm)
        {
	//console.log('search() called');
	//console.log(searchTerm);
	var key = searchTerm.key;
	
	var query = {};
	if (key != '_id')
		query[key] = {$regex:".*" + searchTerm.value + ".*"};
	else
		try
			{
			query[key] = ops.ObjectID(searchTerm.value);
			}
		catch(err)
			{
			io.emit('idError');
			return;
			}
		
	mc.connect(connectionString, function(err,db)
                {
                if(err)
                        {
                        throw err;
                        }
                else
                        {
                        db.collection(activeCollection, function(err,collection)
                                {
                                if (err)
                                        {
                                        throw err;
                                        }
                                else
                                        {
					
					
                                        collection.find(query).toArray(function(err,items)
                                                {
                                                if(err)
                                                        {
                                                        throw err;
                                                        }
                                                else
                                                        {
                                                        items.forEach(function(item)
                                                                {
                            var timeDisplay ='';
                            if(!item.hasOwnProperty('Dependency'))
                                {
                                var dayDisplay = '';
                                switch(item.d)
                                	{
                                	case 41:
                                		dayDisplay = 'Monday';
                                		break;
                                	case 42:
                                		dayDisplay = 'Tuesday';
                                		break;
                                	case 43:
                                		dayDisplay = 'Wednesday';
                                		break;
                                	case 44:
                                		dayDisplay = 'Thursday';
                                		break;
                                	case 45:
                                		dayDisplay = 'Friday';
                                		break;
                                	case 46:
                                		dayDisplay = 'Saturday';
                                		break;
                                	case 47:
                                		dayDisplay = 'Sunday';
                                		break;
                                	case -1:
                                		dayDisplay = 'All';
                                		break;
                                	default:
                                		dayDisplay = item.d
                                	}
                                	
                               	var monthDisplay = '';
                                switch(item.M)
                                	{
                                	case 1:
                                		monthDisplay = 'January';
                                		break;
                                	case 2:
                                		monthDisplay = 'February';
                                		break;
                                	case 3:
                                		monthDisplay = 'March';
                                		break;
                                	case 4:
                                		monthDisplay = 'April';
                                		break;
                                	case 5:
                                		monthDisplay = 'May';
                                		break;
                                	case 6:
                                		monthDisplay = 'June';
                                		break;
                                	case 7:
                                		monthDisplay = 'July';
                                		break;
                                	case 8:
                                		monthDisplay = 'August';
                                		break;
                                	case 9:
                                		monthDisplay = 'September';
                                		break;
                                	case 10:
                                		monthDisplay = 'October';
                                		break;
                                	case 11:
                                		monthDisplay = 'November';
                                		break;
                                	case 12:
                                		monthDisplay = 'December';
                                		break;
                                	case -1:
                                		monthDisplay = 'All';
                                		break;
                                	default:
                                		monthDisplay = item.M
                                	}
                            		
                            	var yearDisplay = item.y;
                            	if(yearDisplay == -1)
                            		yearDisplay = 'All';
                            		
                            	var hourDisplay = item.h;
                            	if(hourDisplay == -1)
                            		hourDisplay = 'All';
                            		
                            	var minuteDisplay = item.m;
                            	if(item.m == -1)
                            		minuteDisplay = 'All';
                            	
                            	timeDisplay = 	"Year: "
												+yearDisplay
												+"<br>Month: "
												+monthDisplay
												+"<br>Day: "
												+dayDisplay
												+"<br>Hour: "
												+hourDisplay
												+"<br>Minute: "
												+minuteDisplay;
								}
							else
								{
								timeDisplay = "Dependency: " + item.Dependency;
								}
                            					if (item.Status == 0)
									var status = "Success";
								else if(item.Status != "N/A")
									var status = "Failure";
								else
									var status = "N/A";
								msg = "<tr><td>"
								+ item.Name
								+"</td><td>"
								+item.Command
								+"</td><td>"
								+timeDisplay
								+"</td><td>"
								+item._id
								+"</td><td>"
								+status
								+"</td><td id=\""
								+item._id
								+"\" class=\"delete\">Delete</td></tr>";
								io.emit('newResult',msg);
								//console.log('\n\n FROM DBOps:\n\n');
								//console.log(io);
								});
								db.close();
                                                        }
                                                });
                                        }
                                });
                        }
                });
        },
	
delete: function(io,id)
	{
        mc.connect(connectionString, function(err,db)
                {
                if(err)
                        {
                        throw err;
                        }
                else
                        {
                        db.collection(activeCollection, function(err,collection)
                                {
                                if (err)
                                        {
                                        throw err;
                                        }
                                else
                                        {
                                        collection.remove({"_id":ops.ObjectID(id)});
                                        }
                                });
                        }
                db.close();
                });

	},

add: function(io,newJob)
        {
        
        mc.connect(connectionString, function(err,db)
                {
                if(err)
                        {
                        throw err;
                        }
                else
                        {
                        db.collection(activeCollection, function(err,collection)
                                {
                                if (err)
                                        {
                                        throw err;
                                        }
                                else if(newJob.byDep == 0)
                                        {
                                        collection.insert({"Name":newJob.Name,"Command":newJob.Command,"y":parseInt(newJob.Year),"M":parseInt(newJob.Month),"d":parseInt(newJob.Day),"h":parseInt(newJob.Hour),"m":parseInt(newJob.Minute),"Status":"N/A"});
                                        }
                                else
                                		{
                                		collection.insert({"Name":newJob.Name,"Command":newJob.Command,"Dependency":newJob.Dependency,"Status":"N/A"});
                                		}
                                });
                        }
                db.close();
                });

        }
};
