package zartox.economypluginrm;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

public class PrivateChest {

    public double[] ChestPosition;
    public double[] SignPosition;
    public String[] AuthorizedPlayersName;
    public String OwnerName;

    public PrivateChest(Location chestPosition, Location signPosition, String[] authorizedPlayersName, String ownerName){
        ChestPosition = new double[3];
        SignPosition = new double[3];
        AuthorizedPlayersName = new String[3];

        ChestPosition[0] = chestPosition.getX();
        ChestPosition[1] = chestPosition.getY();
        ChestPosition[2] = chestPosition.getZ();

        SignPosition[0] = signPosition.getX();
        SignPosition[1] = signPosition.getY();
        SignPosition[2] = signPosition.getZ();

        AuthorizedPlayersName = authorizedPlayersName;
        OwnerName = ownerName;
    }

    public Location GetChestLocation(){
        return new Location(Bukkit.getWorlds().get(0), ChestPosition[0], ChestPosition[1], ChestPosition[2]);
    }

    public Location GetSignLocation(){
        return new Location(Bukkit.getWorlds().get(0), SignPosition[0], SignPosition[1], SignPosition[2]);
    }

    public boolean IsPlayerAuthorized(Player player){
        String playerName = player.getName().toLowerCase();

        if(playerName.equals(OwnerName.toLowerCase())){
            return true;
        }

        for(String name : AuthorizedPlayersName){
            if(playerName.equals(name.toLowerCase())){
                return true;
            }
        }

        return false;
    }

    public Player GetPlayer(){
        return Bukkit.getPlayerExact(OwnerName);
    }

}
