package com.openrsc.server.plugins.minigames.gnomerestaurant;

import com.openrsc.server.constants.ItemId;
import com.openrsc.server.constants.Minigames;
import com.openrsc.server.constants.NpcId;
import com.openrsc.server.constants.Skills;
import com.openrsc.server.model.container.Item;
import com.openrsc.server.model.entity.npc.Npc;
import com.openrsc.server.model.entity.player.Player;
import com.openrsc.server.net.rsc.ActionSender;
import com.openrsc.server.plugins.MiniGameInterface;
import com.openrsc.server.plugins.triggers.DropObjTrigger;
import com.openrsc.server.plugins.triggers.OpInvTrigger;
import com.openrsc.server.plugins.triggers.TalkNpcTrigger;
import com.openrsc.server.util.rsc.DataConversions;

import java.util.Optional;

import static com.openrsc.server.plugins.Functions.*;

public class GnomeRestaurant implements MiniGameInterface, TalkNpcTrigger, OpInvTrigger, DropObjTrigger {

	@Override
	public int getMiniGameId() {
		return Minigames.GNOME_RESTAURANT;
	}

	@Override
	public String getMiniGameName() {
		return "Gnome Restaurant (members)";
	}

	@Override
	public boolean isMembers() {
		return true;
	}

	@Override
	public void handleReward(Player player) {
		//mini-game complete handled already
	}

	@Override
	public boolean blockTalkNpc(Player player, Npc n) {
		return n.getID() == NpcId.ALUFT_GIANNE.id();
	}

