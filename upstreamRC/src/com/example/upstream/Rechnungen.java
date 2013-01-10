package com.example.upstream;

public class Rechnungen {

	 
	 public int berechneXrAusX(float wert){
	    	int retWert = 0;
	    	
	    	if(wert < 0){
	    		if(wert < -30)
	    			retWert = 0;
	    		else{
	    			double d = (wert*127/30)+127;
	    			retWert = (int)d;
	    		}
	    		//x.setText("X werte kleiner 0 => " + testX);
	    	}else if(wert > 0){
	    		
	    		if(wert > 30)
	    			retWert = 255;
	    		else {
	    			double d = (wert*128/30)+127;
	    			retWert =(int) d;
	    		}
	    	//	x.setText("X werte grˆﬂer 0 => " + testX);
	    	}else{
	    		retWert = 127; 
	    	}
	    		    	
	    	return Math.abs(retWert);
	    }
	 
	 public int progressBarAusX(int wert){
	    	int retWert;
	    	
		    retWert=(int)(wert*100/255);
	    	return retWert;
	 }
	 
	    
	public byte analysiere(int progress){
	    	
		if (progress>=45 && progress<=55) {
			return 127;
		}
		
	    	return (byte)(progress * 255/100);
	}
	    
	
}
