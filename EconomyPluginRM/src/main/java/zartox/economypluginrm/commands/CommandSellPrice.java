package zartox.economypluginrm.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import zartox.economypluginrm.EconomyPluginRM;

public class CommandSellPrice implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            if(!player.isOp()){
                player.sendMessage(ChatColor.RED + "You are not OP...");
                return false;
            }
            else{
                if(args.length < 1){
                    player.sendMessage(ChatColor.RED + "You need to pass in the new price");
                    return false;
                }
                else{
                    EconomyPluginRM.settings.diamondPrice = Integer.parseInt(args[0]);
                    player.sendMessage(ChatColor.GREEN + "Diamond price set! (" + EconomyPluginRM.settings.diamondPrice + "$)");
                    return true;
                }
            }
        }
        else{
            System.out.println("Only op player can use this command");
            return false;
        }
    }
}
