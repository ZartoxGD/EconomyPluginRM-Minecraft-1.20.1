package zartox.economypluginrm;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.*;
import org.bukkit.advancement.Advancement;
import org.bukkit.block.*;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class EventsListener implements Listener {

    private final Plugin plugin;

    public EventsListener(Plugin _plugin){
        plugin = _plugin;
    }

    @EventHandler
    public void onPlayerTakeDamage(EntityDamageEvent event){

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();

        if(!EconomyPluginRM.PlayerDataExist(player)){
            PlayerData playerData = new PlayerData(player);
            EconomyPluginRM.playersData.add(playerData);
        }

        // 12 seconds (240 ticks)
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 240, 100));
        player.sendMessage(ChatColor.GREEN + "You are now invincible for 12sec !");

        new BukkitRunnable() {
            @Override
            public void run() {
                player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                player.sendMessage(ChatColor.GREEN + "You are not invincible anymore !");
            }
        }.runTaskLater(plugin, 240L);
    }

    private void CreateShop(Material material, int buyPerClick, int price, int stockAmount, Chest chest, Sign sign, Player player){
        PhysicalShop physicalShop = new PhysicalShop(material, buyPerClick, price, stockAmount, chest, sign, EconomyPluginRM.GetPlayerData(player));
        EconomyPluginRM.physicalShops.add(physicalShop);

        player.sendMessage(ChatColor.GREEN + "Shop created ! | Type: " + material.toString() + " | Stock: " + stockAmount + " | Buy/Click: " + buyPerClick + " | Price: " + price);
    }

    private void CreatePrivateChest(Block chest, Block sign, String[] authorizedPlayersName, Player player){
        PrivateChest privateChest = new PrivateChest(chest.getLocation(), sign.getLocation(), authorizedPlayersName, player.getName());

        EconomyPluginRM.privateChests.add(privateChest);
        player.sendMessage(ChatColor.GREEN + "Private chest created ! Authorized players: " + String.join(", ", authorizedPlayersName));
    }

    private boolean IsShopSign(String signFirstLine){
        return Objects.equals(signFirstLine, "[Shop]") || Objects.equals(signFirstLine, "Shop") || Objects.equals(signFirstLine, "[shop]") || Objects.equals(signFirstLine, "shop");
    }

    private boolean IsWallSign(Sign sign){
        if(!(sign.getBlock().getState().getBlockData() instanceof WallSign)){
            return false;
        }
        return true;
    }

    private boolean IsBlockBehindChest(Block block){
        if(block.getType() != Material.CHEST){
            return false;
        }
        return true;
    }

    private boolean IsChestEmpty(Chest chest){
        if(chest.getInventory().isEmpty()){
            return true;
        }
        return false;
    }

    private Set<String> GetChestContentTypes(Chest chest){
        Set<String> uniqueItemTypes = new HashSet<>();

        for (ItemStack item : chest.getInventory().getContents()) {
            if (item != null) {
                // Add the item type to the set of unique item types
                uniqueItemTypes.add(item.getType().toString());
            }
        }
        return uniqueItemTypes;
    }

    private Set<String> GetChestContentTypes(Inventory inventory){
        Set<String> uniqueItemTypes = new HashSet<>();

        for (ItemStack item : inventory.getContents()) {
            if (item != null) {
                // Add the item type to the set of unique item types
                uniqueItemTypes.add(item.getType().toString());
            }
        }
        return uniqueItemTypes;
    }

    private boolean IsChestContainsSingleType(Set<String> types){
        if(types.size() > 1) {
            return false;
        }
        return true;
    }

    private Material GetMaterial(Set<String> types){
        return Material.matchMaterial(types.iterator().next());
    }

    private int GetStock(Chest chest){
        int shopStockAmount = 0;

        for (ItemStack item : chest.getBlockInventory().getContents()) {
            if (item != null) {
                // Add the item type to the set of unique item types
                shopStockAmount += item.getAmount();
            }
        }

        return shopStockAmount;
    }

    private int GetBuyPerClick(String signSecondLine, Player player, Material shopMaterial){
        try{
            String[] line1 = signSecondLine.split(":");
            return Integer.parseInt(line1[0]);
        }
        catch(Exception e){
            player.sendMessage(ChatColor.RED + "The second line of the sign should be buyPerClick:Price");
            player.sendMessage(ChatColor.RED + "Ex: 64:1000 which means that on each player click on the sign he would buy 64 " + shopMaterial.toString() + " for 1000$");
            return 0;
        }
    }

    private int GetPrice(String signSecondLine, Player player, Material shopMaterial){
        try{
            String[] line1 = signSecondLine.split(":");
            return Integer.parseInt(line1[1]);
        }
        catch(Exception e){
            player.sendMessage(ChatColor.RED + "The second line of the sign should be buyPerClick:Price");
            player.sendMessage(ChatColor.RED + "Ex: 64:1000 which means that on each player click on the sign he would buy 64 " + shopMaterial.toString() + " for 1000$");
            return 0;
        }
    }//OK

    private boolean IsPrivateChest(String firstLine){
        return Objects.equals(firstLine, "[Private]") || Objects.equals(firstLine, "[private]") || Objects.equals(firstLine, "Private") || Objects.equals(firstLine, "private");
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {

        Player player = event.getPlayer();
        Sign sign = (Sign) event.getBlock().getState();
        String[] lines = event.getLines();

        if(IsPrivateChest(lines[0])){
            if(!IsWallSign(sign)){
                return;
            }

            WallSign signData  = (WallSign) sign.getBlock().getState().getBlockData();
            BlockFace attached  = signData.getFacing().getOppositeFace();
            Block blockBehindSign = sign.getBlock().getRelative(attached);

            if(!IsBlockBehindChest(blockBehindSign)){
                return;
            }

            Chest chest = (Chest) blockBehindSign.getState();
            String[] playersName = new String[3];
            playersName[0] = lines[1];
            playersName[1] = lines[2];
            playersName[2] = lines[3];

            CreatePrivateChest(chest.getBlock(), sign.getBlock(), playersName, player);

            event.setLine(0, ChatColor.GREEN + lines[0]);
            event.setLine(1, ChatColor.GREEN + lines[1]);
            event.setLine(2, ChatColor.GREEN + lines[2]);
            event.setLine(3, ChatColor.GREEN + lines[3]);
            return;
        }

        if(!IsShopSign(lines[0])){
            return;
        }

        if(!IsWallSign(sign)){
            return;
        }

        WallSign signData  = (WallSign) sign.getBlock().getState().getBlockData();
        BlockFace attached  = signData.getFacing().getOppositeFace();
        Block blockBehindSign = sign.getBlock().getRelative(attached);

        if(!IsBlockBehindChest(blockBehindSign)){
            return;
        }

        Chest shopChest = (Chest) blockBehindSign.getState();

        if(IsChestEmpty(shopChest)){
            player.sendMessage(ChatColor.RED + "You need to put your items in the chest before creating the shop");
            return;
        }

        Set<String> uniqueItemTypes = GetChestContentTypes(shopChest);

        if (!IsChestContainsSingleType(uniqueItemTypes)) {
            player.sendMessage(ChatColor.RED + "You can only have one type of object at a time in your shop");
            return;
        }

        Material shopMaterial = GetMaterial(uniqueItemTypes);
        int shopStockAmount = GetStock(shopChest);

        int buyPerClick = GetBuyPerClick(lines[1], player, shopMaterial);

        if(buyPerClick <= 0){
            player.sendMessage(ChatColor.RED + "The buy per click cannot be less than or equal to 0");
            return;
        }

        int price = GetPrice(lines[1], player, shopMaterial);

        if(price <= 0){
            player.sendMessage(ChatColor.RED + "The price cannot be less than or equal to 0");
            return;
        }

        CreateShop(shopMaterial, buyPerClick, price, shopStockAmount, shopChest, sign, player);

        event.setLine(0, ChatColor.GREEN + "[Shop]");
        event.setLine(1, ChatColor.GREEN + ""  + price + "$" + "/" + buyPerClick);
        event.setLine(2, ChatColor.GREEN + "" + shopMaterial);
        event.setLine(3, ChatColor.GREEN + "Stock: " + shopStockAmount);
    }

    private boolean IsChestOrSign(Block block){
        return block.getState() instanceof Chest || block.getState() instanceof Sign;
    }

    private boolean IsWallSign(Block block){
        return block.getState() instanceof Sign;
    }

    @EventHandler
    public void onBlockDamaged(BlockDamageEvent event){
        Block block = event.getBlock();
        Player player = event.getPlayer();

        if(!IsChestOrSign(block)){
            return;
        }

        if(EconomyPluginRM.IsPrivateChest(block)){
            PrivateChest chest = EconomyPluginRM.GetPrivateChestByLocation(block.getLocation());//MODIF
            if(!chest.OwnerName.equals(player.getName())){
                player.sendMessage(ChatColor.RED + "You are not the owner of this private chest...");
                event.setCancelled(true);
            }//FIN MODIF
        }

        if(!EconomyPluginRM.IsPhysicalShop(block)){
            return;
        }

        PhysicalShop shop = EconomyPluginRM.GetPhysicalShopByLocation(block.getLocation());

        if(!EconomyPluginRM.IsPhysicalShopOwner(player, shop)){
            player.sendMessage(ChatColor.RED + "You are not the owner of this shop...");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockDestroyed(BlockBreakEvent event){
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if(!IsChestOrSign(block)){
            return;
        }

        if(EconomyPluginRM.IsPrivateChest(block)){
            PrivateChest chest = EconomyPluginRM.GetPrivateChestByLocation(block.getLocation());//MODIF
            if(chest.OwnerName.equals(player.getName())){
                chest.GetPlayer().sendMessage(ChatColor.GREEN + "Successfully destroyed your private chest !");
                EconomyPluginRM.privateChests.remove(chest);
            }
            else{
                player.sendMessage(ChatColor.RED + "You are not the owner of this private chest...");
                event.setCancelled(true);
            }//FIN MODIF
        }

        if(!EconomyPluginRM.IsPhysicalShop(block)){
            return;
        }

        PhysicalShop shop = EconomyPluginRM.GetPhysicalShopByLocation(block.getLocation());

        if(!EconomyPluginRM.IsPhysicalShopOwner(player, shop)){
            player.sendMessage(ChatColor.RED + "You are not the owner of this shop...");
            event.setCancelled(true);
        }
        else{
            player.sendMessage(ChatColor.GREEN + "You successfully destroyed a shop !");
            EconomyPluginRM.physicalShops.remove(shop);
        }
    }

    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent event){
        Block block = event.getBlock();

        if(!IsChestOrSign(block)){
            return;
        }

        if(!EconomyPluginRM.IsPhysicalShop(block)){
            return;
        }

        if(EconomyPluginRM.IsPrivateChest(block)){
            event.setCancelled(true);
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event){
        List<Block> blocks = event.blockList();

        for(Block block : blocks){
            if(EconomyPluginRM.IsPhysicalShop(block)){
                event.setCancelled(true);
            }

            if(EconomyPluginRM.IsPrivateChest(block)){
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event){
        List<Block> blocks = event.blockList();

        for(Block block : blocks){
            if(EconomyPluginRM.IsPhysicalShop(block)){
                event.setCancelled(true);
            }

            if(EconomyPluginRM.IsPrivateChest(block)){
                event.setCancelled(true); //MODIFICATION
            }
        }
    }

    private boolean IsChest(Block block){
        return block.getState() instanceof Chest;
    }

    private boolean IsSign(Block block){
        return block.getState() instanceof Sign;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) throws IOException {
        Block block = event.getClickedBlock();
        PlayerData playerData = EconomyPluginRM.GetPlayerData(event.getPlayer());
        boolean isLeftClick = event.getAction().name().contains("LEFT_CLICK");

        if(block == null){
            return;
        }

        try{
            if(block.getType() == Material.TNT || block.getType() == Material.TNT_MINECART || event.getItem().getType() == Material.FLINT_AND_STEEL) {
                EconomyLogger.AddLog(new Log(event.getPlayer().getName(), "used a " + event.getItem().getType() + " on a " + block.getType()));
            }
        }
        catch (NullPointerException e){
            EconomyLogger.AddLog(new Log(event.getPlayer().getName(), "clicked on a " + block.getType()));
        }

        if(IsSign(block)){
            if(EconomyPluginRM.IsPhysicalShop(block)){
                if(isLeftClick){
                    return;
                }
                PhysicalShop shop = EconomyPluginRM.GetPhysicalShopByLocation(block.getLocation());
                if(!EconomyPluginRM.IsPhysicalShopOwner(event.getPlayer(), shop)){
                    shop.BuyAtShop(playerData);
                }
                else{
                    event.getPlayer().sendMessage(ChatColor.YELLOW + "You can't buy at your own shop...");
                }
                return;
            }
        }

        if(IsChest(block)){
            if(EconomyPluginRM.IsPhysicalShop(block)){
                PhysicalShop shop = EconomyPluginRM.GetPhysicalShopByLocation(block.getLocation());
                if(!EconomyPluginRM.IsPhysicalShopOwner(event.getPlayer(), shop)){
                    event.getPlayer().sendMessage(ChatColor.RED + "This shop does not belong to you...");
                    event.setCancelled(true);
                }
                //shop.DebugShop();
                return;
            }

            if(EconomyPluginRM.IsPrivateChest(block)){
                PrivateChest chest = EconomyPluginRM.GetPrivateChestByLocation(block.getLocation());
                if(!chest.IsPlayerAuthorized(event.getPlayer())){
                    event.getPlayer().sendMessage(ChatColor.RED + "This chest does not belong to you...");
                    event.setCancelled(true);
                }
            }
        }
    }

    /*@EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory inventory = event.getInventory();

        if (!(inventory.getHolder() instanceof Chest)) {
            return;
        }

        Chest chest = (Chest) inventory.getHolder();
        Block chestBlock = chest.getBlock();

        if (!EconomyPluginRM.IsPhysicalShop(chestBlock)) {
            return;
        }

        PhysicalShop shop = EconomyPluginRM.GetPhysicalShopByLocation(chestBlock.getLocation());

        if(event.getCurrentItem().getType() != shop.ShopMaterial){
            player.sendMessage(ChatColor.RED + "Wrong item for this shop...");
            event.setCancelled(true);
            return;
        }

        if (event.getClick().isShiftClick()) {
            // Check if items are being taken from the chest
            if (event.getRawSlot() < inventory.getSize()) {
                if (event.getCurrentItem() != null) {
                    new BukkitRunnable(){
                        @Override
                        public void run() {
                            shop.CountStock();
                        }
                    }.runTaskLaterAsynchronously(plugin, 2);
                    shop.VerifyStock();
                    player.sendMessage(ChatColor.GREEN + "Removed " + event.getCurrentItem().getType() + " from your shop's stock !");
                }
            }

            // Check if items are being placed into the chest
            if (event.getRawSlot() >= inventory.getSize() && event.getRawSlot() < inventory.getSize() + player.getInventory().getSize()) {
                if (event.getCurrentItem() != null) {
                    new BukkitRunnable(){
                        @Override
                        public void run() {
                            shop.CountStock();
                        }
                    }.runTaskLaterAsynchronously(plugin, 2);
                    shop.VerifyStock();
                    player.sendMessage(ChatColor.GREEN + "Added " + event.getCurrentItem().getType() + " to your shop's stock !");
                }
            }
        }
        else {
            player.sendMessage(ChatColor.RED + "Only Shift click authorized !");
            event.setCancelled(true);
        }
    }*/

    @EventHandler
    public void onInventoryClosed(InventoryCloseEvent event){
        Inventory inventory = event.getInventory();
        Player player = (Player) event.getPlayer();

        if (!(inventory.getHolder() instanceof Chest)) {
            return;
        }

        Chest chest = (Chest) inventory.getHolder();
        Block chestBlock = chest.getBlock();

        if (!EconomyPluginRM.IsPhysicalShop(chestBlock)) {
            return;
        }

        PhysicalShop shop = EconomyPluginRM.GetPhysicalShopByLocation(chestBlock.getLocation());
        Set<String> uniqueItemTypes = GetChestContentTypes(inventory);

        if(IsChestEmpty(chest)){
            player.sendMessage(ChatColor.RED + "The shop can't be empty !");
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                player.openInventory(inventory);
            }, 1); // Delay of 1 tick
            return;
        }

        if (!IsChestContainsSingleType(uniqueItemTypes)) {
            player.sendMessage(ChatColor.RED + "You can only have one type of object at a time in your shop");
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                player.openInventory(inventory);
            }, 1); // Delay of 1 tick
            return;
        }

        if(GetMaterial(uniqueItemTypes) != shop.ShopMaterial){
            player.sendMessage(ChatColor.RED + "This is a " + shop.ShopMaterial + " shop ! Remove the " + GetMaterial(uniqueItemTypes) + " stack...");
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                player.openInventory(inventory);
            }, 1); // Delay of 1 tick
            return;
        }

        shop.CountStock();
    }

    public Block[] GetBlocksAround(Block centerBlock, int radius) {
        World world = centerBlock.getWorld();
        Location centerLocation = centerBlock.getLocation();

        int minX = centerLocation.getBlockX() - radius;
        int minY = centerLocation.getBlockY() - radius;
        int minZ = centerLocation.getBlockZ() - radius;
        int maxX = centerLocation.getBlockX() + radius;
        int maxY = centerLocation.getBlockY() + radius;
        int maxZ = centerLocation.getBlockZ() + radius;

        int sizeX = maxX - minX + 1;
        int sizeY = maxY - minY + 1;
        int sizeZ = maxZ - minZ + 1;

        Block[] blocks = new Block[sizeX * sizeY * sizeZ];
        int index = 0;

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Block block = world.getBlockAt(x, y, z);
                    blocks[index] = block;
                    index++;
                }
            }
        }

        return blocks;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) throws IOException {
        Block block = event.getBlock();
        Player player = event.getPlayer();
        int radius = 1;

        Block[] blocksAround = GetBlocksAround(block, radius);

        if(block.getType() == Material.TNT || block.getType() == Material.TNT_MINECART || block.getType() == Material.DRAGON_HEAD || block.getType() == Material.WITHER_SKELETON_SKULL || block.getType() == Material.END_CRYSTAL){
            EconomyLogger.AddLog(new Log(player.getName(), "placed a " + block.getType()));
        }

        for(Block b : blocksAround){
            if(EconomyPluginRM.IsPrivateChest(b)){ //AJOUT
                PrivateChest chest = EconomyPluginRM.GetPrivateChestByLocation(b.getLocation());

                if(!chest.IsPlayerAuthorized(player)){
                    player.sendMessage(ChatColor.RED + "You can't place blocks around someone else's private chest...");
                    event.setCancelled(true);
                    break;
                }
            } //FIN AJOUT

            if(EconomyPluginRM.IsPhysicalShop(b)){
                PhysicalShop shop = EconomyPluginRM.GetPhysicalShopByLocation(b.getLocation());

                if(!EconomyPluginRM.IsPhysicalShopOwner(player, shop)){
                    player.sendMessage(ChatColor.RED + "You can't place blocks around someone else's shop...");
                    event.setCancelled(true);
                    break;
                }
            }
        }
    }
}
