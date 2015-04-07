package com.coding91.test;


import com.coding91.utils.HttpPostUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Administrator
 */
public class TestRadomRead {

    public static String DELETE_DOING = "DEL RDIS DOING FAILURE(ALL) ";
    public static String DELETE_FINISH = "DEL RDIS FINISH FAILURE(ALL) ";
    public static String DELETE_DOING_SUB = "HDEL RDIS DOING FAILURE ";
    public static String DELETE_FINISH_SUB = "HDEL RDIS FINISH FAILURE ";
    public static String SET_DOING_SUB = "HSET RDIS DOING FAILURE,";
    public static String SET_FINISH_SUB = "HSET RDIS FINISH FAILURE,";
    public static String DELETE_USER_EX_SUB = "HDEL RDIS USER_EX FAILURE,";

    public static void main(String[] args) {
//        try {
            //        String originField = "de_de_item_name";
//        String prefix = originField.substring(0, 5);
//        String currentFiled = originField.substring(6);
//        System.out.println("prefix:" + prefix + "  currentFiled:" + currentFiled);

//        FileWriter fw = null;
//        int totalRows = 2000000;
//        try {
//            fw = new FileWriter("d:/uid.txt");
//            for (int i = 0; i < totalRows; i++) {
//                fw.write("100000374596806,10\r\n");
//            }
//
//            fw.close();
//        } catch (IOException ex) {
//            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        Map<String, String> fileNameMapping = new HashMap();
//        fileNameMapping.put(DELETE_DOING, "deleteDoing.txt");
//        fileNameMapping.put(DELETE_FINISH, "deleteFinish.txt");
//        fileNameMapping.put(DELETE_DOING_SUB, "deleteDoingSub.txt");
//        fileNameMapping.put(DELETE_FINISH_SUB, "deleteFinishSub.txt");
//        fileNameMapping.put(SET_DOING_SUB, "setDoingSub.txt");
//        fileNameMapping.put(SET_FINISH_SUB, "setFinishSub.txt");
//        fileNameMapping.put(DELETE_USER_EX_SUB, "deleteUserEx.txt");
//
//        readFileByLines("d:/www/tmp/log.txt", fileNameMapping);
//        String[] uids = new String[]{"100007898268030","100009111409127","100004153355354", "100002004326457", "100003290928002", "100004315785512", "100001613793606", "576470706", "100007711802281", "100000464413419", "100005344982973", "100006394090995", "100004346642317", "100004025483098", "100000220695540", "1366920655", "100005347448223", "100005251202802", "100002322082876", "100000189932041", "100003289713864", "100006272102453", "100000865385389", "100000906504240", "100005494270324", "100002403626354", "100001006414269", "100004908698898", "100001065784966", "100005221064512", "100007180381045", "100001404012033", "100001289885931", "100004018077024", "100002152939064", "100004608251940", "100004483390282", "1034273111", "1129121369", "100006596377017", "100001520518052", "100005080623944", "100009205567805", "100006424053852", "1311878098", "100000624465371", "100004839070674", "100003971030943", "100008570653139", "100006706633910", "100007108579096", "100003871674395", "100003176015530", "100001870487147", "100005816441261", "100008102028769", "100001195917330", "100005182596902", "100002624797835", "100006366390531", "100001226921199", "100006479455406", "1040893036", "100003722684143", "100001913119633", "100002288195553", "100004680822351", "100000851066769", "1740451493", "100000596425469", "100001404407277", "100004285322582", "100005187099254", "100000437102929", "100004859559964", "100000681674917", "100006411272219", "100001987528211", "1397670331", "100005521664157", "100004673351610", "100001919212413", "100004500572642", "100003137401137", "100003883808774", "100002042894603", "100004889472085", "100002697271304", "100003816752567", "1156630735", "100007628767702", "1643443129", "100005366433474", "100005256933707", "100000640448251", "100001625038820", "100004017946957", "100006836430814", "100005237495733", "100003856490054", "74400456", "100004934610701", "100006893891008", "100000953312617", "100006189964334", "1699831292", "100006241151789", "100000029523629", "1005936275", "100004420001412", "100002586915096", "100003956904585", "100003334512320", "100004833673742", "1596271041", "100001704300575", "100003546531611", "637602737", "100000377789246", "100001350666698", "100006299260180", "100004827547833", "100000186334245", "100005624441104", "1509428205", "100000288581534", "100005989022271", "100001014127820", "100004884786127", "100002269013918", "100002031976132", "100003870751319", "100002666144889", "1422272882", "100001429061651", "100005639143583", "1133351357", "100001864120917", "100000168154364", "100006210768644", "100007652675575", "1203803797", "100004663594177", "100005491037205", "100001276891390", "100004049151388", "1385754538", "100005302052074", "100006234490804", "615580116", "100007631551718", "100007063426886", "100006779070619", "100008103856871", "100003107820248", "100005393056882", "100003490627043", "100003171406049", "1783885283", "1808575229", "100002476985316", "100001969701510", "1613382589", "100004014936620", "100007128577910", "100002449633097", "100001006929712", "1292942316", "598520314", "100005183652588", "1321577652", "100002244532253", "100005799630462", "100001273551754", "100001478088509", "100000518223114", "100001683471881", "100001932819275", "100004267418149", "100003881783204", "100004892792574", "100000078746696", "1594260006", "100004796736264", "100006879879575", "100008019990273", "100006447766569", "100006204900533", "548854214", "100003148402661", "100002403728407", "100002083271832", "1160637048", "551663373", "100006653852865", "100000612818524", "100002215328113", "1698507552", "1234582051", "100000554822422", "100005791930986", "100005455213103", "100000130365486", "530662253", "100006721385152", "100003005716969", "100002999411377", "1385443527", "100006127093606", "1640435827", "100003809303674", "100003519565336", "1120846467", "100003315218240", "100003139819368", "100002830370865", "1363987177", "100000321801792", "100005454074539", "100005405463237", "100008368772405", "1471904704", "100000357332766", "100005688721387", "1042947540", "100007082588282", "100005161660323", "100002111560851", "100004490708992", "1321782776", "100000702100900", "1475363321", "100004554362713", "100005935480846", "100007255563855", "100005021670321", "100005151991615", "100003474608765", "1702407444", "100003652805253", "100000422079756", "1582148408", "100001342116648", "1357450745", "100002706771258", "100002891675654", "100001974543666", "100005941809540", "100000517939671", "100005451547015", "100002404486814", "100000773264472", "100001266767400", "100000528430053", "100003171608271", "100004248645751", "100005586876313", "100002959610697", "100004682529245", "100003914583914", "100006125654467", "100005482029980", "100002587341788", "100001545485276", "1520843168", "100006242234303", "100003975164352", "100006178389409", "100002219137821", "100002667851056", "100001252102143", "100002314600029", "1205904787", "100006642437552", "100004575481899", "1525118880", "100007959811766", "100005529033721", "100006086644648", "100006107138000", "100007403892010", "100000176706542", "100004636657063", "100006306792538", "100006066631290", "100008385493012", "100005287334093", "100002403197584", "1527505539", "1578452286", "100001024255302", "100000695768626", "1765591465", "100006426885649", "100007901072535", "100006825306584", "1451630688", "100000182245322", "100002436985815", "100005479020064", "100003677317893", "642394765", "100001821556339", "100000572232248", "100005475641330", "100002724548861", "100006699931843", "100006715626654", "100004496612300", "100004947425482", "1435897553", "100000148969413", "100007252591069", "100003320329043", "1482064326", "100002643220891", "100005833422502", "100005807466559", "100001819961820", "100000151285682", "100000294052358", "100006573030453", "100000003818157", "100004004935971", "100000382428023", "100002711222345", "100007119617679", "100004684120141", "100003839892421", "100000278928974", "100002323695599", "100004406461416", "100000668440908", "100004200435485", "100004030158049", "100003176682605", "1243683701", "100003995803374", "100005922992899", "100001226718264", "100003326704400", "1382561755", "100004236672407", "100000621693129", "688378576", "100002390239614", "1559620235", "1590230513", "100004508465717", "100006849977201", "1527084839", "602917955", "100003637820330", "100003273324486", "100004103115300", "100000937135989", "100006086252304", "100004983758899", "100004260854130", "100001562830509", "100002241226000", "100008005507947", "100003195786053", "100003782125808", "100004003851498", "100005851625055", "1413559600", "100004929516413", "100000843665785", "100004842066180", "100005146510190", "554326106", "1690760959", "100003105798577", "100005092243698", "100006147939242", "100006631713289", "100000234515810", "100004198230488", "100001201576925", "100001114659527", "100005283602169", "100005954580669", "100003076224114", "100000487993230", "100004045414738", "100000553922643", "100003927281748", "100005452241093", "100002134105921", "100002070598025", "100003996256641", "100005725444739", "100001843363152", "100007702664693", "100005981147167", "1615326845", "1463806564", "100006019985156", "100004500974063", "100005909403862", "100002686580660", "672651352", "100002581313453", "100000245538956", "100007727403911", "100008166724377", "1003637746", "100004056551543", "100000406364293", "100002229003852", "100002928884060", "100000479682361", "100000921910562", "100004685523518", "100004069004878", "100004547591488", "1006457974", "100004395693122", "100002972721090", "589147254", "100005819924048", "100000586666756", "1306442027", "100005463670349", "100003731590341", "100003004836620", "100001387904173", "1290705990", "100002957333162", "100007633951960", "100002568252516", "100005609584676", "100000752165715", "100008258719399", "1035760028", "100005669162981", "100001991553638", "100000875747886", "100001935905328", "100001073457431", "100004765661048", "100005095293414", "100004501203817", "100000957198412", "100006478931960", "1453901097", "100000670270593", "100006608352655", "100005888730423", "637055739", "100006260496095", "100003035230691", "100000288828653", "1746521409", "100003946105960", "100004104760922", "100003681522667", "100000296067467", "100003277653421", "100000185932582", "100002046668821", "100005978559856", "100005733284372", "100006823441437", "100000679458273", "100007661254084", "100002446222445", "574580155", "100000291767659", "100001018800551", "100009046547073", "100002579021521", "100003127545680", "100004456504115", "100001992101159", "100005005144125", "100005096942634", "100005906654178", "100000400429474", "100001549249274", "100004411264692", "100001834616956", "100002440826387", "100004076694258", "100003001875635", "100002267735054", "100006381191134", "1142501366", "100006808629634", "100003506483258", "100001725457559", "100002393146406", "100004874310608", "100000428476189", "100001634312654", "100005956986147", "100001892150307", "100006298329746", "100002739453575", "100005186499770", "551404268"};
//        Map params = new HashMap();
//        for (int i = 0; i < uids.length; i++) {
//            String ret = getURLContent(uids[i]);
//            System.out.println(ret);
//            System.out.println("========================");
//        }
//            LineNumberReader lnr = new LineNumberReader(new FileReader("D:/www/test/allinone.txt")); 
//            int countRow = 10;
//            for (int i = 0; i < countRow;i++) {
//                int currentLineNumber = getRandom(100000);
//                lnr.setLineNumber(currentLineNumber);
//                String content = lnr.readLine();
//                System.out.print(currentLineNumber);
//                System.out.println(":" + content);
//            }
//            File file = new File("D:/www/test/allinone.txt");
//            Scanner scanner = new Scanner(file);
//            scanner.useDelimiter("\n");
            //====================================
            String url = "https://dev-fb-dessertshop.shinezone.com/version/dev_jeff/j7/j7.php?/index/getToken";
            Map params = new HashMap();
            for (int i = 0; i < 10000; i++) {
                params.put("fcuid", "100004174536258"/*String.valueOf(i)*/);
                String ret = getURLContent(url, params);
                System.out.println(ret);
            }

//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(TestRadomRead.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(TestRadomRead.class.getName()).log(Level.SEVERE, null, ex);
//        }

    }

