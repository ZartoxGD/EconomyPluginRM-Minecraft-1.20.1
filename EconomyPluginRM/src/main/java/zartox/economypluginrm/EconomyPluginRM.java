package zartox.economypluginrm;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import zartox.economypluginrm.commands.*;
import zartox.economypluginrm.serializableObjects.SerializablePhysicalShop;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public final class EconomyPluginRM extends JavaPlugin {

    public static ArrayList<PlayerData> playersData = new ArrayList<>();
    public static ArrayList<PhysicalShop> physicalShops = new ArrayList<>();
    public static ArrayList<PrivateChest> privateChests = new ArrayList<>();
    public static EconomySettings settings = new EconomySettings();
    public static EconomyPluginRM instance;

    public static void SendMessageToOnlinePlayer(String playerName, String message){
        Player player = Bukkit.getPlayerExact(playerName);

        if(player != null && player.isOnline()){
            player.sendMessage(message);
        }
    }

    public static void SendMessageToOnlinePlayer(Player player, String message){
        if(player != null && player.isOnline()){
            player.sendMessage(message);
        }
    }

    public static boolean IsPhysicalShop(Block block){
        for(PhysicalShop shop : physicalShops){
            if(block.getLocation().getX() == shop.ShopSign.getLocation().getX() && block.getLocation().getY() == shop.ShopSign.getLocation().getY() && block.getLocation().getZ() == shop.ShopSign.getLocation().getZ()){
                return true;
            }

            if(block.getLocation().getX() == shop.ShopChest.getLocation().getX() && block.getLocation().getY() == shop.ShopChest.getLocation().getY() && block.getLocation().getZ() == shop.ShopChest.getLocation().getZ()){
                return true;
            }
        }
        return false;
    }

    public static boolean IsPrivateChest(Block block){
        for(PrivateChest chest : privateChests){
            if(block.getLocation().equals(chest.GetChestLocation())){
                return true;
            }

            if(block.getLocation().equals(chest.GetSignLocation())){
                return true;
            }
        }
        return false;
    }

    public static PrivateChest GetPrivateChestByLocation(Location location){
        for(PrivateChest chest : privateChests){
            if(location.equals(chest.GetChestLocation()) || location.equals(chest.GetSignLocation())){
                return chest;
            }
        }
        return null;
    }

    public static PhysicalShop GetPhysicalShopByLocation(Location location){
        for(PhysicalShop shop : physicalShops){
            if((location.getX() == shop.ShopSign.getLocation().getX() && location.getY() == shop.ShopSign.getLocation().getY() && location.getZ() == shop.ShopSign.getLocation().getZ()) || (location.getX() == shop.ShopChest.getLocation().getX() && location.getY() == shop.ShopChest.getLocation().getY() && location.getZ() == shop.ShopChest.getLocation().getZ())){
                return shop;
            }
        }
        return null;
    }

    public static boolean IsPhysicalShopOwner(Player player, PhysicalShop shop){
        PlayerData playerData = GetPlayerData(player);
        PlayerData ownerPlayerData = shop.OwnerPlayerData;

        return playerData == ownerPlayerData; //PAS CERTAINS QUE CA MARCHE
    }

    public void SavePhysicalShops() throws IOException{
        System.out.println("Saving Physical shops...");
        File file = new File("physicalShops.json");
        file.createNewFile();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        FileWriter fileWriter = new FileWriter("physicalShops.json");

        ArrayList<SerializablePhysicalShop> serShopsArray = new ArrayList<>();

        for(PhysicalShop shop : physicalShops){
            SerializablePhysicalShop ser = shop.GetSerializable();
            serShopsArray.add(ser);
        }

        gson.toJson(serShopsArray, fileWriter);
        fileWriter.close();
        System.out.println("Saved Physical shops !");
    }

    public void SavePrivateChests() throws IOException{
        System.out.println("Saving Private chests...");
        File file = new File("privateChests.json");
        file.createNewFile();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        FileWriter fileWriter = new FileWriter("privateChests.json");

        ArrayList<PrivateChest> privateChestsArray = new ArrayList<>();

        privateChestsArray.addAll(privateChests);

        gson.toJson(privateChestsArray, fileWriter);
        fileWriter.close();
        System.out.println("Saved Private chests !");
    }

    public void LoadPhysicalShops() throws IOException{
        System.out.println("Loading Physical shops...");
        File file = new File("physicalShops.json");

        if(file.exists()){
            Gson gson = new Gson();
            FileReader fileReader = new FileReader("physicalShops.json");
            SerializablePhysicalShop[] physicalShopsArray = gson.fromJson(fileReader, SerializablePhysicalShop[].class);
            fileReader.close();

            ArrayList<PhysicalShop> loadedPhysicalShops = new ArrayList<>();

            for(SerializablePhysicalShop ser : physicalShopsArray){
                PhysicalShop shop = ser.GetNonSerializable();
                loadedPhysicalShops.add(shop);
            }

            physicalShops.clear();
            physicalShops.addAll(loadedPhysicalShops);
        }

        System.out.println("Loaded Physical shops !");
    }

    public void LoadPrivateChests() throws IOException{
        System.out.println("Loading Private chests...");
        File file = new File("physicalShops.json");

        if(file.exists()){
            Gson gson = new Gson();
            FileReader fileReader = new FileReader("privateChests.json");
            PrivateChest[] privateChestsArray = gson.fromJson(fileReader, PrivateChest[].class);
            fileReader.close();

            privateChests.clear();
            Collections.addAll(privateChests, privateChestsArray);
        }

        System.out.println("Loaded Private chests !");
    }

    public static PlayerData GetPlayerData(Player player){
        for (PlayerData playerData : playersData) {
            if (playerData.Name.equals(player.getName())) {
                return playerData;
            }
        }
        return null;
    }

    public static PlayerData GetPlayerDataByName(String name){
        for (PlayerData playerData : playersData) {
            if (playerData.Name.equals(name)) {
                return playerData;
            }
        }
        return null;
    }

    public static boolean PlayerDataExist(Player player) {
        for (PlayerData playerData : playersData) {
            if (playerData.Name.equals(player.getName())) {
                return true;
            }
        }
        return false;
    }

    private void LoadPlayerData() throws IOException {
        System.out.println("Loading playersData...");
        File file = new File("playersData.json");

        if(file.exists()){
            Gson gson = new Gson();
            FileReader fileReader = new FileReader("playersData.json");
            PlayerData[] playerDataArray = gson.fromJson(fileReader, PlayerData[].class);
            fileReader.close();

            if (playerDataArray != null) {
                playersData.clear();
                playersData.addAll(Arrays.asList(playerDataArray));
            }
        }

        System.out.println("Loaded playersData !");
    }

    private void SavePlayerData() throws IOException {
        System.out.println("Saving playersData...");
        File file = new File("playersData.json");
        file.createNewFile();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        FileWriter fileWriter = new FileWriter("playersData.json");
        gson.toJson(playersData, fileWriter);
        fileWriter.close();
        System.out.println("Saved playersData !");
    }

    @Override
    public void onEnable() {
        instance = this;
        registerEvents();
        registerCommands();

        try {
            LoadPlayerData();
            Shop.Load();
            settings.Load();
            LoadPhysicalShops();
            LoadPrivateChests();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("EconomyPluginRM started !");
    }

    private void registerCommands(){
        getCommand("money").setExecutor(new CommandMoney());
        getCommand("pay").setExecutor(new CommandPay());
        getCommand("payanon").setExecutor(new CommandPayAnon());
        getCommand("checkmoney").setExecutor(new CommandCheckMoney());

        getCommand("offeradd").setExecutor(new CommandOfferAdd());
        getCommand("offerremove").setExecutor(new CommandOfferRemove());
        getCommand("offershowmine").setExecutor(new CommandOfferShowMine());
        getCommand("offerbuy").setExecutor(new CommandOfferBuy());
        getCommand("market").setExecutor(new CommandMarket());
        getCommand("enablemarket").setExecutor(new CommandEnableMarket());

        getCommand("sell").setExecutor(new CommandSell());
        getCommand("sellall").setExecutor(new CommandSellAll());
        getCommand("sellprice").setExecutor(new CommandSellPrice());

        getCommand("savelogs").setExecutor(new CommandSaveLogs());
    }

    private void registerEvents(){
        PluginManager pm = Bukkit.getServer().getPluginManager();
        pm.registerEvents(new EventsListener(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        try {
            Shop.Save();
            SavePlayerData();
            settings.Save();
            SavePhysicalShops();
            SavePrivateChests();
            EconomyLogger.SaveLogs();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("EconomyPluginRM stopped !");
    }

}
