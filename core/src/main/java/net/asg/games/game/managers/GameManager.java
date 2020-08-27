package net.asg.games.game.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Queue;
import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;
import com.github.czyzby.kiwi.util.gdx.collection.GdxMaps;

import net.asg.games.game.objects.YokelBlock;
import net.asg.games.game.objects.YokelBlockEval;
import net.asg.games.game.objects.YokelBlockMove;
import net.asg.games.game.objects.YokelGameBoard;
import net.asg.games.game.objects.YokelPlayer;
import net.asg.games.game.objects.YokelSeat;
import net.asg.games.game.objects.YokelTable;
import net.asg.games.utils.YokelUtilities;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Stack;
import java.util.Vector;

public class GameManager {
    private YokelTable table;
    private YokelGameBoard[] gameBoards = new YokelGameBoard[8];
    private boolean[] areCellDropsHandled = new boolean[]{true, true, true, true, true, true, true, true};
    private Array<YokelPlayer> winners = GdxArrays.newArray();
    private boolean isGameRunning;
    private boolean hasGameStarted;
    private boolean showGameOver;

    public GameManager(YokelTable table){
        this.table = table;
        init();
    }

    private void loadGameData() {}

    public void update(float delta){
        if(!isGameRunning) return;

        for(int i = 0; i < 8; i++){
            YokelSeat seat = table.getSeat(i);

            if(isOccupied(seat)){
                YokelGameBoard board = gameBoards[i];

                if(board != null){
                    board.begin();
                    updateBoard(board, delta, i);
                }
            }
        }

        if(isGameOver()){
            stopGame();
        }
    }

    private void updateBoard(YokelGameBoard board, float delta, int index){
        if(board != null){
            board.update(delta);

            //check for yahoo
            int duration = board.checkForYahoos();
            if(duration > 0){
                System.err.println("YAHOOOOOOOOOOO!: " + duration);
            }

            //Check if cells need to drop
            Vector<YokelBlockMove> toDrop = board.getCellsToBeDropped();
            if(toDrop != null && toDrop.size() > 0){
                //Animate Falls before calling
                setIsCellsBrokenHandled(false, index);
            }

            //Clear broken from board if animation is finished
            if(areCellDropsHandled[index]) {
                handleBrokenCellDrops(board);
            }

            //Handle broken cells logic
            if(board.getBrokenCellCount() > 0){
                Vector<YokelBlock> broken = board.getBrokenCells();

                for (YokelBlock b : broken) {
                    if(b != null){
                        board.addPowerToQueue(b);
                        board.incrementBreakCount(b.getBlockType());
                    }
                }
            }
        }
    }

    public void setIsCellsBrokenHandled(boolean b, int index) {
        areCellDropsHandled[index] = b;
    }

    private boolean isOccupied(YokelSeat seat){
        if(seat != null){
            return seat.isOccupied();
        }
        return false;
    }

    public boolean showGameOver(){
        boolean temp = showGameOver;
        if(showGameOver){
            showGameOver = false;
        }
        return temp;
    }

    private boolean isPlayerDead(YokelGameBoard board){
        if(board != null && board.hasGameStarted()){
            return board.hasPlayerDied();
        }
        return true;
    }

    public boolean isPlayerDead(int i){
        return isPlayerDead(getGameBoard(i));
    }

    private void init(){
        long seed = getSeed();
        isGameRunning = false;
        for(int i = 0; i < 8; i++){
            gameBoards[i] = new YokelGameBoard(seed);
        }
    }

    public YokelGameBoard getGameBoard(int i){
        if(i < 0 || i > 8) throw new GdxRuntimeException("Invalid Gameboard index: " + i);
        return gameBoards[i];
    }

    public ObjectMap<Integer, YokelGameBoard> getActiveGameBoards(){
        ObjectMap<Integer, YokelGameBoard> returnBoards = GdxMaps.newObjectMap();
        int index = 0;
        for(YokelGameBoard board : gameBoards){
            if(board != null && board.hasGameStarted() && !board.hasPlayerDied()){
                returnBoards.put(index, board);
            }
            ++index;
        }
        return returnBoards;
    }

    private long getSeed() {
        return 1L;
        //return System.currentTimeMillis();
    }

    public void resetGameBoards(){}

    public void handleBrokenCellDrops(YokelGameBoard board){
        if(board != null){
            board.handleBrokenCellDrops();
        }
    }

    public void handleMoveRight(int boardIndex){
        getGameBoard(boardIndex).movePieceRight();
    }

