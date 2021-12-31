class Queen extends chessPiece
{
    Queen ( Colours c )
    {
        setValue(9);
        setType(Pieces.Queen);
        setColour(c);
    }


    boolean allowedMove ( Locations from, Locations to )
    {
        return from.getCol() - to.getCol() == 0 || from.getRow() - to.getRow() == 0 || Math.abs(from.getCol() - to.getCol()) == Math.abs(from.getRow() - to.getRow());
    }
}