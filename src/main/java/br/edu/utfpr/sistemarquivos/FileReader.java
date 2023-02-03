package br.edu.utfpr.sistemarquivos;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileReader {

    public void read(Path path) {
        // TODO implementar a leitura dos arquivos do PATH aqui

        try{
            Files.readAllLines(path).forEach(System.out::println);
        }catch(IOException e) {
            e.printStackTrace();
        }
    }
}
