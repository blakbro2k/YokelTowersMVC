package net.asg.games.game.managers;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;

import org.mockito.Mockito;

/*******************************************************************************
 * Copyright 2015 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
public class ServerGameRunner implements ApplicationListener, Disposable {
    private ServerManager daemon;

    public ServerGameRunner(ServerManager manager) {
        //super(klass);
        this.daemon = manager;
        HeadlessApplicationConfiguration conf = new HeadlessApplicationConfiguration();

        new HeadlessApplication(this, conf);
        // Use Mockito to mock the OpenGL methods since we are running headlessly
        Gdx.gl20 = Mockito.mock(GL30.class);
        Gdx.gl = Gdx.gl20;
    }

    @Override
    public void create() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void render() {
        try {
            if (daemon == null) throw new GdxRuntimeException("Server daemon has evaporated!");
            //Get all games from Server Manager
            final ObjectMap.Values<GameManager> games = daemon.getAllGames();

            //For each game, lets perform our state actions
            for(GameManager game : games){
                if(game != null){
                    //Check if game is ready to start
                    //checkIsGameReady(game);

                    //Check if Count down is starting
                    //checkGameStart(game);

                    //Handle Player input From Network
                    //daemon.handleServerMessages(game);

                    //Update game state per
                    game.update(Gdx.app.getGraphics().getDeltaTime());
                }
            }
            //Put updated game in source
            daemon.putAllGames(games);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void dispose() {
        if(daemon != null){
            daemon.shutDownServer(-1);
        }
    }
}
