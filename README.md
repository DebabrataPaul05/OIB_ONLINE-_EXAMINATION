# OIB_ONLINE-_EXAMINATION
Online Exam System (using Java)

The Online Exam System is a console-based Java application designed to simulate a structured examination environment for teachers and students. The system allows teachers to register and create multiple-choice question papers, while students can enter their details, attempt exams using a paper code, and receive instant evaluation with detailed results. All data is managed using text files, ensuring simple and persistent storage without the need for a database.

The application follows a modular design approach. Teachers can register using a unique ID and create question papers by defining subject details, paper codes, questions, four answer options per question, the correct option, and a time limit for each question. The system verifies teacher authenticity before allowing paper creation, ensuring controlled access.

Students can register by entering their name, roll number, and department. After providing a valid paper code, the system loads the corresponding questions and presents them one by one. Each question is time-bound using Java’s concurrency utilities, ensuring that students must answer within the specified time limit. If the time expires, the system automatically moves to the next question.

At the end of the exam, the system calculates the total score, displays the result, and shows details of incorrectly answered questions along with the correct answers. Results are stored in a separate file for record-keeping. The project demonstrates practical implementation of core Java concepts such as file handling, exception handling, collections, multithreading with ExecutorService, and structured input validation.

This project serves as a strong example of console-based system design and can be further enhanced by integrating a graphical user interface, database connectivity, authentication mechanisms, or advanced analytics for performance tracking.
