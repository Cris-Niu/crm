package com.xxx.crm.dto;

public class TreeModel {
    private Integer id;
    private String name;
    private Integer  pId;

    //回显用户权限用
    private Boolean checked;

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public TreeModel() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getpId() {
        return pId;
    }

    public void setpId(Integer pId) {
        this.pId = pId;
    }

    @Override
    public String toString() {
        return "TreeModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", pId=" + pId +
                ", checked=" + checked +
                '}';
    }
}
