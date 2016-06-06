package me.RyanStewart.ForbiddenAreas;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import net.md_5.bungee.api.ChatColor;


public class ListenerClass implements Listener{
	
	ForbiddenAreas plugin;
	FileConfiguration config;
	
	public ListenerClass(ForbiddenAreas plugin){
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;
		config = plugin.getConfig();
	}
	
	public void onInArea(Player plr){
		plr.setVelocity(plr.getLocation().getDirection().multiply((double)config.get("rebound_amount") * -1));
	}
	
	
	@EventHandler
	public void onDmg(BlockDamageEvent e){
		if (ForbiddenAreas.selecting == true){
			ForbiddenAreas.clickCount++;
			if (ForbiddenAreas.clickCount == 1){
				//location 1
				ForbiddenAreas.loc1 = e.getBlock().getLocation();
				String m1 = e.getBlock().getType().toString();
				ForbiddenAreas.loc1.getBlock().setType(Material.getMaterial((String) config.get("selection_block")));
				
				
				String pathPos1 = "regions." + (ForbiddenAreas.regionName) + ".loc1pos";
				String pathType1 = "regions." + (ForbiddenAreas.regionName) + ".loc1type";
				config.set(pathPos1, ForbiddenAreas.loc1);
				config.set(pathType1, m1);
				plugin.saveConfig();
				
				
			}
			
			if (ForbiddenAreas.clickCount == 2){
				//location 2
				ForbiddenAreas.loc2 = e.getBlock().getLocation();
				String m2 = e.getBlock().getType().toString();
				ForbiddenAreas.loc2.getBlock().setType(Material.getMaterial((String) config.get("selection_block")));
				
				
				String pathPos2 = "regions." + (ForbiddenAreas.regionName) + ".loc2pos";
				String pathType2 = "regions." + (ForbiddenAreas.regionName) + ".loc2type";
				config.set(pathPos2, ForbiddenAreas.loc2);
				config.set(pathType2, m2);
				plugin.saveConfig();
				
				
				ForbiddenAreas.clickCount = 0;
				ForbiddenAreas.selecting = false;
				e.getPlayer().sendMessage((String) config.get("prefix") + ChatColor.GREEN + "Region \"" + ForbiddenAreas.regionName + "\" created successfully!");
			}
			e.setCancelled(true);
		}
	}
	
	
	@EventHandler
	public void onWalk(PlayerMoveEvent e){
		Player plr = e.getPlayer();
		for (String k : config.getConfigurationSection("regions").getKeys(false)){
			//K is the string name of the regions
			if (config.get("regions." + k + ".loc1pos") != null && config.get("regions." + k + ".loc2pos") != null){
			Location l1L = (Location) config.get("regions." + k + ".loc1pos");
			Location l2L = (Location) config.get("regions." + k + ".loc2pos");
			
				
				if (l1L.getBlockX() > l2L.getBlockX()){
					if (plr.getLocation().getBlockX() >= l2L.getBlockX() && plr.getLocation().getBlockX() <= l1L.getBlockX()){ //if in x range
						if (l1L.getBlockZ() > l2L.getBlockZ()){
							if (plr.getLocation().getBlockZ() >= l2L.getBlockZ() && plr.getLocation().getBlockZ() <= l1L.getBlockZ()){ //if in z range
								onInArea(plr);
							}
						}
						else
						{
							if (plr.getLocation().getBlockZ() <= l2L.getBlockZ() && plr.getLocation().getBlockZ() >= l1L.getBlockZ()){ //if in z range
								onInArea(plr);
							}
						}
					}
				}
				else
				{				
					if (plr.getLocation().getBlockX() <= l2L.getBlockX() && plr.getLocation().getBlockX() >= l1L.getBlockX()){ //if in x range
						if (l1L.getBlockZ() > l2L.getBlockZ()){
							if (plr.getLocation().getBlockZ() >= l2L.getBlockZ() && plr.getLocation().getBlockZ() <= l1L.getBlockZ()){ //if in z range
								onInArea(plr);
							}
						}
						else
						{
							if (plr.getLocation().getBlockZ() <= l2L.getBlockZ() && plr.getLocation().getBlockZ() >= l1L.getBlockZ()){ //if in z range
								onInArea(plr);
							}
						}
					}
				}
			}
		}
	}

}
