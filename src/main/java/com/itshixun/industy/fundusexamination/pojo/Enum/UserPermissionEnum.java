package com.itshixun.industy.fundusexamination.pojo.Enum;

/**
 * 用户权限枚举类，定义不同的用户权限级别。
 */
public enum UserPermissionEnum {
    // 权限
    ADMIN(4, "管理员权限"),
    DOCTOR(3, "医生用户权限"),
    SCIENTIST(2, "科研人员权限"),
    PATIENT(1, "病人权限");

    private final int code;
    private final String description;

    UserPermissionEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
    public static UserPermissionEnum getByCode(int code) {
        for (UserPermissionEnum permission : values()) {
            if (permission.code == code) {
                return permission;
            }
        }
        throw new IllegalArgumentException("无效的权限code: " + code);
    }
}