	@Override
	public void onTalkNpc(Player player, Npc n) {
		if (n.getID() == NpcId.ALUFT_GIANNE.id()) {
			if (!player.getCache().hasKey("gnome_cooking")) {
				say(player, n, "hello");
				npcsay(player, n, "well hello there,you hungry..",
					"you come to the right place",
					"eat green, eat gnome cruisine",
					"my waiter will be glad to take your order");
				say(player, n, "thanks");
				npcsay(player, n, "on the other hand if you looking for some work",
					"i have a cook's position available");
				int menu = multi(player, n, "no thanks i'm no cook", "ok i'll give it a go");
				if (menu == 0) {
					npcsay(player, n, "in that case please, eat and enjoy");
				} else if (menu == 1) {
					npcsay(player, n, "well that's great",
						"of course i'll have to see what you're like first",
						"here, have a look at our menu");
					player.message("Aluft gives you a cook book");
					give(player, ItemId.GIANNE_COOK_BOOK.id(), 1);
					player.getCache().set("gnome_cooking", 1);
					npcsay(player, n, "when you've had a look come back...",
						"... and i'll let you prepare a few dishes");
					say(player, n, "good stuff");
				}
			} else {
				int stage = player.getCache().getInt("gnome_cooking");
				switch (stage) {
					case 1:
						say(player, n, "hi mr gianne");
						npcsay(player, n, "hello my good friend",
							"what did you think");
						say(player, n, "I'm not too sure about toads legs");
						npcsay(player, n, "they're a gnome delicacy, you'll love them",
							"but we'll start with something simple",
							"can you make me a cheese and tomato gnome batta");
						npcsay(player, n, "here's what you need");
						mes(player, player.getWorld().getServer().getConfig().GAME_TICK * 2, "aluft gives you one tomato, some cheese...");
						give(player, ItemId.TOMATO.id(), 1);
						give(player, ItemId.CHEESE.id(), 1);
						player.message("...some equa leaves and some plain dough");
						give(player, ItemId.EQUA_LEAVES.id(), 1);
						give(player, ItemId.GIANNE_DOUGH.id(), 1);
						player.getCache().set("gnome_cooking", 2);
						say(player, n, "thanks");
						npcsay(player, n, "Let me know how you get on");
						break;
					case 2:
						say(player, n, "Hi mr gianne");
						npcsay(player, n, "call me aluft");
						say(player, n, "ok");
						npcsay(player, n, "so how did you get on?");
						if (player.getCarriedItems().hasCatalogID(ItemId.CHEESE_AND_TOMATO_BATTA.id(), Optional.of(false))) {
							say(player, n, "no problem, it was easy");
							mes(player, player.getWorld().getServer().getConfig().GAME_TICK * 3, "you give aluft the gnome batta");
							player.getCarriedItems().remove(new Item(ItemId.CHEESE_AND_TOMATO_BATTA.id()));
							player.message("he takes a bite");
							npcsay(player, n, "not bad...not bad at all",
								"ok now for something a little harder",
								"try and make me a choc bomb.. they're my favorite",
								"here's what you need");
							mes(player, player.getWorld().getServer().getConfig().GAME_TICK * 2, "aluft gives you four bars of chocolate");
							give(player, ItemId.CHOCOLATE_BAR.id(), 4);
							mes(player, player.getWorld().getServer().getConfig().GAME_TICK * 2, "some equa leaves, some chocolate dust...");
							give(player, ItemId.EQUA_LEAVES.id(), 1);
							give(player, ItemId.CHOCOLATE_DUST.id(), 1);
							player.message("...some gianne dough and some cream");
							give(player, ItemId.GIANNE_DOUGH.id(), 1);
							give(player, ItemId.CREAM.id(), 2);
							say(player, n, "ok aluft, i'll be back soon");
							npcsay(player, n, "good stuff");
							player.getCache().set("gnome_cooking", 3);
						} else {
							say(player, n, "erm.. not quite done yet");
							npcsay(player, n, "ok, let me know when you are",
								"i need one cheese and tomato batta");
						}
						break;
					case 3:
						say(player, n, "hi aluft");
						npcsay(player, n, "hello there, how did you get on");
						if (player.getCarriedItems().hasCatalogID(ItemId.CHOCOLATE_BOMB.id(), Optional.of(false))) {
							say(player, n, "here you go");
							player.getCarriedItems().remove(new Item(ItemId.CHOCOLATE_BOMB.id()));
							mes(player, player.getWorld().getServer().getConfig().GAME_TICK * 2, "you give aluft the choc bomb");
							player.message("he takes a bite");
							npcsay(player, n, "yes, yes, yes, that's superb",
								"i'm really impressed");
							say(player, n, "i'm glad");
							npcsay(player, n, "ok then, now can you make me a toad batta",
								"here's what you need");
							give(player, ItemId.GIANNE_DOUGH.id(), 1);
							give(player, ItemId.EQUA_LEAVES.id(), 1);
							give(player, ItemId.GNOME_SPICE.id(), 1);
							mes(player, player.getWorld().getServer().getConfig().GAME_TICK * 3, "mr gianne gives you some dough, some equaleaves...");
							player.message("...and some gnome spice");
							npcsay(player, n, "i'm afraid all are toads legs are served fresh");
							say(player, n, "nice!");
							npcsay(player, n, "so you'll need to go to the swamp on ground level",
								"and catch a toad",
								"let me know when the batta's ready");
							player.getCache().set("gnome_cooking", 4);
						} else {
							say(player, n, "i haven't made it yet");
							npcsay(player, n, "just follow the instructions carefully",
								"i need one choc bomb");
						}
						break;
					case 4:
						say(player, n, "hi mr gianne");
						npcsay(player, n, "aluft");
						say(player, n, "sorry, aluft");
						npcsay(player, n, "so where's my toad batta?");
						if (player.getCarriedItems().hasCatalogID(ItemId.TOAD_BATTA.id(), Optional.of(false))) {
							say(player, n, "here you go, easy");
							mes(player, player.getWorld().getServer().getConfig().GAME_TICK * 3, "you give mr gianne the toad batta");
							player.getCarriedItems().remove(new Item(ItemId.TOAD_BATTA.id()));
							player.message("he takes a bite");
							npcsay(player, n, "ooh, that's some good toad",
								"very nice",
								"let's see if you can make a worm hole");
							say(player, n, "a wormhole?");
							npcsay(player, n, "yes, it's in the cooking guide i gave you",
								"you'll have to get the worms from the swamp",
								"but here's everything else you'll need",
								"let me know when your done");
							give(player, ItemId.GIANNE_DOUGH.id(), 1);
							give(player, ItemId.ONION.id(), 2);
							give(player, ItemId.EQUA_LEAVES.id(), 1);
							player.getCache().set("gnome_cooking", 5);
						} else {
							say(player, n, "i'm not done yet");
							npcsay(player, n, "ok, quick as you can though");
							say(player, n, "no problem");
						}
						break;
					case 5:
						say(player, n, "hello again aluft");
						npcsay(player, n, "hello traveller, how did you do?");
						if (player.getCarriedItems().hasCatalogID(ItemId.WORM_HOLE.id(), Optional.of(false))) {
							say(player, n, "here, see what you think");
							mes(player, player.getWorld().getServer().getConfig().GAME_TICK * 3, "you give mr gianne the worm hole");
							player.getCarriedItems().remove(new Item(ItemId.WORM_HOLE.id()));
							player.message("he takes a bite");
							npcsay(player, n, "hmm, that's actually really good",
								"how about you make me some toad crunchies for desert",
								"then i'll decide whether i can take you on");
							say(player, n, "toad crunchies?");
							npcsay(player, n, "that's right, here's all you need",
								"except the toad");
							give(player, ItemId.GIANNE_DOUGH.id(), 1);
							give(player, ItemId.EQUA_LEAVES.id(), 1);
							player.message("mr gianne gives you some gianne dough and some equa leaves");
							npcsay(player, n, "let me know when your done");
							player.getCache().set("gnome_cooking", 6);
						} else {
							say(player, n, "i'm not done yet");
							npcsay(player, n, "ok, quick as you can though",
								"i need one worm hole");
							say(player, n, "no problem");
						}
						break;
					case 6:
						say(player, n, "hi aluft\"");
						npcsay(player, n, "hello, how are you getting on?");
						if (player.getCarriedItems().hasCatalogID(ItemId.TOAD_CRUNCHIES.id(), Optional.of(false))) {
							say(player, n, "here, try it");
							mes(player, player.getWorld().getServer().getConfig().GAME_TICK * 3, "you give mr gianne the toad crunchie");
							player.getCarriedItems().remove(new Item(ItemId.TOAD_CRUNCHIES.id()));
							player.message("he takes a bite");
							npcsay(player, n, "well for a human you certainly can cook",
								"i'd love to have you on the team",
								"if you ever want to make some money",
								"or want to improve your cooking skills just come and see me",
								"i'll tell you what meals i need, and if you can, you make them");
							say(player, n, "what about ingredients?");
							npcsay(player, n, "well you know where to find toads and worms",
								"you can buy the rest from hudo glenfad the grocer",
								"i'll always pay you much more for the meal than you paid for the ingredients",
								"and it's a great way to improve your cooking skills");
							player.getCache().set("gnome_cooking", 7); // COMPLETED JOB!
						} else {
							say(player, n, "no luck so for");
							npcsay(player, n, "ok then but don't take too long",
								"i need one toad crunchie");
						}
						break;
					case 7:
						/**
						 * Completed and hired for job.
						 */
						if (player.getCache().hasKey("gnome_restaurant_job")) {
							say(player, n, "hi aluft");
							myCurrentJob(player, n);
						} else {
							say(player, n, "hello again aluft");
							npcsay(player, n, "well hello there traveller",
								"have you come to help me out?");
							int menu = multi(player, n,
								"sorry aluft, i'm too busy",
								"i would be glad to help");
							if (menu == 0) {
								npcsay(player, n, "no worries, let me know when you're free");
							} else if (menu == 1) {
								npcsay(player, n, "good stuff");
								randomizeJob(player, n);
							}
						}
						break;
				}
			}
		}
	}

