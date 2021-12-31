class Rook extends chessPiece
{
    private boolean openingMove;

    Rook ( Colours c )
    {
        openingMove = true;
        setValue(5);
        setType(Pieces.Rook);
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
        return from.getCol() == to.getCol() || from.getRow() == to.getRow();
    }
}