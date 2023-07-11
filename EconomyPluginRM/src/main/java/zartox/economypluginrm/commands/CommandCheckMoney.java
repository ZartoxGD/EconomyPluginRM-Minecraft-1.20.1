package zartox.economypluginrm.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import zartox.economypluginrm.EconomyPluginRM;
import zartox.economypluginrm.PlayerData;

public class CommandCheckMoney implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player){
            Player playerSender = (Player) sender;
            EconomyPluginRM.GetPlayerData(playerSender).CheckMoney();
            return true;
        }
        else{
            for(int i = 0; i < EconomyPluginRM.playersData.toArray().length; i++){
                PlayerData playerData = EconomyPluginRM.playersData.get(i);
                System.out.println(playerData.Name + ": " + playerData.MoneyAmount + "$");
            }
            return false;
        }
    }
}
