package ru.samsung.gamestudio;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import ru.samsung.gamestudio.managers.SoundManager;
import ru.samsung.gamestudio.screens.MainMenuScreen;
import ru.samsung.gamestudio.screens.GameScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class MyGdxGame extends Game {
    public OrthographicCamera camera;
    public Vector3 touch;
    public SoundManager soundManager;
    public GameScreen gameScreen;

    @Override
    public void create() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, GameSettings.SCREEN_WIDTH, GameSettings.SCREEN_HEIGHT);
        touch = new Vector3();
        
        // Инициализируем менеджер звуков
        soundManager = new SoundManager();
        
        // Запускаем с главного меню
        setScreen(new MainMenuScreen(this));
    }
    
    @Override
    public void dispose() {
        super.dispose();
        if (soundManager != null) {
            soundManager.dispose();
        }
    }
}
