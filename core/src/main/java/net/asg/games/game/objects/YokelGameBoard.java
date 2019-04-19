package net.asg.games.game.objects;

import com.badlogic.gdx.utils.Array;
import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;

import net.asg.games.utils.Util;

import java.util.Arrays;
import java.util.Iterator;

public class YokelGameBoard extends YokelObject {
    public static final int MAX_WIDTH = 6;
    public static final int MAX_HEIGHT = 16;

    public int[][] cells;
    boolean[] ids;
    int idIndex;
    static int[] targetRows = new int[MAX_WIDTH];
    int[] randomColumnIndices = new int[MAX_WIDTH];
    boolean[][] colorBlastGrid
            = { new boolean[MAX_WIDTH],
            new boolean[MAX_WIDTH],
            new boolean[MAX_WIDTH],
            new boolean[MAX_WIDTH],
            new boolean[MAX_WIDTH],
            new boolean[MAX_WIDTH],
            new boolean[MAX_WIDTH],
            new boolean[MAX_WIDTH],
            new boolean[MAX_WIDTH],
            new boolean[MAX_WIDTH],
            new boolean[MAX_WIDTH],
            new boolean[MAX_WIDTH],
            new boolean[MAX_WIDTH],
            new boolean[MAX_WIDTH],
            new boolean[MAX_WIDTH],
            new boolean[MAX_WIDTH] };
    int[] pushRowOrder = { 0, 1, 2, 2, 1, 0 };
    int[] pushColumnOrder = { 2, 3, 1, 4, 0, 5 };
    int[] countOfPieces = new int[MAX_WIDTH];

    boolean[] cellMatches = new boolean[7];
    int[] cellIndices = { 0,  1, 2,  3,  4,  5,  6 };
    int[] cellHashes = { 5, 25, 7, 49, 35, 19, 23 };

    int[] columnMatchLookup = { -1, -1, 0, 1, 1, 1, 0, -1 };
    int[] rowMatchLookup = { 0, 1, 1, 1, 0, -1, -1, -1 };

    public YokelGameBoard(){
        cells = new int[MAX_WIDTH][MAX_HEIGHT];
    }

    @Override
    public void dispose() {

    }

    public int[][] getCells(){
        return cells;
    }

    public void setCels(int row, int col, int cell){
        cells[col][row] = cell;
    }

    public void clearBoard() {
        //Util.invokeMethodOnMatrix(MAX_WIDTH, MAX_HEIGHT, this, );
        //invokeMethodOnMatrix
        for (int i = 0; i < MAX_WIDTH; i++) {
            for (int j = 0; j < MAX_HEIGHT; j++){
                clearCell(i,j);
            }
        }
        for (int i = 0; i < 128; i++)
            ids[i] = false;
        idIndex = 0;
    }

    private void clearCell(int r, int c){
        cells[r][c] = YokelBlock.CLEAR;
    }
}

