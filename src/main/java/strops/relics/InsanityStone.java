package strops.relics;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.colorless.Madness;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;

public class InsanityStone extends StropsAbstractRelic {
    // 遗物ID（此处的ModHelper在“04 - 本地化”中提到）
    public static final String ID = ModHelper.makePath(InsanityStone.class.getSimpleName());
    // 图片路径
    private static final String IMG_PATH = ModHelper.makeIPath(InsanityStone.class.getSimpleName());
    private static final String IMG_PATH_O = ModHelper.makeOPath(InsanityStone.class.getSimpleName());
    // 遗物类型
    //private static final RelicTier RELIC_TIER = RelicTier.RARE;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.SOLID;

    public static final int NUM1=2,TIER=3;

    public static final IntSliderSetting BONUS=new IntSliderSetting("InsanityStone_Bonus", "N1", NUM1, 1,5);
    public static final IntSliderSetting MH=new IntSliderSetting("InsanityStone_MH","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("InsanityStone_G","G",0,-100,100);
    public static final IntSliderSetting R=new IntSliderSetting("InsanityStone_R","R", TIER,0,5);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(BONUS);
        settings.add(MH);
        settings.add(G);
        settings.add(R);
        return settings;
    }

    public InsanityStone() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(IMG_PATH_O), num2Tier(R.value), LANDING_SOUND);
        showMHaG(MH,G);
        this.cardToPreview=new Madness();
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);
    }

    @Override
    public void atBattleStartPreDraw()
    {
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        flash();
        addToBot(new MakeTempCardInHandAction(new Madness(), BONUS.value, false));
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0], BONUS.value);
    }

    @Override
    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(this.DESCRIPTIONS[0], BONUS.value));
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        return str_out;
    }
}
