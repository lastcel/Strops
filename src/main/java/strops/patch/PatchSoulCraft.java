package strops.patch;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.relics.SacredBark;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import strops.cards.SoulCraftedCard;
import strops.modcore.Strops;
import strops.relics.SoulCraft;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class PatchSoulCraft {

    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "obtainPotion",
            paramtypez = {AbstractPotion.class}
    )
    public static class PatchTool1 {
        @SpirePrefixPatch
        public static SpireReturn<Boolean> Prefix(AbstractPlayer __inst, AbstractPotion potionToObtain) {
            if(AbstractDungeon.player.hasRelic(SoulCraft.ID)&&
                    AbstractDungeon.getCurrRoom().phase!=AbstractRoom.RoomPhase.COMBAT&&
                    (potionToObtain.rarity==AbstractPotion.PotionRarity.COMMON||
                            potionToObtain.rarity==AbstractPotion.PotionRarity.UNCOMMON)){
                AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new SoulCraftedCard(potionToObtain,SoulCraft.USABLE.value),
                        Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                return SpireReturn.Return(true);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = SingleCardViewPopup.class,
            method = "renderTips"
    )
    public static class PatchTool2 {
        @SpirePostfixPatch
        public static void Postfix(SingleCardViewPopup __inst, SpriteBatch sb, AbstractCard ___card) {
            if(___card instanceof SoulCraftedCard&&((SoulCraftedCard)___card).sourcePotion!=null){
                try {
                    Method m = TipHelper.class.getDeclaredMethod("renderPowerTips", float.class, float.class, SpriteBatch.class, ArrayList.class);
                    m.setAccessible(true);
                    m.invoke(___card,Settings.WIDTH / 2.0F - 640.0F * Settings.scale, 420.0F * Settings.scale,sb,((SoulCraftedCard)___card).sourcePotion.tips);
                } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                    Strops.logger.info("An exception happened while PatchSoulCraft's reflect on AbstractCard.renderPowerTips()!");
                }
                ((SoulCraftedCard)___card).sourcePotion.posX=Settings.WIDTH/2.0f;
                ((SoulCraftedCard)___card).sourcePotion.posY=Settings.HEIGHT/2.0f+150.0f*Settings.scale;
                ((SoulCraftedCard)___card).sourcePotion.scale=Settings.scale*6;
                ((SoulCraftedCard)___card).sourcePotion.render(sb);
            }
        }
    }

    @SpirePatch(
            clz = SacredBark.class,
            method = "onEquip"
    )
    public static class PatchTool3 {
        @SpirePostfixPatch
        public static void Postfix(SacredBark __inst) {
            for(AbstractCard c:AbstractDungeon.player.masterDeck.group){
                if(c instanceof SoulCraftedCard&&((SoulCraftedCard)c).sourcePotion!=null){
                    ((SoulCraftedCard)c).sourcePotion.initializeData();
                }
            }
        }
    }
}
