package br.edu.utfpr.sistemarquivos;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.SQLOutput;

public enum Command {

    LIST() {
        @Override
        boolean accept(String command) {
            final var commands = command.split(" ");
            return commands.length > 0 && commands[0].startsWith("LIST") || commands[0].startsWith("list");
        }

        @Override
        Path execute(Path path) throws IOException {

            // TODO implementar conforme enunciado
            System.out.println("Content of " + path);
            //Files.list(path).forEach(System.out::println);

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

            Path finalPath = Paths.get("" + path + File.separator + this.parameters[1]);
            boolean isDirectory = Files.isDirectory(finalPath);
            if (isDirectory) {
                System.out.println("This command should be used with files only");
            } else {
                FileReader file = new FileReader();
                file.read(finalPath);
            }

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

            // TODO implementar conforme enunciado
            String teste = File.separator;
            String[] pathArray = (path.toString()).split(teste);
            System.out.println(pathArray);
            String pathFinal = "";
            for(int i = 0; i < pathArray.length; i ++){
                if(i == pathArray.length - 1){
                    pathFinal = pathFinal + pathArray[i];
                }
                else{
                    pathFinal = pathFinal + File.separator + pathArray[i];
                }
            }
            System.out.println(pathFinal);
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

            // TODO implementar conforme enunciado
            path = Paths.get("" + path + File.separator + this.parameters[1]);

            if(!Files.isDirectory(path)){
                System.out.println("Extension not supported");
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

            // TODO implementar conforme enunciado

            if(parameters[1] != null){
                path = Paths.get("" + path + File.separator + this.parameters[1]);

            }

            BasicFileAttributeView view = Files.getFileAttributeView(path, BasicFileAttributeView.class);
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
