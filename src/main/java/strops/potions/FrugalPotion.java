package strops.potions;

import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import strops.helpers.ModHelper;
import strops.relics.ZhelpFrugalPotion;

import static strops.relics.ZhelpFrugalPotion.*;

public class FrugalPotion extends AbstractStropsPotion implements CustomSavable<Integer> {
    public static final String POTION_ID = ModHelper.makePath(FrugalPotion.class.getSimpleName());
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public int artifactPart=0;

    public FrugalPotion() {
        super(potionStrings.NAME, POTION_ID, PotionRarity.PLACEHOLDER, PotionSize.T, PotionColor.EXPLOSIVE);
        this.isThrown = false;
    }

    public Integer onSave(){
        return artifactPart;
    }

    public void onLoad(Integer savedArtifactPart){
        artifactPart=savedArtifactPart;
        initializeData();
    }

    public void initializeData() {
        this.potency = getPotency();
        this.description = String.format(potionStrings.DESCRIPTIONS[0], MULTIPLIER.value, ARTIFACT.value, potency+artifactPart);
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
    }

    public void use(AbstractCreature target) {

    }

    @Override
    public void atPreBattleFlowing(){
        flash();
        AbstractPlayer p=AbstractDungeon.player;
        p.loseGold(MathUtils.floor(p.gold*(1-ZhelpFrugalPotion.MULTIPLIER.value/100.0f)));
        addToBot(new ApplyPowerAction(p,p,new ArtifactPower(p,ZhelpFrugalPotion.ARTIFACT.value),ZhelpFrugalPotion.ARTIFACT.value));
    }

    @Override
    public void onDiscarded(){
        if ((AbstractDungeon.getCurrRoom()).phase == AbstractRoom.RoomPhase.COMBAT) {
            addToBot(new HealAction(AbstractDungeon.player, AbstractDungeon.player, potency+artifactPart));
        } else {
            AbstractDungeon.player.heal(potency+artifactPart);
        }
    }

    @Override
    public int getPotency(int ascensionLevel) {
        return ZhelpFrugalPotion.HEAL.value;
    }

    @Override
    public boolean canUse() {
        return false;
    }

    @Override
    public AbstractPotion makeCopy() {
        return new FrugalPotion();
    }
}
