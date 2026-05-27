# Tiku（题库）

<p align="center">
  <img src="docs/app-icon-preview.png" width="200" alt="Tiku 应用图标" />
</p>

> 一款面向学生的 Android 刷题应用，支持在线刷题、模拟考试、错题记录与学习统计。

---

## 📖 项目概述

Tiku 是一个由学生团队协作开发的题库管理应用。

我们的目标是打造一个**简洁、高效、易用**的刷题工具，帮助同学们更好地管理和练习各类题目。

### 项目定位

- **目标用户**：在校学生（初中、高中、大学及考研等）
- **核心场景**：日常刷题练习、错题回顾、模拟考试、学习进度追踪
- **设计理念**：简洁优先，体验至上，减少干扰，专注学习

---

## 🎨 设计语言

本项目严格遵循 **Material 3 Expressive (M3E)** 设计规范 —— Google 最新一代设计语言，首次亮相于 Android 16。

### M3E 核心特性

| 特性 | 说明 |
|------|------|
| **情感化设计** | 以情感驱动的 UX，让交互更具表现力和愉悦感 |
| **活力色彩** | 更深沉的色调调色板（Deeper Tonal Palettes）和更广泛的 Token 集合 |
| **直觉动效** | 基于 Token 的动效系统（Motion Physics），实现更易定制、更流畅的过渡动画 |
| **灵活排版** | 利用可变字体轴（Variable Font Axes）实现更富表现力的文字排版 |
| **对比形状** | 全新 35 种装饰性形状库，内置形状变形动效（Shape Morph Motion） |
| **自适应组件** | 组件能够根据屏幕尺寸和用户偏好自适应调整 |

### 设计原则

1. **一致性**：所有页面和组件严格遵循 M3E 规范，保持视觉和交互的一致性
2. **动态色彩**：支持 Material You 动态取色（Dynamic Color），适配用户壁纸主题
3. **无障碍**：确保足够的对比度、触控目标尺寸和屏幕阅读器支持
4. **动效克制**：动效用于提升体验，而非炫技，所有过渡动画需有意义且流畅

### 参考资源

