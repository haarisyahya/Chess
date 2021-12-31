import java.util.LinkedList;
import java.util.List;


/* This class includes the code for the chess board, enpassant, castling, etc.
* @authors <Haaris Yahya & Justin>
* @version 1.0 (<12/02/2021>)       */

class chessBoard
{

    private Block[][] board;

    chessBoard ( )
    {
        board = new Block[8][8];
    }

    void emptyBoard ( )
    {

        for (int i = 0 ; i < 8 ; i ++)
        {
            for (int j = 0 ; j < 8 ; j++)
            {
                board[i][j] = new Block();
            }
        }
    }

    // initializes the board
    void initBoard ( )
    {
        for (int i = 0 ; i < 8 ; i ++)
        {
            for (int j = 0 ; j < 8 ; j++)
            {
                board[i][j] = new Block();
            }
        }
        board[0][0].setPiece(new Rook(Colours.Black));
        board[1][0].setPiece(new Knight(Colours.Black));
        board[2][0].setPiece(new Bishop(Colours.Black));
        board[3][0].setPiece(new Queen(Colours.Black));
        board[4][0].setPiece(new King(Colours.Black));
        board[5][0].setPiece(new Bishop(Colours.Black));
        board[6][0].setPiece(new Knight(Colours.Black));
        board[7][0].setPiece(new Rook(Colours.Black));
        board[0][1].setPiece(new Pawn(Colours.Black));
        board[1][1].setPiece(new Pawn(Colours.Black));
        board[2][1].setPiece(new Pawn(Colours.Black));
        board[3][1].setPiece(new Pawn(Colours.Black));
        board[4][1].setPiece(new Pawn(Colours.Black));
        board[5][1].setPiece(new Pawn(Colours.Black));
        board[6][1].setPiece(new Pawn(Colours.Black));
        board[7][1].setPiece(new Pawn(Colours.Black));
        board[0][6].setPiece(new Pawn(Colours.White));
        board[1][6].setPiece(new Pawn(Colours.White));
        board[2][6].setPiece(new Pawn(Colours.White));
        board[3][6].setPiece(new Pawn(Colours.White));
        board[4][6].setPiece(new Pawn(Colours.White));
        board[5][6].setPiece(new Pawn(Colours.White));
        board[6][6].setPiece(new Pawn(Colours.White));
        board[7][6].setPiece(new Pawn(Colours.White));
        board[0][7].setPiece(new Rook(Colours.White));
        board[1][7].setPiece(new Knight(Colours.White));
        board[2][7].setPiece(new Bishop(Colours.White));
        board[3][7].setPiece(new Queen(Colours.White));
        board[4][7].setPiece(new King(Colours.White));
        board[5][7].setPiece(new Bishop(Colours.White));
        board[6][7].setPiece(new Knight(Colours.White));
        board[7][7].setPiece(new Rook(Colours.White));
    }

    //performs the passed move and any special moves that may have occurred.
    chessPiece makeMove ( Turn m, int moves )
    {
        Locations from = m.getFrom();
        Locations to = m.getTo();
        Block fromSquare = board[from.getCol()][from.getRow()];
        Block toSquare = board[to.getCol()][to.getRow()];
        chessPiece fromPiece = fromSquare.getPiece();
        chessPiece toPiece = null;

        if (!hasPiece(from))
        {
            return null;
        }

        switch (fromPiece.getType())
        {
            case Pawn:
                if (fromPiece.openingMove())
                {
                    fromPiece.openingMoveMade(Math.abs(from.getRow() - to.getRow()) == 2, moves);
                }
                break;
            case Rook:
            case King:
                if (fromPiece.openingMove())
                {
                    fromPiece.openingMoveMade(false, moves);
                }
                break;
        }
        if (toSquare.hasPiece())
        {
            toPiece = toSquare.killPiece();
        }
        toSquare.setPiece(fromSquare.killPiece());

        //Castling
        if (verifyType(m.getTo()) == Pieces.King && Math.abs(m.getTo().getCol()-m.getFrom().getCol()) == 2)
        {
            if (m.getTo().getCol() == 6)
            {
                movePiece(new Locations(7, m.getTo().getRow()), new Locations(5, m.getTo().getRow()));
            }
            else if (m.getTo().getCol() == 2)
            {
                movePiece(new Locations(0, m.getTo().getRow()), new Locations(3, m.getTo().getRow()));
            }
        }

        // En passant
        if (verifyType(m.getTo()) == Pieces.Pawn && m.getTo().getRow() != m.getFrom().getRow() && m.getTo().getCol() != m.getFrom().getCol())
        {
            switch (verifyColour(m.getTo())) // pawn that attacked
            {
                case White:
                    if (m.getTo().getRow() == 2 && verifyEnPassant(new Locations(m.getTo().getCol(), m.getFrom().getRow())))
                    {
                        killPiece(new Locations(m.getTo().getCol(), m.getFrom().getRow()));
                    }
                    break;
                case Black:
                    if (m.getTo().getRow() == 5 && verifyEnPassant(new Locations(m.getTo().getCol(), m.getFrom().getRow())))
                    {
                        killPiece(new Locations(m.getTo().getCol(), m.getFrom().getRow()));
                    }
                    break;
            }
        }
        // remove En passant if not taken
        removeEnPassant(moves);
        return toPiece;
    }

