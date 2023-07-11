package zartox.economypluginrm.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import zartox.economypluginrm.EconomyPluginRM;
import zartox.economypluginrm.Offer;
import zartox.economypluginrm.Shop;

public class CommandMarket implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player playerSender = (Player) sender;

            if(!EconomyPluginRM.settings.enableMarket){
                playerSender.sendMessage(ChatColor.RED + "You need need to enable the market to use this command...");
                playerSender.sendMessage(ChatColor.YELLOW + "Try: /enablemarket true");
                return false;
            }

            if(Shop.offers.size() == 0){
                playerSender.sendMessage(ChatColor.YELLOW + "There is no offers available right now...");
                return true;
            }

            for(int i = 0; i < Shop.offers.size(); i++){
                Offer offer = Shop.offers.get(i);
                if(offer.IsAnon){
                    playerSender.sendMessage(ChatColor.GREEN + String.format("[%d] %d %s: %d$ (%d$/unit) from anonymous", offer.Id, offer.Items.getAmount(), offer.Name, offer.Price, offer.GetPricePerUnit()));
                }
                else{
                    playerSender.sendMessage(ChatColor.GREEN + String.format("[%d] %d %s: %d$ (%d$/unit) from %s", offer.Id, offer.Items.getAmount(), offer.Name, offer.Price, offer.GetPricePerUnit(), offer.Owner.Name));
                }
            }
        }
        else{
            if(!EconomyPluginRM.settings.enableMarket){
                System.out.println("You need need to enable the market to use this command...");
                System.out.println("Try: /enablemarket true");
                return false;
            }

            if(Shop.offers.size() == 0){
                System.out.println("There is no offers available right now...");
                return true;
            }

            for(int i = 0; i < Shop.offers.size(); i++){
                Offer offer = Shop.offers.get(i);
                if(offer.IsAnon){
                    System.out.printf("[%d] %d %s: %d$ (%d$/unit) from anonymous%n", offer.Id, offer.Items.getAmount(), offer.Name, offer.Price, offer.GetPricePerUnit());
                }
                else{
                    System.out.printf("[%d] %d %s: %d$ (%d$/unit) from %s%n", offer.Id, offer.Items.getAmount(), offer.Name, offer.Price, offer.GetPricePerUnit(), offer.Owner.Name);
                }
            }
        }
        return true;
    }
}
