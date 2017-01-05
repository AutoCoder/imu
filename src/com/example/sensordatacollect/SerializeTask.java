package com.example.sensordatacollect;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Vector;

public class SerializeTask extends Thread {
	private String mFilename;
	private Vector<Scalar4> mData = null;
	private SensorType mType;
	
	public SerializeTask(String filename, Vector<Scalar4> data, SensorType t) throws FileNotFoundException
	{
		mFilename = filename;
		mData = data;
		mType = t;
	}
	
	static StringBuilder ToStringUtils(StringBuilder sb, Scalar4 data, SensorType t)
	{
		sb.append(" ");
    	sb.append(data._x);
    	sb.append(" ");
    	sb.append(data._y);
    	sb.append(" ");
    	sb.append(data._z);
    	if (t == SensorType.ROTATION_VECTOR)
    	{
    		sb.append(" ");
    		sb.append(data._v);
    	}
    	sb.append("\n");
    	
    	return sb;
	}
	
    @Override
    public synchronized void run() {
		try { 
			if (mData.isEmpty())
				return;
			
			FileOutputStream f = new FileOutputStream(mFilename);
			OutputStreamWriter w = new OutputStreamWriter(f);
            StringBuilder sb =  new StringBuilder(mType.getName() + "\n" + mType.getDimension());  
            for (int i =0; i < mData.capacity(); ++i)
            {
            	Scalar4 temp = mData.get(i);
            	ToStringUtils(sb, temp, mType);	
            }
            
            w.write(sb.toString());  
            w.close();
            f.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
