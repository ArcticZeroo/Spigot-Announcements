# Spigot-Announcements
This is a relatively simple announcements plugin I created last night and today (it's my first actual plugin, thank you very much) that does basic server announcements.

The code is definitely messy since this is one of my first ventures in Java in general, and is my first completed Bukkit plugin.

I made this because apparently other announcement plugins break in 1.9, and I wanted some Java/Bukkit practice.

Source for the plugin is available on 

#Features:
Works on 1.9 (apparently some others don't for some reason)
Customizable prefix (command & config)
Customizable message title and content (command & config)
Customizable interval between messages
Supports chat colors via &<x> when customizing
Everything can be edited either ingame or through config.yml

#Commands:
/announce <null> | info | version
Displays plugin version information.

/announce interval <time>
Changes the interval between announcements.

/announce add <name> <message>
Adds a message to the announcements list.

/announce prefix <prefix>
Changes the prefix before announcements.

/announce remove <name>
Removes a message from the announcements list.

/announce removeall
Removes all announcements at once.

/announce list
Shows all announcements in the list.

/announce help
Shows all available commands.

#Permissions:
permission - command

announce.add - /announce add
announce.prefix - /announce prefix
announce.remove- /announce remove
announce.list - /announce list
announce.interval - /announce interval
announce.help - /announce help
announce.* - /announce *
