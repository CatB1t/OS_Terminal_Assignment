package com.company;
import java.nio.file.InvalidPathException;
import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.Scanner;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Terminal {

    Scanner inputScanner;
    Parser parser;
    boolean shouldExit = false;
    Path currentPath;

    public void showErrorMessage() {
        System.out.println("Error: Command not found or invalid parameters are entered!");
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

    public void echo(String[] args)
    {
        if(args.length == 0) {
            showErrorMessage();
            return;
        }

        String stringToPrint = "";
        for(String arg : args)
            stringToPrint += arg + " ";
        System.out.println(stringToPrint);
    }

    public void pwd()
    {
        System.out.println(currentPath.toAbsolutePath().toString());
    }

    public void cd(String[] args)
    {
        if(args.length == 0) // Case no args
            currentPath = Paths.get(System.getProperty("user.home"));
        else if(args.length == 1)
        {
            String arg = args[0];
            if(arg.equals("..")) {
                currentPath = currentPath.getParent().toAbsolutePath();
            }
            else
            {
                if(isValidPath(arg))
                {
                    currentPath = Paths.get(arg);
                }
                else {
                    System.out.println("Not a valid path.");
                    return;
                }
            }
        }
        else {
            showErrorMessage();
            return;
        }

        System.out.println("Current Path: " + currentPath.toString());
    }

    //This method will choose the suitable command method to be called
    public void chooseCommandAction()
    {
        String userInput = inputScanner.nextLine();
        parser.parse(userInput);
        switch(parser.commandName)
        {
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
            default:
                showErrorMessage();
                break;
        }
    }

    public Terminal()
    {
        inputScanner = new Scanner(System.in);
        parser = new Parser();
        currentPath = Paths.get("").toAbsolutePath();

        // Indicator that it's running
        System.out.println("Starting Terminal ... Ready.");
    }

    public void Run()
    {
        while(!shouldExit)
        {
            chooseCommandAction();
        }
    }

    public static void main(String[] args)
    {
        Terminal terminal = new Terminal();
        terminal.Run();
    }
}
