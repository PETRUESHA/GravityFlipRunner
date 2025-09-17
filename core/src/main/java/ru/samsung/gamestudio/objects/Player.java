package ru.samsung.gamestudio.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.samsung.gamestudio.GameResources;
import ru.samsung.gamestudio.GameSettings;

public class Player {
    private Texture[] normalTextures;
    private Texture[] reversedTextures;
    private int currentFrame;
    private float animationTime;
    private float frameDuration;
    private float x;
    private float y;
    private float width;
    private float height;
    private float velocityY;
    private boolean gravityInverted;
    private float gravity;
    private float jumpForce;
    
    public Player(String[] normalTexturePaths, String[] reversedTexturePaths) {
        // Загружаем нормальные текстуры
        this.normalTextures = new Texture[normalTexturePaths.length];
        for (int i = 0; i < normalTexturePaths.length; i++) {
            this.normalTextures[i] = new Texture(normalTexturePaths[i]);
            // Настройки фильтрации для сглаживания
            this.normalTextures[i].setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }
        
        // Загружаем перевернутые текстуры
        this.reversedTextures = new Texture[reversedTexturePaths.length];
        for (int i = 0; i < reversedTexturePaths.length; i++) {
            this.reversedTextures[i] = new Texture(reversedTexturePaths[i]);
            // Настройки фильтрации для сглаживания
            this.reversedTextures[i].setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }
        
        this.currentFrame = 0;
        this.animationTime = 0;
        this.frameDuration = 0.1f; // 10 кадров в секунду
        this.width = 64;
        this.height = 64;
        this.x = 100; // Фиксированная позиция по X
        this.y = GameResources.PLAY_AREA_BOTTOM; // Начинаем на полу
        this.velocityY = 0;
        this.gravityInverted = false;
        this.gravity = 500f; // Сила гравитации
        this.jumpForce = 300f; // Сила прыжка
    }
    
    public void update(float delta) {
        // Обновляем анимацию
        animationTime += delta;
        if (animationTime >= frameDuration) {
            animationTime = 0;
            currentFrame = (currentFrame + 1) % normalTextures.length;
        }
        
        // Применяем гравитацию (оригинальная логика)
        if (gravityInverted) {
            velocityY += gravity * delta; // Гравитация вверх (к потолку)
        } else {
            velocityY -= gravity * delta; // Гравитация вниз (к полу)
        }
        
        // Обновляем позицию
        y += velocityY * delta;
        
        // Ограничиваем позицию в зависимости от направления гравитации
        if (gravityInverted) {
            // Когда гравитация перевернута, персонаж "стоит" на потолке
            if (y > GameResources.PLAY_AREA_TOP - height) {
                y = GameResources.PLAY_AREA_TOP - height;
                velocityY = 0;
            }
        } else {
            // Обычная гравитация - персонаж стоит на полу
            if (y < GameResources.PLAY_AREA_BOTTOM) {
                y = GameResources.PLAY_AREA_BOTTOM;
                velocityY = 0;
            }
        }
        
    }
    
    public void flipGravity() {
        gravityInverted = !gravityInverted;
        velocityY = jumpForce * (gravityInverted ? 1 : -1);
    }
    
    public void draw(SpriteBatch batch) {
        // Выбираем правильный набор текстур в зависимости от гравитации
        Texture[] currentTextures = gravityInverted ? reversedTextures : normalTextures;
        Texture currentTexture = currentTextures[currentFrame];
        
        // Рисуем текстуру без поворота (используем готовые перевернутые текстуры)
        batch.draw(currentTexture, x, y, width, height);
    }
    
    public void dispose() {
        for (Texture texture : normalTextures) {
            texture.dispose();
        }
        for (Texture texture : reversedTextures) {
            texture.dispose();
        }
    }
    
    // Геттеры для коллизий
    public float getX() { return x; }
    public float getY() { return y; }
    public float getWidth() { return width; }
    public float getHeight() { return height; }
    public boolean isGravityInverted() { return gravityInverted; }
}