	private void myCurrentJob(Player player, Npc n) {
		int job = player.getCache().getInt("gnome_restaurant_job");
		if (job == 0) {
			npcsay(player, n, "hello again, are the dishes ready?");
			if (ifheld(player, ItemId.WORM_BATTA.id(), 2)
					&& player.getCarriedItems().hasCatalogID(ItemId.VEG_BATTA.id(), Optional.of(false))
					&& player.getCarriedItems().hasCatalogID(ItemId.TOAD_BATTA.id(), Optional.of(false))) {
				say(player, n, "all done, here you go");
				mes(player, player.getWorld().getServer().getConfig().GAME_TICK * 3, "you give aluft two worm batta's a veg batta and a toad batta");
				player.incExp(Skills.COOKING, 425, true);
				player.getCarriedItems().remove(new Item(ItemId.WORM_BATTA.id(), 2));
				player.getCarriedItems().remove(new Item(ItemId.VEG_BATTA.id()));
				player.getCarriedItems().remove(new Item(ItemId.TOAD_BATTA.id()));
				npcsay(player, n, "they look great, well done",
					"here's your share of the profit");
				player.message("mr gianne gives you 45 gold coins");
				give(player, ItemId.COINS.id(), 45);
			} else {
				say(player, n, "i'm not done yet");
				npcsay(player, n, "i need  two worm batta's, one toad batta",
					"...and one veg batta please",
					"be as quick as you can");
				return;
			}
		} else if (job == 1) {
			npcsay(player, n, "hello again, are the dishes ready?");
			if (player.getCarriedItems().hasCatalogID(ItemId.CHOCOLATE_BOMB.id(), Optional.of(false))
					&& ifheld(player, ItemId.CHOC_CRUNCHIES.id(), 2)
					&& ifheld(player, ItemId.TOAD_CRUNCHIES.id(), 2)) {
				say(player, n, "here you go aluft");
				mes(player, player.getWorld().getServer().getConfig().GAME_TICK * 3, "you give aluft choc bomb, two choc crunchies and two toad crunchies");
				player.getCarriedItems().remove(new Item(ItemId.CHOCOLATE_BOMB.id()));
				player.getCarriedItems().remove(new Item(ItemId.CHOC_CRUNCHIES.id(), 2));
				player.getCarriedItems().remove(new Item(ItemId.TOAD_CRUNCHIES.id(), 2));
				player.incExp(Skills.COOKING, 675, true);
				npcsay(player, n, "they look great, well done",
					"here's your share of the profit");
				player.message("mr gianne gives you 75 gold coins");
				give(player, ItemId.COINS.id(), 75);
			} else {
				say(player, n, "i'm not done yet");
				npcsay(player, n, "ok, i need a choc bomb, two choc crunchies and two toad crunchies",
					"don't take too long",
					"it's a full house tonight");
				return;
			}
		} else if (job == 2) {
			npcsay(player, n, "hello again traveller how did you do?");
			if (ifheld(player, ItemId.CHOC_CRUNCHIES.id(), 2)) {
				say(player, n, "all done, here you go");
				mes(player, player.getWorld().getServer().getConfig().GAME_TICK * 3, "you give aluft the two choc crunchies");
				player.getCarriedItems().remove(new Item(ItemId.CHOC_CRUNCHIES.id(), 2));
				player.incExp(Skills.COOKING, 300, true);
				npcsay(player, n, "they look great, well done",
					"here's your share of the profit");
				player.message("mr gianne gives you 30 gold coins");
				give(player, ItemId.COINS.id(), 30);
			} else {
				say(player, n, "i'm not done yet");
				npcsay(player, n, "i just need two choc crunchies",
					"should be easy");
				return;
			}
		} else if (job == 3) {
			npcsay(player, n, "hello again traveller how did you do?");
			if (player.getCarriedItems().hasCatalogID(ItemId.CHOCOLATE_BOMB.id(), Optional.of(false))
					&& ifheld(player, ItemId.CHOC_CRUNCHIES.id(), 2)) {
				say(player, n, "here you go aluft");
				mes(player, player.getWorld().getServer().getConfig().GAME_TICK * 3, "you give aluft one choc bomb and two choc crunchies");
				player.getCarriedItems().remove(new Item(ItemId.CHOCOLATE_BOMB.id()));
				player.getCarriedItems().remove(new Item(ItemId.CHOC_CRUNCHIES.id(), 2));
				player.incExp(Skills.COOKING, 425, true);
				npcsay(player, n, "they look great, well done",
					"here's your share of the profit");
				player.message("mr gianne gives you 45 gold coins");
				give(player, ItemId.COINS.id(), 45);
			} else {
				say(player, n, "i'm not done yet");
				npcsay(player, n, "i need one choc bomb and two choc crunchies please");
				return;
			}
		} else if (job == 4) {
			npcsay(player, n, "hello again traveller how did you do?");
			if (ifheld(player, ItemId.VEG_BATTA.id(), 2)
					&& player.getCarriedItems().hasCatalogID(ItemId.WORM_HOLE.id(), Optional.of(false))) {
				say(player, n, "here you go aluft");
				mes(player, player.getWorld().getServer().getConfig().GAME_TICK * 3, "you give aluft two veg batta's and a worm hole");
				player.getCarriedItems().remove(new Item(ItemId.VEG_BATTA.id(), 2));
				player.getCarriedItems().remove(new Item(ItemId.WORM_HOLE.id()));
				player.incExp(Skills.COOKING, 425, true);
				npcsay(player, n, "they look great, well done",
					"here's your share of the profit");
				player.message("mr gianne gives you 45 gold coins");
				give(player, ItemId.COINS.id(), 45);
			} else {
				say(player, n, "i'm not done yet");
				npcsay(player, n, "ok, i need two veg batta's and one worm hole",
					"ok, but try not to take too long",
					"it's a full house tonight");
				return;
			}
		} else if (job == 5) {
			npcsay(player, n, "hello again, are the dishes ready?");
			if (player.getCarriedItems().hasCatalogID(ItemId.VEGBALL.id(), Optional.of(false))
					&& player.getCarriedItems().hasCatalogID(ItemId.TANGLED_TOADS_LEGS.id(), Optional.of(false))
					&& player.getCarriedItems().hasCatalogID(ItemId.WORM_HOLE.id(), Optional.of(false))) {
				say(player, n, "all done, here you go");
				mes(player, player.getWorld().getServer().getConfig().GAME_TICK * 3, "you give aluft one veg ball, one twisted toads legs and one worm hole");
				player.getCarriedItems().remove(new Item(ItemId.VEGBALL.id(), 1));
				player.getCarriedItems().remove(new Item(ItemId.TANGLED_TOADS_LEGS.id(), 1));
				player.getCarriedItems().remove(new Item(ItemId.WORM_HOLE.id(), 1));
				player.incExp(Skills.COOKING, 425, true);
				npcsay(player, n, "they look great, well done",
					"here's your share of the profit");
				player.message("mr gianne gives you 45 gold coins");
				give(player, ItemId.COINS.id(), 45);
			} else {
				say(player, n, "i'm not done yet");
				npcsay(player, n, "i need  one veg ball, one twisted toads legs...",
					"...and one worm hole please");
				return;
			}
		} else if (job == 6) {
			npcsay(player, n, "hello again traveller how did you do?");
			if (player.getCarriedItems().hasCatalogID(ItemId.CHEESE_AND_TOMATO_BATTA.id(), Optional.of(false))
					&& player.getCarriedItems().hasCatalogID(ItemId.VEGBALL.id(), Optional.of(false))
					&& ifheld(player, ItemId.WORM_CRUNCHIES.id(), 2)) {
				mes(player, player.getWorld().getServer().getConfig().GAME_TICK * 3, "you give one cheese and tomato batta,one veg ball...",
						"...and two portions of worm crunchies");
				player.getCarriedItems().remove(new Item(ItemId.CHEESE_AND_TOMATO_BATTA.id()));
				player.getCarriedItems().remove(new Item(ItemId.VEGBALL.id()));
				player.getCarriedItems().remove(new Item(ItemId.WORM_CRUNCHIES.id(), 2));
				player.incExp(Skills.COOKING, 550, true);
				npcsay(player, n, "they look great, well done",
					"here's your share of the profit");
				player.message("mr gianne gives you 60 gold coins");
				give(player, ItemId.COINS.id(), 60);
			} else {
				say(player, n, "i'm not done yet");
				npcsay(player, n, "i need one cheese and tomato batta,one veg ball...",
					"...and two portions of worm crunchies please");
				return;
			}
		} else if (job == 7) {
			// intentional glitch on minigame, see https://youtu.be/jtc97eKmFWc?t=806
			npcsay(player, n, "hello again, are the dishes ready?");
			if (ifheld(player, ItemId.SPICE_CRUNCHIES.id(), 2)
					&& player.getCarriedItems().hasCatalogID(ItemId.FRUIT_BATTA.id(), Optional.of(false))
					&& player.getCarriedItems().hasCatalogID(ItemId.CHOCOLATE_BOMB.id(), Optional.of(false))
					&& player.getCarriedItems().hasCatalogID(ItemId.VEGBALL.id(), Optional.of(false))) {
				say(player, n, "all done, here you go");
				mes(player, player.getWorld().getServer().getConfig().GAME_TICK * 3, "you give aluft the tangled toads legs and two worm crunchies");
				player.getCarriedItems().remove(new Item(ItemId.SPICE_CRUNCHIES.id(), 2));
				player.getCarriedItems().remove(new Item(ItemId.FRUIT_BATTA.id()));
				player.getCarriedItems().remove(new Item(ItemId.CHOCOLATE_BOMB.id()));
				player.getCarriedItems().remove(new Item(ItemId.VEGBALL.id()));
				player.incExp(Skills.COOKING, 425, true);
				npcsay(player, n, "they look great, well done",
					"here's your share of the profit");
				player.message("mr gianne gives you 45 gold coins");
				give(player, ItemId.COINS.id(), 45);
			} else {
				// dialogue recreated
				say(player, n, "i'm not done yet");
				npcsay(player, n, "i need  two spice crunchies, one fruit batta...",
					"...a choc bomb and a veg ball please");
				return;
			}
		} else if (job == 8) {
			// recreated job from message on job 7
			// made it intentionally glitched
			npcsay(player, n, "hello again, are the dishes ready?");
			if (player.getCarriedItems().hasCatalogID(ItemId.TANGLED_TOADS_LEGS.id(), Optional.of(false))
					&& ifheld(player, ItemId.WORM_CRUNCHIES.id(), 2)) {
				say(player, n, "all done, here you go");
				mes(player, player.getWorld().getServer().getConfig().GAME_TICK * 3, "you give aluft one choc bomb and two choc crunchies");
				player.getCarriedItems().remove(new Item(ItemId.TANGLED_TOADS_LEGS.id()));
				player.getCarriedItems().remove(new Item(ItemId.WORM_CRUNCHIES.id(), 2));
				player.incExp(Skills.COOKING, 425, true);
				npcsay(player, n, "they look great, well done",
					"here's your share of the profit");
				player.message("mr gianne gives you 45 gold coins");
				give(player, ItemId.COINS.id(), 45);
			} else {
				// dialogue recreated
				say(player, n, "i'm not done yet");
				npcsay(player, n, "i need one tangled toads legs and two worm crunchies please");
				return;
			}
		}
		player.getCache().remove("gnome_restaurant_job");
		if (!player.getCache().hasKey("gnome_jobs_completed")) {
			player.getCache().set("gnome_jobs_completed", 1);
		} else {
			int completedJobs = player.getCache().getInt("gnome_jobs_completed");
			player.getCache().set("gnome_jobs_completed", (completedJobs + 1));
		}
		npcsay(player, n, "can you stay and make another dish?");
		int menu = multi(player, n,
			"sorry aluft, i'm too busy",
			"i would be glad to help");
		if (menu == 0) {
			npcsay(player, n, "no worries, let me know when you're free");
		} else if (menu == 1) {
			npcsay(player, n, "your a life saver");
			randomizeJob(player, n);
		}
	}

