package com.lhs.documentTextExtraction.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hwpf.HWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.lhs.documentTextExtraction.service.PDFdocumentService;


import lombok.extern.slf4j.Slf4j;

import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;

@Controller
@Slf4j
@RequestMapping("/api")
public class DocumentsController {
	
	@Autowired
	 private PDFdocumentService documentService;
	
	 List<String> extractedPages = new ArrayList<>();
	 
	 @GetMapping("/PDFTextExtract")
	    public String index() {	
	        return "documentUpload";
//		 return "loginPage";
	    }
	 
	 
//	 @GetMapping("/login")
//	 public String login() {
//		 return "loginPage";
//	 }
	 
	 
	 @PostMapping("/msDocument")
	 public String msDocument( @RequestParam("file") MultipartFile file,   Model model, HttpSession session) { 
	
		 System.out.println("msDocument");
		 
		 String fileExtension = null;
		 String fileType = file.getContentType();
		int file_size= (int) file.getSize();
		 System.out.println("fileType=="+fileType);
		 System.out.println("file=="+file);
		 System.out.println("file_size=="+file_size);
		 
		 String originalFilename = file.getOriginalFilename();
		 model.addAttribute("file_size", file_size);	
		if (originalFilename != null && originalFilename.contains(".")) {		
		    fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
		    System.out.println("File Extension: " + fileExtension);
		} else {
		    System.out.println("File has no extension");
		}
		 
		 System.out.println("fileType=="+fileType);		 		 
		 session.setAttribute("fileType", fileType);
		 model.addAttribute("̥", fileType.toString());	
		 model.addAttribute("̥", fileExtension);
		 			 
			 try {
				 if(fileType.equals("application/pdf")) {
					 StringBuilder extractedText = new StringBuilder();
					 List<String> extractedPages = new ArrayList<>();
					 
					 PDDocument document= PDDocument.load(file.getInputStream());
					 int totalPages = document.getNumberOfPages();
					 PDFTextStripper pdfStripper = new PDFTextStripper();
					 int TotalPages = document.getNumberOfPages();
					 
					 for(int page= 1; page <= TotalPages; page++) {
						 pdfStripper.setStartPage(page);
						 pdfStripper.setEndPage(page);
						 
						 String pageText = pdfStripper.getText(document);
						 extractedText.append("Page No:").append(page).append("\n");
						 extractedText.append(pageText).append("\n\n\n");
						 extractedPages.add(pageText);
					 }
					 System.out.println("totalPages==="+totalPages);
					 model.addAttribute("extractedText", extractedText);
					 model.addAttribute("totalPages", totalPages);
					 
					 session.setAttribute("extractedText", extractedText);	
					 session.setAttribute("extractedPages", extractedPages);
					 document.close();
					 
//				 }else if(fileType.equals("application/msword") || fileType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) {
				 }else if( fileType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) {
					 StringBuilder extractedText = new StringBuilder();
					 List<String> extractedPages = new ArrayList<>();
					 
					 if (file.getOriginalFilename().endsWith(".doc")) {					 
						 try (InputStream is = file.getInputStream()) {
					            HWPFDocument document = new HWPFDocument(is);
					            Range range = document.getRange();

					            int pageNumber = 1;
					            StringBuilder pageText = new StringBuilder();

					            for (int i = 0; i < range.numParagraphs(); i++) {
					                String paragraphText = range.getParagraph(i).text();
					               					                
					                if (!paragraphText.isEmpty()) {
					                    pageText.append(paragraphText).append("\n");					                   
					                }
					                if (paragraphText.isEmpty() && pageText.length() > 0) {
					                    extractedText.append("Page No: " + pageNumber + "\n" + pageText.toString() + "\n\n");
					                    pageNumber++;
					                    pageText.setLength(0);
					                }
					            }
					            if (pageText.length() > 0) {
					            	extractedPages.add("Page No: " + pageNumber + "\n" + pageText.toString() + "\n\n");
					            	System.out.println("extractedPages=="+extractedPages);
					            }
					            
					        } catch (Exception e) {
					            e.printStackTrace();
					        }
						 
//				            extractedText = documentService.extractTextFromDoc(file);
				            System.out.println("extractedText=="+extractedText);
				            model.addAttribute("extractedText", extractedText);				           
				            session.setAttribute("extractedText", extractedText);	
				            
				        } else if (file.getOriginalFilename().endsWith(".docx")) {	
				        	 int pageNumber = 1;
				        	try (InputStream is = file.getInputStream()) {
					            XWPFDocument document = new XWPFDocument(is);
					            List<XWPFParagraph> paragraphs = document.getParagraphs();
					           
					            StringBuilder pageText = new StringBuilder();
					           					            
					            for (XWPFParagraph paragraph : paragraphs) {
					                String paragraphText = paragraph.getText();
					                if (!paragraphText.isEmpty()) {
					                    pageText.append(paragraphText).append("\n");
					                }
					                if (paragraphText.isEmpty() && pageText.length() > 0) {
					                    extractedText.append("Page No: " + pageNumber + "\n" + pageText.toString()+"\n\n");
					                    pageNumber++;
					                    pageText.setLength(0);
					                }
					            }
					            if (pageText.length() > 0) {
					                extractedText.append("Page No: " + pageNumber + "\n" + pageText.toString()+"\n\n");
					            }
					            System.out.println("pageNumber===="+pageNumber);
					        } catch (Exception e) {
					            e.printStackTrace();
					        }
				        	
//				            extractedText = documentService.extractTextFromDocx(file);
				            System.out.println("extractedText=="+extractedText);
				            model.addAttribute("extractedText", extractedText);
				            model.addAttribute("totalPages", pageNumber);
				            session.setAttribute("extractedText", extractedText);					      
				        }

//				 }else if(fileType.equals("application/vnd.ms-powerpoint") || fileType.equals("application/vnd.openxmlformats-officedocument.presentationml.presentation")){
				 }else if( fileType.equals("application/vnd.openxmlformats-officedocument.presentationml.presentation")){
					 int totalslide = 0;
					 StringBuilder extractedText = new StringBuilder();
					 List<String> extractedPages = new ArrayList<>();
					 
					 try (InputStream is = file.getInputStream()) {
			    	        XMLSlideShow ppt = new XMLSlideShow(is);
			    	        List<XSLFSlide> slides = ppt.getSlides();
			    	         totalslide = slides.size();		    	     
			                System.out.println("Total Slides=========: " + slides.size());

			    	        for (int i = 0; i < slides.size(); i++) {
			    	            XSLFSlide slide = slides.get(i);
			    	            String slideText = documentService.extractTextFromSlideXSLFPPTX(slide);
			    	           
			    	            extractedText.append("Slide ").append(i + 1).append("\n");
			    	            extractedText.append(slideText).append("\n\n\n");
			    	            extractedPages.add(slideText);
			    	        }
			    	    } catch (IOException e) {
			    	        e.printStackTrace();
			    	    }
//			    	    System.out.println("extractedText=="+extractedText.toString());
			    	    model.addAttribute("extractedText", extractedText.toString());	
			    	    model.addAttribute("totalPages", totalslide);	
			    	    session.setAttribute("extractedText", extractedText);	
				 }else {
					 System.out.println("out of the condition");
					 int totalPages = 1;
//					 if(!file.isEmpty()) {
					 if (!file.isEmpty() && file.getSize() > 0) {
												
						 StringBuilder extractedText = new StringBuilder();
						 try(InputStream inputStream = file.getInputStream()){							
							 AutoDetectParser parser = new AutoDetectParser();
							 BodyContentHandler handler = new BodyContentHandler(-1);
							 Metadata metadata = new Metadata();
							 parser.parse(inputStream, handler, metadata);
							 extractedText.append(handler.toString());	
							  System.out.println("extractedText=="+extractedText);
							 model.addAttribute("extractedText", extractedText);
							  model.addAttribute("totalPages", totalPages);
							 session.setAttribute("extractedText", extractedText);
							 
						 }catch (Exception e) {
					            e.printStackTrace();
					            model.addAttribute("error", "An error occurred while extracting text.");
					        }
					 } else {
						 System.out.println("something wrong");
						 
					    }
				 }
				 
			 }catch(Exception e) {
				 e.printStackTrace();
			 }			 		 
		return "documentUpload";
//			 return "fileUpload";
//		return "redirect:/api/PDFTextExtract"; 
	 }
	 

	 
	 
