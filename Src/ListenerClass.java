package me.RyanStewart.ForbiddenAreas;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import net.md_5.bungee.api.ChatColor;


public class ListenerClass implements Listener{
	
	public int clickCount = 0;
	public Location loc1;
	public Location loc2;
	public Map<Block, Material> oldLocs = new HashMap<Block, Material>();
	
	
	public ListenerClass(ForbiddenAreas plugin){
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	
	@EventHandler
	public void onDmg(BlockDamageEvent e){
		Player plr = e.getPlayer();
		
		clickCount++;
		if (clickCount == 1){
			loc1 = e.getBlock().getLocation();
			e.getBlock().setType(Material.IRON_BLOCK);
			oldLocs.put(loc1.getBlock(), e.getBlock().getType());
		}
		if (clickCount == 2){
			//on second click
			loc2 = e.getBlock().getLocation();
			e.getBlock().setType(Material.IRON_BLOCK);
			oldLocs.put(loc2.getBlock(), e.getBlock().getType());
		}
		else if (clickCount >= 3){
			
			loc1.getBlock().setType(oldLocs.get(loc1));
			loc2.getBlock().setType(oldLocs.get(loc2));
			
			loc1 = null;
			loc2 = null;
			oldLocs.remove(loc1.getBlock());
			oldLocs.remove(loc2.getBlock());
			
			clickCount = 1;
			loc1 = e.getBlock().getLocation();
			e.getBlock().setType(Material.IRON_BLOCK);
			oldLocs.put(loc1.getBlock(), e.getBlock().getType());
		}
		
		plr.sendMessage(ChatColor.GREEN + "Changed to click number " + clickCount);
		
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onWalk(PlayerMoveEvent e){
		Player plr = e.getPlayer();
		
		if (loc1 != null && loc2 != null){
		
			if (loc1.getBlockX() > loc2.getBlockX()){
				if (plr.getLocation().getBlockX() >= loc2.getBlockX() && plr.getLocation().getBlockX() <= loc1.getBlockX()){ //if in x range
					if (loc1.getBlockZ() > loc2.getBlockZ()){
						if (plr.getLocation().getBlockZ() >= loc2.getBlockZ() && plr.getLocation().getBlockZ() <= loc1.getBlockZ()){ //if in z range
							plr.teleport(new Location(plr.getWorld(), 100, 100, 100));
						}
					}
					else
					{
						if (plr.getLocation().getBlockZ() <= loc2.getBlockZ() && plr.getLocation().getBlockZ() >= loc1.getBlockZ()){ //if in z range
							plr.teleport(new Location(plr.getWorld(), 100, 100, 100));
						}
					}
				}
			}
			else
			{
				plr.sendMessage("BYE");
				
				if (plr.getLocation().getBlockX() <= loc2.getBlockX() && plr.getLocation().getBlockX() >= loc1.getBlockX()){ //if in x range
					if (loc1.getBlockZ() > loc2.getBlockZ()){
						if (plr.getLocation().getBlockZ() >= loc2.getBlockZ() && plr.getLocation().getBlockZ() <= loc1.getBlockZ()){ //if in z range
							plr.teleport(new Location(plr.getWorld(), 100, 100, 100));
						}
					}
					else
					{
						if (plr.getLocation().getBlockZ() <= loc2.getBlockZ() && plr.getLocation().getBlockZ() >= loc1.getBlockZ()){ //if in z range
							plr.teleport(new Location(plr.getWorld(), 100, 100, 100));
						}
					}
				}
			}
		}
	}

}
