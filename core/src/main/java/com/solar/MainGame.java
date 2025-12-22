package com.solar;

import com.solar.physics.WorldContactListener;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.solar.entities.Player;

public class MainGame extends ApplicationAdapter {

    private static final float PPM = 100f;

    private World world;
    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera camera;

    private SpriteBatch batch;
    private Player player;

    @Override
    public void create() {
        world = new World(new Vector2(0, -9.8f), true);
        debugRenderer = new Box2DDebugRenderer();

        world.setContactListener(new WorldContactListener());

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800 / PPM, 480 / PPM);

        createGround();

        batch = new SpriteBatch();
        player = new Player(world);
    }

    @Override
    public void render() {
        world.step(1 / 60f, 6, 2);

        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        player.update(Gdx.graphics.getDeltaTime());

        camera.update();
        batch.setProjectionMatrix(camera.combined.cpy().scl(PPM));
        debugRenderer.render(world, camera.combined);

        batch.begin();
        player.render(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        world.dispose();
        debugRenderer.dispose();
        batch.dispose();
        player.dispose();
    }

    private void createGround() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(4f, 1f);

        Body ground = world.createBody(bodyDef);
        ground.setUserData("ground");

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(20f, 0.5f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.friction = 0.8f;

        ground.createFixture(fixtureDef);
        shape.dispose();
    }
}
