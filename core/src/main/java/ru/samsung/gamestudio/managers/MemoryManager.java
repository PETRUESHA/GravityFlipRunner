package ru.samsung.gamestudio.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Json;

import java.util.ArrayList;
import java.util.Collections;

public class MemoryManager {

    private static final Preferences preferences = Gdx.app.getPreferences("GravityFlipRunner_Saves");

    // Настройки звука
    public static void saveSoundSettings(boolean isOn) {
        preferences.putBoolean("isSoundOn", isOn);
        preferences.flush();
    }

    public static boolean loadIsSoundOn() {
        return preferences.getBoolean("isSoundOn", true);
    }

    // Настройки музыки
    public static void saveMusicSettings(boolean isOn) {
        preferences.putBoolean("isMusicOn", isOn);
        preferences.flush();
    }

    public static boolean loadIsMusicOn() {
        return preferences.getBoolean("isMusicOn", true);
    }

    // Таблица рекордов
    public static void saveTableOfRecords(ArrayList<Integer> table) {
        Json json = new Json();
        String tableInString = json.toJson(table);
        preferences.putString("recordTable", tableInString);
        preferences.flush();
    }

    public static ArrayList<Integer> loadRecordsTable() {
        if (!preferences.contains("recordTable")) {
            return new ArrayList<>();
        }

        String scores = preferences.getString("recordTable");
        Json json = new Json();
        ArrayList<Integer> table = json.fromJson(ArrayList.class, scores);
        return table != null ? table : new ArrayList<>();
    }

    // Добавление нового результата в таблицу рекордов
    public static void addNewScore(int score) {
        ArrayList<Integer> records = loadRecordsTable();
        
        // Добавляем новый результат
        records.add(score);
        
        // Сортируем по убыванию (лучшие результаты вверху)
        Collections.sort(records, Collections.reverseOrder());
        
        // Оставляем только топ-5 результатов
        if (records.size() > 5) {
            records = new ArrayList<>(records.subList(0, 5));
        }
        
        // Сохраняем обновленную таблицу
        saveTableOfRecords(records);
    }

    // Получение топ-5 результатов
    public static ArrayList<Integer> getTop5Records() {
        ArrayList<Integer> records = loadRecordsTable();
        
        // Если записей меньше 5, дополняем нулями
        while (records.size() < 5) {
            records.add(0);
        }
        
        // Возвращаем только топ-5
        if (records.size() > 5) {
            return new ArrayList<>(records.subList(0, 5));
        }
        
        return records;
    }

    // Проверка, является ли результат рекордом
    public static boolean isNewRecord(int score) {
        ArrayList<Integer> records = loadRecordsTable();
        
        // Если записей меньше 5, то это всегда рекорд
        if (records.size() < 5) {
            return true;
        }
        
        // Проверяем, больше ли новый результат худшего из топ-5
        int worstRecord = records.get(records.size() - 1);
        return score > worstRecord;
    }

    // Очистка всех данных
    public static void clearAllData() {
        preferences.clear();
        preferences.flush();
    }
}
