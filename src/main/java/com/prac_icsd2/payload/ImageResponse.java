package com.prac_icsd2.payload;

public class ImageResponse {

    private String imageName;
    private String message;

    // Constructor
    public ImageResponse(String imageName, String message) {
        this.imageName = imageName;
        this.message = message;
    }

    // Getters and Setters (Names now match the variables)
    public String getImageName() { 
        return imageName; 
    }

    public void setImageName(String imageName) { 
        this.imageName = imageName; 
    }

    public String getMessage() { 
        return message; 
    }

    public void setMessage(String message) { 
        this.message = message; 
    }
}