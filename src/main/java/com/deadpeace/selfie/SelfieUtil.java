package com.deadpeace.selfie;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Виталий on 07.10.2015.
 */
public class SelfieUtil
{
    public static final String EMAIL_PATTERN="^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private SelfieUtil()
    {
    }

    public static String saveFile(MultipartFile photo,String name)
    {
        if(!photo.isEmpty())
        {
            try
            {
                byte[] bytes=photo.getBytes();
                BufferedOutputStream stream=new BufferedOutputStream(new FileOutputStream(new File(".\\images",name)));
                stream.write(bytes);
                stream.close();
                createPreview(photo,name);
                return "Done!";
            }
            catch(Exception e)
            {
                return e.getMessage();
            }
        }
        else
            return "File is empty!";
    }

    private static void createPreview(MultipartFile photo,String nameFile) throws IOException
    {
        BufferedImage image=ImageIO.read(photo.getInputStream());
        File file=new File(".\\images\\preview",nameFile);
        ImageIO.write(ImageUtil.toPreview(image),"jpg",file);
    }
}
