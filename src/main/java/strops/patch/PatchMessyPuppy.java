package strops.patch;

import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import strops.relics.Decanter;
import strops.relics.MessyPuppy;

public class PatchMessyPuppy {

    @SpirePatch(
            clz= GameActionManager.class,
            method="getNextAction"
    )
    public static class PatchTool1 {
        @SpireInsertPatch(rloc = 215)
        public static void Insert(GameActionManager __instance) {
            if(AbstractDungeon.player.hasRelic(MessyPuppy.ID)){
                if(AbstractDungeon.player.hasRelic(Decanter.ID)&&
                        ((Decanter)AbstractDungeon.player.getRelic(Decanter.ID))
                                .relicToDisenchant.equals(MessyPuppy.ID)){
                    AbstractDungeon.player.getRelic(Decanter.ID).flash();
                } else {
                    AbstractDungeon.player.getRelic(MessyPuppy.ID).onTrigger();
                }
            }
        }
    }

    @SpirePatch(
            clz= AbstractCreature.class,
            method="loadAnimation"
    )
    public static class PatchTool2 {
        @SpireInsertPatch(rloc = 5)
        public static void Insert(AbstractCreature __instance, String atlasUrl, String skeletonUrl, @ByRef float[] scale) {
            if(MessyPuppy.isSmallerMonster){
                scale[0]+=0.7f;
            }
        }
    }

    /*
    @SpirePatch(
            clz= CardCrawlGame.class,
            method="update"
    )
    public static class PatchTool3 {
        @SpireInsertPatch(rloc = 129)
        public static void Insert(CardCrawlGame __inst) {
            Strops.logger.info("准备getDungeon()："+CardCrawlGame.nextDungeon);
        }
    }

    @SpirePatch(
            clz= CardCrawlGame.class,
            method="update"
    )
    public static class PatchTool4 {
        @SpireInsertPatch(rloc = 115)
        public static void Insert(CardCrawlGame __inst) {
            Strops.logger.info("---------------");
            Strops.logger.info("当下屏幕="+AbstractDungeon.screen);
            Strops.logger.info("此前屏幕="+AbstractDungeon.previousScreen);

            if(CardCrawlGame.mode==null){
                Strops.logger.info(">>>模式为空");
            } else {
                Strops.logger.info("模式="+CardCrawlGame.mode);
            }
            Strops.logger.info("已打败="+AbstractDungeon.isDungeonBeaten);
            if(AbstractDungeon.fadeColor==null){
                Strops.logger.info("###渐变色为空");
            } else {
                Strops.logger.info("渐变色透明度="+AbstractDungeon.fadeColor.a);
            }
            if(CardCrawlGame.dungeonTransitionScreen!=null){
                Strops.logger.info("地牢过渡屏幕是否完成="+CardCrawlGame.dungeonTransitionScreen.isComplete);
            } else {
                Strops.logger.info("~~~地牢过渡屏幕为空");
            }
            if(CardCrawlGame.dungeon==null){
                Strops.logger.info("^^^地牢为空");
            } else {
                Strops.logger.info("^^^地牢不为空");
            }
        }
    }

    @SpirePatch(
            clz= ProceedButton.class,
            method="goToNextDungeon"
    )
    public static class PatchTool5 {
        @SpirePostfixPatch
        public static void Postfix(ProceedButton __inst, AbstractRoom room) {
            Strops.logger.info("★★★★★");
            Strops.logger.info("检测到点击前进按钮，去下一地牢，isDungeonBeaten="+AbstractDungeon.isDungeonBeaten);
        }
    }

     */
}
