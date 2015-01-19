package com.example.ocr_final;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.server.Sizeable;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;

@SuppressWarnings("serial")
@Theme("ocr_final")
public class Ocr_UI extends UI implements Receiver, SucceededListener {

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = Ocr_UI.class)
	
	public static class Servlet extends VaadinServlet {
		
	}

	public File file;
	final Embedded image = new Embedded("Uploaded Image");
	private Button Rotate_Button = new Button("Rotate Image");
	private String ori_filename;
	private String filepath = "/Users/niketpathak/Documents/ISEP_sem2/IMG_processing/Research/img2text_test/";
	private Label res = new Label("Result");
	
	@Override
	protected void init(VaadinRequest request) {
		
		final VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		setContent(layout);
		layout.addComponent(new Label("The Ultimate Identity Card OCR System"));
		
		HorizontalSplitPanel hsplit = new HorizontalSplitPanel();
		layout.addComponent(hsplit);

		
		Upload upload_image = new Upload("Upload Identity Card Image",this);

		upload_image.addSucceededListener(this);

		hsplit.addComponent(upload_image);
		hsplit.addComponent(Rotate_Button);
		layout.addComponent(res);
        
		image.setVisible(false);		//set visibility of following to false
		Rotate_Button.setVisible(false);
		res.setVisible(false);
        image.setWidth("70%");
		layout.addComponent(image);
		
		Rotate_Button.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
//				event.getButton().setCaption("Rotate button clicked");
				
			}
		});

	}

	@Override
	public OutputStream receiveUpload(String filename, String mimeType) {
		// TODO Auto-generated method stub
		FileOutputStream output = null;
		try{
			file = new File(filepath + filename);
			output = new FileOutputStream(file);
			ori_filename = filename;
		}
		catch(final java.io.FileNotFoundException e){
			new Notification("Could not open file<br/>",
                    e.getMessage(),
                    Notification.Type.ERROR_MESSAGE)
				.show(Page.getCurrent());
		   return null;
		}
		return output;
		
	}

	@Override
	public void uploadSucceeded(SucceededEvent event) {
		// TODO Auto-generated method stub
        image.setSource(new FileResource(file));
        image.setVisible(true);
        recognize_text obj = new recognize_text(filepath,ori_filename);
		String[] final_text=obj.recognize_textcontent();
		System.out.println("This is on my UI->"+final_text[0]);
        Rotate_Button.setVisible(true);
        if(final_text[0]!="undefined"){
        res.setValue("Last Name: "+final_text[0]+". First Name: "+final_text[1]+". ID: "+final_text[2]);
        }
        else{
        	res.setValue("Unable to Detect Text");
        }
        res.setVisible(true);
	}
	
	
	
	
	

}