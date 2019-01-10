package net.asg.games.configuration.preferences;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.github.czyzby.autumn.mvc.component.preferences.dto.AbstractPreference;
import com.github.czyzby.lml.util.LmlUtilities;

public class NetworkPreference extends AbstractPreference {

    @Override
    protected Object convert(String rawPreference) {
        return null;
    }

    @Override
    protected String serialize(Object preference) {
        return null;
    }

    @Override
    public Object getDefault() {
        return null;
    }

    @Override
    public Object extractFromActor(Actor actor) {
        return  null;
    }
}