	private void randomizeJob(Player player, Npc n) {
		int randomize = DataConversions.random(0, 8);
		if (randomize == 0) {
			npcsay(player, n, "can you make me a two worm batta's, one toad batta...",
				"...and one veg batta please");
			say(player, n, "ok then");
		} else if (randomize == 1) {
			npcsay(player, n, "ok, i need a choc bomb, two choc crunchies and two toad crunchies");
			say(player, n, "no problem");
		} else if (randomize == 2) {
			npcsay(player, n, "i just need two choc crunchies please");
			say(player, n, "no problem");
		} else if (randomize == 3) {
			npcsay(player, n, "i just need one choc bomb and two choc crunchies please");
			say(player, n, "no problem");
		} else if (randomize == 4) {
			npcsay(player, n, "excellent, i need two veg batta's and one worm hole");
			say(player, n, "no problem");
		} else if (randomize == 5) {
			npcsay(player, n, "can you make me a one veg ball, one twisted toads legs...",
				"...and one worm hole please");
			say(player, n, "ok then");
		} else if (randomize == 6) {
			npcsay(player, n, "i need one cheese and tomato batta,one veg ball...",
				"...and two portions of worm crunchies please");
			say(player, n, "ok, i'll do my best");
		} else if (randomize == 7) {
			npcsay(player, n, "can you make a two spice crunchies, one fruit batta...",
				"...a choc bomb and a veg ball please chef");
			say(player, n, "i'll try");
		} else if (randomize == 8) {
			npcsay(player, n, "i just need one tangled toads legs and two worm crunchies please");
			say(player, n, "ok, i'll do my best");
		}
		if (!player.getCache().hasKey("gnome_restaurant_job")) {
			player.getCache().set("gnome_restaurant_job", randomize);
		}
	}

