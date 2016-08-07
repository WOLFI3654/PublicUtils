package de.wolfi.utils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

@Deprecated
public final class TargetAPI {

	private static Set<Entity> getNearbyPlayers(Location to, double maxDistance, Collection<Entity> through,
			EntityType et) {
		Set<Entity> nearbyPlayers = new HashSet<>();

		for (Entity nearby : through) {
			if (!nearby.getType().equals(et))
				continue;
			if (nearby.getLocation().distanceSquared(to) > maxDistance * maxDistance)
				continue;
			nearbyPlayers.add(nearby);
		}
		return nearbyPlayers;
	}

	private static Set<Entity> getNearbyPlayers(Location to, double maxDistance, EntityType et) {
		Collection<Entity> playersInSameWorld = to.getWorld().getEntitiesByClasses(et.getEntityClass());
		return TargetAPI.getNearbyPlayers(to, maxDistance, playersInSameWorld, et);
	}

	public static Entity getTarget(Player player, EntityType etype) {
		final double maxDistance = 100.0;
		final double precision = 1.5;
		final Location nearbyToPlayer = player.getLocation(); // this is the
																// starting
																// point of a
																// ray

		Set<Entity> nearbyToStart = TargetAPI.getNearbyPlayers(nearbyToPlayer, maxDistance, etype);

		final Vector direction = player.getLocation().getDirection();
		for (int i = 0; i < maxDistance; i += precision) { // then for a
															// distance as big
															// as our specified
															// maximum
			Vector offset = direction.clone().multiply(i);
			Location pointCheck = nearbyToPlayer.clone().add(offset);// we check
																		// a lot
																		// of
																		// points
																		// on
																		// the
																		// line
																		// depending
																		// on
																		// the
																		// precision
			Set<Entity> nearbyToPoint = TargetAPI.getNearbyPlayers(pointCheck, precision, nearbyToStart, etype); // to
																													// see
																													// if
																													// there
																													// are
																													// players
																													// near
																													// that
																													// point

			if (!nearbyToPoint.isEmpty()) { // if there is at least one
				for (Entity en : nearbyToPoint) {
					if (en != player)
						return en;
				}
			}
		}
		return null;
	}

}
