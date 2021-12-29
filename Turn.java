/*
* @authors <Haaris Yahya & Justin>
* @version 1.0 (<12/02/2021>)       */



class Turn
{
    private Locations from, to;

    Turn ( Locations f, Locations t )
    {
        from = f;
        to = t;
    }
    Locations getFrom ( )
    {
        return from;
    }

    Locations getTo ( )
    {
        return to;
    }
}
