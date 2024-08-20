package strops.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import strops.helpers.ModHelper;

import static strops.relics.ProfiteeringMerchant.DISCOUNT_ATTEMPTED;

public class ProfiteeringMerchantAttempted extends CustomRelic {
    public static final String ID = ModHelper.makePath(ProfiteeringMerchantAttempted.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(ProfiteeringMerchantAttempted.class.getSimpleName());
    private static final String IMG_PATH_O = ModHelper.makeOPath(ProfiteeringMerchantAttempted.class.getSimpleName());
    private static final RelicTier RELIC_TIER = RelicTier.SPECIAL;
    private static final LandingSound LANDING_SOUND = LandingSound.SOLID;

    public ProfiteeringMerchantAttempted() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(IMG_PATH_O), RELIC_TIER, LANDING_SOUND);
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
        return String.format(this.DESCRIPTIONS[0], DISCOUNT_ATTEMPTED.value);
    }
}
