package com.company;

public class Terminal {

    Parser parser;
    static boolean shouldExit = false;
    //public String pwd(){...}
    //public void cd(String[] args){...}

    //This method will choose the suitable command method to be called
    public static void chooseCommandAction()
    {

    }

    public static void startTerminal()
    {
        while(!shouldExit)
        {
            chooseCommandAction();
        }
    }

    public static void main(String[] args)
    {
        startTerminal();
    }
}
