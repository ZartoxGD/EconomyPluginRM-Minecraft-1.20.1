package zartox.economypluginrm;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class EconomySettings {

    public boolean enableMarket = true;
    public int diamondPrice = 500;

    public void Save() throws IOException {
        System.out.println("Saving Settings...");
        File file = new File("EconomySettings.json");
        file.createNewFile();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        FileWriter fileWriter = new FileWriter("EconomySettings.json");

        gson.toJson(this, fileWriter);
        fileWriter.close();
        System.out.println("Saved Settings !");
    }

    public void Load() throws IOException{
        System.out.println("Loading Settings...");
        File file = new File("EconomySettings.json");

        if(file.exists()){
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            FileReader fileReader = new FileReader("EconomySettings.json");
            EconomySettings settings = gson.fromJson(fileReader, EconomySettings.class);
            enableMarket = settings.enableMarket;
            diamondPrice = settings.diamondPrice;
            fileReader.close();
        }
        System.out.println("Loaded Settings !");
    }

}
