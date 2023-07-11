package zartox.economypluginrm.serializableObjects;

import org.bukkit.Material;
import zartox.economypluginrm.PhysicalShop;

public class SerializablePhysicalShop {

    public Material ShopMaterial;
    public int BuyPerClick;
    public int Price;
    public int StockAmount;
    public int[] SignPosition;
    public int[] ChestPosition;
    public String OwnerName;

    public SerializablePhysicalShop(PhysicalShop physicalShop){
        ShopMaterial = physicalShop.ShopMaterial;
        BuyPerClick = physicalShop.BuyPerClick;
        Price = physicalShop.Price;
        StockAmount = physicalShop.StockAmount;

        SignPosition = new int[3];
        SignPosition[0] = physicalShop.ShopSign.getBlock().getState().getX();
        SignPosition[1] = physicalShop.ShopSign.getBlock().getState().getY();
        SignPosition[2] = physicalShop.ShopSign.getBlock().getState().getZ();

        ChestPosition = new int[3];
        ChestPosition[0] = physicalShop.ShopChest.getBlock().getState().getX();
        ChestPosition[1] = physicalShop.ShopChest.getBlock().getState().getY();
        ChestPosition[2] = physicalShop.ShopChest.getBlock().getState().getZ();

        OwnerName = physicalShop.OwnerPlayerData.Name;
    }

    public PhysicalShop GetNonSerializable(){
        return new PhysicalShop(this);
    }
}
