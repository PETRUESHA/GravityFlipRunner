package ru.samsung.gamestudio.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import ru.samsung.gamestudio.GameSettings;
import ru.samsung.gamestudio.MyGdxGame;
import ru.samsung.gamestudio.GameResources;
import ru.samsung.gamestudio.components.ParallaxBackgroundView;
import ru.samsung.gamestudio.components.FloorCeilingView;
import ru.samsung.gamestudio.components.ScoreView;
import ru.samsung.gamestudio.objects.Player;
import ru.samsung.gamestudio.managers.ObstacleManager;
import ru.samsung.gamestudio.managers.CoinManager;
import ru.samsung.gamestudio.GameState;
import ru.samsung.gamestudio.components.ButtonView;
import ru.samsung.gamestudio.components.TextView;
import ru.samsung.gamestudio.managers.MemoryManager;

public class GameScreen implements Screen {
    private MyGdxGame myGdxGame;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private ParallaxBackgroundView background;
    private FloorCeilingView floorCeiling;
    private Player player;
    private ObstacleManager obstacleManager;
    private CoinManager coinManager;
    private ScoreView scoreView;
    private GameState gameState;
    private float touchTimer;
    private boolean isTouching;
    private boolean pauseButtonPressed;
    
    // UI элементы для паузы
    private TextView pauseTextView;
    private ButtonView continueButton;
    private ButtonView homeButton;
    private com.badlogic.gdx.graphics.Texture fullBlackoutTexture;
    
    // Кнопка паузы в левом верхнем углу
    private ButtonView pauseButton;

    public GameScreen(MyGdxGame game) {
        this.myGdxGame = game;
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, GameSettings.SCREEN_WIDTH, GameSettings.SCREEN_HEIGHT);
        this.batch = new SpriteBatch();
        
        // Настройки для плавного рендеринга
        this.batch.setBlendFunction(com.badlogic.gdx.graphics.GL20.GL_SRC_ALPHA, com.badlogic.gdx.graphics.GL20.GL_ONE_MINUS_SRC_ALPHA);

        // Инициализируем многослойный фон, пол/потолок, персонажа и менеджеры
        this.background = new ParallaxBackgroundView();
        this.floorCeiling = new FloorCeilingView();
        this.player = new Player(GameResources.PLAYER_FRAMES, GameResources.PLAYER_FRAMES_REVERSED);
        this.obstacleManager = new ObstacleManager();
        this.coinManager = new CoinManager(myGdxGame.soundManager);
        this.scoreView = new ScoreView();
        this.gameState = GameState.PLAYING;
        this.touchTimer = 0f;
        this.isTouching = false;
        this.pauseButtonPressed = false;
        
