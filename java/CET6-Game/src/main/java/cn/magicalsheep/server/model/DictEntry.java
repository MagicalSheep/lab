package cn.magicalsheep.server.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class DictEntry {
    private String enS;
    private final List<ChTrans> chS = new ArrayList<>();

}
