package pt.hidrogine.infinityedge.scene;

import pt.hidrogine.infinityedge.util.ShaderProgram;

/**
 * Created by Henrique on 12/02/2015.
 */
public interface Scene {

    public void update(float delta_t);
    public void draw(final ShaderProgram shader);
    public void end();
}
