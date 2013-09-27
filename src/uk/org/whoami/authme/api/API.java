package uk.org.whoami.authme.api;

import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import uk.org.whoami.authme.AuthMe;
import uk.org.whoami.authme.Utils;
import uk.org.whoami.authme.cache.auth.PlayerAuth;
import uk.org.whoami.authme.cache.auth.PlayerCache;
import uk.org.whoami.authme.datasource.DataSource;
import uk.org.whoami.authme.security.PasswordSecurity;
import uk.org.whoami.authme.settings.Settings;

public class API {

	public static AuthMe instance;
	public static DataSource database;

	public API(AuthMe instance, DataSource database) {
		API.instance = instance;
		API.database = database;
	}
	/**
	 * Hook into AuthMe
	 * @return AuthMe instance
	 */
    public static AuthMe hookAuthMe() {
    	Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("AuthMe");
        if (plugin == null && !(plugin instanceof AuthMe)) {
        	return null;
         }
    	return (AuthMe) plugin;
    }


	public AuthMe getPlugin() {
    	return instance;
    }

    /**
     * 
     * @param player
     * @return true if player is authenticate
     */
    public static boolean isAuthenticated(Player player) {
    	return PlayerCache.getInstance().isAuthenticated(player.getName().toLowerCase());
    }

    /**
     * 
     * @param player
     * @return true if player is a npc
     */
    public boolean isaNPC(Player player) {
    	return instance.getCitizensCommunicator().isNPC(player, instance);
    }

    /**
     * 
     * @param player
     * @return true if the player is unrestricted
     */
    public static boolean isUnrestricted(Player player) {
    	return Utils.getInstance().isUnrestricted(player);
    }

    public static Location getLastLocation(Player player) {
    	try {
    		PlayerAuth auth = PlayerCache.getInstance().getAuth(player.getName().toLowerCase());
        	
        	if (auth != null) {
        		Location loc = new Location(Bukkit.getWorld(auth.getWorld()), auth.getQuitLocX(), auth.getQuitLocY() , auth.getQuitLocZ());
        		return loc;
        	} else {
        		return null;
        	}
        	
    	} catch (NullPointerException ex) {
    		return null;
    	}
    }

    public static void setPlayerInventory(Player player, ItemStack[] content, ItemStack[] armor) {
    	try {
        	player.getInventory().setContents(content);
        	player.getInventory().setArmorContents(armor);
    	} catch (NullPointerException npe) {
    	}
    }
    
    /**
     * 
     * @param playerName
     * @return true if player is registered
     */
    public static boolean isRegistered(String playerName) {
    	String player = playerName.toLowerCase();
    	if (database.isAuthAvailable(player))
    		return true;
    	return false;
    }
    
    /**
     * @param String playerName, String passwordToCheck
     * @return true if the password is correct , false else
     */
    public static boolean checkPassword(String playerName, String passwordToCheck) {
    	if (!isRegistered(playerName.toLowerCase())) return false;
    	String player = playerName.toLowerCase();
    	PlayerAuth auth = database.getAuth(player);
    	try {
			return PasswordSecurity.comparePasswordWithHash(passwordToCheck, auth.getHash(), player);
		} catch (NoSuchAlgorithmException e) {
			return false;
		}
    }
    
    /**
     * Register a player
     * @param String playerName, String password
     * @return true if the player is register correctly
     */
    public static boolean registerPlayer(String playerName, String password) {
        return registerPlayer(playerName, password, "");
    }
    
    /**
     * Register a player
     * @param String playerName, String password, String email
     * @return true if the player is register correctly
     */
    public static boolean registerPlayer(String playerName, String password, String email) {
        try {
            String name = playerName.toLowerCase();
            String hash = PasswordSecurity.getHash(Settings.getPasswordHash, password, name);
            if (isRegistered(name)) {
                return false;
            }
            PlayerAuth auth = new PlayerAuth(name, hash, "198.18.0.1", 0, email);
            if (!database.saveAuth(auth)) {
            	return false;
            }
            return true;
        } catch (NoSuchAlgorithmException ex) {
        	return false;
        }
    }
    
    /**
     * Sets a verification key for a player do activation
     * @param String playerName
     * @return The verification key set for that player
     */
    public static String setActivationKey(String playerName){
    	//Generate the key
    	UUID uuid = UUID.randomUUID();
    	
    	String hash = uuid.toString(); 	
    	String name = playerName.toLowerCase();
    	
    	PlayerAuth auth = new PlayerAuth(name, hash, "198.18.0.1", 0);
    	auth.setActivationKey(hash);
    	
    	if(!database.updateActivationKey(auth)){
    		return "";
    	}
    	
    	return hash;
    }
    
    public static boolean isActivated(String username){
    	return database.isActivated(username.toLowerCase());
    }
    
    /**
     * 
     * @param playerName
     * @param key
     * @return
     */
    public static boolean activateUser(String playerName, String key){
    	System.out.println("activating Users");
    	PlayerAuth auth = new PlayerAuth(playerName.toLowerCase(), "dsds", "198.18.0.1", 0);
    	auth.setActivationKey(key);
    	return database.doActivation(auth);
    }
    
    public static int getEmailCount(String email){
    	System.out.println("Getting email count");
    	return database.getEmailCount(email);
    }
}
