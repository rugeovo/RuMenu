# 🎮 RuMenu - 现代化 Minecraft 交互菜单系统

**基于 TabooLib 框架开发的高性能、可定制化 GUI 菜单插件**

![Minecraft Version](https://img.shields.io/badge/Minecraft-1.17.x%20to%201.21.x-blue)
![License](https://img.shields.io/badge/License-MIT-green)

## 📖 简介

RuMenu 是一款基于 Taboolib 框架开发的 Minecraft 插件，旨在为 Minecraft 服务器提供一个功能强大且灵活的交互式菜单系统。通过 RuMenu，服务器管理员可以轻松创建自定义的用户交互菜单，支持丰富的功能与配置选项，让玩家的游戏体验更加丰富与互动。

## 📚 文档资源
- **技术文档**: [语雀文档中心](https://www.yuque.com/rugeoo/ox16qz)
- 快速入门指南
- 配置示例库

## ✨ 核心特性

- ✅ **可视化配置** - 通过 YAML 文件轻松设计复杂菜单
- ✅ **动态交互** - 支持 Kether 脚本实现条件逻辑与动态内容
- ✅ **跨版本支持** - 兼容 1.17.x ~1.21.x Minecraft版本
- ✅ **核心支持** - Paper/Purpur/Spigot 服务端兼容
- ✅ **高性能** - 基于 TabooLib 的优化架构

## 🛠️ 快速开始

### 构建一个菜单如此简单

```yaml
# test.yml
title: "主菜单"
#菜单布局
layout:
  - "#########"
  - "#       #"
  - "#       #"
  - "#       #"
  - "#########"
icons:
  "#":
    display:
      name: " "
      material: "black_stained_glass_pane"
```
