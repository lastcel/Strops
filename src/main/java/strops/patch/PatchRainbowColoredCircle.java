package strops.patch;

import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import strops.relics.Evasive;
import strops.relics.RainbowColoredCircle;

public class PatchRainbowColoredCircle {

    @SpirePatch(
            clz= AbstractPlayer.class,
            method="damage"
    )
    public static class PatchTool1 {
        @SpireInsertPatch(rloc = 7,localvars = {"damageAmount"})
        public static void Insert(AbstractPlayer __inst, DamageInfo info, @ByRef int[] damageAmount) {
            if (info.type != DamageInfo.DamageType.THORNS &&
                    info.type != DamageInfo.DamageType.HP_LOSS) {
                for(AbstractRelic r: AbstractDungeon.player.relics){
                    if(r.relicId.equals(RainbowColoredCircle.ID)){
                        r.counter++;
                        if(r.counter==RainbowColoredCircle.MATCH.value){
                            r.flash();
                            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, r));
                            damageAmount[0]=0;
                        }
                        break;
                    }
                }

                for(AbstractRelic r: AbstractDungeon.player.relics){
                    if(r.relicId.equals(Evasive.ID)){
                        r.counter++;
                        if(r.counter==Evasive.MATCH_1.value||r.counter==Evasive.MATCH_2.value||r.counter==Evasive.MATCH_3.value){
                            r.flash();
                            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, r));
                            damageAmount[0]=0;
                        }
                        break;
                    }
                }
            }
        }
    }
}
