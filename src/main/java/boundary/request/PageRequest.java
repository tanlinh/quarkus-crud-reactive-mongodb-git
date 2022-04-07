package boundary.request;

import lombok.Data;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

@Data
public class PageRequest {

    @QueryParam("pageNum")
    @DefaultValue("0")
    private int pageNum;

    @QueryParam("pageSize")
    @DefaultValue("10")
    private int pageSize;
}
