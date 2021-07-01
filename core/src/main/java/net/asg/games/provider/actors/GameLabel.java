package net.asg.games.provider.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ObjectMap;

import net.asg.games.utils.Log4LibGDXLogger;
import net.asg.games.utils.Log4LibGDXLoggerService;

public class GameLabel extends Label {
    private final Direction direction;
    private Log4LibGDXLogger logger = Log4LibGDXLoggerService.forClass(GameLabel.class);
    private float velocity = 500;

    public enum Direction {LEFT, RIGHT, UP, DOWN}
    static private final Color tempColor = new Color(1, 1, 1, 1);

    private final BitmapFontCache cache;
    private final LabelStyle style;
    private boolean isAnimationFinished = true;
    private boolean isAnimationEnabled = false;
    private Vector2 finalCords;
    private boolean isSingle;

    public GameLabel(CharSequence text, Skin skin, Direction direction, boolean isEnabled) {
        super(text, skin);

        cache = getBitmapFontCache();
        style = getStyle();
        finalCords = new Vector2(0, 0);
        this.direction = direction;

        isAnimationFinished = false;
        isAnimationEnabled = isEnabled;

    }

    public GameLabel(CharSequence text, Skin skin) {
        this(text, skin, Direction.UP, true);
        ObjectMap<Class<?>, Boolean> actives = Log4LibGDXLoggerService.INSTANCE.getActives();
        System.err.println("actives? " + actives);
        System.err.println("contains key? " + actives.containsKey(GameLabel.class));
        System.err.println("is active? " + GameLabel.class);
        System.err.println("is active? " + Log4LibGDXLoggerService.INSTANCE.isActive(GameLabel.class));
    }

    public GameLabel(CharSequence text, Skin skin, boolean isEnabled) {
        this(text, skin, Direction.UP, isEnabled);
    }

    public void layout(){
        super.layout();
        reset();
    }

    public boolean isEnabled(){
        return isAnimationEnabled;
    }

    public void setVelocity(float v){
        velocity = v;
    }

    public float getVelocity(){
        return velocity;
    }

    private void computeCoords(){
        if(isAnimationEnabled){
            finalCords.x = getX();
            finalCords.y = getY();

            if(isAnimationFinished){
                isAnimationFinished = false;
                if(direction == Direction.UP){
                    setPosition(getX(), 0);
                }
            }
        }
    }

    public void reset(){
        computeCoords();
    }

    public void act(float delta){
        if(!isAnimationFinished){
            if(direction == Direction.UP){
                logger.debug("finalCords: ({},{})", finalCords.x, finalCords.y);
                logger.debug("cache: ({},{})", cache.getX(), cache.getY());
                logger.debug("GameLabel: ({},{})", getX(), getY());
                if(getY() < finalCords.y){
                    setY(getY() + (velocity * delta));
                } else {
                    isAnimationFinished = true;
                }
            }
        }
    }

    public void draw (Batch batch, float parentAlpha) {
        validate();
        Color color = tempColor.set(getColor());
        color.a *= parentAlpha;
        if (style.background != null) {
            batch.setColor(color.r, color.g, color.b, color.a);
            style.background.draw(batch, getX(), getY(), getWidth(), getHeight());
        }
        if (style.fontColor != null) color.mul(style.fontColor);
        cache.tint(color);
        cache.setPosition(getX(), getY());
        cache.draw(batch);
        logger.error("[]["+cache.getFont().getRegions());

    }
}
