package ru.samsung.gamestudio.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import ru.samsung.gamestudio.GameSettings;
import ru.samsung.gamestudio.MyGdxGame;
import ru.samsung.gamestudio.components.ParallaxBackgroundView;
import ru.samsung.gamestudio.components.ButtonView;
import ru.samsung.gamestudio.components.TextView;
import ru.samsung.gamestudio.screens.GameScreen;
import ru.samsung.gamestudio.screens.SettingsScreen;

public class MainMenuScreen implements Screen {
    private MyGdxGame myGdxGame;
    private SpriteBatch batch;
    private ParallaxBackgroundView background;
    private BitmapFont titleFont;
    private BitmapFont buttonFont;
    
    // UI элементы
    private TextView titleTextView;
    private ButtonView startButtonView;
    private ButtonView settingsButtonView;
    private ButtonView exitButtonView;
    
    public MainMenuScreen(MyGdxGame game) {
        this.myGdxGame = game;
        this.batch = new SpriteBatch();
        
        // Инициализируем фон
        this.background = new ParallaxBackgroundView();
        
        // Инициализируем шрифты
        this.titleFont = new BitmapFont();
        this.titleFont.setColor(Color.WHITE);
        this.titleFont.getData().setScale(4.0f); // Увеличиваем размер с 2.5f до 4.0f
        
        this.buttonFont = new BitmapFont();
        this.buttonFont.setColor(Color.WHITE);
        this.buttonFont.getData().setScale(2.0f);
        
        // Создаем заголовок (перемещаем ниже - с -100 до -150)
        this.titleTextView = new TextView(titleFont, 0, GameSettings.SCREEN_HEIGHT - 150, "GravityFlipRunner");
        // Центрируем заголовок
        float titleX = (GameSettings.SCREEN_WIDTH - titleTextView.width) / 2;
        if (titleX < 50) titleX = 50; // Минимальный отступ слева
        if (titleX + titleTextView.width > GameSettings.SCREEN_WIDTH - 50) {
            titleX = GameSettings.SCREEN_WIDTH - titleTextView.width - 50; // Минимальный отступ справа
        }
        titleTextView.x = titleX;
        
        // Создаем кнопки
        this.startButtonView = new ButtonView("Start Game", 
            GameSettings.SCREEN_WIDTH / 2 - 150, 
            GameSettings.SCREEN_HEIGHT / 2 - 50, 
            300, 60, buttonFont);
            
        this.settingsButtonView = new ButtonView("Settings", 
            GameSettings.SCREEN_WIDTH / 2 - 150, 
            GameSettings.SCREEN_HEIGHT / 2 - 120, 
            300, 60, buttonFont);
            
        this.exitButtonView = new ButtonView("Exit", 
            GameSettings.SCREEN_WIDTH / 2 - 150, 
            GameSettings.SCREEN_HEIGHT / 2 - 190, 
            300, 60, buttonFont);
    }
    
    @Override
    public void show() {
        // Запускаем музыку главного меню
        myGdxGame.soundManager.playMenuMusic();
    }
    
    @Override
    public void render(float delta) {
        // Обновляем фон
        background.move(delta);
        
        // Очистка экрана
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        
        // Рендеринг
        batch.setProjectionMatrix(myGdxGame.camera.combined);
        batch.begin();
        
        // Рисуем фон
        background.draw(batch);
        
        // Рисуем заголовок
        titleTextView.draw(batch);
        
        // Рисуем кнопки
        startButtonView.draw(batch);
        settingsButtonView.draw(batch);
        exitButtonView.draw(batch);
        
        batch.end();
        
        // Обработка ввода
        handleInput();
    }
    
    private void handleInput() {
        if (Gdx.input.justTouched()) {
            myGdxGame.touch = myGdxGame.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            
            if (startButtonView.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                // Создаем новый GameScreen и сохраняем ссылку
                myGdxGame.gameScreen = new GameScreen(myGdxGame);
                myGdxGame.setScreen(myGdxGame.gameScreen);
            }
            if (exitButtonView.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                Gdx.app.exit();
            }
            if (settingsButtonView.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                myGdxGame.setScreen(new SettingsScreen(myGdxGame));
            }
        }
    }
    
    @Override
    public void resize(int width, int height) {
        myGdxGame.camera.setToOrtho(false, GameSettings.SCREEN_WIDTH, GameSettings.SCREEN_HEIGHT);
    }
    
    @Override
    public void pause() {
        myGdxGame.soundManager.pauseAllMusic();
    }
    
    @Override
    public void resume() {
        myGdxGame.soundManager.resumeAllMusic();
    }
    
    @Override
    public void hide() {
        
    }
    
    @Override
    public void dispose() {
        batch.dispose();
        background.dispose();
        titleFont.dispose();
        buttonFont.dispose();
        titleTextView.dispose();
        startButtonView.dispose();
        settingsButtonView.dispose();
        exitButtonView.dispose();
    }
}
