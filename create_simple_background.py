#!/usr/bin/env python3
import os

def create_simple_corridor_background():
    """Создает простой бесшовный фон коридора в формате PPM"""
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
    
    # Создаем PPM файл (простой формат изображения)
    with open('assets/textures/seamless_corridor.ppm', 'w') as f:
        f.write('P3\n')
        f.write(f'{width} {height}\n')
        f.write('255\n')
        
        for y in range(height):
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
                
                f.write(f'{r} {g} {b} ')
            f.write('\n')
    
    print('Бесшовный фон коридора создан: assets/textures/seamless_corridor.ppm')
    print(f'Размер: {width}x{height} пикселей')
    print('Примечание: PPM файл нужно конвертировать в PNG для использования в LibGDX')

if __name__ == '__main__':
    create_simple_corridor_background()

