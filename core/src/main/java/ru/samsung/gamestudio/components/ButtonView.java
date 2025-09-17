package ru.samsung.gamestudio.components;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ButtonView extends InteractiveView {

    Texture texture;
    BitmapFont bitmapFont;

    String text;

    float textX;
    float textY;

    public ButtonView(float x, float y, float width, float height, BitmapFont font, String texturePath, String text) {
        super(x, y, width, height);

        this.text = text;
        this.bitmapFont = font;

        texture = new Texture(texturePath);

        GlyphLayout glyphLayout = new GlyphLayout(bitmapFont, text);
        float textWidth = glyphLayout.width;
        float textHeight = glyphLayout.height;

        textX = x + (width - textWidth) / 2;
        textY = y + (height + textHeight) / 2;
    }

    public ButtonView(float x, float y, float width, float height, String texturePath) {
        super(x, y, width, height);

        texture  = new Texture(texturePath);
    }
    
    // Конструктор для текстовых кнопок без текстуры
    public ButtonView(String text, float x, float y, float width, float height, BitmapFont font) {
        super(x, y, width, height);
        this.text = text;
        this.bitmapFont = font;
        
        // Создаем простую текстуру для кнопки (можно заменить на готовую текстуру)
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(0.2f, 0.2f, 0.2f, 0.8f); // Темно-серый цвет с прозрачностью
        pixmap.fill();
        texture = new Texture(pixmap);
        pixmap.dispose();
        
        GlyphLayout glyphLayout = new GlyphLayout(font, text);
        float textWidth = glyphLayout.width;
        float textHeight = glyphLayout.height;
        
        textX = x + (width - textWidth) / 2;
        textY = y + (height + textHeight) / 2;
    }

    @Override
    protected void drawContent(SpriteBatch batch) {
        batch.draw(texture, x, y, width, height);
        if (bitmapFont != null) bitmapFont.draw(batch, text, textX, textY);
    }
    
    public void setText(String newText) {
        this.text = newText;
        if (bitmapFont != null) {
            GlyphLayout glyphLayout = new GlyphLayout(bitmapFont, text);
            float textWidth = glyphLayout.width;
            float textHeight = glyphLayout.height;
            
            textX = x + (width - textWidth) / 2;
            textY = y + (height + textHeight) / 2;
        }
    }
    
    @Override
    public void onTouch(float tx, float ty) {
        // Базовая реализация - ничего не делает
        // Может быть переопределена в наследниках для специфической логики
    }

    @Override
    public void dispose() {
        texture.dispose();
        if (bitmapFont != null) bitmapFont.dispose();
    }

}
