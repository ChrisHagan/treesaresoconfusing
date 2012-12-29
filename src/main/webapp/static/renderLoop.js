function renderLoop(){
    requestAnimationFrame(renderLoop);
    goog.object.forEach(players,function(player){
	if(player.path){
	    var step = player.path.pop();
	    if(player.path.length == 0){
		delete player.path;
	    }
	    player.x = step.x * square;
	    player.y = step.y * square;
	}
    });
    drawBoard();
}

requestAnimationFrame(renderLoop);

