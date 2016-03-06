# Spigot-Announcements
This is a relatively simple announcements plugin I created last night and today (it's my first actual plugin, thank you very much) that does basic server announcements.

The code is definitely messy since this is one of my first ventures in Java in general, and is my first completed Bukkit plugin.

I made this because apparently other announcement plugins break in 1.9, and I wanted some Java/Bukkit practice.

Source for the plugin is available on 

#Features:
- Works on 1.9 (apparently some others don't for some reason)
- Customizable prefix (command & config)
- Customizable message title and content (command & config)
- Customizable interval between messages
- Supports chat colors via &<x> when customizing
- Everything can be edited either ingame or through config.yml

#Commands:
/announce <null> | info | version

_Displays plugin version information._

/announce interval <time>

_Changes the interval between announcements._

/announce add <name> <message>

_Adds a message to the announcements list._

/announce prefix <prefix>

_Changes the prefix before announcements._

/announce remove <name>

_Removes a message from the announcements list._

/announce removeall

_Removes all announcements at once._

/announce list

_Shows all announcements in the list._

/announce help

_Shows all available commands._

#Permissions:
permission - command

- announce.add - /announce add
- announce.prefix - /announce prefix
- announce.remove- /announce remove
- announce.list - /announce list
- announce.interval - /announce interval
- announce.help - /announce help
- announce.* - /announce *
