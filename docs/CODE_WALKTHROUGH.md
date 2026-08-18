# BrainQuest Code Walkthrough

## 1. MainActivity and application

`BrainQuestApplication.kt` enables Hilt. `MainActivity.kt` is the single Android activity. It receives three Hilt-created ViewModels and launches the Compose UI.

## 2. Navigation

`BrainQuestApp.kt` contains the Navigation Compose graph and bottom navigation. The four required destinations are Home, Quiz, Stats and Settings.

## 3. Quiz data flow

1. The user presses **Start Quiz**.
2. `QuizViewModel.startQuiz()` asks `TriviaRepository` for questions.
3. `TriviaRepositoryImpl` calls `TriviaApi` through Retrofit.
4. The repository converts the network DTOs into simple `QuizQuestion` domain objects.
5. `QuizViewModel` owns the current question, selected answer and score.
6. `QuizScorer` handles correctness and percentage calculations.
7. When the quiz is complete, `StatsRepository` saves one attempt into Room.

## 4. Statistics data flow

`AttemptDao.observeAttempts()` exposes Room data as a `Flow`. `StatsRepositoryImpl` converts database entities into `QuizAttempt` objects. `StatsViewModel` exposes a lifecycle-friendly `StateFlow`, and `StatsScreen` displays the result.

## 5. Settings data flow

`SettingsRepository` stores difficulty, question count and feedback preference in Preferences DataStore. `SettingsViewModel` exposes those preferences as a `StateFlow`.

## 6. Dependency injection

`AppModule.kt` creates Retrofit, the API interface, Room database and DAO. A second Hilt module binds repository interfaces to their implementations. This means the UI/ViewModels do not create databases or network services directly.

## 7. Testing

- `QuizScorerTest.kt` checks answer comparison and percentage logic.
- `StatsCalculatorTest.kt` checks average and best-score calculations.
- `LandingScreenTest.kt` is an instrumented Compose UI test that verifies the main Start Quiz action.