    // undoes the passed move and any special moves
    void retractMove ( Turn m, chessPiece p, boolean wasFirst, boolean wasEnPassant, boolean wasPromotion )
    {
        movePiece(m.getTo(), m.getFrom());
        if (p != null)
        {
            setPiece(m.getTo(), p);
        }
        if (wasFirst && hasPiece(m.getFrom()))
        {
            board[m.getFrom().getCol()][m.getFrom().getRow()].getPiece().setOpeningMove();
            board[m.getFrom().getCol()][m.getFrom().getRow()].getPiece().flipEnPassant();
        }

        // move castle back if castling
        if (verifyType(m.getFrom()) == Pieces.King && board[m.getFrom().getCol()][m.getFrom().getRow()].getPiece().openingMove() && m.getFrom().getCol() == 4)
        { // put castle back
            if (m.getFrom().getCol() - m.getTo().getCol() == -2 &&
                    hasPiece(new Locations(m.getTo().getCol() - 1, m.getTo().getRow())) && verifyType(new Locations(m.getTo().getCol() - 1, m.getTo().getRow())) == Pieces.Rook && board[m.getTo().getCol() - 1][m.getFrom().getRow()].getPiece().openingMove())
            {
                setPiece(new Locations(m.getTo().getCol() + 1, m.getTo().getRow()), killPiece(new Locations(m.getTo().getCol() - 1, m.getTo().getRow())));
            }
            else if (m.getFrom().getCol() - m.getTo().getCol() == 2 && hasPiece(new Locations(m.getTo().getCol() + 1, m.getTo().getRow())) && verifyType(new Locations(m.getTo().getCol() + 1, m.getTo().getRow())) == Pieces.Rook && board[m.getTo().getCol() + 1][m.getFrom().getRow()].getPiece().openingMove())
            {
                setPiece(new Locations(m.getTo().getCol() - 2, m.getTo().getRow()), killPiece(new Locations(m.getTo().getCol() + 1, m.getTo().getRow())));
            }
        }

        //Replace pawn if En Passant
        if (wasEnPassant)
        {
            setPiece(new Locations(m.getTo().getCol(), m.getFrom().getRow()), new Pawn(board[m.getFrom().getCol()][m.getFrom().getRow()].getPiece().getColour()));
        }
        // replace pawn if promo
        if (wasPromotion)
        {
            switchPiece(m.getFrom(), Pieces.Pawn);
        }
    }
    // checks the move against the piece on that square
    boolean allowedMove ( Locations from, Locations to)
    {
        return board[from.getCol()][from.getRow()].hasPiece() && board[from.getCol()][from.getRow()].getPiece().allowedMove(from, to);
    }

