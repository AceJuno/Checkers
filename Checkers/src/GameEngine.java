import java.util.Vector;

public class GameEngine {



    public final Vector<Pawn> blackPawns = new Vector<>();
    public final Vector<Pawn> whitePawns = new Vector<>();
    Vector<Point> posMoves = new Vector<>();
    Vector<Point> posAttacks = new Vector<>();
    boolean afterAttack = false;
    boolean whiteTurn = true;
    // --Commented out by Inspection (23.05.2020 17:00):Point pawnAfterAttack;
    Point highlightedPawn = new Point(20, 20);

    GameEngine()
    {
        startUp();
    }

    public void turn (int row, int col)
    {
        if(highlightedPawn.row == row && highlightedPawn.col == col) return;

        if(afterAttack)
        {
            if(attack(row, col))
            {
                posAttacks.clear();
                checkAttacks(row, col);
                if(posAttacks.isEmpty())
                {
                    afterAttack = false;
                    whiteTurn = !whiteTurn;
                    return;
                }
            }
        }

        if(move(row, col))
        {
            posAttacks.clear();
            posMoves.clear();
            whiteTurn = !whiteTurn;
            highlightedPawn = new Point(20,20);
            return;
        }

        if(attack(row, col))
        {
            posAttacks.clear();
            posMoves.clear();
            checkAttacks(row, col);
            if(!posAttacks.isEmpty())
            {
                afterAttack = true;
                return;
            }
            whiteTurn = !whiteTurn;
            highlightedPawn = new Point(20, 20);
            return;
        }
        highlightedPawn = new Point(row, col);
        checkMoves(row, col);
        checkAttacks(row, col);
    }

    public void startUp ()
    {
        //blackPawns
        for (int i = 0; i < 3; i++)
        {
            for (int k = 0; k < 8; k++)
            {
                if(i % 2 == 0 && k % 2 == 1) blackPawns.add (new Pawn(i, k));
                else if(i % 2 == 1 && k % 2 == 0) blackPawns.add (new Pawn(i, k));
            }
        }
        //blackPawns.add(new Pawn (false, 4, 3)); for testing purposes
        //whitePawns
        for (int i = 5; i < 8; i++)
        {
            for (int k = 0; k < 8; k++)
            {
                if(i % 2 == 0 && k % 2 == 1) whitePawns.add (new Pawn(i, k));
                else if(i % 2 == 1 && k % 2 == 0) whitePawns.add (new Pawn(i, k));
            }
        }

    }

    public void testStartUp()
    {
        blackPawns.add (new Pawn(1, 2));
        whitePawns.add (new Pawn(4, 5));
        blackPawns.add (new Pawn(3, 4));
        blackPawns.add (new Pawn(6, 7));
        whitePawns.get(0).isQueen = true;
        blackPawns.get(0).isQueen = true;
    }

    public void checkMoves(int row, int col)
    {
        Vector<Point> temp = new Vector<>();
        if (afterAttack) posMoves = temp;
        if(!whiteTurn) {
            for (Pawn p : blackPawns) {
                if (p.row == row && p.col == col) {
                    if (p.isQueen)
                    {
                        if (checkMoveLeftDown(row, col)) temp.add(new Point(row + 1, col - 1));
                        if (checkMoveRightDown(row, col)) temp.add(new Point(row + 1, col + 1));
                        if (checkMoveLeftUp(row, col)) temp.add(new Point(row - 1, col - 1));
                        if (checkMoveRightUp(row, col)) temp.add(new Point(row - 1, col + 1));
                    }
                    else
                        {
                        if (checkMoveLeftDown(row, col)) temp.add(new Point(row + 1, col - 1));
                        if (checkMoveRightDown(row, col)) temp.add(new Point(row + 1, col + 1));
                        }
                }
            }
        }

        if(whiteTurn) {
            for (Pawn p : whitePawns) {
                if (p.row == row && p.col == col) {
                    if (p.isQueen)
                    {
                        if (checkMoveLeftDown(row, col)) temp.add(new Point(row + 1, col - 1));
                        if (checkMoveRightDown(row, col)) temp.add(new Point(row + 1, col + 1));
                    }
                    if (checkMoveLeftUp(row, col)) temp.add(new Point(row - 1, col - 1));
                    if (checkMoveRightUp(row, col)) temp.add(new Point(row - 1, col + 1));
                }
            }
        }
        posMoves = temp;
    }

