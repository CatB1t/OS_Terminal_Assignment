package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.util.Arrays;
import java.util.Scanner;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

class Parser {

    String commandName;
    String[] args;

    //This method will divide the input into commandName and args
    //where "input" is the string command entered by the user
    public boolean parse(String input)
    {
        input = input.trim();
        String[] inputWords = input.split(" ");
        commandName = inputWords[0];
        args = Arrays.copyOfRange(inputWords, 1, inputWords.length);
        return true;
    }

    public String getCommandName() { return commandName; }

    public String[] getArgs()
    {
        return args;
    }
    public boolean hasArgs() { return args.length > 0; }
}


public class Terminal {

    Scanner inputScanner;
    Parser parser;
    Path currentPath;
    boolean shouldExit = false;

    private void printErrorMessage() {
        System.out.println("Error: Command not found or invalid parameters are entered!");
    }

    private void printIgnoreArgs() {
        System.out.println("Ignoring non-option arguments");
    }

    public boolean isValidPath(String path) {
        try {
            Paths.get(path);
        } catch (InvalidPathException | NullPointerException ex) {
            System.out.println("Not a valid path.");
            return false;
        }
        return true;
    }

    public void echo(String[] args) {
        if (args.length == 0) {
            printErrorMessage();
            return;
        }

        String stringToPrint = "";
        for (String arg : args)
            stringToPrint += arg + " ";
        System.out.println(stringToPrint);
    }

    public void pwd()
    {
        if(parser.hasArgs())
            printIgnoreArgs();
        System.out.println(currentPath.toAbsolutePath().toString());
    }

    public void cd(String[] args) {
        if (args.length == 0) // Case no args
            currentPath = Paths.get(System.getProperty("user.home"));
        else if (args.length == 1) {
            String arg = args[0];
            if (arg.equals("..")) {
                try {
                    currentPath = currentPath.getParent().toAbsolutePath();
                }
                catch(NullPointerException ex) {
                    System.out.println("You are already at the root directory.");
                }
            } else {
                if (isValidPath(arg)) {
                    Path newPath = currentPath.resolve(arg);
                    if(Files.exists(newPath))
                        currentPath = newPath;
                    else
                        System.out.println("Directory does not exist.");
                } else {
                    System.out.println("Not a valid path.");
                    return;
                }
            }
        } else {
            printErrorMessage();
            return;
        }

        System.out.println("Current Path: " + currentPath.toString());
    }

    public void ls(boolean reversed) {
        String[] files;
        File file = currentPath.toFile();
        files = file.list();
        Arrays.sort(files);
        if(reversed)
        {
            for (int i = files.length - 1; i >= 0; i--) {
                System.out.print(files[i] + " \n");
            }
        }
        else {
            for (int i = 0; i < files.length; i++) {
                System.out.print(files[i] + " \n");
            }
        }
    }

    public void mkdir(String[] string) {
        for (String i : string) {

            if (!currentPath.resolve(i).toFile().mkdir()) {
                System.out.println("Error creating directory");
            }
        }
    }

    public void rm(String[] string) {
        File file = currentPath.resolve(string[0]).toFile();
        if (file.exists()) {
            file.delete();
        } else {
            System.out.println("No such file!");
        }
    }

    public void cat(String[] string) {
        if (string.length == 1) {
            File file = currentPath.resolve(string[0]).toFile();
            if (file.exists()) {
                if (file.isDirectory()) {
                    System.out.println("No such file!");
                } else {
                    Scanner input = null;
                    try {
                        input = new Scanner((file));
                    } catch (FileNotFoundException e) {
                        System.out.println("Error");
                        return;

                    }
                    while (input.hasNextLine()) {
                        System.out.println(input.nextLine());
                    }
                }
            } else {
                System.out.println("No such file!");
            }
        } else if (string.length == 2) {
            File firstFile = currentPath.resolve(string[0]).toFile();
            File secondFile = currentPath.resolve(string[1]).toFile();
            if (!(firstFile.exists() && secondFile.exists())) {
                System.out.println("No such file(s)!");
            } else {
                Scanner input = null;
                try {
                    input = new Scanner((firstFile));
                } catch (FileNotFoundException e) {
                    System.out.println("Error");
                    return;

                }
                while (input.hasNextLine()) {
                    System.out.println(input.nextLine());
                }

                try {
                    input = new Scanner((secondFile));
                } catch (FileNotFoundException e) {
                    System.out.println("Error");
                    return;

                }
                while (input.hasNextLine()) {
                    System.out.println(input.nextLine());
                }
            }
        } else {
            System.out.println(": No such file(s)!");
        }
    }

