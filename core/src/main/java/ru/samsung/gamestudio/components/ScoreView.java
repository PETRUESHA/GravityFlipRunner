package ru.samsung.gamestudio.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.samsung.gamestudio.GameSettings;

public class ScoreView extends InteractiveView {
    private BitmapFont font;
    private int score;
    private TextView scoreTextView;
    
    public ScoreView() {
        super(0, 0); // Позиция будет установлена в draw()
        
        // Создаем шрифт для отображения очков
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(2.0f);
        
        // Создаем TextView для отображения счета
        scoreTextView = new TextView(font, 0, 0, "Score: 0");
        updatePosition();
    }
    
    public void setScore(int score) {
        this.score = score;
        scoreTextView.setText("Score: " + score);
        updatePosition();
    }
    
    private void updatePosition() {
        // Позиционируем в верхнем правом углу
        scoreTextView.x = GameSettings.SCREEN_WIDTH - scoreTextView.width - 20;
        scoreTextView.y = GameSettings.SCREEN_HEIGHT - 50;
    }
    
    @Override
    protected void drawContent(SpriteBatch batch) {
        scoreTextView.draw(batch);
    }
    
    @Override
    public void onTouch(float tx, float ty) {
        // ScoreView не реагирует на касания
    }
    
    @Override
    public void dispose() {
        font.dispose();
        scoreTextView.dispose();
    }
}
