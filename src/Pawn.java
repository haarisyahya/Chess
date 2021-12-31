class Pawn extends chessPiece
{
    private boolean openingMove;
    private boolean verifyEnPassant;
    private int enPassant;

    Pawn ( Colours c )
    {
        openingMove = true;
        verifyEnPassant = false;
        setValue(1);
        setType(Pieces.Pawn);
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
        if (twice)
        {
            verifyEnPassant = true;
            enPassant = moves;
        }
    }

    @Override
    void setOpeningMove ( )
    {
        openingMove = true;
    }

    @Override
    boolean verifyEnPassant ( )
    {
        return verifyEnPassant;
    }

    @Override
    void flipEnPassant ( )
    {
        verifyEnPassant = false;
    }

    @Override
    int getEnPassant ( )
    {
        return enPassant;
    }

    boolean allowedMove ( Locations from, Locations to )
    {
        switch (getColour())
        {
            case Black:

                if (from.getCol() == to.getCol() && from.getRow() - to.getRow() == -1)
                { // one move down
                    return true;
                }
                if (from.getCol() - to.getCol() == -1 && from.getRow() - to.getRow() == -1)
                { // diagonal moves
                    return true;
                }
                if (from.getCol() - to.getCol() == 1 && from.getRow() - to.getRow() == -1)
                {
                    return true;
                }
                if (openingMove())
                {// first move can move twice.
                    return from.getCol() == to.getCol() && from.getRow() - to.getRow() == -2;
                }
                return false;
            case White:
                if (from.getCol() == to.getCol() && from.getRow() - to.getRow() == 1)
                { // one move up
                    return true;
                }
                if (from.getCol() - to.getCol() == -1 && from.getRow() - to.getRow() == 1)
                { // diagonal moves
                    return true;
                }
                if (from.getCol() - to.getCol() == 1 && from.getRow() - to.getRow() == 1)
                {
                    return true;
                }
                if (openingMove()) {// first move can move twice.
                    return from.getCol() == to.getCol() && from.getRow() - to.getRow() == 2;
                }
                return false;
        }
        return false;
    }
}