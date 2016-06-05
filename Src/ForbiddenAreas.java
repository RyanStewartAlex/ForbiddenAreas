package me.RyanStewart.ForbiddenAreas;

import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class ForbiddenAreas extends JavaPlugin{
	
	public Logger log = Logger.getLogger("Minecraft");
	
	public void onEnable(){
		new ListenerClass(this);
	}
	
	public void onDisable(){

	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		
		return false;
	}	
}