        // Инициализируем UI элементы для паузы
        initPauseUI();
    }

    private void initPauseUI() {
        // Создаем текстуру для затемнения
        com.badlogic.gdx.graphics.Pixmap pixmap = new com.badlogic.gdx.graphics.Pixmap(1, 1, com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888);
        pixmap.setColor(0.0f, 0.0f, 0.0f, 0.7f);
        pixmap.fill();
        fullBlackoutTexture = new com.badlogic.gdx.graphics.Texture(pixmap);
        pixmap.dispose();
        
        // Создаем шрифты
        com.badlogic.gdx.graphics.g2d.BitmapFont pauseFont = new com.badlogic.gdx.graphics.g2d.BitmapFont();
        pauseFont.setColor(com.badlogic.gdx.graphics.Color.WHITE);
        pauseFont.getData().setScale(3.0f);
        
        com.badlogic.gdx.graphics.g2d.BitmapFont buttonFont = new com.badlogic.gdx.graphics.g2d.BitmapFont();
        buttonFont.setColor(com.badlogic.gdx.graphics.Color.WHITE);
        buttonFont.getData().setScale(2.0f);
        
        // Создаем текст паузы (перемещаем выше - с +50 до +120)
        pauseTextView = new TextView(pauseFont, 0, GameSettings.SCREEN_HEIGHT / 2 + 120, "PAUSED");
        float pauseX = (GameSettings.SCREEN_WIDTH - pauseTextView.width) / 2;
        pauseTextView.x = pauseX;
        
        // Создаем кнопки
        continueButton = new ButtonView("Continue", 
            GameSettings.SCREEN_WIDTH / 2 - 150, 
            GameSettings.SCREEN_HEIGHT / 2 - 20, 
            300, 60, buttonFont);
            
        homeButton = new ButtonView("Home", 
            GameSettings.SCREEN_WIDTH / 2 - 150, 
            GameSettings.SCREEN_HEIGHT / 2 - 100, 
            300, 60, buttonFont);
            
        // Создаем кнопку паузы в левом верхнем углу
        pauseButton = new ButtonView(20, GameSettings.SCREEN_HEIGHT - 80, 60, 60, GameResources.PAUSE_ICON_PATH);
    }

    @Override
    public void show() {
        // Запускаем музыку игры
        myGdxGame.soundManager.playGameMusic();
    }

    @Override
    public void render(float delta) {
        // Обработка ввода
        handleInput(delta);
        
        // Обновление игры только если играем
        if (gameState == GameState.PLAYING) {
            // ВАЖНО: Обновление игры происходит ТОЛЬКО в состоянии PLAYING
            // Обновление игровых объектов
            background.move(delta);
            floorCeiling.move(delta);
            player.update(delta);
            obstacleManager.update(delta, player);
            coinManager.update(delta, player);
            
            // Обновляем отображение очков
            scoreView.setScore(coinManager.getTotalCoinsCollected());
            
            // Отладочная информация об ускорении (каждые 5 секунд)
            if ((int)obstacleManager.getGameTime() % 5 == 0 && obstacleManager.getGameTime() > 0) {
                System.out.println("Game Time: " + String.format("%.1f", obstacleManager.getGameTime()) + 
                    "s, Speed Multiplier: " + String.format("%.2f", obstacleManager.getCurrentSpeedMultiplier()) + 
                    "x, Spawn Multiplier: " + String.format("%.2f", obstacleManager.getCurrentSpawnMultiplier()) + "x");
            }
            
            // Проверка коллизий с препятствиями
            if (obstacleManager.checkCollisions(player)) {
                gameState = GameState.GAME_OVER;
                // Воспроизводим звук удара
                myGdxGame.soundManager.playHitSound();
                // Переходим на экран Game Over
                myGdxGame.setScreen(new GameOverScreen(myGdxGame, coinManager.getTotalCoinsCollected()));
                return;
            }
        }
        
        // Очистка экрана
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        
        // Обновляем камеру для плавности
        camera.update();
        
        // Рендеринг
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        
        // Рисуем игровые объекты
        background.draw(batch);
        floorCeiling.draw(batch);
        player.draw(batch);
        obstacleManager.draw(batch);
        coinManager.draw(batch);
        scoreView.draw(batch);
        
        // Рисуем кнопку паузы (всегда видна во время игры)
        if (gameState == GameState.PLAYING) {
            pauseButton.draw(batch);
        }
        
        // Рисуем UI паузы если игра на паузе
        if (gameState == GameState.PAUSED) {
            // Затемняющий фон
            batch.draw(fullBlackoutTexture, 0, 0, GameSettings.SCREEN_WIDTH, GameSettings.SCREEN_HEIGHT);
            
            // UI элементы паузы
            pauseTextView.draw(batch);
            continueButton.draw(batch);
            homeButton.draw(batch);
        }
        
        batch.end();
    }

    private void handleInput(float delta) {
        if (gameState == GameState.PAUSED) {
            // Обработка ввода в состоянии паузы (ТОЛЬКО кнопки паузы)
            if (Gdx.input.justTouched()) {
                myGdxGame.touch = myGdxGame.camera.unproject(new com.badlogic.gdx.math.Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
                
                if (continueButton.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                    // Возобновляем игру
                    System.out.println("Continue button pressed - resuming game");
                    gameState = GameState.PLAYING;
                    myGdxGame.soundManager.resumeAllMusic();
                    // ВАЖНО: Сбрасываем все флаги при возобновлении игры
                    pauseButtonPressed = false;
                    isTouching = false;
                    touchTimer = 0f;
                    System.out.println("Game state changed to: " + gameState);
                }
                else if (homeButton.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                    // Сохраняем текущий результат в таблицу рекордов
                    MemoryManager.addNewScore(coinManager.getTotalCoinsCollected());
                    
                    // Возвращаемся в главное меню
                    myGdxGame.soundManager.playMenuMusic();
                    myGdxGame.setScreen(new MainMenuScreen(myGdxGame));
                }
                // ВАЖНО: В состоянии паузы НЕТ обработки смены гравитации
            }
            // ВАЖНО: Сбрасываем все флаги касаний в состоянии паузы
            isTouching = false;
            touchTimer = 0f;
            pauseButtonPressed = false;
        } else if (gameState == GameState.PLAYING) {
            // ВАЖНО: Убеждаемся что все флаги сброшены в состоянии PLAYING
            if (pauseButtonPressed) {
                System.out.println("Warning: pauseButtonPressed was true in PLAYING state - resetting");
                pauseButtonPressed = false;
            }
            
            // Обработка касаний экрана
            if (Gdx.input.justTouched()) {
                myGdxGame.touch = myGdxGame.camera.unproject(new com.badlogic.gdx.math.Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
                
                // Проверяем нажатие на кнопку паузы
                if (pauseButton.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                    System.out.println("Pause button pressed - setting pause state");
                    pauseButtonPressed = true; // Устанавливаем флаг
                    gameState = GameState.PAUSED;
                    myGdxGame.soundManager.pauseAllMusic();
                    return; // ВАЖНО: return предотвращает дальнейшую обработку касания
                }
            }
            
            // Обработка длительного нажатия для смены гравитации
            if (Gdx.input.isTouched()) {
                if (!isTouching) {
                    isTouching = true;
                    touchTimer = 0f;
                    pauseButtonPressed = false; // Сбрасываем флаг при начале касания
                } else {
                    touchTimer += delta;
                    // Если держим палец более 1 секунды - ставим на паузу
                    if (touchTimer >= 1.0f) {
                        gameState = GameState.PAUSED;
                        myGdxGame.soundManager.pauseAllMusic();
                        return;
                    }
                }
            } else {
                if (isTouching && touchTimer < 1.0f && !pauseButtonPressed) {
                    // Короткое касание - смена гравитации (только если не нажали на кнопку паузы)
                    System.out.println("Short touch detected - flipping gravity (pauseButtonPressed=" + pauseButtonPressed + ")");
                    player.flipGravity();
                } else if (isTouching && pauseButtonPressed) {
                    System.out.println("Touch detected but pause button was pressed - NO gravity flip (pauseButtonPressed=" + pauseButtonPressed + ")");
                } else if (isTouching) {
                    System.out.println("Touch ended but conditions not met for gravity flip (isTouching=" + isTouching + ", touchTimer=" + touchTimer + ", pauseButtonPressed=" + pauseButtonPressed + ")");
                }
                isTouching = false;
                touchTimer = 0f;
                pauseButtonPressed = false; // Сбрасываем флаг
            }
            
            // Обработка кнопки "Назад" на Android
            if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.BACK)) {
                gameState = GameState.PAUSED;
                myGdxGame.soundManager.pauseAllMusic();
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, GameSettings.SCREEN_WIDTH, GameSettings.SCREEN_HEIGHT);
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
        floorCeiling.dispose();
        player.dispose();
        obstacleManager.dispose();
        coinManager.dispose();
        scoreView.dispose();
        
        // Очищаем ресурсы паузы
        if (fullBlackoutTexture != null) fullBlackoutTexture.dispose();
        if (pauseTextView != null) pauseTextView.dispose();
        if (continueButton != null) continueButton.dispose();
        if (homeButton != null) homeButton.dispose();
        if (pauseButton != null) pauseButton.dispose();
    }
}
