package ru.samsung.gamestudio.components;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Абстрактный класс для фоновых компонентов
 * Предоставляет базовую функциональность для фонов
 */
public abstract class BackgroundView extends View {
    protected int baseSpeed;
    
    public BackgroundView() {
        super(0, 0);
        this.baseSpeed = 2; // Базовая скорость движения фона
    }
    
    /**
     * Обновляет позицию фона
     * @param delta время с последнего кадра
     */
    public abstract void move(float delta);
    
    /**
     * Устанавливает скорость движения фона
     */
    public void setSpeed(int speed) {
        this.baseSpeed = speed;
    }
    
    /**
     * Получает скорость движения фона
     */
    public int getSpeed() {
        return baseSpeed;
    }
}
