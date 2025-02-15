package strops.patch;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.CampfireUI;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import com.megacrit.cardcrawl.ui.campfire.RestOption;
import strops.modcore.Strops;
import strops.relics.LoveChocolate;

public class PatchLoveChocolate {

    /*
    @SpirePatch(
            clz= AbstractPlayer.class,
            method="initializeStarterRelics"
    )
    public static class PatchTool1 {
        @SpirePostfixPatch
        public static void Postfix(AbstractPlayer __inst, AbstractPlayer.PlayerClass chosenClass) {
            if(LoveChocolate.getBelovedDates().contains(new SimpleDateFormat("MM/dd").format(new Date(System.currentTimeMillis())))){
                RelicLibrary.getRelic(LoveChocolate.ID).makeCopy().instantObtain(__inst,99,true);
                return;
            }
            for(String s:LoveChocolate.getBelovedUserNames()){
                if(CardCrawlGame.playerName.toLowerCase().contains(s)){
                    RelicLibrary.getRelic(LoveChocolate.ID).makeCopy().instantObtain(__inst,99,true);
                    return;
                }
            }
        }
    }

     */

    @SpirePatch(
            clz= AbstractRoom.class,
            method="update"
    )
    public static class PatchTool2 {
        @SpireInsertPatch(rloc = 51)
        public static void Insert(AbstractRoom __inst) {
            if(AbstractDungeon.player.hasRelic(LoveChocolate.ID)){
                AbstractDungeon.player.getRelic(LoveChocolate.ID).onTrigger();
            } else {
                if(AbstractDungeon.getCurrRoom() instanceof MonsterRoomElite){
                    Strops.continuousNonElite=0;
                } else {
                    Strops.continuousNonElite++;
                    if(Strops.continuousRest>=LoveChocolate.REST.value
                            &&Strops.continuousNonElite>=LoveChocolate.NONELITE.value){
                        RelicLibrary.getRelic(LoveChocolate.ID).makeCopy().instantObtain();
                    }
                }
            }
        }
    }

    @SpirePatch(
            clz= AbstractCampfireOption.class,
            method="update"
    )
    public static class PatchTool3 {
        @SpireInsertPatch(rloc = 25)
        public static void Insert(AbstractCampfireOption __inst) {
            if(AbstractDungeon.player.hasRelic(LoveChocolate.ID)){
                return;
            }

            if(__inst instanceof RestOption){
                Strops.continuousRest++;
                if(Strops.continuousRest>=LoveChocolate.REST.value
                        &&Strops.continuousNonElite>=LoveChocolate.NONELITE.value){
                    RelicLibrary.getRelic(LoveChocolate.ID).makeCopy().instantObtain();
                }
            } else {
                Strops.continuousRest=0;
            }
        }
    }

    @SpirePatch(
            clz= CampfireUI.class,
            method="updateTouchscreen"
    )
    public static class PatchTool4 {
        @SpireInsertPatch(rloc = 14)
        public static void Insert(CampfireUI __inst) {
            if(AbstractDungeon.player.hasRelic(LoveChocolate.ID)){
                return;
            }

            if(__inst.touchOption instanceof RestOption){
                Strops.continuousRest++;
                if(Strops.continuousRest>=LoveChocolate.REST.value
                        &&Strops.continuousNonElite>=LoveChocolate.NONELITE.value){
                    RelicLibrary.getRelic(LoveChocolate.ID).makeCopy().instantObtain();
                }
            } else {
                Strops.continuousRest=0;
            }
        }
    }
}
