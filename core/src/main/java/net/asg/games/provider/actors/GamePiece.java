package net.asg.games.provider.actors;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.github.czyzby.lml.scene2d.ui.reflected.AnimatedImage;

import net.asg.games.game.objects.YokelBlock;
import net.asg.games.utils.UIUtil;
import net.asg.games.utils.Util;

public class GamePiece extends Actor {
    private GameBlock top;
    private GameBlock middle;
    private GameBlock bottom;
    private Skin skin;
    private Table table;

    public GamePiece(){
        this(null,null,null,null);
    }

    public GamePiece(Skin skin, GameBlock top, GameBlock mid, GameBlock bottom){
        setDebug(true);
        setSkin(skin);
        initialize();
        initializeUiCells();
        //validate();
        setTopBlock(top);
        setMiddleBlock(mid);
        setBottomBlock(bottom);
    }

    public GamePiece(Skin skin, int top, int mid, int bottom){
        this(skin, UIUtil.getInstance().getGameBlock(top), UIUtil.getInstance().getGameBlock(mid), UIUtil.getInstance().getGameBlock(bottom));
    }

    private void initialize(){
        this.top = getClearBlock();
        this.middle = getClearBlock();
        this.bottom = getClearBlock();
        setWidth(top.getImage().getDrawable().getMinWidth());
        setHeight(top.getImage().getDrawable().getMinHeight() * 3);
        this.table = new Table(skin);
        table.setDebug(getDebug());
        top.setDebug(getDebug());
        table.setBounds(getX(), getY(), getWidth(), getHeight());
    }

    private void initializeUiCells(){
        table.add(getTopBlock()).row();
        table.add(getMiddleBlock()).row();
        table.add(getBottomBlock());
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
            setTopBlock(UIUtil.getInstance().getGameBlock(Util.otoi(data[0])));
            setMiddleBlock(UIUtil.getInstance().getGameBlock(Util.otoi(data[1])));
            setBottomBlock(UIUtil.getInstance().getGameBlock(Util.otoi(data[2])));
        }
    }

    public Array<AnimatedImage> getBlockImages(){
        Array<AnimatedImage> images = new Array<>();
        images.add(getTopBlock().getImage());
        images.add(getMiddleBlock().getImage());
        images.add(getBottomBlock().getImage());
        return images;
    }

    public void setSkin (Skin skin) {
        this.skin = skin;
    }

    public Skin getSkin(){
        return this.skin;
    }

    @Override
    public void act(float delta){
        super.act(delta);
        table.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha){
        //validate();
        //batch.begin();
        table.draw(batch, parentAlpha);
        super.draw(batch, parentAlpha);

        //printBounds();
        //printDebugBlocks();
    }

    @Override
    protected void positionChanged() {
        super.positionChanged();
        setPosition(getX(),getY());
        table.setPosition(getX(),getY());
        printBounds();
    }

    private void printDebugBlocks(){
        System.out.println("top=" + top.getImage().getDrawable());
        System.out.println("middle=" + middle.getImage().getDrawable());
        System.out.println("bottom=" + bottom.getImage().getDrawable());
    }

    private void printBounds(){
        System.out.println("table=(" + table.getX() + "," + table.getY() + ")[w:" + table.getWidth() + " h:" + table.getHeight() + "]");
        System.out.println("top=(" + top.getX() + "," + top.getY() + ")[w:" + top.getWidth() + " h:" + top.getHeight() + "]");
        System.out.println("mid=(" + middle.getX() + "," + middle.getY() + ")[w:" + middle.getWidth() + " h:" + middle.getHeight() + "]");
        System.out.println("bottom=(" + bottom.getX() + "," + bottom.getY() + ")[w:" + bottom.getWidth() + " h:" + bottom.getHeight() + "]");
    }
}
