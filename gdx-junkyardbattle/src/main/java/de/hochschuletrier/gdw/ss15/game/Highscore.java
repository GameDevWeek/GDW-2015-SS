package de.hochschuletrier.gdw.ss15.game;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by oliver on 26.09.15.
 */
public class Highscore {


    /// SINGLETON:
    private static Highscore instance;

    /**
     * @return the active highscore
     */
    public static Highscore Get(){
        if(instance == null) instance = new Highscore();
        return instance;
    }

    protected Highscore(){

    }

    public static void reset(){
        instance = null;
    }

    ///
    public HashSet<String> dirtyTeamStats = new HashSet<>();
    public HashSet<String> dirtyPlayerStats = new HashSet<>();

    private HashMap<Integer, HashMap<String, Integer>> teamstats = new HashMap<>();
    private HashMap<Integer, HashMap<String, Integer>> playerstats = new HashMap<>();

    private HashSet<String> teamcategories = new HashSet<>();
    private HashSet<String> playercategories = new HashSet<>();;

    public void addTeam(int teamId){
        teamstats.put(teamId, new HashMap<>());
    }

    public void addPlayer(int player){
        playerstats.put(player, new HashMap<>());
    }

    public void addTeamCategory(String cat){
        teamcategories.add(cat);
    }

    public void addPlayerCategory(String cat){
        teamcategories.add(cat);
    }

    public int getTeamStat(int team, String category){
        if(! teamstats.containsKey(Integer.valueOf(team)))
            throw new IllegalArgumentException("Team " + team + " could not be found");
        if(! teamcategories.contains(category))
            throw new IllegalArgumentException("Highscore does not contain category: " + category);

        return teamstats.get(team).get(category) == null ? 0 : teamstats.get(team).get(category);
    }

    public void setTeamStat(int team, String category, int value){
        if(! teamstats.containsKey(Integer.valueOf(team)))
            throw new IllegalArgumentException("Team " + team + " could not be found");
        if(! teamcategories.contains(category))
            throw new IllegalArgumentException("Highscore does not contain category: " + category);

        teamcategories.add(category);

        teamstats.get(team).put(category, value);
    }



    public int getPlayerStat(int team, String category){
        if(! teamstats.containsKey(Integer.valueOf(team)))
            throw new IllegalArgumentException("Team " + team + " could not be found");
        if(! teamcategories.contains(category))
            throw new IllegalArgumentException("Highscore does not contain category: " + category);

        return teamstats.get(team).get(category) == null ? 0 : teamstats.get(team).get(category);
    }

    public void setPlayerStat(int player, String category, int value){
        if(! playerstats.containsKey(Integer.valueOf(player)))
            throw new IllegalArgumentException("Player " + player + " could not be found");
        if(! playercategories.contains(category))
            throw new IllegalArgumentException("Highscore does not contain category: " + category);

        playercategories.add(category);

        playerstats.get(player).put(category, value);    }

}
