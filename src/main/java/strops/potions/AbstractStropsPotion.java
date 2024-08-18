//本类的patch写在了簪的patch里面
package strops.potions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.potions.AbstractPotion;

public abstract class AbstractStropsPotion extends AbstractPotion {
    public AbstractStropsPotion(String name, String id, PotionRarity rarity, PotionSize size, PotionEffect effect, Color liquidColor, Color hybridColor, Color spotsColor) {
        super(name,id,rarity,size,effect,liquidColor,hybridColor,spotsColor);
    }

    public AbstractStropsPotion(String name, String id, PotionRarity rarity, PotionSize size, PotionColor color) {
        super(name,id,rarity,size,color);
    }

    public void onDiscarded(){

    }

    public void atPreBattleFlowing(){

    }

    public void atBattleStartFlowing(){

    }
}