- [Material Design 3 官方文档](https://material.io/)
- [M3 Expressive 设计语言](https://developer.android.com/develop/ui/compose/designsystems/material3)
- [Material 3 Expressive 博客](https://material.io/blog/material-3-expressive)

---

## 🛠 技术栈

### 核心技术

| 类别 | 技术 | 版本 |
|------|------|------|
| **平台** | Android | minSdk 26 (Android 8.0) / targetSdk 35 (Android 15) |
| **语言** | Kotlin | 2.0.21 |
| **UI 框架** | Jetpack Compose | BOM 2024.12.01 |
| **设计系统** | Material 3 Expressive (M3E) | — |
| **构建系统** | Gradle (Kotlin DSL) | AGP 8.7.3 |
| **JVM 目标** | Java 17 | — |

### 当前依赖

```toml
# Compose
compose-bom = "2024.12.01"
compose-ui / compose-ui-graphics / compose-ui-tooling-preview
compose-material3          # M3E 设计系统
compose-material-icons-extended  # 扩展图标库

# Navigation
navigation-compose = "2.8.5"

# Lifecycle
lifecycle-viewmodel-compose = "2.8.7"
lifecycle-runtime-compose

# Activity
activity-compose = "1.9.3"
core-ktx = "1.15.0"

# Database
room-runtime / room-ktx / room-compiler  # Room 本地数据库

# Serialization
kotlinx-serialization-json              # JSON 序列化

# Preferences
datastore-preferences                   # 用户设置持久化
```

### 计划引入的依赖

> 以下依赖将在对应功能开发时引入，当前尚未集成。

| 依赖 | 用途 | 引入阶段 |
|------|------|----------|
| `Retrofit + OkHttp` | 网络请求，在线题库同步 | 网络层搭建 |
| `Dagger Hilt` / `Koin` | 依赖注入 | 架构优化 |
| `Coil` / `Accompanist` | 图片加载 | 题目图片支持 |
| `ViewPager2` / `HorizontalPager` | 滑动刷题体验 | 刷题功能 |

---

## 📁 项目结构

```
Quiz/
├── app/
│   ├── build.gradle.kts                    # App 模块构建配置
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── java/com/at210co60/tiku/
│       │   ├── MainActivity.kt             # 主 Activity
│       │   ├── TikuApp.kt                  # Composable 入口 & 导航图
│       │   ├── TikuApplication.kt         # Application 类
│       │   ├── data/
│       │   │   ├── model/                 # 数据模型
│       │   │   │   ├── Question.kt        # 题目 & 答题记录模型
│       │   │   │   └── Models.kt         # 题库 & 统计模型
│       │   │   ├── local/
│       │   │   │   ├── entity/            # Room 实体类
│       │   │   │   ├── dao/              # Room DAO 接口
│       │   │   │   └── TikuDatabase.kt  # Room 数据库
│       │   │   └── repository/           # 数据仓库
│       │   │       ├── QuestionRepository.kt
│       │   │       └── SettingsRepository.kt
│       │   ├── navigation/
│       │   │   └── Screen.kt             # 路由定义
│       │   └── ui/
│       │       ├── screen/
│       │       │   ├── home/             # 首页
│       │       │   ├── detail/          # 题库详情
│       │       │   ├── quiz/            # 刷题练习
│       │       │   ├── wrong/           # 错题本
│       │       │   └── settings/        # 设置
│       │       └── theme/
│       │           ├── Color.kt         # 颜色定义
│       │           ├── Theme.kt         # 主题定义 (TikuTheme)
│       │           └── Type.kt          # 字体排版定义
│       └── res/
│           ├── drawable/                 # 图标资源
│           └── mipmap-*/                # 应用图标
├── .github/
│   └── workflows/
│       └── build-release.yml             # CI/CD 自动构建 & 发布
├── gradle/
│   └── libs.versions.toml                # 版本目录 (Version Catalog)
├── build.gradle.kts                      # 根级构建配置
├── settings.gradle.kts                    # 项目设置
├── gradle.properties                      # Gradle 属性
├── LICENSE                               # MIT 许可证
└── .gitignore
```

---

## 🚀 功能规划

### 当前已实现（v1.1.3）

- [x] 项目基础框架搭建（Kotlin + Jetpack Compose）
- [x] 题库列表页（品牌标题 + 导入按钮 + 题库卡片列表 + 设置入口）
- [x] 题库详情页（4 个彩色功能卡片 + 底部数据看板）
- [x] 刷题练习（顺序/随机/模拟考试模式，选择答案后即时反馈 + 解析展示）
- [x] 错题本页面（自动收录错题，支持展开查看详情）
- [x] 设置页（DataStore 持久化：主题切换跟随/亮色/暗色、字号小/标准/大/特大）
- [x] 刷题体验优化（答完自动进入下一题，答题完成显示正确率统计卡片）
- [x] 题目数据模型（支持四种题型：单选/多选/判断/简答）
- [x] Room 本地数据库（QuestionBank / Question / AnswerRecord 三表关联）
- [x] 题库导入（支持从 JSON 文件导入 + 内置示例数据）
- [x] M3E 主题系统（完整亮暗色支持、Material You 动态取色）
- [x] Repository 数据层（Entity ↔ Domain Model 转换，三 DAO 模式）
- [x] 答题记录持久化（AnswerRecord 表，答错自动收录到错题本）
- [x] 自定义应用图标（3D 堆叠卡片 + 绿色对勾）

### 题型支持

| 题型 | 标识 | 说明 |
|------|------|------|
| 单选题 | `SINGLE_CHOICE` | 从多个选项中选择一个正确答案 |
| 多选题 | `MULTI_CHOICE` | 从多个选项中选择多个正确答案 |
| 判断题 | `TRUE_FALSE` | 判断给定陈述的对错 |
| 简答题 | `SHORT_ANSWER` | 用户自行输入答案 |

### 待开发功能

#### P0 — 核心功能（优先级最高）

- [x] **答题记录持久化**：记录每次答题结果到数据库，支持历史统计
- [x] **错题自动收录**：答题结果写入数据库，答错自动记录

#### P1 — 重要功能

- [ ] **模拟考试**：限时答题，交卷后显示成绩和解析
- [ ] **学习统计**：刷题数量、正确率、学习时长等数据可视化
- [ ] **标签分类**：按学科/知识点对题目进行分类管理

#### P2 — 增强功能

- [ ] **在线题库同步**：通过网络 API 获取和同步题库数据
- [ ] **搜索功能**：按关键词搜索题目
- [ ] **收藏功能**：标记重点题目方便复习

#### P3 — 远期规划

- [ ] **多端适配**：平板、桌面端（大屏）适配
- [ ] **Widget 小组件**：桌面小组件展示今日刷题进度
- [ ] **国际化 (i18n)**：支持多语言切换

---

## 🏗 架构规范

### 架构模式

项目采用 **MVVM (Model-View-ViewModel)** 架构，结合 **Repository 模式**进行数据管理。

```
View (Composable Screen)
    ↕ ViewModel (状态管理 & 业务逻辑)
    ↕ Repository (数据聚合)
    ↕ Data Source (Local / Remote)
```

### 关键约定

1. **单向数据流 (UDF)**：UI 层通过 `StateFlow` / `LiveData` 观察数据，通过调用 ViewModel 方法触发事件
2. **Repository 模式**：所有数据访问通过 Repository，屏蔽本地/远程数据源差异
3. **不可变数据**：数据模型使用 `data class` + `val`，确保线程安全
4. **Composable 函数**：UI 组件应为无状态的纯函数，状态由 ViewModel 管理

### 命名规范

| 类别 | 规范 | 示例 |
|------|------|------|
| Screen | `XxxScreen.kt` | `HomeScreen.kt`, `QuizPracticeScreen.kt` |
| ViewModel | `XxxViewModel.kt` | `QuizViewModel.kt`, `SettingsViewModel.kt` |
| 数据模型 | 名词，单数 | `Question.kt`, `AnswerRecord.kt` |
| Repository | `XxxRepository.kt` | `QuestionRepository.kt`, `SettingsRepository.kt` |
| DAO | `XxxDao.kt` | `QuestionDao.kt`, `QuestionBankDao.kt`, `AnswerRecordDao.kt` |
| Entity | `XxxEntity.kt` | `QuestionEntity.kt`, `QuestionBankEntity.kt`, `AnswerRecordEntity.kt` |
| 通用组件 | `XxxCard.kt`, `XxxButton.kt` | `QuestionCard.kt` |
| 路由 | 驼峰命名 | `Home`, `QuizDetail`, `QuizPractice` |

---

## ⚙️ CI/CD 工作流

本项目使用 **GitHub Actions** 实现自动化构建与发布流程。

### 工作方式

1. **AI 辅助开发**：项目代码通过 AI 辅助编写，AI 将代码直接推送到本仓库
2. **自动触发构建**：当代码推送到 `main` 分支时，GitHub Actions 自动触发 APK 构建工作流
3. **自动发布 Release**：构建成功后，自动创建 GitHub Release 并上传 APK 安装包

### 工作流配置

工作流文件位于 `.github/workflows/build-release.yml`，主要流程：

```
代码推送至 main 分支
    → GitHub Actions 自动触发
    → 检出代码 & 配置 JDK 环境
    → Gradle 构建 Release APK (签名)
    → 构建成功 → 创建 GitHub Release
    → 上传 APK 到 Release Assets
```

### 版本管理

- 版本号定义在 `app/build.gradle.kts` 的 `versionCode` 和 `versionName` 中
- 每次功能更新或 Bug 修复时，需同步更新版本号
- 版本号更新时，需同步更新 README 的「版本历史」章节以及应用内的关于页面

### 注意事项

- 构建完成后需等待一段时间，再通过 API 或 GitHub 界面确认构建状态
- Release 标签与 `versionName` 保持一致（如 `v1.1.2`）

---

### Code Review

- 所有合并到 `main` 的代码需经过 Review
- 关注点：代码质量、架构一致性、M3E 设计规范遵循、性能影响

---

## 📋 版本历史

| 版本 | 日期 | 说明 |
|------|------|------|
| v1.1.3 | 2026-05-27 | 刷题体验优化：答完即自动进入下一题（1.5秒后），答题完成显示正确率统计；更新应用图标为文档+对勾风格 |
| v1.1.2 | 2026-05-27 | 数据层重构：QuestionBank/Question/AnswerRecord 三表关联；设置页 DataStore 持久化；错题本接入真实数据；自定义应用图标 |
| v1.1.1 | 2026-05-27 | 重构页面布局：题库列表页、题库详情页（含4个功能入口卡片和统计看板）、设置页、错题本；优化 M3E 主题系统亮暗色支持 |
| v1.1.0 | 2026-05-26 | 新增刷题练习、题库导入、Room 数据库、示例数据 |
| v1.0.0 | 2026-05-26 | 项目初始化，基础框架搭建 |

---

## 📄 许可证

本项目基于 [MIT License](LICENSE) 开源。

Copyright (c) 2026 Everett406
