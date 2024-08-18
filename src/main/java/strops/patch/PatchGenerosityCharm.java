//Adapted from the Bossy Relics mod, credits to Camputer!
package strops.patch;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.CallingBell;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;
import strops.relics.GenerosityCharm;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.*;
import static strops.modcore.Strops.logger;

@SpirePatch(clz = AbstractRoom.class, method = "addRelicToRewards", paramtypez = {AbstractRelic.RelicTier.class})
public class PatchGenerosityCharm {
    public static ArrayList<String> common_relics_stable=new ArrayList<>();

    public static ArrayList<String> uncommon_relics_stable=new ArrayList<>();

    public static ArrayList<String> rare_relics_stable=new ArrayList<>();

    //public static boolean isHolding=false;

    private static final List<AbstractRelic.RelicTier> RELIC_TIERS = Arrays.asList(AbstractRelic.RelicTier.COMMON, AbstractRelic.RelicTier.UNCOMMON, AbstractRelic.RelicTier.RARE);

    private static ArrayList<RewardItem> currentRewards;

    private static final List<List<RewardItem>> linkedRewardItems = new LinkedList<>();

    private static void addLinkedRelics(ArrayList<RewardItem> rewards) {
        try {
            if (rewards == currentRewards) {
                logger.info("Skipping adding linked relics, already handled this rewards list.");
                return;
            }
            logger.info("Adding linked relics for rewards. Clearing linked relic list.");
            linkedRewardItems.clear();
            currentRewards = rewards;
            ListIterator<RewardItem> iterator = rewards.listIterator();
            while (iterator.hasNext()) {
                RewardItem rewardItem = iterator.next();
                if (rewardItem.relic != null&&RELIC_TIERS.contains(rewardItem.relic.tier)) {
                    player.getRelic(GenerosityCharm.ID).flash();

                    List<RewardItem> linkedSet = new ArrayList<>(3);
                    linkedSet.add(rewardItem);

                    Random reliableRelicRng=relicRng;
                    for (int i = 0; i < GenerosityCharm.CHOICES.value-1; i++) {
                        AbstractRelic.RelicTier addedTier=rewardItem.relic.tier;
                        AbstractRelic newRelic = AbstractDungeon.returnRandomRelic(addedTier);
                        logger.info("Adding new relic to rewards for tier {}: {}", addedTier, newRelic);
                        RewardItem newRewardItem = new RewardItem(newRelic);
                        iterator.add(newRewardItem);
                        linkedSet.add(newRewardItem);
                    }
                    relicRng=reliableRelicRng;
                    linkedRewardItems.add(linkedSet);
                }
            }
            logger.info("Linked relic rewards: {}", linkedRelicsToString());
        } catch (Exception e) {
            logger.error("Oops! BossyRelics failed to add new relics to rewards.", e);
        }
    }

