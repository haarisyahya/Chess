import java.util.LinkedList;
import java.util.List;

/*
  
  * @authors <Haaris Yahya & Justin>
  * @version 1.0 (<12/02/2021>)                                                        

  This class contains the AI for the chess game.
  The Minimax method, which is implemented using alpha-beta pruning, will be used by getMove to evaluate the board and identify the optimum move to make.
  The material value is checked by the evaluation function that is utilised to determine the moves to make.
  All the pieces on a given board configuration are arranged in such a way that moves leading to taking better pieces are taken, 
  or better pieces are protected. In addition, the evaluation function considers this mobility factor. It refers to the number of moves accessible to both the player and the AI.
  As the game goes, the difficulty level rises.
*/

class AI
{
    private Colours colour;
    Players type;
    private int moveNum = 0;
    private int initialDepth = 4;
    Turn move;
    AI ( Colours c )
    {
        type = Players.Computer;
        colour = c;
    }

    // Calls the Max function of the minimax algorithm to get the best move

    Turn getMove(chessBoard x)
    {
        chessBoard b = duplicateBoard(x);
        Max(b, Integer.MIN_VALUE, Integer.MAX_VALUE, initialDepth);
        return move;
    }

    // Max function of minimax (alpha-beta)
    private int Max ( chessBoard b, int alpha, int beta, int depth )
    {
        chessPiece p;
        Pieces op, np;
        boolean wasOpeningMove;
        boolean wasEnPassant;
        boolean wasPromo;

        if (depth == 0)
        { //if at max ply
            return evalBoard(b);
        }
        List<Turn> moves = receiveEveryMove(b, colour == Colours.White ? Colours.White : Colours.Black);
        while(moves.size() > 0)
        {
            Turn m = moves.remove(0); // get move
            wasOpeningMove = b.initialMove(m.getFrom());
            wasEnPassant = (b.verifyType(m.getFrom()) == Pieces.Pawn && m.getFrom().getCol() != m.getTo().getCol() && !b.hasPiece(m.getTo()));
            op = b.verifyType(m.getFrom());
            p = b.makeMove(m, moveNum); // make move
            np = b.verifyType(m.getTo());
            wasPromo = op != np;
            moveNum++;
            int value = Min(b, alpha, beta, depth - 1); //call min
            if (value >= beta)
            { // beta cutoff
                b.retractMove(m, p, wasOpeningMove, wasEnPassant, wasPromo);
                moveNum--;
                return beta;
            }
            if (value > alpha)
            {
                if (depth == initialDepth)
                {
                    move = m;
                }
                alpha = value;
            }
            b.retractMove(m, p, wasOpeningMove, wasEnPassant, wasPromo); //undo move
            moveNum--;
        }
        return alpha;
    }

    private int Min( chessBoard b, int alpha, int beta, int depth )
    {
        chessPiece p;
        Pieces op, np;
        boolean wasOpeningMove;
        boolean wasEnPassant;
        boolean wasPromo = false;
        if (depth == 0)
        {
            return evalBoard(b);
        }
        List<Turn> moves = receiveEveryMove(b, colour == Colours.White ? Colours.Black : Colours.White);
        while(moves.size() > 0)
        {
            Turn m = moves.remove(0); // get move
            wasOpeningMove = b.initialMove(m.getFrom());
            wasEnPassant = (b.verifyType(m.getFrom()) == Pieces.Pawn && m.getFrom().getCol() != m.getTo().getCol() && !b.hasPiece(m.getTo()));
            op = b.verifyType(m.getFrom());
            p = b.makeMove(m, moveNum); // make move
            np = b.verifyType(m.getTo());
            if (op != np)
            {
                wasPromo = true;
            }
            moveNum++;
            int value = Max(b, alpha, beta, depth - 1); // call max
            if (value <= alpha) { // alpha cutoff
                b.retractMove(m, p, wasOpeningMove, wasEnPassant, wasPromo); // undo
                moveNum--;
                return alpha;
            }
            if (value < beta)
            {
                if (depth == initialDepth)
                {
                    move = m;
                }
                beta = value;
            }
            b.retractMove(m, p, wasOpeningMove, wasEnPassant, wasPromo); // undo
            moveNum--;
        }
        return beta;
    }

