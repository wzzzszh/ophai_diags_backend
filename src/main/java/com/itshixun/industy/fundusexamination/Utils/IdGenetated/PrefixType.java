package com.itshixun.industy.fundusexamination.Utils.IdGenetated;

public enum PrefixType {
    USER("User", 4),      // 用户ID前缀
    PATIENT("Patient",7); // 患者ID前缀

    private final String prefix;
    private final int length;

    PrefixType(String prefix, int length) {
        this.prefix = prefix;
        this.length = length;
    }

    public String getPrefix() {
        return prefix;
    }

    public int getTotalLength() {
        return length + 16; // 总长度固定16位
    }

    public int getLength() {
        return length;
    }
}