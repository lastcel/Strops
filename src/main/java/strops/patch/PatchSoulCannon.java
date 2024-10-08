package strops.patch;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.saveAndContinue.SaveAndContinue;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import strops.cards.AbstractStropsCard;
import strops.relics.StropsAbstractRelic;
import strops.utilities.ExtendedSaveFile;

import java.util.HashMap;

import static basemod.BaseMod.gson;

public class PatchSoulCannon {

    @SpirePatch(
            clz= AbstractDungeon.class,
            method=SpirePatch.CLASS
    )
    public static class PatchTool1{
        public static StaticSpireField<Integer> notUsedCannonTurn= new StaticSpireField<>(() -> 0);
    }

    @SpirePatch(
            clz= SaveFile.class,
            method=SpirePatch.CLASS
    )
    public static class PatchTool2{
        public static SpireField<Integer> not_used_cannon_turn= new SpireField<>(()->0);
    }

    @SpirePatch(
            clz= SaveFile.class,
            method=SpirePatch.CONSTRUCTOR,
            paramtypez = {SaveFile.SaveType.class}
    )
    public static class PatchTool3 {
        @SpireInsertPatch(rloc = 67)
        public static void Insert(SaveFile __instance, SaveFile.SaveType type) {
            PatchTool2.not_used_cannon_turn.set(__instance, PatchTool1.notUsedCannonTurn.get());
        }
    }

    @SpirePatch(
            clz= SaveAndContinue.class,
            method="save"
    )
    public static class PatchTool4 {
        @SpireInsertPatch(rloc = 92,localvars = {"params"})
        public static void Insert(SaveFile save,@ByRef HashMap<Object, Object>[] params) {
            params[0].put("not_used_cannon_turn", PatchTool2.not_used_cannon_turn.get(save));
        }
    }

    @SpirePatch(
            clz= AbstractDungeon.class,
            method="loadSave"
    )
    public static class PatchTool5 {
        @SpireInsertPatch(rloc = 2)
        public static void Insert(AbstractDungeon __instance, SaveFile saveFile) {
            PatchTool1.notUsedCannonTurn.set(PatchTool2.not_used_cannon_turn.get(saveFile));
            //BaseMod.logger.info("加载地牢存档");
            for(AbstractRelic r:AbstractDungeon.player.relics){
                //BaseMod.logger.info("遗物="+r.name);
                if(AbstractStropsCard.CANNON_RELICS.contains(r.relicId)){
                    //BaseMod.logger.info("检测到二炮");
                    ((StropsAbstractRelic)r).updateFormat();
                    break;
                }
            }
        }
    }

    @SpirePatch(
            clz= SaveAndContinue.class,
            method="loadSaveFile",
            paramtypez={String.class}
    )
    public static class PatchTool6 {
        @SpireInsertPatch(rloc=23,localvars={"savestr","saveFile"})
        public static void Insert(String filePath,String savestr,SaveFile saveFile) {
            ExtendedSaveFile saveFilePlus;
            saveFilePlus=gson.fromJson(savestr,ExtendedSaveFile.class);
            PatchTool2.not_used_cannon_turn.set(saveFile,saveFilePlus.not_used_cannon_turn);
        }
    }

    @SpirePatch(
            clz= AbstractDungeon.class,
            method="reset"
    )
    public static class PatchTool7 {
        @SpireInsertPatch(rloc=3)
        public static void Insert() {
            PatchTool1.notUsedCannonTurn.set(0);
        }
    }

    @SpirePatch(
            clz= GameActionManager.class,
            method="getNextAction"
    )
    public static class PatchTool8 {
        @SpireInsertPatch(rloc = 185)
        public static void Insert(GameActionManager __inst) {
            for(AbstractRelic r:AbstractDungeon.player.relics){
                if(AbstractStropsCard.CANNON_RELICS.contains(r.relicId)){
                    r.onTrigger();
                    break;
                }
            }
        }
    }
}