    // check the move against the other pieces on the board
    boolean checkRoute ( Locations from, Locations to, Pieces piece )
    {
        int fromColumn = from.getCol(), fromRow = from.getRow();
        int toColumn = to.getCol(), toRow = to.getRow();

        switch (piece)
        {
            case Pawn:
                if (fromColumn == toColumn)
                {
                    if (fromRow - toRow == -2) // First move, moving 2 down
                    {
                        return (!board[fromColumn][fromRow + 1].hasPiece() && !board[toColumn][toRow].hasPiece());
                    }
                    else if (fromRow - toRow == 2) // first move, moving 2 up
                    {
                        return (!board[fromColumn][fromRow - 1].hasPiece() && !board[toColumn][toRow].hasPiece());
                    }
                    else if (fromRow - toRow == -1 || fromRow - toRow == 1) // moving 1 up or down
                    {
                        return !board[toColumn][toRow].hasPiece(); // ensure no piece
                    }
                } else if (Math.abs(fromColumn - toColumn) == 1 && Math.abs(fromRow - toRow) == 1) // attempting to take
                {
                    return board[toColumn][toRow].hasPiece() || board[toColumn][fromRow].hasPiece() && board[toColumn][fromRow].getPiece().verifyEnPassant();
                }
                return false;
            case Queen: // Queen Moves are combination of rook and bishop paths
            case Bishop:
                if (fromColumn < toColumn && fromRow < toRow) // moving down, right
                {
                    for (int i = fromColumn + 1, j = fromRow + 1 ; i < toColumn && j < toRow ; i++, j++)
                    {
                        if (board[i][j].hasPiece()) return false;
                    }
                }
                else if (fromColumn < toColumn && fromRow > toRow) // moving up, right
                {
                    for (int i = fromColumn + 1, j = fromRow - 1 ; i < toColumn && j > toRow ; i++, j--)
                    {
                        if (board[i][j].hasPiece()) return false;
                    }
                }
                else if (fromColumn > toColumn && fromRow < toRow) // moving down, left
                {
                    for (int i = fromColumn - 1, j = fromRow + 1 ; i > toColumn && j < toRow ; i--, j++)
                    {
                        if (board[i][j].hasPiece()) return false;
                    }
                }
                else if (fromColumn > toColumn && fromRow > toRow) // moving up, left
                {
                    for (int i = fromColumn - 1, j = fromRow - 1 ; i > toColumn && j > toRow ; i--, j--)
                    {
                        if (board[i][j].hasPiece()) return false;
                    }
                }
                if (piece != Pieces.Queen)  // If Bishop, no other possible moves
                {
                    return true;
                }
            case Rook:
                if (fromRow != toRow && fromColumn == toColumn) // Rook moving vertically (or Queen)
                {
                    if (fromRow < toRow) // moving down
                    {
                        for (int i = fromRow + 1; i < toRow ; i++)
                        {
                            if (board[fromColumn][i].hasPiece())
                            {
                                return false;
                            }
                        }
                    }
                    else // moving up
                    {
                        for (int i = fromRow - 1 ; i > toRow ; i--)
                        {
                            if (board[fromColumn][i].hasPiece())
                            {
                                return false;
                            }
                        }
                    }
                }
                else if (fromRow == toRow && fromColumn != toColumn) // Rook moving horizontally (or Queen)
                {
                    if (fromColumn < toColumn) // moving left
                    {
                        for (int i = fromColumn + 1; i < toColumn ; i++)
                        {
                            if (board[i][fromRow].hasPiece())
                            {
                                return false;
                            }
                        }
                    }
                    else // moving right
                    {
                        for (int i = fromColumn - 1 ; i > toColumn ; i--)
                        {
                            if (board[i][fromRow].hasPiece())
                            {
                                return false;
                            }
                        }
                    }
                }
                return true; // no other moves for rook or queen
            case Knight:
                return true; // Knight can jump over pieces
            case King:
                if (board[fromColumn][fromRow].getPiece().openingMove())  // if kings first move
                {
                    // if castling
                    if (!board[fromColumn - 1][fromRow].hasPiece() && !board[fromColumn - 2][fromRow].hasPiece() && !board[fromColumn - 3][fromRow].hasPiece())
                    {
                        return board[fromColumn - 4][fromRow].hasPiece() && board[fromColumn - 4][fromRow].getPiece().openingMove() && board[fromColumn - 4][fromRow].getPiece().getType() == Pieces.Rook;
                    }
                    if (!board[fromColumn + 1][fromRow].hasPiece() && !board[fromColumn + 2][fromRow].hasPiece())
                    {
                        return board[fromColumn + 3][fromRow].hasPiece() && board[fromColumn + 3][fromRow].getPiece().openingMove() && board[fromColumn + 3][fromRow].getPiece().getType() == Pieces.Rook;
                    }
                }
                return (Math.abs(fromColumn - toColumn) == 1 && Math.abs(fromRow - toRow) == 0) || (Math.abs(fromColumn - toColumn) == 0 && Math.abs(fromRow - toRow) == 1) || (Math.abs(fromColumn - toColumn) == 1 && Math.abs(fromRow - toRow) == 1); // King can only move one square if not first move
        }
        return false;
    }


