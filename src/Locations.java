public class Locations
{
    private int col, row;

    Locations ( )
    {

    }

    Locations ( int c, int r )
    {
        col = c;
        row = r;
    }

    void setCol ( int c )
    {
        col = c;
    }

    void setRow ( int r )
    {
        row = r;
    }

    int getCol ( )
    {
        return col;
    }

    int getRow ( )
    {
        return row;
    }
}