    public void rmdir(String[] string) {
        File[] files;
        if (string[0].equals("*")) {
            File file = currentPath.toFile();
            files = file.listFiles();
            for (File path : files) {
                if (path.list() != null && path.list().length == 0) {
                    path.delete();
                }
            }
        } else {
            File file = currentPath.resolve(string[0]).toFile();
            if (file.exists()) {
                files = file.listFiles();
                if (file.list() != null && file.list().length == 0) {
                    file.delete();
                } else {
                    System.out.println("Error! Selected directory is not empty");
                }
            } else {
                System.out.println("No such directory!");
            }
        }
    }

    public void touch(String[] string) {
        try {
            currentPath.resolve(string[0]).toFile().createNewFile();
        } catch (IOException e) {
            System.out.println("Error while creating");
        }
    }

    public void cp(String[] string) {
        File firstFile = currentPath.resolve(string[0]).toFile();
        File secondFile = currentPath.resolve(string[1]).toFile();
        if (firstFile.isDirectory() && secondFile.isDirectory()) {
            System.out.println("No such files!");
        } else {
            if (firstFile.exists() ) {
                try {
                    Scanner reader = new Scanner(firstFile);
                    FileWriter writer = null;
                    writer = new FileWriter(secondFile);
                    while (reader.hasNextLine()) {
                        writer.write(reader.nextLine());
                        writer.write("\n");
                    }
                    reader.close();
                    writer.close();
                } catch (IOException e) {
                    System.out.println("Error while copying");

                }
            } else {
                System.out.println("No such files!");
            }
        }
    }

    public void cp_r(String[] string) {
        Path firstDir = currentPath.resolve(string[1]);
        Path secondDir = currentPath.resolve(string[2]).resolve(firstDir.getFileName());

        try (Stream<Path> stream = Files.walk(firstDir)) {
            stream.forEachOrdered(sourcePath -> {
                try {
                    Files.copy(sourcePath, secondDir.resolve(firstDir.relativize(sourcePath)));
                } catch (IOException e) {
                    System.out.println("Error while copying");
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //This method will choose the suitable command method to be called
    public void chooseCommandAction() {
        String userInput = inputScanner.nextLine();
        parser.parse(userInput);
        switch (parser.commandName) {
            case "echo":
                echo(parser.args);
                break;
            case "pwd":
                pwd();
                break;
            case "cd":
                cd(parser.args);
                break;
            case "exit":
                shouldExit = true;
                break;
            case "ls":
                if (parser.args.length == 1 && parser.args[0].equals("-r")) {
                    ls(true);
                } else {
                    ls(false);
                }
                break;
            case "mkdir":
                mkdir(parser.args);
                break;
            case "rmdir":
                rmdir(parser.args);
                break;
            case "touch":
                touch(parser.args);
                break;
            case "cp":
                if (parser.args.length == 3 && parser.args[0].equals("-r")) {
                    cp_r(parser.args);
                } else {
                    cp(parser.args);
                }
                break;
            case "rm":
                rm(parser.args);
                break;
            case "cat":
                cat(parser.args);
                break;
            default:
                printErrorMessage();
                break;
        }
    }

    public Terminal() {
        inputScanner = new Scanner(System.in);
        parser = new Parser();
        currentPath = Paths.get("").toAbsolutePath();

        // Indicator that it's running
        System.out.println("Starting Terminal ... Ready.");
    }

    public void Run() {
        while (!shouldExit) {
            chooseCommandAction();
        }
    }

    public static void main(String[] args) {
        Terminal terminal = new Terminal();
        terminal.Run();
    }
}
