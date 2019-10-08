package result;

import lombok.Getter;

@Getter
public class Result {

    private Integer code = 200;

    private String msg = "success";

    private static Result result = new Result();

    private Result() {

    }

    public static Result SUCCESS() {
        return result;
    }

    public static Result FAIL() {
        Result result = new Result();
        result.code = 500;
        result.msg = "系统异常";
        return result;
    }

    public static Result FAIL(Integer code, String msg) {
        Result result = new Result();
        result.code = code;
        result.msg = msg;
        return result;
    }
}