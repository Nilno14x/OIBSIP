package Orasis.OASIS_4th_Task_java;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;

class Login extends JFrame implements ActionListener {
    JButton submitButton;
    JPanel panel;
    JLabel userLabel, passLabel;
    final JTextField usernameField, passwordField;

    Login() {
        userLabel = new JLabel();
        userLabel.setText("Username:");
        usernameField = new JTextField(15);

        passLabel = new JLabel();
        passLabel.setText("Password:");
        passwordField = new JPasswordField(8);

        submitButton = new JButton("SUBMIT");
        panel = new JPanel(new GridLayout(3, 1));
        panel.add(userLabel);
        panel.add(usernameField);
        panel.add(passLabel);
        panel.add(passwordField);
        panel.add(submitButton);

        add(panel, BorderLayout.CENTER);
        submitButton.addActionListener(this);
        setTitle("Login Form");
    }

    public void actionPerformed(ActionEvent ae) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (!password.equals("")) {
            new OnlineTestBegin(username); // Start the quiz with the provided username
        } else {
            JOptionPane.showMessageDialog(this, "Please enter a password.");
        }
    }
}

class OnlineTestBegin extends JFrame implements ActionListener {
    JLabel questionLabel;
    JLabel timeLabel;
    JRadioButton[] options;
    JButton saveAndNextButton, skipForNowButton, resultButton;
    ButtonGroup buttonGroup;

    int count = 0, current = 0;
    int[] markedAnswers = new int[10];
    Timer timer = new Timer();
    int timeRemaining = 600; // Initial time limit in seconds

    String[] questions = {
        "Who is known as the father of the Java programming language?",
        "How many primitive data types are there in Java?",
        "Where is the system class defined?",
        "In Java, where is an exception created by a 'try' block caught?",
        "Which of the following is not an OOPS concept in Java?",
        "Which of these is an infinite loop?",
        "When is the finalize() method called in Java?",
        "What is the implicit return type of a constructor?",
        "What is the topmost class in the exception class hierarchy in Java?",
        "Which component provides a runtime environment for Java bytecode to be executed?"
    };

    String[][] answerChoices = {
        {"Charles Babbage", "James Gosling", "M.P. Java", "Blaise Pascal"},
        {"6", "7", "8", "9"},
        {"java.lang package", "java.util package", "java.io package", "None"},
        {"catch", "throw", "finally", "thrown"},
        {"Polymorphism", "Inheritance", "Compilation", "Encapsulation"},
        {"for(;;)", "for() i=0; j<1; i--", "for(int=0; i++)", "if (All of the above)"},
        {"Before garbage collection", "Before an object goes out of scope", "Before a variable goes out of scope", "None"},
        {"No return type", "A class object in which it is defined", "void", "None"},
        {"ArithmeticException", "Throwable", "Object", "Console"},
        {"JDK", "JVM", "JRE", "JAVAC"}
    };

    int[] correctAnswers = {1, 2, 0, 0, 2, 0, 1, 2, 1, 1}; // Assuming correct answers (0-based index) for each question

    int correctCount = 0; // Track the number of correct answers
    int skippedCount = 0; // Track the number of skipped questions
    int totalQuestions = questions.length; // Total number of questions

    OnlineTestBegin(String username) {
        setTitle("Online Test - Welcome, " + username);

        questionLabel = new JLabel();
        questionLabel.setBounds(30, 40, 450, 20);

        timeLabel = new JLabel();
        timeLabel.setBounds(20, 20, 450, 20);

        options = new JRadioButton[4];
        for (int i = 0; i < 4; i++) {
            options[i] = new JRadioButton();
            options[i].setBounds(50, 80 + i * 30, 400, 20);
        }

        buttonGroup = new ButtonGroup();
        for (int i = 0; i < 4; i++) {
            buttonGroup.add(options[i]);
        }

        saveAndNextButton = new JButton("Save and Next");
        saveAndNextButton.setBounds(95, 240, 140, 30);
        saveAndNextButton.addActionListener(this);

        skipForNowButton = new JButton("Skip for Now");
        skipForNowButton.setBounds(270, 240, 150, 30);
        skipForNowButton.addActionListener(this);

        resultButton = new JButton("Result");
        resultButton.setBounds(470, 240, 140, 30);
        resultButton.addActionListener(this);

        add(questionLabel);
        add(timeLabel);
        for (int i = 0; i < 4; i++) {
            add(options[i]);
        }
        add(saveAndNextButton);

        // Add a condition to make "Skip for Now" invisible on the last question
        if (current < totalQuestions - 1) {
            add(skipForNowButton);
        }

        add(resultButton);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setLocation(250, 100);
        setVisible(true);
        setSize(700, 350);

        startTimer();
        showNextQuestion();
    }

