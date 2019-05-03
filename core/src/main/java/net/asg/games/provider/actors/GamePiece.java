package net.asg.games.provider.actors;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.github.czyzby.lml.scene2d.ui.reflected.AnimatedImage;

import net.asg.games.game.objects.YokelBlock;
import net.asg.games.utils.UIUtil;
import net.asg.games.utils.Util;

public class GamePiece extends Table {
    private GameBlock top;
    private GameBlock middle;
    private GameBlock bottom;

    public GamePiece(){
        this(null,null,null,null);
    }

    public GamePiece(Skin skin, GameBlock top, GameBlock mid, GameBlock bottom){
        //setDebug(true);
        setSkin(skin);
        initialize();
        setTopBlock(top);
        setMiddleBlock(mid);
        setBottomBlock(bottom);
        initializeUiCells();
        //validate();
        invalidate();
    }

    public GamePiece(Skin skin, int top, int mid, int bottom){
        this(skin, UIUtil.getInstance().getGameBlock(top), UIUtil.getInstance().getGameBlock(mid), UIUtil.getInstance().getGameBlock(bottom));
    }

    private void initialize(){
        this.top = getClearBlock();
        this.middle = getClearBlock();
        this.bottom = getClearBlock();
        this.setWidth(top.getWidth());
        this.setHeight(top.getHeight() + middle.getHeight() + bottom.getHeight());
    }

    private void initializeUiCells(){
        setX(0);
        setY(0);

        add(getTopBlock()).row();
        add(getMiddleBlock()).row();
        add(getBottomBlock());
    }

    private GameBlock getClearBlock(){
        return UIUtil.getInstance().getGameBlock(YokelBlock.CLEAR);
    }

    public GameBlock getTopBlock() {
        return top;
    }

    public void setTopBlock(GameBlock top) {
        if(top != null){
            this.top.setImage(top.getImage());
        }
    }

    public GameBlock getMiddleBlock() {
        return middle;
    }

    public void setMiddleBlock(GameBlock middle) {
        if(middle != null){
            this.middle.setImage(middle.getImage());
        }
    }

    public GameBlock getBottomBlock() {
        return bottom;
    }

    public void setBottomBlock(GameBlock bottom) {
        if(bottom != null){
            this.bottom.setImage(bottom.getImage());
        }
    }

    public void cycleUp(){
        GameBlock topBlock = getTopBlock();
        GameBlock midBlock = getMiddleBlock();
        GameBlock bottomBlock = getBottomBlock();

        setTopBlock(bottomBlock);
        setMiddleBlock(topBlock);
        setBottomBlock(midBlock);
    }

    public void cycleDown(){
        GameBlock topBlock = getTopBlock();
        GameBlock midBlock = getMiddleBlock();
        GameBlock bottomBlock = getBottomBlock();

        setTopBlock(midBlock);
        setMiddleBlock(bottomBlock);
        setBottomBlock(topBlock);
    }

    public void setData(String[] data) {
        if(data != null && data.length < 4){
            //System.out.println(Util.otoi(data[0]) + "|1:" + UIUtil.getInstance().getGameBlock(Util.otoi(data[0])));
            setTopBlock(UIUtil.getInstance().getGameBlock(Util.otoi(data[0])));
            setMiddleBlock(UIUtil.getInstance().getGameBlock(Util.otoi(data[1])));
            setBottomBlock(UIUtil.getInstance().getGameBlock(Util.otoi(data[2])));
        }
    }
}
