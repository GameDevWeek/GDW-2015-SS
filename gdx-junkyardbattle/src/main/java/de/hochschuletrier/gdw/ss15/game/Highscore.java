package de.hochschuletrier.gdw.ss15.game;


import de.hochschuletrier.gdw.ss15.events.network.server.SendPacketServerEvent;
import de.hochschuletrier.gdw.ss15.game.network.Packets.SimplePacket;

/**
 * Created by oliver on 26.09.15.
 */
public class Highscore {

	// / SINGLETON:
	private static Highscore instance;

	/**
	 * @return the active highscore
	 */
	public static Highscore Get() {
		if (instance == null)
			instance = new Highscore();
		return instance;
	}

	protected Highscore() {

	}

	public static void reset() {
		instance = null;
	}

	public int[] teamPoints = { 0, 0 };

	// /
	/*
	 * public HashMap<String, Integer> dirtyTeamStats = new HashMap<>(); public
	 * HashMap<String, Integer> dirtyPlayerStats = new HashMap<>();
	 * 
	 * private HashMap<Integer, HashMap<String, Integer>> teamstats = new
	 * HashMap<>(); private HashMap<Integer, HashMap<String, Integer>>
	 * playerstats = new HashMap<>();
	 * 
	 * private HashSet<String> teamcategories = new HashSet<>(); private
	 * HashSet<String> playercategories = new HashSet<>();;
	 * 
	 * public void addTeam(int teamId){ teamstats.put(teamId, new HashMap<>());
	 * }
	 * 
	 * public boolean hasPlayer(int player){ return
	 * playerstats.containsKey(player); } public boolean hasTeam(int team){
	 * return teamstats.containsKey(team); }
	 * 
	 * public boolean hasPlayerCategory(String s){ return
	 * playercategories.contains(s); } public boolean hasTeamCategory(String s){
	 * return teamcategories.contains(s); }
	 * 
	 * public void addPlayer(int player){ playerstats.put(player, new
	 * HashMap<>()); }
	 * 
	 * public void addTeamCategory(String cat){ teamcategories.add(cat); }
	 * 
	 * public void addPlayerCategory(String cat){ teamcategories.add(cat); }
	 * 
	 * public int getTeamStat(int team, String category){ if(!
	 * teamstats.containsKey(Integer.valueOf(team))) throw new
	 * IllegalArgumentException("Team " + team + " could not be found"); if(!
	 * teamcategories.contains(category)) throw new
	 * IllegalArgumentException("Highscore does not contain category: " +
	 * category);
	 * 
	 * return teamstats.get(team).get(category) == null ? 0 :
	 * teamstats.get(team).get(category); }
	 * 
	 * public void setTeamStat(int team, String category, int value){ if(!
	 * teamstats.containsKey(Integer.valueOf(team))) throw new
	 * IllegalArgumentException("Team " + team + " could not be found"); if(!
	 * teamcategories.contains(category)) throw new
	 * IllegalArgumentException("Highscore does not contain category: " +
	 * category);
	 * 
	 * dirtyPlayerStats.put(category, team);
	 * 
	 * teamstats.get(team).put(category, value); }
	 * 
	 * 
	 * 
	 * public int getPlayerStat(int team, String category){ if(!
	 * teamstats.containsKey(Integer.valueOf(team))) throw new
	 * IllegalArgumentException("Team " + team + " could not be found"); if(!
	 * teamcategories.contains(category)) throw new
	 * IllegalArgumentException("Highscore does not contain category: " +
	 * category);
	 * 
	 * return teamstats.get(team).get(category) == null ? 0 :
	 * teamstats.get(team).get(category); }
	 * 
	 * public void setPlayerStat(int player, String category, int value){ if(!
	 * playerstats.containsKey(Integer.valueOf(player))) throw new
	 * IllegalArgumentException("Player " + player + " could not be found");
	 * if(! playercategories.contains(category)) throw new
	 * IllegalArgumentException("Highscore does not contain category: " +
	 * category);
	 * 
	 * dirtyPlayerStats.put(category, player);
	 * 
	 * playerstats.get(player).put(category, value); }
	 */

	public void addTeamPoints(int teamid, int basePointsToAdd) {
		teamPoints[teamid] += basePointsToAdd;
		
		if(teamid == 0)
		{
			SimplePacket sPack = new SimplePacket(SimplePacket.SimplePacketId.HighscorePacket.getValue(),-teamPoints[0]);
			SendPacketServerEvent.emit(sPack, true);
		}
		if(teamid == 1)
		{
			SimplePacket sPack = new SimplePacket(SimplePacket.SimplePacketId.HighscorePacket.getValue(),teamPoints[1]);
			SendPacketServerEvent.emit(sPack, true);
		}
	}
}
