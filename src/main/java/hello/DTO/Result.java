package hello.DTO;

import com.fasterxml.jackson.annotation.JsonView;
import hello.model.Common.Status;
import lombok.Data;

@Data
public class Result {
    @JsonView(resultView.class)
    Status status;
    @JsonView(resultView.class)
    Object data;

    public interface resultView{};


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
