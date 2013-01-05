console.log("Establishing channel");
console.log("Identity",channelIdentity.token);
channel = new goog.appengine.Channel(channelIdentity.token);

function handler(prefix){
    return function(args){
	console.log(prefix,args);
    };
}

socket = channel.open();
socket.onopen = handler("opened");
socket.onmessage = function(message){
    console.log("Msg",message);
    var m = goog.json.parse(message.data);
    switch(m.type){
    case "food":
	food = m;
	break;
    case "player":
	console.log("player",m);
	players[m.identity] = m;
	followPath(players[m.identity]);
	break;
    case "scenery":
	scenery = m.points;
    }
}
socket.onerror = handler("error");
socket.onclose = handler("close");
