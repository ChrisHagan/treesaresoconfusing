goog.events.listen(goog.dom.getElement("tick"),
		   goog.events.EventType.CLICK,
		   function(e){
		       new goog.net.XhrIo().send("/tick");
		   });
