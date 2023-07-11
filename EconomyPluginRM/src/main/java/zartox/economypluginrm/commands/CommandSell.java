package zartox.economypluginrm.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import zartox.economypluginrm.EconomyPluginRM;
import zartox.economypluginrm.PlayerData;

public class CommandSell implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)){
            System.out.println("You cannot use this command from the server.");
            return false;
        }

        Player playerSender = (Player) sender;
        PlayerData senderData = EconomyPluginRM.GetPlayerData(playerSender);
        ItemStack itemStack = playerSender.getInventory().getItemInMainHand();

        if(itemStack.getType() != Material.DIAMOND){
            playerSender.sendMessage(ChatColor.RED + "You can't sell anything else than diamond...");
            return false;
        }

        int amount = itemStack.getAmount();
        senderData.SellDiams(amount);
        playerSender.setItemInHand(null);

        return true;
    }
}
