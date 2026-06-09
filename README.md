# Tiku（题库）


<p align="center">
  <img src="docs/app-icon-preview.png" width="200" alt="Tiku 应用图标" />
</p>

<p align="center">
  一款面向学生的 Android 刷题应用，支持在线刷题、模拟考试、错题记录与学习统计
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Kotlin-1.9.24-blue" />
  <img src="https://img.shields.io/badge/Compose-1.7.0--rc01-brightgreen" />
  <img src="https://img.shields.io/badge/M3E-Android%2016+-orange" />
  <img src="https://img.shields.io/badge/MIT-License-green" />
</p>

---

## 功能特性

### 核心功能
- 📚 **题库管理** - 导入、管理多个题库
- ✍️ **多种题型** - 支持单选、多选、判断、简答
- 🎯 **刷题模式** - 顺序刷题、随机刷题、模拟考试
- 📝 **错题本** - 自动收录答错题目，随时复习
- 📊 **学习统计** - 追踪正确率，记录学习进度

### 技术亮点
- 🎨 **Material Design 3 Expressive** - Google 最新设计语言
- 🌙 **完整暗色支持** - 跟随系统或手动切换
- ♿ **无障碍支持** - 大字号模式，适合不同人群
- 💾 **离线优先** - Room 数据库，本地存储

---

## 技术栈

| 分类 | 技术 |
|------|------|
| 语言 | Kotlin 1.9.24 |
| UI | Jetpack Compose 1.7.0-rc01 + Material 3 |
| 架构 | MVVM + Repository |
| 数据库 | Room 3.0 |
| DI | Hilt |
| 异步 | Kotlin Coroutines + Flow |

---

## 项目结构

```
app/src/main/java/com/at210co60/tiku/
├── data/
│   ├── local/           # Room 数据库、DAO、Entity
│   ├── model/          # Domain Model
│   └── repository/     # 数据仓库
├── di/                 # Hilt 依赖注入模块
├── ui/
│   ├── components/     # 通用 UI 组件
│   ├── screen/        # 页面
│   └── theme/         # 主题、配色、字体
└── viewmodel/         # ViewModel
```

---

## 快速开始

### 环境要求
- Android Studio Hedgehog (2024.1.1) 或更高
- JDK 17
- Android SDK 35

### 构建
```bash
./gradlew assembleDebug    # Debug APK
./gradlew assembleRelease  # Release APK (需要签名配置)
```

---

## 当前已实现（v1.3.0）

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
- [x] **UI 全面重构**：暖色极简设计语言（米白背景、琥珀主色调）、统一组件库 WarmComponents、12dp 圆角规范、大量留白 iOS 风格

---

## 版本历史

| 版本 | 日期 | 说明 |
|------|------|------|
| v1.3.0 | 2026-06-08 | UI 全面重构：暖色极简设计语言（米白背景 WarmWhite #FAFAF8、琥珀主色调 AccentPrimary #C4A574）、新建统一组件库 WarmComponents、重写所有页面（HomeScreen/QuizPracticeScreen/WrongQuestionsScreen/SettingsScreen/QuizDetailScreen）、12dp 圆角规范、大量留白 iOS 风格 |
| v1.2.0 | 2026-05-29 | 重大修复：多选题支持多选并正确判断；简答题显示参考答案；手动确认进入下一题；错题本显示完整题目内容、选项、正确答案和解析；支持删除错题；设置页增加关于和清除数据功能 |

---

## LICENSE

MIT License - 详见 [LICENSE](LICENSE) 文件

---

## CI/CD

项目使用 GitHub Actions 自动构建：

- ✅ 每次 push 到 main 分支自动构建 Debug APK
- ✅ 每次 push 到 main 分支自动构建并发布 Release APK（tag v*.*.* 触发）

构建产物在 GitHub Releases 页面获取。
