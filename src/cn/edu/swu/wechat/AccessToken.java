package cn.edu.swu.wechat;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Calendar;

public class AccessToken {
    private String token;
    private int expiresIn;
    private LocalDateTime obtainTime;

    public AccessToken() {
    }

    public AccessToken(String token, int expiresIn) {
        this(token, expiresIn, LocalDateTime.now());
    }

    public AccessToken(String token, int expiresIn, LocalDateTime obtainTime) {
        this.setToken(token);
        this.setExpiresIn(expiresIn);
        this.setObtainTime(obtainTime);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public LocalDateTime getObtainTime() {
        return obtainTime;
    }

    public void setObtainTime(LocalDateTime obtainTime) {
        this.obtainTime = obtainTime;
    }

    public boolean isExpire() {
        LocalDateTime current = LocalDateTime.now();
        Duration duration = Duration.between(current, this.getObtainTime());
        return duration.getSeconds() >= this.getExpiresIn();
    }
}
