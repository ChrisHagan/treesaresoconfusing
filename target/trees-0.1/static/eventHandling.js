var square = 20
function launchMissile(mouseX,mouseY){
    console.log("launchMissile",mouseX,mouseY);
    var x = mouseX / square;
    var y = mouseY / square;
    new goog.net.XhrIo().send(goog.string.format("/launch?x=%s&y=%s",x,y),"POST");
}
goog.events.listen(goog.dom.getElement("board"),
		   goog.events.EventType.CLICK,
		   function(e){
		       launchMissile(e.offsetX,e.offsetY);
		   });
