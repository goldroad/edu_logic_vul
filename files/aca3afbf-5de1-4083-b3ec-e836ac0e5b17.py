import pyautogui
import time

def move_mouse():
    # 定义移动的像素距离
    move_distance = 10
    # 定义移动方向
    directions = ['up', 'down', 'left', 'right']
    # 无限循环
    while True:
        for direction in directions:
            # 根据方向移动鼠标
            if direction == 'up':
                pyautogui.move(0, -move_distance)
            elif direction == 'down':
                pyautogui.move(0, move_distance)
            elif direction == 'left':
                pyautogui.move(-move_distance, 0)
            elif direction == 'right':
                pyautogui.move(move_distance, 0)
            # 每次移动后等待5秒
            time.sleep(5)

if __name__ == "__main__":
    print("程序已启动")
    print("按 Ctrl+C 停止程序。")
    move_mouse()