package strops.relics;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.rewards.RewardItem;
import strops.helpers.ModHelper;
import strops.potions.FrugalPotion;
import strops.potions.GreedyPotion;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;

public class SwordOfFeastAndFamine extends StropsAbstractRelic{
    public static final String ID = ModHelper.makePath(SwordOfFeastAndFamine.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(SwordOfFeastAndFamine.class.getSimpleName());
    private static final String IMG_PATH_O = ModHelper.makeOPath(SwordOfFeastAndFamine.class.getSimpleName());
    private static final LandingSound LANDING_SOUND = LandingSound.CLINK;

    public static boolean isSwordReady=false;

    public static final int TIER=2;

    public static final IntSliderSetting MH=new IntSliderSetting("SoFaF_MH","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("SoFaF_G","G",0,-100,100);
    public static final IntSliderSetting R=new IntSliderSetting("SoFaF_R","R", TIER,1,3);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(MH);
        settings.add(G);
        settings.add(R);
        return settings;
    }

    public SwordOfFeastAndFamine() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(IMG_PATH_O), num2Tier(R.value), LANDING_SOUND);
        showMHaG(MH,G);
        this.tips.add(new PowerTip(this.DESCRIPTIONS[1], this.DESCRIPTIONS[2]));
        canSpawnInBattle=false;
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);
        if (AbstractDungeon.screen != AbstractDungeon.CurrentScreen.COMBAT_REWARD) {
            AbstractDungeon.combatRewardScreen.open(this.DESCRIPTIONS[5]);
            (AbstractDungeon.getCurrRoom()).rewardPopOutTimer = 0.0F;
            AbstractDungeon.combatRewardScreen.rewards.removeIf(r -> r.type == RewardItem.RewardType.CARD);
            AbstractDungeon.combatRewardScreen.rewards.add(new RewardItem(new FrugalPotion()));
            AbstractDungeon.combatRewardScreen.rewards.add(new RewardItem(new GreedyPotion()));
            AbstractDungeon.combatRewardScreen.positionRewards();
        } else if(AbstractDungeon.combatRewardScreen.rewards.stream().noneMatch(r->r.relic==this)){
            AbstractDungeon.combatRewardScreen.rewards.add(new RewardItem(new FrugalPotion()));
            AbstractDungeon.combatRewardScreen.rewards.add(new RewardItem(new GreedyPotion()));
            AbstractDungeon.combatRewardScreen.positionRewards();
        } else {
            isSwordReady=true;
        }
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(this.DESCRIPTIONS[0]);
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        str_out.add(this.DESCRIPTIONS[1]);
        str_out.add(this.DESCRIPTIONS[2]);
        return str_out;
    }

    @Override
    public boolean canSpawn(){
        return ((Settings.isEndless || AbstractDungeon.actNum <= 3) &&
                !(AbstractDungeon.getCurrRoom() instanceof com.megacrit.cardcrawl.rooms.ShopRoom));
    }
}
