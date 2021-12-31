
/*
* @authors <Haaris Yahya & Justin>
* @version 1.0 (<12/02/2021>)       */


class Block
{
    private chessPiece p;

    Block (  )
    {
        p = null;
    }

    // Returns true if a piece is occupying the square, false otherwise.
    boolean hasPiece ( )
    {
        return p != null;
    }

    // Returns the piece that is occupying the square if it exists, null otherwise.
    chessPiece getPiece ( )
    {
        if (hasPiece())
        {
            return p;
        }
        else
        {
            return null;
        }
    }

    chessPiece killPiece ( )
    {
        chessPiece temp;
        if (hasPiece())
        {
            temp = p;
            p = null;
            return temp;
        }
        else
        {
            return null;
        }
    }

    // Occupies the square with the passed piece if previously unoccupied.
    void setPiece (chessPiece p)
    {
        if (!hasPiece())
        {
            this.p = p;
        }
    }
}