    public static int getRandom(int max) {
        Random random = new Random();
        return random.nextInt(max);
    }

    /**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     *
     * @param fileName
     * @param fileNameMapping
     */
    public static void readFileByLines(String fileName, Map<String, String> fileNameMapping) {
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            FileWriter deleteDoingWriter = null;
            FileWriter deleteFinishWriter = null;
            FileWriter deleteDoingSubWriter = null;
            FileWriter deleteFinishSubWriter = null;
            FileWriter setDoingSubWriter = null;
            FileWriter setFinishSubWriter = null;
            FileWriter deleteSuserExSubWriter = null;
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                tempString = tempString.trim();
                if (tempString.startsWith(DELETE_DOING)) {
                    tempString = tempString.replace(DELETE_DOING, "");
                    if (!tempString.isEmpty()) {
                        String[] info = tempString.split(",");
                        if (deleteDoingWriter == null) {
                            deleteDoingWriter = new FileWriter(LOG_BASE_DIR + fileNameMapping.get(DELETE_DOING));
                        }
                        if (info.length == 3) {
                            deleteDoingWriter.write(info[0] + "," + info[2] + "\r\n");
                        } else {
                            System.out.println("line " + line + ": " + tempString + " info len:" + info.length);
                        }
                    }
                } else if (tempString.startsWith(DELETE_FINISH)) {
                    tempString = tempString.replace(DELETE_FINISH, "");
                    if (!tempString.isEmpty()) {
                        String[] info = tempString.split(",");
                        if (deleteFinishWriter == null) {
                            deleteFinishWriter = new FileWriter(LOG_BASE_DIR + fileNameMapping.get(DELETE_FINISH));
                        }
                        if (info.length == 3) {
                            deleteFinishWriter.write(info[0] + "," + info[2] + "\r\n");
                        } else {
                            System.out.println("line " + line + ": " + tempString + " info len:" + info.length);
                        }
                    }
                } else if (tempString.startsWith(DELETE_DOING_SUB)) {
                    tempString = tempString.replace(DELETE_DOING_SUB, "");
                    if (!tempString.isEmpty()) {
                        String[] info = tempString.split(",");
                        if (deleteDoingSubWriter == null) {
                            deleteDoingSubWriter = new FileWriter(LOG_BASE_DIR + fileNameMapping.get(DELETE_DOING_SUB));
                        }
                        if (info.length == 4) {
                            deleteDoingSubWriter.write(info[0] + "," + info[2] + "," + info[3] + "\r\n");
                        } else {
                            System.out.println("line " + line + ": " + tempString + " info len:" + info.length);
                        }
                    }
                } else if (tempString.startsWith(DELETE_FINISH_SUB)) {
                    tempString = tempString.replace(DELETE_FINISH_SUB, "");
                    if (!tempString.isEmpty()) {
                        String[] info = tempString.split(",");
                        if (deleteFinishSubWriter == null) {
                            deleteFinishSubWriter = new FileWriter(LOG_BASE_DIR + fileNameMapping.get(DELETE_FINISH_SUB));
                        }
                        if (info.length == 4) {
                            deleteFinishSubWriter.write(info[0] + "," + info[2] + "," + info[3] + "\r\n");
                        } else {
                            System.out.println("line " + line + ": " + tempString + " info len:" + info.length);
                        }
                    }
                } else if (tempString.startsWith(SET_DOING_SUB)) {
                    tempString = tempString.replace(SET_DOING_SUB, "");
                    if (!tempString.isEmpty()) {
                        String[] info = tempString.split(",");
                        if (setDoingSubWriter == null) {
                            setDoingSubWriter = new FileWriter(LOG_BASE_DIR + fileNameMapping.get(SET_DOING_SUB));
                        }
                        if (info.length == 4) {
                            setDoingSubWriter.write(info[0] + "," + info[2] + "," + info[3] + "\r\n");
                        } else {
                            System.out.println("line " + line + ": " + tempString + " info len:" + info.length);
                        }
                    }
                } else if (tempString.startsWith(SET_FINISH_SUB)) {
                    tempString = tempString.replace(SET_FINISH_SUB, "");
                    if (!tempString.isEmpty()) {
                        String[] info = tempString.split(",");
                        if (setFinishSubWriter == null) {
                            setFinishSubWriter = new FileWriter(LOG_BASE_DIR + fileNameMapping.get(SET_FINISH_SUB));
                        }
                        if (info.length == 4) {
                            setFinishSubWriter.write(info[0] + "," + info[2] + "," + info[3] + "\r\n");
                        } else {
                            System.out.println("line " + line + ": " + tempString + " info len:" + info.length);
                        }
                    }
                } else if (tempString.startsWith(DELETE_USER_EX_SUB)) {
                    tempString = tempString.replace(DELETE_USER_EX_SUB, "");
                    if (!tempString.isEmpty()) {
                        String[] info = tempString.split(",");
                        if (deleteSuserExSubWriter == null) {
                            deleteSuserExSubWriter = new FileWriter(LOG_BASE_DIR + fileNameMapping.get(DELETE_USER_EX_SUB));
                        }
                        if (info.length == 4) {
                            deleteSuserExSubWriter.write(info[0] + "," + info[2] + "," + info[3] + "\r\n");
                        } else {
                            System.out.println("line " + line + ": " + tempString + " info len:" + info.length);
                        }
                    }
                }
//                System.out.println("line " + line + ": " + tempString);
                line++;
            }

