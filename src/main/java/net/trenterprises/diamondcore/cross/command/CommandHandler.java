/*
 _______ _______ _______ _______ _______ _______ _______ _______ _______ _______ _______ 
|\     /|\     /|\     /|\     /|\     /|\     /|\     /|\     /|\     /|\     /|\     /|
| +---+ | +---+ | +---+ | +---+ | +---+ | +---+ | +---+ | +---+ | +---+ | +---+ | +---+ |
| |   | | |   | | |   | | |   | | |   | | |   | | |   | | |   | | |   | | |   | | |   | |
| |D  | | |i  | | |a  | | |m  | | |o  | | |n  | | |d  | | |C  | | |o  | | |r  | | |e  | |
| +---+ | +---+ | +---+ | +---+ | +---+ | +---+ | +---+ | +---+ | +---+ | +---+ | +---+ |
|/_____\|/_____\|/_____\|/_____\|/_____\|/_____\|/_____\|/_____\|/_____\|/_____\|/_____\|                                                                                                        

*/

package net.trenterprises.diamondcore.cross.command;

import java.lang.reflect.Method;
import java.util.ArrayList;

import net.trenterprises.diamondcore.cross.command.custom.Command;
import net.trenterprises.diamondcore.cross.command.diamond.PluginlistCommand;
import net.trenterprises.diamondcore.cross.command.diamond.ReloadCommand;
import net.trenterprises.diamondcore.cross.command.vanilla.BanCommand;
import net.trenterprises.diamondcore.cross.command.vanilla.HelpCommand;
import net.trenterprises.diamondcore.cross.command.vanilla.PardonCommand;
import net.trenterprises.diamondcore.cross.command.vanilla.SayCommand;
import net.trenterprises.diamondcore.cross.command.vanilla.StopCommand;
import net.trenterprises.diamondcore.cross.command.vanilla.TimeCommand;
import net.trenterprises.diamondcore.cross.logging.DiamondLogger;
import net.trenterprises.diamondcore.cross.logging.Log4j2Logger;

/**
 * Used to execute the commands which are received from the console input
 * 
 * @author Trent Summerlin
 * @version 1.0
 */
public final class CommandHandler {
	
	static DiamondLogger logger = new Log4j2Logger("CONSOLE");
	public final NativeCommand[] commandList = new NativeCommand[] {
			// Vanilla commands
			new BanCommand(this), new PardonCommand(this),
			new SayCommand(this), new HelpCommand(this),
			new TimeCommand(this), new StopCommand(this),
			// Vanilla (DiamondCore) comands
			new PluginlistCommand(this), new ReloadCommand(this)
	};
	
	private CommandHandler() {}
	
	/**
	 * Used to get the command type from a String
	 * 
	 * @param CommandManager (In string form)
	 * @return Command (In CommandType form)
	 * @author Trent Summerlin
	 * @version 1.0
	 */
	public static CommandType getByName(String RawCommand) {
		String Command = RawCommand.split(" ")[0];
		switch(Command) {
			// Server
			case "say":
				return CommandType.SAY;
			case "time":
				return CommandType.TIME;
			case "save-all":
				return CommandType.SAVE_ALL;
			case "save-on":
				return CommandType.SAVE_ON;
			case "save-off":
				return CommandType.SAVE_OFF;
			case "difficulty":
				return CommandType.DIFFICULTY;
			case "toggledownfall":
				return CommandType.TOGGLE_DOWNFALL;
			case "plugins":
				return CommandType.PLUGINS;
			case "reload":
				return CommandType.RELOAD;
			case "stop":
				return CommandType.STOP;
			
			// Opertator
			case "kick":
				return CommandType.KICK;
			case "ban":
				return CommandType.BAN;
			case "ban-ip":
				return CommandType.BAN_IP;
			case "pardon":
				return CommandType.PARDON;
			case "pardon-ip":
				return CommandType.PARDON_IP;
			case "banlist":
				return CommandType.BANLIST;
			case "whitelist":
				return CommandType.WHITELIST;
			case "op":
				return CommandType.OP;
			case "deop":
				return CommandType.DEOP;
						
			// Player Related
			case "teleport":
				return CommandType.TELEPORT;
			case "give":
				return CommandType.GIVE;
			case "gamemode":
				return CommandType.GAMEMODE;
			case "default-gamemode":
				return CommandType.DEFAULT_GAMEMODE;
			case "spawnpoint":
				return CommandType.SPAWNPOINT;
			
			// Other
			case "list":
				return CommandType.LIST;
			case "help":
				return CommandType.HELP;
			case "?":
				return CommandType.ALT_HELP;
			default:
				return CommandType.UNKNOWN;
		}
	}
	
	/**
	 * Used to get the command arguments from a command
	 * @param Command (In string form)
	 * @return Command arguments (in array form)
	 * @author Trent Summerlin
	 * @version 1.0
	 */
	public static String[] getArguments(String Command) {
		String[] args = Command.split(" ");
		args[0] = null;
		String[] newArgs = new String[args.length-1];
		for(int i = 0; i < args.length; i++) if(i > 0) newArgs[i-1] = args[i];
		return newArgs;
	}
	
