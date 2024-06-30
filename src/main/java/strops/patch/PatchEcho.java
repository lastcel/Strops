package strops.patch;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.ui.panels.PotionPopUp;
import strops.modcore.Strops;
import strops.relics.Echo;

public class PatchEcho {

    @SpirePatch(
            clz= PotionPopUp.class,
            method="updateTargetMode"
    )
    public static class PatchTool1 {
        @SpireInsertPatch(rloc = 26)
        public static void Insert(PotionPopUp __inst, AbstractPotion ___potion) {
            Strops.lastPotion=___potion.ID;
            for(AbstractRelic r:AbstractDungeon.player.relics){
                if(r.relicId.equals(Echo.ID)&&r.counter>0){
                    ((Echo) r).setDescriptionAfterObtainingPotion();
                }
            }
        }
    }

    @SpirePatch(
            clz= PotionPopUp.class,
            method="updateInput"
    )
    public static class PatchTool2 {
        @SpireInsertPatch(rloc = 20)
        public static void Insert(PotionPopUp __inst, AbstractPotion ___potion) {
            Strops.lastPotion=___potion.ID;
            for(AbstractRelic r:AbstractDungeon.player.relics){
                if(r.relicId.equals(Echo.ID)&&r.counter>0){
                    ((Echo) r).setDescriptionAfterObtainingPotion();
                }
            }
        }
    }

    @SpirePatch(
            clz= CardCrawlGame.class,
            method="update"
    )
    public static class PatchTool3 {
        @SpireInsertPatch(rloc = 107)
        public static void Insert(CardCrawlGame __inst) {
            Strops.lastPotion="";
        }
    }

    /*
    @SpirePatch(
            clz= AbstractPlayer.class,
            method="obtainPotion"
    )
    public static class PatchTool1 {
        @SpireInsertPatch(rloc = 19)
        public static void Insert(AbstractPlayer __inst, AbstractPotion potionToObtain) {
            Strops.lastPotion=potionToObtain.ID;
            for(AbstractRelic r:AbstractDungeon.player.relics){
                if(r.relicId.equals(Echo.ID)&&r.counter>0){
                    ((Echo) r).setDescriptionAfterObtainingPotion();
                }
            }
        }
    }

     */
}
