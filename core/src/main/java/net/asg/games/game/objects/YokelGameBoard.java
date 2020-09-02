package net.asg.games.game.objects;

import com.badlogic.gdx.utils.Queue;

import net.asg.games.utils.RandomUtil;
import net.asg.games.utils.YokelUtilities;

import java.util.Stack;
import java.util.Vector;

public class YokelGameBoard extends AbstractYokelObject {
    public static final int MAX_RANDOM_BLOCK_NUMBER = 2048;
    public static final int MAX_COLS = 6;
    public static final int MAX_ROWS = 16;
    public static final int MAX_PLAYABLE_ROWS = MAX_ROWS - 3;
    public static final int HORIZONTAL_HOO_TIME = 2;
    public static final int VERTICAL_HOO_TIME = 4;
    public static final int DIAGONAL_HOO_TIME = 3;
    //public static final float FALL_RATE = 0.346f;
    public static final float FALL_RATE = 0.026f;
    public static final float FAST_FALL_RATE = 0.266f;
    private static final int MAX_FALL_VALUE = 1;

    private final YokelPiece MEDUSA_PIECE = new YokelPiece(0, YokelBlock.MEDUSA, YokelBlock.MEDUSA, YokelBlock.MEDUSA);
    private final YokelPiece MIDAS_PIECE = new YokelPiece(0, YokelBlock.BOT_MIDAS, YokelBlock.MID_MIDAS, YokelBlock.TOP_MIDAS);

    private int[][] cells;
    private boolean[] ids;
    private int idIndex;
    private static int[] targetRows = new int[MAX_COLS];
    private int[] randomColumnIndices = new int[MAX_COLS];
    private boolean[][] colorBlastGrid
            = { new boolean[MAX_COLS],
            new boolean[MAX_COLS],
            new boolean[MAX_COLS],
            new boolean[MAX_COLS],
            new boolean[MAX_COLS],
            new boolean[MAX_COLS],
            new boolean[MAX_COLS],
            new boolean[MAX_COLS],
            new boolean[MAX_COLS],
            new boolean[MAX_COLS],
            new boolean[MAX_COLS],
            new boolean[MAX_COLS],
            new boolean[MAX_COLS],
            new boolean[MAX_COLS],
            new boolean[MAX_COLS],
            new boolean[MAX_COLS] };
    private int[] pushRowOrder = { 0, 1, 2, 2, 1, 0};
    private int[] pushColumnOrder = { 2, 3, 1, 4, 0, 5};
    private int[] countOfPieces = new int[MAX_COLS];

    private boolean[] cellMatches = new boolean[7];
    private int[] cellIndices = { 0, 1, 2,  3,  4,  5, 6};
    private int[] cellHashes = { 5, 25, 7, 49, 35, 19, 23};

    private int[] columnMatchLookup = { -1, -1, 0, 1, 1, 1, 0, -1};
    private int[] rowMatchLookup = { 0, 1, 1, 1, 0, -1, -1, -1};

    private YokelPiece piece;
    private YokelPiece nextPiece;

    private float fallNumber;
    private RandomUtil.RandomNumberArray nextBlocks;
    private int currentBlockPointer = -1;
    private boolean fastDown;
    private Queue<Integer> powers;
    private int[] countOfBreaks = new int[MAX_COLS];
    private int[] powersKeep = new int[MAX_COLS];
    private int yahooDuration = 0;
    private boolean hasGameStarted;

    private Queue<Integer> specialPieces;
    private boolean brokenCellsHandled = true;
    private boolean isPieceSet = false;

    //Empty Constructor required for Json.Serializable
    public YokelGameBoard(){}

    public YokelGameBoard(long seed){
        cells = new int[MAX_ROWS][MAX_COLS];
        ids = new boolean[128];
        piece = null;
        fallNumber = MAX_FALL_VALUE;
        powers = new Queue<>();
        specialPieces = new Queue<>();
        reset(seed);
    }

    public void reset(long seed){
        nextBlocks = new RandomUtil.RandomNumberArray(MAX_RANDOM_BLOCK_NUMBER, seed, MAX_COLS);
        clearBoard();
    }

    @Override
    public void dispose() {
        specialPieces.clear();
        powers.clear();
        piece.dispose();
        nextPiece.dispose();
        MEDUSA_PIECE.dispose();
        MIDAS_PIECE.dispose();
    }

    public void begin(){
        if(!hasGameStarted){
            getNewNextPiece();
            hasGameStarted = true;
        }
    }

    public boolean hasGameStarted(){
        return hasGameStarted;
    }

    public void end(){
        hasGameStarted = false;
    }

    public YokelPiece getNextPiece(){
        return nextPiece;
    }

    public int[][] getCells(){
        return cells;
    }

    public void setCell(int row, int col, int cell){
        cells[row][col] = cell;
    }

    public int getPieceValue(int c, int r){
        return YokelBlockEval.getCellFlag(cells[r][c]);
    }

    public int getBlockValueAt(int column, int row) {
        return cells[row][column];
    }

    public void clearBoard() {
        for (int c = 0; c < MAX_COLS; c++) {
            for (int r = 0; r < MAX_ROWS; r++){
                clearCell(r,c);
            }
        }
        for (int i = 0; i < 128; i++){
            ids[i] = false;
        }
        idIndex = 0;
    }
    
    private static boolean isCellInBoard(int c, int r) {
        return c >= 0 && c < MAX_COLS && r >= 0 && r < MAX_ROWS;
    }

    public int getSafeCell(int c, int r) {
        if (!isCellInBoard(c, r))
            return MAX_COLS;

        int value = getPieceValue(c, r);

        if (value >= MAX_COLS) {
            value = MAX_COLS;
        }

        return value;
    }

    int incrementID() {
        do {
            idIndex++;

            if (idIndex == ids.length)
                idIndex = 0;
        } while (ids[idIndex]);

        ids[idIndex] = true;
        return idIndex;
    }

    void releaseID(int index) {
        if (!ids[index])
            System.out.println("Assertion failure: id " + index
                    + " released but not held");

        ids[index] = false;
    }

    public boolean isArtificiallyAdded(int column, int row) {
        return YokelBlockEval.hasAddedByYahooFlag(cells[row][column]);
    }