    private void startTimer() {
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                timeLabel.setText("Time left: " + timeRemaining + " seconds");
                timeRemaining--;
                if (timeRemaining < 0) {
                    timer.cancel();
                    timeLabel.setText("Time Out");
                    showResult();
                }
            }
        }, 0, 1000);
    }

    private void showNextQuestion() {
        questionLabel.setText("Que" + (current + 1) + ": " + questions[current]);
        for (int i = 0; i < options.length; i++) {
            options[i].setText(answerChoices[current][i]);
            options[i].setSelected(false);
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == saveAndNextButton) {
            check();
            current++;
            if (current == totalQuestions - 1) {
                saveAndNextButton.setEnabled(false);
                resultButton.setText("Result");
            }
            showNextQuestion();
        } else if (e.getSource() == skipForNowButton) {
            if (current == totalQuestions - 1) {
                // Skip the last question and count it as skipped
                skippedCount++;
                current++;
                skipForNowButton.setEnabled(false);
                resultButton.setText("Result");
            } else {
                markedAnswers[current] = -1; // Mark as skipped
                skippedCount++;
                current++;
                if (current == totalQuestions - 1) {
                    skipForNowButton.setEnabled(false); // Disable "Skip for Now" on the last question
                    resultButton.setText("Result");
                }
            }
            showNextQuestion();
        } else if (e.getSource() == resultButton) {
            calculateLastQuestion();
            showResult();
        }
    }

    private void check() {
        int selectedOption = getSelectedOption();
        if (selectedOption != -1 && selectedOption == correctAnswers[current]) {
            correctCount++;
        }
        markedAnswers[current] = selectedOption;
    }

    private int getSelectedOption() {
        for (int i = 0; i < options.length; i++) {
            if (options[i].isSelected()) {
                return i;
            }
        }
        return -1; // No option selected
    }

    private void calculateLastQuestion() {
        check(); // Calculate marks for the current question
        int attended = 0;
        int totalPoints = totalQuestions * 2; // Assuming each question is worth 2 marks

        for (int i = 0; i < markedAnswers.length; i++) {
            if (markedAnswers[i] != -1) {
                attended++;
            }
        }

        int marks = correctCount * 2; // Calculate marks based on the number of correct answers

        String resultMessage = "Questions Attended: " + attended + "\n";
        resultMessage += "Questions Skipped: " + skippedCount + "\n"; // Display skipped count
        resultMessage += "Marks Obtained: " + marks + " out of " + totalPoints;

        JOptionPane.showMessageDialog(this, resultMessage, "Result", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }

    private void showResult() {
        int attended = 0;
        int totalPoints = totalQuestions * 2; // Assuming each question is worth 2 marks

        for (int i = 0; i < markedAnswers.length; i++) {
            if (markedAnswers[i] != -1) {
                attended++;
            }
        }

        int marks = correctCount * 2; // Calculate marks based on the number of correct answers

        String resultMessage = "Questions Attended: " + attended + "\n";
        resultMessage += "Questions Skipped: " + skippedCount + "\n"; // Display skipped count
        resultMessage += "Marks Obtained: " + marks + " out of " + totalPoints;

        JOptionPane.showMessageDialog(this, resultMessage, "Result", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }
}

class OnlineExam {
    public static void main(String args[]) {
        try {
            Login form = new Login();
            form.setSize(400, 150);
            form.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
}
