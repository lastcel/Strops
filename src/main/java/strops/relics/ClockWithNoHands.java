package strops.relics;

import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
//import com.megacrit.cardcrawl.dungeons.TheBeyond;
//import com.megacrit.cardcrawl.dungeons.TheCity;
//import com.megacrit.cardcrawl.dungeons.TheEnding;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.monsters.MonsterInfo;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import strops.helpers.ModHelper;
import strops.patch.PatchClockWithNoHands;
import strops.patch.PatchDecanter;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class ClockWithNoHands extends StropsAbstractRelic implements CustomSavable<ArrayList<ArrayList<MonsterInfo>>> {
    public static final String ID = ModHelper.makePath(ClockWithNoHands.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(ClockWithNoHands.class.getSimpleName());
    private static final LandingSound LANDING_SOUND = LandingSound.CLINK;

    public static final int NUM1=25,TIER=3;

    public static final IntSliderSetting PENALTY=new IntSliderSetting("Clock_PENALTY", "N1", NUM1, 0,100);
    public static final IntSliderSetting MH=new IntSliderSetting("Clock_MH","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("Clock_G","G",0,-100,100);
    public static final IntSliderSetting R=new IntSliderSetting("Clock_R","R", TIER,0,5);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(PENALTY);
        settings.add(MH);
        settings.add(G);
        settings.add(R);
        return settings;
    }

    public ClockWithNoHands() {
        super(ID, ImageMaster.loadImage(IMG_PATH), num2Tier(R.value), LANDING_SOUND);
        showMHaG(MH,G);
        this.tips.add(new PowerTip(this.DESCRIPTIONS[1], this.DESCRIPTIONS[2]));
        canCopy=false;
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);

        generalGenerateElites();

        /*
        if(AbstractDungeon.id.equals(TheCity.ID)||AbstractDungeon.id.equals(TheBeyond.ID)){
            try {
                Method m = AbstractDungeon.class.getDeclaredMethod("generateElites", int.class);
                m.setAccessible(true);
                m.invoke(CardCrawlGame.dungeon,10);
            } catch (IllegalAccessException|NoSuchMethodException|InvocationTargetException e) {
                e.printStackTrace();
            }
        } else if(AbstractDungeon.id.equals(TheEnding.ID)){
            try {
                Method m = AbstractDungeon.class.getDeclaredMethod("generateMonsters");
                m.setAccessible(true);
                m.invoke(CardCrawlGame.dungeon);
            } catch (IllegalAccessException|NoSuchMethodException|InvocationTargetException e) {
                e.printStackTrace();
            }
        }

         */
    }

    @Override
    public void onUnequip(){
        PatchDecanter.isPreventHasRelic=true;
        if(AbstractDungeon.actNum==2||AbstractDungeon.actNum==3){
            try {
                AbstractDungeon.eliteMonsterList.clear();
                Method m = AbstractDungeon.class.getDeclaredMethod("generateElites", int.class);
                m.setAccessible(true);
                m.invoke(CardCrawlGame.dungeon,10);
            } catch (IllegalAccessException|NoSuchMethodException|InvocationTargetException e) {
                e.printStackTrace();
            }
        } else if(AbstractDungeon.actNum==4){
            try {
                AbstractDungeon.eliteMonsterList.clear();
                Method m = AbstractDungeon.class.getDeclaredMethod("generateMonsters");
                m.setAccessible(true);
                m.invoke(CardCrawlGame.dungeon);
            } catch (IllegalAccessException|NoSuchMethodException|InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        /*
        if(AbstractDungeon.id.equals(TheCity.ID)){
            try {
                TheCity.eliteMonsterList.clear();
                Method m = AbstractDungeon.class.getDeclaredMethod("generateElites", int.class);
                m.setAccessible(true);
                m.invoke(CardCrawlGame.dungeon,10);
            } catch (IllegalAccessException|NoSuchMethodException|InvocationTargetException e) {
                e.printStackTrace();
            }
        } else if(AbstractDungeon.id.equals(TheBeyond.ID)){
            try {
                TheBeyond.eliteMonsterList.clear();
                Method m = AbstractDungeon.class.getDeclaredMethod("generateElites", int.class);
                m.setAccessible(true);
                m.invoke(CardCrawlGame.dungeon,10);
            } catch (IllegalAccessException|NoSuchMethodException|InvocationTargetException e) {
                e.printStackTrace();
            }
        } else if(AbstractDungeon.id.equals(TheEnding.ID)){
            try {
                TheEnding.eliteMonsterList.clear();
                Method m = AbstractDungeon.class.getDeclaredMethod("generateMonsters");
                m.setAccessible(true);
                m.invoke(CardCrawlGame.dungeon);
            } catch (IllegalAccessException|NoSuchMethodException|InvocationTargetException e) {
                e.printStackTrace();
            }
        }

         */
        PatchDecanter.isPreventHasRelic=false;
    }

    @Override
    public void atBattleStart(){
        if(AbstractDungeon.getCurrRoom() instanceof MonsterRoomElite && AbstractDungeon.actNum>=2){
            flash();
        }
    }

    @Override
    public boolean canSpawn(){
        return !Settings.isEndless&&AbstractDungeon.floorNum<=54;
    }

    @Override
    public ArrayList<ArrayList<MonsterInfo>> onSave() {
        ArrayList<ArrayList<MonsterInfo>> arr=new ArrayList<>();
        arr.add(PatchClockWithNoHands.savedCustomElites1);
        arr.add(PatchClockWithNoHands.savedCustomElites2);
        arr.add(PatchClockWithNoHands.savedCustomElites3);
        return arr;
    }

    @Override
    public void onLoad(ArrayList<ArrayList<MonsterInfo>> arr) {
        PatchClockWithNoHands.savedCustomElites1=arr.get(0);
        PatchClockWithNoHands.savedCustomElites2=arr.get(1);
        PatchClockWithNoHands.savedCustomElites3=arr.get(2);
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0], PENALTY.value);
    }

    @Override
    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(this.DESCRIPTIONS[0], PENALTY.value));
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        str_out.add(this.DESCRIPTIONS[1]);
        str_out.add(this.DESCRIPTIONS[2]);
        return str_out;
    }

    public static void generalGenerateElites(){
        if(!AbstractDungeon.player.hasRelic(ClockWithNoHands.ID)){
            return;
        }

        ArrayList<MonsterInfo> monsters = new ArrayList<>();
        switch (AbstractDungeon.actNum){
            case 2:
                AbstractDungeon.eliteMonsterList.clear();

                monsters.add(new MonsterInfo("Gremlin Nob", 1.0F));
                monsters.add(new MonsterInfo("Lagavulin", 1.0F));
                monsters.add(new MonsterInfo("3 Sentries", 1.0F));
                for(MonsterInfo i:PatchClockWithNoHands.savedCustomElites1){
                    monsters.add(new MonsterInfo(i.name,i.weight));
                }
                PatchClockWithNoHands.myNormalizeWeights(monsters);
                CardCrawlGame.dungeon.populateMonsterList(monsters, 10, true);
                break;
            case 3:
                AbstractDungeon.eliteMonsterList.clear();

                monsters.add(new MonsterInfo("Gremlin Leader", 1.0F));
                monsters.add(new MonsterInfo("Slavers", 1.0F));
                monsters.add(new MonsterInfo("Book of Stabbing", 1.0F));
                for(MonsterInfo i:PatchClockWithNoHands.savedCustomElites2){
                    monsters.add(new MonsterInfo(i.name,i.weight));
                }
                PatchClockWithNoHands.myNormalizeWeights(monsters);
                CardCrawlGame.dungeon.populateMonsterList(monsters, 10, true);
                break;
            case 4:
                AbstractDungeon.eliteMonsterList.clear();

                monsters.add(new MonsterInfo("Giant Head", 2.0F));
                monsters.add(new MonsterInfo("Nemesis", 2.0F));
                monsters.add(new MonsterInfo("Reptomancer", 2.0F));
                for(MonsterInfo i:PatchClockWithNoHands.savedCustomElites3){
                    monsters.add(new MonsterInfo(i.name,i.weight));
                }
                PatchClockWithNoHands.myNormalizeWeights(monsters);
                CardCrawlGame.dungeon.populateMonsterList(monsters, 3, true);
                break;
            default:
                break;
        }
    }
}
