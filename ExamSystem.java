import java.io.*;
import java.util.*;
import java.util.concurrent.*;

class Teacher
{
    String id;
    String name;
    String department;

    public Teacher(String id, String name, String department)
    {
        this.id = id;
        this.name = name;
        this.department = department;
    }
}

public class ExamSystem
{
    static Scanner sc = new Scanner(System.in);

    static final String TEACHER_FILE = "teachers.txt";
    static final String PAPER_FILE = "papers.txt";
    static final String QUESTION_FILE = "questions.txt";
    static final String STUDENT_FILE = "students.txt";
    static final String RESULT_FILE = "results.txt";

    public static void main(String[] args) throws Exception
    {
        while (true)
        {
            System.out.println("\n========== EXAM SYSTEM ==========");
            System.out.println("1. Register Teacher");
            System.out.println("2. Create Question Paper");
            System.out.println("3. Student Entry");
            System.out.println("4. Exit");
            System.out.print("Enter Choice: ");

            int choice = Integer.parseInt(sc.nextLine());

            switch (choice)
            {
                case 1:
                {
                    registerTeacher();
                    break;
                }

                case 2:
                {
                    createPaper();
                    break;
                }

                case 3:
                {
                    studentExam();
                    break;
                }

                case 4:
                {
                    System.out.println("System Closed.");
                    System.exit(0);
                }

                default:
                {
                    System.out.println("Invalid Choice!");
                }
            }
        }
    }

    static void registerTeacher() throws Exception
    {
        System.out.print("Enter Teacher ID: ");
        String id = sc.nextLine();

        System.out.print("Enter Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Department: ");
        String dept = sc.nextLine();

        BufferedWriter bw = new BufferedWriter(new FileWriter(TEACHER_FILE, true));
        bw.write(id + "," + name + "," + dept);
        bw.newLine();
        bw.close();

        System.out.println("Teacher Registered Successfully!");
    }

    static boolean teacherExists(String id) throws Exception
    {
        File file = new File(TEACHER_FILE);

        if (!file.exists())
        {
            return false;
        }

        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;

        while ((line = br.readLine()) != null)
        {
            String[] data = line.split(",");
            if (data[0].equals(id))
            {
                br.close();
                return true;
            }
        }

        br.close();
        return false;
    }

    static void createPaper() throws Exception
    {
        System.out.print("Enter Teacher ID: ");
        String teacherID = sc.nextLine();

        if (!teacherExists(teacherID))
        {
            System.out.println("Invalid Teacher ID!");
            return;
        }

        System.out.print("Enter Subject: ");
        String subject = sc.nextLine();

        System.out.print("Enter Subject Code: ");
        String subjectCode = sc.nextLine();

        System.out.print("Enter Paper Code: ");
        String paperCode = sc.nextLine();

        BufferedWriter bw = new BufferedWriter(new FileWriter(PAPER_FILE, true));
        bw.write(paperCode + "," + subject + "," + subjectCode + "," + teacherID);
        bw.newLine();
        bw.close();

        System.out.print("Enter number of questions: ");
        int n = Integer.parseInt(sc.nextLine());

        for (int i = 0; i < n; i++)
        {
            System.out.println("\nEnter Question:");
            String qText = sc.nextLine();

            String[] options = new String[4];

            for (int j = 0; j < 4; j++)
            {
                System.out.print("Option " + (j + 1) + ": ");
                options[j] = sc.nextLine();
            }

            System.out.print("Correct Option (1-4): ");
            int correct = Integer.parseInt(sc.nextLine());

            System.out.print("Time Limit (seconds): ");
            int timeLimit = Integer.parseInt(sc.nextLine());

            BufferedWriter qb = new BufferedWriter(new FileWriter(QUESTION_FILE, true));
            qb.write(paperCode + "|" + qText + "|" +
                    options[0] + "|" +
                    options[1] + "|" +
                    options[2] + "|" +
                    options[3] + "|" +
                    correct + "|" + timeLimit);
            qb.newLine();
            qb.close();
        }

        System.out.println("Paper Created Successfully!");
    }

    static void studentExam() throws Exception
    {
        System.out.print("Enter Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Roll: ");
        String roll = sc.nextLine();

        System.out.print("Enter Department: ");
        String dept = sc.nextLine();

        BufferedWriter sw = new BufferedWriter(new FileWriter(STUDENT_FILE, true));
        sw.write(roll + "," + name + "," + dept);
        sw.newLine();
        sw.close();

        System.out.print("Enter Paper Code: ");
        String paperCode = sc.nextLine();

        BufferedReader br = new BufferedReader(new FileReader(QUESTION_FILE));

        String line;
        int score = 0;
        int total = 0;

        ArrayList<String> wrongQuestions = new ArrayList<>();

        while ((line = br.readLine()) != null)
        {
            String[] data = line.split("\\|");

            if (data[0].equals(paperCode))
            {
                total++;

                System.out.println("\nQuestion: " + data[1]);

                for (int i = 2; i <= 5; i++)
                {
                    System.out.println((i - 1) + ". " + data[i]);
                }

                int correct = Integer.parseInt(data[6]);
                int timeLimit = Integer.parseInt(data[7]);

                ExecutorService executor = Executors.newSingleThreadExecutor();

                Future<String> future = executor.submit(() ->
                {
                    Scanner tempScanner = new Scanner(System.in);
                    return tempScanner.nextLine();
                });

                String answer = null;
                boolean timeout = false;

                try
                {
                    answer = future.get(timeLimit, TimeUnit.SECONDS);
                }
                catch (TimeoutException e)
                {
                    timeout = true;
                    System.out.println("\nTime Up! Moving to next question...");
                }
                catch (Exception e)
                {
                    timeout = true;
                }

                executor.shutdownNow();

                int ans = -1;

                if (!timeout)
                {
                    try
                    {
                        ans = Integer.parseInt(answer);
                    }
                    catch (Exception e)
                    {
                        ans = -1;
                    }
                }

                if (!timeout && ans == correct)
                {
                    score++;
                }
                else
                {
                    String wrongDetail =
                            "\nQuestion: " + data[1] +
                            "\nYour Answer: " + (timeout ? "Not Answered (Time Up)" : ans) +
                            "\nCorrect Answer: " + correct + ". " + data[correct + 1];

                    wrongQuestions.add(wrongDetail);
                }
            }
        }

        br.close();

        BufferedWriter rw = new BufferedWriter(new FileWriter(RESULT_FILE, true));
        rw.write(roll + "," + paperCode + "," + score + "," + total);
        rw.newLine();
        rw.close();

        System.out.println("\n========== RESULT ==========");
        System.out.println("Score: " + score + "/" + total);

        if (!wrongQuestions.isEmpty())
        {
            System.out.println("\nWrong Answered Questions:");

            for (String w : wrongQuestions)
            {
                System.out.println(w);
                System.out.println("-------------------------");
            }
        }
        else
        {
            System.out.println("All Answers Correct!");
        }

        System.out.println("\nResult Saved Successfully!");
    }
}
