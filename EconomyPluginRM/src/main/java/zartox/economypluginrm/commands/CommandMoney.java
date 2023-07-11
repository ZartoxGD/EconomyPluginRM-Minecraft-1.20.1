package zartox.economypluginrm.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import zartox.economypluginrm.EconomyPluginRM;
import zartox.economypluginrm.PlayerData;

public class CommandMoney implements CommandExecutor {

    // This method is called, when somebody uses our command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player targetPlayer = Bukkit.getPlayerExact(args[0]);
        int amount = Integer.parseInt(args[1]);

        if(!(sender instanceof Player)){
            if(args.length == 2){
                PlayerData playerData = EconomyPluginRM.GetPlayerData(targetPlayer);
                playerData.MoneyAmount += amount;
                System.out.println("Added " + amount + "$ to " + targetPlayer.getName() + " Money= " + playerData.MoneyAmount + "$");
            }
            else{
                System.out.println("Syntax error. Try: /addmoney Player Amount");
                return false;
            }
        }
        else{
            Player playerSender = (Player) sender;
            if(playerSender.isOp()){
                if(args.length == 2){
                    PlayerData playerData = EconomyPluginRM.GetPlayerData(targetPlayer);

                    playerData.MoneyAmount += amount;

                    if(amount < 0){
                        playerSender.sendMessage(ChatColor.GREEN + "Removed " + amount + "$ from " + targetPlayer.getName());
                        System.out.println(playerSender.getName() + " Removed " + amount + "$ from " + targetPlayer.getName() + " Money= " + playerData.MoneyAmount + "$");
                    }
                    else{
                        System.out.println(playerSender.getName() + " Added " + amount + "$ to " + targetPlayer.getName() + " Money= " + playerData.MoneyAmount + "$");
                        playerSender.sendMessage(ChatColor.GREEN + "Added " + amount + "$ to " + targetPlayer.getName());
                    }

                    playerData.SetMoneyGreaterZero();
                }
                else{
                    System.out.println("Syntax error. Try: /addmoney Player Amount");
                    return false;
                }
            }
            else{
                playerSender.sendMessage(ChatColor.RED + "Only an admin can use this command");
            }
        }

        return true;
    }

}
