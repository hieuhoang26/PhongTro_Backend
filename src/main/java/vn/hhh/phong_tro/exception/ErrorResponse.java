package vn.hhh.phong_tro.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class ErrorResponse {
    private Date timeStamp;
    private  int status;
    private  String path; // url
    private String error;
    private String message;
    private List<String> details;
}