    public void checkAttacks(int row, int col)
    {

        Vector<Point> temp = new Vector<>();
        if (afterAttack) posAttacks = temp;
        if(whiteTurn)
        {
            for (Pawn p : whitePawns) {
                if (p.row == row && p.col == col) {
                    if (p.isQueen) {
                        if(checkAttackLeftUp(row, col, true)) temp.add(new Point(row - 2, col - 2));
                        if(checkAttackRightUp(row, col, true)) temp.add(new Point(row - 2, col + 2));
                        if(checkAttackLeftDown(row, col, true)) temp.add(new Point(row + 2, col - 2));
                        if(checkAttackRightDown(row, col, true)) temp.add(new Point(row + 2, col + 2));
                    } else {
                        if(checkAttackLeftUp(row, col, true)) temp.add(new Point(row - 2, col - 2));
                        if(checkAttackRightUp(row, col, true)) temp.add(new Point(row - 2, col + 2));
                    }
                }
            }
        }
        if(!whiteTurn)
        {
            for (Pawn p : blackPawns) {
                if (p.row == row && p.col == col) {
                    if (p.isQueen) {
                        if(checkAttackLeftUp(row, col, false)) temp.add(new Point(row - 2, col - 2));
                        if(checkAttackRightUp(row, col, false)) temp.add(new Point(row - 2, col + 2));
                    }
                    if(checkAttackLeftDown(row, col, false)) temp.add(new Point(row + 2, col - 2));
                    if(checkAttackRightDown(row, col, false)) temp.add(new Point(row + 2, col + 2));
                }
            }
        }




        posAttacks = temp;
    }

    public boolean attack(int row, int col)
    {
        boolean attackMade = false;
        for(Point point : posAttacks)
        {
            if(point.col == col && point.row == row) {
                if (!whiteTurn) {
                    for (Pawn p1 : blackPawns) {
                        if (row == p1.row + 2 && col == p1.col + 2 && highlightedPawn.row == p1.row && highlightedPawn.col == p1.col) {
                            whitePawns.removeIf(removed -> removed.row == p1.row + 1 && removed.col == p1.col + 1);
                            attackMade = true;
                            break;
                        }
                        if (row == p1.row + 2 && col == p1.col - 2 && highlightedPawn.row == p1.row && highlightedPawn.col == p1.col) {
                            whitePawns.removeIf(removed -> removed.row == p1.row + 1 && removed.col == p1.col - 1);
                            attackMade = true;
                            break;
                        }
                        if (p1.isQueen) {
                            if (row == p1.row - 2 && col == p1.col + 2 && highlightedPawn.row == p1.row && highlightedPawn.col == p1.col) {
                                whitePawns.removeIf(removed -> removed.row == p1.row - 1 && removed.col == p1.col + 1);
                                attackMade = true;
                                break;
                            }
                            if (row == p1.row - 2 && col == p1.col - 2 && highlightedPawn.row == p1.row && highlightedPawn.col == p1.col) {
                                whitePawns.removeIf(removed -> removed.row == p1.row - 1 && removed.col == p1.col - 1);
                                attackMade = true;
                                break;
                            }
                        }
                    }
                } else {
                    for (Pawn p1 : whitePawns) {
                        if (row == p1.row - 2 && col == p1.col + 2 && highlightedPawn.row == p1.row && highlightedPawn.col == p1.col) {
                            blackPawns.removeIf(removed -> removed.row == p1.row - 1 && removed.col == p1.col + 1);
                            attackMade = true;
                            break;
                        }
                        if (row == p1.row - 2 && col == p1.col - 2 && highlightedPawn.row == p1.row && highlightedPawn.col == p1.col) {
                            blackPawns.removeIf(removed -> removed.row == p1.row - 1 && removed.col == p1.col - 1);
                            attackMade = true;
                            break;
                        }
                        if (p1.isQueen) {
                            if (row == p1.row + 2 && col == p1.col + 2 && highlightedPawn.row == p1.row && highlightedPawn.col == p1.col) {
                                blackPawns.removeIf(removed -> removed.row == p1.row + 1 && removed.col == p1.col + 1);
                                attackMade = true;
                                break;
                            }
                            if (row == p1.row + 2 && col == p1.col - 2 && highlightedPawn.row == p1.row && highlightedPawn.col == p1.col) {
                                blackPawns.removeIf(removed -> removed.row == p1.row + 1 && removed.col == p1.col - 1);
                                attackMade = true;
                                break;
                            }
                        }
                    }
                }
            }
        }
        if(attackMade) {
            for (Pawn p1 : blackPawns) {
                if(highlightedPawn.row == p1.row && highlightedPawn.col == p1.col)
                {
                    p1.col = col;
                    p1.row = row;
                    if (row == 7) p1.isQueen = true;
                }
            }

            for (Pawn p1 : whitePawns) {
                if(highlightedPawn.row == p1.row && highlightedPawn.col == p1.col)
                {
                    p1.col = col;
                    p1.row = row;
                    if (row == 0) p1.isQueen = true;
                }
            }
        }
        return attackMade;
    }

