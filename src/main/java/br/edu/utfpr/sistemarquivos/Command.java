package br.edu.utfpr.sistemarquivos;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;

public enum Command {

    LIST() {
        @Override
        boolean accept(String command) {
            final var commands = command.split(" ");
            return commands.length > 0 && commands[0].startsWith("LIST") || commands[0].startsWith("list");
        }

        @Override
        Path execute(Path path) throws IOException {

            System.out.println("Content of " + path);

            String[] elements;
            File file = new File(path.toString());

            elements = file.list();
            for(String element: elements){
                System.out.println(element);
            }

            return path;
        }
    },
    SHOW() {
        private String[] parameters = new String[]{};

        @Override
        void setParameters(String[] parameters) {
            this.parameters = parameters;
        }

        @Override
        boolean accept(String command) {
            final var commands = command.split(" ");
            return commands.length > 0 && commands[0].startsWith("SHOW") || commands[0].startsWith("show");
        }

        @Override
        Path execute(Path path) throws IOException{

            int tam = parameters.length;
            if(tam < 2)
            {
                System.out.println("Parameter not informed");
                return path;
            }

            Path finalPath = Paths.get("" + path + File.separator + this.parameters[1]);

            if(!Files.exists(finalPath)){
                System.out.println("File do not exists");
                return path;
            }

            if (Files.isDirectory(finalPath)) {
                System.out.println("This command should be used with files only");
                return path;
            }

            String extension = Arrays.stream(parameters[1].split("\\.")).reduce((a, b) -> b).orElse(null);

            if(!extension.equals("txt")){
                System.out.println("Extension not supported");
                return path;
            }

            FileReader file = new FileReader();
            file.read(finalPath);

            return path;
        }
    },
    BACK() {
        @Override
        boolean accept(String command) {
            final var commands = command.split(" ");
            return commands.length > 0 && commands[0].startsWith("BACK") || commands[0].startsWith("back");
        }

        @Override
        Path execute(Path path) {
            String root = Application.ROOT;

            if((path.toString()).equals(root)){
                System.out.println("In the ROOT already");
                return path;
            }

            String split =  File.separator.replace("\\", "\\\\");
            String[] pathArray = (path.toString()).split(split);

            String pathFinal = "";
            for(int i = 0; i < pathArray.length - 1; i ++){
                if(i == 0){
                    pathFinal =  pathArray[i] +  File.separator ;
                }
                else{
                    pathFinal = pathFinal + pathArray[i] + File.separator;
                }
            }
            path = Paths.get(pathFinal);
            return path;
        }
    },
    OPEN() {
        private String[] parameters = new String[]{};

        @Override
        void setParameters(String[] parameters) {
            this.parameters = parameters;
        }

        @Override
        boolean accept(String command) {
            final var commands = command.split(" ");
            return commands.length > 0 && commands[0].startsWith("OPEN") || commands[0].startsWith("open");
        }

        @Override
        Path execute(Path path) {

            int tam = parameters.length;
            if(tam < 2){
                System.out.println("Parameter not informed");
                return path;
            }

            Path finalPath = Paths.get("" + path + File.separator + this.parameters[1]);

            if(!Files.exists(finalPath)){
                System.out.println("Directory do not exists");
            }
            else if(!Files.isDirectory(finalPath)){
                System.out.println("Extension not supported");
            }
            else{
                path = finalPath;
            }

            return path;
        }
    },
    DETAIL() {
        private String[] parameters = new String[]{};

        @Override
        void setParameters(String[] parameters) {
            this.parameters = parameters;
        }

        @Override
        boolean accept(String command) {
            final var commands = command.split(" ");
            return commands.length > 0 && commands[0].startsWith("DETAIL") || commands[0].startsWith("detail");
        }

        @Override
        Path execute(Path path) throws IOException {
            Path finalPath = null;
            int tam = parameters.length;

            if(tam < 2){
                System.out.println("Parameter not informed");
                return path;
            }

            finalPath = Paths.get("" + path + File.separator + this.parameters[1]);

            if(!Files.exists(finalPath)){
                System.out.println("Directory/File does not exists");
                return path;
            }

            BasicFileAttributeView view = Files.getFileAttributeView(finalPath, BasicFileAttributeView.class);
            BasicFileAttributes basicAttribs = view.readAttributes();
            System.out.println("Is directory [" + basicAttribs.isDirectory() +"]");
            System.out.println("Size [" + basicAttribs.size() +"]");
            System.out.println("Created on [" + basicAttribs.creationTime() +"]");
            System.out.println("Last access time [" + basicAttribs.lastAccessTime() +"]");

            return path;
        }
    },
    EXIT() {
        @Override
        boolean accept(String command) {
            final var commands = command.split(" ");
            return commands.length > 0 && commands[0].startsWith("EXIT") || commands[0].startsWith("exit");
        }

        @Override
        Path execute(Path path) {
            System.out.print("Saindo...");
            return path;
        }

        @Override
        boolean shouldStop() {
            return true;
        }
    };

    abstract Path execute(Path path) throws IOException;

    abstract boolean accept(String command);

    void setParameters(String[] parameters) {
    }

    boolean shouldStop() {
        return false;
    }

    public static Command parseCommand(String commandToParse) {

        if (commandToParse.isBlank()) {
            throw new UnsupportedOperationException("Type something...");
        }

        final var possibleCommands = values();

        for (Command possibleCommand : possibleCommands) {
            if (possibleCommand.accept(commandToParse)) {
                possibleCommand.setParameters(commandToParse.split(" "));
                return possibleCommand;
            }
        }

        throw new UnsupportedOperationException("Can't parse command [%s]".formatted(commandToParse));
    }
}
