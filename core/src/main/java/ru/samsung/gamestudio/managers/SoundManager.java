package ru.samsung.gamestudio.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Disposable;
import ru.samsung.gamestudio.GameResources;

public class SoundManager implements Disposable {
    // Музыка
    private Music menuMusic;
    private Music gameMusic;
    
    // Звуки
    private Sound coinSound;
    private Sound hitSound;
    
    // Настройки звука
    private boolean musicEnabled = true;
    private boolean soundEnabled = true;
    private float musicVolume = 0.7f;
    private float soundVolume = 0.8f;
    
    public SoundManager() {
        // Загружаем настройки из памяти
        musicEnabled = MemoryManager.loadIsMusicOn();
        soundEnabled = MemoryManager.loadIsSoundOn();
        
        // Загружаем музыку
        menuMusic = Gdx.audio.newMusic(Gdx.files.internal(GameResources.MENU_MUSIC_PATH));
        gameMusic = Gdx.audio.newMusic(Gdx.files.internal(GameResources.GAME_MUSIC_PATH));
        
        // Настраиваем музыку
        menuMusic.setLooping(true);
        menuMusic.setVolume(musicVolume);
        
        gameMusic.setLooping(true);
        gameMusic.setVolume(musicVolume);
        
        // Загружаем звуки
        coinSound = Gdx.audio.newSound(Gdx.files.internal(GameResources.COIN_SOUND_PATH));
        hitSound = Gdx.audio.newSound(Gdx.files.internal(GameResources.HIT_SOUND_PATH));
    }
    
    // Методы для музыки
    public void playMenuMusic() {
        if (musicEnabled && !menuMusic.isPlaying()) {
            stopAllMusic();
            menuMusic.play();
        }
    }
    
    public void playGameMusic() {
        if (musicEnabled && !gameMusic.isPlaying()) {
            stopAllMusic();
            gameMusic.play();
        }
    }
    
    public void stopAllMusic() {
        if (menuMusic.isPlaying()) {
            menuMusic.stop();
        }
        if (gameMusic.isPlaying()) {
            gameMusic.stop();
        }
    }
    
    public void pauseAllMusic() {
        if (menuMusic.isPlaying()) {
            menuMusic.pause();
        }
        if (gameMusic.isPlaying()) {
            gameMusic.pause();
        }
    }
    
    public void resumeAllMusic() {
        if (musicEnabled) {
            // Проверяем, была ли музыка поставлена на паузу (позиция > 0)
            if (gameMusic.getPosition() > 0) {
                gameMusic.play();
            }
        }
    }
    
    // Методы для звуков
    public void playCoinSound() {
        if (soundEnabled) {
            coinSound.play(soundVolume);
        }
    }
    
    public void playHitSound() {
        if (soundEnabled) {
            hitSound.play(soundVolume);
        }
    }
    
    // Настройки
    public void setMusicEnabled(boolean enabled) {
        this.musicEnabled = enabled;
        MemoryManager.saveMusicSettings(enabled);
        if (!enabled) {
            stopAllMusic();
        } else {
            // Если музыка включена, запускаем музыку меню
            // (playMenuMusic() сам остановит музыку игры если она играет)
            playMenuMusic();
        }
    }
    
    public void setSoundEnabled(boolean enabled) {
        this.soundEnabled = enabled;
        MemoryManager.saveSoundSettings(enabled);
    }
    
    // Геттеры
    public boolean isMusicEnabled() { return musicEnabled; }
    public boolean isSoundEnabled() { return soundEnabled; }
    
    @Override
    public void dispose() {
        if (menuMusic != null) menuMusic.dispose();
        if (gameMusic != null) gameMusic.dispose();
        if (coinSound != null) coinSound.dispose();
        if (hitSound != null) hitSound.dispose();
    }
}
