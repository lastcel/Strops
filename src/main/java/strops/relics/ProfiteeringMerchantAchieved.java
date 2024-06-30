package strops.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import strops.helpers.ModHelper;

import static strops.relics.ProfiteeringMerchant.DISCOUNT_ACHIEVED;

public class ProfiteeringMerchantAchieved extends CustomRelic {
    public static final String ID = ModHelper.makePath(ProfiteeringMerchantAchieved.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(ProfiteeringMerchantAchieved.class.getSimpleName());
    //private static final String IMG_PATH_O = ModHelper.makeOPath(ProfiteeringMerchantAchieved.class.getSimpleName());
    private static final RelicTier RELIC_TIER = RelicTier.SPECIAL;
    private static final LandingSound LANDING_SOUND = LandingSound.SOLID;

    public ProfiteeringMerchantAchieved() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
    }

    @Override
    public void justEnteredRoom(AbstractRoom room) {
        if (room instanceof com.megacrit.cardcrawl.rooms.ShopRoom) {
            flash();
            this.pulse = true;
        } else {
            this.pulse = false;
        }
    }

    @Override
    public boolean canSpawn(){
        return false;
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0], DISCOUNT_ACHIEVED.value);
    }


    public AbstractRelic makeCopy() {return new ProfiteeringMerchantAchieved();}
}
