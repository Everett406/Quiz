# Tiku（题库）

> 一款面向学生的 Android 刷题应用，支持在线刷题、模拟考试、错题记录与学习统计。

---

## 📖 项目概述

Tiku 是一个由学生团队协作开发的题库管理应用。项目 fork 自 [At210Co60/Quiz](https://github.com/At210Co60/Quiz)，在此基础上进行功能迭代与定制开发。

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
```

### 计划引入的依赖

> 以下依赖将在对应功能开发时引入，当前尚未集成。

| 依赖 | 用途 | 引入阶段 |
|------|------|----------|
| `Room` | 本地数据库，持久化题目和答题记录 | 数据层搭建 |
| `Retrofit + OkHttp` | 网络请求，在线题库同步 | 网络层搭建 |
| `Dagger Hilt` / `Koin` | 依赖注入 | 架构优化 |
| `DataStore` | 用户偏好设置存储 | 设置功能 |
| `Coil` / `Accompanist` | 图片加载 | 题目图片支持 |
| `Kotlinx Serialization` / `Gson` | JSON 序列化 | 数据解析 |
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
│       │   ├── TikuApplication.kt          # Application 类
│       │   ├── data/
│       │   │   └── model/
│       │   │       └── Question.kt         # 题目数据模型
│       │   ├── navigation/
│       │   │   └── Screen.kt               # 路由定义
│       │   └── ui/
│       │       ├── screen/
│       │       │   ├── home/
│       │       │   │   └── HomeScreen.kt   # 首页
│       │       │   └── question/
│       │       │       └── QuestionListScreen.kt  # 题目列表页
│       │       └── theme/
│       │           ├── Color.kt            # 颜色定义
│       │           ├── Theme.kt            # 主题定义 (TikuTheme)
│       │           └── Type.kt             # 字体排版定义
│       └── res/
│           └── values/                     # 资源文件
├── gradle/
│   └── libs.versions.toml                  # 版本目录 (Version Catalog)
├── build.gradle.kts                        # 根级构建配置
├── settings.gradle.kts                     # 项目设置
├── gradle.properties                       # Gradle 属性
└── .gitignore
```

### 计划扩展的目录结构

```
app/src/main/java/com/at210co60/tiku/
├── data/
│   ├── model/              # 数据模型 (Question, AnswerRecord, Category...)
│   ├── local/              # 本地数据源 (Room DAO, Database)
│   ├── remote/             # 远程数据源 (API 接口)
│   └── repository/         # 数据仓库 (Repository 模式)
├── ui/
│   ├── screen/             # 各页面
│   │   ├── home/           # 首页
│   │   ├── question/       # 刷题相关页面
│   │   ├── exam/           # 模拟考试
│   │   ├── wrong/          # 错题本
│   │   ├── stats/          # 学习统计
│   │   └── settings/       # 设置
│   ├── component/          # 通用可复用组件
│   └── theme/              # 主题、颜色、字体
├── navigation/             # 导航路由
├── viewmodel/              # ViewModel 层
└── util/                   # 工具类
```

---

## 🚀 功能规划

### 当前已实现（v1.0.0）

- [x] 项目基础框架搭建（Kotlin + Jetpack Compose）
- [x] 首页 UI（功能入口卡片）
- [x] 题目列表页（LazyColumn 展示）
- [x] 基础导航系统（首页 ↔ 题目列表）
- [x] 题目数据模型（支持四种题型）

### 题型支持

| 题型 | 标识 | 说明 |
|------|------|------|
| 单选题 | `SINGLE_CHOICE` | 从多个选项中选择一个正确答案 |
| 多选题 | `MULTI_CHOICE` | 从多个选项中选择多个正确答案 |
| 判断题 | `TRUE_FALSE` | 判断给定陈述的对错 |
| 简答题 | `SHORT_ANSWER` | 用户自行输入答案 |

### 待开发功能

#### P0 — 核心功能（优先级最高）

- [ ] **数据持久化**：引入 Room 数据库，本地存储题目和答题记录
- [ ] **刷题练习**：顺序刷题 / 随机刷题，支持即时反馈和解析展示
- [ ] **错题本**：自动收录做错的题目，支持按题型/标签筛选和重新练习
- [ ] **答题结果记录**：记录每道题的答题情况（正确/错误/未答）

#### P1 — 重要功能

- [ ] **模拟考试**：限时答题，交卷后显示成绩和解析
- [ ] **学习统计**：刷题数量、正确率、学习时长等数据可视化
- [ ] **题库导入**：支持从 JSON/CSV 文件导入题目数据
- [ ] **标签分类**：按学科/知识点对题目进行分类管理

#### P2 — 增强功能

- [ ] **在线题库同步**：通过网络 API 获取和同步题库数据
- [ ] **用户设置**：主题切换、字体大小、每日刷题目标等
- [ ] **搜索功能**：按关键词搜索题目
- [ ] **收藏功能**：标记重点题目方便复习

#### P3 — 远期规划

- [ ] **多端适配**：平板、桌面端（大屏）适配
- [ ] **Widget 小组件**：桌面小组件展示今日刷题进度
- [ ] **深色模式优化**：完善深色模式下的视觉体验
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
| Screen | `XxxScreen.kt` | `HomeScreen.kt`, `QuestionListScreen.kt` |
| ViewModel | `XxxViewModel.kt` | `QuizViewModel.kt`, `StatsViewModel.kt` |
| 数据模型 | 名词，单数 | `Question.kt`, `AnswerRecord.kt` |
| Repository | `XxxRepository.kt` | `QuestionRepository.kt` |
| DAO | `XxxDao.kt` | `QuestionDao.kt` |
| 通用组件 | `XxxCard.kt`, `XxxButton.kt` | `QuestionCard.kt` |
| 路由 | 驼峰命名 | `Home`, `QuestionList`, `QuizPractice` |

---

## 🤝 协作规范

### Git 工作流

- **主分支**：`main` — 稳定版本，受保护
- **开发分支**：`dev` — 日常开发合并目标
- **功能分支**：`feature/xxx` — 单个功能开发
- **修复分支**：`fix/xxx` — Bug 修复

### Commit 规范

遵循 [Conventional Commits](https://www.conventionalcommits.org/) 规范：

```
<type>(<scope>): <description>

type: feat | fix | docs | style | refactor | perf | test | chore
scope: ui | data | nav | build | ...
```

示例：
```
feat(ui): add quiz practice screen
fix(data): correct question model nullable fields
docs: update README with M3E design guidelines
```

### Code Review

- 所有合并到 `dev` / `main` 的代码需经过 Review
- 关注点：代码质量、架构一致性、M3E 设计规范遵循、性能影响

---

## 📋 版本历史

| 版本 | 日期 | 说明 |
|------|------|------|
| v1.0.0 | 2026-05-26 | 项目初始化，基础框架搭建 |

---

## 📄 许可证

本项目基于原项目 [At210Co60/Quiz](https://github.com/At210Co60/Quiz) 进行开发，具体许可证请参阅原项目。
