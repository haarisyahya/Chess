class Knight extends chessPiece
{
    Knight ( Colours c )
    {
        setValue(3);
        setType(Pieces.Knight);
        setColour(c);
    }

    boolean allowedMove ( Locations from, Locations to )
    {
        return (Math.abs(from.getCol() - to.getCol()) == 2 && Math.abs(from.getRow() - to.getRow()) == 1) || (Math.abs(from.getCol() - to.getCol()) == 1 && Math.abs(from.getRow() - to.getRow()) == 2);
    }
}