    public boolean isCellBroken(int column, int row) {
        return YokelBlockEval.hasBrokenFlag(cells[row][column]);
    }

    public void setValueWithID(int column, int row, int value) {
        cells[row][column] = YokelBlockEval.setIDFlag(value, incrementID());
    }

    void addRow(int amount) {
        for (int row = MAX_PLAYABLE_ROWS; row >= amount; row--) {
            for (int col = 0; col < MAX_COLS; col++)
                cells[row][col] = cells[row - amount][col];
        }

        for (int row = 0; row < amount; row++) {
            for (int col = 0; col < MAX_COLS; col++)
                cells[row][col] = MAX_COLS;
        }

        int hash = getBoardMakeupHash();

        for (int i = amount - 1; i >= 0; i--) {
            for (int col = 0; col < MAX_COLS; col++) {
                int value = getNonAdjacentCell(col, i, hash + i + col);

                value = YokelBlockEval.setIDFlag(value, incrementID());

                if (value < 0) {
                    System.out.println("Assertion failure: unable to find non-adjacent cell " + col + "," + i);
                }

                cells[i][col] = value;
            }
        }
       updateBoard();
    }

    void removeRow(int amount) {
        for (int row = 0; row < MAX_PLAYABLE_ROWS - amount; row++) {
            for (int col = 0; col < MAX_COLS; col++) {
                if (row < amount && YokelBlockEval.getCellFlag(cells[row][col]) < MAX_COLS)
                    releaseID(YokelBlockEval.getID(cells[row][col]));

                cells[row][col] = cells[row + amount][col];
            }
        }

        for (int row = MAX_PLAYABLE_ROWS - amount; row < MAX_PLAYABLE_ROWS; row++) {
            for (int col = 0; col < MAX_COLS; col++) {
                cells[row][col] = MAX_COLS;
            }
        }

        updateBoard();
    }

    void shuffleColumnIndices() {
        for (int i = 0; i < MAX_COLS; i++) {
            randomColumnIndices[i] = i;
        }

        RandomUtil.RandomNumber generator = new RandomUtil.RandomNumber((long) getBoardMakeupHash());

        for (int i = 0; i < 10; i++) {
            int first = generator.next(MAX_COLS);
            int second = generator.next(MAX_COLS);
            int value = randomColumnIndices[first];
            randomColumnIndices[first] = randomColumnIndices[second];
            randomColumnIndices[second] = value;
        }
    }

    void addStone(int amount) {
        shuffleColumnIndices();

        for (int i = 0; i < amount; i++) {
            int x = randomColumnIndices[i];

            for (int y = 0; y < MAX_PLAYABLE_ROWS; y++) {
                if (getPieceValue(x, y) == MAX_COLS) {
                    cells[y][x] = YokelBlock.STONE;
                    break;
                }
            }
        }

        updateBoard();
    }

