package cn.magicalsheep.common.request;

import cn.magicalsheep.common.Validate;

import java.io.Serializable;

public record ModeRequest(
        int mode
) implements Serializable, Validate {

    @Override
    public boolean validate() {
        return mode >= 1 && mode <= 2;
    }
}
