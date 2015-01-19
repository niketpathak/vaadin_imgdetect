package com.example.ocr_final;
import java.io.*;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class launch_process {

	public static void main(String args[]) {
		 Boolean filetype_accepted=false;
        try {

        	Runtime runtime = Runtime.getRuntime();
        	Process pr_i = runtime.exec("identify /Users/niketpathak/Documents/ISEP_sem2/IMG_processing/Research/img2text_test/PI5555.jpg");
//        	Process pr_i = runtime.exec("identify /Users/niketpathak/Documents/logo-gkm.png");

            BufferedReader input = new BufferedReader(new InputStreamReader(pr_i.getInputStream()));
            BufferedReader error_if_any = new BufferedReader(new InputStreamReader(pr_i.getErrorStream()));
            String line=null;
            String err =null;
            while((line=input.readLine()) != null) {
                System.out.println("Identify output->"+line);
//                String[] expected_ext = {"JPEG","PNG","TIFF"};
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
            	Process pr_grayscale = runtime.exec("convert /Users/niketpathak/Documents/ISEP_sem2/IMG_processing/Research/img2text_test/PI5555.jpg -set colorspace Gray -separate -average /Users/niketpathak/Documents/ISEP_sem2/IMG_processing/Research/img2text_test/PI5555_grscl.jpg");
            	int exitVal_gr = pr_grayscale.waitFor();
            	 BufferedReader input_gr = new BufferedReader(new InputStreamReader(pr_grayscale.getInputStream()));
                 BufferedReader error_if_any_gr = new BufferedReader(new InputStreamReader(pr_grayscale.getErrorStream()));
                 line=null;err =null;
                 while((line=input_gr.readLine()) != null) {
                     System.out.println("output_grayscale->"+line);
                 	}
                 while ((err = error_if_any_gr.readLine()) != null) {
                     System.out.println("This is Err_gr Code->"+err);
                 }
            	System.out.println("Grayscale Exit error code "+exitVal_gr);
                Process pr_t = runtime.exec("tesseract /Users/niketpathak/Documents/ISEP_sem2/IMG_processing/Research/img2text_test/PI5555_grscl.jpg /Users/niketpathak/Documents/ISEP_sem2/IMG_processing/Research/img2text_test/niket1 -l eng+fra");
                BufferedReader input_t = new BufferedReader(new InputStreamReader(pr_t.getInputStream()));
                BufferedReader error_if_any_t = new BufferedReader(new InputStreamReader(pr_t.getErrorStream()));
                line=null;err =null;
                while((line=input_t.readLine()) != null) {
                    System.out.println("output_tesseract->"+line);
                	}
                while ((err = error_if_any_t.readLine()) != null) {
                    System.out.println("This is Err_t Code->"+err);
                }
                int exitVal_t = pr_t.waitFor();
                System.out.println("Tesseract Exit error code "+exitVal_t);
            
            } else {
            	System.out.println("Unsupported File Type");
            }
            String file_path_ts = "/Users/niketpathak/Documents/ISEP_sem2/IMG_processing/Research/img2text_test/niket1.txt";
            BufferedReader file_readd = new BufferedReader(new InputStreamReader(new FileInputStream(file_path_ts)));
            int linecount=0;
//            String[] file_cnt_arry = new String[30];
            String text_cnt = "";
            while((line=file_readd.readLine()) != null) {
             	 line=line.replaceAll("\\s+","");		//eliminate white-spaces
             	text_cnt = text_cnt +" "+ line;
//            	     file_cnt_arry[linecount]=line;
            	     linecount++;
            	}
            System.out.println("FILE_CONTENT->"+text_cnt);
            int indexfound = text_cnt.toUpperCase().indexOf("Nom".toUpperCase()); //indexfound returns index of 'N'
            String[] all_colin_sep = text_cnt.substring(indexfound+4).split(":");
            String[] last_name = all_colin_sep[0].split(" ");	//get last name
            String[] first_name = all_colin_sep[1].split(" ");	//get first name
            String[] dob = all_colin_sep[3].split(" ");			//get DOB
            String ret_id_pr = dob[dob.length-1];				//get the last element in array
            String[] obt = ret_id_pr.split("");					//split it into characters
            StringBuffer result_buffer = new StringBuffer();
            for (int i = 0; i < 14; i++) {						
            	result_buffer.append( obt[i] );					//append the first 15-char viz ID!
            }
            String identity_no = result_buffer.toString();

            System.out.println("Last Name is ->"+last_name[0]+". FirstName is ->"+first_name[0]+ ". DOB->"+dob[0]+ ". Final_ID No.->"+identity_no+ ". Process_string->"+ret_id_pr);
//   	     	if (indexfound > -1) {	// If greater than -1, means we found the word
//             System.out.println("Word was found at position " + indexfound);
//   	                    }
//              System.out.println(Arrays.toString(dob));
//              System.out.println("My file-content:"+text_cnt);
//            System.out.println("Boolen value is->"+filetype_accepted);	//this variable is accessible globally
            
        } catch(Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }

        
    }
	
}
