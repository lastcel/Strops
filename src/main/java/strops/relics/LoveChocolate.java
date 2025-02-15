//部分patch写在了灵魂缝补项链、回响形态的patch里面
package strops.relics;

import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import com.megacrit.cardcrawl.vfx.SpeechBubble;
import strops.helpers.ModHelper;
import strops.modcore.Strops;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class LoveChocolate extends StropsAbstractRelic implements ClickableRelic {
    public static final String ID = ModHelper.makePath(LoveChocolate.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(LoveChocolate.class.getSimpleName());
    private static final String IMG_PATH_O = ModHelper.makeOPath(LoveChocolate.class.getSimpleName());
    private static final LandingSound LANDING_SOUND = LandingSound.MAGICAL;

    public static final int NUM1=50,NUM2=50,NUM3=2,NUM4=7;

    public int initialHP;

    public static final IntSliderSetting HOT=new IntSliderSetting("LoveChocolate_Bonus","N1", NUM1,100);
    public static final IntSliderSetting BURNING=new IntSliderSetting("LoveChocolate_Penalty","N2", NUM2,200);
    public static final IntSliderSetting MH=new IntSliderSetting("LoveChocolate_MH","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("LoveChocolate_G","G",0,-100,100);
    public static final IntSliderSetting REST=new IntSliderSetting("LoveChocolate_S1","S1", NUM3,1,5);
    public static final IntSliderSetting NONELITE=new IntSliderSetting("LoveChocolate_S2","S2", NUM4,3,10);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(HOT);
        settings.add(BURNING);
        settings.add(MH);
        settings.add(G);
        settings.add(REST);
        settings.add(NONELITE);
        return settings;
    }

    public LoveChocolate() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(IMG_PATH_O), RelicTier.SPECIAL, LANDING_SOUND);
        showMHaG(MH,G);
        this.tips.add(new PowerTip(this.DESCRIPTIONS[1], this.DESCRIPTIONS[2]));
        canCopy=false;
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);
        counter=0;
        secondCounter=-1;
    }

    @Override
    public void onRightClick() {
        //Strops.logger.info("怪物血量随机数="+AbstractDungeon.monsterHpRng+"，洗牌随机数="+AbstractDungeon.shuffleRng);

        if(AbstractDungeon.getCurrRoom().phase==AbstractRoom.RoomPhase.COMBAT){
            return;
        }

        if(!(AbstractDungeon.getCurrRoom() instanceof MonsterRoomElite)){
            return;
        }

        if(grayscale){
            return;
        }

        if(AbstractDungeon.actNum == 4 && AbstractDungeon.getCurrRoom() instanceof MonsterRoomElite){
            flash();
            AbstractDungeon.effectList.add(new SpeechBubble(AbstractDungeon.player.dialogX + 500.0F*Settings.scale, AbstractDungeon.player.dialogY, 3.0F, this.DESCRIPTIONS[5], true));
            AbstractDungeon.player.heal(MathUtils.floor(
                    (AbstractDungeon.player.maxHealth-AbstractDungeon.player.currentHealth)
                            *HOT.value/100.0f/2),true);
            grayscale=true;
            return;
        }

        AbstractDungeon.monsterHpRng = new Random(Settings.seed + AbstractDungeon.floorNum + 60);
        AbstractDungeon.aiRng = new Random(Settings.seed + AbstractDungeon.floorNum + 60);
        AbstractDungeon.shuffleRng = new Random(Settings.seed + AbstractDungeon.floorNum + 60);
        AbstractDungeon.cardRandomRng = new Random(Settings.seed + AbstractDungeon.floorNum + 60);
        AbstractDungeon.miscRng = new Random(Settings.seed + AbstractDungeon.floorNum + 60);

        AbstractDungeon.closeCurrentScreen();
        flash();
        beginLongPulse();
        secondCounter=-2;
        AbstractDungeon.getCurrRoom().monsters = myGetEliteMonsterForRoomCreation();
        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMBAT;
        AbstractDungeon.getCurrRoom().isBattleOver = false;
        AbstractDungeon.getCurrRoom().monsters.init();
        AbstractRoom.waitTimer = 0.1F;
        AbstractDungeon.player.preBattlePrep();
        AbstractDungeon.getCurrRoom().rewardAllowed=false;
        initialHP=AbstractDungeon.player.currentHealth;
    }

    /*
    @Override
    public ArrayList<Integer> onSave(){
        return secondCounter;
    }

    @Override
    public void onLoad(ArrayList<Integer> savedSecondCounter){
        secondCounter=savedSecondCounter;
        if(secondCounter==-2){
            flash();
            beginLongPulse();
        } else if(secondCounter==-3){
            grayscale=true;
        }
    }

     */

    @Override
    public void onVictory(){
        if(secondCounter==-2){
            AbstractDungeon.player.currentHealth=initialHP;
            AbstractDungeon.player.heal(MathUtils.floor(
                    (AbstractDungeon.player.maxHealth-initialHP)
                            *HOT.value/100.0f),true);
        } else if(secondCounter==-3){
            counter+=MathUtils.floor(initialHP*BURNING.value/100.0f);
        } else {
            return;
        }

        secondCounter=-1;
        stopPulse();
        grayscale=true;

        if (!AbstractDungeon.eliteMonsterList.isEmpty()) {
            Strops.logger.info("Removing elite: " + AbstractDungeon.eliteMonsterList.get(0) + " from monster list.");
            AbstractDungeon.eliteMonsterList.remove(0);
        } else {
            try {
                Method m = AbstractDungeon.class.getDeclaredMethod("generateElites", int.class);
                m.setAccessible(true);
                m.invoke(CardCrawlGame.dungeon,10);
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                Strops.logger.info("<1>An exception happened during LoveChocolate's reflection on AbstractDungeon.#abstractDungeon#.generateElites()!");
            }
        }
    }

    @Override
    public void onTrigger(){
        if(isToBurn()){
            addToBot(new LoseHPAction(AbstractDungeon.player,AbstractDungeon.player,counter,AbstractGameAction.AttackEffect.FIRE));
        }
    }

    @Override
    public void onEnterRoom(AbstractRoom room){
        grayscale=false;
    }

    private boolean isToBurn(){
        if(Settings.isFinalActAvailable && hasTriColor()){
            return AbstractDungeon.actNum == 4 && AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss;
        }
        if(AbstractDungeon.actNum == 3 && AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss
                &&AbstractDungeon.ascensionLevel < 20){
            return true;
        }
        return AbstractDungeon.actNum==3 && AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss
                && AbstractDungeon.ascensionLevel >= 20 && AbstractDungeon.bossList.size() == 1;
    }

    @Override
    public void onEquipMods(IntSliderSetting sA, IntSliderSetting sB){
        if(sA.value>0){
            AbstractDungeon.player.increaseMaxHp(sA.value,true);
        } else if(sA.value<0){
            AbstractDungeon.player.decreaseMaxHealth(-sA.value);
        }

        if(sB.value>0){
            AbstractDungeon.player.gainGold(sB.value);
        } else if(sB.value<0){
            if(AbstractDungeon.currMapNode!=null){
                AbstractDungeon.player.loseGold(-sB.value);
            } else {
                AbstractDungeon.player.gold += sB.value;
                if (AbstractDungeon.player.gold < 0){
                    AbstractDungeon.player.gold = 0;
                }
                for (AbstractRelic r : AbstractDungeon.player.relics){
                    r.onLoseGold();
                }
            }
        }
    }

    @Override
    public boolean canSpawn() {
        return false;
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0],HOT.value,BURNING.value);
    }

    @Override
    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(this.DESCRIPTIONS[0],HOT.value,BURNING.value));
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        str_out.add(this.DESCRIPTIONS[1]);
        str_out.add(this.DESCRIPTIONS[2]);
        return str_out;
    }

    private static final Set<String> BELOVED_DATES = new HashSet<>(Arrays.asList(
            "01/01",
            "02/03",
            "02/04",
            "02/05",
            "02/14",
            "02/15",
            "02/16",
            "04/05",
            "04/09",
            "04/10",
            "04/11",
            "06/21",
            "09/23",
            "12/22"
    ));

    public static Set<String> getBelovedDates(){
        return BELOVED_DATES;
    }

    private static final Set<String> BELOVED_USER_NAMES = new HashSet<>(Arrays.asList(
            "心愛",
            "ココア",
            "ここあ",
            "心爱",
            "cocoa",
            "kokoa",
            "可可"
    ));

    public static Set<String> getBelovedUserNames(){
        return BELOVED_USER_NAMES;
    }

    private MonsterGroup myGetEliteMonsterForRoomCreation() {
        if (AbstractDungeon.eliteMonsterList.size()<2){
            try {
                Method m = AbstractDungeon.class.getDeclaredMethod("generateElites", int.class);
                m.setAccessible(true);
                m.invoke(CardCrawlGame.dungeon,10);
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                Strops.logger.info("<2>An exception happened during LoveChocolate's reflection on AbstractDungeon.#abstractDungeon#.generateElites()!");
            }
        }

        Strops.logger.info("ELITE: " + AbstractDungeon.eliteMonsterList.get(1));
        AbstractDungeon.lastCombatMetricKey = AbstractDungeon.eliteMonsterList.get(1);
        return MonsterHelper.getEncounter(AbstractDungeon.eliteMonsterList.get(1));
    }
}