    void dropStone(int amount) {
        int count = 0;

        for (int y = 12; y >= 0; y--) {
            for (int x = 0; x < MAX_COLS; x++) {
                if (getPieceValue(x, y) == YokelBlock.STONE) {
                    for (int i = y; i >= 1; i--) {
                        cells[i][x] = cells[i - 1][x];
                    }

                    cells[0][x] = YokelBlock.STONE;

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
        for (int row = 0; row < MAX_ROWS; row++) {
            for (int col = 0; col < MAX_COLS; col++) {
                colorBlastGrid[row][col] = false;
            }
        }

        // loop through cells
        for (int row = 0; row < MAX_ROWS; row++) {
            for (int col = 0; col < MAX_COLS; col++) {

                // if the piece is purple
                if (YokelBlockEval.getCellFlag(cells[row][col]) == YokelBlock.Op_BLOCK
                        // And the piece is a power
                        && YokelBlockEval.getPowerFlag(cells[row][col]) != 0) {

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
        for (int row = 0; row < MAX_ROWS; row++) {
            for (int col = 0; col < MAX_COLS; col++) {
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
            for (int x = 0; x < MAX_COLS; x++) {
                int col = pushColumnOrder[x];
                int row = y + pushRowOrder[col];

                if (row >= 0 && YokelBlockEval.getCellFlag(cells[row][col]) != YokelBlock.STONE) {
                    if (YokelBlockEval.getCellFlag(cells[row][col]) < MAX_COLS) {
                        releaseID(YokelBlockEval.getID(cells[row][col]));
                    }

                    cells[row][col] = YokelBlockEval.setIDFlag(value, incrementID());
                    return;
                }
            }
        }

        updateBoard();
    }

    void colorBlast() {
        markColorBlast();

        if (isColorBlastGridEmpty()){
            pushCellToBottomOfBoard(YokelBlockEval.addPowerBlockFlag(YokelBlockEval.setPowerFlag(YokelBlock.Op_BLOCK, YokelBlock.DEFENSIVE_MINOR)));
        } else {
            for (int row = 0; row < MAX_ROWS; row++) {
                for (int col = 0; col < MAX_COLS; col++) {
                    if (colorBlastGrid[row][col]) {
                        if (YokelBlockEval.getCellFlag(cells[row][col]) < MAX_COLS) {
                            releaseID(YokelBlockEval.getID(cells[row][col]));
                        }

                        cells[row][col] = YokelBlockEval.setIDFlag(YokelBlock.Op_BLOCK, incrementID());
                    }
                }
            }

            for (int col = 0; col < MAX_COLS; col++) {
                boolean bool = false;

                for (int row = 15; row >= 0; row--) {
                    if (YokelBlockEval.getCellFlag(cells[row][col]) == MAX_COLS) {
                        if (bool) {
                            cells[row][col] = YokelBlockEval.setIDFlag(YokelBlock.Op_BLOCK, incrementID());
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

        for (int row = 0; row < MAX_ROWS; row++) {
            for (int col = 0; col < MAX_COLS; col++) {
                if (YokelBlockEval.getCellFlag(cells[row][col]) == YokelBlock.Op_BLOCK
                        && YokelBlockEval.getPowerFlag(cells[row][col]) != YokelBlock.Y_BLOCK) {

                    if (YokelBlockEval.getCellFlag(cells[row][col]) < MAX_COLS)
                        releaseID(YokelBlockEval.getID(cells[row][col]));

                    if (YokelBlockEval.getCellFlag(cells[row][col]) != MAX_COLS)
                        cells[row][col] = YokelBlock.STONE;

                    if (++cellsDefused == intensity)
                        return;
                }
            }
        }

        updateBoard();

        for (int i = cellsDefused; i < intensity; i++) {
            pushCellToBottomOfBoard(YokelBlockEval.addPowerBlockFlag(YokelBlockEval.setPowerFlag(YokelBlock.Op_BLOCK, YokelBlock.OFFENSIVE_MINOR)));
        }
    }

    void removeColorFromBoard() {
        // Clear the count
        for (int i = 0; i < countOfPieces.length; i++) {
            countOfPieces[i] = 0;
        }

        // Loop through the board and tally up count of cells
        for (int row = 0; row < MAX_ROWS; row++) {
            for (int col = 0; col < MAX_COLS; col++) {
                int value = YokelBlockEval.getCellFlag(cells[row][col]);

                if (value < MAX_COLS) {
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

        for (int row = 0; row < MAX_ROWS; row++) {
            for (int col = 0; col < MAX_COLS; col++) {
                if (YokelBlockEval.getCellFlag(cells[row][col]) == index) {
                    cells[row][col] = YokelBlockEval.addBrokenFlag(cells[row][col]);
                    colorRemoved = true;
                }
            }
        }

        if (colorRemoved) {
            updateBoard();
            handleBrokenCellDrops();
        }
    }

    public void handleBrokenCellDrops() {
        for (int col = 0; col < MAX_COLS; col++) {
            int index = 0;

            for (int row = 0; row < MAX_ROWS; row++) {
                if (isCellBroken(col, row)) {
                    if (YokelBlockEval.getCellFlag(cells[row][col]) < MAX_COLS)
                        releaseID(YokelBlockEval.getID(cells[row][col]));
                } else {
                    cells[index][col] = cells[row][col];
                    index++;
                }
            }

            for (; index < MAX_ROWS; index++){
                cells[index][col] = MAX_COLS;
            }
        }
        brokenCellsHandled = true;
        updateBoard();
    }

    public void flagPowerBlockCells() {
        for (int row = 0; row < MAX_ROWS; row++) {
            for (int col = 0; col < MAX_COLS; col++) {
                if (YokelBlockEval.hasPowerBlockFlag(cells[row][col])) {
                    cells[row][col] = YokelBlockEval.addBrokenFlag(cells[row][col]);
                }
            }
        }
        updateBoard();
    }

    public void flagBoardMatches() {
        for (int i = 0; i < MAX_ROWS; i++) {
            for (int j = 0; j < MAX_COLS; j++)
                flagCellForMatches(j, i);
        }
    }

    void flagCellForMatches(int column, int row) {
        if (getPieceValue(column, row) < MAX_COLS) {
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
                     && getPieceValue(x + count * _x, y + count * _y) == cell);
                     //&& YokelBlockEval.hasPowerBlockFlag(cells[y + count * _y][x + count * _x]) == false);
             count++) {
            /* empty */
        }

        if (count >= 3) {
            for (int i = 0; i < count; i++) {
                int copy = cells[y + i * _y][x + i * _x];
                copy = YokelBlockEval.addBrokenFlag(copy);
                cells[y + i * _y][x + i * _x] = copy;
            }
        }
    }

    public void handlePower(int i) {
        if (YokelBlockEval.getPowerFlag(i) == 0) {
            switch (i) {
                case YokelBlock.SPECIAL_BLOCK_1:
                    removeAllPowersFromBoard();
                    break;
                case YokelBlock.SPECIAL_BLOCK_2:
                    removeAllStonesFromBoard();
                    break;
                default:
                    System.out.println("Assertion failure: invalid rare attack " + i);
                    break;
            }
        } else {
            boolean isOffensive = YokelBlockEval.isOffensive(i);
            int level = YokelBlockEval.getPowerLevel(i);

            switch (YokelBlockEval.getCellFlag(i)) {
                //Y
                case YokelBlock.Y_BLOCK:
                    if (isOffensive) {
                        addRow(1);
                    } else {
                        removeRow(1);
                    }

                    break;
                case YokelBlock.A_BLOCK:
                    if (isOffensive) {
                        dither(2 * Math.min(level, 3));
                    } else {
                        clump(2 * Math.min(level, 3));
                    }

                    break;
                case YokelBlock.H_BLOCK:
                    if (isOffensive) {
                        addStone(Math.min(level, 3));
                    } else {
                        dropStone(Math.min(level, 3));
                    }

                    break;
                case YokelBlock.Op_BLOCK:
                    if (isOffensive) {
                        defuse(Math.min(level, 3));
                    } else {
                        colorBlast();
                        break;
                    }

                    break;
                case YokelBlock.Oy_BLOCK:
                    System.out.println ("Assertion failure: invalid CELL5 board attack " + i);
                    break;
                case YokelBlock.EX_BLOCK:
                    removeColorFromBoard();
                    break;
                default:
                    System.out.println("Assertion failure: invalid attack" + i);
            }
        }
    }

    public void addRemovedPowersToBoard(Stack<Integer> powers) {
        shuffleColumnIndices();

        int count = 0;

        while (powers.size() > 0) {
            int value = powers.pop();

            //powers.removeAt(0);

            int i;

            for (i = count; i != (count + MAX_COLS - 1) % MAX_COLS; i = (i + 1) % MAX_COLS) {
                int col = randomColumnIndices[i];

                if (cells[12][col] == MAX_COLS) {
                    for (int row = 15; row >= 1; row--) {
                        cells[row][col] = cells[row - 1][col];
                    }

                    cells[0][col] = value;

                    if (!hasFullMatchInProximity(col, 0)) {
                        cells[0][col] = YokelBlockEval.setIDFlag(value, incrementID());
                        break;
                    }

                    for (int row = 0; row < 15; row++) {
                        cells[row][col] = cells[row + 1][col];
                    }

                    cells[15][col] = MAX_COLS;
                }
            }

            count = (i + 1) % MAX_COLS;
        }

        updateBoard();
    }

    void dither(int intensity) {
        int num = 0;

        for (int row = 12; row >= 0; row--) {
            for (int col = 0; col < MAX_COLS; col++) {
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
            for (int r = 0; r < MAX_PLAYABLE_ROWS && !successfulSwap; r++) {
                for (int c = 0; c < MAX_COLS; c++) {
                    if (getPieceValue(c, r) < MAX_COLS) {
                        int swap = cells[r][c];

                        // Swap passed cell with another on board
                        cells[r][c] = cells[row][col];
                        cells[row][col] = swap;

                        // If both no longer have cells nearby, it's success
                        if (!hasMatchingCellInProximity(col, row)
                                && !hasMatchingCellInProximity(c, r)) {
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
            for (int col = 0; col < MAX_COLS; col++) {
                if (getPieceValue(col, row) < MAX_COLS
                        // If there's not a matching cell nearby
                        && !hasMatchingCellInProximity(col, row)) {

                    boolean successfulSwap = false;

                    // Start from the bottom and work up
                    for (int y = 0; y < MAX_PLAYABLE_ROWS && !successfulSwap; y++) {
                        for (int x = 0; x < MAX_COLS; x++) {
                            if (getPieceValue(x, y) < MAX_COLS) {
                                int copy = cells[y][x];

                                // swap the two cells
                                cells[y][x] = cells[row][col];
                                cells[row][col] = copy;

                                // If one of the cells now has a matching cell nearby
                                if ((hasMatchingCellInProximity(col, row) || hasMatchingCellInProximity(x, y))
                                        // and not a full match, since that would be too significant of a change
                                        && !hasFullMatchInProximity(col, row)
                                        && !hasFullMatchInProximity(x, y)) {
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

                    if (successfulSwap && ++swapCount == numberOfCellsToChange){
                        return;
                    }
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

        for (int col = 0; col < MAX_COLS; col++) {
            if (!cellMatches[(cellIndices[id] + value * col) % MAX_COLS]) {
                return (cellIndices[id] + value * col) % MAX_COLS;
            }
        }

        return -1;
    }

    //int[] colS = { -1, -1, 0, 1, 1,  1,  0, -1 };
    //int[] rowS = {  0,  1, 1, 1, 0, -1, -1, -1 };

    boolean hasMatchingCellInProximity(int col, int row) {
        int value = getPieceValue(col, row);

        if (value >= MAX_COLS) return false;

        for (int i = 0; i < columnMatchLookup.length; i++) {
            if (getSafeCell(col + columnMatchLookup[i], row + rowMatchLookup[i]) == value)
                return true;
        }

        return false;
    }

    boolean hasFullMatchInProximity(int x, int y) {
        int value = getPieceValue(x, y);

        if (value < MAX_COLS) {
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

    void removeAllPowersFromBoard() {
        for (int row = 0; row < MAX_PLAYABLE_ROWS; row++) {
            for (int col = 0; col < MAX_COLS; col++) {
                int value = cells[row][col];

                if (YokelBlockEval.getPowerFlag(value) != 0) {
                    cells[row][col] = YokelBlockEval.setPowerFlag(value, 0);
                }
            }
        }
        updateBoard();
    }

    void removeAllStonesFromBoard() {
        for (int x = 0; x < MAX_COLS; x++) {
            int row = 0;

            for (int y = 0; y < MAX_ROWS; y++) {
                if (YokelBlockEval.getCellFlag(cells[y][x]) != 7) {
                    cells[row][x] = cells[y][x];
                    row++;
                }
            }

            for (; row < MAX_ROWS; row++)
                cells[row][x] = MAX_COLS;
        }
        updateBoard();
    }

    int getBoardMakeupHash() {
        int num = 999;

        for (int row = 0; row < MAX_PLAYABLE_ROWS; row++) {
            for (int col = 0; col < MAX_COLS; col++) {
                num += YokelBlockEval.removePartnerBreakFlag(cells[row][col]) * (row * MAX_COLS + col);
            }
        }

        return num;
    }

    public int getColumnFill(int column) {
        int row;
        for (row = MAX_ROWS; row > 0; row--) {
            if (getPieceValue(column, row - 1) != MAX_COLS)
                break;
        }
        return row;
    }

    public boolean isDownCellFree(int column, int row) {
        return row > 0 && row < MAX_PLAYABLE_ROWS + 1 && getPieceValue(column, row - 1) == YokelBlock.CLEAR_BLOCK;
    }

    public boolean isRightCellFree(int column, int row) {
        return column < MAX_COLS - 1 && getPieceValue(column + 1, row) == YokelBlock.CLEAR_BLOCK;
    }

    public boolean isLeftCellFree(int column, int row) {
        return column > 0 && getPieceValue(column - 1, row) == YokelBlock.CLEAR_BLOCK;
    }

    public int getColumnWithPossiblePieceMatch(YokelPiece piece) {
        shuffleColumnIndices();

        for (int i = 0; i < MAX_COLS; i++) {
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

                    cells[y][x] = MAX_COLS;
                    cells[y + 1][x] = MAX_COLS;
                    cells[y + 2][x] = MAX_COLS;

                    updateBoard();

                    if (hasFullMatch)
                        return j << 8 | x;
                }
            }
        }

        int column = getBoardMakeupHash() % MAX_COLS;

        for (int col = 0; col < MAX_COLS; col++) {
            if (getColumnFill(col) < getColumnFill(column)) {
                column = col;
            }
        }

        return column;
    }

    public int getColumnToPlaceYahooCell(int value) {
        // pick "random" column to start
        int x = getBoardMakeupHash() % MAX_COLS;

        // loop through columns
        for (int i = 0; i < MAX_COLS; i++) {
            // get the height of the column
            int y = getColumnFill(x);

            // if the height fits in visible rows
            if (y < 12) {
                // put the pending piece in that spot
                cells[y][x] = value;

                // check for a near match
                boolean match = hasMatchingCellInProximity(x, y);

                // reset the cell
                cells[y][x] = MAX_COLS;

                // if there is no match, return the column
                if (!match)
                    return x;
            }

            // move to next column wrapped around column count
            x = (x + 1) % MAX_COLS;
        }

        // If unable to find a column without piece like this nearby,
        // get less picky and only ensure that the piece won't make a break

        // loop through columns again
        for (int i = 0; i < MAX_COLS; i++) {
            // get height of column
            int y = getColumnFill(x);

            if (y < 12) {
                cells[y][x] = value;
                boolean bool = hasFullMatchInProximity(x, y);
                cells[y][x] = MAX_COLS;

                if (!bool)
                    return x;
            }

            x = (x + 1) % MAX_COLS;
        }

        return -1;
    }

    int getYahooDuration() {
        int duration = 0;

        for (int row = 0; row < MAX_PLAYABLE_ROWS; row++) {
            if (checkForNonVerticalYahoo(row, 0)) {
                duration += HORIZONTAL_HOO_TIME;

                for (int column = 0; column < MAX_COLS; column++) {
                    cells[row][column] = YokelBlockEval.addBrokenFlag(cells[row][column]);
                }
            }
        }

        for (int column = 0; column < MAX_COLS; column++) {
            for (int row = 0; row < 10; row++) {
                if (YokelBlockEval.getCellFlag(cells[row][column]) == 5) {
                    if (YokelBlockEval.getCellFlag(cells[row + 1][column]) == 4) {
                        if (YokelBlockEval.getCellFlag(cells[row + 2][column]) != 3) {
                            continue;
                        }
                    } else if (YokelBlockEval.getCellFlag(cells[row + 1][column]) != 3
                            || YokelBlockEval.getCellFlag(cells[row + 2][column]) != 4) {
                        continue;
                    }

                    if (YokelBlockEval.getCellFlag(cells[row + 3][column]) == 2
                            && YokelBlockEval.getCellFlag(cells[row + 4][column]) == 1
                            && YokelBlockEval.getCellFlag(cells[row + 5][column]) == 0) {

                        duration += VERTICAL_HOO_TIME;

                        for (int i = 0; i < MAX_COLS; i++) {
                            cells[row + i][column] = YokelBlockEval.addBrokenFlag(cells[row + i][column]);
                        }
                    }
                }
            }
        }

        for (int row = 0; row < 8; row++) {
            if (checkForNonVerticalYahoo(row, 1)) {
                duration += DIAGONAL_HOO_TIME;

                for (int col = 0; col < MAX_COLS; col++) {
                    cells[row + col][col] = YokelBlockEval.addBrokenFlag(cells[row + col][col]);
                }
            }
        }

        for (int row = 5; row < MAX_PLAYABLE_ROWS; row++) {
            if (checkForNonVerticalYahoo(row, -1)) {
                duration += DIAGONAL_HOO_TIME;

                for (int col = 0; col < MAX_COLS; col++) {
                    cells[row - col][col] = YokelBlockEval.addBrokenFlag(cells[row - col][col]);
                }
            }
        }

        updateBoard();
        return duration;
    }

    public int getIdIndex(){ return idIndex;}

    // Possible UI function
    public void placeBlockAt(YokelPiece block, int x, int y) {
        this.piece = block;
        int index = block.getIndex();

        int v0 = block.getValueAt(index % 3);
        v0 = YokelBlockEval.setIDFlag(v0, incrementID());

        int v1 = block.getValueAt((1 + index) % 3);
        v1 = YokelBlockEval.setIDFlag(v1, incrementID());

        int v2 = block.getValueAt((2 + index) % 3);
        v2 = YokelBlockEval.setIDFlag(v2, incrementID());

        if (YokelBlockEval.getCellFlag(cells[y][x]) != YokelBlock.CLEAR_BLOCK) {
            Thread.dumpStack();
            System.out.println("Assertion failure: grid at " + x + "," + y
                    + " isn't empty for piece placement");
        }
        if (YokelBlockEval.getCellFlag(cells[y + 1][x]) != YokelBlock.CLEAR_BLOCK) {
            Thread.dumpStack();
            System.out.println("Assertion failure: grid at " + x + "," + y
                    + " isn't empty for piece placement");
        }
        if (YokelBlockEval.getCellFlag(cells[y + 2][x]) != YokelBlock.CLEAR_BLOCK) {
            Thread.dumpStack();
            System.out.println("Assertion failure: grid at " + x + "," + y
                    + " isn't empty for piece placement");
        }
        cells[y][x] = v2;
        cells[y + 1][x] = v1;
        cells[y + 2][x] = v0;

        updateBoard();
    }

    //Medusa or Midas
    public void handlePlacedPowerBlock(int type) {
        for (int y = 0; y < MAX_ROWS; y++) {
            for (int x = 0; x < MAX_COLS; x++) {
                if (YokelBlockEval.hasPowerBlockFlag(cells[y][x])) {
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
        if (!YokelBlockEval.hasPowerBlockFlag(value)) {
            System.out.println("Assertion failure:  cell isn't weird " + value);
        } else if (YokelBlockEval.getCellFlag(value) != YokelBlock.Oy_BLOCK) {
            System.out.println("Assertion failure:  cell isn't weird type " + value);
        } else if (!YokelBlockEval.hasPowerBlockFlag(cells[row][col])) {
            boolean isAttack = YokelBlockEval.isOffensive(value);

            if (isAttack) {
                if (YokelBlockEval.getCellFlag(cells[row][col]) < YokelBlock.CLEAR_BLOCK) {
                    releaseID(YokelBlockEval.getID(cells[row][col]));
                }

                if (YokelBlockEval.getCellFlag(cells[row][col]) != YokelBlock.CLEAR_BLOCK) {
                    cells[row][col] = YokelBlock.STONE;
                }
            } else if (YokelBlockEval.getCellFlag(cells[row][col]) < YokelBlock.CLEAR_BLOCK) {
                cells[row][col] = YokelBlockEval.addArtificialFlag(YokelBlockEval.setValueFlag(cells[row][col], YokelBlock.Oy_BLOCK));
            }

            updateBoard();
        }
    }

    public void setValueAt(int value, int column, int row) {
        if (YokelBlockEval.getCellFlag(cells[row][column]) != MAX_COLS){
            System.out.println("Assertion failure: grid at " + column + "," + row + " isn't empty for cell placement");
        }
        value = YokelBlockEval.setIDFlag(value, incrementID());
        cells[row][column] = value;
        updateBoard();
    }

    public boolean hasPlayerDied() {
        /*
        for (int row = MAX_PLAYABLE_ROWS; row < MAX_ROWS; row++) {
            for (int col = 0; col < MAX_COLS; col++) {
                if (getPieceValue(col, row) != YokelBlock.CLEAR_BLOCK)
                    return true;
            }
        }*/
        return getPieceValue(2, 12) != YokelBlock.CLEAR_BLOCK;
    }

    public int getBrokenCellCount() {
        int count = 0;

        for (int i = 0; i < MAX_ROWS; i++) {
            for (int j = 0; j < MAX_COLS; j++) {
                if (YokelBlockEval.hasBrokenFlag(cells[i][j]))
                    count++;
            }
        }

        return count;
    }

    public Vector<YokelBlock> getBrokenCells() {
        Vector<YokelBlock> vector = new Vector<>();

        for (int i = 0; i < MAX_ROWS; i++) {
            for (int j = 0; j < MAX_COLS; j++) {
                if (YokelBlockEval.hasBrokenFlag(cells[i][j])){
                    int cell = cells[i][j];
                    YokelBlock block = new YokelBlock(j, i, YokelBlockEval.getCellFlag(cell));
                    if(YokelBlockEval.hasPowerBlockFlag(cell)){
                        block.setPower(YokelBlockEval.getPowerFlag(cell));
                    }
                    vector.addElement(block);
                }
            }
        }
        return vector;
    }


    /*
    public void flagBrokenCells(ByteStack stack) {
        int index = 0;

        while (index < stack.length()) {
            int count = 0;

            for (int row = 0; row < MAX_ROWS; row++) {
                for (int col = 0; col < MAX_COLS; col++) {
                    if (YokelBlockEval.getCellFlag(cells[row][col]) < MAX_COLS) {
                        int id = YokelBlockEval.getID(cells[row][col]);

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
            for (int row = 0; row < MAX_ROWS; row++) {
                for (int col = 0; col < MAX_COLS; col++) {
                    if (YokelBlockEval.getCellFlag(cells[row][col]) < MAX_COLS
                            && stack.getValueAt(i) == YokelBlockEval.getID(cells[row][col])) {

                        cells[row][col] = YokelBlockEval.addBrokenFlag(cells[row][col]);
                    }
                }
            }
        }

        updateBoard();
    }*/

    /*
    public ByteStack getBrokenByPartnerCellIDs() {
        ByteStack stack = new ByteStack();

        for (int row = 0; row < MAX_ROWS; row++) {
            for (int col = 0; col < MAX_COLS; col++) {
                if (YokelBlockEval.hasPartnerBreakFlag(cells[row][col])) {
                    stack.push(YokelBlockEval.getID(cells[row][col]));
                }
            }
        }

        return stack;
    }*/

    public Stack<Integer> getBrokenByPartnerCellIDs() {
        Stack<Integer> stack = new Stack<>();

        for (int row = 0; row < MAX_ROWS; row++) {
            for (int col = 0; col < MAX_COLS; col++) {
                if (YokelBlockEval.hasPartnerBreakFlag(cells[row][col])) {
                    stack.push(YokelBlockEval.getID(cells[row][col]));
                }
            }
        }

        return stack;
    }

    public void flagBrokenCells(Stack<Integer> stack) {
        int index = 0;

        while (index < stack.size()) {
            int count = 0;

            for (int row = 0; row < MAX_ROWS; row++) {
                for (int col = 0; col < MAX_COLS; col++) {
                    if (YokelBlockEval.getCellFlag(cells[row][col]) < MAX_COLS) {
                        int id = YokelBlockEval.getID(cells[row][col]);

                        if (stack.elementAt(index) == id) {
                            count++;
                        }
                    }
                }
            }

            switch (count) {
                default:
                    System.out.println("fucked up, found " + count
                            + " instances of id " + stack.elementAt(index));
                    /* fall through */
                case 0:
                case 1:
                    index++;
            }
        }

        for (int i = 0; i < stack.size(); i++) {
            for (int row = 0; row < MAX_ROWS; row++) {
                for (int col = 0; col < MAX_COLS; col++) {
                    if (YokelBlockEval.getCellFlag(cells[row][col]) < MAX_COLS
                            && stack.elementAt(i) == YokelBlockEval.getID(cells[row][col])) {

                        cells[row][col] = YokelBlockEval.addBrokenFlag(cells[row][col]);
                    }
                }
            }
        }

        updateBoard();
    }


    // Possible UI Function
    public Vector<YokelBlockMove> getCellsToBeDropped() {
        Vector<YokelBlockMove> vector = new Vector<>();

        for (int i = 0; i < targetRows.length; i++) {
            targetRows[i] = 0;
        }

        for (int y = 0; y < MAX_ROWS; y++) {
            for (int x = 0; x < MAX_COLS; x++) {
                // The imporant thing to note is that the targetRow will not get
                // incremented when a cell is to be broken.
                if (!isCellBroken(x, y)) {
                    if (targetRows[x] != y && getPieceValue(x, y) != MAX_COLS) {
                        vector.addElement(new YokelBlockMove(x, y, targetRows[x]));
                    }

                    targetRows[x]++;
                }
            }
        }

        return vector;
    }

    public void checkBoardForPartnerBreaks(YokelGameBoard partner, boolean isPartnerOnRight) {
        for (int row = 0; row < MAX_ROWS; row++) {
            for (int col = 0; col < MAX_COLS; col++) {
                checkCellForPartnerBreak(partner, isPartnerOnRight, col, row);
            }
        }
    }

    void checkCellForPartnerBreak(YokelGameBoard board, boolean isPartnerOnRight, int col, int row) {
        //boolean bool_53_ = true;
        int colOffset;

        if (isPartnerOnRight)
            colOffset = 1;
        else
            colOffset = -1;

        if (getPieceValue(col, row) < MAX_COLS) {
            checkCellForNonVerticalPartnerBreaks(board, col, row, colOffset, -1);
            checkCellForNonVerticalPartnerBreaks(board, col, row, colOffset, 0);
            checkCellForNonVerticalPartnerBreaks(board, col, row, colOffset, 1);
        }
    }

    void checkCellForNonVerticalPartnerBreaks(YokelGameBoard partner, int col, int row, int _x, int _y) {
        int value = getPieceValue(col, row);

        int matchCount;

        // Tally up matches on this board
        for (matchCount = 1;
             (isCellInBoard(col + matchCount * _x, row + matchCount * _y)
                     && getPieceValue(col + matchCount * _x, row + matchCount * _y) == value
                     && !YokelBlockEval.hasPowerBlockFlag(cells[row + matchCount * _y][col + matchCount * _x]));
             matchCount++) {
            /* empty */
        }

        // If we're moving to partner side
        if (!isCellInBoard(col + matchCount * _x, row + matchCount * _y)) {
            int partnerMatchCount = 0;
            int pX;

            if (_x > 0)
                pX = -(matchCount * _x);
            else
                pX = 5 - matchCount * _x;

            for (/**/;
                     (isCellInBoard(pX + matchCount * _x, row + matchCount * _y)
                             && partner.getPieceValue(pX + matchCount * _x, row + matchCount * _y) == value
                             && !YokelBlockEval.hasPowerBlockFlag(partner.cells[row + matchCount * _y][pX + matchCount * _x]));
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
                            copy = YokelBlockEval.addPartnerBreakFlag(copy);
                            cells[y][x] = copy;
                        } else {
                            x = pX + i * _x;
                            int copy = partner.cells[y][x];
                            copy = YokelBlockEval.addPartnerBreakFlag(copy);
                            partner.cells[y][x] = copy;
                        }
                    }
                    updateBoard();
                }
            }
        }
    }

    boolean checkForNonVerticalYahoo(int y, int _y) {
        boolean result = true;

        int row = y;

        if (YokelBlockEval.getCellFlag(cells[row][0]) != YokelBlock.Y_BLOCK) {
            result = false;
        }

        row += _y;

        if (YokelBlockEval.getCellFlag(cells[row][1]) != YokelBlock.A_BLOCK) {
            result = false;
        }

        row += _y;

        if (YokelBlockEval.getCellFlag(cells[row][2]) != YokelBlock.H_BLOCK) {
            result = false;
        }

        row += _y;

        if (YokelBlockEval.getCellFlag(cells[row][3]) == YokelBlock.Op_BLOCK) {
            if (YokelBlockEval.getCellFlag(cells[row + _y][4]) != YokelBlock.Oy_BLOCK)
                result = false;
        } else {
            if (YokelBlockEval.getCellFlag(cells[row][3]) != YokelBlock.Oy_BLOCK)
                result = false;
            if (YokelBlockEval.getCellFlag(cells[row + _y][4]) != YokelBlock.Op_BLOCK)
                result = false;
        }
        row += 2 * _y;
        if (YokelBlockEval.getCellFlag(cells[row][5]) != YokelBlock.EX_BLOCK)
            result = false;

        return result;
    }

    /**
     * Custom Helper methods
     */

    public String toString(){
        StringBuilder out = new StringBuilder();
        //System.out.println("out = " + out);
        if(piece != null){
            out.append("piece pos(").append(piece.column).append(",").append(piece.row).append(")").append("\n");
        }

        addPrintLine(out);
        for (int r = MAX_ROWS - 1; r > -1; r--){
            for (int c = 0; c < MAX_COLS; c++) {
                int block = isPieceBlock(r, c) ? getPieceBlock(r) : getPieceValue(c,r);

                if(block == YokelBlock.CLEAR_BLOCK){
                    out.append('|').append(' ');
                } else {
                    if (YokelBlockEval.hasPowerBlockFlag(block)) {
                        out.append('|').append(YokelBlockEval.getPowerLabel(block));
                    } else {
                        out.append('|').append(YokelBlockEval.getNormalLabel(block));
                    }
                }
            }
            out.append("|\n");
        }
        addPrintLine(out);
        return out.toString();
    }

    private boolean isPieceBlock(int row, int col){
        return piece != null && piece.column == col && (piece.row == row || piece.row + 1 == row || piece.row + 2 == row);
    }

    private int getPieceBlock(int row){
        return piece.getValueAt(row - piece.row);
    }

    private void addPrintLine(StringBuilder sb){
        for(int a = 0; a < MAX_COLS; a++){
            sb.append("+-");
        }
        sb.append('+').append('\n');
    }

    public YokelPiece fetchCurrentPiece(){
        return piece;
    }

    public float fetchCurrentFallnumber(){
        return fallNumber;
    }

    public YokelPiece fetchCurrentNextPiece(){
        if(specialPieces.size > 0){
            int special = peekSpecialQueue();
            return special % 2 == 0 ? MIDAS_PIECE : MEDUSA_PIECE;
        }
        return nextPiece;
    }

    private void clearCell(int r, int c){
        cells[r][c] = YokelBlock.CLEAR_BLOCK;
    }

    public void update(float delta){
        if(!hasPlayerDied()){
            this.fallNumber -= getCurrentFallRate();
            if(this.fallNumber < 0){
                movePieceDown();
            }
        }
        updateBoard();
    }

    private float getCurrentFallRate() {
        if(fastDown){
            return FAST_FALL_RATE;
        } else {
            return FALL_RATE;
        }
    }

    private void updateBoard(){
        flagBoardMatches();
    }

    private void movePieceDown(){
        if(piece != null){
            if(isDownCellFree(piece.column, piece.row)){
                this.fallNumber = MAX_FALL_VALUE;
                piece.setPosition(piece.row - 1, piece.column);
            } else {
                if(!isPieceSet){
                    setNextPiece();
                    isPieceSet = true;
                }
                if(brokenCellsHandled) {
                    getNewNextPiece();
                    isPieceSet = false;
                }
            }
        }
    }

    public void movePieceRight(){
        if(isMovingValid()){
            if(isRightCellFree(piece.column, piece.row)){
                piece.setPosition(piece.row, piece.column + 1);
            }
        }
    }

    public void movePieceLeft(){
        if(isMovingValid()){
            if(isLeftCellFree(piece.column, piece.row)){
                piece.setPosition(piece.row, piece.column - 1);
            }
        }
    }

    public void cycleDown(){
        if(isMovingValid()){
            piece.cycleDown();
        }
    }

    public void cycleUp(){
        if(isMovingValid()){
            piece.cycleUp();
        }
    }

    private boolean isMovingValid(){
        return piece != null && brokenCellsHandled;
    }

    public void startMoveDown(){
        fastDown = true;
    }

    public void stopMoveDown(){
        fastDown = false;
    }

    public int getNextBlock(){
        return nextBlocks.getRandomNumberAt(++currentBlockPointer);
    }

    public void setNextPiece() {
        if(piece != null) {
            int block = YokelBlockEval.getCellFlag(piece.getBlock1());
            if (block == YokelBlock.MEDUSA) {
                piece.setBlock1(YokelBlockEval.addPowerBlockFlag(YokelBlockEval.setPowerFlag(YokelBlock.Oy_BLOCK, 3)));
                piece.setBlock2(YokelBlockEval.addPowerBlockFlag(YokelBlockEval.setPowerFlag(YokelBlock.Oy_BLOCK, 3)));
                piece.setBlock3(YokelBlockEval.addPowerBlockFlag(YokelBlockEval.setPowerFlag(YokelBlock.Oy_BLOCK, 3)));
            } else if (block == YokelBlock.TOP_MIDAS || block == YokelBlock.MID_MIDAS || block == YokelBlock.BOT_MIDAS) {
                piece.setBlock1(YokelBlockEval.addPowerBlockFlag(YokelBlockEval.setPowerFlag(YokelBlock.Oy_BLOCK, 2)));
                piece.setBlock2(YokelBlockEval.addPowerBlockFlag(YokelBlockEval.setPowerFlag(YokelBlock.Oy_BLOCK, 2)));
                piece.setBlock3(YokelBlockEval.addPowerBlockFlag(YokelBlockEval.setPowerFlag(YokelBlock.Oy_BLOCK, 2)));
            }

            placeBlockAt(piece, piece.column, piece.row);

            //Remove powers from placed block
            if (block == YokelBlock.MEDUSA || block == YokelBlock.TOP_MIDAS || block == YokelBlock.MID_MIDAS || block == YokelBlock.BOT_MIDAS) {
                handlePlacedPowerBlock(piece.getBlock1());
                cells[piece.row][piece.column] = YokelBlockEval.setIDFlag(YokelBlock.Oy_BLOCK, YokelBlockEval.getID(cells[piece.row][piece.column]));
                cells[piece.row + 1][piece.column] = YokelBlockEval.setIDFlag(YokelBlock.Oy_BLOCK, YokelBlockEval.getID(cells[piece.row + 1][piece.column]));
                cells[piece.row + 2][piece.column] = YokelBlockEval.setIDFlag(YokelBlock.Oy_BLOCK, YokelBlockEval.getID(cells[piece.row + 2][piece.column]));
            }
            //piece = null;
            brokenCellsHandled = false;
        }
    }

    public int peekSpecialQueue(){
        return specialPieces.first();
    }

    public void addSpecialPiece(int piece){
        if(piece > 2 || piece < 1) {
            System.out.println("Assertion Error: invalid special block: " + piece);
            return;
        }
        specialPieces.addLast(piece);
    }

    public void getNewNextPiece() {
        int isSpecial = 0;

        if(!YokelUtilities.isQueueEmpty(specialPieces)){
            isSpecial = specialPieces.removeFirst();
        }

        if(nextPiece == null){
            this.nextPiece = newPieceBock(isSpecial);
        }

        if(isSpecial == 0) {
            this.piece = nextPiece;
            this.nextPiece = newPieceBock(isSpecial);
        } else {
            this.piece = newPieceBock(isSpecial);
        }
    }

    private YokelPiece newPieceBock(int isSpecial){
        int block1;
        int block2;
        int block3;

        if(isSpecial > 0){
            if(isSpecial % 2 == 1){
                block1 = YokelBlockEval.addPowerBlockFlag(YokelBlockEval.setPowerFlag(YokelBlock.MEDUSA, 3));
                block2 = YokelBlockEval.addPowerBlockFlag(YokelBlockEval.setPowerFlag(YokelBlock.MEDUSA, 3));
                block3 = YokelBlockEval.addPowerBlockFlag(YokelBlockEval.setPowerFlag(YokelBlock.MEDUSA, 3));
            } else {
                block1 = YokelBlockEval.addPowerBlockFlag(YokelBlockEval.setPowerFlag(YokelBlock.BOT_MIDAS, 2));
                block2 = YokelBlockEval.addPowerBlockFlag(YokelBlockEval.setPowerFlag(YokelBlock.MID_MIDAS, 2));
                block3 = YokelBlockEval.addPowerBlockFlag(YokelBlockEval.setPowerFlag(YokelBlock.TOP_MIDAS, 2));
            }
        } else {
            block1 = powerUpBlock(getNextBlock());
            block2 = powerUpBlock(getNextBlock());
            block3 = powerUpBlock(getNextBlock());
        }

        YokelPiece piece = new YokelPiece(getIdIndex(), block1, block2, block3);
        fallNumber = MAX_FALL_VALUE;
        piece.setPosition(MAX_PLAYABLE_ROWS, 2);
        return piece;
    }

    //TODO: Make this safe
    private int powerUpBlock(int block){
        if(countOfBreaks[block] > 3){
            //System.out.println("breaks" + Arrays.toString(countOfBreaks));
            //System.out.println("block[" + block + "] has broken." );
            //System.out.println("powers keep" + Arrays.toString(powersKeep));

            //powerUp
            countOfBreaks[block] -= 4;
            ++powersKeep[block];
            return getBlockPower(block, powersKeep[block]);
        } else {
            return block;
        }
    }

    public Queue<Integer> getPowers() {
        return powers;
    }

    private int getBlockPower(int block, int intensity){
        if(intensity == 1) {
            intensity = 3;
        }
        if(intensity > 7) {
            intensity = 7;
        }
        return YokelBlockEval.addPowerBlockFlag(YokelBlockEval.setPowerFlag(block, intensity));
    }

    public void incrementBreakCount(int type){
        if(type < 0 || type > MAX_COLS) return;
        ++countOfBreaks[type];
    }

    public void addPowerToQueue(YokelBlock block){
        if(block != null){
            int b = block.getBlockType();
            if(block.hasPower()){
                powers.addFirst(YokelBlockEval.addPowerBlockFlag(YokelBlockEval.setPowerFlag(b, block.getPower())));
            }
        }
    }

    public int checkForYahoos(){
        yahooDuration += getYahooDuration();
        return yahooDuration;
    }

    public boolean isPieceSet() {
        return isPieceSet;
    }
}