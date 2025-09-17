package ru.samsung.gamestudio;

public class GameResources {
    public static final String LIVE_IMG_PATH = "libgdx.png";
    public static final String PLAYER_IMG_PATH = "textures/frame-1.png";

    // Многослойный фон
    public static final String[] BACKGROUND_LAYERS = {
        "textures/background/1.png",  // Самый дальний слой
        "textures/background/2.png",  // Средний слой
        "textures/background/3.png",  // Ближний слой
        "textures/background/4.png",  // Основной слой
        "textures/background/5.png"   // Передний слой
    };
    
    // Скорости движения для каждого слоя (параллакс эффект)
    public static final float[] LAYER_SPEEDS = {
        0.2f,  // Самый медленный (дальний)
        0.4f,  // Медленный
        0.6f,  // Средний
        1.0f,  // Нормальный (основной)
        1.5f   // Быстрый (передний)
    };

    // Анимация персонажа (нормальная)
    public static final String[] PLAYER_FRAMES = {
        "textures/player/frame-1.png",
        "textures/player/frame-2.png",
        "textures/player/frame-3.png",
        "textures/player/frame-4.png",
        "textures/player/frame-5.png",
        "textures/player/frame-6.png"
    };
    
    // Анимация персонажа (перевернутая)
    public static final String[] PLAYER_FRAMES_REVERSED = {
        "textures/player/frame-1_rev.png",
        "textures/player/frame-2_rev.png",
        "textures/player/frame-3_rev.png",
        "textures/player/frame-4_rev.png",
        "textures/player/frame-5_rev.png",
        "textures/player/frame-6_rev.png"
    };
    
    // Текстуры кораблей-препятствий
    public static final String[] SHIP_TEXTURES = {
        "textures/obstacles/sh1.png",
        "textures/obstacles/sh2.png",
        "textures/obstacles/sh3.png",
        "textures/obstacles/sh4.png",
        "textures/obstacles/sh5.png",
        "textures/obstacles/sh6.png"
    };
    
    // Текстуры облаков-препятствий (резерв)
    public static final String[] CLOUD_TEXTURES = {
        "textures/obstacles/cl1.png",
        "textures/obstacles/cl2.png"
    };
    
    // Текстуры монет
    public static final String[] COIN_TEXTURES = {
        "textures/obstacles/coin_big.png",
        "textures/obstacles/coin_medium.png"
    };
    
    // Текстуры пола и потолка
    public static final String FLOOR_TEXTURE = "textures/background/floor.png";
    public static final String CEILING_TEXTURE = "textures/background/ceil.png";
    
    // GUI текстуры
    public static final String PAUSE_ICON_PATH = "textures/gui/pause_icon.png";
    
    // Звуки
    public static final String MENU_MUSIC_PATH = "sounds/menu_music.mp3";
    public static final String GAME_MUSIC_PATH = "sounds/game_music.mp3";
    public static final String COIN_SOUND_PATH = "sounds/coin_sound.wav";
    public static final String HIT_SOUND_PATH = "sounds/hit_sound.mp3";
    
    // Константы для границ игры
    public static final float FLOOR_HEIGHT = 120f;  // Высота текстуры пола
    public static final float CEILING_HEIGHT = 120f; // Высота текстуры потолка
    public static final float PLAY_AREA_TOP = GameSettings.SCREEN_HEIGHT - CEILING_HEIGHT; // Верхняя граница игровой зоны
    public static final float PLAY_AREA_BOTTOM = FLOOR_HEIGHT; // Нижняя граница игровой зоны
}