    // All of the player's moves are returned by this method
    private List<Turn> receiveEveryMove(chessBoard b, Colours colour)
    {
        Locations c;
        List<Turn> allMoves = new LinkedList<>();
        for (int i = 0 ; i < 8 ; i++)
        {
            for (int j = 0 ; j < 8 ; j++)
            {
                c = new Locations(i, j);
                if (b.hasPiece(c) && b.verifyColour(c) == colour)
                {
                    allMoves.addAll(mergeLists(c, b.getMoves(colour, c)));
                }
            }
        }
        return allMoves;
    }

    private List<Turn> mergeLists ( Locations c, List<Locations> to )
    {
        List<Turn> newList = new LinkedList<>();
        for (Locations t : to)
        {
            newList.add(new Turn(c, t));
        }
        return newList;
    }

    // This method makes a replica of the board that is only visible to you
    chessBoard duplicateBoard (chessBoard b)
    {
        chessBoard nb = new chessBoard();
        nb.emptyBoard();
        Locations c;
        chessPiece p = null;

        for (int i = 0 ; i < 8 ; i++)
        {
            for (int j = 0 ; j < 8 ; j++)
            {
                c = new Locations(i, j);
                if (b.hasPiece(c))
                {
                    switch (b.verifyType(c))
                    {
                        case Pawn:
                            p = new Pawn(b.verifyColour(c));
                            break;
                        case Rook:
                            p = new Rook(b.verifyColour(c));
                            break;
                        case Knight:
                            p = new Knight(b.verifyColour(c));
                            break;
                        case Bishop:
                            p = new Bishop(b.verifyColour(c));
                            break;
                        case King:
                            p = new King(b.verifyColour(c));
                            break;
                        case Queen:
                            p = new Queen(b.verifyColour(c));
                            break;
                    }
                    nb.setPiece(c, p);
                }
            }
        }
        return nb;
    }

    AI setPly ( int ply )
    {
        initialDepth = ply;
        return this;
    }


    // returns the evaluation of the board
    int evalBoard ( chessBoard b )
    {
        return boardValue(b) + movesAvailable(b); // This compensates for basic chess rules such as not having knights on the rim and providing bishops with open flanks by including moves available.
    }

    // returns the number of pieces on the board
    int maxPieces ( chessBoard b )
    {
        int c = 0;
        for (int i = 0 ; i < 8 ; i++)
        {
            for (int j = 0 ; j < 8 ; j++)
            {
                if (b.hasPiece(new Locations(i, j)))
                {
                    c++;
                }
            }
        }
        return c;
    }

    // returns the material valuation of the board (the sum of the piece values)
    int boardValue ( chessBoard b )
    {
        Locations c;
        int v = 0;
        for (int i = 0 ; i < 8 ; i++)
        {
            for (int j = 0 ; j < 8 ; j++)
            {
                c = new Locations(i, j);
                if(b.hasPiece(c))
                {
                    if (b.verifyColour(c) == colour)
                    {
                        v+=b.verifyValue(c);
                    }
                    else
                    {
                        v-=b.verifyValue(c);
                    }
                }
            }
        }
        return v;
    }

    // returns the number of moves available. Value becomes more heavily weighted as the game progresses
    int movesAvailable ( chessBoard b )
    {
        Locations c;
        double v = 0;
        for (int i = 0 ; i < 8 ; i++)
        {
            for (int j = 0 ; j < 8 ; j++)
            {
                c = new Locations(i, j);
                if(b.hasPiece(c))
                {
                    if (b.verifyColour(c) == colour)
                    {
                        v+=b.getMoves(colour, c).toArray().length;
                    }
                    else
                    {
                        v-=b.getMoves(colour, c).toArray().length;
                    }
                }
            }
        }
        return (int)Math.round(v*((maxPieces(b)/32) + 0.1));
    }

    AI setType ( Players type )
    {
        this.type = type;
        return this;
    }

    public Players getType ( )
    {
        return type;
    }

    Colours getColour ( )
    {
        return colour;
    }
}