package net.asg.games.controller.dialog;

    import com.badlogic.gdx.graphics.g2d.Batch;
    import com.badlogic.gdx.scenes.scene2d.Actor;
    import com.badlogic.gdx.scenes.scene2d.Stage;
    import com.github.czyzby.autumn.annotation.Inject;
    import com.github.czyzby.autumn.mvc.stereotype.ViewDialog;
    import com.github.czyzby.lml.annotation.LmlAction;
    import com.github.czyzby.lml.parser.action.ActionContainer;
    import com.kotcrab.vis.ui.VisUI.SkinScale;

    import net.asg.games.controller.ControllerNames;
    import net.asg.games.service.ScaleService;
    import net.asg.games.utils.GlobalConstants;

/** This is a settings dialog, which can be shown in any views by using "show:settings" LML action or - in Java code -
     * through InterfaceService.showDialog(Class) method. Thanks to the fact that it implements ActionContainer, its methods
     * will be available in the LML template. */
    @ViewDialog(id = GlobalConstants.SETTINGS_DIALOG, value = GlobalConstants.SETTINGS_DIALOG_PATH)
    public class SettingsController implements ActionContainer {
        // @Inject-annotated fields will be automatically filled with values from the context.
        @Inject private ScaleService scaleService;

        /** @return array of available GUI scales. */
        @LmlAction("scales")
        public SkinScale[] getGuiScales() {
            return scaleService.getScales();
        }

        /** @param actor requested scale change. Its ID represents a GUI scale. */
        @LmlAction("changeScale")
        public void changeGuiScale(final Actor actor) {
            final SkinScale scale = scaleService.getPreference().extractFromActor(actor);
            scaleService.changeScale(scale);
        }


    public void draw (Batch batch, float parentAlpha) {
        //validate();
        //super.draw(batch, parentAlpha);
        System.err.println("Draw?");
        }
    }