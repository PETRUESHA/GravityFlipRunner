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
import ru.samsung.gamestudio.managers.MemoryManager;

import java.util.ArrayList;

public class SettingsScreen implements Screen {
    private MyGdxGame myGdxGame;
    private SpriteBatch batch;
    private ParallaxBackgroundView background;
    private BitmapFont titleFont;
    private BitmapFont buttonFont;
    private BitmapFont smallFont;
    private BitmapFont recordsFont; // Шрифт для таблицы рекордов
    
    // UI элементы
    private TextView titleTextView;
    private ButtonView backButtonView;
    
    // Настройки звука
    private ButtonView musicToggleButton;
    private ButtonView soundToggleButton;
    private TextView musicStatusTextView;
    private TextView soundStatusTextView;
    
    // Таблица рекордов
    private ButtonView recordsButton;
    private TextView recordsTitleTextView;
    private TextView[] recordTextViews;
    private boolean showRecords = false;
    
    public SettingsScreen(MyGdxGame game) {
        this.myGdxGame = game;
        this.batch = new SpriteBatch();
        
        // Инициализируем фон
        this.background = new ParallaxBackgroundView();
        
        // Инициализируем шрифты
        this.titleFont = new BitmapFont();
        this.titleFont.setColor(Color.WHITE);
        this.titleFont.getData().setScale(3.0f);
        
        this.buttonFont = new BitmapFont();
        this.buttonFont.setColor(Color.WHITE);
        this.buttonFont.getData().setScale(1.8f);
        
        this.smallFont = new BitmapFont();
        this.smallFont.setColor(Color.WHITE);
        this.smallFont.getData().setScale(1.2f);
        
        // Создаем отдельный шрифт для таблицы рекордов (больше и ярче)
        this.recordsFont = new BitmapFont();
        this.recordsFont.setColor(Color.YELLOW); // Желтый цвет для лучшей видимости
        this.recordsFont.getData().setScale(2.0f); // Увеличиваем размер в 1.67 раза
        
        // Создаем заголовок
        this.titleTextView = new TextView(titleFont, 0, GameSettings.SCREEN_HEIGHT - 80, "Settings");
        float titleX = (GameSettings.SCREEN_WIDTH - titleTextView.width) / 2;
        titleTextView.x = titleX;
        
        // Создаем кнопки настроек
        this.musicToggleButton = new ButtonView("Music: ON", 
            GameSettings.SCREEN_WIDTH / 2 - 150, 
            GameSettings.SCREEN_HEIGHT - 200, 
            300, 50, buttonFont);
            
        this.soundToggleButton = new ButtonView("Sound: ON", 
            GameSettings.SCREEN_WIDTH / 2 - 150, 
            GameSettings.SCREEN_HEIGHT - 270, 
            300, 50, buttonFont);
            
        this.recordsButton = new ButtonView("Records", 
            GameSettings.SCREEN_WIDTH / 2 - 150, 
            GameSettings.SCREEN_HEIGHT - 340, 
            300, 50, buttonFont);
        
        // Создаем кнопку "Назад"
        this.backButtonView = new ButtonView("Back", 
            GameSettings.SCREEN_WIDTH / 2 - 100, 
            50, 
            200, 50, buttonFont);
            
        // Инициализируем статусы настроек
        updateSettingsButtons();
        
        // Инициализируем таблицу рекордов
        initRecordsTable();
    }
    
    private void updateSettingsButtons() {
        boolean musicOn = myGdxGame.soundManager.isMusicEnabled();
        boolean soundOn = myGdxGame.soundManager.isSoundEnabled();
        
        musicToggleButton.setText("Music: " + (musicOn ? "ON" : "OFF"));
        soundToggleButton.setText("Sound: " + (soundOn ? "ON" : "OFF"));
    }
    
    private void initRecordsTable() {
        // Заголовок таблицы рекордов (больше и ярче)
        recordsTitleTextView = new TextView(recordsFont, 0, GameSettings.SCREEN_HEIGHT - 420, "Top 5 Records:");
        float titleX = (GameSettings.SCREEN_WIDTH - recordsTitleTextView.width) / 2;
        recordsTitleTextView.x = titleX;
        
        recordTextViews = new TextView[5];
        ArrayList<Integer> records = MemoryManager.getTop5Records();
        
        for (int i = 0; i < 5; i++) {
            int score = records.get(i);
            String recordText;
            if (score > 0) {
                recordText = (i + 1) + ". " + score;
            } else {
                recordText = (i + 1) + ". ---"; // Показываем прочерки для пустых мест
            }
            // Увеличиваем расстояние между строками с 25 до 35 пикселей
            recordTextViews[i] = new TextView(recordsFont, 0, GameSettings.SCREEN_HEIGHT - 460 - (i * 35), recordText);
            float recordX = (GameSettings.SCREEN_WIDTH - recordTextViews[i].width) / 2;
            recordTextViews[i].x = recordX;
        }
    }
    
    @Override
    public void show() {
        myGdxGame.soundManager.playMenuMusic();
    }
    
    @Override
    public void render(float delta) {
        background.move(delta);
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        
        myGdxGame.camera.update();
        batch.setProjectionMatrix(myGdxGame.camera.combined);
        batch.begin();
        
        // Рисуем фон
        background.draw(batch);
        
        // Рисуем заголовок
        titleTextView.draw(batch);
        
        // Рисуем кнопки настроек
        musicToggleButton.draw(batch);
        soundToggleButton.draw(batch);
        recordsButton.draw(batch);
        
        // Рисуем таблицу рекордов если показана
        if (showRecords) {
            recordsTitleTextView.draw(batch);
            for (TextView recordView : recordTextViews) {
                recordView.draw(batch);
            }
        }
        
        // Рисуем кнопку "Назад"
        backButtonView.draw(batch);
        
        batch.end();
        
        handleInput();
    }
    
    private void handleInput() {
        if (Gdx.input.justTouched()) {
            myGdxGame.touch = myGdxGame.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            
            if (backButtonView.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                myGdxGame.setScreen(new MainMenuScreen(myGdxGame));
            }
            else if (musicToggleButton.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                boolean currentState = myGdxGame.soundManager.isMusicEnabled();
                myGdxGame.soundManager.setMusicEnabled(!currentState);
                updateSettingsButtons();
            }
            else if (soundToggleButton.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                boolean currentState = myGdxGame.soundManager.isSoundEnabled();
                myGdxGame.soundManager.setSoundEnabled(!currentState);
                updateSettingsButtons();
            }
            else if (recordsButton.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                showRecords = !showRecords;
                if (showRecords) {
                    initRecordsTable(); // Обновляем таблицу
                }
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
        smallFont.dispose();
        recordsFont.dispose();
        titleTextView.dispose();
        backButtonView.dispose();
        musicToggleButton.dispose();
        soundToggleButton.dispose();
        recordsButton.dispose();
        if (recordsTitleTextView != null) recordsTitleTextView.dispose();
        if (recordTextViews != null) {
            for (TextView recordView : recordTextViews) {
                if (recordView != null) recordView.dispose();
            }
        }
    }
}