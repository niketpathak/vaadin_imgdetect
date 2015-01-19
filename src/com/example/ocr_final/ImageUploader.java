package com.example.ocr_final;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;

class ImageUploader implements Receiver, SucceededListener {
    public File file;
    
    public OutputStream receiveUpload(String filename,
                                      String mimeType) {
        // Create upload stream
        FileOutputStream fos = null; // Stream to write to
        try {
            // Open the file for writing.
            file = new File("/tmp/uploads/" + filename);
            fos = new FileOutputStream(file);
        } catch (final java.io.FileNotFoundException e) {
            new Notification("Could not open file<br/>",
                             e.getMessage(),
                             Notification.Type.ERROR_MESSAGE)
                .show(Page.getCurrent());
            return null;
        }
        return fos; // Return the output stream to write to
    }

    public void uploadSucceeded(SucceededEvent event) {
        // Show the uploaded file in the image viewer
//        image.setVisible(true);
//        image.setSource(new FileResource(file));
    }
};




















//package com.example.ocr_final;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.OutputStream;
//
//import javax.servlet.annotation.WebServlet;
//
//import com.vaadin.annotations.Theme;
//import com.vaadin.annotations.VaadinServletConfiguration;
//import com.vaadin.server.FileResource;
//import com.vaadin.server.Page;
//import com.vaadin.server.Sizeable;
//import com.vaadin.server.VaadinRequest;
//import com.vaadin.server.VaadinServlet;
//import com.vaadin.ui.Button;
//import com.vaadin.ui.Button.ClickEvent;
//import com.vaadin.ui.Embedded;
//import com.vaadin.ui.HorizontalLayout;
//import com.vaadin.ui.HorizontalSplitPanel;
//import com.vaadin.ui.Label;
//import com.vaadin.ui.Layout;
//import com.vaadin.ui.Notification;
//import com.vaadin.ui.Panel;
//import com.vaadin.ui.UI;
//import com.vaadin.ui.Upload;
//import com.vaadin.ui.Upload.Receiver;
//import com.vaadin.ui.Upload.SucceededEvent;
//import com.vaadin.ui.Upload.SucceededListener;
//import com.vaadin.ui.VerticalLayout;
//import com.vaadin.ui.VerticalSplitPanel;
//
//@SuppressWarnings("serial")
//@Theme("ocr_final")
//public class Ocr_UI extends UI implements Receiver, SucceededListener {
//
//	@WebServlet(value = "/*", asyncSupported = true)
//	@VaadinServletConfiguration(productionMode = false, ui = Ocr_UI.class)
//	
//	public static class Servlet extends VaadinServlet {
//	}
//
//	public File file;
//	final Embedded image = new Embedded("Uploaded Image");
//	private Button Rotate_Button = new Button("Rotate Image");
//	private String ori_filename;
//	
//	@Override
//	protected void init(VaadinRequest request) {
//		
//		final VerticalLayout layout = new VerticalLayout();
//		layout.setMargin(true);
//		setContent(layout);
//		layout.addComponent(new Label("The Ultimate Identity Card OCR System"));
//		
//		HorizontalSplitPanel hsplit = new HorizontalSplitPanel();
//		layout.addComponent(hsplit);
//		
////		Upload upload_image = new Upload("Upload Identity Card Image",this);
////		layout.addComponent(upload_image);
//		
//		Upload upload_image = new Upload("Upload Identity Card Image",this);
////		upload_image.setButtonCaption("Start Upload");
//		upload_image.addSucceededListener(this);
//		
//		//******add components to layout
////        layout.addComponent(upload_image);
////        layout.addComponent(Rotate_Button);
//		hsplit.addComponent(upload_image);
//		hsplit.addComponent(Rotate_Button);
//        
//		image.setVisible(false);		//set visibility of following to false
//		Rotate_Button.setVisible(false);
//		
//        image.setWidth("70%");
//		layout.addComponent(image);
//		
//		// Layout inside layout
////        HorizontalLayout hor = new HorizontalLayout();
////        hor.setSizeFull();
////		layout.addComponent(hor);
////		layout.setExpandRatio(hor, 1); // Expand to fill
//		
//	}
//
//	@Override
//	public OutputStream receiveUpload(String filename, String mimeType) {
//		// TODO Auto-generated method stub
//		FileOutputStream output = null;
//		try{
//			file = new File("/Users/niketpathak/Documents/ISEP_sem2/IMG_processing/Research/img2text_test/" + filename);
//			output = new FileOutputStream(file);
//			ori_filename = filename;
//		}
//		catch(final java.io.FileNotFoundException e){
//			new Notification("Could not open file<br/>",
//                    e.getMessage(),
//                    Notification.Type.ERROR_MESSAGE)
//				.show(Page.getCurrent());
//		   return null;
//		}
//		return output;
//		
//	}
//
//	@Override
//	public void uploadSucceeded(SucceededEvent event) {
//		// TODO Auto-generated method stub
//        image.setSource(new FileResource(file));
//        image.setVisible(true);
//        Rotate_Button.setVisible(true);
//	}
//	
//	
//	
//	
//	
//
//}