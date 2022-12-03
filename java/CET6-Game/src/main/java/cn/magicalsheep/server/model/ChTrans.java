package cn.magicalsheep.server.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder(toBuilder = true)
public class ChTrans {
    private int id;
    private String pos;
    private final List<String> mean = new ArrayList<>();
}
