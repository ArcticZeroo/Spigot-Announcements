package artix.announcements;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

public class AnnounceCommands{
	
	private AnnounceMain plugin;
	
	public AnnounceCommands(AnnounceMain plugin){
		this.plugin = plugin;
	}
	
	FileConfiguration config = plugin.config;
	int position = config.getInt("position");
	ArrayList<String> announcementsList = new ArrayList<String>();
	boolean announcementsRunning = config.getBoolean("running");
	BukkitScheduler scheduler = plugin.getServer().getScheduler();
	
	public final String INTERVAL = "interval";
	public final String PREFIX = "prefix";
	public final String ADD = "add";
	public final String REMOVE = "remove";
	public final String REMOVEALL = "removeall";
	public final String LIST = "list";
	public final String HELP = "help";
	public final String VERSION = "version";
	public final String MESSAGE = "message";
	public final String STOP = "stop";
	public final String START = "stop";
	public final String STATUS = "status";
	
	public void interval(String[] args, Player player){
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
	
	public void remove(String[] args, Player player){
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
					plugin.saveConfig();
					
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
	
	public void removeall(String[] args, Player player){
		int announcementsRemoved = 0;
		for(String key : config.getConfigurationSection("announcements").getKeys(true)){
					config.set("announcements." + key, null);

					config.set("position", 0);
					
					announcementsRemoved++;
					
					plugin.saveConfig();
					announcementsList = getList();
					setInterval();
			}
			if(announcementsRemoved > 0){
				player.sendMessage(ChatColor.BLUE + "Announce> " + ChatColor.GRAY + "All " + ChatColor.YELLOW + announcementsRemoved + ChatColor.GRAY + " announcements" + ChatColor.GRAY + " removed.");	
			}else{
				player.sendMessage(ChatColor.BLUE + "Announce> " + ChatColor.YELLOW + "0" + ChatColor.GRAY + " announcements" + ChatColor.GRAY + " to remove.");	
			}
	}
	
	public void add(String[] args, Player player){
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
				plugin.saveConfig();
				
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

	public void list(Player player){
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
	
	public void help(Player player){
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

	public void version(Player player){
		player.sendMessage(ChatColor.BLUE + "Announce> " + ChatColor.GRAY + "Announcements version " + ChatColor.YELLOW + "" + plugin.getDescription().getVersion() + ChatColor.GRAY +" by " + ChatColor.YELLOW + "Artix" + ChatColor.GRAY + " is active." );
	}
	
	public void unknown(Player player){
		player.sendMessage(ChatColor.BLUE + "Announce> " + ChatColor.RED + "Error: " + ChatColor.GRAY + "Unknown Sub-command.");
	}
	
	public void prefix(String[] args, Player player){
		int length = args.length;
		
		if(length > 1){
			String[] prefixArgs = Arrays.copyOfRange(args, 1, args.length);
			
			String prefix = "";
			
			for(String argument : prefixArgs){
				prefix += " " + argument;
			}
			
			String path = "prefix";
			
			config.set(path, colorize(prefix));
			plugin.saveConfig();
				
			player.sendMessage(ChatColor.BLUE + "Announce> " + ChatColor.GRAY + "Prefix " +  prefix + ChatColor.GRAY + " added successfully." );
		}else{
			incorrectArgs(player, "/announce prefix", args.length, 1);
		}	
	}

	public void message(String[] args){
		String[] messageArgs = Arrays.copyOfRange(args, 1, args.length);
		String announcement = "";
		
		for(String argument : messageArgs){
			announcement += " " + argument;
		}
		broadcast(announcement);
	}

	public void stop(String[] args, Player player){
		if(!announcementsRunning){
			player.sendMessage(ChatColor.BLUE + "Announce> " + ChatColor.RED + "Error: " + ChatColor.GRAY + "Announcements are" + ChatColor.YELLOW + " already stopped" + ChatColor.GRAY + ".");
		}else{
			scheduler.cancelTasks(plugin);
			player.sendMessage(ChatColor.BLUE + "Announce> " + ChatColor.GRAY + "Announcements " + ChatColor.YELLOW + "stopped " + ChatColor.GRAY + "successfully.");
			
			config.set("running", false);
			plugin.saveConfig();
			announcementsRunning = false;
		}
	}
	
	public void start(String[] args, Player player){
		if(announcementsRunning){
			player.sendMessage(ChatColor.BLUE + "Announce> " + ChatColor.RED + "Error: " + ChatColor.GRAY + "Announcements are" + ChatColor.YELLOW + " already started" + ChatColor.GRAY + ".");
		}else{
			setInterval();
			player.sendMessage(ChatColor.BLUE + "Announce> " + ChatColor.GRAY + "Announcements " + ChatColor.YELLOW + "started " + ChatColor.GRAY + "successfully with an interval of " + ChatColor.YELLOW + config.getInt("interval") +  " seconds" + ChatColor.GRAY + ".");
			
			config.set("running", true);
			plugin.saveConfig();
			announcementsRunning = true;
			
		}
	}
	
	public void status(Player player){
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
	
	public void broadcast(String announcement){
		String prefix = config.getString("prefix");
		prefix = prefix.substring(1, prefix.length());
		
		plugin.getServer().broadcastMessage(ChatColor.WHITE + colorize(prefix) + ChatColor.WHITE + colorize(announcement));
	}
	
	public void setInterval(){
		if(announcementsList.size() != 0){
			int interval = config.getInt("interval");
			
			scheduler.cancelTasks(plugin);
			scheduler.scheduleSyncRepeatingTask(plugin, new Runnable(){
				public void run(){
					position = config.getInt("position");
					
					if(announcementsList.size() > 0){
						broadcast(announcementsList.get(position));
						config.set("running", true);
					}else{
						config.set("running", false);
					}
	
					position++;
					
					if(position >= announcementsList.size()){
						position = 0;
					}
					
					config.set("position", position);
					plugin.saveConfig();
					
				}
			}, interval * 20L, interval * 20L);
		}
	}
}