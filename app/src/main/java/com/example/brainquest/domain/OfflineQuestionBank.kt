package com.example.brainquest.domain

object OfflineQuestionBank {
    val questions = listOf(
        QuizQuestion(
            question = "Which data structure follows First In, First Out (FIFO)?",
            correctAnswer = "Queue",
            options = listOf("Stack", "Queue", "Tree", "Graph")
        ),
        QuizQuestion(
            question = "What does SQL primarily manage?",
            correctAnswer = "Relational data",
            options = listOf("Relational data", "CPU instructions", "Image pixels", "Network cables")
        ),
        QuizQuestion(
            question = "Which HTTP method is commonly used to retrieve a resource?",
            correctAnswer = "GET",
            options = listOf("GET", "POST", "DELETE", "PATCH")
        ),
        QuizQuestion(
            question = "In object-oriented programming, what is encapsulation?",
            correctAnswer = "Bundling data and behaviour together",
            options = listOf(
                "Bundling data and behaviour together",
                "Running code only once",
                "Removing all classes",
                "Storing data only in files"
            )
        ),
        QuizQuestion(
            question = "Which component stores data permanently in this BrainQuest app?",
            correctAnswer = "Room database",
            options = listOf("Room database", "Composable function", "Navigation route", "Preview annotation")
        )
    )
}
