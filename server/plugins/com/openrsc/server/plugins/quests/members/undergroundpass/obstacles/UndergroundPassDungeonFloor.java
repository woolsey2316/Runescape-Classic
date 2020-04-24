package com.openrsc.server.plugins.quests.members.undergroundpass.obstacles;

import com.openrsc.server.constants.ItemId;
import com.openrsc.server.constants.Quests;
import com.openrsc.server.constants.Skills;
import com.openrsc.server.model.Point;
import com.openrsc.server.model.container.Item;
import com.openrsc.server.model.entity.GameObject;
import com.openrsc.server.model.entity.player.Player;
import com.openrsc.server.plugins.triggers.UseLocTrigger;
import com.openrsc.server.plugins.triggers.OpLocTrigger;
import com.openrsc.server.plugins.triggers.OpBoundTrigger;

import java.util.Optional;

import static com.openrsc.server.plugins.Functions.*;

public class UndergroundPassDungeonFloor implements OpLocTrigger, OpBoundTrigger, UseLocTrigger {

	/**
	 * OBJECT IDs
	 **/
	public static int SPIDER_NEST_RAILING = 171;
	public static int LADDER = 920;
	public static int TOMB_OF_IBAN = 878;
	public static int DWARF_BARREL = 880;
	public static int PILE_OF_MUD = 890;

	@Override
	public boolean blockOpLoc(GameObject obj, String command, Player player) {
		return obj.getID() == LADDER || obj.getID() == TOMB_OF_IBAN || obj.getID() == DWARF_BARREL || obj.getID() == PILE_OF_MUD;
	}

	@Override
	public void onOpLoc(GameObject obj, String command, Player player) {
		if (obj.getID() == LADDER) {
			mes(player, "you climb the ladder");
			player.message("it leads to some stairs, you walk up...");
			player.teleport(782, 3549);
		}
		else if (obj.getID() == TOMB_OF_IBAN) {
			mes(player, "you try to open the door of the tomb");
			player.message("but the door refuses to open");
			mes(player, "you hear a noise from below");
			player.message("@red@leave me be");
			GameObject claws_of_iban = new GameObject(player.getWorld(), Point.location(player.getX(), player.getY()), 879, 0, 0);
			addloc(claws_of_iban);
			player.damage(((int) getCurrentLevel(player, Skills.HITS) / 5) + 5);
			say(player, null, "aaarrgghhh");
			delay(player.getWorld().getServer().getConfig().GAME_TICK * 2);
			delloc(claws_of_iban);
		}
		else if (obj.getID() == DWARF_BARREL) {
			if (!player.getCarriedItems().hasCatalogID(ItemId.BUCKET.id(), Optional.of(false))) {
				player.message("you need a bucket first");
			} else {
				player.message("you poor some of the strong brew into your bucket");
				player.getCarriedItems().getInventory().replace(ItemId.BUCKET.id(), ItemId.DWARF_BREW.id());
			}
		}
		else if (obj.getID() == PILE_OF_MUD) {
			mes(player, "you climb the pile of mud");
			player.message("it leads to an old stair way");
			player.teleport(773, 3417);
		}
	}

	@Override
	public boolean blockUseLoc(GameObject obj, Item item, Player player) {
		return obj.getID() == TOMB_OF_IBAN && (item.getCatalogId() == ItemId.DWARF_BREW.id() || item.getCatalogId() == ItemId.TINDERBOX.id());
	}

	@Override
	public void onUseLoc(GameObject obj, Item item, Player player) {
		if (obj.getID() == TOMB_OF_IBAN && item.getCatalogId() == ItemId.DWARF_BREW.id()) {
			if (player.getCache().hasKey("doll_of_iban") && player.getQuestStage(Quests.UNDERGROUND_PASS) == 6) {
				player.message("you pour the strong alcohol over the tomb");
				if (!player.getCache().hasKey("brew_on_tomb") && !player.getCache().hasKey("ash_on_doll")) {
					player.getCache().store("brew_on_tomb", true);
				}
				player.getCarriedItems().getInventory().replace(ItemId.DWARF_BREW.id(), ItemId.BUCKET.id());
			} else {
				mes(player, "you consider pouring the brew over the grave");
				player.message("but it seems such a waste");
			}
		}
		else if (obj.getID() == TOMB_OF_IBAN && item.getCatalogId() == ItemId.TINDERBOX.id()) {
			mes(player, "you try to set alight to the tomb");
			if (player.getCache().hasKey("brew_on_tomb") && !player.getCache().hasKey("ash_on_doll")) {
				mes(player, "it bursts into flames");
				changeloc(obj, new GameObject(obj.getWorld(), obj.getLocation(), 97, obj.getDirection(), obj
					.getType()));
				addloc(obj.getWorld(), obj.getLoc(), 10000);
				mes(player, "you search through the remains");
				if (!player.getCarriedItems().hasCatalogID(ItemId.IBANS_ASHES.id(), Optional.of(false))) {
					player.message("and find the ashes of ibans corpse");
					addobject(ItemId.IBANS_ASHES.id(), 1, 726, 654, player);
				} else {
					player.message("but find nothing");
				}
				player.getCache().remove("brew_on_tomb");
			} else {
				player.message("but it will not light");
			}
		}
	}

	@Override
	public boolean blockOpBound(GameObject obj, Integer click, Player player) {
		return obj.getID() == SPIDER_NEST_RAILING;
	}

	@Override
	public void onOpBound(GameObject obj, Integer click, Player player) {
		if (obj.getID() == SPIDER_NEST_RAILING) {
			mes(player, "you search the bars");
			if (player.getCache().hasKey("doll_of_iban") || player.getQuestStage(Quests.UNDERGROUND_PASS) >= 7 || player.getQuestStage(Quests.UNDERGROUND_PASS) == -1) {
				mes(player, "there's a gap big enough to squeeze through");
				player.message("would you like to try");
				int menu = multi(player,
					"nope",
					"yes, lets do it");
				if (menu == 1) {
					player.message("you squeeze through the old railings");
					if (obj.getDirection() == 0) {
						if (obj.getY() == player.getY())
							player.teleport(obj.getX(), obj.getY() - 1);
						else
							player.teleport(obj.getX(), obj.getY());
					}
				}
			} else {
				player.message("but you can't quite squeeze through");
			}
		}
	}
}
