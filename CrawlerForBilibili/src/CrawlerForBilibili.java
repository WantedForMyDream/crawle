import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CrawlerForBilibili {
    private static  String aid;
    private static  String cid;
    private  static  String title;
    private  static  String up;
    private  static  String des;
    public static List<String> list = new ArrayList<String>();
    public static List<String> listurl = new ArrayList<String>();

    public static void main(String[] args) throws IOException, SQLException {
        // TODO Auto-generated method stub
        getElements();
        insertPNameAndURL();
    }

    private static void getElements( ) throws IOException, SQLException {
        // TODO Auto-generated method stub

        BufferedReader buffer = new BufferedReader(new InputStreamReader( ConnectionUtils.getConnection("https://www.bilibili.com/video/av40519765?t=135").getInputStream()) );

        //获得该视频的av号aid及弹幕序列号cid
        String targetStr="";
        String line=null;
        while (( line = buffer.readLine() ) != null) {
            targetStr=targetStr+line+"\n";
        }

        String reg = "cid=([0-9]+?)&aid=([0-9]+?)&";
        Pattern pattern = Pattern.compile(reg);

        //matcher:匹配
        Matcher matcher = pattern.matcher(targetStr);
        if(matcher.find())
        {
            aid = matcher.group(2);
            cid=" http://comment.bilibili.com/"+matcher.group(1)+".xml";
        }


        reg="<title([\\s\\S]+?)>([\\s\\S]+?)</title>";
        Pattern pattern2 = Pattern.compile(reg);

        Matcher matcher2 = pattern2.matcher(targetStr);
        if(matcher2.find())
        {
            up=matcher2.group(2);
        }
        System.out.println(title);

        //获取up主
        reg="name=\"author\" ([\\s\\S]+?)/>";
        Pattern pattern3 = Pattern.compile(reg);

        Matcher matcher3 = pattern3.matcher(targetStr);
        if(matcher2.find())
        {
            up=matcher3.group(1).replace("content","").replace("=","").replace("\"","");
        }
        System.out.println(up);


        //获取描述
        reg="\"desc\":\"(.+?)\",\"state";
        Pattern pattern4 = Pattern.compile(reg);

        Matcher matcher4 = pattern4.matcher(targetStr);
        if(matcher2.find())
        {
            des=matcher4.group(1);
        }
        System.out.println(des);

        //获取分p的名字
        reg="\"part\":\"(.+?)\"";
        Pattern pattern5 = Pattern.compile(reg);

        Matcher matcher5 = pattern5.matcher(targetStr);
        while( matcher2.find())
        {
                String string = matcher5.group(1);
                list.add(string);
        }

        //获取分p的url
        for (int i=1;i<=list.size();i++)
        {
            String string="https://www.bilibili.com/video/av"+aid+"/?p="+i;
            listurl.add(string);
            System.out.println(string);
        }

//        InputStreamReader isr = new InputStreamReader(ConnectionUtils.getConnection(cid).getInputStream(), "utf-8");
//        BufferedReader buffer2 = new BufferedReader(isr);
//
//        targetStr="";
//        line=null;
//        while (( line = buffer2.readLine() ) != null) {
//            targetStr=targetStr+line+"\n";
//        }
//
//        System.out.println(targetStr);


//        //正则匹配弹幕列表
//        String reg2 = "<d p=\"(.*?)\">([\\s\\S]+?)</d>";
//        Pattern pattern2 = Pattern.compile(reg2);
//
//        //matcher:匹配
//        Matcher matcher2 = pattern2.matcher(targetStr);
//        if(matcher2.find()){
//            while ( matcher2.find() ) {
//                String string = matcher2.group();
//                List<String> list = new ArrayList<String>();
//                list.add(string);
//                System.out.println(string);
//
////            for ( String string1 : list ) {
////                JDBC.insert(list1,list2,"Name","URL");
////                System.out.println( string1);
////            }
//            }
//        }
    }



    public static void insertPNameAndURL() throws SQLException {
        int i=0;
        for ( String string1 : list ) {
            JDBC.insert("nameandurl",list,listurl,"Name","URL");
        }

    }
}
