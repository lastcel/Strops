package strops.patch;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import strops.relics.Register;

public class PatchRegister {

    @SpirePatch(clz = AbstractPlayer.class, method = "clickAndDragCards")
    public static class PatchTool1 {
        @SpireInsertPatch(rloc = 121)
        public static SpireReturn<Boolean> Insert(AbstractPlayer __inst) {
            AbstractRelic r=AbstractDungeon.player.getRelic(Register.ID);
            if(r!=null&&!r.grayscale){
                return SpireReturn.Return(true);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "clickAndDragCards")
    public static class PatchTool2 {
        @SpireInsertPatch(rloc = 147)
        public static SpireReturn<Boolean> Insert(AbstractPlayer __inst) {
            AbstractRelic r=AbstractDungeon.player.getRelic(Register.ID);
            if(r!=null&&!r.grayscale&&
                    __inst.hoveredCard.target == AbstractCard.CardTarget.ENEMY||
                    __inst.hoveredCard.target == AbstractCard.CardTarget.SELF_AND_ENEMY){
                return SpireReturn.Return(true);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "updateInput")
    public static class PatchTool3 {
        @SpireInsertPatch(rloc = 118)
        public static void Insert(AbstractPlayer __inst) {
            AbstractRelic r=AbstractDungeon.player.getRelic(Register.ID);
            if(r!=null&&!r.grayscale&&AbstractDungeon.player.isDraggingCard){
                __inst.hoveredCard.shrink(true);
            }
        }
    }
}
