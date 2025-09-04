@echo off
chcp 65001
setlocal

:: 设置工程路径（如果脚本在工程根目录下，可以省略或设置为当前目录）
set "PROJECT_PATH=%~dp0"

:: 设置远程仓库的名称
set "REMOTE_NAME=origin"

:: 切换到工程目录
cd /d "%PROJECT_PATH%"

:: 检查是否在 Git 仓库中
git rev-parse --is-inside-work-tree >nul 2>&1
if %errorlevel% neq 0 (
    echo 当前目录不是一个 Git 仓库，请确保脚本在正确的工程目录下运行。
    exit /b 1
)

:: 配置 Git 凭据助手（自动保存凭据）
git config --global credential.helper store

:: 添加所有更改到暂存区
git add .

:: 提交更改
git commit -m "bak"

:: 推送到远程仓库
git push %REMOTE_NAME% master

echo 提交完成。
pause