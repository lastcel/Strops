package strops.patch;

import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapEdge;
import com.megacrit.cardcrawl.map.MapRoomNode;
import strops.relics.Leviboard;

public class PatchLeviboard {

    public static boolean savedWingedConnection;

    @SpirePatch(
            clz = MapRoomNode.class,
            method = "wingedIsConnectedTo"
    )
    public static class PatchTool1 {
        @SpireInsertPatch(rloc = 1,localvars = {"edge"})
        public static SpireReturn<Boolean> Insert(MapRoomNode __inst, MapRoomNode node, MapEdge edge) {
            if(node.y==edge.dstY&&AbstractDungeon.player.hasRelic(Leviboard.ID)&&Leviboard.canFly()){
                return SpireReturn.Return(true);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = MapRoomNode.class,
            method = "update"
    )
    public static class PatchTool2 {
        @SpireInsertPatch(rloc = 72,localvars = {"wingedConnection"})
        public static void Insert(MapRoomNode __inst, @ByRef boolean[] wingedConnection) {
            savedWingedConnection=wingedConnection[0];
            if(AbstractDungeon.player.hasRelic(Leviboard.ID)&&Leviboard.canFly()){
                wingedConnection[0]=false;
            }
        }
    }

    @SpirePatch(
            clz = MapRoomNode.class,
            method = "update"
    )
    public static class PatchTool3 {
        @SpireInsertPatch(rloc = 82,localvars = {"wingedConnection"})
        public static void Insert(MapRoomNode __inst, @ByRef boolean[] wingedConnection) {
            wingedConnection[0]=savedWingedConnection;
        }
    }
}
