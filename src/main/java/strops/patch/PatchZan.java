package strops.patch;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PotionHelper;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.EntropicBrew;
import com.megacrit.cardcrawl.potions.PotionSlot;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import strops.potions.Blizzard;
import strops.potions.PhantasmalShootingStar;
import strops.relics.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PatchZan {

    @SpirePatch(
            clz= AbstractPlayer.class,
            method="obtainPotion",
            paramtypez = {AbstractPotion.class}
    )
    public static class PatchTool1 {
        @SpireInsertPatch(rloc = 17)
        public static SpireReturn<Boolean> Insert(AbstractPlayer __inst, AbstractPotion potionToObtain) {
            if(AbstractDungeon.player.hasRelic(Zan.ID)&&
                    AbstractDungeon.getCurrRoom().phase!=AbstractRoom.RoomPhase.COMBAT&&
                    ((Zan)AbstractDungeon.player.getRelic(Zan.ID)).secondCounter<Zan.LIMIT){
                AbstractRelic zan=AbstractDungeon.player.getRelic(Zan.ID);
                zan.flash();
                ((Zan)zan).secondCounter++;
                int healAmount,overHealAmount;
                if(__inst.potions.get(0).ID.equals(Blizzard.POTION_ID)){
                    healAmount=Zan.BLIZZARD_BONUS;
                } else if (__inst.potions.get(0).ID.equals(PhantasmalShootingStar.POTION_ID)){
                    healAmount= Zan.SHOOTINGSTAR_BONUS;
                } else {
                    switch(__inst.potions.get(0).rarity){
                        case COMMON:healAmount=Zan.COMMON_BONUS.value;break;
                        case UNCOMMON:healAmount=Zan.UNCOMMON_BONUS.value;break;
                        case RARE:healAmount=Zan.RARE_BONUS.value;break;
                        default:healAmount=0;break;
                    }
                }

                overHealAmount=AbstractDungeon.player.currentHealth+healAmount-AbstractDungeon.player.maxHealth;
                if(overHealAmount>0&&Zan.STORAGE.value>0){
                    zan.counter+=overHealAmount;
                    if(zan.counter>Zan.STORAGE.value){
                        zan.counter=Zan.STORAGE.value;
                    }
                }

                AbstractDungeon.player.heal(healAmount,true);

                //logger.info("药水栏位数="+__inst.potionSlots);
                for(int comb=0;comb<=__inst.potionSlots-2;comb++){
                    //logger.info("梳子="+comb);
                    //logger.info("左药水="+__inst.potions.get(comb).ID);
                    //logger.info("右药水="+__inst.potions.get(comb+1).ID);
                    __inst.potions.set(comb,__inst.potions.get(comb+1));
                    __inst.potions.get(comb).setAsObtained(comb);
                }
                //logger.info("待得药水="+potionToObtain.ID);
                __inst.potions.set(__inst.potionSlots-1,potionToObtain);
                potionToObtain.setAsObtained(__inst.potionSlots-1);
                potionToObtain.flash();
                AbstractPotion.playPotionSound();
                return SpireReturn.Return(true);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz= EntropicBrew.class,
            method="use"
    )
    public static class PatchTool2 {
        @SpirePrefixPatch
        public static void Prefix(EntropicBrew __inst, AbstractCreature target) {
            if(AbstractDungeon.player.hasRelic(Zan.ID)&&
                    ((Zan)AbstractDungeon.player.getRelic(Zan.ID)).secondCounter<Zan.LIMIT&&
                    AbstractDungeon.getCurrRoom().phase!=AbstractRoom.RoomPhase.COMBAT){
                AbstractDungeon.player.potionSlots=0;
                for(AbstractPotion p:AbstractDungeon.player.potions){
                    if(p instanceof PotionSlot){
                        AbstractDungeon.player.potionSlots++;
                    }
                }
                AbstractDungeon.player.potionSlots++;
            }
        }
    }

    @SpirePatch(
            clz= EntropicBrew.class,
            method="use"
    )
    public static class PatchTool3 {
        @SpirePostfixPatch
        public static void Postfix(EntropicBrew __inst, AbstractCreature target) {
            AbstractDungeon.player.potionSlots=AbstractDungeon.player.potions.size();
        }
    }

    @SpirePatch(
            clz= PotionHelper.class,
            method="getRandomPotion",
            paramtypez = {Random.class}
    )
    public static class PatchTool4 {
        @SpireInsertPatch(rloc = 1,localvars = {"randomKey"})
        public static void Insert(Random rng, @ByRef String[] randomKey) {
            while(SPECIAL_POTIONS.contains(randomKey[0])){
                randomKey[0] = PotionHelper.potions.get(rng.random(PotionHelper.potions.size() - 1));
            }
        }
    }

    @SpirePatch(
            clz= PotionHelper.class,
            method="getRandomPotion",
            paramtypez = {}
    )
    public static class PatchTool5 {
        @SpireInsertPatch(rloc = 1,localvars = {"randomKey"})
        public static void Insert(@ByRef String[] randomKey) {
            while(SPECIAL_POTIONS.contains(randomKey[0])){
                randomKey[0] = PotionHelper.potions.get(AbstractDungeon.potionRng.random(PotionHelper.potions.size() - 1));
            }
        }
    }

    public static final Set<String> SPECIAL_POTIONS = new HashSet<>(Arrays.asList(
            Blizzard.POTION_ID,
            PhantasmalShootingStar.POTION_ID
    ));
}
