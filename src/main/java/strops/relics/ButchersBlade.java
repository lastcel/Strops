package strops.relics;

import basemod.BaseMod;
import com.evacipated.cardcrawl.mod.stslib.relics.OnAfterUseCardRelic;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;

public class ButchersBlade extends StropsAbstractRelic implements OnAfterUseCardRelic {
    // 遗物ID（此处的ModHelper在“04 - 本地化”中提到）
    public static final String ID = ModHelper.makePath(ButchersBlade.class.getSimpleName());
    // 图片路径
    private static final String IMG_PATH = ModHelper.makeIPath(ButchersBlade.class.getSimpleName());
    // 轮廓图片路径
    private static final String IMG_PATH_O = ModHelper.makeOPath(ButchersBlade.class.getSimpleName());
    // 遗物类型
    //private static final RelicTier RELIC_TIER = RelicTier.COMMON;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.CLINK;

    public static final int NUM1=1,TIER=1;

    public static final IntSliderSetting THRESHOLD=new IntSliderSetting("ButchersBlade_Threshold","N1", NUM1,1,5);
    public static final IntSliderSetting MH=new IntSliderSetting("ButchersBlade_MH","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("ButchersBlade_G","G",0,-100,100);
    public static final IntSliderSetting R=new IntSliderSetting("ButchersBlade_R","R", TIER,0,5);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(THRESHOLD);
        settings.add(MH);
        settings.add(G);
        settings.add(R);
        return settings;
    }

    public ButchersBlade() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(IMG_PATH_O), num2Tier(R.value), LANDING_SOUND);
        showMHaG(MH,G);
        canCopy=false;
    }

    //每回合打出的第一张牌如果为攻击牌，则返回手牌
    @Override
    public void atTurnStart(){
        counter=0;
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action){
        if((counter<THRESHOLD.value)&&(card.type==AbstractCard.CardType.ATTACK)&&
                AbstractDungeon.player.hand.size()<BaseMod.MAX_HAND_SIZE){
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            flash();
            action.returnToHand=true;
        }
        counter++;
        if(counter>=THRESHOLD.value){
            grayscale=true;
        }
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);
    }

    @Override
    public void onVictory() {
        this.counter = -1;
        grayscale=false;
    }

    // 获取遗物描述，但原版游戏只在初始化和获取遗物时调用，故该方法等于初始描述
    @Override
    public String getUpdatedDescription() {
        return String.format(DESCRIPTIONS[0],THRESHOLD.value);
    }

    @Override
    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(DESCRIPTIONS[0],THRESHOLD.value));
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        return str_out;
    }
}