	@Override
	public boolean blockOpInv(Item item, Player player, String command) {
		return item.getCatalogId() == ItemId.GIANNE_COOK_BOOK.id();
	}

	@Override
	public void onOpInv(Item item, Player player, String command) {
		if (item.getCatalogId() == ItemId.GIANNE_COOK_BOOK.id()) {
			player.message("you open aluft's cook book");
			player.message("inside are various gnome dishes");
			int menu = multi(player,
				"gnomebattas",
				"gnomebakes",
				"gnomecrunchies");
			if (menu == 0) {
				int battaMenu = multi(player,
					"cheese and tomato batta",
					"toad batta",
					"worm batta",
					"fruit batta",
					"veg batta");
				if (battaMenu == 0) {
					ActionSender.sendBox(player, "@yel@Cheese and tomato batta% %Make some gnome batta dough from the Gianne dough% %Bake the gnome batta, once removed place cheese and then tomato on top% %Place batta in oven once more untill cheese has melted, remove and top with equaleaves.", true);
				} else if (battaMenu == 1) {
					ActionSender.sendBox(player, "@yel@Toad batta% %Make some gnome batta dough from the Gianne dough% %Bake the gnome batta, mix some equa leaves with your toad's legs and then add some gnomespice% %Place the seasoned toads legs on the batta, add cheese and bake once more.", true);
				} else if (battaMenu == 2) {
					ActionSender.sendBox(player, "@yel@Worm batta% %Make some gnome batta dough from the Gianne dough% %Bake the gnome batta, mix some gnomespice with a king worm% %Place the seasoned worm on the batta, add cheese and bake once more% %Remove from oven and finish with a sprinkle of equaleaves...yum.", true);
				} else if (battaMenu == 3) {
					ActionSender.sendBox(player, "@yel@Fruit batta% %Make some gnome batta dough from the Gianne dough% %Bake the gnome batta and remove from oven, then lay four sprigs of equa leaves on the batta and bake once more% %Add chunks of pineapple, orange and lime then finish with a sprinkle of gnomespice.", true);
				} else if (battaMenu == 4) {
					ActionSender.sendBox(player, "@yel@Veg Batta% %Make some gnome batta dough from the Gianne dough% %Bake the gnome batta then add an onion, two tomatos, one cabbage and some dwellberrys, next place the batta in the oven% %Add some cheese and place in the oven once more% %To finish add a sprinkle of equa leaves.", true);
				}
			} else if (menu == 1) {
				int bakesMenu = multi(player,
					"choc bomb",
					"veg ball",
					"wormhole",
					"tangled toads legs");
				if (bakesMenu == 0) {
					ActionSender.sendBox(player, "@yel@Choc bomb% %Make some gnomebowl dough from the Gianne dough% %Bake the gnome bowl% %Add to the gnomebowl four bars of chocolate and one sprig of equaleaves% %Bake the gnome bowl in an oven% %Next add two portions of cream and finish with a sprinkle of chocolate dust.", true);
				} else if (bakesMenu == 1) {
					ActionSender.sendBox(player, "@yel@Vegball% %Make some gnomebowl dough from the Gianne dough% %Bake the gnomebowl% %Add two onions,two potatoes and some gnome spice% %Bake the gnomebowl once more% %To finish sprinkle with equaleaves", true);
				} else if (bakesMenu == 2) {
					ActionSender.sendBox(player, "@yel@Worm hole% %Make some gnomebowl dough from the Gianne dough% %Bake the gnomebowl% %Add six king worms, two onions and some gnome spice% %Bake the gnomebowl once more% %To finish sprinkle with equaleaves", true);
				} else if (bakesMenu == 3) {
					ActionSender.sendBox(player, "@yel@Tangled toads legs% %Make some gnomebowl dough from the Gianne dough% %Bake the gnomebowl% %Add two portions of cheese, five pairs of toad's legs, two sprigs of equa leaves, some dwell berries and two sprinkle's of gnomespice% %Bake the gnomebowl once more", true);
				}
			} else if (menu == 2) {
				int crunchiesMenu = multi(player,
					"choc crunchies",
					"worm crunchies",
					"toad crunchies",
					"spice crunchies");
				if (crunchiesMenu == 0) {
					ActionSender.sendBox(player, "@yel@choc crunchies% %Mix some gnome spice and two bars of chocolate with the Gianne dough% %Use dough to make gnomecrunchie dough% %Bake in oven% %Add of sprinkle of chocolate dust", true);
				} else if (crunchiesMenu == 1) {
					ActionSender.sendBox(player, "@yel@worm crunchies% %Mix some gnome spice, two king worms and some equa leaves with the Gianne dough% %Use dough to make gnomecrunchie dough% %Bake in oven% %Add of sprinkle of gnome spice", true);
				} else if (crunchiesMenu == 2) {
					ActionSender.sendBox(player, "@yel@toad crunchies% %Mix some gnome spice and two pair's of toads legs with the Gianne dough% %Use dough to make gnomecrunchie dough% %Bake in oven% %Add of sprinkle of equa leaves", true);
				} else if (crunchiesMenu == 3) {
					ActionSender.sendBox(player, "@yel@spice crunchies% %Mix three sprinkles of gnomespice and two sprigs of equa leaves with Gianne dough% %Use dough to make gnomecrunchie dough% %Bake in oven% %Add of sprinkle of gnome spice", true);
				}
			}
		}
	}

	@Override
	public void onDropObj(Player player, Item i, Boolean fromInventory) {
		if (i.getCatalogId() == ItemId.GNOMECRUNCHIE.id() || i.getCatalogId() == ItemId.GNOMEBOWL.id() || i.getCatalogId() == ItemId.GNOMEBATTA.id()) {
			resetGnomeCooking(player);
		}
	}

	@Override
	public boolean blockDropObj(Player player, Item i, Boolean fromInventory) {
		return false;
	}
}
