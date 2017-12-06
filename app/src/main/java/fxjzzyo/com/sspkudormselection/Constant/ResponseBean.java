package fxjzzyo.com.sspkudormselection.Constant;

import java.io.Serializable;

/**
 * Created by fxjzzyo on 2017/11/21.
 */

public class ResponseBean implements Serializable {
    private String errcode;
    private String data;

    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
    }
    public String getErrcode() {
        return errcode;
    }
    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    @Override
    public String toString() {
        return "ResponseBean{" +
                "errcode='" + errcode + '\'' +
                ", datas=" + data +
                '}';
    }
}
