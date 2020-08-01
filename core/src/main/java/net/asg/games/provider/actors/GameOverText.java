package net.asg.games.provider.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import net.asg.games.game.objects.YokelPlayer;

public class GameOverText extends Table {
    private final static String GAME_OVER_LABEL = "Game Over";
    private static final String YOU_WIN_LABEL = "You Win!";
    private static final String YOU_LOSE_LABEL = "You Lose!";
    private static final String CONGRATULATIONS_LABEL = "Congratulations";
    private static final String AND_LABEL = "and";
    private static final float H1Scale = 6;
    private static final float H2Scale = 4;
    private static final Color PINK = new Color(1f, 0.09f, 0.98f, 1);
    private static final Color YELLOW = new Color(0.83f, 0.83f, 0.01f, 1);

    private String player1Name;
    private String player2Name;
    private final boolean win;

    private boolean hasAnimationStarted;

    public GameOverText(boolean winOrLoss, YokelPlayer player1, YokelPlayer player2, Skin skin){
        if(player1 != null){
            this.player1Name = player1.getName();
        }

        if(player2 != null){
            this.player2Name = player2.getName();
        }
        this.win = winOrLoss;
        setSkin(skin);
        init();
    }

    public GameOverText(boolean winOrLoss, String player1Name, String player2Name, Skin skin){
        this.player1Name = player1Name;
        this.player2Name = player2Name;
        this.win = winOrLoss;
        setSkin(skin);
        init();
    }

    private void init(){
        Table container = new Table();

        Label gameOver = getGameOverLabel();
        Label winLoss = getWinLossLabel();
        Label congrats = getCongratsLabel();

        container.add(gameOver).row();
        container.add(winLoss).row();
        container.add(congrats).row();

        if(player1Name != null && player2Name != null){
            container.add(getPlayerNameLabel(player1Name)).row();
            container.add(getPlayerNameLabel(AND_LABEL + " " + player2Name));
        } else {
            container.add(getPlayerNameLabel(player1Name == null ? player2Name : player1Name));
        }
        add(container);

        Vector2 startPos = container.localToParentCoordinates(new Vector2(0,0));
        //gameOver.setPosition(startPos.x / 2, 200);
        gameOver.addAction(Actions.moveTo(startPos.x / 2, 200, 0.1f, Interpolation.bounceOut));
        hasAnimationStarted = true;
    }

    private Label getGameOverLabel(){
        //add(container);
       //Vector2 pos = container.localToParentCoordinates(new Vector2(0, 0));
        //System.err.println("container x=" + pos);
/*
        for(int i=0; i < GAME_OVER_LABEL.length(); i++){
            Label charLabel = new Label(GAME_OVER_LABEL.charAt(i) + "", getSkin());
            //charLabel.addAction();

            charLabel.setFontScale(H1Scale);
            charLabel.setColor(PINK);
            charLabel.setPosition(pos.x / 2, 0);
            charLabel.addAction(Actions.moveTo(pos.x / 2, pos.y / 2, 1f, Interpolation.bounceOut));
            this.addActor(charLabel);
            //container.add(charLabel);
            pos.x += charLabel.getWidth();
        }*/

        Label gameOver = new Label(GAME_OVER_LABEL, getSkin());
        gameOver.setFontScale(H1Scale);
        gameOver.setColor(PINK);
        //gameOver.setPosition(pos.x / 2, 0);
        //gameOver.addAction(Actions.moveTo(pos.x / 2, 0, 1f, Interpolation.linear));
        return gameOver;
    }

    private Label getWinLossLabel(){
        Label winLoss = new Label(win ? YOU_WIN_LABEL : YOU_LOSE_LABEL, getSkin());
        winLoss.setFontScale(H2Scale);
        winLoss.setColor(YELLOW);
        return winLoss;
    }

    private Label getCongratsLabel(){
        Label congratsLabel = new Label(CONGRATULATIONS_LABEL, getSkin());
        congratsLabel.setFontScale(H2Scale);
        congratsLabel.setColor(PINK);
        return congratsLabel;
    }

    private Label getPlayerNameLabel(String name){
        Label playerName = new Label(name, getSkin());
        playerName.setFontScale(H2Scale);
        playerName.setColor(PINK);
        return playerName;
    }
}
