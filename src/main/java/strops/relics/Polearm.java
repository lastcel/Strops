package strops.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ThornsPower;
import com.megacrit.cardcrawl.powers.ThousandCutsPower;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;

public class Polearm extends StropsAbstractRelic {
    public static final String ID = ModHelper.makePath(Polearm.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(Polearm.class.getSimpleName());
    private static final RelicTier RELIC_TIER = RelicTier.BOSS;
    private static final LandingSound LANDING_SOUND = LandingSound.SOLID;

    public static int NUM1=6,NUM2=2;

    public static final IntSliderSetting BONUS = new IntSliderSetting("Polearm_BONUS", "N1", NUM1, 8);
    public static final IntSliderSetting PENALTY = new IntSliderSetting("Polearm_PENALTY", "N2", NUM2, 4);
    public static final IntSliderSetting MH=new IntSliderSetting("Polearm_MH","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("Polearm_G","G",0,-100,100);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(BONUS);
        settings.add(PENALTY);
        settings.add(MH);
        settings.add(G);
        return settings;
    }

    public Polearm() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
        showMHaG(MH,G);
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);
    }

    @Override
    public void atBattleStart() {
        flash();
        if(BONUS.value>0){
            addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ThousandCutsPower(AbstractDungeon.player, BONUS.value), BONUS.value));
            addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        }
        if(PENALTY.value>0){
            for (AbstractMonster m : (AbstractDungeon.getMonsters()).monsters) {
                addToTop(new RelicAboveCreatureAction(m, this));
                m.addPower(new ThornsPower(m, PENALTY.value));
            }
        }
        AbstractDungeon.onModifyPower();
    }

    @Override
    public void onSpawnMonster(AbstractMonster monster) {
        flash();
        if(PENALTY.value>0){
            monster.addPower(new ThornsPower(monster, PENALTY.value));
        }
        AbstractDungeon.onModifyPower();
    }

    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0], BONUS.value,PENALTY.value);
    }

    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(this.DESCRIPTIONS[0], BONUS.value, PENALTY.value));
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        return str_out;
    }
}
