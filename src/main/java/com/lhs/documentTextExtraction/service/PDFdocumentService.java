package com.lhs.documentTextExtraction.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextParagraph;
import org.apache.poi.xslf.usermodel.XSLFTextRun;
import org.apache.poi.xslf.usermodel.XSLFTextShape;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.lhs.documentTextExtraction.entity.UserMast;
import com.lhs.documentTextExtraction.repo.UserMastRepo;

@Service
public class PDFdocumentService {
 
	@Autowired
	public UserMastRepo repo;
	
	
	
	 public StringBuilder extractTextFromDoc(MultipartFile file) {
		 StringBuilder extractedText = new StringBuilder();
		 List<String> extractedPages = new ArrayList<>();

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
	        return extractedText;
	    }
	 
	 	 
	 
	 
	  public StringBuilder extractTextFromDocx(MultipartFile file) {
		  StringBuilder extractedText = new StringBuilder();

	        try (InputStream is = file.getInputStream()) {
	            XWPFDocument document = new XWPFDocument(is);
	            List<XWPFParagraph> paragraphs = document.getParagraphs();

	            int pageNumber = 1;
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
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return extractedText;
	    }
	  
	  
	  
	  	  
	  public String extractTextFromSlideXSLFPPTX(XSLFSlide slide) {
		    StringBuilder slideText = new StringBuilder();

		    for (XSLFShape shape : slide.getShapes()) {
		        if (shape instanceof XSLFTextShape) {
		            XSLFTextShape textShape = (XSLFTextShape) shape;
		            for (XSLFTextParagraph paragraph : textShape) {
		                for (XSLFTextRun run : paragraph) {
		                    slideText.append(run.getRawText()).append(" ");
		                }
		            }
		        }
		    }

		    return slideText.toString();
		}
		    
		
}