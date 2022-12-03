package cn.magicalsheep.common.request;

import cn.magicalsheep.common.Validate;

import java.io.Serializable;

public record Mode2AnswerRequest(
        int answer
) implements Serializable, Validate {

    public boolean validate() {
        return answer >= 1 && answer <= 4;
    }

}
