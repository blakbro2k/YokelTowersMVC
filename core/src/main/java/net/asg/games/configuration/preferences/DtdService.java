package net.asg.games.configuration.preferences;

import com.badlogic.gdx.Gdx;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.util.LmlApplicationListener;

public class DtdService extends LmlApplicationListener {
    @Override
    protected LmlParser createParser() {
        saveDtdSchema(Gdx.files.local("lml.dtd"));
        return null;
    }


}
