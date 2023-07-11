package zartox.economypluginrm.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import zartox.economypluginrm.EconomyLogger;

import java.io.IOException;

public class CommandSaveLogs implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            if(player.isOp()){
                try {
                    EconomyLogger.SaveLogs();
                    player.sendMessage(ChatColor.GREEN + "Saved Logs !");
                    return true;
                } catch (IOException e) {
                    player.sendMessage(ChatColor.RED + "Something went wrong while saving logs... :(");
                    return false;
                }
            }
            else{
                player.sendMessage(ChatColor.RED + "You need to be a server operator to run this command !");
                return false;
            }
        }
        else{
            try {
                EconomyLogger.SaveLogs();
            } catch (IOException e) {
                System.out.println("Something went wrong while saving logs... :(");
                return false;
            }
        }
        return true;
    }
}
