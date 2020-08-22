package net.asg.games.game.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Queue;
import com.github.czyzby.kiwi.util.gdx.GdxUtilities;
import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;

import net.asg.games.game.objects.YokelBlock;
import net.asg.games.game.objects.YokelBlockEval;
import net.asg.games.game.objects.YokelGameBoard;
import net.asg.games.game.objects.YokelPlayer;
import net.asg.games.game.objects.YokelSeat;
import net.asg.games.game.objects.YokelTable;
import net.asg.games.utils.YokelUtilities;

import java.util.Iterator;
import java.util.Vector;

public class GameManager {
    private YokelTable table;
    private YokelGameBoard[] gameBoards = new YokelGameBoard[8];
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
                    updateBoard(board, delta);
                }
            }
        }

        if(isGameOver()){
            stopGame();
        }
    }

    private void updateBoard(YokelGameBoard board, float delta){
        if(board != null){
            board.update(delta);

            //Check broken Pieces
            board.flagBoardMatches();
            board.checkForYahoos();

            if(board.getBrokenCellCount() > 0){
                Vector<YokelBlock> broken = board.getBrokenCells();

                System.out.println("Broken Cells: " + broken);
                for (YokelBlock b : broken) {
                    board.addPowerToQueue(b);
                    board.incrementBreakCount(b.getBlockType());
                }
            }
            board.handleBrokenCellDrops();
        }
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

    public Array<YokelGameBoard> getActiveGameBoards(){
        Array<YokelGameBoard> returnBoards = GdxArrays.newArray();
        for(YokelGameBoard board : gameBoards){
            if(board != null && board.hasGameStarted() && !board.hasPlayerDied()){
                returnBoards.add(board);
            }
        }
        return returnBoards;
    }

    private long getSeed() {
        return 1L;
        //return System.currentTimeMillis();
    }

    public void resetGameBoards(){}

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

    private int popPowerFromBoard(int boardIndex){
        int block = YokelBlock.CLEAR_BLOCK;
        //given board, get player powers
        YokelGameBoard gameBoard = getGameBoard(boardIndex);
        Queue<Integer> powers = gameBoard.getPowers();
        if(powers != null){
            //pop next power
            if(!powers.isEmpty()){
                block = powers.removeFirst();
            }
        }
        return block;
    }

    public void handleAttack(int boardIndex, int target){
        int block = popPowerFromBoard(boardIndex);
        //Get all active boards
        if(block != YokelBlock.CLEAR_BLOCK){
            Array<YokelGameBoard> activeBoards = getActiveGameBoards();

            if(!YokelUtilities.isArrayEmpty(activeBoards)){
                YokelGameBoard gameBoard = activeBoards.get(target);

                if(gameBoard != null){
                    System.out.println("Power = " + block);
                    System.out.println("Powah? = " + block);
                    System.out.println("activeBoards = " + activeBoards);

                    //If offensive Yokel.L, set medusa next piece to target
                    //else
                    //If defensive Yokel.L, set midas next piece to target
                    //else
                    //if offensive Yokel.Ex, need to remove powers from target
                    //else
                    gameBoard.handlePower(block);
                }
            }
        }
    }

    public void handleRandomAttack(int boardIndex){
        int block = popPowerFromBoard(boardIndex);
        int partnerIndex = (boardIndex % 2 == 0)? boardIndex + 1 : boardIndex - 1;

        Array<Integer> boardIndexes = GdxArrays.newArray();
        for(int i = 0; i < 8; i++){
            boardIndexes.add(i);
        }

        int target;
        Iterator<Integer> iter = boardIndexes.iterator();
        if(YokelBlockEval.isOffensive(block)){
            //if block is offensive, get random non partner or player board
            while(iter.hasNext()){
                int x = iter.next();
                if(x == boardIndex || x == partnerIndex){
                    iter.remove();
                }
            }
        } else {
            //if block is defensive, get random partner or player
            while(iter.hasNext()){
                int x = iter.next();
                if(x != boardIndex && x != partnerIndex){
                    iter.remove();
                }
            }
        }

        int index = MathUtils.random(boardIndexes.size);
        handleAttack(boardIndex, boardIndexes.get(index));
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

    private static class GameState{}
}