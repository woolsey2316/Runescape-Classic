package com.openrsc.server.event.rsc.impl.combat.scripts.all;

import com.openrsc.server.constants.NpcId;
import com.openrsc.server.constants.Skill;
import com.openrsc.server.event.rsc.impl.combat.scripts.CombatAggroScript;
import com.openrsc.server.event.rsc.impl.combat.scripts.OnCombatStartScript;
import com.openrsc.server.model.entity.Mob;
import com.openrsc.server.model.entity.npc.Npc;
import com.openrsc.server.model.entity.player.Player;
import com.openrsc.server.model.entity.update.ChatMessage;

import static com.openrsc.server.constants.Skills.*;

public class SkeletonMage implements CombatAggroScript, OnCombatStartScript {

	@Override
	public void executeScript(Mob attacker, Mob victim) {
		if (attacker.isNpc()) {
			Player player = (Player) victim;
			Npc npc = (Npc) attacker;

			npc.getUpdateFlags().setChatMessage(new ChatMessage(npc, "i infect your body with rot", player));

			player.message("You feel slightly weakened");

			int[] stats = {Skill.of(ATTACK).id(), Skill.of(DEFENSE).id(), Skill.of(STRENGTH).id()};
			for(int affectedStat : stats) {
				/* How much to lower the stat */
				int lowerBy = (int) Math.ceil(((player.getSkills().getMaxStat(affectedStat) + 20) * 0.05));
				/* New current level */
				final int newStat = Math.max(0, player.getSkills().getLevel(affectedStat) - lowerBy);
				player.getSkills().setLevel(affectedStat, newStat);
			}
		}
	}

	@Override
	public boolean shouldExecute(Mob attacker, Mob victim) {
		return attacker.isNpc() && !((Npc)attacker).executedAggroScript()
				&& attacker.getID() == NpcId.SKELETON_MAGE.id();
	}

}
