package elefant.clone.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class ImageHandler {
    @Value("${upload_dir}")
    public final String UPLOAD_DIR;

    public ImageHandler(@Value("${upload_dir}") String UPLOAD_DIR) {
        this.UPLOAD_DIR = UPLOAD_DIR;
    }
    @GetMapping("/images_spring/{filename}")
    public ResponseEntity<?> downloadImageFromFile(@PathVariable String filename)  {
        UPLOAD_DIR.replace("\\","/");
        Path uploadPath = Paths.get(UPLOAD_DIR);

        Path filePath = uploadPath.resolve(filename);

        byte[] imageData = new byte[0];
        try {
            imageData = Files.readAllBytes(filePath);

        } catch (Exception e){

        }
        return ResponseEntity.status(HttpStatus.OK).body(imageData);
    }
}
