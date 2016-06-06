package me.RyanStewart.ForbiddenAreas;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class ForbiddenAreas extends JavaPlugin{
	
	public Logger log = Logger.getLogger("Minecraft");
	public static boolean selecting = false;
	public static int clickCount = 0;
	public static Location loc1;
	public static Location loc2;
	public static String regionName = null;
	
	public void onEnable(){
		new ListenerClass(this);
		this.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "-----------------------------------------------------");
		this.getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "[Forbidden Areas] Forbidden Areas enabled successfully!");
		this.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "-----------------------------------------------------");	
	
		PluginManager pm = this.getServer().getPluginManager();
		pm.addPermission(new Permission("ForbiddenAreas.create")); //to create areas
		pm.addPermission(new Permission("ForbiddenAreas.destroy")); //to delete areas
		pm.addPermission(new Permission("ForbiddenAreas.toggle")); //to show/hide areas
		pm.addPermission(new Permission("ForbiddenAreas.list")); //to list areas
				
		if (!this.getConfig().contains("prefix")){
			this.getConfig().addDefault("prefix", ChatColor.GREEN + "[" + ChatColor.AQUA + "" + ChatColor.BOLD + "FA" + ChatColor.RESET + "" + ChatColor.GREEN + "] ");
		}
		if (!this.getConfig().contains("rebound_amount")){
			this.getConfig().addDefault("rebound_amount", .6);
		}
		if (!this.getConfig().contains("selection_block")){
			this.getConfig().addDefault("selection_block", Material.DOUBLE_STEP.toString());
		}
		if (!this.getConfig().contains("regions")){
			this.getConfig().createSection("regions");
		}
		this.getConfig().options().copyDefaults(true);
		saveConfig();
	}
	
	public void onDisable(){
		this.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "-----------------------------------------------------");
		this.getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "[Forbidden Areas] Forbidden Areas disabled successfully.");
		this.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "-----------------------------------------------------");
		saveConfig();
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		Player plr = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("fa") && sender instanceof Player){
			if (args.length == 1 && !args[0].equalsIgnoreCase("cancel") && !args[0].equalsIgnoreCase("list")){
				if (plr.hasPermission("ForbiddenAreas.create")){
					if (!this.getConfig().contains("regions." + args[0])){
						plr.sendMessage((String) this.getConfig().get("prefix") + ChatColor.GREEN + "Please select two diagonals of a rectangular region. Type \"/fa cancel\" to cancel the selection.");
						regionName = args[0];
						selecting = true;
					}else{
						plr.sendMessage((String) this.getConfig().get("prefix") + ChatColor.RED + "A region with this name already exists. To overwrite it, please delete the region and try again.");
					}
				}else{
					plr.sendMessage(ChatColor.RED + "You do not have permission to do that.");
				}
			}else if (args.length == 1 && args[0].equalsIgnoreCase("cancel")){
				if (plr.hasPermission("ForbiddenAreas.create")){
					if (selecting == true && (loc1 == null || loc2 == null)){
						plr.sendMessage((String) this.getConfig().get("prefix") + ChatColor.GOLD + "Selection cancelled.");
						
						if (loc1 != null){
							String l1Type = (String) this.getConfig().get("regions." + regionName + ".loc1type");
							loc1.getBlock().setType(Material.getMaterial(l1Type));
							loc1 = null;
						}
						if (loc2 != null){
							String l2Type = (String) this.getConfig().get("regions." + regionName + ".loc2type");
							loc2.getBlock().setType(Material.getMaterial(l2Type));
							loc2 = null;
						}
						
						this.getConfig().set("regions." + regionName, null);
						saveConfig();
						
						clickCount = 0;
						selecting = false;
						
					}else{
						plr.sendMessage((String) this.getConfig().get("prefix") + ChatColor.RED + "There is no operation to cancel!");
					}
				}else{
					plr.sendMessage(ChatColor.RED + "You do not have permission to do that.");
				}
			}else if (args.length == 1 && args[0].equalsIgnoreCase("list")){
				if (plr.hasPermission("ForbiddenAreas.list")){
					if (this.getConfig().getConfigurationSection("regions").getKeys(false) != null){
						String nodes = this.getConfig().getConfigurationSection("regions").getKeys(false).toString();
						if (this.getConfig().getConfigurationSection("regions").getKeys(false).size() == 0){
							plr.sendMessage((String) this.getConfig().get("prefix") + ChatColor.GOLD + "No regions to be listed.");
						}else{
							nodes = nodes.replace("[", "");
							nodes = nodes.replace("]", "");
							plr.sendMessage((String) this.getConfig().get("prefix") + ChatColor.AQUA + nodes);
						}
					}
				}else{
					plr.sendMessage(ChatColor.RED + "You do not have permission to do that." );
				}
			}else if (args.length == 2 && args[1].equalsIgnoreCase("delete")){
				if (plr.hasPermission("ForbiddenAreas.destroy")){
					if (this.getConfig().get("regions." + args[0]) != null){
						Location l1 = (Location) this.getConfig().get("regions." + args[0] + ".loc1pos");
						Material m1 = Material.getMaterial((String) this.getConfig().get("regions." + args[0] + ".loc1type"));
						l1.getBlock().setType(m1);
						Location l2 = (Location) this.getConfig().get("regions." + args[0] + ".loc2pos");
						Material m2 = Material.getMaterial((String) this.getConfig().get("regions." + args[0] + ".loc2type"));
						l2.getBlock().setType(m2);
						this.getConfig().set("regions." + args[0], null);
						saveConfig();
						loc1 = null;
						loc2 = null;
						plr.sendMessage((String) this.getConfig().get("prefix") + ChatColor.GREEN + "Region deleted successfully!");
					}else{
						plr.sendMessage((String) this.getConfig().get("prefix") + ChatColor.RED + "\"" + args[0] + "\"" + " is not a valid region name.");
					}
				}else{
					plr.sendMessage(ChatColor.RED + "You do not have permission to do that.");
				}
			}else if(args.length == 2 && args[1].equalsIgnoreCase("hide")){
				if (plr.hasPermission("ForbiddenAreas.toggle")){
					if (this.getConfig().get("regions." + args[0]) != null){
						if (this.getConfig().get("regions." + args[0] + ".loc1pos") != null){
							Material l1Type = Material.getMaterial((String) this.getConfig().get("regions." + args[0] + ".loc1type"));
							((Location) this.getConfig().get("regions." + args[0] + ".loc1pos")).getBlock().setType(l1Type);
						}
						if (this.getConfig().get("regions." + args[0] + ".loc2pos") != null){
							Material l2Type = Material.getMaterial((String) this.getConfig().get("regions." + args[0] + ".loc2type"));
							((Location) this.getConfig().get("regions." + args[0] + ".loc2pos")).getBlock().setType(l2Type);
						}
						plr.sendMessage((String) this.getConfig().get("prefix") + ChatColor.GREEN + "Region successfully hidden.");
					}else{
						plr.sendMessage(ChatColor.RED + "\"" + args[0] + "\"" + " is not a valid region name.");
					}
				}else{
					plr.sendMessage(ChatColor.RED + "You do not have permission to do that.");
				}
			}else if (args.length == 2 && args[1].equalsIgnoreCase("show")){
				if (plr.hasPermission("ForbiddenAreas.toggle")){
					if (this.getConfig().get("regions." + args[0]) != null){
						if (this.getConfig().get("regions." + args[0] + ".loc1pos") != null){
							Location l1 = (Location) this.getConfig().get("regions." + args[0] + ".loc1pos");
							l1.getBlock().setType(Material.getMaterial((String) this.getConfig().get("selection_block")));
						}
						if (this.getConfig().get("regions." + args[0] + ".loc2pos") != null){
							Location l2 = (Location) this.getConfig().get("regions." + args[0] + ".loc2pos");
							l2.getBlock().setType(Material.getMaterial((String) this.getConfig().get("selection_block")));
						}
						plr.sendMessage((String) this.getConfig().get("prefix") + ChatColor.GREEN  + "Region successfully shown.");
					}else{
						plr.sendMessage(ChatColor.RED + "\"" + args[0] + "\"" + " is not a valid region name.");
					}
				}else{
					plr.sendMessage(ChatColor.RED + "You do not have permission to do that.");
				}
			}else{
				plr.sendMessage(ChatColor.RED + "Too many or incorrect arguments.");
			}
			
			return true;
		}
		
		return false;
	}	
}
