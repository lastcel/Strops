package strops.patch;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import strops.helpers.ModHelper;
import strops.relics.DarkEnergyLantern;

public class PatchDarkEnergyLantern {

    @SpirePatch(
            clz= AbstractOrb.class,
            method=SpirePatch.CLASS
    )
    public static class PatchTool1{
        public static SpireField<Integer> channeledTurnCount= new SpireField<>(() -> 1);
    }

    @SpirePatch(
            clz= AbstractPlayer.class,
            method="channelOrb"
    )
    public static class PatchTool2{
        @SpireInsertPatch(rloc=24)
        public static void Insert(AbstractPlayer __inst,AbstractOrb orbToSet){
            PatchTool1.channeledTurnCount.set(orbToSet,
                    PatchGrassNowAndFlowersThen.PatchTool1
                            .earliestTurnCount.get(AbstractDungeon.player));
        }
    }

    @SpirePatch(
            clz= Dark.class,
            method="render"
    )
    public static class PatchTool3{
        @SpirePrefixPatch
        public static void Prefix(Dark __inst, SpriteBatch sb, @ByRef Texture[] ___img){
            if(AbstractDungeon.player.hasRelic(ModHelper.makePath(DarkEnergyLantern.class.getSimpleName()))){
                int curTurn,chaTurn,passedTurn;
                curTurn=PatchGrassNowAndFlowersThen.PatchTool1.earliestTurnCount.get(AbstractDungeon.player);
                chaTurn=PatchTool1.channeledTurnCount.get(__inst);
                passedTurn=curTurn-chaTurn;
                if(passedTurn==0){
                    ___img[0]= ImageMaster.loadImage("StropsResources/img/misc/DarkOrbThisTurn.png");
                } else if(passedTurn==1){
                    ___img[0]= ImageMaster.loadImage("StropsResources/img/misc/DarkOrbLastTurn.png");
                } else {
                    ___img[0]= ImageMaster.ORB_DARK;
                }
            }
        }
    }

    @SpirePatch(
            clz= Dark.class,
            method="onEvoke"
    )
    public static class PatchTool4 {
        @SpirePrefixPatch
        public static void Postfix(Dark __inst) {
            int curTurn,chaTurn,passedTurn;
            curTurn=PatchGrassNowAndFlowersThen.PatchTool1.earliestTurnCount.get(AbstractDungeon.player);
            chaTurn=PatchTool1.channeledTurnCount.get(__inst);
            passedTurn=curTurn-chaTurn;
            if(passedTurn<=1){
                for (AbstractRelic r : AbstractDungeon.player.relics) {
                    if (r.relicId.equals(ModHelper.makePath(DarkEnergyLantern.class.getSimpleName()))) {
                        r.counter=(++r.counter)%DarkEnergyLantern.THRESHOLD.value;
                        //频闪需要onTrigger
                        if(r.counter==0){
                            r.flash();
                            AbstractDungeon.actionManager.addToBottom(new DrawCardAction(AbstractDungeon.player, DarkEnergyLantern.BONUS.value));
                            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, r));
                        }
                    }
                }
            }
        }
    }
}
