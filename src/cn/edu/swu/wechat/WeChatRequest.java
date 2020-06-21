package cn.edu.swu.wechat;


public class WeChatRequest {

    private String toUserName = null;
    private String fromUserName = null;
    private long createTime = Long.MIN_VALUE;
    private MessageType msgType = MessageType.UNKNOWN;
    private String content = null;
    private String msgId = null;
    private String picUrl = null;
    private String mediaId = null;
    private String thumbMediaId = null;
    private String openId = null;

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public MessageType getMsgType() {
        return msgType;
    }

    public void setMsgType(MessageType msgType) {
        this.msgType = msgType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getPicUrl() { return picUrl; }

    public void setPicUrl(String picUrl) { this.picUrl = picUrl; }

    public String getMediaId() { return mediaId; }

    public void setMediaId(String mediaId) { this.mediaId = mediaId; }

    public String getThumbMediaId() { return thumbMediaId; }

    public void setThumbMediaId(String thumbMediaId) { this.thumbMediaId = thumbMediaId; }

    public String getOpenId() { return openId; }

    public void setOpenId(String openId) { this.openId = openId; }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ToUserName: ").append(this.getToUserName()).append("\n");
        sb.append("FromUserName: ").append(this.getFromUserName()).append("\n");
        sb.append("CreateTime: ").append(this.getCreateTime()).append("\n");
        sb.append("MsgId: ").append(this.getMsgId()).append("\n");
        sb.append("MsgType: ").append(this.getMsgType()).append("\n");
        sb.append("Content: ").append(this.getContent());
        //sb.append("PicUrl: ").append(this.getPicUrl()).append("\n");
        //sb.append("MediaId: ").append(this.getMediaId()).append("\n");
        //sb.append("ThumbMediaId: ").append(this.getThumbMediaId());
        return sb.toString();
    }
}
