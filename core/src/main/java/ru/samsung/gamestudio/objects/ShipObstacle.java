package ru.samsung.gamestudio.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.samsung.gamestudio.GameSettings;

public class ShipObstacle {
    private Texture texture;
    private float x;
    private float y;
    private float width;
    private float height;
    private float speed;
    private float baseSpeed; // Базовая скорость
    private float speedMultiplier; // Множитель скорости
    private boolean active;
    
    public ShipObstacle(String texturePath) {
        this.texture = new Texture(texturePath);
        // Настройки фильтрации для сглаживания
        this.texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        this.width = 60;  // Размер корабля
        this.height = 40;
        this.baseSpeed = 250f; // Базовая скорость движения корабля
        this.speed = baseSpeed;
        this.speedMultiplier = 1.0f; // Начальный множитель скорости
        this.active = false;
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
            // Обновляем текущую скорость на основе множителя
            speed = baseSpeed * speedMultiplier;
            x -= speed * delta;
            
            // Если корабль вышел за левый край экрана, деактивируем его
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
    
    // Сеттеры
    public void setActive(boolean active) { this.active = active; }
    public void setSpeedMultiplier(float multiplier) { 
        this.speedMultiplier = multiplier;
        this.speed = baseSpeed * speedMultiplier;
    }
    
    // Геттеры для ускорения
    public float getSpeedMultiplier() { return speedMultiplier; }
}
