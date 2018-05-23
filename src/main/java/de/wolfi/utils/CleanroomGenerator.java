package de.wolfi.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

public class CleanroomGenerator extends ChunkGenerator {
	private short[] layer;
	private byte[] layerDataValues;
	private Logger log = Logger.getLogger("Minecraft");

	public CleanroomGenerator() {
		this("64,stone");
	}

	@SuppressWarnings("deprecation")
	public CleanroomGenerator(String id) {
		if (id != null) {
			try {
				int y = 0;

				this.layer = new short[128]; // Default to 128, will be resized
												// later if required
				this.layerDataValues = null;

				if (id.length() > 0 && id.charAt(0) == '.') // Is the first
															// character a '.'?
															// If so, skip
															// bedrock
															// generation.
				{
					id = id.substring(1); // Skip bedrock then and remove the .

				} else // Guess not, bedrock at layer0 it is then.
				{
					this.layer[y++] = (short) Material.BEDROCK.getId();
				}

				if (id.length() > 0) {
					String tokens[] = id.split("[,]");

					if (tokens.length % 2 != 0)
						throw new Exception();

					for (int i = 0; i < tokens.length; i += 2) {
						int height = Integer.parseInt(tokens[i]);
						if (height <= 0) {
							this.log.warning(
									"[CleanroomGenerator] Invalid height '" + tokens[i] + "'. Using 64 instead.");
							height = 64;
						}

						String materialTokens[] = tokens[i + 1].split("[:]", 2);
						byte dataValue = 0;
						if (materialTokens.length == 2) {
							try {
								// Lets try to read the data value
								dataValue = Byte.parseByte(materialTokens[1]);
							} catch (Exception e) {
								this.log.warning("[CleanroomGenerator] Invalid Data Value '" + materialTokens[1]
										+ "'. Defaulting to 0.");
								dataValue = 0;
							}
						}
						Material mat = Material.matchMaterial(materialTokens[0]);
						if (mat == null) {
							try {
								// Mabe it's an integer?
								mat = Material.getMaterial(Integer.parseInt(materialTokens[0]));
							} catch (Exception e) {
								// Well, I guess it wasn't an integer after
								// all... Awkward...
							}

							if (mat == null) {
								this.log.warning("[CleanroomGenerator] Invalid Block ID '" + materialTokens[0]
										+ "'. Defaulting to stone.");
								mat = Material.STONE;
							}
						}

						if (!mat.isBlock()) {
							this.log.warning("[CleanroomGenerator] Error, '" + materialTokens[0]
									+ "' is not a block. Defaulting to stone.");
							mat = Material.STONE;
						}

						if (y + height > this.layer.length) {
							short[] newLayer = new short[Math.max(y + height, this.layer.length * 2)];
							System.arraycopy(this.layer, 0, newLayer, 0, y);
							this.layer = newLayer;
							if (this.layerDataValues != null) {
								byte[] newLayerDataValues = new byte[Math.max(y + height,
										this.layerDataValues.length * 2)];
								System.arraycopy(this.layerDataValues, 0, newLayerDataValues, 0, y);
								this.layerDataValues = newLayerDataValues;
							}
						}

						Arrays.fill(this.layer, y, y + height, (short) mat.getId());
						if (dataValue != 0) {
							if (this.layerDataValues == null) {
								this.layerDataValues = new byte[this.layer.length];
							}
							Arrays.fill(this.layerDataValues, y, y + height, dataValue);
						}
						y += height;
					}
				}

				// Trim to size
				if (this.layer.length > y) {
					short[] newLayer = new short[y];
					System.arraycopy(this.layer, 0, newLayer, 0, y);
					this.layer = newLayer;
				}
				if (this.layerDataValues != null && this.layerDataValues.length > y) {
					byte[] newLayerDataValues = new byte[y];
					System.arraycopy(this.layerDataValues, 0, newLayerDataValues, 0, y);
					this.layerDataValues = newLayerDataValues;
				}
			} catch (Exception e) {
				this.log.severe("[CleanroomGenerator] Error parsing CleanroomGenerator ID '" + id
						+ "'. using defaults '64,1': " + e.toString());
				e.printStackTrace();
				this.layerDataValues = null;
				this.layer = new short[65];
				this.layer[0] = (short) Material.BEDROCK.getId();
				Arrays.fill(this.layer, 1, 65, (short) Material.STONE.getId());
			}
		} else {
			this.layerDataValues = null;
			this.layer = new short[65];
			this.layer[0] = (short) Material.BEDROCK.getId();
			Arrays.fill(this.layer, 1, 65, (short) Material.STONE.getId());
		}
	}

	@SuppressWarnings("deprecation")
	@Deprecated
	@Override
	public short[][] generateExtBlockSections(World world, Random random, int x, int z, BiomeGrid biomes) {
		int maxHeight = world.getMaxHeight();
		if (this.layer.length > maxHeight) {
			this.log.warning("[CleanroomGenerator] Error, chunk height " + this.layer.length
					+ " is greater than the world max height (" + maxHeight + "). Trimming to world max height.");
			short[] newLayer = new short[maxHeight];
			System.arraycopy(this.layer, 0, newLayer, 0, maxHeight);
			this.layer = newLayer;
		}
		short[][] result = new short[maxHeight / 16][]; // 16x16x16 chunks
		for (int i = 0; i < this.layer.length; i += 16) {
			result[i >> 4] = new short[4096];
			for (int y = 0; y < Math.min(16, this.layer.length - i); y++) {
				Arrays.fill(result[i >> 4], y * 16 * 16, (y + 1) * 16 * 16, this.layer[i + y]);
			}
		}

		return result;
	}

	@Override
	public List<BlockPopulator> getDefaultPopulators(World world) {
		if (this.layerDataValues != null) {
			return Arrays.asList((BlockPopulator) new CleanroomPulolator(this.layerDataValues));
		} else {
			// This is the default, but just in case default populators change
			// to stock minecraft populators by default...
			return new ArrayList<BlockPopulator>();
		}
	}

	@Override
	public Location getFixedSpawnLocation(World world, Random random) {
		if (!world.isChunkLoaded(0, 0)) {
			world.loadChunk(0, 0);
		}

		if (world.getHighestBlockYAt(0, 0) <= 0 && world.getBlockAt(0, 0, 0).getType() == Material.AIR) // SPACE!
		{
			world.getBlockAt(0, 0, 0).setType(Material.GOLD_BLOCK);
			return new Location(world, 0, 64, 0); // Lets allow people to drop a
													// little before hitting the
													// void then shall we?
		}

		return new Location(world, 0, world.getHighestBlockYAt(0, 0), 0);
	}
}
