package strops.relics;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.unique.PoisonLoseHpAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;

public class PlagueStopwatch extends StropsAbstractRelic{
    public static final String ID = ModHelper.makePath(PlagueStopwatch.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(PlagueStopwatch.class.getSimpleName());
    //private static final String IMG_PATH_O = ModHelper.makeOPath(PlagueStopwatch.class.getSimpleName());
    private static final RelicTier RELIC_TIER = RelicTier.RARE;
    private static final LandingSound LANDING_SOUND = LandingSound.CLINK;

    public static final int NUM1=12;

    public static final IntSliderSetting THRESHOLD=new IntSliderSetting("PlagueStopwatch_Threshold_v0.12.0", "N1", NUM1, 6,24);
    public static final IntSliderSetting MH=new IntSliderSetting("PlagueStopwatch_MH_v0.12.0","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("PlagueStopwatch_G_v0.12.0","G",0,-100,100);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(THRESHOLD);
        settings.add(MH);
        settings.add(G);
        return settings;
    }

    public PlagueStopwatch() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
        showMHaG(MH,G);
        /*
        AbstractCard c=new Envenom();
        c.upgrade();
        this.cardToPreview=c;

         */
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);
        counter=0;
        /*
        InputHelper.moveCursorToNeutralPosition();
        ArrayList<AbstractCard> envenomChoices = new ArrayList<>();
        envenomChoices.add(new DoAddEnvenom());
        envenomChoices.add(new DoNothing());
        if(AbstractDungeon.isScreenUp){
            AbstractDungeon.dynamicBanner.hide();
            AbstractDungeon.overlayMenu.cancelButton.hide();
            AbstractDungeon.previousScreen = AbstractDungeon.screen;
        }
        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.INCOMPLETE;
        AbstractDungeon.cardRewardScreen.chooseOneOpen(envenomChoices);

         */
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        counter++;
        if (counter % THRESHOLD.value == 0) {
            flash();
            counter = 0;
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            for (AbstractMonster m : (AbstractDungeon.getCurrRoom()).monsters.monsters){
                if(m.hasPower(PoisonPower.POWER_ID)){
                    AbstractPower p= m.getPower(PoisonPower.POWER_ID);
                    addToBot(new PoisonLoseHpAction(m, AbstractDungeon.player,
                            p.amount, AbstractGameAction.AttackEffect.POISON));
                }
            }
        }
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0], THRESHOLD.value);
    }


    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(this.DESCRIPTIONS[0], THRESHOLD.value));
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        return str_out;
    }
}
