package com.solar;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.solar.config.GameConfig;
import com.solar.data.DataManager;

public class MyGdxGame extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture image; // Ảnh Trái Đất

    private OrthographicCamera camera;
    private Viewport viewport;

    private World world;
    private Box2DDebugRenderer debugRenderer;

    @Override
    public void create() {
        batch = new SpriteBatch();

        // --- 1. SỬA ĐƯỜNG DẪN ẢNH ---
        // Lưu ý: Trong code chỉ viết "planets/earth.png" (Không viết "assets/")
        // LibGDX tự động tìm trong folder assets.
        try {
            image = new Texture("planets/earth.png");
        } catch (Exception e) {
            System.err.println("❌ Lỗi: Không tìm thấy file 'assets/planets/earth.png'. Kiểm tra lại tên file/thư mục!");
            // Tạo một ảnh tạm màu trắng nếu không tìm thấy để tránh crash
            // image = new Texture(1, 1, com.badlogic.gdx.graphics.Pixmap.Format.RGB888);
        }

        // Load Data (Giữ nguyên)
        try {
            DataManager.getInstance().loadStaticData();
        } catch (Exception e) { }

        camera = new OrthographicCamera();

        // Setup Viewport (Giả sử World rộng 16 mét, cao 9 mét)
        // Nếu GameConfig chưa có, thay số trực tiếp: new FitViewport(16, 9, camera);
        viewport = new FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, camera);
        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);

        Box2D.init();
        world = new World(new Vector2(0, -9.8f), true);
        debugRenderer = new Box2DDebugRenderer();

        // ... (Các dòng code cũ)
        world = new World(new Vector2(0, -9.8f), true);
        debugRenderer = new Box2DDebugRenderer();

// --- THÊM ĐOẠN NÀY ĐỂ TẠO VẬT THỂ RƠI ---

// 1. Định nghĩa tính chất vật lý (BodyDef)
        com.badlogic.gdx.physics.box2d.BodyDef bodyDef = new com.badlogic.gdx.physics.box2d.BodyDef();
        bodyDef.type = com.badlogic.gdx.physics.box2d.BodyDef.BodyType.DynamicBody; // Dynamic = Chịu tác động trọng lực
// Đặt vị trí ở giữa màn hình và trên cao (để nó rơi xuống)
        bodyDef.position.set(GameConfig.WORLD_WIDTH / 2, GameConfig.WORLD_HEIGHT - 2);

// 2. Tạo Body trong thế giới
        com.badlogic.gdx.physics.box2d.Body body = world.createBody(bodyDef);

// 3. Tạo hình dáng (Shape) - Hình vuông 1x1 mét
        com.badlogic.gdx.physics.box2d.PolygonShape shape = new com.badlogic.gdx.physics.box2d.PolygonShape();
        shape.setAsBox(0.5f, 0.5f); // Kích thước tính từ tâm (0.5 * 2 = 1 mét)

// 4. Tạo Fixture (Chất liệu, độ nảy)
        com.badlogic.gdx.physics.box2d.FixtureDef fixtureDef = new com.badlogic.gdx.physics.box2d.FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;      // Mật độ (để tính khối lượng)
        fixtureDef.friction = 0.5f;   // Ma sát
        fixtureDef.restitution = 0.6f; // Độ nảy (rơi xuống đất sẽ tưng lên)

// Gắn Fixture vào Body
        body.createFixture(fixtureDef);

// Dọn dẹp Shape sau khi dùng xong (quan trọng để không tốn RAM)
        shape.dispose();

// --- TẠO MẶT ĐẤT (Để vật rơi xuống có chỗ đứng) ---
        com.badlogic.gdx.physics.box2d.BodyDef groundDef = new com.badlogic.gdx.physics.box2d.BodyDef();
        groundDef.type = com.badlogic.gdx.physics.box2d.BodyDef.BodyType.StaticBody; // Static = Đứng yên
        groundDef.position.set(GameConfig.WORLD_WIDTH / 2, 1); // Cách đáy 1 mét

        com.badlogic.gdx.physics.box2d.Body groundBody = world.createBody(groundDef);
        com.badlogic.gdx.physics.box2d.PolygonShape groundShape = new com.badlogic.gdx.physics.box2d.PolygonShape();
        groundShape.setAsBox(GameConfig.WORLD_WIDTH / 2, 0.5f); // Dài hết màn hình, dày 1 mét

        groundBody.createFixture(groundShape, 0.0f);
        groundShape.dispose();
    }

    @Override
    public void render() {
        // Logic Update
        float delta = Gdx.graphics.getDeltaTime();
        world.step(delta, 6, 2);
        camera.update();

        // --- 2. VẼ (RENDER) ---
        ScreenUtils.clear(0.1f, 0.1f, 0.15f, 1f); // Màu nền vũ trụ tối

        // Đồng bộ Batch với Camera (Để vẽ theo đơn vị mét thay vì pixel)
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        if (image != null) {
            // --- QUAN TRỌNG: VẼ THEO ĐƠN VỊ MÉT ---
            // Nếu ảnh 500px vẽ vào thế giới 16 mét sẽ cực to. Ta phải thu nhỏ lại.
            // Ví dụ: Vẽ Trái đất rộng 2 mét, cao 2 mét, đặt ở giữa màn hình.
            float width = 2f;
            float height = 2f;
            float x = (viewport.getWorldWidth() - width) / 2; // Căn giữa X
            float y = (viewport.getWorldHeight() - height) / 2; // Căn giữa Y

            batch.draw(image, x, y, width, height);
        }
        batch.end();

        // Vẽ Debug Box2D đè lên trên để kiểm tra
        debugRenderer.render(world, camera.combined);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        if (world != null) world.dispose();
        if (debugRenderer != null) debugRenderer.dispose();
        if (batch != null) batch.dispose();
        if (image != null) image.dispose();
    }
}
