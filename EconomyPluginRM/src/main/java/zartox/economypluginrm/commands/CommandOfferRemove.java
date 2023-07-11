package zartox.economypluginrm.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import zartox.economypluginrm.EconomyPluginRM;
import zartox.economypluginrm.Offer;
import zartox.economypluginrm.PlayerData;
import zartox.economypluginrm.Shop;

public class CommandOfferRemove implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player playerSender = (Player) sender;
        PlayerData playerData = EconomyPluginRM.GetPlayerData(playerSender);

        if(!EconomyPluginRM.settings.enableMarket){
            playerSender.sendMessage(ChatColor.RED + "You need need to enable the market to use this command...");
            playerSender.sendMessage(ChatColor.YELLOW + "Try: /enablemarket true");
            return false;
        }

        if(!(sender instanceof Player)){
            System.out.println("Command reserved to players !");
            return false;
        }

        Offer offer = new Offer();

        try{
            offer = Shop.offers.get(Integer.parseInt(args[0]));
        }
        catch(Exception e){
            playerSender.sendMessage(ChatColor.YELLOW + "You don't have any offer with this id");
            return false;
        }

        if(!(offer.Owner == playerData)){
            playerSender.sendMessage(ChatColor.YELLOW + "You can't cancel the offers made by other players !");
            return false;
        }

        playerSender.getInventory().addItem(offer.Items);
        Shop.RemoveOffer(offer);
        playerSender.sendMessage(ChatColor.GREEN + "Offer removed !");

        return true;
    }
}