	 @PostMapping("/msDocumentdoc")
	 public String msDocumentdoc( @RequestParam("file") MultipartFile file,   Model model, HttpSession session) { 
		 System.out.println("msDocumentdoc");
	
		 String fileExtension = null;
		 String fileType = file.getContentType();
		 System.out.println("fileType=="+fileType);
		 System.out.println("file=="+file);
		 
		 String originalFilename = file.getOriginalFilename();
		
		if (originalFilename != null && originalFilename.contains(".")) {		
		    fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
		    System.out.println("File Extension: " + fileExtension);
		} else {
		    System.out.println("File has no extension");
		}
		 
		 System.out.println("fileType=="+fileType);		 		 
		 session.setAttribute("fileType", fileType);
		 model.addAttribute("fileType", fileType.toString());	
		 model.addAttribute("fileExtension", fileExtension);
		 			 
			 try {
				 if(fileType.equals("application/pdf")) {
					 StringBuilder extractedText = new StringBuilder();
					 List<String> extractedPages = new ArrayList<>();
					 
					 PDDocument document= PDDocument.load(file.getInputStream());
					 int totalPages = document.getNumberOfPages();
					 PDFTextStripper pdfStripper = new PDFTextStripper();
					 int TotalPages = document.getNumberOfPages();
					 
					 for(int page= 1; page <= TotalPages; page++) {
						 pdfStripper.setStartPage(page);
						 pdfStripper.setEndPage(page);
						 
						 String pageText = pdfStripper.getText(document);
						 extractedText.append("Page No:").append(page).append("\n");
						 extractedText.append(pageText).append("\n\n\n");
						 extractedPages.add(pageText);
					 }
					 System.out.println("totalPages==="+totalPages);
					 model.addAttribute("extractedText", extractedText);
					 model.addAttribute("totalPages", totalPages);
					 
					 session.setAttribute("extractedText", extractedText);	
					 session.setAttribute("extractedPages", extractedPages);
					 document.close();
					 
//				 }else if(fileType.equals("application/msword") || fileType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) {
				 }else if( fileType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) {
					 StringBuilder extractedText = new StringBuilder();
					 List<String> extractedPages = new ArrayList<>();
					 
					 if (file.getOriginalFilename().endsWith(".doc")) {					 
						 try (InputStream is = file.getInputStream()) {
					            HWPFDocument document = new HWPFDocument(is);
					            Range range = document.getRange();

					            int pageNumber = 1;
					            StringBuilder pageText = new StringBuilder();

					            for (int i = 0; i < range.numParagraphs(); i++) {
					                String paragraphText = range.getParagraph(i).text();
					               					                
					                if (!paragraphText.isEmpty()) {
					                    pageText.append(paragraphText).append("\n");					                   
					                }
					                if (paragraphText.isEmpty() && pageText.length() > 0) {
					                    extractedText.append("Page No: " + pageNumber + "\n" + pageText.toString() + "\n\n");
					                    pageNumber++;
					                    pageText.setLength(0);
					                }
					            }
					            if (pageText.length() > 0) {
					            	extractedPages.add("Page No: " + pageNumber + "\n" + pageText.toString() + "\n\n");
					            	System.out.println("extractedPages=="+extractedPages);
					            }
					            
					        } catch (Exception e) {
					            e.printStackTrace();
					        }
						 
//				            extractedText = documentService.extractTextFromDoc(file);
				            System.out.println("extractedText=="+extractedText);
				            model.addAttribute("extractedText", extractedText);				           
				            session.setAttribute("extractedText", extractedText);	
				            
				        } else if (file.getOriginalFilename().endsWith(".docx")) {	
				        	 int pageNumber = 1;
				        	try (InputStream is = file.getInputStream()) {
					            XWPFDocument document = new XWPFDocument(is);
					            List<XWPFParagraph> paragraphs = document.getParagraphs();
					           
					            StringBuilder pageText = new StringBuilder();
					           					            
					            for (XWPFParagraph paragraph : paragraphs) {
					                String paragraphText = paragraph.getText();
					                if (!paragraphText.isEmpty()) {
					                    pageText.append(paragraphText).append("\n");
					                }
					                if (paragraphText.isEmpty() && pageText.length() > 0) {
					                    extractedText.append("Page No: " + pageNumber + "\n" + pageText.toString()+"\n\n");
					                    pageNumber++;
					                    pageText.setLength(0);
					                }
					            }
					            if (pageText.length() > 0) {
					                extractedText.append("Page No: " + pageNumber + "\n" + pageText.toString()+"\n\n");
					            }
					            System.out.println("pageNumber===="+pageNumber);
					        } catch (Exception e) {
					            e.printStackTrace();
					        }
				        	
//				            extractedText = documentService.extractTextFromDocx(file);
				            System.out.println("extractedText=="+extractedText);
				            model.addAttribute("extractedText", extractedText);
				            model.addAttribute("totalPages", pageNumber);
				            session.setAttribute("extractedText", extractedText);					      
				        }

//				 }else if(fileType.equals("application/vnd.ms-powerpoint") || fileType.equals("application/vnd.openxmlformats-officedocument.presentationml.presentation")){
				 }else if( fileType.equals("application/vnd.openxmlformats-officedocument.presentationml.presentation")){
					 int totalslide = 0;
					 StringBuilder extractedText = new StringBuilder();
					 List<String> extractedPages = new ArrayList<>();
					 
					 try (InputStream is = file.getInputStream()) {
			    	        XMLSlideShow ppt = new XMLSlideShow(is);
			    	        List<XSLFSlide> slides = ppt.getSlides();
			    	         totalslide = slides.size();		    	     
			                System.out.println("Total Slides=========: " + slides.size());

			    	        for (int i = 0; i < slides.size(); i++) {
			    	            XSLFSlide slide = slides.get(i);
			    	            String slideText = documentService.extractTextFromSlideXSLFPPTX(slide);
			    	           
			    	            extractedText.append("Slide ").append(i + 1).append("\n");
			    	            extractedText.append(slideText).append("\n\n\n");
			    	            extractedPages.add(slideText);
			    	        }
			    	    } catch (IOException e) {
			    	        e.printStackTrace();
			    	    }
//			    	    System.out.println("extractedText=="+extractedText.toString());
			    	    model.addAttribute("extractedText", extractedText.toString());	
			    	    model.addAttribute("totalPages", totalslide);	
			    	    session.setAttribute("extractedText", extractedText);	
				 }else {
					 System.out.println("out of the condition");
					 int totalPages = 1;
//					 if(!file.isEmpty()) {
					 if (!file.isEmpty() && file.getSize() > 0) {
											
						 StringBuilder extractedText = new StringBuilder();
						 try(InputStream inputStream = file.getInputStream()){
							 System.out.println("inside file..................................22222222222222222........................");
							 AutoDetectParser parser = new AutoDetectParser();
							 BodyContentHandler handler = new BodyContentHandler(-1);
							 Metadata metadata = new Metadata();
							 parser.parse(inputStream, handler, metadata);
							 System.out.println("inside file..................................33333333333333333........................");
							 extractedText.append(handler.toString());	
							 System.out.println("inside file..................................444444444444444444........................");
							  System.out.println("extractedText=="+extractedText);
							 model.addAttribute("extractedText", extractedText);
							  model.addAttribute("totalPages", totalPages);
							 session.setAttribute("extractedText", extractedText);
							 
						 }catch (Exception e) {
					            e.printStackTrace();
					            model.addAttribute("error", "An error occurred while extracting text.");
					        }
					 } else {
						 System.out.println("something wrong");
						 
					    }
				 }
				 
			 }catch(Exception e) {
				 e.printStackTrace();
			 }			 		 
		return "documentUploaddoc";

	 }
	 
	 
	 
	 
	 
