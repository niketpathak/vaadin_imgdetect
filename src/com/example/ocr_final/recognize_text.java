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
		Process pr_t = runtime.exec("tesseract "+filepath+"grayscaled.jpg "+filepath+"niket1 -l eng+fra -psm 1");
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
        String[] split_con= text_cnt.split(" ");
        try{
        String process_fn =  split_con[split_con.length-2];	
        String process_ln =  split_con[split_con.length-1];
//        System.out.println(Arrays.toString(split_con));
        System.out.println("FName_p->"+process_fn+" LName_p->"+process_ln);
        int br_pt=process_fn.indexOf("<")+1;
        String[] obt = process_fn.split("");					//split it into characters
      StringBuffer result_buffer = new StringBuffer();
      for (int i = 6; i < br_pt; i++) {						
      	result_buffer.append(obt[i]);					//append the char viz fname!
      }
      String fname = result_buffer.toString();
     obt=process_ln.split("");
     result_buffer=new StringBuffer();
     for (int i = 0; i < 13; i++) {						
       	result_buffer.append(obt[i]);					//append the char viz ID!
       }
     String id_f = result_buffer.toString();
     result_buffer=new StringBuffer();
     for (int i = 14; i < process_ln.length()-8; i++) {						
        	result_buffer.append(obt[i]);					//append the char viz lname with <<!
        }
     String lname = result_buffer.toString();
     lname = lname.replaceAll("<<|<", " ");					//remove </<<+
     System.out.println("f_name->"+fname+". l_name->"+lname+". ID->"+id_f);
      	data[0]=lname;data[1]=fname;data[2]=id_f;
        }
        catch (Exception e){
        	e.printStackTrace();
        }
        return data;
	}
	
	public void rotate() throws IOException, InterruptedException{
//		 Process p3 = rt.exec("/Program Files/ImageMagick-6.3.9-Q16/mogrify -rotate 90 " + imageName);
		Process rotate = runtime.exec("mogrify -rotate 90 "+filepath+filename);
    	int exitVal_gr = rotate.waitFor();
    	 BufferedReader input_rot = new BufferedReader(new InputStreamReader(rotate.getInputStream()));
         BufferedReader error_if_any_gr = new BufferedReader(new InputStreamReader(rotate.getErrorStream()));
         String line=null;String err =null;
         while((line=input_rot.readLine()) != null) {
             System.out.println("output_Rotate->"+line);
         	}
         while ((err = error_if_any_gr.readLine()) != null) {
             System.out.println("This is Err_gr Code->"+err);
         }
    	System.out.println("Rotate Exit error code "+exitVal_gr);
	}
}
