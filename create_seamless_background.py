#!/usr/bin/env python3
from PIL import Image, ImageDraw
import os

def create_seamless_corridor_background(width=1280, height=720):
    """Создает бесшовный пиксельный фон коридора"""
    img = Image.new('RGB', (width, height))
    draw = ImageDraw.Draw(img)
    
    # Цвета для пиксельного стиля
    wall_color = (100, 100, 100)      # Серые стены
    floor_color = (80, 80, 80)        # Тёмный пол
    ceiling_color = (120, 120, 120)   # Светлый потолок
    accent_color = (60, 60, 60)       # Акценты (тени/кирпичи)
    highlight_color = (140, 140, 140) # Подсветка
    
    # Рисуем пол (нижняя треть)
    floor_height = height // 3
    for y in range(height - floor_height, height):
        for x in range(width):
            # Пиксельный паттерн плитки
            if (x // 8 + y // 8) % 2 == 0:
                img.putpixel((x, y), floor_color)
            else:
                img.putpixel((x, y), accent_color)
    
    # Рисуем потолок (верхняя треть)
    ceiling_height = height // 3
    for y in range(0, ceiling_height):
        for x in range(width):
            # Пиксельный паттерн потолка
            if (x // 8 + y // 8) % 2 == 0:
                img.putpixel((x, y), ceiling_color)
            else:
                img.putpixel((x, y), highlight_color)
    
    # Рисуем стены (середина) - бесшовный кирпичный паттерн
    for y in range(ceiling_height, height - floor_height):
        for x in range(width):
            brick_width = 16
            brick_height = 8
            offset = (y // brick_height) * (brick_width // 2)
            
            if ((x + offset) % brick_width < brick_width // 2) ^ (y % brick_height < brick_height // 2):
                img.putpixel((x, y), wall_color)
            else:
                img.putpixel((x, y), accent_color)
    
    # Добавляем градиент для глубины (тени)
    for y in range(height):
        for x in range(width):
            r, g, b = img.getpixel((x, y))
            # Лёгкий градиент от центра к краям
            center_x = width // 2
            distance_from_center = abs(x - center_x)
            shadow = int((distance_from_center / center_x) * 30)
            img.putpixel((x, y), (max(0, r - shadow), max(0, g - shadow), max(0, b - shadow)))
    
    # Добавляем линии перспективы на полу
    for y in range(height - floor_height, height):
        for x in range(width):
            # Линии перспективы (сходящиеся к центру)
            center_x = width // 2
            if abs(x - center_x) < 2 and (y - (height - floor_height)) % 4 == 0:
                img.putpixel((x, y), highlight_color)
    
    # Сохраняем в assets/textures
    os.makedirs('assets/textures', exist_ok=True)
    img.save('assets/textures/seamless_corridor.png')
    print('Бесшовный пиксельный фон коридора создан: assets/textures/seamless_corridor.png')
    print(f'Размер: {width}x{height} пикселей')

if __name__ == '__main__':
    create_seamless_corridor_background()

