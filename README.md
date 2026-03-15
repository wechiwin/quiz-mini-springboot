# Quiz Mini - Spaced Repetition Learning System

<div align="left" style="display: flex; flex-wrap: wrap; gap: 0.5rem; align-items: center; margin: 0.75rem 0;">
  <img src="https://img.shields.io/badge/Java-8+-ED8B00.svg?style=flat&logo=openjdk" alt="Java 8+" />
  <img src="https://img.shields.io/badge/Spring%20Boot-2.2.2-6DB33F.svg?style=flat&logo=springboot" alt="Spring Boot 2.2.2" />
  <img src="https://img.shields.io/badge/MyBatis%20Plus-3.5.0-BF1BB9.svg?style=flat&logo=mybatis" alt="MyBatis Plus 3.5.0" />
  <img src="https://img.shields.io/badge/SQLite-3-003B57.svg?style=flat&logo=sqlite" alt="SQLite" />
  <img src="https://img.shields.io/badge/Thymeleaf-3-005F0F.svg?style=flat&logo=thymeleaf" alt="Thymeleaf" />
  <img src="https://img.shields.io/badge/Vue.js-3-4FC08D.svg?style=flat&logo=vuedotjs" alt="Vue.js" />
  <img src="https://img.shields.io/badge/License-MIT-yellow.svg" alt="MIT License" />
</div>

[简体中文](README_zh-CN.md)

A lightweight Italian language learning application built with Spring Boot, implementing the Ebbinghaus forgetting curve
algorithm for efficient spaced repetition learning of Italian vocabulary and grammar.

## Features

- **Italian Language Focus**: Designed specifically for learning Italian vocabulary, verb conjugations, and grammar
- **Spaced Repetition**: Implements Ebbinghaus forgetting curve with review intervals of 1, 2, 4, 7, 15, 30, 60, and 90
  days
- **Folder Management**: Organize your flashcards into different folders/categories (e.g., by topic or difficulty)
- **Excel Import/Export**: Bulk import Italian vocabulary via Excel templates
- **Random Study Mode**: Randomized card selection for variety in learning
- **Progress Tracking**: Track consecutive successful recalls and mark cards as mastered
- **SQLite Database**: Lightweight, file-based storage with no external database required

## Tech Stack

- Java 8+
- Spring Boot 2.2.2
- MyBatis-Plus 3.5.0
- SQLite
- Thymeleaf
- EasyExcel
- Hutool

## Getting Started

### Prerequisites

- JDK 8 or higher
- Maven 3.x

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/quiz-mini-springboot.git
   cd quiz-mini-springboot
   ```

2. Build and run:
   ```bash
   mvn clean package
   java -jar target/quiz.jar
   ```

3. Open your browser and navigate to `http://localhost:8889`

## Usage

### Creating Folders

Folders help you organize Italian flashcards by subject or category (e.g., verbs, nouns, greetings, travel phrases).

### Adding Cards

Each card contains:

- **Verb/Original Text**: The Italian word or sentence to learn
- **Conjugation/Translation**: The conjugation or translation
- **Example Sentence**: Optional context in Italian
- **Grammatical Person**: Optional grammatical information (io, tu, lui/lei, etc.)

### Study Modes

1. **Sequential Study**: Review cards in order based on their review dates
2. **Random Study**: Practice with randomly selected cards
3. **Relearn**: Reset progress for a folder and start over

### Progress Tracking

- Cards move through review intervals based on successful recalls
- Mark cards as "done" when fully mastered
- Track your learning progress over time

## Forgetting Curve Algorithm

The application uses the Ebbinghaus forgetting curve with the following review intervals:

| Consecutive Successes | Review Interval |
|-----------------------|-----------------|
| 1                     | 1 day           |
| 2                     | 2 days          |
| 3                     | 4 days          |
| 4                     | 7 days          |
| 5                     | 15 days         |
| 6                     | 30 days         |
| 7                     | 60 days         |
| 8                     | 90 days         |

## Docker Support

Build and run with Docker:

```bash
docker build -t quiz-mini .
docker run -p 8889:8889 quiz-mini
```