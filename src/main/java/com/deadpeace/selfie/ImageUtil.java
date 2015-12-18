package com.deadpeace.selfie;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.awt.image.WritableRaster;

/**
 * Created by Виталий on 09.10.2015.
 */
public class ImageUtil
{
    private ImageUtil()
    {

    }

    public static BufferedImage toGray(BufferedImage img)
    {
        int width=img.getWidth();
        int height=img.getHeight();
        for(int i=0;i<height;i++)
            for(int j=0;j<width;j++)
            {
                Color c=new Color(img.getRGB(j,i));
                int red=(int)(c.getRed()*0.299);
                int green=(int)(c.getGreen()*0.587);
                int blue=(int)(c.getBlue()*0.114);
                Color newColor=new Color(red+green+blue,red+green+blue,red+green+blue);
                img.setRGB(j,i,newColor.getRGB());
            }
        return img;
    }

    public static BufferedImage toSepia(BufferedImage img,int intensity)
    {
        BufferedImage sepia=new BufferedImage(img.getWidth(),img.getHeight(),img.getType());
        // Play around with this.  20 works well and was recommended
        //   by another developer. 0 produces black/white image
        int sepiaDepth=20;
        int w=img.getWidth();
        int h=img.getHeight();
        WritableRaster raster=sepia.getRaster();
        // We need 3 integers (for R,G,B color values) per pixel.
        int[] pixels=new int[w*h*3];
        img.getRaster().getPixels(0,0,w,h,pixels);
        //  Process 3 ints at a time for each pixel.  Each pixel has 3 RGB
        //    colors in array
        for(int i=0;i<pixels.length;i+=3)
        {
            int r=pixels[i];
            int g=pixels[i+1];
            int b=pixels[i+2];
            int gry=(r+g+b)/3;
            r=g=b=gry;
            r=r+(sepiaDepth*2);
            g=g+sepiaDepth;
            if(r>255)
                r=255;
            if(g>255)
                g=255;
            if(b>255)
                b=255;
            // Darken blue color to increase sepia effect
            b-=intensity;
            // normalize if out of bounds
            if(b<0)
                b=0;
            if(b>255)
                b=255;
            pixels[i]=r;
            pixels[i+1]=g;
            pixels[i+2]=b;
        }
        raster.setPixels(0,0,w,h,pixels);
        return sepia;
    }

    public static BufferedImage toBrightness(BufferedImage img,int brightness)
    {
        RescaleOp rescaleOp=new RescaleOp(brightness/10f,0,null);
        return rescaleOp.filter(img,new BufferedImage(img.getWidth(),img.getHeight(),img.getType()));
    }

    public static BufferedImage toPreview(BufferedImage img)
    {
        int height=img.getHeight();
        int width=img.getWidth();
        if(height<=800||width<=800)
            return img;
        float coef=(height>width?height:width)/800f;
        BufferedImage outputImage=new BufferedImage(Math.round(width/coef),Math.round(height/coef),img.getType());
        Graphics2D graphics2D=outputImage.createGraphics();
        graphics2D.drawImage(img,0,0,outputImage.getWidth(),outputImage.getHeight(),null);
        graphics2D.dispose();
        return outputImage;
    }

    public static BufferedImage doRotate(BufferedImage img, int degree)
    {
        AffineTransform xform = new AffineTransform();
        xform.translate(0.5*img.getHeight(), 0.5*img.getWidth());
        xform.rotate(Math.toRadians(degree));
        xform.translate(-0.5*img.getWidth(), -0.5*img.getHeight());
        BufferedImage rotate=new BufferedImage(img.getHeight(),img.getWidth(),img.getType());
        AffineTransformOp op=new AffineTransformOp(xform,AffineTransformOp.TYPE_BILINEAR);
        Graphics2D graphics2D=rotate.createGraphics();
        graphics2D.drawImage(op.filter(img,rotate),rotate.getWidth(),rotate.getHeight(),null);
        graphics2D.dispose();
        return rotate;
    }
}
