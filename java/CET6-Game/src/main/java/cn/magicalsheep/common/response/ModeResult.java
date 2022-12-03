package cn.magicalsheep.common.response;

import java.io.Serializable;

public record ModeResult(
        boolean isTrue,
        String msg,
        int currentScore,
        String answer
) implements Serializable {
}
