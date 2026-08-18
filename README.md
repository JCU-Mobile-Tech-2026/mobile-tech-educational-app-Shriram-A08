# BrainQuest – CP3406 Education App# BrainQuest

BrainQuest is an educational quiz app created for CP3406 Assessment 3. The app is designed to help students practise basic Computer Science and IT questions.

## Main Features

* Multiple-choice Computer Science quizzes
* Different difficulty levels
* Different number of questions
* Online questions using the Open Trivia Database API
* Offline backup questions
* Settings saved using DataStore
* Quiz results saved using Room Database
* Statistics showing attempts, best score and average score

## Main Screens

The app has four main screens:

* Home
* Quiz
* Settings
* Statistics

## Technologies Used

* Kotlin
* Jetpack Compose
* Material Design 3
* Navigation Compose
* ViewModel
* Repository Pattern
* Retrofit
* Room Database
* DataStore
* JUnit and Compose UI Testing

## App Structure

The app uses ViewModels and repositories to separate the UI, app logic and data.

UI  
↓  
ViewModel  
↓  
Repository  
↓  
API / Room / DataStore
## Testing

Tests are included for quiz scoring, statistics calculations and basic UI functionality.

## Ethical Design

BrainQuest does not require an account or unnecessary permissions such as location, camera or microphone. Quiz results are stored locally and the user can clear their saved history.

## Running the App

1. Open the project in Android Studio.
2. Wait for Gradle Sync to finish.
3. Select an emulator or Android device.
4. Click Run

## AI Use

Generative AI was used for guidance, Kotlin/Compose coding support and debugging. The required AI declaration will be submitted separately.