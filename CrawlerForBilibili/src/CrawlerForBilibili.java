import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
    public static List<String> nameList = new ArrayList<String>();
    public static List<String> urlList = new ArrayList<String>();

    public static void main(String[] args) throws IOException, SQLException {
        // TODO Auto-generated method stub
        getElements();
        insertPNameAndURL();
    }

    private static void getElements( ) throws IOException, SQLException {
        // TODO Auto-generated method stub

        BufferedReader buffer = new BufferedReader(new InputStreamReader( ConnectionUtils.getConnection("https://www.bilibili.com/video/av29306544/?p=1").getInputStream()) );

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
            cid=matcher.group(1);
        }
        String commentURL=" http://comment.bilibili.com/"+cid+".xml";//获取弹幕地址
//        System.out.println(commentURL);

        reg="<title([\\s\\S]+?)>([\\s\\S]+?)</title>";
        Pattern pattern2 = Pattern.compile(reg);

        Matcher matcher2 = pattern2.matcher(targetStr);
        if(matcher2.find())
        {
            title=matcher2.group(2);
        }
//        System.out.println(title);
        JDBC.insert("elements","Name","Content","title",title);

        //获取up主
        reg="name=\"author\" ([\\s\\S]+?)/>";
        Pattern pattern3 = Pattern.compile(reg);

        Matcher matcher3 = pattern3.matcher(targetStr);
        if(matcher3.find())
        {
            up=matcher3.group(1).replace("content","").replace("=","").replace("\"","");
        }
//        System.out.println(up);
        JDBC.insert("elements","Name","Content","up",up);


        //获取描述
        reg="\"desc\":\"(.+?)\",\"state";
        Pattern pattern4 = Pattern.compile(reg);

        Matcher matcher4 = pattern4.matcher(targetStr);
        if(matcher4.find())
        {
            des=matcher4.group(1);
        }
//        System.out.println(des);
        JDBC.insert("elements","Name","Content","des",des);

        //获取分p的名字
        reg="\"part\":\"(.+?)\"";
        Pattern pattern5 = Pattern.compile(reg);

        Matcher matcher5 = pattern5.matcher(targetStr);
        while( matcher5.find())
        {
                String string = matcher5.group(1);
                nameList.add(string);
                System.out.println(string);
        }

        //获取分p的url
        for (int i=1;i<=nameList.size();i++)
        {
            String string="https://www.bilibili.com/video/av"+aid+"/?p="+i;
            urlList.add(string);
            System.out.println(string);
        }


//        BufferedReader buffer2 = new BufferedReader(new InputStreamReader( ConnectionUtils.getConnection(commentURL).getInputStream(),"UTF-8") );
//
//        targetStr="";
//        line=null;
//        while (( line = buffer2.readLine() ) != null) {
//            targetStr=targetStr+line+"\n";
//        }
//
////        System.out.println(targetStr);
//
//        //正则匹配弹幕列表
//        String reg2 = "<d p=\"(.*?)\">([\\s\\S]+?)</d>";
//        Pattern pattern6 = Pattern.compile(reg2);
//
//        //matcher:匹配
//        Matcher matcher6 = pattern6.matcher(targetStr);
//        if(matcher6.find()){
//            while ( matcher6.find() ) {
//                String string = matcher6.group();
//                List<String> list = new ArrayList<String>();
//                list.add(string);
//                System.out.println(string);
//
//            }
//        }
    }

    public static void insertPNameAndURL() throws SQLException {

            JDBC.insert("nameandurl",nameList,urlList,"Name","Content");

    }
}