    //removes the en passant flag from pawns
    void removeEnPassant ( int moves )
    {
        for (int i = 0 ; i < 7 ; i++)
        {
            if (board[i][3].hasPiece() && board[i][3].getPiece().verifyEnPassant() && board[i][3].getPiece().getEnPassant() != moves) {
                board[i][3].getPiece().flipEnPassant();
            }
        }
        for (int i = 0 ; i < 7 ; i++)
        {
            if (board[i][4].hasPiece() && board[i][4].getPiece().verifyEnPassant() && board[i][4].getPiece().getEnPassant() != moves) {
                board[i][4].getPiece().flipEnPassant();
            }
        }
    }

    boolean verifyEnPassant ( Locations s )
    {
        return board[s.getCol()][s.getRow()].hasPiece() && board[s.getCol()][s.getRow()].getPiece().verifyEnPassant();
    }

    Pieces verifyType ( Locations s )
    {
        if (board[s.getCol()][s.getRow()].hasPiece())
        {
            return board[s.getCol()][s.getRow()].getPiece().getType();
        }
        return null;
    }

    Colours verifyColour ( Locations s )
    {
        return board[s.getCol()][s.getRow()].getPiece().getColour();
    }

    int verifyValue ( Locations s )
    {
        return board[s.getCol()][s.getRow()].getPiece().getValue();
    }

    boolean initialMove ( Locations s )
    {
        return board[s.getCol()][s.getRow()].hasPiece() && board[s.getCol()][s.getRow()].getPiece().openingMove();
    }

    // change the piece on a given square with a new piece
    void switchPiece ( Locations s, Pieces newPiece )
    {
        if (board[s.getCol()][s.getRow()].hasPiece())
        {
            switch (newPiece)
            {
                case Rook:
                    board[s.getCol()][s.getRow()].setPiece(new Rook(board[s.getCol()][s.getRow()].killPiece().getColour()));
                    break;
                case Knight:
                    board[s.getCol()][s.getRow()].setPiece(new Knight(board[s.getCol()][s.getRow()].killPiece().getColour()));
                    break;
                case Bishop:
                    board[s.getCol()][s.getRow()].setPiece(new Bishop(board[s.getCol()][s.getRow()].killPiece().getColour()));
                    break;
                case Queen:
                    board[s.getCol()][s.getRow()].setPiece(new Queen(board[s.getCol()][s.getRow()].killPiece().getColour()));
                    break;
                case Pawn:
                    board[s.getCol()][s.getRow()].setPiece(new Pawn(board[s.getCol()][s.getRow()].killPiece().getColour()));
                    break;
            }
        }
    }

    void movePiece ( Locations f, Locations t )
    {
        board[t.getCol()][t.getRow()].setPiece(board[f.getCol()][f.getRow()].killPiece());
    }

    chessPiece killPiece ( Locations s )
    {
            return board[s.getCol()][s.getRow()].killPiece();
    }

    void setPiece (Locations s, chessPiece p )
    {
        if (!board[s.getCol()][s.getRow()].hasPiece())
        {
             board[s.getCol()][s.getRow()].setPiece(p);
        }
    }

    boolean hasPiece ( Locations s )
    {
        return board[s.getCol()][s.getRow()].hasPiece();
    }

    // old console text board (Now redundant)
    void printBoard ( ) {
        for (int i = 0 ; i < 8 ; i++) {
            System.out.print("\t\t" + i + "\t\t");
        }
        System.out.println();
        System.out.println();
        for (int i = 0 ; i < 8 ; i++) {
            System.out.print(i);
            for (int j = 0 ; j < 8 ; j++) {
                System.out.print(board[j][i].hasPiece() ?
                        "\t" + board[j][i].getPiece().getColour() + board[j][i].getPiece().getType() + "\t" :
                        "\t\t\t\t");
            }
            System.out.println(i);
            System.out.println();
        }
        for (int i = 0 ; i < 8 ; i++) {
            System.out.print("\t\t" + i + "\t\t");
        }
        System.out.println();
        System.out.println();
    }

