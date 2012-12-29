package game;

import ai.PlayBoard;

public class Map{
    private PlayBoard board;

    public Map(int width, int height){
	board = new PlayBoard(width,height);
    }
}
