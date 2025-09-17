package ru.samsung.gamestudio;

public class GameSettings {
    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 720;
    
    // Настройки ускорения препятствий
    public static final float ACCELERATION_START_TIME = 10f; // Время начала ускорения (секунды)
    public static final float ACCELERATION_DURATION = 30f; // Продолжительность ускорения (секунды)
    public static final float MAX_SPEED_MULTIPLIER = 1.5f; // Максимальный множитель скорости
    public static final float MAX_SPAWN_MULTIPLIER = 1.3f; // Максимальный множитель частоты спавна
}
