package cn.edu.swu.wechat;


public class WeChatRequest {

    private String toUserName = null;
    private String fromUserName = null;
    private long createTime = Long.MIN_VALUE;
    private String msgType = null;
    private String content = null;
    private String msgId = null;
    private String picUrl = null;
    private String mediaId = null;
    private String thumbMediaId = null;

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

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
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

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ToUserName: ").append(this.getToUserName()).append("\n");
        sb.append("FromUserName: ").append(this.getFromUserName()).append("\n");
        sb.append("CreateTime: ").append(this.getCreateTime()).append("\n");
        sb.append("MsgId: ").append(this.getMsgId()).append("\n");
        sb.append("MsgType: ").append(this.getMsgType()).append("\n");
        sb.append("Content: ").append(this.getContent()).append("\n");
        sb.append("picUrl: ").append(this.getPicUrl()).append("\n");
        sb.append("mediaId: ").append(this.getMediaId()).append("\n");
        sb.append("thumbMediaId: ").append(this.getThumbMediaId());
        return sb.toString();
    }
}
