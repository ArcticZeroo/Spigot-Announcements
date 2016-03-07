package artix.announcements;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class AnnouncementMain extends JavaPlugin{

	//Initial Declarations
	FileConfiguration config = this.getConfig();
	ArrayList<String> announcementsList = new ArrayList<String>();
	boolean announcementsRunning = config.getBoolean("running");
	private AnnouncementCommands command = new AnnouncementCommands(this);
	//End Initial Declarations
	
	@Override
	public void onEnable() {
		//new ListenerClass(this);
		config.addDefault("interval", 120);
		config.addDefault("position", 0);
		config.addDefault("prefix", command.colorize("&9Announcement>"));
		config.addDefault("running", true);
		config.options().copyDefaults(true);
		
		announcementsList = command.getList();
		
		if(announcementsList.size() > 0 && announcementsRunning){
			command.setInterval();
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
				if(args[0].equalsIgnoreCase(command.INTERVAL)){
					if(sender.hasPermission(AnnouncementPermissions.INTERVAL) || sender.hasPermission(AnnouncementPermissions.ALL)){
						command.interval(args, player);
					}else{
						command.noPermission(player, "/announce " + command.INTERVAL);
					}
				}else if(args[0].equalsIgnoreCase(command.HELP)){
					if(sender.hasPermission(AnnouncementPermissions.HELP) || sender.hasPermission(AnnouncementPermissions.ALL)){
						command.help(player);
					}else{
						command.noPermission(player, "/announce " + command.HELP);
					}
				}else if(args[0].equalsIgnoreCase(command.ADD)){
					if(sender.hasPermission(AnnouncementPermissions.ADD) || sender.hasPermission(AnnouncementPermissions.ALL)){
						command.add(args, player);
					}else{
						command.noPermission(player, "/announce " + command.ADD);
					}
				}else if(args[0].equalsIgnoreCase(command.LIST)){
					if(sender.hasPermission(AnnouncementPermissions.LIST) || sender.hasPermission(AnnouncementPermissions.ALL)){
						command.list(player);
					}else{
						command.noPermission(player, "/announce " + command.LIST);
					}
				}else if(args[0].equalsIgnoreCase(command.REMOVE)){
					if(sender.hasPermission(AnnouncementPermissions.REMOVE) || sender.hasPermission(AnnouncementPermissions.ALL)){
						command.remove(args, player);
					}else{
						command.noPermission(player, "/announce " + command.REMOVE);
					}
				
				}else if(args[0].equalsIgnoreCase(command.REMOVEALL)){
					if(sender.hasPermission(AnnouncementPermissions.REMOVE) || sender.hasPermission(AnnouncementPermissions.ALL)){
						command.removeall(args, player);
					}else{
						command.noPermission(player, "/announce " + command.REMOVEALL);
					}
				
				}else if(args[0].equalsIgnoreCase(command.PREFIX)){
					if(sender.hasPermission(AnnouncementPermissions.PREFIX) || sender.hasPermission(AnnouncementPermissions.ALL)){
						command.prefix(args, player);
					}else{
						command.noPermission(player, "/announce " + command.PREFIX);
					}
				}else if(args[0].equalsIgnoreCase("info") || args[0].equalsIgnoreCase("version")){
					command.version(player);
				}else if(args[0].equalsIgnoreCase(command.MESSAGE)){
					if(sender.hasPermission(AnnouncementPermissions.MESSAGE) || sender.hasPermission(AnnouncementPermissions.ALL)){
						command.message(args);
					}else{
						command.noPermission(player, "/announce " + command.MESSAGE);
					}
				}else if(args[0].equalsIgnoreCase(command.START)){
					if(sender.hasPermission(AnnouncementPermissions.START) || sender.hasPermission(AnnouncementPermissions.ALL)){
						command.start(args, player);
					}else{
						command.noPermission(player, "/announce " + command.START);
					}
				}else if(args[0].equalsIgnoreCase(command.STOP)){
					if(sender.hasPermission(AnnouncementPermissions.STOP) || sender.hasPermission(AnnouncementPermissions.ALL)){
						command.stop(args, player);
					}else{
						command.noPermission(player, "/announce " + command.STOP);
					}
				}else if(args[0].equalsIgnoreCase(command.STATUS)){
					if(sender.hasPermission(AnnouncementPermissions.STATUS) || sender.hasPermission(AnnouncementPermissions.ALL)){
						command.status(player);
					}else{
						command.noPermission(player, "/announce " + command.STATUS);
					}
				}
				else{
					command.unknown(player);
				}
			}else if(length == 0){
				command.version(player);
			}
			return true;
			
		}
		
		return false;	
		
	}

}
