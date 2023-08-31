package zartox.economypluginrm.commands;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import zartox.economypluginrm.EconomyPluginRM;
import zartox.economypluginrm.PhysicalShop;
import zartox.economypluginrm.PrivateChest;

public class CommandDestroy implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)){
            System.out.println("Player reserved command !");
            return false;
        }

        Player player = (Player) sender;

        if(!player.isOp()){
            player.sendMessage(ChatColor.RED + "Admin reserved command !");
            return false;
        }

        Block block = player.getTargetBlockExact(5);
        player.sendMessage("" + block.getType());

        if(block == null || (!EconomyPluginRM.IsPrivateChest(block) && !EconomyPluginRM.IsPhysicalShop(block))){
            player.sendMessage(ChatColor.RED + "You need to be against a shop or private chest and aim at it for this command to work...");
            return false;
        }

        PhysicalShop shop = EconomyPluginRM.GetPhysicalShopByLocation(block.getLocation());
        PrivateChest chest = EconomyPluginRM.GetPrivateChestByLocation(block.getLocation());

        if(shop != null){
            player.sendMessage(ChatColor.GREEN + "Successfully destroyed a shop !");
            shop.ShopSign.getBlock().breakNaturally();
            shop.ShopChest.getBlock().breakNaturally();
            EconomyPluginRM.SendMessageToOnlinePlayer(shop.OwnerPlayerData.GetPlayer(), ChatColor.YELLOW + "An admin destroyed your " + shop.ShopMaterial + " shop !");
            EconomyPluginRM.physicalShops.remove(shop);
            return true;
        }

        if(chest != null){
            player.sendMessage(ChatColor.GREEN + "Successfully destroyed " + chest.OwnerName + "'s chest !");
            chest.GetChestLocation().getBlock().breakNaturally();
            chest.GetSignLocation().getBlock().breakNaturally();
            EconomyPluginRM.SendMessageToOnlinePlayer(chest.OwnerName, ChatColor.YELLOW + "An admin destroyed your private chest ! (at X:" + chest.GetSignLocation().getX() + " Y:" + chest.GetSignLocation().getY() + " Z:" + chest.GetSignLocation().getZ() + ")");
            EconomyPluginRM.privateChests.remove(chest);
            return true;
        }

        return false;
    }
}