/*


public class BoardEvaluator implements IDataIO
{
    public int[][] cells;
    boolean[] ids;
    int idIndex;
    static int[] targetRows = new int[6];
    int[] randomColumnIndices = new int[6];
    boolean[][] colorBlastGrid
            = { new boolean[6],
            new boolean[6],
            new boolean[6],
            new boolean[6],
            new boolean[6],
            new boolean[6],
            new boolean[6],
            new boolean[6],
            new boolean[6],
            new boolean[6],
            new boolean[6],
            new boolean[6],
            new boolean[6],
            new boolean[6],
            new boolean[6],
            new boolean[6] };
    int[] pushRowOrder = { 0, 1, 2, 2, 1, 0 };
    int[] pushColumnOrder = { 2, 3, 1, 4, 0, 5 };
    int[] countOfPieces = new int[6];

    boolean[] cellMatches = new boolean[7];
    int[] cellIndices = { 0,  1, 2,  3,  4,  5,  6 };
    int[] cellHashes = { 5, 25, 7, 49, 35, 19, 23 };

    int[] columnMatchLookup = { -1, -1, 0, 1, 1, 1, 0, -1 };
    int[] rowMatchLookup = { 0, 1, 1, 1, 0, -1, -1, -1 };

    Board brd;

    public BoardEvaluator() {
        cells = new int[16][6];
        ids = new boolean[128];
        clearBoard();
    }

    public void setBoard(Board board) {
        brd = board;
    }

    public void readIn(DataInputStream datainputstream) throws IOException {
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 6; j++)
                cells[i][j] = datainputstream.readInt();
        }
        for (int i = 0; i < ids.length; i++) {
            byte byteVal = datainputstream.readByte();
            switch (byteVal) {
                case 0:
                    ids[i] = false;
                    break;
                case 1:
                    ids[i] = true;
                    break;
                default:
                    throw new IOException();
            }
        }
        idIndex = datainputstream.readInt();
        updateBoard();
    }

    public void printOut(DataOutputStream dataoutputstream) throws IOException {
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 6; j++)
                dataoutputstream.writeInt(cells[i][j]);
        }
        for (int i = 0; i < ids.length; i++)
            dataoutputstream.writeByte(ids[i] ? 1 : 0);
        dataoutputstream.writeInt(idIndex);
    }

    public void clearBoard() {
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 6; j++)
                cells[i][j] = 6;
        }
        for (int i = 0; i < 128; i++)
            ids[i] = false;
        idIndex = 0;
        updateBoard();
    }

    int incrementID() {
        do {
            idIndex++;

            if (idIndex == ids.length)
                idIndex = 0;
        } while (ids[idIndex] != false);

        ids[idIndex] = true;
        return idIndex;
    }

    void releaseID(int index) {
        if (ids[index] == false)
            System.out.println("Assertion failure: id " + index
                    + " released but not held");

        ids[index] = false;
    }

    public int getPieceValue(int column, int row) {
        return CellEval.getCellFlag(cells[row][column]);
    }

    public int getBlockValueAt(int column, int row) {
        return cells[row][column];
    }

    public boolean isArtificiallyAdded(int column, int row) {
        return CellEval.hasAddedByYahooFlag(cells[row][column]);
    }

    public boolean isCellBroken(int column, int row) {
        return CellEval.hasBrokenFlag(cells[row][column]);
    }

    public void setValueWithID(int column, int row, int value) {
        cells[row][column] = CellEval.setIDFlag(value, incrementID());
    }

    public int getColumnWithPossiblePieceMatch(GameBoardBlock piece) {
        shuffleColumnIndices();

        for (int i = 0; i < 6; i++) {
            int x = randomColumnIndices[i];
            int y = getColumnFill(x);

            if (y < 12) {
                for (int j = 0; j < 3; j++) {
                    cells[y][x] = piece.getValueAt(j % 3);
                    cells[y + 1][x] = piece.getValueAt((1 + j) % 3);
                    cells[y + 2][x] = piece.getValueAt((2 + j) % 3);

                    boolean hasFullMatch =
                            (hasFullMatchInProximity(x, y)
                                    || hasFullMatchInProximity(x, y + 1)
                                    || hasFullMatchInProximity(x, y + 2));

                    cells[y][x] = 6;
                    cells[y + 1][x] = 6;
                    cells[y + 2][x] = 6;

                    updateBoard();

                    if (hasFullMatch)
                        return j << 8 | x;
                }
            }
        }

        int column = getBoardMakeupHash() % 6;

        for (int col = 0; col < 6; col++) {
            if (getColumnFill(col) < getColumnFill(column)) {
                column = col;
            }
        }

        return column;
    }

    public int getColumnToPlaceYahooCell(int value) {
        // pick "random" column to start
        int x = getBoardMakeupHash() % 6;

        // loop through columns
        for (int i = 0; i < 6; i++) {
            // get the height of the column
            int y = getColumnFill(x);

            // if the height fits in visible rows
            if (y < 12) {
                // put the pending piece in that spot
                cells[y][x] = value;

                // check for a near match
                boolean match = hasMatchingCellInProximity(x, y);

                // reset the cell
                cells[y][x] = 6;

                // if there is no match, return the column
                if (match == false)
                    return x;
            }

            // move to next column wrapped around column count
            x = (x + 1) % 6;
        }

        // If unable to find a column without piece like this nearby,
        // get less picky and only ensure that the piece won't make a break

        // loop through columns again
        for (int i = 0; i < 6; i++) {
            // get height of column
            int y = getColumnFill(x);

            if (y < 12) {
                cells[y][x] = value;
                boolean bool = hasFullMatchInProximity(x, y);
                cells[y][x] = 6;

                if (bool == false)
                    return x;
            }

            x = (x + 1) % 6;
        }

        return -1;
    }

    public boolean canPlacePieceAt(int column, int row) {
        if (column < 0 || column >= 6 || row < 0)
            return false;
        if (row < 13 && getPieceValue(column, row) != 6)
            return false;
        if (row + 1 < 13 && getPieceValue(column, row + 1) != 6)
            return false;
        if (row + 2 < 13 && getPieceValue(column, row + 2) != 6)
            return false;
        return true;
    }

    public int getColumnFill(int column) {
        int row;
        for (row = 16; row > 0; row--) {
            if (getPieceValue(column, row - 1) != 6)
                break;
        }
        return row;
    }

    public boolean isCellFree(int column, int row) {

        if (row < 0 || row > 13)
            return false;

        if (row == 0)
            return true;

        if (getPieceValue(column, row - 1) == 6)
            return false;

        return true;
    }

    public void placeBlockAt(GameBoardBlock block, int x, int y) {
        int index = block.getIndex();

        int v0 = block.getValueAt(index % 3);
        v0 = CellEval.setIDFlag(v0, incrementID());

        int v1 = block.getValueAt((1 + index) % 3);
        v1 = CellEval.setIDFlag(v1, incrementID());
        int v2 = block.getValueAt((2 + index) % 3);
        v2 = CellEval.setIDFlag(v2, incrementID());
        if (CellEval.getCellFlag(cells[y][x]) != 6) {
            Thread.dumpStack();
            System.out.println("Assertion failure: grid at " + x + "," + y
                    + " isn't empty for piece placement");
        }
        if (CellEval.getCellFlag(cells[y + 1][x]) != 6) {
            Thread.dumpStack();
            System.out.println("Assertion failure: grid at " + x + "," + y
                    + " isn't empty for piece placement");
        }
        if (CellEval.getCellFlag(cells[y + 2][x]) != 6) {
            Thread.dumpStack();
            System.out.println("Assertion failure: grid at " + x + "," + y
                    + " isn't empty for piece placement");
        }
        cells[y][x] = v0;
        cells[y + 1][x] = v1;
        cells[y + 2][x] = v2;

        updateBoard();
    }

    public void handlePlacedPowerBlock(int type) {
        for (int y = 0; y < 16; y++) {
            for (int x = 0; x < 6; x++) {
                if (CellEval.hasPowerBlockFlag(cells[y][x])) {
                    if (isCellInBoard(x - 1, y - 1))
                        applyPowerBlockAt(type, x - 1, y - 1);
                    if (isCellInBoard(x, y - 1))
                        applyPowerBlockAt(type, x, y - 1);
                    if (isCellInBoard(x + 1, y - 1))
                        applyPowerBlockAt(type, x + 1, y - 1);
                    if (isCellInBoard(x - 1, y))
                        applyPowerBlockAt(type, x - 1, y);
                    if (isCellInBoard(x, y))
                        applyPowerBlockAt(type, x, y);
                    if (isCellInBoard(x + 1, y))
                        applyPowerBlockAt(type, x + 1, y);
                    if (isCellInBoard(x - 1, y + 1))
                        applyPowerBlockAt(type, x - 1, y + 1);
                    if (isCellInBoard(x, y + 1))
                        applyPowerBlockAt(type, x, y + 1);
                    if (isCellInBoard(x + 1, y + 1))
                        applyPowerBlockAt(type, x + 1, y + 1);
                }
            }
        }
    }

    void applyPowerBlockAt(int value, int col, int row) {
        if (CellEval.hasPowerBlockFlag(value) == false) {
            System.out.println("Assertion failure:  cell isn't weird " + value);
        } else if (CellEval.getCellFlag(value) != 4) {
            System.out
                    .println("Assertion failure:  cell isn't weird type " + value);
        } else if (!CellEval.hasPowerBlockFlag(cells[row][col])) {
            boolean isAttack = CellEval.isOffensive(value);

            if (isAttack) {
                if (CellEval.getCellFlag(cells[row][col]) < 6) {
                    releaseID(CellEval.getID(cells[row][col]));
                }

                if (CellEval.getCellFlag(cells[row][col]) != 6) {
                    cells[row][col] = 7;
                }
            } else if (CellEval.getCellFlag(cells[row][col]) < 6)
                cells[row][col] = CellEval.addArtificialFlag(CellEval.setValueFlag(cells[row][col], 4));

            updateBoard();
        }
    }

    public void setValueAt(int value, int column, int row) {
        if (CellEval.getCellFlag(cells[row][column]) != 6)
            System.out.println("Assertion failure: grid at " + column + ","
                    + row + " isn't empty for cell placement");
        value = CellEval.setIDFlag(value, incrementID());
        cells[row][column] = value;
        updateBoard();
    }

    static boolean isCellInBoard(int column, int row) {
        if (column < 0 || column >= 6 || row < 0 || row >= 16)
            return false;
        return true;
    }

    public boolean hasPlayerDied() {
        for (int row = 13; row < 16; row++) {
            for (int col = 0; col < 6; col++) {
                if (getPieceValue(col, row) != 6)
                    return true;
            }
        }

        if (getPieceValue(2, 12) != 6)
            return true;

        return false;
    }

    public void handleBrokenCellDrops() {
        for (int col = 0; col < 6; col++) {
            int index = 0;

            for (int row = 0; row < 16; row++) {
                if (isCellBroken(col, row)) {
                    if (CellEval.getCellFlag(cells[row][col]) < 6)
                        releaseID(CellEval.getID(cells[row][col]));
                } else {
                    cells[index][col] = cells[row][col];
                    index++;
                }
            }

            for (; index < 16; index++)
                cells[index][col] = 6;
        }
        updateBoard();
    }

    public int getBrokenCellCount() {
        int count = 0;

        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 6; j++) {
                if (CellEval.hasBrokenFlag(cells[i][j]))
                    count++;
            }
        }

        return count;
    }

    public Vector<Cell> getBrokenCells() {
        Vector<Cell> vector = new Vector<Cell>();

        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 6; j++) {
                if (CellEval.hasBrokenFlag(cells[i][j]))
                    vector.addElement(new Cell(j, i));
            }
        }
        return vector;
    }

    public ByteStack getBrokenByPartnerCellIDs() {
        ByteStack stack = new ByteStack();

        for (int row = 0; row < 16; row++) {
            for (int col = 0; col < 6; col++) {
                if (CellEval.hasPartnerBreakFlag(cells[row][col])) {
                    stack.push(CellEval.getID(cells[row][col]));
                }
            }
        }

        return stack;
    }

    public void flagBrokenCells(ByteStack stack) {
        int index = 0;

        while (index < stack.length()) {
            int count = 0;

            for (int row = 0; row < 16; row++) {
                for (int col = 0; col < 6; col++) {
                    if (CellEval.getCellFlag(cells[row][col]) < 6) {
                        int id = CellEval.getID(cells[row][col]);

                        if (stack.getValueAt(index) == id) {
                            count++;
                        }
                    }
                }
            }

            switch (count) {
                default:
                    System.out.println("fucked up, found " + count
                            + " instances of id " + stack.getValueAt(index));
                    /* fall through *//*
                case 0:
                case 1:
                    index++;
            }
        }

        for (int i = 0; i < stack.length(); i++) {
            for (int row = 0; row < 16; row++) {
                for (int col = 0; col < 6; col++) {
                    if (CellEval.getCellFlag(cells[row][col]) < 6
                            && stack.getValueAt(i) == CellEval.getID(cells[row][col])) {

                        cells[row][col] = CellEval.addBrokenFlag(cells[row][col]);
                    }
                }
            }
        }

        updateBoard();
    }

    public Vector getCellsToBeDropped() {
        Vector<CellMove> vector = new Vector<CellMove>();

        for (int i = 0; i < targetRows.length; i++) {
            targetRows[i] = 0;
        }

        for (int y = 0; y < 16; y++) {
            for (int x = 0; x < 6; x++) {
                // The imporant thing to note is that the targetRow will not get
                // incremented when a cell is to be broken.
                if (isCellBroken(x, y) == false) {
                    if (targetRows[x] != y
                            && getPieceValue(x, y) != 6) {

                        vector.addElement(new CellMove(x, y, targetRows[x]));
                    }

                    targetRows[x]++;
                }
            }
        }

        return vector;
    }

    public void checkBoardForPartnerBreaks(BoardEvaluator partner, boolean isPartnerOnRight) {
        for (int row = 0; row < 16; row++) {
            for (int col = 0; col < 6; col++) {
                checkCellForPartnerBreak(partner, isPartnerOnRight, col, row);
            }
        }
    }

    void checkCellForPartnerBreak(BoardEvaluator board, boolean isPartnerOnRight, int col, int row) {
        //boolean bool_53_ = true;
        int colOffset;

        if (isPartnerOnRight == true)
            colOffset = 1;
        else
            colOffset = -1;

        if (getPieceValue(col, row) < 6) {
            checkCellForNonVerticalPartnerBreaks(board, col, row, colOffset, -1);
            checkCellForNonVerticalPartnerBreaks(board, col, row, colOffset, 0);
            checkCellForNonVerticalPartnerBreaks(board, col, row, colOffset, 1);
        }
    }

    void checkCellForNonVerticalPartnerBreaks(BoardEvaluator partner, int col, int row, int _x, int _y) {
        int value = getPieceValue(col, row);

        int matchCount;

        // Tally up matches on this board
        for (matchCount = 1;
             (isCellInBoard(col + matchCount * _x, row + matchCount * _y)
                     && getPieceValue(col + matchCount * _x, row + matchCount * _y) == value
                     && CellEval.hasPowerBlockFlag(cells[row + matchCount * _y][col + matchCount * _x]) == false);
             matchCount++) {
            /* empty *//*
        }

        // If we're moving to partner side
        if (isCellInBoard(col + matchCount * _x, row + matchCount * _y) != true) {
            int partnerMatchCount = 0;
            int pX;

            if (_x > 0)
                pX = -(matchCount * _x);
            else
                pX = 5 - matchCount * _x;

            for (/**/;/*
                     (isCellInBoard(pX + matchCount * _x, row + matchCount * _y)
                             && partner.getPieceValue(pX + matchCount * _x, row + matchCount * _y) == value
                             && CellEval.hasPowerBlockFlag(partner.cells[row + matchCount * _y][pX + matchCount * _x]) == false);
                     matchCount++) {
                partnerMatchCount++;
            }


            if (partnerMatchCount != 0) {
                if (matchCount >= 3) {
                    for (int i = 0; i < matchCount; i++) {
                        int y = row + i * _y;
                        int x = col + i * _x;

                        if (isCellInBoard(x, y)) {
                            int copy = cells[y][x];
                            copy = CellEval.addPartnerBreakFlag(copy);
                            cells[y][x] = copy;
                        } else {
                            x = pX + i * _x;
                            int copy = partner.cells[y][x];
                            copy = CellEval.addPartnerBreakFlag(copy);
                            partner.cells[y][x] = copy;
                        }
                    }
                    updateBoard();
                }
            }
        }
    }

    int getYahooDuration() {
        int duration = 0;
        int horizontal = 2;
        int vertical = 4;
        int diagonal = 3;

        for (int row = 0; row < 13; row++) {
            if (checkForNonVerticalYahoo(row, 0)) {
                duration += horizontal;

                for (int column = 0; column < 6; column++) {
                    cells[row][column] = CellEval.addBrokenFlag(cells[row][column]);
                }
            }
        }

        for (int column = 0; column < 6; column++) {
            for (int row = 0; row < 10; row++) {
                if (CellEval.getCellFlag(cells[row][column]) == 5) {
                    if (CellEval.getCellFlag(cells[row + 1][column]) == 4) {
                        if (CellEval.getCellFlag(cells[row + 2][column]) != 3) {
                            continue;
                        }
                    } else if (CellEval.getCellFlag(cells[row + 1][column]) != 3
                            || CellEval.getCellFlag(cells[row + 2][column]) != 4) {
                        continue;
                    }

                    if (CellEval.getCellFlag(cells[row + 3][column]) == 2
                            && CellEval.getCellFlag(cells[row + 4][column]) == 1
                            && CellEval.getCellFlag(cells[row + 5][column]) == 0) {

                        duration += vertical;

                        for (int i = 0; i < 6; i++) {
                            cells[row + i][column] = CellEval.addBrokenFlag(cells[row + i][column]);
                        }
                    }
                }
            }
        }

        for (int row = 0; row < 8; row++) {
            if (checkForNonVerticalYahoo(row, 1)) {
                duration += diagonal;

                for (int col = 0; col < 6; col++) {
                    cells[row + col][col] = CellEval.addBrokenFlag(cells[row + col][col]);
                }
            }
        }

        for (int row = 5; row < 13; row++) {
            if (checkForNonVerticalYahoo(row, -1)) {
                duration += diagonal;

                for (int col = 0; col < 6; col++) {
                    cells[row - col][col] = CellEval.addBrokenFlag(cells[row - col][col]);
                }
            }
        }

        updateBoard();
        return duration;
    }

    boolean checkForNonVerticalYahoo(int y, int _y) {
        boolean result = true;

        int row = y;

        if (CellEval.getCellFlag(cells[row][0]) != 0) {
            result = false;
        }

        row += _y;

        if (CellEval.getCellFlag(cells[row][1]) != 1) {
            result = false;
        }

        row += _y;

        if (CellEval.getCellFlag(cells[row][2]) != 2) {
            result = false;
        }

        row += _y;

        if (CellEval.getCellFlag(cells[row][3]) == 3) {
            if (CellEval.getCellFlag(cells[row + _y][4]) != 4)
                result = false;
        } else {
            if (CellEval.getCellFlag(cells[row][3]) != 4)
                result = false;
            if (CellEval.getCellFlag(cells[row + _y][4]) != 3)
                result = false;
        }
        row += 2 * _y;
        if (CellEval.getCellFlag(cells[row][5]) != 5)
            result = false;

        return result;
    }

    public void flagPowerBlockCells() {
        for (int row = 0; row < 16; row++) {
            for (int col = 0; col < 6; col++) {
                if (CellEval.hasPowerBlockFlag(cells[row][col])) {
                    cells[row][col] = CellEval.addBrokenFlag(cells[row][col]);
                }
            }
        }
        updateBoard();
    }

    public void flagBoardMatches() {
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 6; j++)
                flagCellForMatches(j, i);
        }
    }

    void flagCellForMatches(int column, int row) {
        if (getPieceValue(column, row) < 6) {
            flagCellForMatches(column, row, -1, 1);
            flagCellForMatches(column, row, 0, 1);
            flagCellForMatches(column, row, 1, 1);
            flagCellForMatches(column, row, 1, 0);
        }
    }

    void flagCellForMatches(int x, int y, int _x, int _y) {
        int cell = getPieceValue(x, y);

        int count;
        for (count = 1;
             (isCellInBoard(x + count * _x, y + count * _y)
                     && getPieceValue(x + count * _x, y + count * _y) == cell
                     && CellEval.hasPowerBlockFlag(cells[y + count * _y][x + count * _x]) == false);
             count++) {
            /* empty *//*
        }

        if (count >= 3) {
            for (int i = 0; i < count; i++) {
                int copy = cells[y + i * _y][x + i * _x];
                copy = CellEval.addBrokenFlag(copy);
                cells[y + i * _y][x + i * _x] = copy;
            }
        }
    }

    public void handlePower(int i) {
        if (CellEval.getPowerFlag(i) == 0) {
            switch (i) {
                case 1024:
                    removeAllPowersFromBoard();
                    break;
                case 1026:
                    removeAllStonesFromBoard();
                    break;
                default:
                    System.out			    .println("Assertion failure: invalid rare attack " + i);
                    break;
            }
        } else {
            boolean isOffensive = CellEval.isOffensive(i);
            int level = CellEval.getPowerLevel(i);

            switch (CellEval.getCellFlag(i)) {
                case 0:
                    if (isOffensive) {
                        addRow(1);
                    } else {
                        removeRow(1);
                    }

                    break;
                case 1:
                    if (isOffensive) {
                        dither(2 * Math.min(level, 3));
                    } else {
                        clump(2 * Math.min(level, 3));
                    }

                    break;
                case 2:
                    if (isOffensive) {
                        addStone(Math.min(level, 3));
                    } else {
                        dropStone(Math.min(level, 3));
                    }

                    break;
                case 3:
                    if (isOffensive) {
                        defuse(Math.min(level, 3));
                    } else {
                        colorBlast();
                        break;
                    }

                    break;
                case 4:
                    System.out.println
                            ("Assertion failure: invalid CELL5 board attack " + i);
                    break;
                case 5:
                    removeColorFromBoard();
                    break;
                default:
                    System.out.println("Assertion failure: invalid attack" + i);
            }
        }
    }

    void addRow(int amount) {
        for (int row = 13; row >= amount; row--) {
            for (int col = 0; col < 6; col++)
                cells[row][col] = cells[row - amount][col];
        }

        for (int row = 0; row < amount; row++) {
            for (int col = 0; col < 6; col++)
                cells[row][col] = 6;
        }

        int hash = getBoardMakeupHash();

        for (int i = amount - 1; i >= 0; i--) {
            for (int col = 0; col < 6; col++) {
                int value = getNonAdjacentCell(col, i, hash + i + col);

                value = CellEval.setIDFlag(value, incrementID());

                if (value < 0) {
                    System.out.println
                            ("Assertion failure: unable to find non-adjacent cell "
                                    + col + "," + i);
                }

                cells[i][col] = value;
            }
        }
        updateBoard();
    }

    void removeRow(int amount) {
        for (int row = 0; row < 13 - amount; row++) {
            for (int col = 0; col < 6; col++) {
                if (row < amount && CellEval.getCellFlag(cells[row][col]) < 6)
                    releaseID(CellEval.getID(cells[row][col]));

                cells[row][col] = cells[row + amount][col];
            }
        }

        for (int row = 13 - amount; row < 13; row++) {
            for (int col = 0; col < 6; col++) {
                cells[row][col] = 6;
            }
        }

        updateBoard();
    }

    void shuffleColumnIndices() {
        for (int i = 0; i < 6; i++) {
            randomColumnIndices[i] = i;
        }

        RandomNumber generator = new RandomNumber((long) getBoardMakeupHash());

        for (int i = 0; i < 10; i++) {
            int first = generator.next(6);
            int second = generator.next(6);
            int value = randomColumnIndices[first];
            randomColumnIndices[first] = randomColumnIndices[second];
            randomColumnIndices[second] = value;
        }
    }

    void addStone(int amount) {
        shuffleColumnIndices();

        for (int i = 0; i < amount; i++) {
            int x = randomColumnIndices[i];

            for (int y = 0; y < 13; y++) {
                if (getPieceValue(x, y) == 6) {
                    cells[y][x] = 7;
                    break;
                }
            }
        }

        updateBoard();
    }

    void dropStone(int amount) {
        int count = 0;

        for (int y = 12; y >= 0; y--) {
            for (int x = 0; x < 6; x++) {
                if (getPieceValue(x, y) == 7) {
                    for (int i = y; i >= 1; i--) {
                        cells[i][x] = cells[i - 1][x];
                    }

                    cells[0][x] = 7;

                    if (++count == amount) {
                        return;
                    }
                }
            }
        }

        updateBoard();
    }

    void markColorBlast() {
        // Clear the grid
        for (int row = 0; row < 16; row++) {
            for (int col = 0; col < 6; col++) {
                colorBlastGrid[row][col] = false;
            }
        }

        // loop through cells
        for (int row = 0; row < 16; row++) {
            for (int col = 0; col < 6; col++) {

                // if the piece is purple
                if (CellEval.getCellFlag(cells[row][col]) == 3
                        // And the piece is a power
                        && CellEval.getPowerFlag(cells[row][col]) != 0) {

                    if (isCellInBoard(col - 1, row - 1))
                        colorBlastGrid[row - 1][col - 1] = true;
                    if (isCellInBoard(col, row - 1))
                        colorBlastGrid[row - 1][col] = true;
                    if (isCellInBoard(col + 1, row - 1))
                        colorBlastGrid[row - 1][col + 1] = true;
                    if (isCellInBoard(col - 1, row))
                        colorBlastGrid[row][col - 1] = true;
                    if (isCellInBoard(col, row))
                        colorBlastGrid[row][col] = true;
                    if (isCellInBoard(col + 1, row))
                        colorBlastGrid[row][col + 1] = true;
                    if (isCellInBoard(col - 1, row + 1))
                        colorBlastGrid[row + 1][col - 1] = true;
                    if (isCellInBoard(col, row + 1))
                        colorBlastGrid[row + 1][col] = true;
                    if (isCellInBoard(col + 1, row + 1))
                        colorBlastGrid[row + 1][col + 1] = true;
                }
            }
        }

        // clear the cell pieces start in
        colorBlastGrid[15][2] = false;
    }

    boolean isColorBlastGridEmpty() {
        for (int row = 0; row < 16; row++) {
            for (int col = 0; col < 6; col++) {
                if (colorBlastGrid[row][col]) {
                    return false;
                }
            }
        }

        return true;
    }

    //    int[] pushRowOrder = { 0, 1, 2, 2, 1, 0 };
    //    int[] pushColumnOrder = { 2, 3, 1, 4, 0, 5 };

    void pushCellToBottomOfBoard(int value) {
        for (int y = -2; y < 8; y++) {
            for (int x = 0; x < 6; x++) {
                int col = pushColumnOrder[x];
                int row = y + pushRowOrder[col];

                if (row >= 0 && CellEval.getCellFlag(cells[row][col]) != 7) {
                    if (CellEval.getCellFlag(cells[row][col]) < 6) {
                        releaseID(CellEval.getID(cells[row][col]));
                    }

                    cells[row][col] = CellEval.setIDFlag(value, incrementID());
                    return;
                }
            }
        }

        updateBoard();
    }

    void colorBlast() {
        markColorBlast();

        if (isColorBlastGridEmpty())
            pushCellToBottomOfBoard(CellEval.setPowerFlag(3, 2));
        else {
            for (int row = 0; row < 16; row++) {
                for (int col = 0; col < 6; col++) {
                    if (colorBlastGrid[row][col]) {
                        if (CellEval.getCellFlag(cells[row][col]) < 6) {
                            releaseID(CellEval.getID(cells[row][col]));
                        }

                        cells[row][col] = CellEval.setIDFlag(3, incrementID());
                    }
                }
            }

            for (int col = 0; col < 6; col++) {
                boolean bool = false;

                for (int row = 15; row >= 0; row--) {
                    if (CellEval.getCellFlag(cells[row][col]) == 6) {
                        if (bool) {
                            cells[row][col] = CellEval.setIDFlag(3, incrementID());
                        }
                    } else {
                        bool = true;
                    }
                }
            }
        }
        updateBoard();
    }

    void defuse(int intensity) {
        int cellsDefused = 0;

        for (int row = 0; row < 16; row++) {
            for (int col = 0; col < 6; col++) {
                if (CellEval.getCellFlag(cells[row][col]) == 3
                        && CellEval.getPowerFlag(cells[row][col]) != 0) {

                    if (CellEval.getCellFlag(cells[row][col]) < 6)
                        releaseID(CellEval.getID(cells[row][col]));

                    if (CellEval.getCellFlag(cells[row][col]) != 6)
                        cells[row][col] = 7;

                    if (++cellsDefused == intensity)
                        return;
                }
            }
        }

        updateBoard();

        for (int i = cellsDefused; i < intensity; i++) {
            pushCellToBottomOfBoard(CellEval.setPowerFlag(3, 3));
        }
    }

    void removeColorFromBoard() {
        // Clear the count
        for (int i = 0; i < countOfPieces.length; i++) {
            countOfPieces[i] = 0;
        }

        // Loop through the board and tally up count of cells
        for (int row = 0; row < 16; row++) {
            for (int col = 0; col < 6; col++) {
                int value = CellEval.getCellFlag(cells[row][col]);

                if (value < 6) {
                    countOfPieces[value]++;
                }
            }
        }

        int index = 0;

        // Loop left to right
        for (int i = 0; i < countOfPieces.length; i++) {
            // If there is a color move the index to the farthest right
            if (countOfPieces[i] > 0) {
                index = i;
            }
        }

        // Loop left to right
        for (int i = 0; i < countOfPieces.length; i++) {
            // Select the color with the fewest amount of cells
            if (countOfPieces[i] > 0 && countOfPieces[i] < countOfPieces[index]) {
                index = i;
            }
        }

        boolean colorRemoved = false;

        for (int row = 0; row < 16; row++) {
            for (int col = 0; col < 6; col++) {
                if (CellEval.getCellFlag(cells[row][col]) == index) {
                    cells[row][col] = CellEval.addBrokenFlag(cells[row][col]);
                    colorRemoved = true;
                }
            }
        }

        if (colorRemoved) {
            updateBoard();
            handleBrokenCellDrops();
        }
    }

    public void addRemovedPowersToBoard(IntStack powers) {
        shuffleColumnIndices();

        int count = 0;

        while (powers.length() > 0) {
            int value = powers.getValueAt(0);

            powers.removeAt(0);

            int i;

            for (i = count; i != (count + 6 - 1) % 6; i = (i + 1) % 6) {
                int col = randomColumnIndices[i];

                if (cells[12][col] == 6) {
                    for (int row = 15; row >= 1; row--) {
                        cells[row][col] = cells[row - 1][col];
                    }

                    cells[0][col] = value;

                    if (hasFullMatchInProximity(col, 0) == false) {
                        cells[0][col] = CellEval.setIDFlag(value, incrementID());
                        break;
                    }

                    for (int row = 0; row < 15; row++) {
                        cells[row][col] = cells[row + 1][col];
                    }

                    cells[15][col] = 6;
                }
            }

            count = (i + 1) % 6;
        }

        updateBoard();
    }

    void dither(int intensity) {
        int num = 0;

        for (int row = 12; row >= 0; row--) {
            for (int col = 0; col < 6; col++) {
                boolean bool = unmatchCell(col, row);

                if (bool && ++num == intensity)
                    return;
            }
        }
    }

    boolean unmatchCell(int col, int row) {
        boolean successfulSwap = false;

        // If there's a matching cell nearby
        if (hasMatchingCellInProximity(col, row)) {
            // Loop through the board
            for (int r = 0; r < 13 && !successfulSwap; r++) {
                for (int c = 0; c < 6; c++) {
                    if (getPieceValue(c, r) < 6) {
                        int swap = cells[r][c];

                        // Swap passed cell with another on board
                        cells[r][c] = cells[row][col];
                        cells[row][col] = swap;

                        // If both no longer have cells nearby, it's success
                        if (hasMatchingCellInProximity(col, row) == false
                                && hasMatchingCellInProximity(c, r) == false) {
                            successfulSwap = true;
                            break;
                        }

                        // Undo the swap
                        swap = cells[r][c];
                        cells[r][c] = cells[row][col];
                        cells[row][col] = swap;
                    }
                }
            }
        }

        updateBoard();
        return successfulSwap;
    }

    void clump(int numberOfCellsToChange) {
        int swapCount = 0;
        // start from the top and work way down
        for (int row = 12; row >= 0; row--) {
            for (int col = 0; col < 6; col++) {
                if (getPieceValue(col, row) < 6
                        // If there's not a matching cell nearby
                        && hasMatchingCellInProximity(col, row) == false) {

                    boolean successfulSwap = false;

                    // Start from the bottom and work up
                    for (int y = 0; y < 13 && !successfulSwap; y++) {
                        for (int x = 0; x < 6; x++) {
                            if (getPieceValue(x, y) < 6) {
                                int copy = cells[y][x];

                                // swap the two cells
                                cells[y][x] = cells[row][col];
                                cells[row][col] = copy;

                                // If one of the cells now has a matching cell nearby
                                if ((hasMatchingCellInProximity(col, row) || hasMatchingCellInProximity(x, y))
                                        // and not a full match, since that would be too significant of a change
                                        && hasFullMatchInProximity(col, row) == false
                                        && hasFullMatchInProximity(x, y) == false) {
                                    // the swap is then successful then break and add to changed pieces
                                    successfulSwap = true;
                                    break;
                                }

                                // undo the swap
                                copy = cells[y][x];
                                cells[y][x] = cells[row][col];
                                cells[row][col] = copy;
                            }
                        }
                    }

                    updateBoard();

                    if (successfulSwap && ++swapCount == numberOfCellsToChange)
                        return;
                }
            }
        }
    }

    int getNonAdjacentCell(int x, int y, int hash) {
        for (int i = 0; i < cellMatches.length; i++) {
            cellMatches[i] = false;
        }

        cellMatches[getSafeCell(x - 1, y)] = true;
        cellMatches[getSafeCell(x - 1, y + 1)] = true;
        cellMatches[getSafeCell(x, y + 1)] = true;
        cellMatches[getSafeCell(x + 1, y + 1)] = true;
        cellMatches[getSafeCell(x + 1, y)] = true;
        cellMatches[getSafeCell(x + 1, y - 1)] = true;
        cellMatches[getSafeCell(x, y - 1)] = true;
        cellMatches[getSafeCell(x - 1, y - 1)] = true;

        int id = hash % cellHashes.length;
        int value = cellHashes[id];

        for (int col = 0; col < 6; col++) {
            if (cellMatches[(cellIndices[id] + value * col) % 6] == false) {
                return (cellIndices[id] + value * col) % 6;
            }
        }

        return -1;
    }

    //int[] colS = { -1, -1, 0, 1, 1,  1,  0, -1 };
    //int[] rowS = {  0,  1, 1, 1, 0, -1, -1, -1 };

    boolean hasMatchingCellInProximity(int col, int row) {
        int value = getPieceValue(col, row);

        if (value >= 6) return false;

        for (int i = 0; i < columnMatchLookup.length; i++) {
            if (getSafeCell(col + columnMatchLookup[i], row + rowMatchLookup[i]) == value)
                return true;
        }

        return false;
    }

    boolean hasFullMatchInProximity(int x, int y) {
        int value = getPieceValue(x, y);

        if (value < 6) {
            // x = 2, y = 4

            // x + -1, y + 0 = 1, 4
            // x - -1, y + 0 = 3, 4

            // x + -1, y + 0 = 1, 4
            // x + -2, y + 0 = 0, 4

            for (int i = 0; i < columnMatchLookup.length; i++) {
                // If there's a match like XXX
                if (getSafeCell(x + columnMatchLookup[i], y + rowMatchLookup[i]) == value
                        && getSafeCell(x - columnMatchLookup[i], y - rowMatchLookup[i]) == value)
                    return true;

                // If there's a a match like XXOX
                if (getSafeCell(x + columnMatchLookup[i], y + rowMatchLookup[i]) == value
                        && getSafeCell(x + 2 * columnMatchLookup[i], y + 2 * rowMatchLookup[i]) == value)
                    return true;

                // If there's a match like XXOX
                if (getSafeCell(x - columnMatchLookup[i], y - rowMatchLookup[i]) == value
                        && getSafeCell(x - 2 * columnMatchLookup[i], y - 2 * rowMatchLookup[i]) == value)
                    return true;
            }
        }

        return false;
    }

    public int getSafeCell(int col, int row) {
        if (isCellInBoard(col, row) == false)
            return 6;

        int value = getPieceValue(col, row);

        if (value >= 6) {
            value = 6;
        }

        return value;
    }

    void removeAllPowersFromBoard() {
        for (int row = 0; row < 13; row++) {
            for (int col = 0; col < 6; col++) {
                int value = cells[row][col];

                if (CellEval.getPowerFlag(value) != 0) {
                    cells[row][col] = CellEval.setPowerFlag(value, 0);
                }
            }
        }

        updateBoard();
    }

    void removeAllStonesFromBoard() {
        for (int x = 0; x < 6; x++) {
            int row = 0;

            for (int y = 0; y < 16; y++) {
                if (CellEval.getCellFlag(cells[y][x]) != 7) {
                    cells[row][x] = cells[y][x];
                    row++;
                }
            }

            for (; row < 16; row++)
                cells[row][x] = 6;
        }
        updateBoard();
    }

    int getBoardMakeupHash() {
        int num = 999;

        for (int row = 0; row < 13; row++) {
            for (int col = 0; col < 6; col++) {
                num += CellEval.removePartnerBreakFlag(cells[row][col]) * (row * 6 + col);
            }
        }

        return num;
    }

    private void updateBoard() {
        if ( brd != null )
            brd.update();
    }
}









 */