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
socket.onmessage = handler("msg");
socket.onerror = handler("error");
socket.onclose = handler("close");

