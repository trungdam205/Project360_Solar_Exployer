package com.solar.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.solar.MainGame;
import com.solar.config.GameConfig;

public class GameScreen extends BaseScreen {

    // --- CẤU HÌNH BOX2D ---
    // PPM (Pixels Per Meter): Quy đổi 1 mét trong Box2D = 32 pixels màn hình
    // Box2D tính toán bằng mét, nên cần số này để vẽ đúng tỉ lệ.
    private static final float PPM = 32f;

    // --- CÁC BIẾN VẬT LÝ ---
    private World world;
    private Box2DDebugRenderer debugRenderer; // Để vẽ khung dây xanh lá (debug)
    private OrthographicCamera gameCamera;    // Camera quay thế giới game (khác camera UI)

    // --- NHÂN VẬT (PLAYER) ---
    private Body playerBody;
    private TextureRegion playerTexture;
    private TextureAtlas atlas;

    public GameScreen(MainGame game) {
        super(game);
    }

    @Override
    public void show() {
        // 1. Dùng GameConfig.PPM thay vì số 32f
        gameCamera.setToOrtho(false,
            GameConfig.WORLD_WIDTH / GameConfig.PPM,
            GameConfig.WORLD_HEIGHT / GameConfig.PPM);
        // 1. SETUP CAMERA
        gameCamera = new OrthographicCamera();
        // Zoom camera: viewportWidth / PPM để nhìn thấy thế giới theo hệ mét
        gameCamera.setToOrtho(false, Gdx.graphics.getWidth() / PPM, Gdx.graphics.getHeight() / PPM);

        // 2. SETUP THẾ GIỚI VẬT LÝ (ZERO GRAVITY)
        // Vector2(0, 0) -> Không trọng lực (Top-down space)
        world = new World(new Vector2(0, 0), true);
        debugRenderer = new Box2DDebugRenderer();

        // 3. LẤY ẢNH TỪ ATLAS
        // Đảm bảo atlas đã load ở LoadingScreen
        if (game.getAssetManager().isLoaded("images/assets.atlas")) {
            atlas = game.getAssetManager().get("images/assets.atlas", TextureAtlas.class);
            // Lấy vùng ảnh nhân vật (Ví dụ tên là "idle" hoặc "rocket" trong file atlas của bạn)
            playerTexture = atlas.findRegion("idle");
        }

        // 4. TẠO NHÂN VẬT
        createPlayer();

        // Setup nút Back từ BaseScreen
        addBackButton(() -> game.setScreen(new SolarSystemScreen(game)));
    }

    private void createPlayer() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        // Đặt vị trí ban đầu (theo mét, ví dụ: giữa màn hình ảo)
        bodyDef.position.set(10, 10);

        // --- QUAN TRỌNG: MA SÁT KHÔNG GIAN ---
        bodyDef.linearDamping = 1.0f;  // Lực cản giúp tàu dừng lại khi thả phím
        bodyDef.angularDamping = 1.0f; // Lực cản xoay

        playerBody = world.createBody(bodyDef);

        // Tạo hình dáng (Fixture)
        CircleShape shape = new CircleShape();
        shape.setRadius(0.5f); // Bán kính 0.5 mét (tương đương 16 pixel)

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;     // Mật độ (để tính khối lượng)
        fixtureDef.friction = 0.4f;    // Ma sát bề mặt
        fixtureDef.restitution = 0.5f; // Độ đàn hồi (nảy)

        playerBody.createFixture(fixtureDef);
        shape.dispose(); // Hủy shape sau khi tạo xong
    }

    // Xử lý nút bấm di chuyển
    private void handleInput(float delta) {
        float force = 10f; // Lực đẩy động cơ

        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            // Đẩy vật thể lên trên (Apply Force vào tâm)
            playerBody.applyForceToCenter(0, force, true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            playerBody.applyForceToCenter(0, -force, true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            playerBody.applyForceToCenter(-force, 0, true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            playerBody.applyForceToCenter(force, 0, true);
        }
    }

    @Override
    public void render(float delta) {
        // 2. Dùng TIME_STEP thay vì 1/60f
        world.step(GameConfig.TIME_STEP, 6, 2);

        // 3. Kiểm tra DEBUG_MODE trước khi vẽ debug
        if (GameConfig.DEBUG_MODE) {
            debugRenderer.render(world, gameCamera.combined);
        }
        // 1. XỬ LÝ INPUT
        handleInput(delta);

        // 2. CẬP NHẬT BOX2D
        // 60 lần/giây, độ chính xác velocity 6, position 2
        world.step(1 / 60f, 6, 2);

        // 3. CẬP NHẬT CAMERA
        // Camera bám theo nhân vật
        gameCamera.position.set(playerBody.getPosition().x, playerBody.getPosition().y, 0);
        gameCamera.update();

        // 4. XÓA MÀN HÌNH
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // 5. VẼ GAME (SPRITE)
        // Set camera cho batch để vẽ đúng vị trí thế giới
        game.getBatch().setProjectionMatrix(gameCamera.combined);
        game.getBatch().begin();
        if (playerTexture != null) {
            // Vẽ texture tại vị trí Body
            // Lưu ý: Body tính từ tâm, Draw tính từ góc dưới trái -> Cần trừ radius
            // Và nhân với PPM để đổi từ mét sang pixel
            float drawX = (playerBody.getPosition().x * PPM) - (playerTexture.getRegionWidth() / 2f);
            float drawY = (playerBody.getPosition().y * PPM) - (playerTexture.getRegionHeight() / 2f);

            // Vẽ player (có thể thêm logic xoay theo playerBody.getAngle())
            game.getBatch().draw(playerTexture, drawX, drawY);
        }
        game.getBatch().end();

        // 6. VẼ DEBUG BOX2D (Khung dây xanh lá)
        // RenderBox2D cần matrix tỉ lệ theo mét
        debugRenderer.render(world, gameCamera.combined);

        // 7. VẼ UI (Nút Back, HUD...)
        // Stage dùng viewport riêng, không bị ảnh hưởng bởi gameCamera
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        // Update viewport cho gameCamera
        gameCamera.viewportWidth = width / PPM;
        gameCamera.viewportHeight = height / PPM;
        gameCamera.update();
    }

    @Override
    public void dispose() {
        super.dispose();
        world.dispose();
        debugRenderer.dispose();
    }

    @Override
    public void resume() {
        // Resume the screen (delegate to BaseScreen)
        super.resume();
    }
}
