package me.RyanStewart.ForbiddenAreas;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;


public class ListenerClass implements Listener{
	
	private Location loc1 = ForbiddenAreas.loc1;
	private Location loc2 = ForbiddenAreas.loc2;
	private Material selectionBlock = Material.DOUBLE_STONE_SLAB2;
	
	public ListenerClass(ForbiddenAreas plugin){
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	public void onInArea(Player plr){
		plr.setVelocity(plr.getLocation().getDirection().multiply(-1.0023));
	}
	
	
	@EventHandler
	public void onDmg(BlockDamageEvent e){
		if (ForbiddenAreas.selecting == true){
			ForbiddenAreas.clickCount++;
			if (ForbiddenAreas.clickCount == 1){
				//resetting
				ForbiddenAreas.oldLocs.clear();
				loc1 = null;
				loc2 = null;
				//location 1
				loc1 = e.getBlock().getLocation();
				Material m1 = e.getBlock().getType();
				ForbiddenAreas.oldLocs.put(loc1.getBlock(), m1);
				loc1.getBlock().setType(selectionBlock);
			}
			
			if (ForbiddenAreas.clickCount == 2){
				//location 2
				loc2 = e.getBlock().getLocation();
				Material m2 = e.getBlock().getType();
				ForbiddenAreas.oldLocs.put(loc2.getBlock(), m2);
				loc2.getBlock().setType(selectionBlock);
				ForbiddenAreas.clickCount = 0;
				ForbiddenAreas.selecting = false;
			}
			e.setCancelled(true);
		}
	}
	
	
	
	@EventHandler
	public void onWalk(PlayerMoveEvent e){
		Player plr = e.getPlayer();
		
		if (loc1 != null && loc2 != null){
		
			if (loc1.getBlockX() > loc2.getBlockX()){
				if (plr.getLocation().getBlockX() >= loc2.getBlockX() && plr.getLocation().getBlockX() <= loc1.getBlockX()){ //if in x range
					if (loc1.getBlockZ() > loc2.getBlockZ()){
						if (plr.getLocation().getBlockZ() >= loc2.getBlockZ() && plr.getLocation().getBlockZ() <= loc1.getBlockZ()){ //if in z range
							onInArea(plr);
						}
					}
					else
					{
						if (plr.getLocation().getBlockZ() <= loc2.getBlockZ() && plr.getLocation().getBlockZ() >= loc1.getBlockZ()){ //if in z range
							onInArea(plr);
						}
					}
				}
			}
			else
			{				
				if (plr.getLocation().getBlockX() <= loc2.getBlockX() && plr.getLocation().getBlockX() >= loc1.getBlockX()){ //if in x range
					if (loc1.getBlockZ() > loc2.getBlockZ()){
						if (plr.getLocation().getBlockZ() >= loc2.getBlockZ() && plr.getLocation().getBlockZ() <= loc1.getBlockZ()){ //if in z range
							onInArea(plr);
						}
					}
					else
					{
						if (plr.getLocation().getBlockZ() <= loc2.getBlockZ() && plr.getLocation().getBlockZ() >= loc1.getBlockZ()){ //if in z range
							onInArea(plr);
						}
					}
				}
			}
		}
	}

}
