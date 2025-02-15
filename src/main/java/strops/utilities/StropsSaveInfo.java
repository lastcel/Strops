package strops.utilities;

public class StropsSaveInfo {
    public String s_lastPotion;
    public int s_continuousRest;
    public int s_continuousNonElite;
    public StropsSaveInfo(String lastPotion, int continuousRest, int continuousNonElite){
        s_lastPotion=lastPotion;
        s_continuousRest=continuousRest;
        s_continuousNonElite=continuousNonElite;
    }
}
