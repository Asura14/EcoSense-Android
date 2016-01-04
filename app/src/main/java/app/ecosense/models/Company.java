package app.ecosense.models;

public class Company {
    private String name;
    private Boolean aproved;
    private String avatar;


    public String getName() {
        return name;
    }
    public Boolean getAproved() {
        return aproved;
    }
    public String getAvatar() {
        if(avatar.equals("")) {
            return "https://www.fancyhands.com/images/default-avatar-250x250.png";
        } else {
            return avatar;
        }
    }

    public void setAproved(Boolean aproved) {
        this.aproved = aproved;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    public void setName(String name) {
        this.name = name;
    }
}

