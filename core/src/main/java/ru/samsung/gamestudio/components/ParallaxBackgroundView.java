package ru.samsung.gamestudio.components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.samsung.gamestudio.GameResources;
import ru.samsung.gamestudio.GameSettings;

public class ParallaxBackgroundView extends BackgroundView {
    
    private Texture[] textures;
    private float[] layerPositions;
    private float[] layerSpeeds;
    private int baseSpeed;
    
    public ParallaxBackgroundView(String[] texturePaths, float[] speeds) {
        super();
        this.textures = new Texture[texturePaths.length];
        this.layerPositions = new float[texturePaths.length];
        this.layerSpeeds = speeds.clone();
        this.baseSpeed = 2; // Инициализируем базовую скорость
        
        // Загружаем текстуры
        for (int i = 0; i < texturePaths.length; i++) {
            this.textures[i] = new Texture(texturePaths[i]);
            this.layerPositions[i] = 0;
        }
    }
    
    public ParallaxBackgroundView() {
        this(GameResources.BACKGROUND_LAYERS, GameResources.LAYER_SPEEDS);
    }
    
    public void move(float delta) {
        // Обновляем позиции всех слоев с разными скоростями
        for (int i = 0; i < layerPositions.length; i++) {
            layerPositions[i] -= baseSpeed * layerSpeeds[i];
            
            // Если слой полностью вышел за экран, возвращаем его в начало
            // Используем модуло для плавного перехода
            if (layerPositions[i] <= -GameSettings.SCREEN_WIDTH) {
                layerPositions[i] += GameSettings.SCREEN_WIDTH * 2;
            }
        }
    }
    
    @Override
    public void draw(SpriteBatch batch) {
        // Рисуем все слои от дальнего к ближнему
        for (int i = 0; i < textures.length; i++) {
            // Рисуем основной экземпляр слоя
            batch.draw(textures[i], layerPositions[i], 0, 
                     GameSettings.SCREEN_WIDTH, GameSettings.SCREEN_HEIGHT);
            
            // Рисуем дополнительный экземпляр для бесшовности
            float secondPosition = layerPositions[i] + GameSettings.SCREEN_WIDTH;
            if (secondPosition > 0) { // Рисуем только если видим
                batch.draw(textures[i], secondPosition, 0, 
                         GameSettings.SCREEN_WIDTH, GameSettings.SCREEN_HEIGHT);
            }
            
            // Рисуем третий экземпляр для полной бесшовности
            float thirdPosition = layerPositions[i] - GameSettings.SCREEN_WIDTH;
            if (thirdPosition < GameSettings.SCREEN_WIDTH) { // Рисуем только если видим
                batch.draw(textures[i], thirdPosition, 0, 
                         GameSettings.SCREEN_WIDTH, GameSettings.SCREEN_HEIGHT);
            }
        }
    }
    
    // Методы setSpeed и getSpeed уже определены в BackgroundView
    
    @Override
    public void dispose() {
        for (Texture texture : textures) {
            texture.dispose();
        }
    }
}