    @SpirePatch(clz = RewardItem.class, method = "claimReward")
    public static class RewardItemClaimRewardPatch {
        public static boolean Postfix(boolean result, RewardItem _instance) {
            try {
                logger.info("Reward claimed (type: {}, relic: {}). isDone: {}, ignoreReward: {}. claimReward returned: {}, Linked relics: {}", _instance.type, _instance.relic,

                        _instance.isDone, _instance.ignoreReward, result, linkedRelicsToString());
                List<RewardItem> linkedRewards = null;
                for (List<RewardItem> items : linkedRewardItems) {
                    if (items.contains(_instance)) {
                        linkedRewards = items;
                        break;
                    }
                }
                if (_instance.type == RewardItem.RewardType.SAPPHIRE_KEY &&
                        !_instance.ignoreReward) {
                    logger.info("Sapphire Key claimed. Removing linked relics. num relic sets: {}", linkedRewardItems.size());
                    linkedRewards = linkedRewardItems.get(linkedRewardItems.size() - 1);
                }
                if (linkedRewards == null)
                    return result;
                for (RewardItem linkedRewardItem : linkedRewards) {
                    if (linkedRewardItem != _instance) {
                        logger.info("Discarding linked relic {}", linkedRewardItem.relic);
                        linkedRewardItem.isDone = true;
                        linkedRewardItem.ignoreReward = true;
                        doneLinkedRewards.add(linkedRewardItem);

                        if(_instance.relicLink==null||linkedRewardItem.relic!=_instance.relicLink.relic){
                            int rng;
                            switch (linkedRewardItem.relic.tier){
                                case COMMON:
                                    rng=GenerosityCharm.generosityCharmRng.random(commonRelicPool.size());
                                    commonRelicPool.add(rng,linkedRewardItem.relic.relicId);
                                    logger.info("The relic {} has been put back into the common relic pool",linkedRewardItem.relic);
                                    break;
                                case UNCOMMON:
                                    rng=GenerosityCharm.generosityCharmRng.random(uncommonRelicPool.size());
                                    uncommonRelicPool.add(rng,linkedRewardItem.relic.relicId);
                                    logger.info("The relic {} has been put back into the uncommon relic pool",linkedRewardItem.relic);
                                    break;
                                case RARE:
                                    rng=GenerosityCharm.generosityCharmRng.random(rareRelicPool.size());
                                    rareRelicPool.add(rng,linkedRewardItem.relic.relicId);
                                    logger.info("The relic {} has been put back into the rare relic pool",linkedRewardItem.relic);
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                }
                linkedRewardItems.remove(linkedRewards);
                linkedRewards.clear();
            } catch (Exception e) {
                logger.error("Oops! BossyRelics failed to handle reward claimed: {}", _instance, e);
            }
            return result;
        }
    }

    private static final List<RewardItem> doneLinkedRewards = new ArrayList<>();

    @SpirePatch(clz = CombatRewardScreen.class, method = "rewardViewUpdate")
    public static class CombatRewardScreenRewardViewUpdatePatch {
        public static void Postfix(CombatRewardScreen _instance) {
            ListIterator<RewardItem> iterator = doneLinkedRewards.listIterator();
            while (iterator.hasNext()) {
                RewardItem rewardItem = iterator.next();
                if (!rewardItem.isDone) {
                    rewardItem.isDone = true;
                    continue;
                }
                if (_instance.rewards.contains(rewardItem)) {
                    logger.info("CombatRewardsScreen still contains {}", rewardItem.relic);
                    continue;
                }
                logger.info("CombatRewardsScreen no longer contains {}", rewardItem.relic);
                iterator.remove();
            }
        }
    }

    @SpirePatch(clz = RewardItem.class, method = "render")
    public static class RewardItemRenderPatch {
        public static void Postfix(RewardItem _instance, SpriteBatch sb) {
            if (_instance.relic == null)
                return;
            for (List<RewardItem> rewardItems : linkedRewardItems) {
                if (isRewardItemAfterFirstItemInList(_instance, rewardItems))
                    try {
                        Method renderRelicLinkMethod = RewardItem.class.getDeclaredMethod("renderRelicLink", SpriteBatch.class);
                        renderRelicLinkMethod.setAccessible(true);
                        renderRelicLinkMethod.invoke(_instance, sb);
                    } catch (NoSuchMethodException ex) {
                        logger.error("Oops! BossyRelics renderRelicLink method not found.", ex);
                    } catch (Exception ex) {
                        logger.error("Error while trying to invoke renderRelicLink method.", ex);
                    }
            }
        }
    }

    private static boolean isRewardItemAfterFirstItemInList(RewardItem item, List<RewardItem> list) {
        return (list.contains(item) && list.get(0) != item);
    }

    private static boolean addCallingBellRewards = false;

    @SpirePatch(clz = CallingBell.class, method = "update")
    public static class CallingBellUpdatePatch {
        public static void Postfix(CallingBell _instance) {
            try {
                if (addCallingBellRewards) {
                    ArrayList<RewardItem> roomRewards = AbstractDungeon.combatRewardScreen.rewards;
                    if (roomRewards.size() == 3) {
                        logger.info("Adding linked relics for Calling Bell relics: {}", rewardsToString(roomRewards));
                        currentRewards = null;
                        addLinkedRelics(roomRewards);
                        AbstractDungeon.combatRewardScreen.positionRewards();
                        addCallingBellRewards = false;
                    }
                }
            } catch (Exception e) {
                logger.error("Oops! BossyRelics failed to add relic rewards for Calling Bell.", e);
            }
        }
    }

    @SpirePatch(clz = CallingBell.class, method = "onEquip")
    public static class CallingBellOnEquipPatch {
        public static void Postfix(CallingBell _instance) {
            if(player.hasRelic(GenerosityCharm.ID)){
                player.getRelic(GenerosityCharm.ID).flash();
                addCallingBellRewards = true;
            }
        }
    }

    @SpirePatch(clz = CombatRewardScreen.class, method = "setupItemReward")
    public static class CombatRewardScreenPatch {
        public static void Postfix(CombatRewardScreen _instance) {
            common_relics_stable.clear();
            uncommon_relics_stable.clear();
            rare_relics_stable.clear();
            common_relics_stable.addAll(commonRelicPool);
            uncommon_relics_stable.addAll(uncommonRelicPool);
            rare_relics_stable.addAll(rareRelicPool);
            /*
            if(!loading_post_combat){
                isHolding=true;
                GenerosityCharm.heldCounter=GenerosityCharm.generosityCharmRng.counter;
            }

             */

            if(player.hasRelic(GenerosityCharm.ID)){
                logger.info("CombatRewardScreen#setupItemReward patch triggered.");
                addLinkedRelics(_instance.rewards);
                _instance.positionRewards();
            }
        }
    }

    private static String rewardsToString(List<RewardItem> rewardItems) {
        return rewardItems.stream().map(item -> item.relic).collect(Collectors.toList()).toString();
    }

    private static String linkedRelicsToString() {
        return linkedRewardItems.stream().map(PatchGenerosityCharm::rewardsToString).collect(Collectors.toList()).toString();
    }

    @SpirePatch(clz = AbstractRoom.class, method = "update")
    public static class PatchTool1 {
        @SpireInsertPatch(rloc = 190,localvars = {"saveFile"})
        public static void Insert(AbstractRoom __inst, SaveFile saveFile){
            saveFile.common_relics=common_relics_stable;
            saveFile.uncommon_relics=uncommon_relics_stable;
            saveFile.rare_relics=rare_relics_stable;
        }
    }
}
