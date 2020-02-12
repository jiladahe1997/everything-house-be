package hello.DTO;

import hello.model.Common.Status;
import lombok.Data;

@Data
public class Result {
    Status status;
    Object data;

    public Result success(Object data) {
        Status status = new Status();
        status.setCode(0);
        status.setMsg("");
        this.setStatus(status);
        this.setData(data);
        return this;
    }

    public  Result success() {
        Status status = new Status();
        status.setCode(0);
        status.setMsg("");
        this.setStatus(status);
        this.setData(null);
        return this;
    }

    public Result fail(String reason) {
        Status status = new Status();
        status.setCode(-1);
        status.setMsg(reason);
        this.setStatus(status);
        return this;
    }

    public Result fail(Integer code, String reason) {
        Status status = new Status();
        status.setCode(code);
        status.setMsg(reason);
        this.setStatus(status);
        return this;
    }
}
