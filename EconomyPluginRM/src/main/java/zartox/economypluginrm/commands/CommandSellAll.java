package zartox.economypluginrm.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import zartox.economypluginrm.EconomyPluginRM;
import zartox.economypluginrm.PlayerData;

public class CommandSellAll implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)){
            System.out.println("You cannot use this command from the server.");
            return false;
        }

        Player playerSender = (Player) sender;
        PlayerData senderData = EconomyPluginRM.GetPlayerData(playerSender);
        Inventory inventory = playerSender.getInventory();
        ItemStack[] contents = inventory.getContents();
        int diamondCount = 0;

        // Count the number of diamonds in the player's inventory
        for (ItemStack itemStack : contents) {
            if (itemStack != null && itemStack.getType() == Material.DIAMOND) {
                diamondCount += itemStack.getAmount();
            }
        }

        senderData.SellDiams(diamondCount);

        // Delete the diamonds from the player's inventory
        for (int i = 0; i < contents.length; i++) {
            ItemStack itemStack = contents[i];
            if (itemStack != null && itemStack.getType() == Material.DIAMOND) {
                contents[i] = null;
            }
        }

        inventory.setContents(contents);
        return true;
    }

    private void countAndDeleteDiamonds(Player player) {



    }
}
