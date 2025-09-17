package ru.samsung.gamestudio.managers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.samsung.gamestudio.GameResources;
import ru.samsung.gamestudio.GameSettings;
import ru.samsung.gamestudio.objects.ShipObstacle;
import ru.samsung.gamestudio.objects.Player;

import java.util.ArrayList;
import java.util.List;

public class ObstacleManager {
    private List<ShipObstacle> obstacles;
    private String[] shipTextures;
    private float spawnTimer;
    private float spawnInterval;
    private float minSpawnInterval;
    private float maxSpawnInterval;
    private int score;
    private float gameTime; // Время игры для расчета ускорения
    private float currentSpeedMultiplier; // Текущий множитель скорости
    private float currentSpawnMultiplier; // Текущий множитель частоты спавна
    
    public ObstacleManager() {
        this.obstacles = new ArrayList<>();
        this.shipTextures = GameResources.SHIP_TEXTURES;
        this.spawnTimer = 0;
        this.minSpawnInterval = 1.2f; // Минимальный интервал между спавном (корабли быстрее)
        this.maxSpawnInterval = 2.5f; // Максимальный интервал между спавном
        this.spawnInterval = maxSpawnInterval;
        this.score = 0;
        this.gameTime = 0f;
        this.currentSpeedMultiplier = 1.0f;
        this.currentSpawnMultiplier = 1.0f;
        
        // Создаем пул препятствий
        for (int i = 0; i < 12; i++) { // 12 кораблей в пуле
            obstacles.add(new ShipObstacle(shipTextures[i % shipTextures.length]));
        }
    }
    
    public void update(float delta, Player player) {
        // Обновляем время игры
        gameTime += delta;
        
        // Рассчитываем ускорение на основе времени игры
        calculateAcceleration();
        
        // Обновляем таймер спавна
        spawnTimer += delta;
        
        // Спавним новое препятствие
        if (spawnTimer >= spawnInterval) {
            spawnObstacle(player);
            spawnTimer = 0;
            // Уменьшаем интервал спавна со временем и ускорением
            float baseInterval = Math.max(minSpawnInterval, 
                maxSpawnInterval - (score * 0.01f));
            spawnInterval = baseInterval / currentSpawnMultiplier;
        }
        
        // Обновляем все активные препятствия
        for (ShipObstacle obstacle : obstacles) {
            obstacle.update(delta);
        }
        
        // Увеличиваем счет
        score += (int)(delta * 10);
    }
    
    private void spawnObstacle(Player player) {
        // Находим неактивное препятствие
        ShipObstacle obstacle = null;
        for (ShipObstacle obs : obstacles) {
            if (!obs.isActive()) {
                obstacle = obs;
                break;
            }
        }
        
        if (obstacle != null) {
            // Спавним препятствие в случайной позиции по Y в игровой зоне
            float playAreaHeight = GameResources.PLAY_AREA_TOP - GameResources.PLAY_AREA_BOTTOM;
            float yPosition = GameResources.PLAY_AREA_BOTTOM + 
                (float)(Math.random() * (playAreaHeight - obstacle.getHeight()));
            obstacle.spawn(yPosition);
            // Устанавливаем текущий множитель скорости для нового препятствия
            obstacle.setSpeedMultiplier(currentSpeedMultiplier);
        }
    }
    
    private void calculateAcceleration() {
        // Проверяем, началось ли время ускорения
        if (gameTime >= GameSettings.ACCELERATION_START_TIME) {
            // Рассчитываем прогресс ускорения (от 0 до 1)
            float accelerationProgress = (gameTime - GameSettings.ACCELERATION_START_TIME) / GameSettings.ACCELERATION_DURATION;
            accelerationProgress = Math.min(1.0f, accelerationProgress); // Ограничиваем максимумом 1.0
            
            // Применяем плавное ускорение (используем квадратичную функцию для более плавного перехода)
            float smoothProgress = accelerationProgress * accelerationProgress;
            
            // Рассчитываем множители скорости и спавна
            currentSpeedMultiplier = 1.0f + (GameSettings.MAX_SPEED_MULTIPLIER - 1.0f) * smoothProgress;
            currentSpawnMultiplier = 1.0f + (GameSettings.MAX_SPAWN_MULTIPLIER - 1.0f) * smoothProgress;
            
            // Обновляем множители скорости для всех активных препятствий
            for (ShipObstacle obstacle : obstacles) {
                if (obstacle.isActive()) {
                    obstacle.setSpeedMultiplier(currentSpeedMultiplier);
                }
            }
        } else {
            // До начала ускорения используем базовые значения
            currentSpeedMultiplier = 1.0f;
            currentSpawnMultiplier = 1.0f;
        }
    }
    
    public void draw(SpriteBatch batch) {
        for (ShipObstacle obstacle : obstacles) {
            obstacle.draw(batch);
        }
    }
    
    public boolean checkCollisions(Player player) {
        for (ShipObstacle obstacle : obstacles) {
            if (obstacle.isActive() && obstacle.isCollidingWith(player)) {
                return true; // Коллизия обнаружена
            }
        }
        return false;
    }
    
    public void dispose() {
        for (ShipObstacle obstacle : obstacles) {
            obstacle.dispose();
        }
    }
    
    // Геттеры для ускорения
    public float getGameTime() { return gameTime; }
    public float getCurrentSpeedMultiplier() { return currentSpeedMultiplier; }
    public float getCurrentSpawnMultiplier() { return currentSpawnMultiplier; }
    
    // Сброс для новой игры
    public void reset() {
        for (ShipObstacle obstacle : obstacles) {
            obstacle.setActive(false);
        }
        score = 0;
        spawnTimer = 0;
        spawnInterval = maxSpawnInterval;
        gameTime = 0f;
        currentSpeedMultiplier = 1.0f;
        currentSpawnMultiplier = 1.0f;
    }
}
