package strops.patch;

import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import strops.relics.Evasive;
import strops.relics.RainbowColoredCircle;
import strops.relics.StrikersVeil;

import java.util.ArrayList;

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

                AbstractPlayer p=AbstractDungeon.player;
                ArrayList<AbstractCard> draw=p.drawPile.group;
                if(p.hasRelic(StrikersVeil.ID)){
                    StrikersVeil sv=(StrikersVeil)p.getRelic(StrikersVeil.ID);

                    sv.cards.clear();
                    for(int i=0;i<draw.size()&&i<StrikersVeil.REVEAL.value;i++){
                        AbstractCard tempCard=draw.get(draw.size()-1-i).makeStatEquivalentCopy();
                        tempCard.current_x=Settings.WIDTH * 0.08F+tempCard.hb.width*i;
                        tempCard.current_y=Settings.HEIGHT * 0.7F;
                        tempCard.drawScale=0.5f;
                        sv.cards.add(tempCard);
                    }
                    for(int i=0;i<draw.size()&&i<StrikersVeil.REVEAL.value;i++){
                        AbstractCard realCard=draw.get(draw.size()-1-i);
                        if(realCard.type == AbstractCard.CardType.ATTACK &&
                                (realCard.costForTurn >= StrikersVeil.THRESHOLD.value && !realCard.freeToPlayOnce)){
                            p.getRelic(StrikersVeil.ID).flash();
                            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(p, sv));
                            damageAmount[0]=0;
                            p.drawPile.moveToDiscardPile(realCard);
                            break;
                        }
                    }

                    /*
                    if(sv.card!=null&&p.drawPile.group.contains(sv.card)){
                        sv.card.shrink();
                        (AbstractDungeon.getCurrRoom()).souls.onToDeck(sv.card, false,true);
                    }

                     */

                    /*
                    AbstractCard tempCard=p.drawPile.group.get(p.drawPile.group.size()-1);
                    sv.card=tempCard.makeStatEquivalentCopy();
                    sv.card.current_x=Settings.WIDTH * 0.08F;
                    sv.card.current_y=Settings.HEIGHT * 0.5F;
                    sv.card.drawScale=0.5f;
                    if(tempCard.type == AbstractCard.CardType.ATTACK &&
                            (tempCard.costForTurn >= 2 && !tempCard.freeToPlayOnce)){
                        p.getRelic(StrikersVeil.ID).flash();
                        AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, p.getRelic(StrikersVeil.ID)));
                        damageAmount[0]=0;
                        p.drawPile.moveToDiscardPile(tempCard);
                        //sv.card=null;
                    }

                     */
                }
            }
        }
    }
}