    public boolean move(int row, int col) {
        boolean moveMade = false;
        for (Point point : posMoves) {
            if (point.col == col && point.row == row) {
                for (Pawn p1 : blackPawns) {
                    if (highlightedPawn.col == p1.col && highlightedPawn.row == p1.row) {
                        p1.col = col;
                        p1.row = row;
                        if (row == 7) p1.isQueen = true;
                        moveMade = true;
                    }
                }
                for (Pawn p1 : whitePawns) {
                    if (highlightedPawn.col == p1.col && highlightedPawn.row == p1.row) {
                        p1.col = col;
                        p1.row = row;
                        if (row == 0) p1.isQueen = true;
                        moveMade = true;
                    }
                }
            }
        }
        return moveMade;
    }

    public boolean checkMoveLeftUp(int row, int col)
    {
        boolean occupied = false;
                if (col != 0 && row != 0) {
                    for (Pawn p1 : whitePawns)
                    {
                        if (p1.row == row - 1 && p1.col == col - 1)
                        {
                            occupied = true;
                            break;
                        }
                    }
                    if (!occupied) {
                        for (Pawn p2 : blackPawns)
                        {
                            if (p2.row == row - 1 && p2.col == col - 1)
                            {
                                occupied = true;
                                break;
                            }
                        }
                    }
                }
                else return false;

        return !occupied;
    }

    public boolean checkMoveRightUp(int row, int col)
    {
        boolean occupied = false;
                if (col != 7 && row != 0) {
                    for (Pawn p1 : whitePawns)
                    {
                        if (p1.row == row - 1 && p1.col == col + 1)
                        {
                            occupied = true;
                            break;
                        }
                    }
                    if (!occupied) {
                        for (Pawn p2 : blackPawns)
                        {
                            if (p2.row == row - 1 && p2.col == col + 1)
                            {
                                occupied = true;
                                break;
                            }
                        }
                    }
                }
                else return false;
        return !occupied;
    }

    public boolean checkMoveRightDown(int row, int col)
    {
        boolean occupied = false;
                if (col != 7 && row != 7) {
                    for (Pawn p1 : whitePawns)
                    {
                        if (p1.row == row + 1 && p1.col == col + 1)
                        {
                            occupied = true;
                            break;
                        }
                    }
                    if (!occupied) {
                        for (Pawn p2 : blackPawns)
                        {
                            if (p2.row == row + 1 && p2.col == col + 1)
                            {
                                occupied = true;
                                break;
                            }
                        }
                    }
                }
                else return false;

        return !occupied;
    }

    public boolean checkMoveLeftDown(int row, int col)
    {
        boolean occupied = false;
                if (col != 0 && row != 7) {
                    for (Pawn p1 : whitePawns)
                    {
                        if (p1.row == row + 1 && p1.col == col - 1)
                        {
                            occupied = true;
                            break;
                        }
                    }
                    if (!occupied) {
                        for (Pawn p2 : blackPawns)
                        {
                            if (p2.row == row + 1 && p2.col == col - 1)
                            {
                                occupied = true;
                                break;
                            }
                        }
                    }
                }
                else return false;
        return !occupied;
    }

