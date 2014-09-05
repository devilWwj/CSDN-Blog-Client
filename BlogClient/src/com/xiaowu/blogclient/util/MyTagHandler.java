package com.xiaowu.blogclient.util;

import org.xml.sax.XMLReader;

import android.graphics.Color;
import android.text.Editable;
import android.text.Html.TagHandler;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

public class MyTagHandler implements TagHandler{
	boolean first= true;
	String parent="ul";
	int index=1;
	
	private int sIndex = 0;  
    private int eIndex=0;
	@Override
	public void handleTag(boolean opening, String tag, Editable output,
	        XMLReader xmlReader) {

	    if(tag.equals("ul")) parent="ul";
	    else if(tag.equals("ol")) parent="ol";
	    if(tag.equals("li")){
	        if(parent.equals("ul")){
	            if(first){
	                output.append("\n• ");
	                first= false;
	            }else{
	                first = true;
	            }
	        }
	        else{
	            if(first){
	                output.append("\n"+index+". ");
	                first= false;
	                index++;
	            }else{
	                first = true;
	            }
	        }   
	    }
	    
	    
	    if(tag.equals("bold")){
	    	System.out.println("tag" + tag);
	    	if (opening) {
                sIndex=output.length();
                System.out.println("sIndex" + sIndex);
            }else {
                eIndex=output.length();
                System.out.println("eIndex" + eIndex);
                output.setSpan(new ForegroundColorSpan(Color.BLACK), sIndex, eIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                //output.setSpan(new SubscriptSpan(), sIndex, eIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
	    }
	}
	}
