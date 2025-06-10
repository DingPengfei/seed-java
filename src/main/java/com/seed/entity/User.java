package com.seed.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户编码，全局唯一
     */
    private String code;

    /**
     * 用户名称
     */
    private String name;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 密码
     */
    private String passwd;

    /**
     * 空间编码，关联外键
     */
    private String spaceCode;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 家庭角色（1000-爸爸；2000-妈妈；3000-儿子；4000-女儿）
     */
    private String familyRole;

    /**
     * 出生日期
     */
    private Date birthDate;

    /**
     * 性别（1-男；0-女）
     */
    private String gender;

    /**
     * 声纹
     */
    private String voicePrintFile;

    /**
     * 创建者
     */
    private String createOperatorMan;

    /**
     * 创建者id
     */
    private String createOperatorId;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 修改者
     */
    private String modifyOperatorMan;

    /**
     * 修改者id
     */
    private String modifyOperatorId;

    /**
     * 更新时间
     */
    private Date gmtModified;

    /**
     * 是否删除 1-删除 0-未删除
     */
    private String isDeleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        User other = (User) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getCode() == null ? other.getCode() == null : this.getCode().equals(other.getCode()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getPhone() == null ? other.getPhone() == null : this.getPhone().equals(other.getPhone()))
            && (this.getEmail() == null ? other.getEmail() == null : this.getEmail().equals(other.getEmail()))
            && (this.getPasswd() == null ? other.getPasswd() == null : this.getPasswd().equals(other.getPasswd()))
            && (this.getSpaceCode() == null ? other.getSpaceCode() == null : this.getSpaceCode().equals(other.getSpaceCode()))
            && (this.getAvatar() == null ? other.getAvatar() == null : this.getAvatar().equals(other.getAvatar()))
            && (this.getFamilyRole() == null ? other.getFamilyRole() == null : this.getFamilyRole().equals(other.getFamilyRole()))
            && (this.getBirthDate() == null ? other.getBirthDate() == null : this.getBirthDate().equals(other.getBirthDate()))
            && (this.getGender() == null ? other.getGender() == null : this.getGender().equals(other.getGender()))
            && (this.getVoicePrintFile() == null ? other.getVoicePrintFile() == null : this.getVoicePrintFile().equals(other.getVoicePrintFile()))
            && (this.getCreateOperatorMan() == null ? other.getCreateOperatorMan() == null : this.getCreateOperatorMan().equals(other.getCreateOperatorMan()))
            && (this.getCreateOperatorId() == null ? other.getCreateOperatorId() == null : this.getCreateOperatorId().equals(other.getCreateOperatorId()))
            && (this.getGmtCreate() == null ? other.getGmtCreate() == null : this.getGmtCreate().equals(other.getGmtCreate()))
            && (this.getModifyOperatorMan() == null ? other.getModifyOperatorMan() == null : this.getModifyOperatorMan().equals(other.getModifyOperatorMan()))
            && (this.getModifyOperatorId() == null ? other.getModifyOperatorId() == null : this.getModifyOperatorId().equals(other.getModifyOperatorId()))
            && (this.getGmtModified() == null ? other.getGmtModified() == null : this.getGmtModified().equals(other.getGmtModified()))
            && (this.getIsDeleted() == null ? other.getIsDeleted() == null : this.getIsDeleted().equals(other.getIsDeleted()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getCode() == null) ? 0 : getCode().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getPhone() == null) ? 0 : getPhone().hashCode());
        result = prime * result + ((getEmail() == null) ? 0 : getEmail().hashCode());
        result = prime * result + ((getPasswd() == null) ? 0 : getPasswd().hashCode());
        result = prime * result + ((getSpaceCode() == null) ? 0 : getSpaceCode().hashCode());
        result = prime * result + ((getAvatar() == null) ? 0 : getAvatar().hashCode());
        result = prime * result + ((getFamilyRole() == null) ? 0 : getFamilyRole().hashCode());
        result = prime * result + ((getBirthDate() == null) ? 0 : getBirthDate().hashCode());
        result = prime * result + ((getGender() == null) ? 0 : getGender().hashCode());
        result = prime * result + ((getVoicePrintFile() == null) ? 0 : getVoicePrintFile().hashCode());
        result = prime * result + ((getCreateOperatorMan() == null) ? 0 : getCreateOperatorMan().hashCode());
        result = prime * result + ((getCreateOperatorId() == null) ? 0 : getCreateOperatorId().hashCode());
        result = prime * result + ((getGmtCreate() == null) ? 0 : getGmtCreate().hashCode());
        result = prime * result + ((getModifyOperatorMan() == null) ? 0 : getModifyOperatorMan().hashCode());
        result = prime * result + ((getModifyOperatorId() == null) ? 0 : getModifyOperatorId().hashCode());
        result = prime * result + ((getGmtModified() == null) ? 0 : getGmtModified().hashCode());
        result = prime * result + ((getIsDeleted() == null) ? 0 : getIsDeleted().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", code=").append(code);
        sb.append(", name=").append(name);
        sb.append(", phone=").append(phone);
        sb.append(", email=").append(email);
        sb.append(", passwd=").append(passwd);
        sb.append(", spaceCode=").append(spaceCode);
        sb.append(", avatar=").append(avatar);
        sb.append(", familyRole=").append(familyRole);
        sb.append(", birthDate=").append(birthDate);
        sb.append(", gender=").append(gender);
        sb.append(", voicePrintFile=").append(voicePrintFile);
        sb.append(", createOperatorMan=").append(createOperatorMan);
        sb.append(", createOperatorId=").append(createOperatorId);
        sb.append(", gmtCreate=").append(gmtCreate);
        sb.append(", modifyOperatorMan=").append(modifyOperatorMan);
        sb.append(", modifyOperatorId=").append(modifyOperatorId);
        sb.append(", gmtModified=").append(gmtModified);
        sb.append(", isDeleted=").append(isDeleted);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}