package com.solar. actor;

import com.badlogic. gdx.graphics. Color;
import com.badlogic. gdx.graphics. glutils.ShapeRenderer;
import com. badlogic.gdx.math.MathUtils;

/**
 * Star background actor for space scenes
 */
public class Star {
    public float x, y;
    public float size;
    public float twinkleDuration;
    public float time;
    public float baseOpacity;
    public Color color;

    public Star(float x, float y, float size, float twinkleDuration) {
        this. x = x;
        this.y = y;
        this.size = size;
        this.twinkleDuration = twinkleDuration;
        this.time = MathUtils.random(twinkleDuration);
        this.baseOpacity = MathUtils.random(0.3f, 0.8f);

        float colorVar = MathUtils. random();
        if (colorVar < 0.7f) {
            color = Color.WHITE;
        } else if (colorVar < 0.85f) {
            color = new Color(0.8f, 0.9f, 1f, 1f);
        } else {
            color = new Color(1f, 0.95f, 0.8f, 1f);
        }
    }

    public void update(float delta) {
        time += delta;
        if (time > twinkleDuration) {
            time = 0;
        }
    }

    public void render(ShapeRenderer renderer) {
        float progress = time / twinkleDuration;
        float opacity = baseOpacity + (1f - baseOpacity) * MathUtils.sin(progress * MathUtils.PI);
        float currentSize = size * (1f + 0.3f * MathUtils.sin(progress * MathUtils.PI));

        renderer.setColor(color.r, color.g, color.b, opacity);
        renderer.circle(x, y, currentSize, 6);

        if (size > 2f) {
            renderer.setColor(color.r, color.g, color. b, opacity * 0.3f);
            renderer.circle(x, y, currentSize * 1.5f, 8);
        }
    }
}
