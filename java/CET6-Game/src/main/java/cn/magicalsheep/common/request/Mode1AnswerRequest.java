package cn.magicalsheep.common.request;

import cn.magicalsheep.common.Validate;

import java.io.Serializable;

public record Mode1AnswerRequest(
        String word
) implements Serializable, Validate {
    @Override
    public boolean validate() {
        return true;
    }
}
