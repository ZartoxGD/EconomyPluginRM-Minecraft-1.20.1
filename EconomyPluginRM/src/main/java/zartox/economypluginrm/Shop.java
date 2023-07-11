package zartox.economypluginrm;

import com.google.gson.*;
import zartox.economypluginrm.serializableObjects.SerializableOffer;

import java.io.*;
import java.util.ArrayList;

public class Shop {

    public static ArrayList<Offer> offers = new ArrayList<>();

    public static void AddOffer(Offer offer){
        offers.add(offer);
    }

    public static void RemoveOffer(Offer offer){
        offers.remove(offer);
    }

    public static boolean DoesPlayerHaveAnOffer(PlayerData playerData){
        for (Offer offer : offers) {
            if (offer.Owner == playerData) {
                return true;
            }
        }
        return false;
    }

    public static void Save() throws IOException {
        System.out.println("Saving Shop...");
        File file = new File("shop.json");
        file.createNewFile();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        FileWriter fileWriter = new FileWriter("shop.json");

        ArrayList<SerializableOffer> serializableOfferArrayList = new ArrayList<>();

        for (Offer offer : offers) {
            SerializableOffer serializableOffer = new SerializableOffer(offer);
            serializableOfferArrayList.add(serializableOffer);
        }

        gson.toJson(serializableOfferArrayList, fileWriter);
        fileWriter.close();
        System.out.println("Saved Shop !");
    }

    public static void Load() throws IOException{
        System.out.println("Loading Shop...");
        File file = new File("shop.json");

        if(file.exists()){
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            FileReader fileReader = new FileReader("shop.json");
            SerializableOffer[] serializableOffers = gson.fromJson(fileReader, SerializableOffer[].class);
            fileReader.close();

            for (SerializableOffer serializableOffer : serializableOffers) {
                Offer offer = new Offer(serializableOffer);
                offers.add(offer);
            }
        }

        System.out.println("Loaded Shop !");
    }
}
