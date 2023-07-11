package zartox.economypluginrm.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import zartox.economypluginrm.EconomyPluginRM;

public class CommandEnableMarket implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player){
            Player playerSender = (Player) sender;

            if(!(args.length < 1)){
                boolean isEnabled = Boolean.parseBoolean(args[0]);
                EconomyPluginRM.settings.enableMarket = isEnabled;

                if(isEnabled){
                    playerSender.sendMessage(ChatColor.GREEN + "Market enabled !");
                }
                else{
                    playerSender.sendMessage(ChatColor.GREEN + "Market disabled !");
                }
            }
            else{
                playerSender.sendMessage(ChatColor.RED + "Wrong args ! Try: /enablemarket true/false");
                return false;
            }
        }
        else{
            if(!(args.length < 1)){
                boolean isEnabled = Boolean.parseBoolean(args[0]);
                EconomyPluginRM.settings.enableMarket = isEnabled;

                if(isEnabled){
                    System.out.println("Market enabled !");
                }
                else{
                    System.out.println("Market disabled !");
                }
            }
            else{
                System.out.println("Wrong args ! Try: /enablemarket true/false");
                return false;
            }
        }

        return true;
    }
}
