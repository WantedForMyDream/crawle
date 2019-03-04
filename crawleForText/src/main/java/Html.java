import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Html {

    private static String title;
    private static String  author;
    private  static String des;
    private  static String img;
    public static List<String> list1 = new ArrayList<String>();
    public static List<String>list2=new ArrayList<String>();


    public static void main(String[] args) throws IOException, SQLException {
        // TODO Auto-generated method stub
        getHeadElements();
        getChapterNameAndURL();
        insertChapterNameAndURL();
        insertTXT();
    }

    private static void getHeadElements() throws IOException, SQLException {

        BufferedReader buffer = new BufferedReader(new InputStreamReader(HtmlUtils.getConnection("http://www.xbiquge.la/7/7004/").getInputStream()) );


        //得到标题
        String line=null;
        String reg1="book_name\" (.+)/";
        Pattern p1=Pattern.compile(reg1);

        //作者
        String reg2="author\" (.+)/";
        Pattern p2=Pattern.compile(reg2);

        //简介
        String reg3="og:description\" ([\\s\\S]+?)/>";
        Pattern p3=Pattern.compile(reg3);

        //图片
        String reg4="img alt=(.+)src=\"(.+?)\"";
        Pattern p4=Pattern.compile(reg4);

        //matcher:匹配标题
        String targetStr=null;
        while (( line = buffer.readLine() ) != null) {
            targetStr =targetStr+line+"\n";
        }
            Matcher matcher1 = p1.matcher(targetStr);
            Matcher matcher2 = p2.matcher(targetStr);
            Matcher matcher3 = p3.matcher(targetStr);
            Matcher matcher4 = p4.matcher(targetStr);

            if(matcher1.find()){
                title=matcher1.group(1).replace("content","").replace("=","").replace("\"","");
                JDBC.insert("HeadElements","title",title);
            }

            if(matcher2.find()){
                author=matcher2.group(1).replace("content","").replace("=","").replace("\"","");
                JDBC.insert("HeadElements","author",author);
            }

            if(matcher3.find()){
                des=matcher3.group(1).replace(" ","").replace("content","").replace("=","").replace("\"","");
                JDBC.insert("HeadElements","desr",des);
            }

            if(matcher4.find()){
                img=matcher4.group(2);
                JDBC.insert("HeadElements","img",img);
            }
    }


    private static void getChapterNameAndURL( ) throws IOException, SQLException {
        // TODO Auto-generated method stub

        BufferedReader buffer = new BufferedReader(new InputStreamReader(HtmlUtils.getConnection("http://www.xbiquge.la/7/7004/").getInputStream()) );


        //得到每一章节的标题和内容
        String line = null;
        String reg = "<dd><a href='([\\s\\S]+?)' >(.+?)</a></dd>";
        Pattern pattern = Pattern.compile(reg);

        //matcher:匹配
        while (( line = buffer.readLine() ) != null) {
            Matcher matcher = pattern.matcher(line);
            while ( matcher.find() ) {
                String string = matcher.group(2);
                list1.add(string);

                string=matcher.group(1);
                list2.add("http://www.xbiquge.la"+string);
            }
        }
    }

    //得到每一章小说的内容
    public static String getTXT(String string1) throws IOException {
        List<String> list3 = new ArrayList<String>();

        BufferedReader buffer = new BufferedReader(new InputStreamReader(HtmlUtils.getConnection(string1).getInputStream()) );

            String line = null;
            String reg = "&nbsp;&nbsp;&nbsp;&nbsp;([\\s\\S]+)";
            Pattern pattern = Pattern.compile(reg);

            while (( line = buffer.readLine() ) != null) {
                Matcher matcher = pattern.matcher(line);
                while ( matcher.find() ) {
                    String string = matcher.group(1).replace( "&nbsp","").replace("&amp","");
                    list3.add(string+"\n");
                }
            }

            list3.remove(list3.size()-1);

            String txt="";
            for (String s:list3)
            {
                txt=txt+s;
            }

            return txt;
    }

    //插入小说每一章的名字和内容
    public static void insertChapterNameAndURL() throws SQLException {
        int i=0;
        for ( String string1 : list1 ) {
            JDBC.insert("chapterandurl",list1,list2,"Name","URL");
        }

    }

    //插入小说的内容
    public  static void insertTXT() throws SQLException, IOException {
        int i=0;
        for(String string:list2)
        {
            JDBC.insert("content","Name","Content",list1.get(i),getTXT(string));
        }
    }

}
