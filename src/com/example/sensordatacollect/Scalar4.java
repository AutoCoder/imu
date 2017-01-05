package com.example.sensordatacollect;

public class Scalar4 {
	public Scalar4(double x, double y, double z){
		_x = x;
		_y = y;
		_z = z;
	}
	
	public Scalar4(double x, double y, double z, double v){
		_x = x;
		_y = y;
		_z = z;
		_v = v;
	}
	public double _x;
	public double _y;
	public double _z;
	public double _v;
}
