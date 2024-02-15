/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.config;

import com.google.common.io.Files;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import git.jbredwards.subaquatic.mod.common.recipe.BlockSoakRecipe;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionType;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.items.ItemHandlerHelper;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author jbred
 *
 */
public final class SubaquaticBlockSoakRecipesConfig
{
    @SuppressWarnings("UnstableApiUsage")
    public static void buildRecipes() throws IOException {
        final File file = new File("config/subaquatic", "block_soak_recipes.jsonc");
        //read from existing
        try { parseRecipes(new FileReader(file)); }
        catch(FileNotFoundException e) {
            //generate from default values
            parseRecipes(new StringReader(defaultRecipes));

            //create new file
            Files.createParentDirs(file);
            if(file.createNewFile()) {
                final FileWriter writer = new FileWriter(file);
                writer.write(defaultRecipes);
                writer.close();
            }
        }
    }

    static void parseRecipes(@Nonnull Reader reader) {
        new JsonParser().parse(reader).getAsJsonArray().forEach(jsonIn -> {
            final JsonObject json = jsonIn.getAsJsonObject();

            //reads the list of potions
            final List<PotionType> potions = new LinkedList<>();
            if(!json.has("potions")) potions.add(PotionTypes.WATER);
            else JsonUtils.getJsonArray(json, "potions").forEach(element -> potions.add(
                    Objects.requireNonNull(PotionType.getPotionTypeForName(element.getAsString()))));

            //reads the list of inputs
            final List<Pair<IBlockState, ItemStack>> inputs = new LinkedList<>();
            JsonUtils.getJsonArray(json, "inputs").forEach(elementIn -> {
                final JsonObject input = elementIn.getAsJsonObject();
                final Block block = Objects.requireNonNull(Block.getBlockFromName(JsonUtils.getString(input, "blockId")));
                final int metadata = input.has("metadata") ? JsonUtils.getInt(input, "metadata") : 0;

                inputs.add(Pair.of(block.getStateFromMeta(metadata), readStack(block, metadata, input)));
            });

            //reads the output
            final JsonObject output = JsonUtils.getJsonObject(json, "output");
            final Block block = Objects.requireNonNull(Block.getBlockFromName(JsonUtils.getString(output, "blockId")));
            final int metadata = output.has("metadata") ? JsonUtils.getInt(output, "metadata") : 0;

            //adds the newly created recipe
            BlockSoakRecipe.RECIPES.add(new BlockSoakRecipe(potions, inputs, Pair.of(block.getStateFromMeta(metadata), readStack(block, metadata, output))));
        });
    }

    @Nonnull
    static ItemStack readStack(@Nonnull Block block, int metadata, @Nonnull JsonObject json) {
        if(!json.has("jeiStack")) return new ItemStack(block, 1, metadata);
        return ItemHandlerHelper.copyStackWithSize(new ItemStack(Objects.requireNonNull(
                net.minecraftforge.common.util.JsonUtils.readNBT(json, "jeiStack"))), 1);
    }

