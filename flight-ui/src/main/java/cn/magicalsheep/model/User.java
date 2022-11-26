package cn.magicalsheep.model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class User {

    private String id;
    private String name;
    private String pwd;
    private int group;
    private int money;
    private final Map<String, Ticket> ticketList = new HashMap<>();

    public void addTicket(Ticket ticket) {
        ticketList.put(ticket.getCompany() + " " + ticket.getName(), ticket);
    }

    public void removeTicket(String planeName) {
        ticketList.remove(planeName);
    }

    public boolean hasTicket(String planeName) {
        return ticketList.containsKey(planeName);
    }

    public Ticket getTicket(String planeName) {
        return ticketList.get(planeName);
    }

    public void addMoney(int val) {
        money += val;
    }

}