    public void handleMoveLeft(int boardIndex){
        getGameBoard(boardIndex).movePieceLeft();
    }

    public void handleCycleDown(int boardIndex){
        getGameBoard(boardIndex).cycleDown();
    }

    public void handleCycleUp(int boardIndex){
        getGameBoard(boardIndex).cycleUp();
    }

    public void handleStartMoveDown(int boardIndex){
        getGameBoard(boardIndex).startMoveDown();
    }

    public void handleStopMoveDown(int boardIndex){
        getGameBoard(boardIndex).stopMoveDown();
    }

    private Stack<Integer> popPowersFromBoard(int boardIndex, int amount){
        Stack<Integer> powerStack = new Stack<>();
        //given board, get player powers
        YokelGameBoard gameBoard = getGameBoard(boardIndex);
        Queue<Integer> powers = gameBoard.getPowers();
        if(powers != null){
            //pop next power
            while(amount-- > 0){
                if(!powers.isEmpty()){
                    powerStack.push(powers.removeFirst());
                }
            }
        }
        return powerStack;
    }

    private void handleAttackGivenAttack(int target, int attackBlock){
        //Get all active boards
        if(attackBlock != YokelBlock.CLEAR_BLOCK){
            ObjectMap<Integer, YokelGameBoard> activeBoards = getActiveGameBoards();
            YokelGameBoard gameBoard = activeBoards.get(target);

            if(gameBoard != null){
                boolean isOffensive = YokelBlockEval.isOffensive(attackBlock);
                int value = YokelBlockEval.getCellFlag(attackBlock);

                if(value == YokelBlock.Oy_BLOCK) {
                    //If offensive Yokel.L, set medusa next piece to target
                    if(isOffensive){
                        gameBoard.addSpecialPiece(1);
                    } else {
                        //If defensive Yokel.L, set midas next piece to target
                        gameBoard.addSpecialPiece(2);
                    }
                } else if(value == YokelBlock.EX_BLOCK) {
                    if(isOffensive){
                        //if offensive Yokel.Ex, need to remove powers from target
                        int level = YokelBlockEval.getPowerLevel(value);
                        Stack<Integer> blockStack = popPowersFromBoard(target, level);
                        gameBoard.addRemovedPowersToBoard(blockStack);
                    } else {
                        gameBoard.handlePower(attackBlock);
                    }
                } else {
                    gameBoard.handlePower(attackBlock);
                }
            }
        }
    }

    public void handleAttack(int currentBoardSeat, int seatTarget){
        Stack<Integer> blocks = popPowersFromBoard(currentBoardSeat, 1);

        if(blocks.size() > 0) {
            int block = blocks.pop();
            handleAttackGivenAttack(seatTarget, block);
        }
    }

    public void handleRandomAttack(int currentBoardSeat){
        Stack<Integer> blocks = popPowersFromBoard(currentBoardSeat, 1);

        if(blocks.size() > 0) {
            int block = blocks.pop();
            int partnerIndex = (currentBoardSeat % 2 == 0)? currentBoardSeat + 1 : currentBoardSeat - 1;

            Array<Integer> boardIndexes = GdxArrays.newArray();
            for(int i = 0; i < 8; i++){
                boardIndexes.add(i);
            }

            Iterator<Integer> iter = boardIndexes.iterator();
            while(iter.hasNext()){
                int x = iter.next();

                if(YokelBlockEval.isOffensive(block)){
                    if(x == currentBoardSeat || x == partnerIndex){
                        iter.remove();
                    }
                } else {
                    if(x != currentBoardSeat && x != partnerIndex){
                        iter.remove();
                    }
                }
            }

            ObjectMap<Integer, YokelGameBoard> activeGameboards = getActiveGameBoards();
            Array<Integer> activeBoards = activeGameboards.keys().toArray();
            Iterator<Integer> active = boardIndexes.iterator();

            while(active.hasNext()){
                int a = active.next();
                if(!activeBoards.contains(a, true)){
                    active.remove();
                }
            }

            YokelUtilities.flushIterator(iter);
            YokelUtilities.flushIterator(activeBoards.iterator());
            YokelUtilities.flushIterator(active);

            int index = MathUtils.random(boardIndexes.size - 1);
            handleAttackGivenAttack(boardIndexes.get(index), block);
        }
    }

    public String[] getBoardState(){
        return null;
    }

    public String printTables(){
        StringBuilder sbSeats = new StringBuilder();

        if(table != null){
            for(YokelGameBoard board : gameBoards){
                sbSeats.append(board.toString());
            }
        }
        return sbSeats.toString();
    }

