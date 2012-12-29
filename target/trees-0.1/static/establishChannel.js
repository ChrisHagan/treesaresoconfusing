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
    case "player":
	players[m.identity] = m;
	break;
    case "path":
	console.log("path",m);
	players[m.identity].path = m.vertices;
	break;
    case "scenery":
	scenery[m.identity] = m;
    }
}
socket.onerror = handler("error");
socket.onclose = handler("close");

