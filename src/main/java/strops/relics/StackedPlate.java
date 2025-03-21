package strops.relics;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;

public class StackedPlate extends StropsAbstractRelic{
    public static final String ID = ModHelper.makePath(StackedPlate.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(StackedPlate.class.getSimpleName());
    private static final String IMG_PATH_O = ModHelper.makeOPath(StackedPlate.class.getSimpleName());
    //private static final RelicTier RELIC_TIER = RelicTier.RARE;
    private static final LandingSound LANDING_SOUND = LandingSound.HEAVY;

    public static final int NUM1=1,NUM2=2,NUM3=10,TIER=3;

    public static final IntSliderSetting BASE=new IntSliderSetting("StackedPlate_Base_v0.12.5", "N1", NUM1, 3);
    public static final IntSliderSetting BONUS=new IntSliderSetting("StackedPlate_Bonus_v0.12.5", "N2", NUM2, 4);
    public static final IntSliderSetting MULTIPLIER=new IntSliderSetting("StackedPlate_Multiplier", "N3/10", NUM3, 30);
    //public static final IntSliderSetting THRESHOLD=new IntSliderSetting("StackedPlate_Threshold", "N2", NUM2, 1,17);
    public static final IntSliderSetting MH=new IntSliderSetting("StackedPlate_MH_v0.12.5","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("StackedPlate_G_v0.12.5","G",0,-100,100);
    public static final IntSliderSetting R=new IntSliderSetting("StackedPlate_R_v0.12.5","R", TIER,0,5);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(BASE);
        settings.add(BONUS);
        settings.add(MULTIPLIER);
        settings.add(MH);
        settings.add(G);
        settings.add(R);
        return settings;
    }

    public StackedPlate() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(IMG_PATH_O), num2Tier(R.value), LANDING_SOUND);
        showMHaG(MH,G);
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);
    }

    @Override
    public void atBattleStart(){
        flash();
        addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new PlatedArmorPower(AbstractDungeon.player, BASE.value), BASE.value));
        addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
    }

    /*
    @Override
    public void atTurnStart(){
        counter=0;
    }

     */

    @Override
    public int onAttacked(DamageInfo info, int damageAmount){
        AbstractPlayer p=AbstractDungeon.player;
        if (info.type != DamageInfo.DamageType.THORNS && info.type != DamageInfo.DamageType.HP_LOSS && info.owner != null && info.owner != p && damageAmount > 0) {
            flash();
            if(p.hasPower(PlatedArmorPower.POWER_ID)){
                if(BONUS.value>0){
                    addToBot(new ApplyPowerAction(p, p, new PlatedArmorPower(p, BONUS.value), BONUS.value));
                }
            } else if(BONUS.value>1){
                addToBot(new ApplyPowerAction(p, p, new PlatedArmorPower(p, BONUS.value-1), BONUS.value-1));
            }
        }
        return damageAmount;
    }

    @Override
    public void onVictory(){
        if(MULTIPLIER.value==0){
            return;
        }
        AbstractPlayer p = AbstractDungeon.player;
        AbstractPower pow=p.getPower(PlatedArmorPower.POWER_ID);
        if (p.currentHealth > 0 && pow != null){
            flash();
            addToTop(new RelicAboveCreatureAction(p, this));
            p.heal(MathUtils.floor(pow.amount*MULTIPLIER.value/10.0f));
        }
    }

    /*
    @Override
    public boolean canSpawn() {
        return (Settings.isEndless || (AbstractDungeon.actNum <= 3));
    }

     */

    @Override
    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0], BASE.value, BONUS.value, MULTIPLIER.value*10);
    }

    @Override
    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(this.DESCRIPTIONS[0], BASE.value, BONUS.value, MULTIPLIER.value*10));
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        return str_out;
    }
}
