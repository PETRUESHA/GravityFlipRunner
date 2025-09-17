#!/usr/bin/env python3
import struct
import os

def create_png_corridor_background():
    """Создает PNG файл с бесшовным фоном коридора"""
    width = 1280
    height = 720
    
    # Цвета в RGB
    wall_color = (100, 100, 100)      # Серые стены
    floor_color = (80, 80, 80)        # Тёмный пол
    ceiling_color = (120, 120, 120)   # Светлый потолок
    accent_color = (60, 60, 60)       # Акценты
    highlight_color = (140, 140, 140) # Подсветка
    
    # Создаем папку если её нет
    os.makedirs('assets/textures', exist_ok=True)
    
    # Создаем простой BMP файл (более простой формат)
    with open('assets/textures/seamless_corridor.bmp', 'wb') as f:
        # BMP заголовок
        f.write(b'BM')  # Сигнатура
        f.write(struct.pack('<I', 54 + width * height * 3))  # Размер файла
        f.write(struct.pack('<I', 0))  # Зарезервировано
        f.write(struct.pack('<I', 54))  # Смещение до данных
        f.write(struct.pack('<I', 40))  # Размер заголовка
        f.write(struct.pack('<I', width))  # Ширина
        f.write(struct.pack('<I', height))  # Высота
        f.write(struct.pack('<H', 1))  # Плоскости
        f.write(struct.pack('<H', 24))  # Бит на пиксель
        f.write(struct.pack('<I', 0))  # Сжатие
        f.write(struct.pack('<I', 0))  # Размер изображения
        f.write(struct.pack('<I', 0))  # X пикселей на метр
        f.write(struct.pack('<I', 0))  # Y пикселей на метр
        f.write(struct.pack('<I', 0))  # Цвета в палитре
        f.write(struct.pack('<I', 0))  # Важные цвета
        
        # Данные изображения (BGR формат для BMP)
        for y in range(height - 1, -1, -1):  # BMP хранит снизу вверх
            for x in range(width):
                # Определяем область (потолок, стены, пол)
                if y < height // 3:  # Потолок
                    if (x // 8 + y // 8) % 2 == 0:
                        r, g, b = ceiling_color
                    else:
                        r, g, b = highlight_color
                elif y > 2 * height // 3:  # Пол
                    if (x // 8 + y // 8) % 2 == 0:
                        r, g, b = floor_color
                    else:
                        r, g, b = accent_color
                else:  # Стены
                    brick_width = 16
                    brick_height = 8
                    offset = (y // brick_height) * (brick_width // 2)
                    
                    if ((x + offset) % brick_width < brick_width // 2) ^ (y % brick_height < brick_height // 2):
                        r, g, b = wall_color
                    else:
                        r, g, b = accent_color
                
                # Добавляем градиент для глубины
                center_x = width // 2
                distance_from_center = abs(x - center_x)
                shadow = int((distance_from_center / center_x) * 30)
                r = max(0, r - shadow)
                g = max(0, g - shadow)
                b = max(0, b - shadow)
                
                # BMP использует BGR порядок
                f.write(struct.pack('BBB', b, g, r))
    
    print('Бесшовный фон коридора создан: assets/textures/seamless_corridor.bmp')
    print(f'Размер: {width}x{height} пикселей')
    print('BMP файл можно использовать в LibGDX')

if __name__ == '__main__':
    create_png_corridor_background()

