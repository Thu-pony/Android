package csumissu.fakewechat;

import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;

import junit.framework.Assert;

import org.junit.Test;

/**
 * @author sunyaxi
 * @date 2016/7/1
 */
public class JPinyinTest {

    @Test
    public void testHanzi() {
        String pinyin = PinyinHelper.convertToPinyinString("汉字", ",", PinyinFormat.WITHOUT_TONE);
        System.out.println("汉字=" + pinyin);
        Assert.assertEquals("han,zi", pinyin);
    }

    @Test
    public void testAscII() {
        String pinyin = PinyinHelper.convertToPinyinString("abc,;?", ",", PinyinFormat.WITHOUT_TONE);
        System.out.println("AscII=" + pinyin);
        Assert.assertEquals("abc,;?", pinyin);
    }

    @Test
    public void testAscIIAndHanzi() {
        String pinyin = PinyinHelper.convertToPinyinString("a汉B字,", ",", PinyinFormat.WITHOUT_TONE);
        System.out.println("AscIIAndHanzi=" + pinyin);
        Assert.assertEquals("a,han,B,zi,,", pinyin);
    }

    @Test
    public void testWhiteSpace() {
        String pinyin = PinyinHelper.convertToPinyinString(" a", ",", PinyinFormat.WITHOUT_TONE);
        System.out.println("HasWhiteSpace=" + pinyin);
        System.out.println("HasWhiteSpace(short)=" + PinyinHelper.getShortPinyin(" a"));
        Assert.assertEquals(" a", pinyin);
    }

    @Test
    public void testCompare() {
        String p1 = PinyinHelper.convertToPinyinString("安a娜b", "", PinyinFormat.WITHOUT_TONE);
        String p2 = PinyinHelper.convertToPinyinString("安a娜d", "", PinyinFormat.WITHOUT_TONE);
        String p3 = PinyinHelper.convertToPinyinString(" 安a娜d", "", PinyinFormat.WITHOUT_TONE);
        System.out.println(p1.compareTo(p2));
        System.out.println(p2.compareTo(p3));
    }


}
