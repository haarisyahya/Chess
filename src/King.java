/*
* @authors <Haaris Yahya & Justin>
* @version 1.0 (<12/02/2021>)       */


class King extends chessPiece
{
    private boolean openingMove;

    King ( Colours c )
    {
        openingMove = true;
        setValue(200);
        setType(Pieces.King);
        setColour(c);
    }

    @Override
    boolean openingMove ( )
    {
        return openingMove;
    }

    @Override
    void openingMoveMade ( boolean twice,  int moves )
    {
        openingMove = false;
    }

    @Override
    void setOpeningMove ( )
    {
        openingMove = true;
    }

    boolean allowedMove ( Locations from, Locations to )
    {
        if (openingMove())
        {
            if (from.getRow() == to.getRow() && Math.abs(from.getCol() - to.getCol()) == 2)
            {
                    return true;
            }
        }
        return (Math.abs(from.getCol() - to.getCol()) == 1) && (Math.abs(from.getRow() - to.getRow()) == 1) || (Math.abs(from.getCol() - to.getCol()) == 1) && (from.getRow() - to.getRow() == 0) || (from.getCol() - to.getCol() == 0) && (Math.abs(from.getRow() - to.getRow()) == 1);
    }
}