package kr.or.ddit.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Locale;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class TestController {
	
	
	@RequestMapping(value = "/chatting", method = RequestMethod.GET)
	public String chatting(Locale locale, Model model) {
		
		
		return "chatting";
	}
	
	@RequestMapping(value = "/fileSend", method = RequestMethod.POST)
	public ResponseEntity<String> fileSend(MultipartFile file) {
		
		ResponseEntity<String> entity = null;
		try {
			
			String uploadDir = "C:\\Users\\PC-26\\Desktop\\test";
			Path filePath = Paths.get(uploadDir, file.getOriginalFilename());
			long copiedBytes = Files.copy(file.getInputStream(), filePath,StandardCopyOption.REPLACE_EXISTING);
			if (copiedBytes == file.getSize()) {
                // 파일 저장 완료 후 처리할 작업 수행
                // ...
				entity=new ResponseEntity<String>(HttpStatus.OK);
            } else {
                // 파일 복사 실패 처리
            	entity=new ResponseEntity<String>(HttpStatus.EXPECTATION_FAILED);
            }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			entity=new ResponseEntity<String>(HttpStatus.EXPECTATION_FAILED);
		}
		return entity;
	}
	@RequestMapping(value = "/fileRead", method = RequestMethod.POST)
	public ResponseEntity<byte[]> fileRead(String fname) {
		// 파일을 읽는 로직
	    byte[] fileContent=null;
		try {
			File file = new File("C:\\Users\\PC-26\\Desktop\\test\\"+fname);
			FileInputStream fis = new FileInputStream(file);
			fileContent = IOUtils.toByteArray(fis);
			fis.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    // 응답 헤더 설정
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_PDF);
	    headers.setContentDispositionFormData("attachment", "file.pdf");
	    
	    return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
	}
}
