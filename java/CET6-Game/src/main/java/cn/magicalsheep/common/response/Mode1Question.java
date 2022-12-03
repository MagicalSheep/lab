package cn.magicalsheep.common.response;

import java.io.Serializable;

public record Mode1Question(
        String mean,
        String tips,
        int remain
) implements Serializable {
}
