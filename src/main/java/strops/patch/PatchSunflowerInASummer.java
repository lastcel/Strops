package strops.patch;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.*;
import com.megacrit.cardcrawl.saveAndContinue.SaveAndContinue;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import strops.helpers.ModHelper;
import strops.potions.PhantasmalShootingStar;
import strops.relics.SunflowerInASummer;
import strops.utilities.ExtendedSaveFile;

import java.util.HashMap;
import java.util.ArrayList;

import static basemod.BaseMod.gson;

public class PatchSunflowerInASummer {

    @SpirePatch(
            clz= AbstractDungeon.class,
            method="generateMap"
    )
    public static class PatchTool1{
        @SpireInsertPatch(rloc=33,localvars={"map"})
        public static void Insert(ArrayList<ArrayList<MapRoomNode>> map){
            if((PatchTool2.everMetSunflower.get())&&
                    (AbstractDungeon.actNum == 2)) {
                for (AbstractRelic r : AbstractDungeon.player.relics) {
                    if (r.relicId.equals(ModHelper.makePath(SunflowerInASummer.class.getSimpleName()))) {
                        r.flash();
                        r.stopPulse();
                        break;
                    }
                }
                for (ArrayList<MapRoomNode> a : map) {
                    for (MapRoomNode n : a) {
                        if(n.room instanceof EventRoom){
                            n.room=new RestRoom();
                        }

                        if(SunflowerInASummer.ISELITELIZE.value==1&&n.room instanceof MonsterRoom&&
                                !(n.room instanceof MonsterRoomElite)&&
                                !(n.room instanceof MonsterRoomBoss)){
                            n.room=new MonsterRoomElite();
                        }
                    }
                }
            }
        }
    }

    @SpirePatch(
            clz= AbstractDungeon.class,
            method=SpirePatch.CLASS
    )
    public static class PatchTool2{
        public static StaticSpireField<Boolean> everMetSunflower= new StaticSpireField<>(() -> false);
    }

    @SpirePatch(
            clz= SaveFile.class,
            method=SpirePatch.CLASS
    )
    public static class PatchTool3{
        public static SpireField<Boolean> ever_met_sunflower= new SpireField<>(()->false);
    }

    @SpirePatch(
            clz= SaveFile.class,
            method=SpirePatch.CONSTRUCTOR,
            paramtypez = {SaveFile.SaveType.class}
    )
    public static class PatchTool4 {
        @SpireInsertPatch(rloc = 67)
        public static void Insert(SaveFile __instance, SaveFile.SaveType type) {
            PatchTool3.ever_met_sunflower.set(__instance, PatchTool2.everMetSunflower.get());
        }
    }

    @SpirePatch(
            clz= SaveAndContinue.class,
            method="save"
    )
    public static class PatchTool5 {
        @SpireInsertPatch(rloc = 92,localvars = {"params"})
        public static void Insert(SaveFile save,@ByRef HashMap<Object, Object>[] params) {
            params[0].put("ever_met_sunflower",PatchTool3.ever_met_sunflower.get(save));
        }
    }

    @SpirePatch(
            clz= AbstractDungeon.class,
            method="loadSave"
    )
    public static class PatchTool6 {
        @SpirePrefixPatch
        public static void Prefix(AbstractDungeon __instance, SaveFile saveFile) {
            PatchTool2.everMetSunflower.set(PatchTool3.ever_met_sunflower.get(saveFile));
            if(PatchTool2.everMetSunflower.get()){
                for(AbstractRelic r:AbstractDungeon.player.relics){
                    if(r.relicId.equals(SunflowerInASummer.ID)){
                        r.flash();
                        r.beginLongPulse();
                        break;
                    }
                }
            }
        }
    }

    @SpirePatch(
            clz= SaveAndContinue.class,
            method="loadSaveFile",
            paramtypez={String.class}
    )
    public static class PatchTool7 {
        @SpireInsertPatch(rloc=23,localvars={"savestr","saveFile"})
        public static void Insert(String filePath,String savestr,SaveFile saveFile) {
            ExtendedSaveFile saveFilePlus;
            saveFilePlus=gson.fromJson(savestr,ExtendedSaveFile.class);
            PatchTool3.ever_met_sunflower.set(saveFile,saveFilePlus.ever_met_sunflower);
        }
    }

    @SpirePatch(
            clz= AbstractDungeon.class,
            method="reset"
    )
    public static class PatchTool8 {
        @SpireInsertPatch(rloc=3)
        public static void Insert() {
            PatchTool2.everMetSunflower.set(false);
        }
    }

    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "dungeonTransitionSetup"
    )
    public static class PatchTool9 {
        @SpirePostfixPatch
        public static void Postfix() {
            for(AbstractPotion p:AbstractDungeon.player.potions){
                if(p instanceof PhantasmalShootingStar){
                    AbstractDungeon.topPanel.destroyPotion(p.slot);
                }
            }
        }
    }
}
