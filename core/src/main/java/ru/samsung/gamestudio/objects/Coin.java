package ru.samsung.gamestudio.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.samsung.gamestudio.GameSettings;

public class Coin {
    private Texture texture;
    private float x;
    private float y;
    private float width;
    private float height;
    private float speed;
    private boolean active;
    private int value; // Стоимость монеты в очках
    
    public Coin(String texturePath) {
        this.texture = new Texture(texturePath);
        // Настройки фильтрации для сглаживания
        this.texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        this.width = 30;  // Размер монеты
        this.height = 30;
        this.speed = 250f; // Скорость движения монеты (как у препятствий)
        this.active = false;
        this.value = 1; // 1 монета = 1 очко
        this.x = GameSettings.SCREEN_WIDTH; // Начинаем за экраном
        this.y = 0;
    }
    
    public void spawn(float yPosition) {
        this.x = GameSettings.SCREEN_WIDTH;
        this.y = yPosition;
        this.active = true;
    }
    
    public void update(float delta) {
        if (active) {
            x -= speed * delta;
            
            // Если монета вышла за левый край экрана, деактивируем её
            if (x + width < 0) {
                active = false;
            }
        }
    }
    
    public void draw(SpriteBatch batch) {
        if (active) {
            // Рисуем с округлением для плавности
            batch.draw(texture, 
                      Math.round(x), Math.round(y), 
                      width, height);
        }
    }
    
    public boolean isCollidingWith(Player player) {
        if (!active) return false;
        
        // Простая проверка коллизии AABB (Axis-Aligned Bounding Box)
        return (x < player.getX() + player.getWidth() &&
                x + width > player.getX() &&
                y < player.getY() + player.getHeight() &&
                y + height > player.getY());
    }
    
    public void dispose() {
        texture.dispose();
    }
    
    // Геттеры
    public boolean isActive() { return active; }
    public float getX() { return x; }
    public float getY() { return y; }
    public float getWidth() { return width; }
    public float getHeight() { return height; }
    public int getValue() { return value; }
    
    // Сеттеры
    public void setActive(boolean active) { this.active = active; }
    public void setValue(int value) { this.value = value; }
}

