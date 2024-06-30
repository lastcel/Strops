//The relic selection screen being used is copied from Jedi mod, credits to jedi!
package strops.relics;

import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.*;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSelectScreen;
import strops.utilities.RelicSetting;

import java.util.*;
import java.util.stream.Collectors;

public class Decanter extends StropsAbstractRelic implements ClickableRelic,
        CustomSavable<String> {
    public static final String ID = ModHelper.makePath(Decanter.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(Decanter.class.getSimpleName());
    private static final RelicTier RELIC_TIER = RelicTier.COMMON;
    private static final LandingSound LANDING_SOUND = LandingSound.CLINK;

    public String relicToDisenchant="";
    protected AbstractRoom.RoomPhase roomPhase;
    protected boolean relicSelected = true;
    protected RelicSelectScreen relicSelectScreen = new RelicSelectScreen();
    protected boolean fakeHover = false;

    public static final int NUM1=3;

    public static final IntSliderSetting USABLE=new IntSliderSetting("Decanter_USABLE", "N1", NUM1, 1,10);
    public static final IntSliderSetting MH=new IntSliderSetting("Decanter_MH","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("Decanter_G","G",0,-100,100);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(USABLE);
        settings.add(MH);
        settings.add(G);
        return settings;
    }

    public Decanter() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
        showMHaG(MH,G);
        this.tips.add(new PowerTip(this.DESCRIPTIONS[1], this.DESCRIPTIONS[2]));
        canCopy=false;
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);
        counter=USABLE.value;
    }

    @Override
    public void onRightClick() {
        if(counter<=0) {
            return;
        }

        AbstractRoom currRoom= AbstractDungeon.getCurrRoom();
        if((currRoom!=null)&&(currRoom.phase == AbstractRoom.RoomPhase.COMBAT)){
            return;
        }

        ArrayList<AbstractRelic> toDisenchant = new ArrayList<>();
        for(AbstractRelic r:AbstractDungeon.player.relics){
            if(COMPENSATE_ABLE_RELICS.contains(r.relicId)){
                AbstractRelic r2;
                if(r instanceof StropsAbstractRelic){
                    r2=((StropsAbstractRelic) r).dontCheckMakeCopy();
                } else {
                    r2=r.makeCopy();
                }
                r2.isSeen = true;
                toDisenchant.add(r2);
            }
        }

        AbstractRelic r3=new ZhelpDecanter();
        r3.isSeen=true;
        toDisenchant.add(r3);

        /*
        ArrayList<AbstractRelic> toDisenchant = AbstractDungeon.player.relics.stream()
                .filter(r -> COMPENSATE_ABLE_RELICS.contains(r.relicId)).collect(Collectors.toCollection(ArrayList::new));

         */

        if(toDisenchant.size()<=1){
            return;
        }

        AbstractDungeon.closeCurrentScreen();
        if (AbstractDungeon.isScreenUp) {
            AbstractDungeon.dynamicBanner.hide();
            AbstractDungeon.overlayMenu.cancelButton.hide();
            AbstractDungeon.previousScreen = AbstractDungeon.screen;
        }
        this.roomPhase = (AbstractDungeon.getCurrRoom()).phase;
        (AbstractDungeon.getCurrRoom()).phase = AbstractRoom.RoomPhase.INCOMPLETE;
        openRelicSelect(toDisenchant);
    }

    protected void openRelicSelect(ArrayList<AbstractRelic> relics) {
        this.relicSelected = false;
        this.relicSelectScreen.open(relics);
    }

    public void update() {
        super.update();
        if (!this.relicSelected){
            if (this.relicSelectScreen.doneSelecting()) {
                this.relicSelected = true;
                relicToDisenchant=this.relicSelectScreen.getSelectedRelics().get(0).relicId;
                if(relicToDisenchant.equals(ZhelpDecanter.ID)){
                    relicToDisenchant="";
                    setDescriptionAfterLoading(false);
                } else {
                    setDescriptionAfterLoading(true);
                }
                (AbstractDungeon.getCurrRoom()).phase = this.roomPhase;
            } else {
                this.relicSelectScreen.update();
                if (!this.hb.hovered){
                    this.fakeHover = true;
                }
                this.hb.hovered = true;
            }
        }
    }

    @Override
    public void renderTip(SpriteBatch sb) {
        if (!this.relicSelected && this.fakeHover)
            this.relicSelectScreen.render(sb);
        if (this.fakeHover) {
            this.fakeHover = false;
            this.hb.hovered = false;
        } else {
            super.renderTip(sb);
        }
    }

    @Override
    public void renderInTopPanel(SpriteBatch sb) {
        //logger.info("进入方法Decanter.renderInTopPanel");
        super.renderInTopPanel(sb);
        if (!this.relicSelected && !this.fakeHover) {
            boolean hideRelics = Settings.hideRelics;
            Settings.hideRelics = false;
            this.relicSelectScreen.render(sb);
            Settings.hideRelics = hideRelics;
        }
    }

    @Override
    public String onSave() {
        return relicToDisenchant;
    }

    @Override
    public void onLoad(String savedRelicToDisenchant) {
        relicToDisenchant=savedRelicToDisenchant;
        if(!relicToDisenchant.equals("")){
            setDescriptionAfterLoading(true);
        }
    }

    @Override
    public void atBattleStart(){
        List<AbstractRelic> inBattleRelics = AbstractDungeon.player.relics.stream()
                .filter(r->IN_BATTLE_RELICS.contains(r.relicId)).collect(Collectors.toList());
        if(inBattleRelics.size()>0&&IN_BATTLE_RELICS.contains(relicToDisenchant)&&
                !relicToDisenchant.equals(SlaversCollar.ID)){
            for (AbstractRelic r:AbstractDungeon.player.relics){
                if(r.relicId.equals(relicToDisenchant)){
                    beginLongPulse();
                    flash();
                    break;
                }
            }
        }
    }

    @Override
    public void onVictory(){
        if(pulse){
            decay();
            stopPulse();
        }
    }

    @Override
    public boolean canSpawn(){
        return (float)COMPENSATE_ABLE_RELICS.size()/AbstractDungeon.bossRelicPool.size()>=0.25;
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0], USABLE.value);
    }


    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(this.DESCRIPTIONS[0], USABLE.value));
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        str_out.add(this.DESCRIPTIONS[1]);
        str_out.add(this.DESCRIPTIONS[2]);
        return str_out;
    }

    public void setDescriptionAfterLoading(boolean isInEffect) {
        if(isInEffect){
            description = String.format(DESCRIPTIONS[0] + " NL " + DESCRIPTIONS[5],USABLE.value,
                    RelicLibrary.getRelic(relicToDisenchant).name);
        } else {
            description = String.format(this.DESCRIPTIONS[0], USABLE.value);
        }
        tips.clear();
        tips.add(new PowerTip(name, description));
        showMHaG(MH,G);
        this.tips.add(new PowerTip(this.DESCRIPTIONS[1], this.DESCRIPTIONS[2]));
        initializeTips();
    }

    public void decay(){
        counter--;
        relicToDisenchant="";
        if(counter==0){
            counter=-2;
            pulse=false;
            usedUp();
        } else {
            setDescriptionAfterLoading(false);
        }
    }

    @Override
    public void setCounter(int setCounter){
        this.counter=setCounter;
        if (setCounter == -2) {
            usedUp();
            this.counter = -2;
        }
    }

    private static final Set<String> COMPENSATE_ABLE_RELICS = new HashSet<>(Arrays.asList(
            //原版游戏
            CursedKey.ID,
            Ectoplasm.ID,
            PhilosopherStone.ID,
            RunicDome.ID,
            Sozu.ID,
            VelvetChoker.ID,
            BustedCrown.ID,
            FusionHammer.ID,
            CoffeeDripper.ID,
            HoveringKite.ID,
            SlaversCollar.ID,
            MarkOfPain.ID,
            //strops
            BambooDragonflyOfHanyuHorner.ID,
            DelayedGratification.ID,
            DepartmentStore.ID,
            Key.ID,
            FTLEngines.ID,
            MessyPuppy.ID,
            FilmStarryCurtain.ID
    ));

    private static final Set<String> IN_BATTLE_RELICS = new HashSet<>(Arrays.asList(
            // 原版游戏
            PhilosopherStone.ID,
            RunicDome.ID,
            VelvetChoker.ID,
            HoveringKite.ID,
            SlaversCollar.ID,
            MarkOfPain.ID,
            //strops
            BambooDragonflyOfHanyuHorner.ID,
            DelayedGratification.ID,
            DepartmentStore.ID,
            FTLEngines.ID,
            MessyPuppy.ID,
            FilmStarryCurtain.ID
    ));
}
