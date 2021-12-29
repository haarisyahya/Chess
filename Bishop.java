/*
* @authors <Haaris Yahya & Justin>
* @version 1.0 (<12/02/2021>)       */

class Bishop extends chessPiece
{
    Bishop ( Colours c )
    {
        setValue(3);
        setType(Pieces.Bishop);
        setColour(c);
    }

    boolean allowedMove ( Locations from, Locations to )
    {
        return Math.abs(from.getCol() - to.getCol()) == Math.abs(from.getRow() - to.getRow());
    }
}