package com.lhs.documentTextExtraction.utility;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.List;
import java.util.Locale;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

@Component
public class Utility {

	public String getBaseName(String fileName) {
		int index = fileName.lastIndexOf('.');
		if (index == -1) {
			return fileName;
		} else {
			return fileName.substring(0, index);
		}
	}
	
	public boolean isnull(String comparion_value) {
		boolean null_value = true;
		try {
			if (comparion_value != null && !"".equals(comparion_value) && !"null".equalsIgnoreCase(comparion_value) && comparion_value.length() > 0) {
				null_value = false;
			}
		} catch (NullPointerException npe) {
			null_value = true;
		} catch (Exception ex) {
			null_value = true;
		}
		return null_value;
	}//End Method
	
	public static boolean isnullcap(String comparion_value) {
		comparion_value = comparion_value.toUpperCase(Locale.ENGLISH);
		boolean null_value = true;
		try {
			if (comparion_value != null && !"".equals(comparion_value) && !"null".equalsIgnoreCase(comparion_value) && comparion_value.length() > 0) {
				null_value = false;
			}
		} catch (NullPointerException npe) {
			null_value = true;
		} catch (Exception ex) {
			null_value = true;
		}
		return null_value;
	}//End Method

	public Resource getResources(String fileNamePath, HttpServletResponse response, String filename) {
		Resource resource = null;
		try {
			File file = null;
			if (fileNamePath != null) {
				file = new File(fileNamePath);
				String mimeType = URLConnection.guessContentTypeFromName(file.getName());
				if (mimeType == null) {
					System.out.println("mimetype is not detectable, will take default");
					mimeType = "application/octet-stream";
				}
				response.setContentType(mimeType);
				response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
				response.setHeader("filename", filename);
				response.setContentLength((int) file.length());
				InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
				try {
					FileCopyUtils.copy(inputStream, response.getOutputStream());
				} catch (Exception e) {
				}
				resource = new FileSystemResource(fileNamePath);
				System.out.println("mimetype : " + mimeType);
				System.out.println("file.getName() : " + file.getName());
				System.out.println("path : " + fileNamePath);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resource;
	}//End Method


	
	public static byte[] decompressZLib(byte[] data) {
		Inflater inflater = new Inflater();
		inflater.setInput(data);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
		byte[] buffer = new byte[1024];
		try {
			while (!inflater.finished()) {
				int count = inflater.inflate(buffer);
				outputStream.write(buffer, 0, count);
			}
			outputStream.close();
		} catch (IOException ioe) {
		} catch (DataFormatException e) {
		}
		return outputStream.toByteArray();
	}
	
	public byte[] zipFiles(List<String> files) {
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream baos = null;
        try {
            int count = 0;
            baos = new ByteArrayOutputStream();

            ZipOutputStream zos = new ZipOutputStream(baos);
            for (String file : files) {
                if (!isnull(file)) {
                    File f1 = new File(file);
                    if (f1.exists() && f1.isFile()) {
                        count++;
                        ZipEntry ze = new ZipEntry(count + "_" + f1.getName());
                        zos.putNextEntry(ze);
                        System.out.println("FilePdf:"+file);
                        FileInputStream in = new FileInputStream(file);
                        int len;
                        while ((len = in.read(buffer)) > 0) {
                            zos.write(buffer, 0, len);
                        }
                        in.close();
                    }
                }
            }
         
            zos.closeEntry();
            zos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        System.out.println("Size:"+baos.size());
        return (baos != null) ? baos.toByteArray() : null;
    }


	 
	 public static byte[] compressZLib(byte[] data) {
			Deflater deflater = new Deflater();
			deflater.setInput(data);
			deflater.finish();

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
			byte[] buffer = new byte[1024];
			while (!deflater.finished()) {
				int count = deflater.deflate(buffer);
				outputStream.write(buffer, 0, count);
			}
			try {
				outputStream.close();
			} catch (IOException e) {
			}
			System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);

			return outputStream.toByteArray();
		}
	 
	 

}