    // returns the move for a given piece
    List<Locations> getMoves(Colours currentTurn, Locations p)
    {
        List<Locations> allMoves = new LinkedList<>();
        if (hasPiece(p) && currentTurn == verifyColour(p))
        {
            for (int i = 0 ; i < 8 ; i++) {
                for (int j = 0 ; j < 8 ; j++) {
                    if (allowedMove(p, new Locations(i, j)) &&
                            checkRoute(p, new Locations(i, j), verifyType(p)) &&
                            (!hasPiece(new Locations(i, j)) || verifyColour(new Locations(i, j)) != currentTurn)) {
                        if (!anyCouldAttackKing(p, new Locations(i, j))) {
                            allMoves.add(new Locations(i, j));
                        }
                    }
                }
            }
        }
        return allMoves;
    }

    // returns true if any pieces can attack the king
    boolean vulnerableKing ( Colours currentTurn )
    {
        for (int i = 0 ; i < 8 ; i++)
        {
            for (int j = 0 ; j < 8 ; j++)
            {
                if (hasPiece(new Locations(i, j)) && verifyColour(new Locations(i, j)) == currentTurn)
                {
                    if (canAttackKing(new Locations(i, j)))
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // true if the passed piece can attack the king
    boolean canAttackKing ( Locations s )
    {
        for (int i = 0 ; i < 8 ; i++)
        {
            for (int j = 0 ; j < 8 ; j++)
            {
                if (hasPiece(new Locations(i, j)) && verifyType(new Locations(i, j)) == Pieces.King && verifyColour(new Locations(i, j)) != verifyColour(s))
                {
                    if (allowedMove(s, new Locations(i, j)) && checkRoute(s, new Locations(i, j), verifyType(s)))
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //true if any piece could attack the king if the move was made
    boolean anyCouldAttackKing ( Locations from, Locations to )
    {
        chessPiece p, q = null, c = null;
        boolean could;
        p = killPiece(from);
        if (hasPiece(to))
        {
            if (verifyColour(to) != p.getColour())
            {
                q = killPiece(to);
            }
            else
            {
                return false;
            }
        }
        setPiece(to, p);
        if (p.getType() == Pieces.King && p.openingMove())
        { // if castling
            if (from.getCol() - to.getCol() == -2 && from.getCol() == 4 && hasPiece(new Locations(to.getCol() + 1, to.getRow())) && verifyType(new Locations(to.getCol() + 1, to.getRow())) == Pieces.Rook)
            {
                if ((c = killPiece(new Locations(to.getCol() + 1, to.getRow()))).openingMove())
                {
                    setPiece(new Locations(to.getCol() - 1, to.getRow()), c);
                }
                else
                {
                    setPiece(new Locations(to.getCol() + 1, to.getRow()), c);
                }
            }
            else if (from.getCol() - to.getCol() == 2 && from.getCol() == 4 && hasPiece(new Locations(to.getCol() - 2, to.getRow())) && verifyType(new Locations(to.getCol() - 2, to.getRow())) == Pieces.Rook)
            {
                if ((c = killPiece(new Locations(to.getCol() - 2, to.getRow()))).openingMove())
                {
                    setPiece(new Locations(to.getCol() + 1, to.getRow()), c);
                }
                else
                {
                    setPiece(new Locations(to.getCol() - 2, to.getRow()), c);
                }
            }
        }
        if (verifyColour(to) == Colours.White)
        {
            could = vulnerableKing(Colours.Black);
        }
        else
        {
            could = vulnerableKing(Colours.White);
        }
        p = killPiece(to);
        if (p.getType() == Pieces.King && p.openingMove())
        { // put castle back
            if (from.getCol() - to.getCol() == -2 && c != null && c.openingMove())
            {
                setPiece(new Locations(to.getCol() + 1, to.getRow()), killPiece(new Locations(to.getCol() - 1, to.getRow())));
            }
            else if (from.getCol() - to.getCol() == 2 && c != null && c.openingMove())
            {
                setPiece(new Locations(to.getCol() - 2, to.getRow()), killPiece(new Locations(to.getCol() + 1, to.getRow())));
            }
        }
        if (q != null)
        {
            setPiece(to, q);
        }
        setPiece(from, p);
        return could;
    }
}