/*
* @authors <Haaris Yahya & Justin>
* @version 1.0 (<12/02/2021>)       */


abstract class chessPiece
{
    private Pieces type;
    private int value;
    private Colours colour;

    void setType (Pieces p )
    {
        type = p;
    }

    Pieces getType ( )
    {
        return type;
    }

    void setValue (int v )
    {
        value = v;
    }

    int getValue ( )
    {
        return value;
    }

    void setColour (Colours c )
    {
        colour = c;
    }

    Colours getColour ( )
    {
        return colour;
    }

    abstract boolean allowedMove ( Locations from, Locations to );

    void openingMoveMade ( boolean twice, int moves ) {}

    boolean openingMove ( )
    { // King, Rook, and Pawn Override. Does not matter for others.
        return false;
    }

    boolean verifyEnPassant ( )
    {
        return false;
    }

    void flipEnPassant ( )
    {

    }

    int getEnPassant ( )
    { //Pawn Overrides, rest don't matter.
        return 0;
    }

    void setOpeningMove ( ) {}
}