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
    static final private int GRID_OFFSET = 6;

    public GamePiece(Skin skin){
        this(skin,null,null,null);
    }

    public GamePiece(Skin skin, GameBlock top, GameBlock mid, GameBlock bottom){
        setSkin(skin);
        initialize(top, mid, bottom);
        setUpUiCells();
    }

    public GamePiece(Skin skin, int top, int mid, int bottom){
        this(skin, UIUtil.getInstance().getGameBlock(top), UIUtil.getInstance().getGameBlock(mid), UIUtil.getInstance().getGameBlock(bottom));
    }

    public GamePiece(Skin skin, String[] data){
        this(skin);
        setSkin(skin);
        setData(data);
    }

    private void initialize(GameBlock top, GameBlock middle, GameBlock bottom){
        this.top = getBlock(top);
        this.middle = getBlock(middle);
        this.bottom = getBlock(bottom);
        this.setWidth(this.top.getWidth());
        this.setHeight(this.top.getHeight() * 3);
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

    public void setDebug(boolean enabled) {
        super.setDebug(enabled);
    }

//    public void act(float delta){
 //       setPosition(getX(), getY());
   // }

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
        return UIUtil.getInstance().getGameBlock(YokelBlock.CLEAR_BLOCK);
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

    public void setData(String[] data) {
        if(data != null && data.length < 4){
            setTopBlock(UIUtil.getInstance().getGameBlock(Util.otoi(data[0])));
            setMiddleBlock(UIUtil.getInstance().getGameBlock(Util.otoi(data[1])));
            setBottomBlock(UIUtil.getInstance().getGameBlock(Util.otoi(data[2])));
        }
    }
}