    public boolean startGame() {
        if(!isGameRunning){
            isGameRunning = table.isTableStartReady();
        }
        return isGameRunning;
    }

    public boolean stopGame(){
        for(YokelGameBoard gameboard : gameBoards){
            if(gameboard != null){
                gameboard.end();
                showGameOver = true;
            }
        }
        return isGameRunning = hasGameStarted = false;
    }

    public boolean isRunning() {
        return isGameRunning;
    }

    public boolean isGameOver(){
        //TODO: implement action history to break tie with last placed block.
        boolean player1 = isPlayerDead(getGameBoard(0));
        boolean player2 = isPlayerDead(getGameBoard(1));
        boolean player3 = isPlayerDead(getGameBoard(2));
        boolean player4 = isPlayerDead(getGameBoard(3));
        boolean player5 = isPlayerDead(getGameBoard(4));
        boolean player6 = isPlayerDead(getGameBoard(5));
        boolean player7 = isPlayerDead(getGameBoard(6));
        boolean player8 = isPlayerDead(getGameBoard(7));

        boolean isGroup1Dead = player1 && player2;
        boolean isGroup2Dead = player3 && player4;
        boolean isGroup3Dead = player5 && player6;
        boolean isGroup4Dead = player7 && player8;

        /*
        System.out.println("player1 dead? " + player1);
        System.out.println("player2 dead? " + player2);
        System.out.println("player3 dead? " + player3);
        System.out.println("player4 dead? " + player4);
        System.out.println("player5 dead? " + player5);
        System.out.println("player6 dead? " + player6);
        System.out.println("player7 dead? " + player7);
        System.out.println("player8 dead? " + player8);
        *
         */

        boolean group1won = !isGroup1Dead && isGroup2Dead && isGroup3Dead && isGroup4Dead;
        boolean group2won = isGroup1Dead && !isGroup2Dead && isGroup3Dead && isGroup4Dead;
        boolean group3won = isGroup1Dead && isGroup2Dead && !isGroup3Dead && isGroup4Dead;
        boolean group4won = isGroup1Dead && isGroup2Dead && isGroup3Dead && !isGroup4Dead;

        /*
        System.err.println("group1won dead? " + group1won);
        System.err.println("group2won dead? " + group2won);
        System.err.println("group3won dead? " + group3won);
        System.err.println("group4won dead? " + group4won);
        */

        if(group1won){
            setWinners(getPlayerFromBoard(table.getSeat(0)), getPlayerFromBoard(table.getSeat(1)));
        }

        if(group2won){
            setWinners(getPlayerFromBoard(table.getSeat(2)), getPlayerFromBoard(table.getSeat(3)));
        }

        if(group3won){
            setWinners(getPlayerFromBoard(table.getSeat(4)), getPlayerFromBoard(table.getSeat(5)));
        }

        if(group4won){
            setWinners(getPlayerFromBoard(table.getSeat(6)), getPlayerFromBoard(table.getSeat(7)));
        }

        return group1won || group2won || group3won || group4won;
    }

    private void setWinners(YokelPlayer player1, YokelPlayer player2) {
        winners.clear();
        if(player1 != null){
            winners.add(player1);
        }

        if(player2 != null){
            winners.add(player2);
        }
    }

    public Array<YokelPlayer> getWinners(){
        return winners;
    }

    private YokelPlayer getPlayerFromBoard(YokelSeat seat){
        if(seat != null){
            return seat.getSeatedPlayer();
        }
        return null;
    }

    //TODO: Remove test methods
    public void testMedusa(int target) {
        ObjectMap<Integer, YokelGameBoard> activeBoards = getActiveGameBoards();
        YokelGameBoard gameBoard = activeBoards.get(target);

        if(gameBoard != null){
            System.out.println("Added a Medusa");
            gameBoard.addSpecialPiece(1);
        }
    }

    public void testMidas(int target) {
        ObjectMap<Integer, YokelGameBoard> activeBoards = getActiveGameBoards();
        YokelGameBoard gameBoard = activeBoards.get(target);

        if(gameBoard != null){
            System.out.println("Added a Midas");
            gameBoard.addSpecialPiece(2);
        }
    }

    public void showGameBoard(int target) {
        System.out.println(gameBoards[target]);
    }

    public void testGameBoard(int target) {
        System.out.println(gameBoards[target]);
        int[][] cells = gameBoards[target].getCells();

        for(int[] inner : cells){
            System.out.println(Arrays.toString(inner));
        }
        Gdx.app.exit();
    }

    private static class GameState{}
}