package strops.relics;

import basemod.abstracts.CustomBottleRelic;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.blue.Hologram;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import strops.helpers.ModHelper;
import strops.patch.PatchThreefoldCheque;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;
import java.util.function.Predicate;

public class ThreefoldCheque extends StropsAbstractRelic implements CustomBottleRelic {
    public static final String ID = ModHelper.makePath(ThreefoldCheque.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(ThreefoldCheque.class.getSimpleName());
    private static final String IMG_PATH_O = ModHelper.makeOPath(ThreefoldCheque.class.getSimpleName());
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;

    public static final int NUM1=1,TIER=5;

    public static final IntSliderSetting BONUS=new IntSliderSetting("ThreefoldCheque_Bonus","N1", NUM1,1,5);
    public static final IntSliderSetting MH=new IntSliderSetting("ThreefoldCheque_MH","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("ThreefoldCheque_G","G",0,-100,100);
    public static final IntSliderSetting R=new IntSliderSetting("ThreefoldCheque_R","R", TIER,0,5);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(BONUS);
        settings.add(MH);
        settings.add(G);
        settings.add(R);
        return settings;
    }

    public ThreefoldCheque() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(IMG_PATH_O), num2Tier(R.value), LANDING_SOUND);
        showMHaG(MH,G);
        this.tips.add(new PowerTip(this.DESCRIPTIONS[1], this.DESCRIPTIONS[2]));
        canCopy=false;
    }

    @Override
    public Predicate<AbstractCard> isOnCard() {
        return PatchThreefoldCheque.PatchTool1.isReadyToUnfold::get;
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action){
        if(card.cardID.equals(Hologram.ID)){
            return;
        }

        for (AbstractCard c : GetAllInBattleInstances.get(card.uuid)){
            if(PatchThreefoldCheque.PatchTool1.isReadyToUnfold.get(c)){
                c.modifyCostForCombat(-1);
                PatchThreefoldCheque.PatchTool1.isReadyToUnfold.set(c,false);
            } else {
                PatchThreefoldCheque.PatchTool1.isReadyToUnfold.set(c,true);
            }
        }

    }

    @Override
    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0],BONUS.value);
    }

    @Override
    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(this.DESCRIPTIONS[0],BONUS.value));
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        str_out.add(this.DESCRIPTIONS[1]);
        str_out.add(this.DESCRIPTIONS[2]);
        return str_out;
    }
}
