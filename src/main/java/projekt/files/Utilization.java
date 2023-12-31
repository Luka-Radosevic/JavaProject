package projekt.files;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Utilization {

    public static <T> List<T> getDesirialization(List<T> list, String folderName) throws IOException, ClassNotFoundException{
        Path path = Paths.get(folderName);
        if(Files.exists(path)){
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(folderName));

            try{
                list.addAll((List<T>) in.readObject());

            }catch(EOFException ex){
                System.err.println(ex);
            }

            in.close();
            System.out.println("Deserijalizacija obavljena!");
        }
        else{
            System.out.println("Datoteka ne postoji!");
        }

        return list;
    }

    public static <T> void getSerialization(List<T> list, String folderName) throws IOException{
        try{
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(folderName));
            out.writeObject(list);

        }catch(IOException ex){
            System.err.println(ex);
        }
    }
}
