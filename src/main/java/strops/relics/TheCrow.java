package strops.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.powers.RitualPower;
import strops.helpers.ModHelper;
import strops.powers.RitualOfRitualPower;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;

public class TheCrow extends StropsAbstractRelic {
    public static final String ID = ModHelper.makePath(TheCrow.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(TheCrow.class.getSimpleName());
    private static final String IMG_PATH_O = ModHelper.makeOPath(TheCrow.class.getSimpleName());
    private static final RelicTier RELIC_TIER = RelicTier.BOSS;
    private static final LandingSound LANDING_SOUND = LandingSound.SOLID;

    public static final int NUM1=2,NUM2=2;

    public static final IntSliderSetting BONUS1=new IntSliderSetting("TheCrow_Bonus1", "N1", NUM1, 6);
    public static final IntSliderSetting BONUS2=new IntSliderSetting("TheCrow_Bonus2", "N2", NUM2, 5);
    public static final IntSliderSetting MH=new IntSliderSetting("TheCrow_MH","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("TheCrow_G","G",0,-100,100);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(BONUS1);
        settings.add(BONUS2);
        settings.add(MH);
        settings.add(G);
        return settings;
    }

    public TheCrow() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(IMG_PATH_O), RELIC_TIER, LANDING_SOUND);
        showMHaG(MH,G);
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);
    }

    public void atBattleStart() {
        flash();
        addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                new RitualPower(AbstractDungeon.player, BONUS1.value, true),BONUS1.value));
        if(AbstractDungeon.player.hasRelic("CultistMask")){
            addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                    new RitualOfRitualPower(AbstractDungeon.player, BONUS2.value),BONUS2.value));
        }
    }

    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0], BONUS1.value, BONUS2.value);
    }

    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(this.DESCRIPTIONS[0], BONUS1.value,BONUS2.value));
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        return str_out;
    }
}
