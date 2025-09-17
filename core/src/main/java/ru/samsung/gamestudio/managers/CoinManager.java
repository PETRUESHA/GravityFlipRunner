package ru.samsung.gamestudio.managers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.samsung.gamestudio.GameResources;
import ru.samsung.gamestudio.GameSettings;
import ru.samsung.gamestudio.objects.Coin;
import ru.samsung.gamestudio.objects.Player;

import java.util.ArrayList;
import java.util.List;

public class CoinManager {
    private List<Coin> coins;
    private String[] coinTextures;
    private float spawnTimer;
    private float spawnInterval;
    private float minSpawnInterval;
    private float maxSpawnInterval;
    private int totalCoinsCollected;
    private SoundManager soundManager;
    
    public CoinManager(SoundManager soundManager) {
        this.coins = new ArrayList<>();
        this.coinTextures = GameResources.COIN_TEXTURES;
        this.spawnTimer = 0;
        this.minSpawnInterval = 2.0f; // Минимальный интервал между спавном монет
        this.maxSpawnInterval = 4.0f; // Максимальный интервал между спавном монет
        this.spawnInterval = maxSpawnInterval;
        this.totalCoinsCollected = 0;
        this.soundManager = soundManager;
        
        // Создаем пул монет
        for (int i = 0; i < 8; i++) { // 8 монет в пуле
            coins.add(new Coin(coinTextures[i % coinTextures.length]));
        }
    }
    
    public void update(float delta, Player player) {
        // Обновляем таймер спавна
        spawnTimer += delta;
        
        // Спавним монету если пришло время
        if (spawnTimer >= spawnInterval) {
            spawnCoin(player);
            spawnTimer = 0;
            // Случайный интервал для следующей монеты
            spawnInterval = minSpawnInterval + (float)(Math.random() * (maxSpawnInterval - minSpawnInterval));
        }
        
        // Обновляем все активные монеты
        for (Coin coin : coins) {
            coin.update(delta);
        }
        
        // Проверяем коллизии с игроком
        checkCollisions(player);
    }
    
    private void spawnCoin(Player player) {
        Coin coin = null;
        // Ищем неактивную монету в пуле
        for (Coin c : coins) {
            if (!c.isActive()) {
                coin = c;
                break;
            }
        }
        
        if (coin != null) {
            // Спавним монету в случайной позиции в игровой зоне
            float playAreaHeight = GameResources.PLAY_AREA_TOP - GameResources.PLAY_AREA_BOTTOM;
            float yPosition = GameResources.PLAY_AREA_BOTTOM + 
                (float)(Math.random() * (playAreaHeight - coin.getHeight()));
            coin.spawn(yPosition);
        }
    }
    
    private void checkCollisions(Player player) {
        for (Coin coin : coins) {
            if (coin.isActive() && coin.isCollidingWith(player)) {
                // Игрок собрал монету
                coin.setActive(false);
                totalCoinsCollected += coin.getValue();
                // Воспроизводим звук подбора монеты
                if (soundManager != null) {
                    soundManager.playCoinSound();
                }
                System.out.println("Coin collected! Total: " + totalCoinsCollected);
            }
        }
    }
    
    public void draw(SpriteBatch batch) {
        for (Coin coin : coins) {
            coin.draw(batch);
        }
    }
    
    public void dispose() {
        for (Coin coin : coins) {
            coin.dispose();
        }
    }
    
    // Геттеры
    public int getTotalCoinsCollected() { 
        return totalCoinsCollected; 
    }
    
    public int getActiveCoinsCount() {
        int count = 0;
        for (Coin coin : coins) {
            if (coin.isActive()) count++;
        }
        return count;
    }
    
    public void reset() {
        for (Coin coin : coins) {
            coin.setActive(false);
        }
        totalCoinsCollected = 0;
        spawnTimer = 0;
        spawnInterval = maxSpawnInterval;
    }
}

