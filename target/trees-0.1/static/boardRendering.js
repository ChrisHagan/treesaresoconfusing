var context = goog.dom.getElement("board").getContext("2d");

function drawPlayer(player){
    context.fillRect(player.x,player.y,square,square);
}

function drawScenery(sceneryItem){
    context.fillRect(sceneryItem.x * square,sceneryItem.y * square,square,square);
}
function drawFood(f){
    context.fillRect(f.x * square, f.y * square,square,square);
}

function drawBoard(){
    context.fillStyle = "gray";
    context.fillRect(0,0,boardSide,boardSide);
    context.fillStyle = "blue";
    goog.object.forEach(scenery,drawScenery);
    context.fillStyle = "red";
    drawFood(food);
    context.fillStyle = "yellow";
    goog.object.forEach(players,drawPlayer);
};