	 @PostMapping("/msDocument1")
	    @ResponseBody
	    public String saveDefaultEntryDetail(@RequestParam("file") MultipartFile file, HttpServletRequest request, Model map) {
	        String response = "error";
	        System.out.println("inside response controller");

	        try {
	            if (file != null && !file.isEmpty()) {
	            	System.out.println("file successfullyb upload...............");
	                response = "success";
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	            System.out.println("file are not upload...................");
	        }

	        return response;
	    }
	
	 
	 
	 
	 @GetMapping("/downloadAll")
	 public ResponseEntity<byte[]> downloadAll(HttpSession session, @RequestParam(value = "fileType", required = false) String fileType) {	  
		
	     try {	        
	         StringBuilder extractedText = (StringBuilder) session.getAttribute("extractedText");
	              
	         if (extractedText == null) {
	             return ResponseEntity.badRequest().body(new byte[0]);
	         }	     
	         File tempDir = FileUtils.getTempDirectory();
	         File tempFile = new File(tempDir, "extractedText.zip");

	         try (ZipArchiveOutputStream zipOutputStream = new ZipArchiveOutputStream(new FileOutputStream(tempFile))) {
	        	 System.out.println("zipOutputStream started.. ..............");
	             byte[] content = extractedText.toString().getBytes();
	             String entryName = "extractedText." + (fileType != null ? fileType : "txt");

	             ZipArchiveEntry zipEntry = new ZipArchiveEntry(entryName);
	             zipOutputStream.putArchiveEntry(zipEntry);
	             zipOutputStream.write(content);
	             zipOutputStream.closeArchiveEntry();
	        	 System.out.println("zipOutputStream end.. ..............");

	         }
	         HttpHeaders headers = new HttpHeaders();
	         headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
	         headers.setContentDispositionFormData("attachment", "extractedText.zip");

	         return ResponseEntity.ok().headers(headers).body(FileUtils.readFileToByteArray(tempFile));
	     } catch (IOException e) {
	         e.printStackTrace();
	         return ResponseEntity.badRequest().body(new byte[0]);
	     }
	 }
	 
	 
	 
	 
	 @GetMapping("/downloadPage")
	 public ResponseEntity<byte[]> downloadPage(
	     @RequestParam(value = "pageNo", required = false) Integer  pageNo, HttpSession session) {
		 
	     StringBuilder extractedPages = (StringBuilder) session.getAttribute("extractedText");
	     System.out.println("pageNo=="+pageNo);

	     if (extractedPages == null || pageNo < 1) {
	         return ResponseEntity.badRequest().body(new byte[0]);
	     }
	     String[] pagesArray = extractedPages.toString().split("\n\n\n");

	     if (pageNo > pagesArray.length || pageNo < 1) {
	         return ResponseEntity.badRequest().body(new byte[0]);
	     }
	     String pageText = pagesArray[pageNo - 1]; 
	     byte[] content = pageText.getBytes();

	     HttpHeaders headers = new HttpHeaders();
	     headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
	     headers.setContentDispositionFormData("attachment", "page_" + pageNo + ".txt");

	     return ResponseEntity.ok().headers(headers).body(content);
	 }
	 
	 
	 
	 @GetMapping("/downloadAllSeperatePages")
	 @ResponseBody
	 public ResponseEntity<byte[]> downloadAllSeperatePages(HttpSession session, Model model ) {
	     try {	    	
	         StringBuilder extractedPages = (StringBuilder) session.getAttribute("extractedText");
	         System.out.println("extractedPages==="+extractedPages);

	         String[] pagesArray = extractedPages.toString().split("\n\n\n");
	         ByteArrayOutputStream baos = new ByteArrayOutputStream();
	         
	         try (ZipOutputStream zipOut = new ZipOutputStream(baos)) {
	             for (int i = 0; i < pagesArray.length; i++) {
	                 String pageText = pagesArray[i];
	                 ZipEntry zipEntry = new ZipEntry("page_" + (i + 1) + ".txt");
	                 zipOut.putNextEntry(zipEntry);
	                 zipOut.write(pageText.getBytes());
	                 zipOut.closeEntry();
	             }
	         }
	         HttpHeaders headers = new HttpHeaders();
	         headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
	         headers.setContentDispositionFormData("attachment", "all_pages.zip");

	         return ResponseEntity.ok().headers(headers).body(baos.toByteArray());
	     } catch (IOException e) {
	         e.printStackTrace();
	         return ResponseEntity.badRequest().body(new byte[0]);
	     }
	 }
	 	 
}


       