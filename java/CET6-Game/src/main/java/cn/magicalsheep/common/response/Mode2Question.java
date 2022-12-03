package cn.magicalsheep.common.response;

import java.io.Serializable;
import java.util.List;

public record Mode2Question(
        String word,
        List<String> options
) implements Serializable {
}
