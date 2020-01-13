package cn.edu.swu.silkworm.faq;

import java.util.ArrayList;
import java.util.List;

public class Question {
    private String type;
    private String question;
    private List<String> keywords = new ArrayList<>();
    private String answer;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public void addKeyword(String key) {
        if (key == null || key.trim().isEmpty()) {
            return;
        }
        this.keywords.add(key.trim());
    }

    public String getKeywordString() {
        StringBuffer sb = new StringBuffer();
        for(String keyword : this.getKeywords()) {
            sb.append(keyword).append(",");
        }
        return sb.toString();
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.getQuestion()).append("\t");
        sb.append(this.getType()).append("\t");
        for(String keyword : this.getKeywords()) {
            sb.append(keyword).append("\t");
        }
        sb.append(this.getAnswer()).append("\t");
        return sb.toString();
    }

    public String flat() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.getType()).append("。");
        sb.append(this.getQuestion()).append("。");
        for(String keyword : this.getKeywords()) {
            sb.append(keyword).append("。");
        }
        sb.append(this.getAnswer()).append("。");
        return sb.toString();
    }
}
