package ru.samsung.gamestudio.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;
import ru.samsung.gamestudio.GameSettings;
import ru.samsung.gamestudio.MyGdxGame;
import ru.samsung.gamestudio.components.TextView;
import ru.samsung.gamestudio.managers.MemoryManager;

public class GameOverScreen implements Screen {
    private MyGdxGame myGdxGame;
    private SpriteBatch batch;
    private int finalScore;
    private BitmapFont titleFont;
    private BitmapFont scoreFont;
    
    // UI элементы
    private TextView gameOverTextView;
    private TextView scoreTextView;
    private TextView tapToContinueTextView;
    
    public GameOverScreen(MyGdxGame game, int score) {
        this.myGdxGame = game;
        this.finalScore = score;
        this.batch = new SpriteBatch();
        
        // Сохраняем результат в таблицу рекордов
        MemoryManager.addNewScore(score);
        
        // Инициализируем шрифты
        this.titleFont = new BitmapFont();
        this.titleFont.setColor(Color.RED);
        this.titleFont.getData().setScale(4.0f);
        
        this.scoreFont = new BitmapFont();
        this.scoreFont.setColor(Color.WHITE);
        this.scoreFont.getData().setScale(2.0f);
        
        // Создаем текст "Game Over"
        this.gameOverTextView = new TextView(titleFont, 0, GameSettings.SCREEN_HEIGHT / 2 + 100, "GAME OVER");
        // Центрируем заголовок
        float gameOverX = (GameSettings.SCREEN_WIDTH - gameOverTextView.width) / 2;
        gameOverTextView.x = gameOverX;
        
        // Создаем текст со счетом
        this.scoreTextView = new TextView(scoreFont, 0, GameSettings.SCREEN_HEIGHT / 2, "Score: " + finalScore);
        // Центрируем счет
        float scoreX = (GameSettings.SCREEN_WIDTH - scoreTextView.width) / 2;
        scoreTextView.x = scoreX;
        
        // Создаем текст "Tap to continue"
        this.tapToContinueTextView = new TextView(scoreFont, 0, GameSettings.SCREEN_HEIGHT / 2 - 100, "Tap to continue");
        // Центрируем текст
        float tapX = (GameSettings.SCREEN_WIDTH - tapToContinueTextView.width) / 2;
        tapToContinueTextView.x = tapX;
    }
    
    @Override
    public void show() {
        
    }
    
    @Override
    public void render(float delta) {
        // Обработка ввода
        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.SPACE) || Gdx.input.justTouched()) {
            // Возвращаемся в главное меню
            myGdxGame.setScreen(new MainMenuScreen(myGdxGame));
        }
        
        // Очистка экрана
        ScreenUtils.clear(0.2f, 0.1f, 0.1f, 1f);
        
        // Рендеринг
        batch.setProjectionMatrix(myGdxGame.camera.combined);
        batch.begin();
        
        // Рисуем тексты
        gameOverTextView.draw(batch);
        scoreTextView.draw(batch);
        tapToContinueTextView.draw(batch);
        
        batch.end();
    }
    
    @Override
    public void resize(int width, int height) {
        myGdxGame.camera.setToOrtho(false, GameSettings.SCREEN_WIDTH, GameSettings.SCREEN_HEIGHT);
    }
    
    @Override
    public void pause() {
        
    }
    
    @Override
    public void resume() {
        
    }
    
    @Override
    public void hide() {
        
    }
    
    @Override
    public void dispose() {
        batch.dispose();
        titleFont.dispose();
        scoreFont.dispose();
        gameOverTextView.dispose();
        scoreTextView.dispose();
        tapToContinueTextView.dispose();
    }
}
