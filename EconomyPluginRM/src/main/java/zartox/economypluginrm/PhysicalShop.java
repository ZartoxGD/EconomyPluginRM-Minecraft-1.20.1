package zartox.economypluginrm;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;
import org.checkerframework.checker.units.qual.C;
import zartox.economypluginrm.serializableObjects.SerializablePhysicalShop;

public class PhysicalShop {

    public Material ShopMaterial;
    public int BuyPerClick;
    public int Price;
    public int StockAmount;
    public Chest ShopChest;
    public Sign ShopSign;
    public PlayerData OwnerPlayerData;

    public PhysicalShop(Material material, int buyPerClick, int price, int stockAmount, Chest chest, Sign sign, PlayerData playerData){
        ShopMaterial = material;
        BuyPerClick = buyPerClick;
        Price = price;
        StockAmount = stockAmount;
        ShopChest = chest;
        ShopSign = sign;
        OwnerPlayerData = playerData;
    }

    public PhysicalShop(SerializablePhysicalShop serializablePhysicalShop){
        ShopMaterial = serializablePhysicalShop.ShopMaterial;
        BuyPerClick = serializablePhysicalShop.BuyPerClick;
        Price = serializablePhysicalShop.Price;
        StockAmount = serializablePhysicalShop.StockAmount;
        OwnerPlayerData = EconomyPluginRM.GetPlayerDataByName(serializablePhysicalShop.OwnerName);

        World world = Bukkit.getWorlds().get(0);
        Location chestLocation = new Location(world, serializablePhysicalShop.ChestPosition[0], serializablePhysicalShop.ChestPosition[1], serializablePhysicalShop.ChestPosition[2]);
        Location signLocation = new Location(world, serializablePhysicalShop.SignPosition[0], serializablePhysicalShop.SignPosition[1], serializablePhysicalShop.SignPosition[2]);

        ShopChest = (Chest) chestLocation.getBlock().getState();
        ShopSign = (Sign) signLocation.getBlock().getState();
    }

    public void BuyAtShop(PlayerData playerData){
        Player buyerPlayer = Bukkit.getPlayerExact(playerData.Name);

        if(playerData.MoneyAmount < Price){
            buyerPlayer.sendMessage(ChatColor.RED + "You don't have enough money " + playerData.MoneyAmount + "$/" + Price + "$");
            return;
        }

        if(StockAmount < BuyPerClick){
            EconomyPluginRM.SendMessageToOnlinePlayer(OwnerPlayerData.Name, ChatColor.YELLOW + playerData.Name + " tried to buy to your " + ShopMaterial + " shop but it's out of stock");
            //Bukkit.getPlayerExact(OwnerPlayerData.Name).sendMessage(ChatColor.YELLOW + playerData.Name + " tried to buy to your " + ShopMaterial + " shop but it's out of stock");
            buyerPlayer.sendMessage(ChatColor.YELLOW + "This shop is out of stock");
            return;
        }

        playerData.MoneyAmount -= Price;
        ItemStack items = new ItemStack(ShopMaterial, BuyPerClick);
        buyerPlayer.getInventory().addItem(items);

        RemoveItemsFromChest();

        StockAmount -= BuyPerClick;
        OwnerPlayerData.MoneyAmount += Price;

        buyerPlayer.sendMessage(ChatColor.GREEN + "You just bought " + BuyPerClick + " " + ShopMaterial + " from " + OwnerPlayerData.Name + " for " + Price + "$ !");
        EconomyPluginRM.SendMessageToOnlinePlayer(OwnerPlayerData.Name, ChatColor.GREEN + "You just sold " + BuyPerClick + " " + ShopMaterial + " to " + buyerPlayer.getName() + " for " + Price + "$ !");
        //Bukkit.getPlayerExact(OwnerPlayerData.Name).sendMessage(ChatColor.GREEN + "You just sold " + BuyPerClick + " " + ShopMaterial + " to " + buyerPlayer.getName() + " for " + Price + "$ !");
        VerifyStock();
    }

    public void VerifyStock(){
        if(StockAmount < BuyPerClick){
            EconomyPluginRM.SendMessageToOnlinePlayer(OwnerPlayerData.Name, ChatColor.YELLOW + "Your " + ShopMaterial + " shop is out of stock");
            //Bukkit.getPlayerExact(OwnerPlayerData.Name).sendMessage(ChatColor.YELLOW + "Your " + ShopMaterial + " shop is out of stock");
            SetSignTextRed();
        }
        else{
            SetSignTextGreen();
        }
    }

    public void DebugShop(){
        Player player = Bukkit.getPlayerExact(OwnerPlayerData.Name);

        player.sendMessage("Debug Mat: " + ShopMaterial);
        player.sendMessage("Debug Price: " + Price);
        player.sendMessage("Debug BuyPerClick: " + BuyPerClick);
        player.sendMessage("Debug Stock: " + StockAmount);
    }

    private void RemoveItemsFromChest(){
        Inventory chestInventory = ShopChest.getInventory();
        int buyPerClick = BuyPerClick;

        ItemStack[] contents = chestInventory.getContents();
        for (int i = 0; i < contents.length; i++) {
            ItemStack item = contents[i];
            if (item != null && item.getType() == ShopMaterial) {
                int itemAmount = item.getAmount();
                if (itemAmount <= buyPerClick) {
                    buyPerClick -= itemAmount;
                    contents[i] = null;
                } else {
                    item.setAmount(itemAmount - buyPerClick);
                    break;
                }
            }
        }
        chestInventory.setContents(contents);
    }

    public void CountStock() {
        int itemCount = 0;
        StockAmount = 0;
        for (ItemStack itemStack : ShopChest.getBlockInventory().getContents()) {
            if (itemStack != null) {
                itemCount += itemStack.getAmount();
            }
        }

        StockAmount = itemCount;

        if(StockAmount < 0){
            StockAmount = 0;
        }
        VerifyStock();
    }

    public void SetSignTextGreen() {
        SignSide side = ShopSign.getSide(Side.FRONT);

        side.setLine(0, ChatColor.WHITE + "[Shop]");
        side.setLine(1, ChatColor.WHITE + "" + ChatColor.GREEN + Price + "$" + "/" + BuyPerClick);
        side.setLine(2, ChatColor.WHITE + "" + ChatColor.GREEN + ShopMaterial);
        side.setLine(3, ChatColor.WHITE + "" + ChatColor.GREEN + "Stock: " + StockAmount);

        ShopSign.update(true);
    }

    public void SetSignTextRed() {
        SignSide side = ShopSign.getSide(Side.FRONT);

        side.setLine(0, ChatColor.WHITE + "[Shop]");
        side.setLine(1, ChatColor.WHITE + "" + ChatColor.RED + Price + "$" + "/" + BuyPerClick);
        side.setLine(2, ChatColor.WHITE + "" + ChatColor.RED + ShopMaterial);
        side.setLine(3, ChatColor.WHITE + "" + ChatColor.RED + "Stock: " + StockAmount);

        ShopSign.update(true);
    }


    public SerializablePhysicalShop GetSerializable(){
        return new SerializablePhysicalShop(this);
    }

}
