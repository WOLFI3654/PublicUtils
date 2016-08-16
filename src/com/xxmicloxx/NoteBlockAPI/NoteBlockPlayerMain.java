package com.xxmicloxx.NoteBlockAPI;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Player;

public class NoteBlockPlayerMain {

    public static NoteBlockPlayerMain plugin;
    public static HashMap<String, ArrayList<SongPlayer>> playingSongs = new HashMap<String, ArrayList<SongPlayer>>();
    public static HashMap<String, Byte> playerVolume = new HashMap<String, Byte>();

    public static boolean isReceivingSong(Player p) {
        return ((playingSongs.get(p.getName()) != null) && (!playingSongs.get(p.getName()).isEmpty()));
    }

    public static void stopPlaying(Player p) {
        if (playingSongs.get(p.getName()) == null) {
            return;
        }
        for (SongPlayer s : playingSongs.get(p.getName())) {
            s.removePlayer(p);
        }
    }

    public static void setPlayerVolume(Player p, byte volume) {
        playerVolume.put(p.getName(), volume);
    }

    public static byte getPlayerVolume(Player p) {
        Byte b = playerVolume.get(p.getName());
        if (b == null) {
            b = 100;
            playerVolume.put(p.getName(), b);
        }
        return b;
    }


}
