package Orasis.OASIS_2nd_Task_Java;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
import java.util.Collections;

public class NumberGuessingGame {
    private ArrayList<Integer> scoreBoard = new ArrayList<>();
    private int maxAttempts = 10;
    private int rounds = 1;
    private boolean displayMenu = true; // Flag to control menu display

    public static void main(String[] args) {
        NumberGuessingGame game = new NumberGuessingGame();
        game.run();
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;

        while (isRunning) {
            if (displayMenu) {
                displayMenu();
            }
            int choice = getUserChoice(scanner);

            switch (choice) {
                case 1:
                    playGame(scanner);
                    break;
                case 2:
                    displayScoreBoard();
                    break;
                case 3:
                    isRunning = false;
                    System.out.println("Thanks for playing the game!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        scanner.close();
    }

    private void displayMenu() {
        String border = "------------------------";
        System.out.println(border);
        System.out.println("|   Welcome to the     |");
        System.out.println("| Number Guessing Game |");
        System.out.println(border);
        System.out.println("|  1) Play the Game    |");
        System.out.println("|  2) Score Board      |");
        System.out.println("|  3) Exit the Game    |");
        System.out.println(border);
        displayMenu = false; // Set the flag to false to hide the menu after displaying it once
    }

    private int getUserChoice(Scanner scanner) {
        int choice = 0;
        boolean isValidChoice = false;

        while (!isValidChoice) {
            try {
                System.out.print("Enter your choice: ");
                choice = scanner.nextInt();
                isValidChoice = true;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next(); // Clear the invalid input
            }
        }

        return choice;
    }

    private void playGame(Scanner scanner) {
        System.out.print("Enter the range of numbers (e.g., 1-100): ");
        int numberRange = getValidNumberRange(scanner);
        int randomNumber = generateRandomNumber(numberRange);

        System.out.println("\nRound " + rounds + ": Guess the number between 1 and " + numberRange);
        System.out.println("You have a maximum of " + maxAttempts + " attempts in this round.");
        
        int attempts = guessNumber(randomNumber, scanner);

        if (attempts <= maxAttempts) {
            System.out.println("Congratulations! You guessed the number in " + attempts + " attempts.");
            scoreBoard.add(attempts);
        } else {
            System.out.println("Sorry, you've exceeded the maximum number of attempts.");
        }

        rounds++;
    }

    private int getValidNumberRange(Scanner scanner) {
        int numberRange = 0;
        boolean isValidRange = false;

        while (!isValidRange) {
            try {
                String rangeInput = scanner.next();
                String[] rangeParts = rangeInput.split("-");
                int min = Integer.parseInt(rangeParts[0]);
                int max = Integer.parseInt(rangeParts[1]);

                if (min < max && min > 0) {
                    numberRange = max - min + 1;
                    isValidRange = true;
                } else {
                    System.out.println("Invalid range. Please enter a valid range (e.g., 1-100).");
                }
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                System.out.println("Invalid input. Please enter a valid range (e.g., 1-100).");
            }
        }

        return numberRange;
    }

    private int generateRandomNumber(int numberRange) {
        Random random = new Random();
        return random.nextInt(numberRange) + 1;
    }

    private int guessNumber(int randomNumber, Scanner scanner) {
        int attempts = 0;

        while (true) {
            System.out.print("Enter your guess: ");
            int userGuess = scanner.nextInt();
            attempts++;

            if (userGuess > randomNumber) {
                System.out.println("Lower.");
            } else if (userGuess < randomNumber) {
                System.out.println("Higher.");
            } else {
                break;
            }
        }

        return attempts;
    }

    private void displayScoreBoard() {
        String border = "-------------------------------------";
        System.out.println(border);
        System.out.println("|              Score Board          |");
        System.out.println(border);

        if (scoreBoard.isEmpty()) {
            System.out.println("|         No games played yet       |");
        } else {
            Collections.sort(scoreBoard);
            System.out.println("| Your best score:   " + String.format("%5d", scoreBoard.get(0)) + " attempts |");

            System.out.println("| All scores:                       |");
            for (int i = 0; i < scoreBoard.size(); i++) {
                System.out.println("| Round " + (i + 1) + ":           " + String.format("%5d", scoreBoard.get(i)) + " attempts |");
            }
        }
        
        System.out.println(border);
    }
}