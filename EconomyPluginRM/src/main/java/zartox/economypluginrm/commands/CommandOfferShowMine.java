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

public class CommandOfferShowMine implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player playerSender = (Player) sender;
        PlayerData playerData = EconomyPluginRM.GetPlayerData(playerSender);

        if(!EconomyPluginRM.settings.enableMarket){
            playerSender.sendMessage(ChatColor.RED + "You need need to enable the market to use this command...");
            playerSender.sendMessage(ChatColor.YELLOW + "Try: /enablemarket true");
            return false;
        }

        if(sender instanceof Player){
            if(Shop.offers.size() == 0){
                playerSender.sendMessage(ChatColor.YELLOW + "There is no offers available right now...");
                return true;
            }

            if(Shop.DoesPlayerHaveAnOffer(playerData)){
                for(int i = 0; i < Shop.offers.size(); i++){
                    Offer offer = Shop.offers.get(i);
                    if(offer.Owner == playerData){
                        if(offer.IsAnon){
                            playerSender.sendMessage(ChatColor.GREEN + String.format("[%d] %d %s: %d$ (%d$/unit) from anonymous", offer.Id, offer.Items.getAmount(), offer.Name, offer.Price, offer.GetPricePerUnit()));
                        }
                        else{
                            playerSender.sendMessage(ChatColor.GREEN + String.format("[%d] %d %s: %d$ (%d$/unit) from %s", offer.Id, offer.Items.getAmount(), offer.Name, offer.Price, offer.GetPricePerUnit(), offer.Owner.Name));
                        }
                    }
                }
            }
            else{
                playerSender.sendMessage(ChatColor.YELLOW + "You don't have any offers to show");
            }
        }
        else{
            System.out.println("Command reserved to players !");
        }
        return true;
    }
}
