package csumissu.fakewechat;

import junit.framework.Assert;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class WeiboTest {

    private static final String AT = "@[\u4e00-\u9fa5\\w]+";
    private static final String TOPIC = "#[\u4e00-\u9fa5\\w]+#";
    private static final String EMOJI = "\\[[\u4e00-\u9fa5\\w]+\\]";
    private static final String URL = "http://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
    private static final String TEXT = "#你是猪#一不小心火锅@吃撑了，那就动动手指头在#亚洲新歌榜#为" +
            "@张靓颖 http://t.cn/RqFnKaZ  的http://t.cn/RqFvkxM 打榜吧！[哈哈]";

    @Test
    public void testUrl() throws Exception {
        singleTest(URL, "url", 2);
    }

    @Test
    public void testTopic() throws Exception {
        singleTest(TOPIC, "topic", 2);
    }

    @Test
    public void testAt() throws Exception {
        singleTest(AT, "at", 2);
    }

    @Test
    public void testEmoji() throws Exception {
        singleTest(EMOJI, "emoji", 1);
    }

    @Test
    public void testMulti() throws Exception {
        System.out.println("-------testMulti---------");
        Pattern pattern = Pattern.compile("(" + AT + ")|(" + TOPIC + ")|(" + EMOJI + ")|(" + URL + ")");
        Matcher matcher = pattern.matcher(TEXT);
        while (matcher.find()) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                String str = matcher.group(i);
                if (str != null) {
                    System.out.println("i=" + i + ", " + matcher.group(i));
                }
            }
        }
    }

    private void singleTest(String reg, String title, int expectValue) {
        // System.out.println("-------" + title + "--------");
        Pattern pattern = Pattern.compile("(" + reg + ")");
        Matcher matcher = pattern.matcher(TEXT);
        int count = 0;
        while (matcher.find()) {
            for (int i = 0; i < matcher.groupCount(); i++) {
                // System.out.println("i=" + i + ", " + matcher.group(i));
                count++;
            }
        }
        Assert.assertEquals(expectValue, count);
    }

}