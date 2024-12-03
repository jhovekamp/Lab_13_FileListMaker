import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

    public class Lab_13_FileListMaker {
        private static ArrayList<String> myArrList = new ArrayList<>();
        private static Scanner in = new Scanner(System.in);
        private static boolean toSave = false;  // Tracks if the list has been modified

        public static void main(String[] args) {
            boolean done = false;
            while (!done) {
                displayMenu();
                String command = SafeInput.getRegExString(in, "\nSelect an action from the menu", "[AaDdIiVvQqMmOoSsCc]");

                switch (command.toUpperCase()) {
                    case "A":
                        addItem();
                        break;
                    case "C":
                        clearList();
                        break;
                    case "D":
                        deleteItem();
                        break;
                    case "I":
                        insertItem();
                        break;
                    case "M":
                        moveItem();
                        break;
                    case "O":
                        openFile();
                        break;
                    case "S":
                        saveFile();
                        break;
                    case "V":
                        viewList();
                        break;
                    case "Q":
                        if (toSave && SafeInput.getYNConfirm(in, "Do you want to save the list before quitting? (Y/N): ")) {
                            saveFile();
                        }
                        System.out.println("Good Bye");
                        return;
                }
            }
        }

        private static void displayMenu() {
            System.out.println("\n-----Current List-----");
            viewList();
            System.out.println("\n-----Menu Choices-----");
            System.out.println("A – Add an item to the list");
            System.out.println("C – Clear removes all the elements from the current list");
            System.out.println("D – Delete an item from the list");
            System.out.println("I – Insert an item into the list");
            System.out.println("M – Move an item");
            System.out.println("O – Open a list file from disk");
            System.out.println("S - Save the current list to disk");
            System.out.println("V - View");
            System.out.println("Q – Quit the program");
        }

        private static void addItem() {
            String item = SafeInput.getRegExString(in, "\nEnter the item you would like to add: ", ".*");
            myArrList.add(item);
            toSave = true;
        }


        private static void clearList()
        {
            if (myArrList.isEmpty()) {
                System.out.println("The list is blank.");
            } else {
                myArrList.clear();
                toSave = true;
            }
        }


        private static void deleteItem() {
            if (myArrList.isEmpty()) {
                System.out.println("TThe list is blank. Cannot delete.");
                return;
            }

            viewList();
            int index = SafeInput.getRangedInt(in, "Select the item number you would like to delete: ", 1, myArrList.size()) - 1;
            String removedItem = myArrList.remove(index);
            toSave = true;
        }

        private static void insertItem() {
            if (myArrList.isEmpty()) {
                System.out.println("The list is blank. Adding an item");
            }

            viewList();
            int index = SafeInput.getRangedInt(in, "Select the item number where you would like to insert the new item: ", 1, myArrList.size() + 1) - 1;
            String item = SafeInput.getRegExString(in, "Enter the item to add: ", ".*");
            myArrList.add(index, item);
            toSave = true;
        }


        private static void moveItem() {
            if (myArrList.isEmpty()) {
                System.out.println("The list is blank. Cannot move items.");
                return;
            }

            viewList();
            int currentIndex = SafeInput.getRangedInt(in, "Select the item number you would like to move: ", 1, myArrList.size()) - 1;
            int newIndex = SafeInput.getRangedInt(in, "Enter the new position for the item: ", 1, myArrList.size()) - 1;

            String itemToMove = myArrList.remove(currentIndex);
            myArrList.add(newIndex, itemToMove);
            toSave = true;
        }


        private static void openFile() {
            if (toSave && SafeInput.getYNConfirm(in, "You have unsaved changes. Do you want to save them before opening a new file? (Y/N): ")) {
                saveFile();
            }

            String fileName = SafeInput.getRegExString(in, "Enter the filename you would like to open [filename.txt]: ", ".*");
            Path filePath = Paths.get("src", fileName);

            try {
                if (Files.exists(filePath)) {
                    myArrList.clear();  // Clear the current list before loading the new one
                    Files.lines(filePath).forEach(myArrList::add);
                    toSave = false;  // No need to save after loading
                    System.out.println("File has been saved.");
                } else {
                    System.out.println("File not found.");
                }
            } catch (IOException e) {
                System.out.println("Error opening the file: " + e.getMessage());
            }
        }


       private static void saveFile() {
            if (myArrList.isEmpty()) {
                System.out.println("The list is blank. Cannot save.");
                return;
            }

            String fileName = SafeInput.getRegExString(in, "Enter the filename [MyList.txt]: ", ".*");
            Path filePath = Paths.get("src", fileName);

            try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
                for (String item : myArrList) {
                    writer.write(item);
                    writer.newLine();
                }
                toSave = false;
                System.out.println("List saved to the following location: " + filePath);
            } catch (IOException e) {
                System.out.println("Error saving the file: " + e.getMessage());
            }
        }


        private static void viewList() {
            if (myArrList.isEmpty()) {
                System.out.println("The list is blank.");
                return;
            }

            for (int i = 0; i < myArrList.size(); i++) {
                System.out.println((i + 1) + ". " + myArrList.get(i));
            }
        }

    }