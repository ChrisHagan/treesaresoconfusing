var context = goog.dom.getElement("board").getContext("2d");

function drawPlayer(player){
    context.fillRect(player.x,player.y,square,square);
}

function drawBoard(){
    context.fillStyle = "gray";
    context.fillRect(0,0,boardSide,boardSide);
    context.fillStyle = "yellow";
    goog.object.forEach(players,drawPlayer);
};

