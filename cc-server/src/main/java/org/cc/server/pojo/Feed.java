package org.cc.server.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Feed {
    private String textContent;
    private String location;
    private String longitude;
    private String latitude;
    private MultipartFile[] imageContent;
}
