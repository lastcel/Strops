package strops.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import strops.helpers.ModHelper;


public class ZhelpDecanter extends CustomRelic {
    public static final String ID = ModHelper.makePath(ZhelpDecanter.class.getSimpleName());
    //private static final String IMG_PATH = ModHelper.makeIPath(ZhelpDecanter.class.getSimpleName());
    private static final RelicTier RELIC_TIER = RelicTier.SPECIAL;
    private static final LandingSound LANDING_SOUND = LandingSound.MAGICAL;

    public ZhelpDecanter() {
        super(ID, ImageMaster.loadImage(StropsAbstractRelic.TOOL_PATH), RELIC_TIER, LANDING_SOUND);
    }

    @Override
    public boolean canSpawn(){
        return false;
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }


    public AbstractRelic makeCopy() {return new ZhelpDecanter();}
}
