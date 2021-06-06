package net.asg.games.controller.dialog;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.TimeUtils;
import com.github.czyzby.autumn.annotation.Inject;
import com.github.czyzby.autumn.mvc.component.ui.controller.ViewDialogShower;
import com.github.czyzby.autumn.mvc.stereotype.ViewDialog;
import com.github.czyzby.lml.annotation.LmlActor;
import com.github.czyzby.lml.parser.action.ActionContainer;

import net.asg.games.service.UserInterfaceService;
import net.asg.games.utils.GlobalConstants;
import net.asg.games.utils.Log4LibGDXLogger;
import net.asg.games.utils.Log4LibGDXLoggerService;

/** This is a settings dialog, which can be shown in any views by using "show:settings" LML action or - in Java code -
 * through InterfaceService.showDialog(Class) method. Thanks to the fact that it implements ActionContainer, its methods
 * will be available in the LML template. */
@ViewDialog(id = GlobalConstants.NEXT_GAME_DIALOG, value = GlobalConstants.NEXT_GAME_PATH)
public class NextGameController implements ActionContainer, ViewDialogShower {
    public static final int NEXT_GAME_SECONDS = 15;
    private Log4LibGDXLogger logger = Log4LibGDXLoggerService.forClass(NextGameController.class);
    @Inject private UserInterfaceService uiService;
    @LmlActor("timerLabel") private Label timerLabel;

    @Override
    public void doBeforeShow(Window dialog) {
        logger.enter("doBeforeShow");
        uiService.getSoundFXFactory().playGameStartSound();

        if(timerLabel != null) {
            CountLabelToAction countLabelToAction = countDownTo();
            timerLabel.addAction(Actions.sequence(countLabelToAction, Actions.removeAction(countLabelToAction, timerLabel)));
        }
        logger.exit("doBeforeShow");
    }

    private static class CountLabelToAction extends Action {
        private Log4LibGDXLogger logger = Log4LibGDXLoggerService.forClass(CountLabelToAction.class);

        private long start;
        private int end;
        private boolean reverse, began, complete;

        protected void update(int time) {
            Actor label = getActor();
            if(label instanceof Label){
                ((Label) label).setText(time);
            }
        }

        void setCountDown(int i) {
            end = i;
        }

        public void setReverse(boolean reverse) {
            this.reverse = reverse;
        }

        int getCountDown() {
            return end;
        }

        public boolean getReverse() {
            return reverse;
        }

        /** Called the first time {@link #act(float)} is called. This is a good place to query the {@link #actor actor's} starting
         * state. */
        void begin() {
            start = TimeUtils.millis();
        }

        /** Called the last time {@link #act(float)} is called. */
        protected void end () {
        }

        int getElapsedSeconds(){
            return (int) ((TimeUtils.millis() - start) / 1000);
        }

        public void restart () {
            start = -1;
            end = -1;
            began = false;
            complete = false;
        }

        public void reset () {
            super.reset();
            reverse = false;
            restart();
        }

        @Override
        public boolean act (float delta) {
            if (complete) return true;
            Pool pool = getPool();
            setPool(null); // Ensure this action can't be returned to the pool while executing.
            try {
                if (!began) {
                    begin();
                    began = true;
                }
                int elapsed = getElapsedSeconds();
                complete = elapsed >= end;
                if(reverse){
                    update(elapsed);
                } else {
                    update(end - elapsed);
                }
                if (complete) end();
                return complete;
            } finally {
                setPool(pool);
            }
        }
    }

    private static CountLabelToAction countDownTo() {
        CountLabelToAction action = Actions.action(CountLabelToAction.class);
        action.setCountDown(NEXT_GAME_SECONDS);
        return action;
    }
}