package strops.relics;

import com.evacipated.cardcrawl.mod.stslib.relics.OnAfterUseCardRelic;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.unique.PoisonLoseHpAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.green.Envenom;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import strops.cards.DoAddEnvenom;
import strops.cards.DoNothing;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;

public class PlagueStopwatch extends StropsAbstractRelic implements OnAfterUseCardRelic {
    public static final String ID = ModHelper.makePath(PlagueStopwatch.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(PlagueStopwatch.class.getSimpleName());
    private static final String IMG_PATH_O = ModHelper.makeOPath(PlagueStopwatch.class.getSimpleName());
    //private static final RelicTier RELIC_TIER = RelicTier.RARE;
    private static final LandingSound LANDING_SOUND = LandingSound.CLINK;

    public static final int NUM1=1,NUM2=12,TIER=3;

    public static final IntSliderSetting BONUS=new IntSliderSetting("PlagueStopwatch_Bonus", "N1", NUM1, 0,3);
    public static final IntSliderSetting THRESHOLD=new IntSliderSetting("PlagueStopwatch_Threshold_v0.14.3", "N2", NUM2, 6,24);
    public static final IntSliderSetting MH=new IntSliderSetting("PlagueStopwatch_MH_v0.14.3","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("PlagueStopwatch_G_v0.14.3","G",0,-100,100);
    public static final IntSliderSetting R=new IntSliderSetting("PlagueStopwatch_R_v0.14.3","R", TIER,0,5);
    public static final IntSliderSetting B1=new IntSliderSetting("PlagueStopwatch_B1","B1", 0,1);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(THRESHOLD);
        settings.add(MH);
        settings.add(G);
        settings.add(R);
        settings.add(B1);
        return settings;
    }

    public PlagueStopwatch() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(IMG_PATH_O), num2Tier(R.value), LANDING_SOUND);
        showMHaG(MH,G);
        canSpawnInBattle=false;

        AbstractCard c=new Envenom();
        if(B1.value==0){
            c.upgrade();
        }
        this.cardToPreview=c;
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);
        counter=0;

        if(BONUS.value==0){
            return;
        }

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
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
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
        if(B1.value==1){
            return String.format(this.DESCRIPTIONS[5], BONUS.value, THRESHOLD.value);
        }
        return String.format(this.DESCRIPTIONS[0], BONUS.value, THRESHOLD.value);
    }

    @Override
    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        if(B1.value==1){
            str_out.add(String.format(this.DESCRIPTIONS[5], BONUS.value, THRESHOLD.value));
        } else {
            str_out.add(String.format(this.DESCRIPTIONS[0], BONUS.value, THRESHOLD.value));
        }
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        return str_out;
    }
}