	/**
	 * Used to execute a command
	 * 
	 * @param sender type
	 * @param CommandManager to run
	 * @param CommandManager arguments
	 * @author Trent Summerlin
	 * @version 1.0
	 */
	public static void run(CommandSender sender, String rawCommandLabel, String[] args) {
		String commandLabel = rawCommandLabel.split(" ")[0];
		CommandType commandType = getByName(commandLabel);
		if(commandType.equals(CommandType.SAY)) {
			SayCommand command = new SayCommand(args);
			boolean shouldRun = sender.equals(CommandSender.CONSOLE);
			if(!shouldRun) {
				// Check other things to see if should run should be true
			}
			if(shouldRun) command.execute(sender, logger);
		}
		else if(commandType.equals(CommandType.TIME)) {
			TimeCommand command = new TimeCommand(args);
			boolean shouldRun = sender.equals(CommandSender.CONSOLE);
			if(shouldRun) command.execute(sender, logger);
		}
		else if(commandType.equals(CommandType.SAVE_ALL)) {
			
		}
		else if(commandType.equals(CommandType.SAVE_ON)) {
			
		}
		else if(commandType.equals(CommandType.SAVE_OFF)) {
			
		}
		else if(commandType.equals(CommandType.DIFFICULTY)) {
			
		}
		else if(commandType.equals(CommandType.TOGGLE_DOWNFALL)) {
			
		}
		else if(commandType.equals(CommandType.PLUGINS)) {
			PluginlistCommand command = new PluginlistCommand();
			command.execute(sender, logger);
		}
		else if(commandType.equals(CommandType.RELOAD)) {
			ReloadCommand command = new ReloadCommand();
			boolean shouldRun = sender.equals(CommandSender.CONSOLE);
			if(shouldRun) command.execute(sender, logger);
		}
		else if(commandType.equals(CommandType.STOP)) {
			StopCommand command = new StopCommand();
			boolean shouldRun = sender.equals(CommandSender.CONSOLE);
			if(shouldRun) command.execute(sender, logger);
		}
		else if(commandType.equals(CommandType.KICK)) {
			
		}
		else if(commandType.equals(CommandType.BAN)) {
			BanCommand command = null;
			boolean shouldRun = sender.equals(CommandSender.CONSOLE);
			if(!shouldRun) {
				// Check other things to see if should run should be true
			}
			if(shouldRun) {
				if(args.length == 1) command = new BanCommand(args[0]);
				else if(args.length == 2) command = new BanCommand(args[0], args[1]);
				command.execute(sender, logger);
			} else {
				if(sender.equals(CommandSender.POCKETPLAYER)) {
					// Send PE packet result
				} else if(sender.equals(CommandSender.DESKTOPPLAYER)) {
					// Send PC packet result
				}
			}
		}
		else if(commandType.equals(CommandType.BAN_IP)) {
			
		}
		else if(commandType.equals(CommandType.PARDON)) {
			if(args.length == 1) {
				PardonCommand command = new PardonCommand(args[0]);
				boolean shouldRun = sender.equals(CommandSender.CONSOLE);
				if(!shouldRun) {
					// Check if it should be true
				}
				if(shouldRun) command.execute(sender, logger); 
			}
			else {
				logger.err("INSUFFICIENT ARGS");
			}
		}
		else if(commandType.equals(CommandType.PARDON_IP)) {
			
		}
		else if(commandType.equals(CommandType.BANLIST)) {
			
		}
		else if(commandType.equals(CommandType.WHITELIST)) {
			
		}
		else if(commandType.equals(CommandType.OP)) {
			
		}
		else if(commandType.equals(CommandType.DEOP)) {
			
		}
		else if(commandType.equals(CommandType.TELEPORT)) {
			
		}
		else if(commandType.equals(CommandType.GIVE)) {
			
		}
		else if(commandType.equals(CommandType.GAMEMODE)) {
			
		}
		else if(commandType.equals(CommandType.DEFAULT_GAMEMODE)) {
			
		}
		else if(commandType.equals(CommandType.SPAWNPOINT)) {
			
		}
		else if(commandType.equals(CommandType.LIST)) {
			
		}
		else if(commandType.equals(CommandType.HELP) || commandType.equals(CommandType.ALT_HELP)) {
			HelpCommand command = new HelpCommand(new CommandHandler().commandList);
			command.execute(sender, logger);
		}
		else {
			// Make sure the command entered isn't a custom command from a plugin
			ArrayList<Command> commands = Command.commands;
			boolean foundCommand = false;
			for(Command command : commands) {
				if(command.getName().equalsIgnoreCase(commandLabel)) {
					try {
						/*JavaSession session = command.getPluginSession();
						Method toInvoke = null;
						
						/*
						 * Try and load the executor as it was from the main class of the plugin.
						 * If an exception is thrown, it's alright. As it's likely the command is
						 * from another class.
						
						try {
							toInvoke = session.getClassFromPlugin(
							session.getMainClass()).getMethod("onCommand", 
							sender.getClass(), commandLabel.getClass(), args.getClass());
						}
						catch(Exception e) {
							toInvoke = null; 
						}*/
						Method toInvoke = null;
						if(command.getExecutor() != null) toInvoke = command.getExecutor().getClass().getMethod("onCommand", sender.getClass(), commandLabel.getClass(), args.getClass());
						toInvoke.invoke(command.getExecutor(), sender, commandLabel, args);
						foundCommand = true;
					} catch(Exception e) {
						foundCommand = false;
					}
					break;
				}
			}
			if(!foundCommand) logger.info("Unknown command!");
		}
	}
}