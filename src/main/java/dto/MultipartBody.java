package dto;

import lombok.Data;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

import javax.ws.rs.FormParam;
import javax.ws.rs.core.MediaType;
import java.io.InputStream;

@Data
public class MultipartBody {

    @FormParam("fileName")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    private String fileName;

    @FormParam("file")
    private InputStream file;
}
