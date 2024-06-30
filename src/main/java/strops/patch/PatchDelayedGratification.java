//对应遗物“延迟的满足感”有一部分Patch工作是在“燕雀”的Patch中做的
package strops.patch;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import strops.actions.EnableCanDelayAction;

import static strops.relics.DelayedGratification.canBeDelayed;

public class PatchDelayedGratification {

    @SpirePatch(
            clz= AbstractRoom.class,
            method="update"
    )
    public static class PatchTool1 {
        @SpireInsertPatch(rloc = 41)
        public static void Insert(AbstractRoom __instance) {
            canBeDelayed = false;
        }
    }

    @SpirePatch(
            clz= AbstractRoom.class,
            method="update"
    )
    public static class PatchTool2 {
        @SpireInsertPatch(rloc = 65)
        public static void Insert(AbstractRoom __instance) {
            AbstractDungeon.actionManager.addToBottom(new EnableCanDelayAction());
        }
    }

    @SpirePatch(
            clz= GameActionManager.class,
            method="getNextAction"
    )
    public static class PatchTool3 {
        @SpireInsertPatch(rloc = 227)
        public static void Insert(GameActionManager __instance) {
            canBeDelayed = false;
        }
    }

    @SpirePatch(
            clz= GameActionManager.class,
            method="getNextAction"
    )
    public static class PatchTool4 {
        @SpireInsertPatch(rloc = 261)
        public static void Insert(GameActionManager __instance) {
            AbstractDungeon.actionManager.addToBottom(new EnableCanDelayAction());
        }
    }
}
