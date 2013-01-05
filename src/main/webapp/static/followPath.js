function followPath(player){
    var path = player.path;
    if(path.length > 0){
	var step = path.shift();
	player.x = step.x * square;
	player.y = step.y * square;
	setTimeout(goog.bind(followPath,player,player), 1000 / (player.speed * 2));
    }
}
