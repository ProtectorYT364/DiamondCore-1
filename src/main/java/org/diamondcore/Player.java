/*
 _______ _______ _______ _______ _______ _______ _______ _______ _______ _______ _______ 
|\     /|\     /|\     /|\     /|\     /|\     /|\     /|\     /|\     /|\     /|\     /|
| +---+ | +---+ | +---+ | +---+ | +---+ | +---+ | +---+ | +---+ | +---+ | +---+ | +---+ |
| |   | | |   | | |   | | |   | | |   | | |   | | |   | | |   | | |   | | |   | | |   | |
| |D  | | |i  | | |a  | | |m  | | |o  | | |n  | | |d  | | |C  | | |o  | | |r  | | |e  | |
| +---+ | +---+ | +---+ | +---+ | +---+ | +---+ | +---+ | +---+ | +---+ | +---+ | +---+ |
|/_____\|/_____\|/_____\|/_____\|/_____\|/_____\|/_____\|/_____\|/_____\|/_____\|/_____\|
                                                                                                 
*/

package org.diamondcore;

import java.net.Socket;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Used to represent a player and manage it easily
 * 
 * @author Trent Summerlin
 * @version 0.1.0-SNAPSHOT
 */
public final class Player {
	
	// Player list
	protected static ArrayList<Player> playerList = new ArrayList<Player>();
	protected static ArrayList<UUID> uuidList = new ArrayList<UUID>();
	public static Player[] getPlayers() {
		Player[] list = new Player[playerList.size()];
		for(int i = 0; i < list.length; i++)
			list[i] = playerList.get(i);
		return list;
	}
	
	// Basic player info
	protected String username;
	protected final UUID uuid;
	
	// Advance player info
	
	
	public Player(String username, UUID uuid) {
		// Set player info
		this.username = username;
		this.uuid = uuid;
		
		// Make sure player is not duplicate
		if(uuidList.contains(uuid)) {
			// Throw new exception
			return;
		}
		
		// Add player to list
		playerList.add(this);
		uuidList.add(uuid);
	}
	
	/**
	 * Used to get the player's name
	 * 
	 * @return Player name
	 * @author Trent Summerlin
	 */
	public final String getName() {
		return this.username;
	}
	
	/**
	 * Used to get the player's UUID
	 * 
	 * @return Player UUID
	 * @author Trent Summerlin
	 */
	public final UUID getUUID() {
		return this.uuid;
	}
	
	public final void disconnect(String reason) {
		// Disconnect player
	}
	
	/**
	 * Used to get the socket the player is using
	 * 
	 * @return Player socket
	 * @author Trent Summerlin
	 */
	protected final Socket getSocket() {
		// Return socket
		return null;
	}
	
}