            if (deleteDoingWriter != null) {
                deleteDoingWriter.close();
            }
            if (deleteFinishWriter != null) {
                deleteFinishWriter.close();
            }
            if (deleteDoingSubWriter != null) {
                deleteDoingSubWriter.close();
            }
            if (deleteFinishSubWriter != null) {
                deleteFinishSubWriter.close();
            }
            if (setDoingSubWriter != null) {
                setDoingSubWriter.close();
            }
            if (setFinishSubWriter != null) {
                setFinishSubWriter.close();
            }
            if (deleteSuserExSubWriter != null) {
                deleteSuserExSubWriter.close();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
    private static final String LOG_BASE_DIR = "d:/www/tmp/";

    public static String getURLContent(String uid) {
        uid = uid.trim();
        //String urlStr = "https://dev-fb-dessertshop.shinezone.com/version/dev_qa/j7/j7.php?/Cgi/GetMissionInfo&fcuid=" + uid;
        String urlStr = "https://dessertshop-fb.shinezone.com/version/V1.11.3.2000/j7/j7.php?/Cgi/GetMissionInfo&fcuid=" + uid;
        Map<String, String> paramMap = new HashMap();
        String result = HttpPostUtils.httpPost(urlStr, paramMap);
        return result;
    }

    public static String getURLContent(String url, Map paramMap) {
        String result = HttpPostUtils.httpPost(url, paramMap);
        return result;
    }

}
