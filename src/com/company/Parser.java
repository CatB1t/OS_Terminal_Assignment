package com.company;

import java.util.Arrays;

public class Parser {

        String commandName;
        String[] args;

        //This method will divide the input into commandName and args
        //where "input" is the string command entered by the user
        public boolean parse(String input)
        {
            // set the local args equal to what the user input
            String[] currentWords = input.split(" ");

            commandName = currentWords[0];
            args = Arrays.copyOfRange(currentWords, 1, currentWords.length);
            return true;
        }

        public String getCommandName()
        {
            //
            return commandName;
        }

        public String[] getArgs()
        {
            return args;
        }
}

