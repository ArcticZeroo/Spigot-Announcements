package artix.announcements;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class MainAnnouncement extends JavaPlugin{

	//Initial Declarations
	FileConfiguration config = this.getConfig();
	int position = config.getInt("position");
	ArrayList<String> announcementsList = new ArrayList<String>();
	boolean announcementsRunning = config.getBoolean("running");
	BukkitScheduler scheduler = getServer().getScheduler();
	//End Initial Declarations
	
	@Override
	public void onEnable() {
		//new ListenerClass(this);
		config.addDefault("interval", 120);
		config.addDefault("position", 0);
		config.addDefault("prefix", colorize("&f[&9Announcement&f]&r"));
		config.addDefault("running", true);
		config.options().copyDefaults(true);
		
		announcementsList = getList();
		
		if(announcementsList.size() > 0 && announcementsRunning){
			setInterval();
		}
	}
	
	@Override
	public void onDisable() {
		saveConfig();
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		Player player = (Player) sender;
		
		int length = args.length;
		
		if (cmd.getName().equalsIgnoreCase("announce") && sender instanceof Player) {
			
			if(length > 0){
				if(args[0].equalsIgnoreCase("interval")){
					if(sender.hasPermission(Permissions.INTERVAL) || sender.hasPermission(Permissions.ALL)){
						announceInterval(args, player);
					}else{
						noPermission(player, "/announce interval");
					}
				}else if(args[0].equalsIgnoreCase("help")){
					if(sender.hasPermission(Permissions.HELP) || sender.hasPermission(Permissions.ALL)){
						announceHelp(player);
					}else{
						noPermission(player, "/announce help");
					}
				}else if(args[0].equalsIgnoreCase("add")){
					if(sender.hasPermission(Permissions.ADD) || sender.hasPermission(Permissions.ALL)){
						announceAdd(args, player);
					}else{
						noPermission(player, "/announce add");
					}
				}else if(args[0].equalsIgnoreCase("list")){
					if(sender.hasPermission(Permissions.LIST) || sender.hasPermission(Permissions.ALL)){
						announceList(player);
					}else{
						noPermission(player, "/announce list");
					}
				}else if(args[0].equalsIgnoreCase("remove")){
					if(sender.hasPermission(Permissions.REMOVE) || sender.hasPermission(Permissions.ALL)){
						announceRemove(args, player);
					}else{
						noPermission(player, "/announce remove");
					}
				
				}else if(args[0].equalsIgnoreCase("removeall")){
					if(sender.hasPermission(Permissions.REMOVE) || sender.hasPermission(Permissions.ALL)){
						announceRemoveAll(args, player);
					}else{
						noPermission(player, "/announce removeall");
					}
				
				}else if(args[0].equalsIgnoreCase("prefix")){
					if(sender.hasPermission(Permissions.PREFIX) || sender.hasPermission(Permissions.ALL)){
						announcePrefix(args, player);
					}else{
						noPermission(player, "/announce prefix");
					}
				}else if(args[0].equalsIgnoreCase("info") || args[0].equalsIgnoreCase("version")){
					announceVersion(player);
				}else if(args[0].equalsIgnoreCase("message")){
					if(sender.hasPermission(Permissions.MESSAGE) || sender.hasPermission(Permissions.ALL)){
						announceMessage(args);
					}else{
						noPermission(player, "/announce message");
					}
				}else if(args[0].equalsIgnoreCase("start")){
					if(sender.hasPermission(Permissions.START) || sender.hasPermission(Permissions.ALL)){
						announceStart(args, player);
					}else{
						noPermission(player, "/announce start");
					}
				}else if(args[0].equalsIgnoreCase("stop")){
					if(sender.hasPermission(Permissions.STOP) || sender.hasPermission(Permissions.ALL)){
						announceStop(args, player);
					}else{
						noPermission(player, "/announce stop");
					}
				}else if(args[0].equalsIgnoreCase("status")){
					if(sender.hasPermission(Permissions.STATUS) || sender.hasPermission(Permissions.ALL)){
						announceStatus(player);
					}else{
						noPermission(player, "/announce status");
					}
				}else if(args[0].equalsIgnoreCase("start")){
					
				}else if(args[0].equalsIgnoreCase("stop")){
					
				}else if(args[0].equalsIgnoreCase("status")){
					
				}
				else{
					announceUnknown(player);
				}
			}else if(length == 0){
				announceVersion(player);
			}
			return true;
			
		}
		
		return false;	
		
	}
	
	public void announceInterval(String[] args, Player player){
		int length = args.length;
		
		if(length == 2){
			if(NumberUtils.isNumber(args[1])){
				config.set("interval", Integer.parseInt(args[1]));
				player.sendMessage(ChatColor.BLUE + "Announce> " + ChatColor.GRAY + "Interval of " + ChatColor.YELLOW +  Integer.parseInt(args[1]) + " seconds" + ChatColor.GRAY + " added successfully." );
				setInterval();
			}else{
				player.sendMessage(ChatColor.BLUE + "Announce> " + ChatColor.RED + "Error: " + ChatColor.GRAY + "The " + ChatColor.YELLOW + "interval " + ChatColor.GRAY + "must be a number.");
			}
		}else{
			incorrectArgs(player, "/announce interval", args.length, 1);
		}
	}
	
	public void announceRemove(String[] args, Player player){
		int length = args.length;
		
		if(length == 2){
			boolean success = false;
			
			for(String key : config.getConfigurationSection("announcements").getKeys(true)){
				if(key.equalsIgnoreCase(args[1])){
					config.set("announcements." + key, null);
					player.sendMessage(ChatColor.BLUE + "Announce> " + ChatColor.GRAY + "Announcement " + ChatColor.YELLOW + key + ChatColor.GRAY + " removed.");
					
					int position = config.getInt("position");
					config.set("position", position - 1);
					
					success = true;
					saveConfig();
					
					announcementsList = getList();
					
					break;
				}
			}
			if(!success){
				player.sendMessage(ChatColor.BLUE + "Announce> " + ChatColor.RED + "Error: " + ChatColor.GRAY + "Unable to remove announcement " + ChatColor.YELLOW + args[1] + ChatColor.GRAY + ".");
			}
		}else{
			incorrectArgs(player, "/announce remove", args.length, 1);
		}
	}
	
	public void announceRemoveAll(String[] args, Player player){
		int announcementsRemoved = 0;
		for(String key : config.getConfigurationSection("announcements").getKeys(true)){
					config.set("announcements." + key, null);

					config.set("position", 0);
					
					announcementsRemoved++;
					
					saveConfig();
					announcementsList = getList();
					setInterval();
			}
			if(announcementsRemoved > 0){
				player.sendMessage(ChatColor.BLUE + "Announce> " + ChatColor.GRAY + "All " + ChatColor.YELLOW + announcementsRemoved + ChatColor.GRAY + " announcements" + ChatColor.GRAY + " removed.");	
			}else{
				player.sendMessage(ChatColor.BLUE + "Announce> " + ChatColor.YELLOW + "0" + ChatColor.GRAY + " announcements" + ChatColor.GRAY + " to remove.");	
			}
	}
	
	public void announceAdd(String[] args, Player player){
		int length = args.length;
		
		if(length > 2){
			String[] addArgs = Arrays.copyOfRange(args, 2, args.length);
			String addName = args[1];
			
			String announcement = "";
			
			for(String argument : addArgs){
				announcement += " " + argument;
			}
			
			String path = "announcements." + addName;
			
			if(!config.contains(path)){
				config.set(path, colorize(announcement));
				saveConfig();
				
				player.sendMessage(ChatColor.BLUE + "Announce> " + ChatColor.GRAY + "Announcement " + ChatColor.YELLOW +  addName + ChatColor.GRAY + " added successfully." );
				
				announcementsList = getList();
				setInterval();
			}else{
				player.sendMessage(ChatColor.BLUE + "Announce> " + ChatColor.RED + "Error: " + ChatColor.GRAY + "You've already used this announcement name!");
			}
		}else{
			incorrectArgs(player, "/announce add", args.length, 2);
		}	
	}

	public void announceList(Player player){
		if((config.getConfigurationSection("announcements").getKeys(true)).size() > 0){ 
			player.sendMessage(ChatColor.BLUE + "Announce> " + ChatColor.GRAY + "Available " + ChatColor.YELLOW + "announcements" + ChatColor.GRAY + ":");
			for(String key : config.getConfigurationSection("announcements").getKeys(true)){
				String announcement = config.getString("announcements." + key);
				player.sendMessage(ChatColor.BLUE + "> " + ChatColor.YELLOW + key + ChatColor.GRAY + " - \"" + ChatColor.WHITE + announcement.substring(1, announcement.length()) + ChatColor.GRAY + "\"");
			}
		}else{
			player.sendMessage(ChatColor.BLUE + "Announce> " + ChatColor.GRAY + "No available " + ChatColor.YELLOW + "announcements" + ChatColor.GRAY + ".");
		}
	}
	
	public void announceHelp(Player player){
		player.sendMessage(ChatColor.BLUE + "Announce> " + ChatColor.GRAY + "Available " + ChatColor.YELLOW + "/announce" + ChatColor.GRAY + " commands:");
		player.sendMessage(ChatColor.BLUE + "> " + ChatColor.YELLOW + "/announce <null> | info | version" + ChatColor.GRAY + ": Displays plugin version information.");
		player.sendMessage(ChatColor.BLUE + "> " + ChatColor.YELLOW + "/announce interval <time>" + ChatColor.GRAY + ": Changes the interval between announcements (in seconds).");
		player.sendMessage(ChatColor.BLUE + "> " + ChatColor.YELLOW + "/announce add <name> <message>" + ChatColor.GRAY + ": Adds a message to the announcements list.");
		player.sendMessage(ChatColor.BLUE + "> " + ChatColor.YELLOW + "/announce prefix <prefix>" + ChatColor.GRAY + ": Changes the prefix before announcements.");
		player.sendMessage(ChatColor.BLUE + "> " + ChatColor.YELLOW + "/announce remove <name>" + ChatColor.GRAY + ": Removes a message from the announcements list.");
		player.sendMessage(ChatColor.BLUE + "> " + ChatColor.YELLOW + "/announce removeall" + ChatColor.GRAY + ": Removes all announcements at once.");
		player.sendMessage(ChatColor.BLUE + "> " + ChatColor.YELLOW + "/announce message <message>" + ChatColor.GRAY + ": Instantly announces the chosen message with your prefix.");
		player.sendMessage(ChatColor.BLUE + "> " + ChatColor.YELLOW + "/announce stop" + ChatColor.GRAY + ": Stops announcements, if started.");
		player.sendMessage(ChatColor.BLUE + "> " + ChatColor.YELLOW + "/announce start" + ChatColor.GRAY + ": Starts announcements, if stopped. Also resets timer.");
		player.sendMessage(ChatColor.BLUE + "> " + ChatColor.YELLOW + "/announce list" + ChatColor.GRAY + ": Shows all announcements in the list.");
		player.sendMessage(ChatColor.BLUE + "> " + ChatColor.YELLOW + "/announce help" + ChatColor.GRAY + ": Shows you this list!");
	}

	public void announceVersion(Player player){
		player.sendMessage(ChatColor.BLUE + "Announce> " + ChatColor.GRAY + "Announcements version " + ChatColor.YELLOW + "" + this.getDescription().getVersion() + ChatColor.GRAY +" by " + ChatColor.YELLOW + "Artix" + ChatColor.GRAY + " is active." );
	}
	
	public void announceUnknown(Player player){
		player.sendMessage(ChatColor.BLUE + "Announce> " + ChatColor.RED + "Error: " + ChatColor.GRAY + "Unknown Sub-command.");
	}
	
	public void announcePrefix(String[] args, Player player){
		int length = args.length;
		
		if(length > 1){
			String[] prefixArgs = Arrays.copyOfRange(args, 1, args.length);
			
			String prefix = "";
			
			for(String argument : prefixArgs){
				prefix += " " + argument;
			}
			
			String path = "prefix";
			
			config.set(path, colorize(prefix));
			saveConfig();
				
			player.sendMessage(ChatColor.BLUE + "Announce> " + ChatColor.GRAY + "Prefix " +  prefix + ChatColor.GRAY + " added successfully." );
		}else{
			incorrectArgs(player, "/announce prefix", args.length, 1);
		}	
	}

	public void announceMessage(String[] args){
		String[] messageArgs = Arrays.copyOfRange(args, 1, args.length);
		String announcement = "";
		
		for(String argument : messageArgs){
			announcement += " " + argument;
		}
		broadcastAnnouncement(announcement);
	}

	public void announceStop(String[] args, Player player){
		if(!announcementsRunning){
			player.sendMessage(ChatColor.BLUE + "Announce> " + ChatColor.RED + "Error: " + ChatColor.GRAY + "Announcements are" + ChatColor.YELLOW + " already stopped" + ChatColor.GRAY + ".");
		}else{
			scheduler.cancelTasks(this);
			player.sendMessage(ChatColor.BLUE + "Announce> " + ChatColor.GRAY + "Announcements " + ChatColor.YELLOW + "stopped " + ChatColor.GRAY + "successfully.");
			
			config.set("running", false);
			saveConfig();
			announcementsRunning = false;
		}
	}
	
	public void announceStart(String[] args, Player player){
		if(announcementsRunning){
			player.sendMessage(ChatColor.BLUE + "Announce> " + ChatColor.RED + "Error: " + ChatColor.GRAY + "Announcements are" + ChatColor.YELLOW + " already started" + ChatColor.GRAY + ".");
		}else{
			setInterval();
			player.sendMessage(ChatColor.BLUE + "Announce> " + ChatColor.GRAY + "Announcements " + ChatColor.YELLOW + "started " + ChatColor.GRAY + "successfully with an interval of " + ChatColor.YELLOW + config.getInt("interval") +  " seconds" + ChatColor.GRAY + ".");
			
			config.set("running", true);
			saveConfig();
			announcementsRunning = true;
			
		}
	}
	
	public void announceStatus(Player player){
		int interval = config.getInt("interval");
		int position = config.getInt("position");
		String prefix = config.getString("prefix");
		prefix = prefix.substring(1, prefix.length());
		String announcementStatus = (announcementsRunning)? "running" : "stopped";

		if(announcementsList.size() > 0){
			String announcement = announcementsList.get(position);
			announcement = announcement.substring(1, announcement.length());
			player.sendMessage(ChatColor.BLUE + "Announce> " + ChatColor.GRAY + "Announcements are currently " + ChatColor.YELLOW + announcementStatus + ChatColor.GRAY + ", with an interval of " + ChatColor.YELLOW + interval + " seconds" + ChatColor.GRAY + ". The next announcement will be:");
			player.sendMessage(ChatColor.BLUE + "> " + ChatColor.GRAY + "\"" + ChatColor.WHITE + colorize(announcement) + ChatColor.GRAY + "\"");
		}else{
			player.sendMessage(ChatColor.BLUE + "Announce> " + ChatColor.GRAY + "Announcements are currently " + ChatColor.YELLOW + announcementStatus + ChatColor.GRAY + ", with an interval of " + ChatColor.YELLOW + interval + " seconds" + ChatColor.GRAY + ". There are currently " + ChatColor.YELLOW + "no scheduled announcements" + ChatColor.GRAY + ".");
		}
	}
	
	public void noPermission(Player player, String command){
		player.sendMessage(ChatColor.BLUE + "Announce> " + ChatColor.RED + "Error: " + ChatColor.GRAY + "You do not have permission to perform " + ChatColor.YELLOW + command + ChatColor.GRAY + ".");
	}
	
	public void incorrectArgs(Player player, String command, int arguments, int requiredArguments){
		player.sendMessage(ChatColor.BLUE + "Announce> " + ChatColor.RED + "Error: " + ChatColor.GRAY + "You entered " + ChatColor.YELLOW + (arguments - 1) + ChatColor.GRAY + " arguments. " + ChatColor.YELLOW + command + ChatColor.GRAY + " requires " + ChatColor.YELLOW + requiredArguments + ChatColor.GRAY + ".");
	}
	
	public ArrayList<String> getList(){
		
		ArrayList<String> announcements = new ArrayList<String>();
		
		for(String key : config.getConfigurationSection("announcements").getKeys(false)){
			announcements.add(config.getString("announcements." + key));
		}
		return announcements;
	}

	public String colorize(String msg)
    {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
	
	public void broadcastAnnouncement(String announcement){
		String prefix = config.getString("prefix");
		prefix = prefix.substring(1, prefix.length());
		
		getServer().broadcastMessage(ChatColor.WHITE + colorize(prefix) + ChatColor.WHITE + colorize(announcement));
	}
	
	public void setInterval(){
		if(announcementsList.size() != 0){
			int interval = config.getInt("interval");
			
			scheduler.cancelTasks(this);
			scheduler.scheduleSyncRepeatingTask(this, new Runnable(){
				public void run(){
					position = config.getInt("position");
					
					if(announcementsList.size() > 0){
						broadcastAnnouncement(announcementsList.get(position));
						config.set("running", true);
					}else{
						config.set("running", false);
					}
	
					position++;
					
					if(position >= announcementsList.size()){
						position = 0;
					}
					
					config.set("position", position);
					saveConfig();
					
				}
			}, interval * 20L, interval * 20L);
		}
	}
}
