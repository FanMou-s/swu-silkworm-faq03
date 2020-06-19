package cn.edu.swu.wechat;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/*
# 文字
<xml>
<ToUserName><![CDATA[gh_99f283298ae1]]></ToUserName>
<FromUserName><![CDATA[oLLBW0XYtUuoz9UpmSixw0LEBfVI]]></FromUserName>
<CreateTime>1592577377</CreateTime>
<MsgType><![CDATA[text]]></MsgType>
<Content><![CDATA[蚕茧]]></Content>
<MsgId>22800227607571304</MsgId>
</xml>

# 图片
<xml>
<ToUserName><![CDATA[gh_99f283298ae1]]></ToUserName>
<FromUserName><![CDATA[oLLBW0XYtUuoz9UpmSixw0LEBfVI]]></FromUserName>
<CreateTime>1592580109</CreateTime>
<MsgType><![CDATA[image]]></MsgType>
<PicUrl><![CDATA[http://mmbiz.qpic.cn/mmbiz_jpg/6UjvUNl74I1WejjedTib8Am0ge4GY5YEZzdSjLEy7cJlGzSVlDkQS8Dlb0iaWYuuHU29FmOj0O2DzYRp6HV4R2zg/0]]></PicUrl>
<MsgId>22800264023876468</MsgId>
<MediaId><![CDATA[rO_na_kQ0woyaOOvZyXigWb_7L-AY6OIHfDlWe_qQ1aO5lCWyE0HDaYxFhFlg4F-]]></MediaId>
</xml>

# 视频
<xml>
<ToUserName><![CDATA[gh_99f283298ae1]]></ToUserName>
<FromUserName><![CDATA[oLLBW0XYtUuoz9UpmSixw0LEBfVI]]></FromUserName>
<CreateTime>1592580872</CreateTime>
<MsgType><![CDATA[video]]></MsgType>
<MediaId><![CDATA[sFpi6q7eym-AXfwK7A0JtX68wOfsqIlNiLYKKtd-bOze7wvgH9iaHF6hS3-5yNPl]]></MediaId>
<ThumbMediaId><![CDATA[biAOGkUzBg-4663WnM-HqDpZ2sPqVVhXG9aJbvhSepCptiBTKkKVT4xm91xsREC0]]></ThumbMediaId>
<MsgId>22800274580610871</MsgId>
</xml>

# 表情
<xml>
<ToUserName><![CDATA[gh_99f283298ae1]]></ToUserName>
<FromUserName><![CDATA[oLLBW0XYtUuoz9UpmSixw0LEBfVI]]></FromUserName>
<CreateTime>1592580661</CreateTime>
<MsgType><![CDATA[text]]></MsgType>
<Content><![CDATA[/::~]]></Content>
<MsgId>22800275412952825</MsgId>
</xml>

 */

public class WeChatRequestHandler extends DefaultHandler {
    public static final String ToUserName = "ToUserName";
    public static final String FromUserName = "FromUserName";
    public static final String CreateTime = "CreateTime";
    public static final String MsgType = "MsgType";
    public static final String Content = "Content";
    public static final String MsgId = "MsgId";
    public static final String PicUrl = "PicUrl";
    public static final String MediaId = "MediaId";
    public static final String ThumbMediaId = "ThumbMediaId";

    private WeChatRequest weixinRequest = null;
    private String elementValue =  null;

    @Override
    public void startDocument() throws SAXException {
        weixinRequest = new WeChatRequest();
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        elementValue = new String(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        switch (qName) {
            case ToUserName:
                this.weixinRequest.setToUserName(elementValue);
                break;
            case FromUserName:
                this.weixinRequest.setFromUserName(elementValue);
                break;
            case CreateTime:
                this.weixinRequest.setCreateTime(Long.valueOf(elementValue));
                break;
            case MsgType:
                this.weixinRequest.setMsgType(elementValue);
                break;
            case Content:
                this.weixinRequest.setContent(elementValue);
                break;
            case MsgId:
                this.weixinRequest.setMsgId(elementValue);
                break;
            case PicUrl:
                this.weixinRequest.setPicUrl(elementValue);
                break;
            case MediaId:
                this.weixinRequest.setMediaId(elementValue);
                break;
            case ThumbMediaId:
                this.weixinRequest.setThumbMediaId(elementValue);
                break;
        }
    }

    public WeChatRequest getWeixinRequest() {
        return this.weixinRequest;
    }
}
