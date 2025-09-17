package ru.samsung.gamestudio.components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.samsung.gamestudio.GameResources;
import ru.samsung.gamestudio.GameSettings;

public class FloorCeilingView extends BackgroundView {
    private Texture floorTexture;
    private Texture ceilingTexture;
    private float floor1X, floor2X;
    private float ceiling1X, ceiling2X;
    private int speed;
    
    public FloorCeilingView() {
        super();
        this.floorTexture = new Texture(GameResources.FLOOR_TEXTURE);
        this.ceilingTexture = new Texture(GameResources.CEILING_TEXTURE);
        this.speed = 2; // Инициализируем скорость
        
        // Инициализируем позиции для бесшовного движения
        this.floor1X = 0;
        this.floor2X = GameSettings.SCREEN_WIDTH;
        this.ceiling1X = 0;
        this.ceiling2X = GameSettings.SCREEN_WIDTH;
    }
    
    public void move(float delta) {
        // Двигаем пол
        floor1X -= speed;
        floor2X -= speed;
        
        // Двигаем потолок
        ceiling1X -= speed;
        ceiling2X -= speed;
        
        // Сбрасываем позиции для бесшовности
        if (floor1X <= -GameSettings.SCREEN_WIDTH) {
            floor1X = GameSettings.SCREEN_WIDTH;
        }
        if (floor2X <= -GameSettings.SCREEN_WIDTH) {
            floor2X = GameSettings.SCREEN_WIDTH;
        }
        
        if (ceiling1X <= -GameSettings.SCREEN_WIDTH) {
            ceiling1X = GameSettings.SCREEN_WIDTH;
        }
        if (ceiling2X <= -GameSettings.SCREEN_WIDTH) {
            ceiling2X = GameSettings.SCREEN_WIDTH;
        }
    }
    
    @Override
    public void draw(SpriteBatch batch) {
        // Рисуем пол (внизу экрана)
        batch.draw(floorTexture, floor1X, 0, GameSettings.SCREEN_WIDTH, GameResources.FLOOR_HEIGHT);
        batch.draw(floorTexture, floor2X, 0, GameSettings.SCREEN_WIDTH, GameResources.FLOOR_HEIGHT);
        
        // Рисуем потолок (вверху экрана)
        float ceilingY = GameSettings.SCREEN_HEIGHT - GameResources.CEILING_HEIGHT;
        batch.draw(ceilingTexture, ceiling1X, ceilingY, GameSettings.SCREEN_WIDTH, GameResources.CEILING_HEIGHT);
        batch.draw(ceilingTexture, ceiling2X, ceilingY, GameSettings.SCREEN_WIDTH, GameResources.CEILING_HEIGHT);
    }
    
    // Методы setSpeed и getSpeed уже определены в BackgroundView
    
    @Override
    public void dispose() {
        floorTexture.dispose();
        ceilingTexture.dispose();
    }
}