    public boolean checkAttackLeftUp(int row, int col, boolean white)
    {
        boolean occupied = false;
        boolean isPawnToAttack = false;

        if (col != 0 && col != 1 && row != 0 && row != 1) {
            if(!white) {
                for (Pawn p1 : whitePawns) {
                    if (p1.row == row - 1 && p1.col == col - 1) {
                        isPawnToAttack = true;
                        break;
                    }
                }
            }
            if (white) {
                for (Pawn p2 : blackPawns)
                {
                    if (p2.row == row - 1 && p2.col == col - 1)
                    {
                        isPawnToAttack = true;
                        break;
                    }
                }
            }
        }
        else return false;

        if(isPawnToAttack) {
            for (Pawn behind : whitePawns) {
                if (behind.row == row - 2 && behind.col == col - 2) {
                    occupied = true;
                    break;
                }
            }
            if (occupied) return false;
            else {
                for (Pawn behind : blackPawns) {
                    if (behind.row == row - 2 && behind.col == col - 2) {
                        occupied = true;
                        break;
                    }
                }
            }
            if (occupied) return false;
        }
        return isPawnToAttack;


    }

    public boolean checkAttackRightUp(int row, int col, boolean white)
    {
        boolean occupied = false;
        boolean isPawnToAttack = false;

        if (col != 7 && col != 6 && row != 0 && row != 1) {
            if(!white) {
                for (Pawn p1 : whitePawns) {
                    if (p1.row == row - 1 && p1.col == col + 1) {
                        isPawnToAttack = true;
                        break;
                    }
                }
            }
            if (white) {
                for (Pawn p2 : blackPawns)
                {
                    if (p2.row == row - 1 && p2.col == col + 1)
                    {
                        isPawnToAttack = true;
                        break;
                    }
                }
            }
        }
        else return false;

        if(isPawnToAttack) {
            for (Pawn behind : whitePawns) {
                if (behind.row == row - 2 && behind.col == col + 2) {
                    occupied = true;
                    break;
                }
            }
            if (occupied) return false;
            else {
                for (Pawn behind : blackPawns) {
                    if (behind.row == row - 2 && behind.col == col + 2) {
                        occupied = true;
                        break;
                    }
                }
            }
            if (occupied) return false;
        }
        return isPawnToAttack;


    }

    public boolean checkAttackRightDown(int row, int col, boolean white)
    {
        boolean occupied = false;
        boolean isPawnToAttack = false;

        if (col != 7 && col != 6 && row != 6 && row != 7) {
            if(!white) {
                for (Pawn p1 : whitePawns) {
                    if (p1.row == row + 1 && p1.col == col + 1) {
                        isPawnToAttack = true;
                        break;
                    }
                }
            }
            if (white) {
                for (Pawn p2 : blackPawns)
                {
                    if (p2.row == row + 1 && p2.col == col + 1)
                    {
                        isPawnToAttack = true;
                        break;
                    }
                }
            }
        }
        else return false;

        if(isPawnToAttack) {
            for (Pawn behind : whitePawns) {
                if (behind.row == row + 2 && behind.col == col + 2) {
                    occupied = true;
                    break;
                }
            }
            if (occupied) return false;
            else {
                for (Pawn behind : blackPawns) {
                    if (behind.row == row + 2 && behind.col == col + 2) {
                        occupied = true;
                        break;
                    }
                }
            }
            if (occupied) return false;
        }
        return isPawnToAttack;


    }

    public boolean checkAttackLeftDown(int row, int col, boolean white)
    {
        boolean occupied = false;
        boolean isPawnToAttack = false;

        if (col != 1 && col != 0 && row != 7 && row != 6) {
            if(!white) {
                for (Pawn p1 : whitePawns) {
                    if (p1.row == row + 1 && p1.col == col - 1) {
                        isPawnToAttack = true;
                        break;
                    }
                }
            }
            if (white) {
                for (Pawn p2 : blackPawns)
                {
                    if (p2.row == row + 1 && p2.col == col - 1)
                    {
                        isPawnToAttack = true;
                        break;
                    }
                }
            }
        }
        else return false;

        if(isPawnToAttack) {
            for (Pawn behind : whitePawns) {
                if (behind.row == row + 2 && behind.col == col - 2) {
                    occupied = true;
                    break;
                }
            }
            if (occupied) return false;
            else {
                for (Pawn behind : blackPawns) {
                    if (behind.row == row + 2 && behind.col == col - 2) {
                        occupied = true;
                        break;
                    }
                }
            }
            if (occupied) return false;
        }
        return isPawnToAttack;


    }
}
