package cn.magicalsheep.model;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Plane {

    private String company;
    private String name;
    private String from;
    private String to;
    private Date startTime;
    private Date endTime;
    private String startPlace;
    private String endPlace;
    private int level1TicketNumber;
    private int remainLevel1TicketNumber;
    private int level1Price;
    private int level2TicketNumber;
    private int remainLevel2TicketNumber;
    private int level2Price;
    private int level3TicketNumber;
    private int remainLevel3TicketNumber;
    private int level3Price;

}
