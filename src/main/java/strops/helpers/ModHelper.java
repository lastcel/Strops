package strops.helpers;

public class ModHelper {
    public static String makePath(String id) {
        return "Strops:" + id;
    }

    //生成遗物图片路径
    public static String makeIPath(String id){
        return "StropsResources/img/relics/"+id+".png";
    }

    //生成遗物轮廓图片路径
    public static String makeOPath(String id){
        return "StropsResources/img/relics/outline/"+id+".png";
    }
}
