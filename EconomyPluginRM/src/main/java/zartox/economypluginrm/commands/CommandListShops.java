package zartox.economypluginrm.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import zartox.economypluginrm.EconomyPluginRM;
import zartox.economypluginrm.PhysicalShop;

public class CommandListShops implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;

            if(!player.isOp()){
                player.sendMessage(ChatColor.RED + "Admin reserved command !");
                return false;
            }

            player.sendMessage(ChatColor.GREEN + "Shop List:");
            player.sendMessage(ChatColor.BLUE + "-----------------------------------------------------");

            for(PhysicalShop shop : EconomyPluginRM.physicalShops){
                player.sendMessage(ChatColor.GREEN + "Id: " + EconomyPluginRM.physicalShops.indexOf(shop));
                player.sendMessage(ChatColor.GREEN + "Owner: " + shop.OwnerPlayerData.Name);
                player.sendMessage(ChatColor.GREEN + "Material: " + shop.ShopMaterial);
                player.sendMessage(ChatColor.GREEN + "Position: X:" + shop.ShopChest.getX() + " Y:" + shop.ShopChest.getY() + " Z:" + shop.ShopChest.getZ());
                player.sendMessage(ChatColor.GREEN + "Buy per click: " + shop.BuyPerClick);
                player.sendMessage(ChatColor.GREEN + "Price: " + shop.Price + "$");
                player.sendMessage(ChatColor.GREEN + "Stock: " + shop.StockAmount);
                player.sendMessage(ChatColor.BLUE + "-----------------------------------------------------");
            }
            return true;
        }
        else{
            System.out.println("Shop List:");
            System.out.println("-----------------------------------------------------");

            for(PhysicalShop shop : EconomyPluginRM.physicalShops){
                System.out.println("Id: " + EconomyPluginRM.physicalShops.indexOf(shop));
                System.out.println("Owner: " + shop.OwnerPlayerData.Name);
                System.out.println("Material: " + shop.ShopMaterial);
                System.out.println("Position: X:" + shop.ShopChest.getX() + " Y:" + shop.ShopChest.getY() + " Z:" + shop.ShopChest.getZ());
                System.out.println("Buy per click: " + shop.BuyPerClick);
                System.out.println("Price: " + shop.Price + "$");
                System.out.println("Stock: " + shop.StockAmount);
                System.out.println("-----------------------------------------------------");
            }
            return true;
        }
    }
}
