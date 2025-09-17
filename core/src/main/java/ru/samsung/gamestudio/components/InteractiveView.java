package ru.samsung.gamestudio.components;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Абстрактный класс для интерактивных компонентов
 * Предоставляет базовую функциональность для взаимодействия
 */
public abstract class InteractiveView extends View {
    protected boolean isEnabled;
    protected boolean isVisible;
    
    public InteractiveView(float x, float y, float width, float height) {
        super(x, y, width, height);
        this.isEnabled = true;
        this.isVisible = true;
    }
    
    public InteractiveView(float x, float y) {
        super(x, y);
        this.isEnabled = true;
        this.isVisible = true;
    }
    
    /**
     * Проверяет, находится ли точка внутри компонента
     */
    public boolean isHit(float tx, float ty) {
        if (!isEnabled || !isVisible) return false;
        return (tx >= x && tx <= x + width && ty >= y && ty <= y + height);
    }
    
    /**
     * Обрабатывает касание
     */
    public abstract void onTouch(float tx, float ty);
    
    /**
     * Включает/выключает компонент
     */
    public void setEnabled(boolean enabled) {
        this.isEnabled = enabled;
    }
    
    /**
     * Показывает/скрывает компонент
     */
    public void setVisible(boolean visible) {
        this.isVisible = visible;
    }
    
    /**
     * Проверяет, включен ли компонент
     */
    public boolean isEnabled() {
        return isEnabled;
    }
    
    /**
     * Проверяет, видим ли компонент
     */
    public boolean isVisible() {
        return isVisible;
    }
    
    @Override
    public void draw(SpriteBatch batch) {
        if (isVisible) {
            drawContent(batch);
        }
    }
    
    /**
     * Рисует содержимое компонента
     * Должен быть переопределен в наследниках
     */
    protected abstract void drawContent(SpriteBatch batch);
}
