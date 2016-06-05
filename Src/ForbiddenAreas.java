package me.RyanStewart.ForbiddenAreas;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class ForbiddenAreas extends JavaPlugin{
	
	public Logger log = Logger.getLogger("Minecraft");
	public static boolean selecting = false;
	public static int clickCount = 0;
	public static Map<Block, Material> oldLocs = new HashMap<Block, Material>();
	public static Location loc1;
	public static Location loc2;
	
	public void onEnable(){
		new ListenerClass(this);
	}
	
	public void onDisable(){

	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		Player plr = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("fa") && sender instanceof Player){
			if (args.length == 0){
				plr.sendMessage(ChatColor.GREEN + "Please select two diagonals of a rectangular region. Type \"/fa cancel\" to cancel the selection.");
				selecting = true;
			}else if (args.length == 1 && args[0].equalsIgnoreCase("cancel")){
				if (selecting == true && (loc1 == null || loc2 == null)){
					plr.sendMessage(ChatColor.GOLD + "Selection cancelled.");
					selecting = false;
					if (loc1 != null){
						Material l1Type = oldLocs.get(loc1.getBlock());
						loc1.getBlock().setType(l1Type);
						oldLocs.clear();
						loc1 = null;
					}
					if (loc1 != null){
						Material l1Type = oldLocs.get(loc1.getBlock());
						loc1.getBlock().setType(l1Type);
						oldLocs.clear();
						loc1 = null;
					}
					
					clickCount = 0;
					
				}else{
					plr.sendMessage(ChatColor.RED + "There is no operation to cancel!");
				}
				
			}else{
				plr.sendMessage(ChatColor.RED + "Too many or incorrect arguments.");
			}
			
			return true;
		}
		
		return false;
	}	
}
