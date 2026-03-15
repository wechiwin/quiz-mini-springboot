# Quiz Mini - 间隔重复学习系统

<div align="left" style="display: flex; flex-wrap: wrap; gap: 0.5rem; align-items: center; margin: 0.75rem 0;">
  <img src="https://img.shields.io/badge/Java-8+-ED8B00.svg?style=flat&logo=openjdk" alt="Java 8+" />
  <img src="https://img.shields.io/badge/Spring%20Boot-2.2.2-6DB33F.svg?style=flat&logo=springboot" alt="Spring Boot 2.2.2" />
  <img src="https://img.shields.io/badge/MyBatis%20Plus-3.5.0-BF1BB9.svg?style=flat&logo=mybatis" alt="MyBatis Plus 3.5.0" />
  <img src="https://img.shields.io/badge/SQLite-3-003B57.svg?style=flat&logo=sqlite" alt="SQLite" />
  <img src="https://img.shields.io/badge/Thymeleaf-3-005F0F.svg?style=flat&logo=thymeleaf" alt="Thymeleaf" />
  <img src="https://img.shields.io/badge/Vue.js-3-4FC08D.svg?style=flat&logo=vuedotjs" alt="Vue.js" />
  <img src="https://img.shields.io/badge/License-MIT-yellow.svg" alt="MIT License" />
</div>

[English](README.md)

一个基于 Spring Boot 的轻量级意大利语学习应用，实现了艾宾浩斯遗忘曲线算法，用于高效的间隔重复学习意大利语词汇和语法。

## 功能特性

- **专注意大利语**：专为学习意大利语词汇、动词变位和语法设计
- **间隔重复**：实现艾宾浩斯遗忘曲线，复习间隔为 1、2、4、7、15、30、60 和 90 天
- **文件夹管理**：将记忆卡片组织到不同的文件夹/分类中（如按主题或难度）
- **Excel 导入/导出**：通过 Excel 模板批量导入意大利语词汇
- **随机学习模式**：随机选择卡片，增加学习多样性
- **进度追踪**：追踪连续成功次数，并可将卡片标记为已掌握
- **SQLite 数据库**：轻量级文件存储，无需外部数据库

## 技术栈

- Java 8+
- Spring Boot 2.2.2
- MyBatis-Plus 3.5.0
- SQLite
- Thymeleaf
- EasyExcel
- Hutool

## 快速开始

### 环境要求

- JDK 8 或更高版本
- Maven 3.x

### 安装步骤

1. 克隆仓库：
   ```bash
   git clone https://github.com/yourusername/quiz-mini-springboot.git
   cd quiz-mini-springboot
   ```

2. 构建并运行：
   ```bash
   mvn clean package
   java -jar target/quiz.jar
   ```

3. 打开浏览器访问 `http://localhost:8889`

## 使用说明

### 创建文件夹

文件夹帮助您按主题或分类组织意大利语记忆卡片（例如，动词、名词、问候语、旅行短语）。

### 添加卡片

每张卡片包含：

- **动词/原文**：需要学习的意大利语单词或句子
- **变位/翻译**：动词变位或翻译
- **例句**：可选的意大利语例句
- **人称**：可选的语法信息（io, tu, lui/lei 等）

### 学习模式

1. **顺序学习**：根据复习日期按顺序复习卡片
2. **随机学习**：随机选择卡片进行练习
3. **重新学习**：重置文件夹进度，从头开始

### 进度追踪

- 卡片根据成功回忆次数在复习间隔中推进
- 完全掌握后将卡片标记为"已完成"
- 随时间追踪学习进度

## 遗忘曲线算法

应用使用艾宾浩斯遗忘曲线，复习间隔如下：

| 连续成功次数 | 复习间隔 |
|--------|------|
| 1      | 1 天  |
| 2      | 2 天  |
| 3      | 4 天  |
| 4      | 7 天  |
| 5      | 15 天 |
| 6      | 30 天 |
| 7      | 60 天 |
| 8      | 90 天 |

## Docker 支持

使用 Docker 构建并运行：

```bash
docker build -t quiz-mini .
docker run -p 8889:8889 quiz-mini
```