    @Nonnull
    public static final String defaultRecipes =
            "[\n" +
            "    //========\n" +
            "    // Guide:\n" +
            "    //======== \n" +
            "    /*\n" +
            "    Each recipe can have the following arguments:\n" +
            "\n" +
            "    (optional) potions:\n" +
            "    - A string array of the valid PotionTypes that can be used for the recipe (defaults to \"minecraft:water\").\n" +
            "    \n" +
            "    (required) inputs:\n" +
            "    - Each input consists of a block (required), and an jeiStack (optional, defaults to Item.getItemFromBlock() + block metadata).\n" +
            "      The block consists of the blockId (required) and metadata (optional, defaults to 0)\n" +
            "      The jeiStack consists of the ItemStack, and has no impact on the recipe in-game. It is only used for jei integration\n" +
            "      Full example: {\"blockId\":\"stained_glass\",\"metadata\":13,\"item\":{\"id\":\"wool\",\"Damage\":4}}\n" +
            "\n" +
            "    (required) output:\n" +
            "    - Consists of a blockId (required), metadata (optional, defaults to 0), and jeiStack (optional, defaults to Item.getItemFromBlock() + block metadata)\n" +
            "    */\n" +
            "\n" +
            "    //===================\n" +
            "    // Built-in Recipes:\n" +
            "    //===================\n" +
            "    {\n" +
            "        \"inputs\":[{\"blockId\":\"dirt\"},{\"blockId\":\"dirt\",\"metadata\":1},{\"blockId\":\"subaquatic:rooted_dirt\"}],\n" +
            "        \"output\":{\"blockId\":\"subaquatic:mud\"}\n" +
            "    },\n" +
            "    {\n" +
            "        \"inputs\":[{\"blockId\":\"magma\"}],\n" +
            "        \"output\":{\"blockId\":\"obsidian\"}\n" +
            "    },\n" +
            "    {\n" +
            "        \"inputs\":[{\"blockId\":\"sponge\"}],\n" +
            "        \"output\":{\"blockId\":\"sponge\",\"metadata\":1}\n" +
            "    },\n" +
            "    {\n" +
            "        \"inputs\":[{\"blockId\":\"concrete_powder\"}],\n" +
            "        \"output\":{\"blockId\":\"concrete\"}\n" +
            "    },\n" +
            "    {\n" +
            "        \"inputs\":[{\"blockId\":\"concrete_powder\",\"metadata\":1}],\n" +
            "        \"output\":{\"blockId\":\"concrete\",\"metadata\":1}\n" +
            "    },\n" +
            "    {\n" +
            "        \"inputs\":[{\"blockId\":\"concrete_powder\",\"metadata\":2}],\n" +
            "        \"output\":{\"blockId\":\"concrete\",\"metadata\":2}\n" +
            "    },\n" +
            "    {\n" +
            "        \"inputs\":[{\"blockId\":\"concrete_powder\",\"metadata\":3}],\n" +
            "        \"output\":{\"blockId\":\"concrete\",\"metadata\":3}\n" +
            "    },\n" +
            "    {\n" +
            "        \"inputs\":[{\"blockId\":\"concrete_powder\",\"metadata\":4}],\n" +
            "        \"output\":{\"blockId\":\"concrete\",\"metadata\":4}\n" +
            "    },\n" +
            "    {\n" +
            "        \"inputs\":[{\"blockId\":\"concrete_powder\",\"metadata\":5}],\n" +
            "        \"output\":{\"blockId\":\"concrete\",\"metadata\":5}\n" +
            "    },\n" +
            "    {\n" +
            "        \"inputs\":[{\"blockId\":\"concrete_powder\",\"metadata\":6}],\n" +
            "        \"output\":{\"blockId\":\"concrete\",\"metadata\":6}\n" +
            "    },\n" +
            "    {\n" +
            "        \"inputs\":[{\"blockId\":\"concrete_powder\",\"metadata\":7}],\n" +
            "        \"output\":{\"blockId\":\"concrete\",\"metadata\":7}\n" +
            "    },\n" +
            "    {\n" +
            "        \"inputs\":[{\"blockId\":\"concrete_powder\",\"metadata\":8}],\n" +
            "        \"output\":{\"blockId\":\"concrete\",\"metadata\":8}\n" +
            "    },\n" +
            "    {\n" +
            "        \"inputs\":[{\"blockId\":\"concrete_powder\",\"metadata\":9}],\n" +
            "        \"output\":{\"blockId\":\"concrete\",\"metadata\":9}\n" +
            "    },\n" +
            "    {\n" +
            "        \"inputs\":[{\"blockId\":\"concrete_powder\",\"metadata\":10}],\n" +
            "        \"output\":{\"blockId\":\"concrete\",\"metadata\":10}\n" +
            "    },\n" +
            "    {\n" +
            "        \"inputs\":[{\"blockId\":\"concrete_powder\",\"metadata\":11}],\n" +
            "        \"output\":{\"blockId\":\"concrete\",\"metadata\":11}\n" +
            "    },\n" +
            "    {\n" +
            "        \"inputs\":[{\"blockId\":\"concrete_powder\",\"metadata\":12}],\n" +
            "        \"output\":{\"blockId\":\"concrete\",\"metadata\":12}\n" +
            "    },\n" +
            "    {\n" +
            "        \"inputs\":[{\"blockId\":\"concrete_powder\",\"metadata\":13}],\n" +
            "        \"output\":{\"blockId\":\"concrete\",\"metadata\":13}\n" +
            "    },\n" +
            "    {\n" +
            "        \"inputs\":[{\"blockId\":\"concrete_powder\",\"metadata\":14}],\n" +
            "        \"output\":{\"blockId\":\"concrete\",\"metadata\":14}\n" +
            "    },\n" +
            "    {\n" +
            "        \"inputs\":[{\"blockId\":\"concrete_powder\",\"metadata\":15}],\n" +
            "        \"output\":{\"blockId\":\"concrete\",\"metadata\":15}\n" +
            "    }\n" +
            "]";
}
