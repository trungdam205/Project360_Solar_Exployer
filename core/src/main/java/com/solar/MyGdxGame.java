package com.solar;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.solar.config.GameConfig; // Import class config vừa tạo

public class MyGdxGame extends ApplicationAdapter {

    // --- Task 2.2: Camera & Viewport ---
    private OrthographicCamera camera;
    private Viewport viewport;

    // --- Task 3.1: Box2D Variables ---
    private World world;
    private Box2DDebugRenderer debugRenderer;

    @Override
    public void create() {
        // --- Task 2.2: Setup Camera & Viewport ---
        // Tạo camera
        camera = new OrthographicCamera();

        // Setup Viewport: Sử dụng FitViewport để giữ tỷ lệ khung hình [4]
        // Sử dụng đơn vị World (mét) từ GameConfig để ánh xạ trực tiếp với Box2D
        viewport = new FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, camera);

        // Căn giữa camera (thường được xử lý bởi viewport.update trong resize, nhưng có thể set ban đầu)
        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);

        // --- Task 3.1: Khởi tạo Box2D ---
        // Khởi tạo hệ thống Box2D (quan trọng để load các thư viện native)
        Box2D.init();

        // Tạo World với trọng lực (0, -9.8f) mô phỏng trái đất [6]
        // true: cho phép các body ngủ (sleeping) để tối ưu hiệu năng khi không chuyển động
        world = new World(new Vector2(0, -9.8f), true);

        // Tạo DebugRenderer để vẽ các đường bao vật lý (hitbox) giúp debug [5]
        debugRenderer = new Box2DDebugRenderer();
    }

    @Override
    public void render() {
        // --- Task 3.2: Update World & Render ---

        // 1. Clear screen mỗi frame (Xóa màn hình cũ) [7]
        // Sử dụng màu đen hoặc màu tùy ý
        ScreenUtils.clear(0, 0, 0, 1); // R, G, B, Alpha

        // 2. Logic Step: Cập nhật vật lý Box2D [5]
        // step(timeStep, velocityIterations, positionIterations)
        // timeStep: thời gian trôi qua giữa các frame (delta)
        // 6 và 2 là các giá trị iteration khuyến nghị cho độ chính xác [5]
        float delta = Gdx.graphics.getDeltaTime();
        world.step(delta, 6, 2);

        // 3. Update Camera
        camera.update();

        // 4. Render Debug Box2D [5]
        // Vẽ thế giới vật lý lên màn hình thông qua camera
        debugRenderer.render(world, camera.combined);
    }

    @Override
    public void resize(int width, int height) {
        // Cập nhật viewport khi kích thước cửa sổ thay đổi [8]
        // true: căn giữa camera
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        // Giải phóng tài nguyên khi thoát game để tránh rò rỉ bộ nhớ [9]
        if (world != null) world.dispose();
        if (debugRenderer != null) debugRenderer.dispose();
    }
}
