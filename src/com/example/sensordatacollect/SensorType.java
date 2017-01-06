package com.example.sensordatacollect;

public enum SensorType {
	Undifined("Undinfined", "invalid..."),
	ACCELEROMETER("Accelerometer", "x y z (m/s2)"), //  m/s2
	GRAVITY("Gravity", "x y z (m/s2)"),             //  m/s2
	GYROSCOPE("Gyroscope", "x y z (rad/s)"),         //  rad/s
	MAGNETIC("MAGNETIC", "x y z"),
	LINEAR_ACCELERATION("Line_Acceleration", "x y z (m/s2)"), // m/s2
	ROTATION_VECTOR("Rotation_Vector", "x y z v"); //   
	
    private String name ;
    private String dimension ;
    
    private SensorType( String name , String dimension ){
        this.name = name ;
        this.dimension = dimension ;
    }
     
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDimension() {
        return dimension;
    }
    public void setDimension(String d) {
        this.dimension = d;
    }
}
