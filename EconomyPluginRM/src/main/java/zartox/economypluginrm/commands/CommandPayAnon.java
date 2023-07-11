package zartox.economypluginrm.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import zartox.economypluginrm.EconomyPluginRM;
import zartox.economypluginrm.PlayerData;

public class CommandPayAnon implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)){
            System.out.println("You cannot use this command from the server.");
            return false;
        }

        Player playerSender = (Player) sender;

        if(args.length == 2){
            PlayerData senderData = EconomyPluginRM.GetPlayerData(playerSender);
            PlayerData receiverData = EconomyPluginRM.GetPlayerData(Bukkit.getPlayerExact(args[0]));
            int amount = Integer.parseInt(args[1]);
            senderData.SendMoneyAnonymous(receiverData, amount);
            return true;
        }
        else{
            playerSender.sendMessage(ChatColor.RED + "Syntax error. Try: /pay PlayerName Amount");
            return false;
        }
    }
}
