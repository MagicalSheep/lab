package cn.magicalsheep.model;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ticket {

    private int id;
    private int level;
    private int price;
    private String ownerID;
    private String ownerName;
    private String company;
    private String name;
    private String from;
    private String to;
    private Date startTime;
    private Date endTime;
    private String startPlace;
    private String endPlace;

    public Ticket(Plane plane, User owner, int level) {
        this.level = level;
        if (level == 1)
            price = plane.getLevel1Price();
        else if (level == 2)
            price = plane.getLevel2Price();
        else
            price = plane.getLevel3Price();
        company = plane.getCompany();
        name = plane.getName();
        ownerID = owner.getId();
        ownerName = owner.getName();
        from = plane.getFrom();
        to = plane.getTo();
        startTime = plane.getStartTime();
        endTime = plane.getEndTime();
        startPlace = plane.getStartPlace();
        endPlace = plane.getEndPlace();
    }

    public String getLevel() {
        if (level == 1)
            return "经济舱";
        else if (level == 2)
            return "公务舱";
        else
            return "头等舱";
    }

    public int getLevelID() {
        return level;
    }

}
