function launchMissile(mouseX,mouseY){
    console.log("launchMissile",mouseX,mouseY);
    var x = Math.floor(mouseX / square);
    var y = Math.floor(mouseY / square);
    new goog.net.XhrIo().send(goog.string.format("/launch?x=%s&y=%s",x,y),"POST");
}
goog.events.listen(goog.dom.getElement("board"),
		   goog.events.EventType.CLICK,
		   function(e){
		       launchMissile(e.offsetX,e.offsetY);
		   });
goog.events.listen(goog.dom.getElement("tick"),
		   goog.events.EventType.CLICK,
		   function(e){
		       new goog.net.XhrIo().send("/tick");
		   });
