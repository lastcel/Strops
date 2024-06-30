package strops.patch;

import basemod.BaseMod;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.unique.IncreaseMaxHpAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.*;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterInfo;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import strops.relics.ClockWithNoHands;

import java.util.ArrayList;
import java.util.Collections;

public class PatchClockWithNoHands {

    public static ArrayList<MonsterInfo> savedCustomElites1=new ArrayList<>();
    public static ArrayList<MonsterInfo> savedCustomElites2=new ArrayList<>();
    public static ArrayList<MonsterInfo> savedCustomElites3=new ArrayList<>();
    public static boolean isRecorded=false;

    @SpirePatch(
            clz= TheCity.class,
            method="generateElites"
    )
    public static class PatchTool1 {
        @SpirePostfixPatch
        public static void Postfix(TheCity __inst, int count) {
            if(AbstractDungeon.player.hasRelic(ClockWithNoHands.ID)){
                TheCity.eliteMonsterList.clear();

                ArrayList<MonsterInfo> monsters = new ArrayList<>();
                monsters.add(new MonsterInfo("Gremlin Nob", 1.0F));
                monsters.add(new MonsterInfo("Lagavulin", 1.0F));
                monsters.add(new MonsterInfo("3 Sentries", 1.0F));
                for(MonsterInfo i:savedCustomElites1){
                    monsters.add(new MonsterInfo(i.name,i.weight));
                }
                myNormalizeWeights(monsters);
                __inst.populateMonsterList(monsters, count, true);
            }
        }
    }

     /*
            try {
                Field f= AddCustomMonsters.class.getDeclaredField("calls");
                f.setAccessible(true);
                f.set(null,0);
            } catch (Exception e) {
                e.printStackTrace();
            }

             */

    @SpirePatch(
            clz= TheBeyond.class,
            method="generateElites"
    )
    public static class PatchTool2 {
        @SpirePostfixPatch
        public static void Postfix(TheBeyond __inst, int count) {
            if(AbstractDungeon.player.hasRelic(ClockWithNoHands.ID)){
                TheBeyond.eliteMonsterList.clear();

                ArrayList<MonsterInfo> monsters = new ArrayList<>();
                monsters.add(new MonsterInfo("Gremlin Leader", 1.0F));
                monsters.add(new MonsterInfo("Slavers", 1.0F));
                monsters.add(new MonsterInfo("Book of Stabbing", 1.0F));
                for(MonsterInfo i:savedCustomElites2){
                    monsters.add(new MonsterInfo(i.name,i.weight));
                }
                myNormalizeWeights(monsters);
                __inst.populateMonsterList(monsters, count, true);
            }
        }
    }

    @SpirePatch(
            clz= TheEnding.class,
            method="generateMonsters"
    )
    public static class PatchTool3 {
        @SpirePostfixPatch
        public static void Postfix(TheEnding __inst) {
            if(AbstractDungeon.player.hasRelic(ClockWithNoHands.ID)){
                TheEnding.eliteMonsterList.clear();

                ArrayList<MonsterInfo> monsters = new ArrayList<>();
                monsters.add(new MonsterInfo("Giant Head", 2.0F));
                monsters.add(new MonsterInfo("Nemesis", 2.0F));
                monsters.add(new MonsterInfo("Reptomancer", 2.0F));
                for(MonsterInfo i:savedCustomElites3){
                    monsters.add(new MonsterInfo(i.name,i.weight));
                }
                myNormalizeWeights(monsters);
                __inst.populateMonsterList(monsters, 3, true);
            }
        }
    }

    @SpirePatch(
            clz= Exordium.class,
            method="generateMonsters"
    )
    public static class PatchTool4 {
        @SpirePrefixPatch
        public static void Prefix(Exordium __inst) {
            if(!isRecorded){
                savedCustomElites1.clear();
                savedCustomElites2.clear();
                savedCustomElites3.clear();
                for(MonsterInfo i:BaseMod.getEliteEncounters(Exordium.ID)){
                    savedCustomElites1.add(new MonsterInfo(i.name,i.weight));
                }
                for(MonsterInfo i:BaseMod.getEliteEncounters(TheCity.ID)){
                    savedCustomElites2.add(new MonsterInfo(i.name,i.weight));
                }
                for(MonsterInfo i:BaseMod.getEliteEncounters(TheBeyond.ID)){
                    savedCustomElites3.add(new MonsterInfo(i.name,i.weight));
                }
                isRecorded=true;
            }
        }
    }

    @SpirePatch(
            clz= AbstractPlayer.class,
            method="preBattlePrep"
    )
    public static class PatchTool5 {
        @SpireInsertPatch(rloc = 57)
        public static void Insert(AbstractPlayer __inst) {
            if (AbstractDungeon.actNum>=2&&AbstractDungeon.player.hasRelic(ClockWithNoHands.ID)&&
                    ClockWithNoHands.PENALTY.value>0&&
                    AbstractDungeon.getCurrRoom() instanceof MonsterRoomElite) {
                for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters){
                    AbstractDungeon.actionManager.addToBottom(new IncreaseMaxHpAction(m,
                            (float) ClockWithNoHands.PENALTY.value/100, true));
                }
            }
        }
    }

    public static void myNormalizeWeights(ArrayList<MonsterInfo> list) {
        Collections.sort(list);
        float total = 0.0F;
        for (MonsterInfo i : list)
            total += i.weight;
        for (MonsterInfo i : list) {
            //logger.info("来来来来来,total="+total+"/"+i.name + ": " + i.weight + "%");
            i.weight /= total;
        }
    }
}
