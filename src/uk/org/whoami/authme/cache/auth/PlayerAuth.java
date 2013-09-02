/*
 * Copyright 2011 Sebastian Köhler <sebkoehler@whoami.org.uk>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.org.whoami.authme.cache.auth;

import uk.org.whoami.authme.security.PasswordSecurity;
import uk.org.whoami.authme.settings.Settings;

public class PlayerAuth {

    private String nickname;
    private String hash;
    private String ip = "198.18.0.1";
    private long lastLogin;
    private int x = 0;
    private int y = 0;
    private int z = 0;
    private String world = "world";
    private String salt = "";
    private String vBhash = null;
    private int groupId;
    private String email = "your@email.com";
    private int isActivated = 0;
    private String activationKey = null;

    public PlayerAuth(String nickname, String hash, String ip, long lastLogin) {
        this.nickname = nickname;
        this.hash = hash;
        this.ip = ip;
        this.lastLogin = lastLogin;
    }

    public PlayerAuth(String nickname, String hash, String ip, long lastLogin, String email) {
        this.nickname = nickname;
        this.hash = hash;
        this.ip = ip;
        this.lastLogin = lastLogin;
        this.email = email;
    }

    public PlayerAuth(String nickname, int x, int y, int z, String world) {
        this.nickname = nickname;
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
    }

    public PlayerAuth(String nickname, String hash, String ip, long lastLogin, int x, int y, int z, String world, String email) {
        this.nickname = nickname;
        this.hash = hash;
        this.ip = ip;
        this.lastLogin = lastLogin;
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
        this.email = email;
    }

    public PlayerAuth(String nickname, String hash, String salt, int groupId, String ip, long lastLogin, int x, int y, int z, String world,  String email) {
        this.nickname = nickname;
        this.hash = hash;
        this.ip = ip;
        this.lastLogin = lastLogin;
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
        this.salt = salt;
        this.groupId = groupId;
        this.email = email; 
    }

    public PlayerAuth(String nickname, String hash, String salt, int groupId , String ip, long lastLogin) {
        this.nickname = nickname;
        this.hash = hash;
        this.ip = ip;
        this.lastLogin = lastLogin;  
        this.salt = salt;
        this.groupId = groupId;
    }

    public PlayerAuth(String nickname, String hash, String salt, String ip, long lastLogin) {
        this.nickname = nickname;
        this.hash = hash;
        this.ip = ip;
        this.lastLogin = lastLogin;  
        this.salt = salt;
    }

    public PlayerAuth(String nickname, String hash, String salt, String ip, long lastLogin, int x, int y, int z, String world, String email) {
        this.nickname = nickname;
        this.hash = hash;
        this.ip = ip;
        this.lastLogin = lastLogin;
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
        this.salt = salt;
        this.email = email; 
    }

    public PlayerAuth(String nickname, String hash, String ip, long lastLogin, int x, int y, int z, String world) {
        this.nickname = nickname;
        this.hash = hash;
        this.ip = ip;
        this.lastLogin = lastLogin;
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
        this.email = "your@email.com";
    }

	public String getIp() {
        return ip;
    }

    public String getNickname() {
        return nickname;
    }

    public String getHash() {
        if(!salt.isEmpty() && Settings.getPasswordHash == PasswordSecurity.HashAlgorithm.MD5VB) {
        	vBhash = "$MD5vb$"+salt+"$"+hash;
            return vBhash;
        }
        else {
        	return hash;
        } 
    }

    public String getSalt() {
    	return this.salt;
    }

    public int getGroupId() {
        return groupId;
    }

    public int getQuitLocX() {
        return x;
    }
    public int getQuitLocY() {
        return y;
    }
    public int getQuitLocZ() {
        return z;
    }
    public String getEmail() {
    	return email;
    }
    public int getIsActivated() {
		return isActivated;
	}

	public String getActivationKey() {
		return activationKey;
	}

	public void setActivationKey(String activationKey) {
		this.activationKey = activationKey;
	}

	public void setIsActivated(int isActivated) {
		this.isActivated = isActivated;
	}

	public void setQuitLocX(int x) {
        this.x = x;
    }
    public void setQuitLocY(int y) {
        this.y = y;
    }
    public void setQuitLocZ(int z) {
        this.z = z;
    }  
    public long getLastLogin() {
        return lastLogin;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setLastLogin(long lastLogin) {
        this.lastLogin = lastLogin;
    }

    public void setEmail(String email) {
    	this.email = email;
    }

    public void setSalt(String salt) {
    	this.salt = salt;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PlayerAuth)) {
            return false;
        }
        PlayerAuth other = (PlayerAuth) obj;
        return other.getIp().equals(this.ip) && other.getNickname().equals(this.nickname);
    }

    @Override
    public int hashCode() {
        int hashCode = 7;
        hashCode = 71 * hashCode + (this.nickname != null ? this.nickname.hashCode() : 0);
        hashCode = 71 * hashCode + (this.ip != null ? this.ip.hashCode() : 0);
        return hashCode;
    }

	public void setWorld(String world) {
		this.world = world;
	}

	public String getWorld() {
		return world;
	}
	
	@Override
	public String toString() {
		String s = "Player : " + nickname + " ! IP : " + ip + " ! LastLogin : " + lastLogin + " ! LastPosition : " + x + "," + y + "," + z + "," + world
		+ " ! Email : " + email + " ! Hash : " + hash + " ! Salt : " + salt;
		return s;
			
	}

}
