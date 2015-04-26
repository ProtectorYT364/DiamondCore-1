/*
 _______ _______ _______ _______ _______ _______ _______ _______ _______ _______ _______ 
|\     /|\     /|\     /|\     /|\     /|\     /|\     /|\     /|\     /|\     /|\     /|
| +---+ | +---+ | +---+ | +---+ | +---+ | +---+ | +---+ | +---+ | +---+ | +---+ | +---+ |
| |   | | |   | | |   | | |   | | |   | | |   | | |   | | |   | | |   | | |   | | |   | |
| |D  | | |i  | | |a  | | |m  | | |o  | | |n  | | |d  | | |C  | | |o  | | |r  | | |e  | |
| +---+ | +---+ | +---+ | +---+ | +---+ | +---+ | +---+ | +---+ | +---+ | +---+ | +---+ |
|/_____\|/_____\|/_____\|/_____\|/_____\|/_____\|/_____\|/_____\|/_____\|/_____\|/_____\|                                                                                                        
 
*/

package net.trenterprises.diamondcore;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;

import net.trenterprises.diamondcore.cross.Ticker;
import net.trenterprises.diamondcore.cross.api.PluginLoader;
import net.trenterprises.diamondcore.cross.api.html.HTMLLoader;
import net.trenterprises.diamondcore.cross.command.custom.exception.InvalidCommandException;
import net.trenterprises.diamondcore.cross.console.ConsoleInputReader;
import net.trenterprises.diamondcore.cross.file.FileCheckup;
import net.trenterprises.diamondcore.cross.file.FileList;
import net.trenterprises.diamondcore.cross.file.PropertiesCheckup;
import net.trenterprises.diamondcore.cross.logging.DiamondLogger;
import net.trenterprises.diamondcore.cross.logging.Log4j2Logger;
import net.trenterprises.diamondcore.cross.settings.ServerSettings;
import net.trenterprises.diamondcore.cross.world.time.WorldTime;
import net.trenterprises.diamondcore.desktop.network.DesktopPacketHandler;
import net.trenterprises.diamondcore.pocket.network.PocketPacketHandler;

/* NOTE: In order to load the server in debug mode in eclipse,
 * go to run configurations and add "true" in arguments! */

/**
 * This is the second-to-main class for the software
 * and can only be called by the "run" class.
 */
public class DiamondCoreServer {
	
	// Running Variable
	protected static boolean running = false;
	protected static boolean debug = false;
	
	// Logger
	protected static DiamondLogger Logger = new Log4j2Logger("DiamondCore");
	
	// Threads
	protected static PocketPacketHandler pocketPacketHandler;
	protected static DatagramSocket pocketSocket;
	protected static DesktopPacketHandler desktopPacketHandler;
	protected static ServerSocket desktopSocket;
	
	public DiamondCoreServer(boolean shouldDebug) throws IOException, InterruptedException, InvalidCommandException {
		debug = shouldDebug;
		Logger.info("Starting Pocket Server!");
		Logger.info("Debug: " + (debug ? "activated" : "de-activated"));
		
		// Check Files and Properties
		FileList.setDebug(shouldDebug);
		new FileCheckup();
		new PropertiesCheckup();
		
		// Load Server Settings
		ServerSettings.load();
		
		// Open Socket
		pocketSocket = new DatagramSocket(ServerSettings.getPEPort());
		desktopSocket = new ServerSocket(ServerSettings.getPCPort());
		
		// Load plugins
		PluginLoader.loadPlugins();
		HTMLLoader.loadPlugins();
		
		// Initialize everything
		ConsoleInputReader consoleInput = new ConsoleInputReader();
		
		// Finish startup
		new PacketHandlerThread(this, consoleInput).start();
		new MainTicker().start();
		
		running = true;
		Logger.info("Started Server!");
	}
	
	/**
	 * Used to see if the DiamondCore server software is running
	 * 
	 * @author Trent Summerlin
	 * @version 1.0
	 * @return DiamondCore server running state
	 */
	public static boolean isRunning() {
		return running;
	}
	
	/**
	 * Used to see if the server is running in debug mode
	 * 
	 * @author Trent Summerlin
	 * @version 1.0
	 * @return Server debug state
	 */
	public static boolean isDebug() {
		return debug;
	}
	
	/**
	 * Used to retrieve the MCPE server socket so it can be maganged
	 * 
	 * @author Trent Summerlin
	 * @version 1.0
	 * @return Server MCPE socket
	 */
	public static DatagramSocket getPocketSocket() {
		return pocketSocket;
	}
	
}

class MainTicker extends Thread implements Runnable {
	// Initialize everything before startup
	private Ticker ticker = new Ticker(20);
	private DiamondLogger logger = new Log4j2Logger("CONSOLE");
	
	public void run() {
		ticker.start();
		int lastTick = -1;
		int ticksRanSlow = -1;
		int tickReset = 1200;
		boolean runningSlow = false;
		while(DiamondCoreServer.isRunning()) {
			int currentTick = ticker.getTick();
			if(currentTick != lastTick) {
				WorldTime.tick();
				if(currentTick > lastTick+1) {
					int skippedTicks = (currentTick - lastTick - 1); /* Subtract 1 more because the current tick will ALWAYS be 1 or more than the last one */
					if(!runningSlow || ticksRanSlow >= tickReset) logger.info("Can't keep up! Did the system time change, or is the server overloaded? Skipping " + skippedTicks + " ticks");
					if(ticksRanSlow >= tickReset) ticksRanSlow = 0;
					runningSlow = true;
				}
				if(runningSlow) ticksRanSlow++;
				else {
					ticksRanSlow = -1;
					runningSlow = false;
				}
				lastTick = currentTick;
			}
			if(!DiamondCoreServer.isRunning()) PluginLoader.unloadPlugins();
		}
	}
}

// Packet Thread
class PacketHandlerThread extends Thread implements Runnable {
	private DiamondCoreServer server;
	private ConsoleInputReader consoleInput = null;

	public PacketHandlerThread(DiamondCoreServer server, ConsoleInputReader consoleInput){
		this.server = server;
		this.consoleInput = consoleInput;
	}

	public void run() {
		try {
			DiamondCoreServer.pocketPacketHandler = new PocketPacketHandler(DiamondCoreServer.pocketSocket, server);
			DiamondCoreServer.pocketPacketHandler.start();
			DiamondCoreServer.desktopPacketHandler = new DesktopPacketHandler(DiamondCoreServer.desktopSocket, server);
			DiamondCoreServer.desktopPacketHandler.start();
			while(true) {
				consoleInput.tick();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
