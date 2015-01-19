package com.example.ocr_final;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class recognize_text {
	private String filename;
	private String filepath;
	Runtime runtime = Runtime.getRuntime();
	
	public recognize_text(String filepath_t, String filename_ori){
		filename = filename_ori;
		filepath=filepath_t;
	}
	
	public String[] recognize_textcontent(){
		
//		 System.out.println("Filepath->"+filepath+" and Filename -> "+filename_ori);
		 Boolean filetype_accepted=false;
		 String[] final_data={};
	        try {
	        	Process pr_i = runtime.exec("identify "+filepath+filename);

	            BufferedReader input = new BufferedReader(new InputStreamReader(pr_i.getInputStream()));
	            BufferedReader error_if_any = new BufferedReader(new InputStreamReader(pr_i.getErrorStream()));
	            String line=null;
	            String err =null;
	            while((line=input.readLine()) != null) {
	                System.out.println("Identify output->"+line);
//	                String[] expected_ext = {"JPEG","PNG","TIFF"};
	                Pattern p = Pattern.compile("(JPEG|TIFF|PNG)");		//using regex here -> http://www.tutorialspoint.com/java/java_regular_expressions.htm
	                Matcher m = p.matcher(line);
	                filetype_accepted = m.find();
	            }
	            while ((err = error_if_any.readLine()) != null) {
	                System.out.println("This is Err_i Code->"+err);
	            }
	            int exitVal_i = pr_i.waitFor();
	            System.out.println("Identify Exit error code "+exitVal_i);
	            if(filetype_accepted && exitVal_i==0){
	            	this.make_grayscale();
	            	this.tesseract_detection();
	            } else {
	            	System.out.println("Unsupported File Type");
	            }
	            
	            final_data=this.processtext();
//	            System.out.println(final_data[0]+ final_data[1] + final_data[2]);		//data obtained here!
//	          return "Name: "+first_name[0]+" "+last_name[0]+". ID: "+identity_no+ ".";
	            
	        } catch(Exception e) {
	            System.out.println(e.toString());
	            e.printStackTrace();
	            final_data[0]="undefined";	//set lname to undefined
	        }
	        return final_data;
	        
	}
		
	public void make_grayscale() throws IOException, InterruptedException{
		Process pr_grayscale = runtime.exec("convert "+filepath+filename+" -set colorspace Gray -separate -average "+filepath+"grayscaled.jpg");
    	int exitVal_gr = pr_grayscale.waitFor();
    	 BufferedReader input_gr = new BufferedReader(new InputStreamReader(pr_grayscale.getInputStream()));
         BufferedReader error_if_any_gr = new BufferedReader(new InputStreamReader(pr_grayscale.getErrorStream()));
         String line=null;String err =null;
         while((line=input_gr.readLine()) != null) {
             System.out.println("output_grayscale->"+line);
         	}
         while ((err = error_if_any_gr.readLine()) != null) {
             System.out.println("This is Err_gr Code->"+err);
         }
    	System.out.println("Grayscale Exit error code "+exitVal_gr);
	}
	
	public void tesseract_detection() throws IOException, InterruptedException{
		Process pr_t = runtime.exec("tesseract "+filepath+"grayscaled.jpg "+filepath+"niket1 -l eng+fra");
        BufferedReader input_t = new BufferedReader(new InputStreamReader(pr_t.getInputStream()));
        BufferedReader error_if_any_t = new BufferedReader(new InputStreamReader(pr_t.getErrorStream()));
        String line=null;String err =null;
        while((line=input_t.readLine()) != null) {
            System.out.println("output_tesseract->"+line);
        	}
        while ((err = error_if_any_t.readLine()) != null) {
            System.out.println("This is Err_t Code->"+err);
        }
        int exitVal_t = pr_t.waitFor();
        System.out.println("Tesseract Exit error code "+exitVal_t);
	}
	
	public String[] processtext() throws IOException{
		String text_cnt = "";String line="";String[] data= {"undefined","undefined","0"};	// lname, fname, ID number
		String file_path_ts = filepath+"niket1.txt";
        BufferedReader file_readd;
		try {
			file_readd = new BufferedReader(new InputStreamReader(new FileInputStream(file_path_ts)));
			  while((line=file_readd.readLine()) != null) {
		         	 line=line.replaceAll("\\s+","");		//eliminate white-spaces
		         	text_cnt = text_cnt +" "+ line;
		        	}
			  file_readd.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        System.out.println("FILE_CONTENT->"+text_cnt);
        try{
        	if(text_cnt!=""){
        int indexfound = text_cnt.toUpperCase().indexOf("Nom".toUpperCase()); //indexfound returns index of 'N'
        String[] all_colin_sep = text_cnt.substring(indexfound+4).split(":");
        String[] last_name = all_colin_sep[0].split(" ");	//get last name
        String[] first_name = all_colin_sep[1].split(" ");	//get first name
        String[] dob = all_colin_sep[3].split(" ");			//get DOB
        String ret_id_pr = dob[dob.length-1];				//get the last element in array
        String[] obt = ret_id_pr.split("");					//split it into characters
        StringBuffer result_buffer = new StringBuffer();
        for (int i = 0; i < 13; i++) {						
        	result_buffer.append( obt[i] );					//append the first 15-char viz ID!
        }
        String identity_no = result_buffer.toString();

        System.out.println("Last Name is ->"+last_name[0]+". FirstName is ->"+first_name[0]+ ". DOB->"+dob[0]+ ". Final_ID No.->"+identity_no+ ". Process_string->"+ret_id_pr);
        	data[0]=last_name[0];data[1]=first_name[0];data[2]=identity_no;
 
        	}
        	else{
        					//empty
        		
        	}
        }
        catch (Exception e){
        	e.printStackTrace();
        }
        return data;
	}
	
	public void rotate() throws IOException, InterruptedException{
//		 Process p3 = rt.exec("/Program Files/ImageMagick-6.3.9-Q16/mogrify -rotate 90 " + imageName);
		Process rotate = runtime.exec("convert "+filepath+filename+" -set colorspace Gray -separate -average "+filepath+"grayscaled.jpg");
    	int exitVal_gr = rotate.waitFor();
    	 BufferedReader input_gr = new BufferedReader(new InputStreamReader(rotate.getInputStream()));
         BufferedReader error_if_any_gr = new BufferedReader(new InputStreamReader(rotate.getErrorStream()));
         String line=null;String err =null;
         while((line=input_gr.readLine()) != null) {
             System.out.println("output_grayscale->"+line);
         	}
         while ((err = error_if_any_gr.readLine()) != null) {
             System.out.println("This is Err_gr Code->"+err);
         }
    	System.out.println("Grayscale Exit error code "+exitVal_gr);
	}
}
