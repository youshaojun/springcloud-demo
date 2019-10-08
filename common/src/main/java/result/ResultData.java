package result;

import lombok.Getter;

@Getter
public class ResultData<T> {

    private Integer code = 200;

    private String msg = "success";

    private T data;

    private ResultData() {

    }

    public static <T> ResultData SUCCESS(T data) {
        ResultData resultData = new ResultData();
        resultData.data = data;
        return resultData;
    }
}
