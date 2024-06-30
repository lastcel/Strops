//本遗物需要考证相关英文风味文本的原文，其他有些也是

package strops.relics;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import strops.cards.ArcaneTalents;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;

public class GrassNowAndFlowersThen extends StropsAbstractRelic {
    public static final String ID = ModHelper.makePath(GrassNowAndFlowersThen.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(GrassNowAndFlowersThen.class.getSimpleName());
    private static final RelicTier RELIC_TIER = RelicTier.RARE;
    private static final LandingSound LANDING_SOUND = LandingSound.MAGICAL;

    public static final IntSliderSetting MH=new IntSliderSetting("GrassNowAndFlowersThen_MH","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("GrassNowAndFlowersThen_G","G",0,-100,100);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(MH);
        settings.add(G);
        return settings;
    }

    public GrassNowAndFlowersThen() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
        showMHaG(MH,G);
        this.cardToPreview=new ArcaneTalents();
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);
        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new ArcaneTalents(),
                Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(this.DESCRIPTIONS[0]);
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        return str_out;
    }
}
