package net.asg.games.provider.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

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
        setSkin(skin);
        initialize(top, mid, bottom);
        setUpUiCells();
    }

    public GamePiece(Skin skin, int top, int mid, int bottom){
        this(skin, UIUtil.getInstance().getGameBlock(top), UIUtil.getInstance().getGameBlock(mid), UIUtil.getInstance().getGameBlock(bottom));
    }

    private void initialize(GameBlock top, GameBlock middle, GameBlock bottom){
        setX(0);
        setY(0);
        this.top = getBlock(top);
        this.middle = getBlock(middle);
        this.bottom = getBlock(bottom);
        this.setWidth(this.top.getWidth());
        this.setHeight(this.top.getHeight() + this.middle.getHeight() + this.bottom.getHeight());
        setTopBlock(top);
        setMiddleBlock(middle);
        setBottomBlock(bottom);
    }

    private GameBlock getBlock(GameBlock block){
        if(block == null){
            return getClearBlock();
        } else {
            return block;
        }
    }

    public void draw(Batch batch, float a){
        super.draw(batch, a);
        //setPosition(this.getX(), this.getY());
        System.out.println(this);
    }

    public String toString(){
        return "this(" + this.getX() + "," + this.getY() + ")" +
                "top(" + top.getX() + "," + top.getY() + ")" +
                "middle(" + middle.getX() + "," + middle.getY() + ")" +
                "bottom(" + bottom.getX() + "," + bottom.getY() + ")";
    }

    private void setUpUiCells(){
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

    @Override
    public void setPosition(float x, float y){
        super.setPosition(x, y);
        top.setPosition(x, y);
        middle.setPosition(x, y + top.getHeight());
        bottom.setPosition(x, y + (top.getHeight() * 2));
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
            setTopBlock(UIUtil.getInstance().getGameBlock(Util.otoi(data[0])));
            setMiddleBlock(UIUtil.getInstance().getGameBlock(Util.otoi(data[1])));
            setBottomBlock(UIUtil.getInstance().getGameBlock(Util.otoi(data[2])));
        }
    }
}
