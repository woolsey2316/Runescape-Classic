package com.openrsc.server.plugins.npcs.varrock;

import com.openrsc.server.model.entity.npc.Npc;
import com.openrsc.server.model.entity.player.Player;
import com.openrsc.server.plugins.listeners.TalkToNpcListener;

import static com.openrsc.server.plugins.Functions.npcTalk;

import com.openrsc.server.constants.NpcId;

public class HeadChef implements TalkToNpcListener {

	@Override
	public void onTalkToNpc(Player p, Npc n) {
		npcTalk(p, n, "Hello welcome to the chef's guild",
			"Only accomplished chefs and cooks are allowed in here",
			"Feel free to use any of our facilities");
	}

	@Override
	public boolean blockTalkToNpc(Player p, Npc n) {
		return n.getID() == NpcId.HEAD_CHEF.id();
	}

}
