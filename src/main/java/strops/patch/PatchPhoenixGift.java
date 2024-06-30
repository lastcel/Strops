package strops.patch;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import strops.relics.PhoenixGift;

public class PatchPhoenixGift {

    @SpirePatch(
            clz= GameActionManager.class,
            method="callEndOfTurnActions"
    )
    public static class PatchTool1 {
        @SpirePostfixPatch
        public static void Postfix(GameActionManager __inst) {
            if(AbstractDungeon.player.currentBlock==0){
                for(AbstractRelic r: AbstractDungeon.player.relics){
                    if(r.relicId.equals(PhoenixGift.ID)){
                        ((PhoenixGift) r).isEnabled=true;
                    }
                }
            }
        }
    }

    @SpirePatch(
            clz= AbstractPlayer.class,
            method="damage"
    )
    public static class PatchTool2 {
        @SpireInsertPatch(rloc=57,localvars = {"damageAmount"})
        public static void Insert(AbstractPlayer __inst, DamageInfo info, int damageAmount) {
            if(damageAmount>0&&info.type != DamageInfo.DamageType.THORNS && info.type != DamageInfo.DamageType.HP_LOSS &&
                    info.owner != null && info.owner != AbstractDungeon.player){
                for(AbstractRelic r: AbstractDungeon.player.relics){
                    if(r.relicId.equals(PhoenixGift.ID)&&((PhoenixGift) r).isEnabled&&r.counter>0){
                        r.flash();
                        r.counter-=damageAmount;
                        if(r.counter<0){
                            r.counter=0;
                        }
                        if(r.counter==0&&!isAct3Boss()){
                            AbstractDungeon.getCurrRoom().addRelicToRewards(AbstractRelic.RelicTier.RARE);
                            r.setCounter(-2);
                        }
                    }
                }
            }
        }
    }

    @SpirePatch(
            clz= AbstractPlayer.class,
            method="damage"
    )
    public static class PatchTool3 {
        @SpireInsertPatch(rloc = 150)
        public static void Insert(AbstractPlayer __inst, DamageInfo info) {
            for(AbstractRelic r:AbstractDungeon.player.relics){
                if(r.relicId.equals(PhoenixGift.ID)&&r.counter>0){
                    ((PhoenixGift)r).setDescriptionAfterDeath();
                }
            }
        }
    }

    static boolean isAct3Boss() {
        return AbstractDungeon.actNum == 3 && AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss;
    }
}
