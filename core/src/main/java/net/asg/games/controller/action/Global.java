package net.asg.games.controller.action;

import com.badlogic.gdx.utils.Array;
import com.github.czyzby.autumn.annotation.Inject;
import com.github.czyzby.autumn.mvc.component.ui.InterfaceService;
import com.github.czyzby.autumn.mvc.stereotype.ViewActionContainer;
import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;
import com.github.czyzby.lml.annotation.LmlAction;
import com.github.czyzby.lml.parser.action.ActionContainer;

import net.asg.games.controller.dialog.ErrorController;
import net.asg.games.service.SessionService;
import net.asg.games.utils.PostLoader;
import net.asg.games.utils.Util;

/**
 * Since this class implements ActionContainer and is annotated with ViewActionContainer, its methods will be reflected
 * and available in all LML templates. Note that this class is a component like any other, so it can inject any fields,
 * use Initiate-annotated methods, etc.
 */
@ViewActionContainer("global")
public class Global implements ActionContainer {
    @Inject private SessionService sessionService;
    @Inject private InterfaceService interfaceService;

    /**
     * This is a mock-up method that does nothing. It will be available in LML templates through "close" (annotation
     * argument) and "noOp" (method name) IDs.
     */
    @LmlAction("close")
    public void noOp() {
    }

    @LmlAction("isUITest")
    public boolean isUITest(){
        System.out.println("isUITestResult  called.");
        return PostLoader.UI_TEST.equalsIgnoreCase(PostLoader.getInstance().getPreLoader());
    }

    @LmlAction("isDebug")
    public boolean isDebug(){
        System.out.println("isDebugResult  called.");
        return PostLoader.DEBUG.equalsIgnoreCase(PostLoader.getInstance().getPreLoader());
    }
}
