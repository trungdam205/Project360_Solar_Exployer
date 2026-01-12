package com.solar;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class SkinGenerator {

    public static Skin createSkin() {
        Skin skin = new Skin();

        // Create pixmap for textures
        Pixmap pixmap = new Pixmap(256, 256, Pixmap.Format. RGBA8888);

        // White pixel (1,1 - 3x3)
        pixmap. setColor(Color. WHITE);
        pixmap.fillRectangle(1, 1, 3, 3);

        // Button normal (5,1 - 24x24)
        drawButton(pixmap, 5, 1, 24, 24,
            new Color(0.08f, 0.15f, 0.4f, 0.9f),
            new Color(0.3f, 0.5f, 1f, 0.4f));

        // Button down (30,1 - 24x24)
        drawButton(pixmap, 30, 1, 24, 24,
            new Color(0.15f, 0.25f, 0.5f, 0.9f),
            new Color(0.5f, 0.7f, 1f, 0.7f));

        // Button checked (55,1 - 24x24)
        drawButton(pixmap, 55, 1, 24, 24,
            new Color(0.2f, 0.35f, 0.6f, 0.9f),
            new Color(0.6f, 0.8f, 1f, 0.8f));

        // Button over (80,1 - 24x24)
        drawButton(pixmap, 80, 1, 24, 24,
            new Color(0.12f, 0.22f, 0.5f, 0.9f),
            new Color(0.4f, 0.6f, 1f, 0.6f));

        // Window (1,51 - 48x48)
        drawWindow(pixmap, 1, 51, 48, 48);

        // Slider background horizontal (50,51 - 48x12)
        drawSliderBg(pixmap, 50, 51, 48, 12);

        // Slider knob normal (112,51 - 14x20)
        drawSliderKnob(pixmap, 112, 51, 14, 20, new Color(0.4f, 0.6f, 1f, 1f));

        // Slider knob over (127,51 - 14x20)
        drawSliderKnob(pixmap, 127, 51, 14, 20, new Color(0.5f, 0.7f, 1f, 1f));

        // Slider knob down (142,51 - 14x20)
        drawSliderKnob(pixmap, 142, 51, 14, 20, new Color(0f, 1f, 1f, 1f));

        // Scroll bar vertical (100,100 - 10x32)
        drawScrollBar(pixmap, 100, 100, 10, 32);

        // Scroll knob vertical (115,100 - 10x24)
        drawScrollKnob(pixmap, 115, 100, 10, 24);

        // Scroll bar horizontal (130,100 - 32x10)
        drawScrollBar(pixmap, 130, 100, 32, 10);

        // Scroll knob horizontal (165,100 - 24x10)
        drawScrollKnob(pixmap, 165, 100, 24, 10);

        // List background (1,121 - 24x24)
        drawButton(pixmap, 1, 121, 24, 24,
            new Color(0.03f, 0.06f, 0.12f, 0.95f),
            new Color(0.2f, 0.4f, 0.8f, 0.3f));

        // Selection (30,26 - 16x16)
        pixmap.setColor(new Color(0.3f, 0.6f, 0.8f, 0.5f));
        pixmap.fillRectangle(30, 26, 16, 16);

        // Create texture from pixmap
        Texture texture = new Texture(pixmap);
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        pixmap.dispose();

        // Add texture regions to skin
        skin.add("white", new TextureRegion(texture, 1, 1, 3, 3));
        skin.add("button", new TextureRegion(texture, 5, 1, 24, 24));
        skin.add("button-down", new TextureRegion(texture, 30, 1, 24, 24));
        skin.add("button-checked", new TextureRegion(texture, 55, 1, 24, 24));
        skin.add("button-over", new TextureRegion(texture, 80, 1, 24, 24));
        skin.add("window", new TextureRegion(texture, 1, 51, 48, 48));
        skin.add("slider-background", new TextureRegion(texture, 50, 51, 48, 12));
        skin.add("slider-knob", new TextureRegion(texture, 112, 51, 14, 20));
        skin.add("slider-knob-over", new TextureRegion(texture, 127, 51, 14, 20));
        skin.add("slider-knob-down", new TextureRegion(texture, 142, 51, 14, 20));
        skin.add("scroll-vertical", new TextureRegion(texture, 100, 100, 10, 32));
        skin.add("scroll-knob-vertical", new TextureRegion(texture, 115, 100, 10, 24));
        skin.add("scroll-horizontal", new TextureRegion(texture, 130, 100, 32, 10));
        skin.add("scroll-knob-horizontal", new TextureRegion(texture, 165, 100, 24, 10));
        skin.add("list-background", new TextureRegion(texture, 1, 121, 24, 24));
        skin.add("selection", new TextureRegion(texture, 30, 26, 16, 16));

        // Add font
        BitmapFont font = new BitmapFont();
        font.getData().setScale(1f);
        font.setColor(Color.WHITE);
        skin.add("default-font", font);

        // Add colors
        skin.add("white", Color.WHITE);
        skin.add("black", Color.BLACK);
        skin.add("gray", Color.GRAY);
        skin.add("cyan", Color.CYAN);
        skin.add("light-blue", new Color(0.5f, 0.8f, 1f, 1f));

        // Build all styles
        buildLabelStyles(skin);
        buildButtonStyles(skin);
        buildSliderStyles(skin);
        buildWindowStyles(skin);
        buildScrollPaneStyles(skin);
        buildListStyles(skin);

        return skin;
    }

    // ==================== DRAWING METHODS ====================

    private static void drawButton(Pixmap pixmap, int x, int y, int w, int h, Color bg, Color border) {
        pixmap.setColor(bg);
        pixmap.fillRectangle(x, y, w, h);

        pixmap.setColor(border);
        pixmap.drawRectangle(x, y, w, h);
        pixmap.drawRectangle(x + 1, y + 1, w - 2, h - 2);

        pixmap.setColor(border.r + 0.1f, border.g + 0.1f, border.b + 0.1f, 0.3f);
        pixmap.drawLine(x + 2, y + 2, x + w - 3, y + 2);
    }

    private static void drawWindow(Pixmap pixmap, int x, int y, int w, int h) {
        pixmap. setColor(new Color(0.04f, 0.08f, 0.16f, 0.95f));
        pixmap.fillRectangle(x, y, w, h);

        pixmap.setColor(new Color(0.08f, 0.15f, 0.3f, 0.98f));
        pixmap.fillRectangle(x, y, w, 24);

        pixmap.setColor(new Color(0.3f, 0.5f, 1f, 0.5f));
        pixmap.drawRectangle(x, y, w, h);

        pixmap.setColor(new Color(0.3f, 0.5f, 1f, 0.3f));
        pixmap.drawLine(x, y + 24, x + w, y + 24);
    }

    private static void drawSliderBg(Pixmap pixmap, int x, int y, int w, int h) {
        pixmap. setColor(new Color(0.05f, 0.1f, 0.2f, 0.8f));
        pixmap.fillRectangle(x, y, w, h);

        pixmap.setColor(new Color(0.2f, 0.4f, 0.8f, 0.4f));
        pixmap.drawRectangle(x, y, w, h);
    }

    private static void drawSliderKnob(Pixmap pixmap, int x, int y, int w, int h, Color color) {
        pixmap.setColor(color);
        pixmap.fillRectangle(x + 2, y + 2, w - 4, h - 4);

        Color borderColor = new Color(
            Math.min(1f, color.r + 0.2f),
            Math.min(1f, color.g + 0.2f),
            Math.min(1f, color.b + 0.2f),
            1f
        );
        pixmap.setColor(borderColor);
        pixmap.drawRectangle(x, y, w, h);
    }

    private static void drawScrollBar(Pixmap pixmap, int x, int y, int w, int h) {
        pixmap.setColor(new Color(0.05f, 0.1f, 0.2f, 0.6f));
        pixmap.fillRectangle(x, y, w, h);
    }

    private static void drawScrollKnob(Pixmap pixmap, int x, int y, int w, int h) {
        pixmap.setColor(new Color(0.3f, 0.5f, 1f, 0.7f));
        pixmap.fillRectangle(x, y, w, h);
    }

    // ==================== STYLE BUILDERS ====================

    private static void buildLabelStyles(Skin skin) {
        Label.LabelStyle defaultStyle = new Label. LabelStyle();
        defaultStyle.font = skin.getFont("default-font");
        defaultStyle.fontColor = Color.WHITE;
        skin.add("default", defaultStyle);

        Label.LabelStyle titleStyle = new Label. LabelStyle();
        titleStyle.font = skin.getFont("default-font");
        titleStyle.fontColor = Color. CYAN;
        skin. add("title", titleStyle);
    }

    private static void buildButtonStyles(Skin skin) {
        TextButton.TextButtonStyle defaultStyle = new TextButton.TextButtonStyle();
        defaultStyle. font = skin.getFont("default-font");
        defaultStyle.fontColor = Color.WHITE;
        defaultStyle.overFontColor = new Color(0.5f, 0.8f, 1f, 1f);
        defaultStyle.downFontColor = Color. CYAN;
        defaultStyle.disabledFontColor = Color.GRAY;
        defaultStyle.up = new TextureRegionDrawable(skin.getRegion("button"));
        defaultStyle. down = new TextureRegionDrawable(skin.getRegion("button-down"));
        defaultStyle. over = new TextureRegionDrawable(skin.getRegion("button-over"));
        skin.add("default", defaultStyle);

        TextButton. TextButtonStyle toggleStyle = new TextButton.TextButtonStyle();
        toggleStyle. font = skin.getFont("default-font");
        toggleStyle.fontColor = Color.WHITE;
        toggleStyle.overFontColor = new Color(0.5f, 0.8f, 1f, 1f);
        toggleStyle. downFontColor = Color.CYAN;
        toggleStyle. checkedFontColor = Color. CYAN;
        toggleStyle.disabledFontColor = Color. GRAY;
        toggleStyle.up = new TextureRegionDrawable(skin. getRegion("button"));
        toggleStyle. down = new TextureRegionDrawable(skin.getRegion("button-down"));
        toggleStyle. over = new TextureRegionDrawable(skin.getRegion("button-over"));
        toggleStyle. checked = new TextureRegionDrawable(skin.getRegion("button-checked"));
        skin.add("toggle", toggleStyle);
    }

    private static void buildSliderStyles(Skin skin) {
        Slider.SliderStyle horizontalStyle = new Slider.SliderStyle();
        horizontalStyle.background = new TextureRegionDrawable(skin.getRegion("slider-background"));
        horizontalStyle.knob = new TextureRegionDrawable(skin.getRegion("slider-knob"));
        horizontalStyle.knobOver = new TextureRegionDrawable(skin. getRegion("slider-knob-over"));
        horizontalStyle.knobDown = new TextureRegionDrawable(skin.getRegion("slider-knob-down"));
        skin.add("default-horizontal", horizontalStyle);
    }

    private static void buildWindowStyles(Skin skin) {
        Window.WindowStyle windowStyle = new Window. WindowStyle();
        windowStyle.titleFont = skin.getFont("default-font");
        windowStyle.titleFontColor = Color.CYAN;
        windowStyle.background = new TextureRegionDrawable(skin.getRegion("window"));
        skin.add("default", windowStyle);
    }

    private static void buildScrollPaneStyles(Skin skin) {
        ScrollPane. ScrollPaneStyle scrollStyle = new ScrollPane. ScrollPaneStyle();
        scrollStyle. vScroll = new TextureRegionDrawable(skin.getRegion("scroll-vertical"));
        scrollStyle.vScrollKnob = new TextureRegionDrawable(skin.getRegion("scroll-knob-vertical"));
        scrollStyle.hScroll = new TextureRegionDrawable(skin.getRegion("scroll-horizontal"));
        scrollStyle.hScrollKnob = new TextureRegionDrawable(skin.getRegion("scroll-knob-horizontal"));
        skin.add("default", scrollStyle);
    }

    private static void buildListStyles(Skin skin) {
        List.ListStyle listStyle = new List. ListStyle();
        listStyle.font = skin.getFont("default-font");
        listStyle. fontColorSelected = Color.CYAN;
        listStyle.fontColorUnselected = Color. WHITE;
        listStyle.selection = new TextureRegionDrawable(skin.getRegion("selection"));
        listStyle.background = new TextureRegionDrawable(skin.getRegion("list-background"));
        skin.add("default", listStyle);
    }
}
