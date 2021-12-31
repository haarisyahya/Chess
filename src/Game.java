/*
* @authors <Haaris Yahya & Justin>
* @version 1.0 (<12/02/2021>)       */


public class Game
{
    private chessBoard b;
    private AI whitePiece, blackPiece;

    Game(AI whitePiece, AI blackPiece)
    {
        this.whitePiece = whitePiece;
        this.blackPiece = blackPiece;
        b = new chessBoard();
        b.initBoard(); 
    }

    chessBoard getBoard ( )
    {
        return b;
    }
}
