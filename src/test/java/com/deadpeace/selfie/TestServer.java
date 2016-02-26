package com.deadpeace.selfie;

import com.deadpeace.selfie.client.SelfieSvcApi;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import retrofit.RestAdapter;
import retrofit.client.ApacheClient;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedFile;

import java.io.File;
import java.io.FileOutputStream;

public class TestServer
{
    private static final String TEST_URL="https://localhost:8443";

    private static SelfieSvcApi selfieSvcApi=new RestAdapter.Builder()
            .setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
            .setEndpoint(TEST_URL)
            .setLogLevel(RestAdapter.LogLevel.FULL).build()
            .create(SelfieSvcApi.class);

    @Before
    @Test
    public void testLogin()
    {
        selfieSvcApi.login("admin", "pass");
    }

    @Test
    @After
    public void testLogout()
    {
        try
        {
            selfieSvcApi.logout();
        }
        catch(Exception e)
        {
            Assert.fail(e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    public void testRotateLeft()
    {
        try
        {
            File send=new File("D:\\","House.jpg");
            File getting=new File("D:\\","Rotate_left.jpg");
            FileOutputStream output = new FileOutputStream(getting);
            IOUtils.write(((TypedByteArray) selfieSvcApi.rotate(new TypedFile("image/jpg", send), -90).getBody()).getBytes(), output);
        }
        catch(Exception e)
        {
            Assert.fail(e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    public void testRotateRight()
    {
        try
        {
            File send=new File("D:\\","House.jpg");
            File getting=new File("D:\\","Rotate_right.jpg");
            FileOutputStream output = new FileOutputStream(getting);
            IOUtils.write(((TypedByteArray) selfieSvcApi.rotate(new TypedFile("image/jpg", send), 90).getBody()).getBytes(), output);
        }
        catch(Exception e)
        {
            Assert.fail(e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    public void testSepia()
    {
        try
        {
            File send=new File("D:\\","House.jpg");
            File getting=new File("D:\\","Sepia.jpg");
            FileOutputStream output = new FileOutputStream(getting);
            IOUtils.write(((TypedByteArray) selfieSvcApi.toSepia(new TypedFile("image/jpg", send), 80).getBody()).getBytes(), output);
        }
        catch(Exception e)
        {
            Assert.fail(e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    public void testGrey()
    {
        try
        {
            File send=new File("D:\\","House.jpg");
            File getting=new File("D:\\","Grey.jpg");
            FileOutputStream output = new FileOutputStream(getting);
            IOUtils.write(((TypedByteArray) selfieSvcApi.toBlackAndWhite(new TypedFile("image/jpg", send)).getBody()).getBytes(), output);
        }
        catch(Exception e)
        {
            Assert.fail(e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    public void testBrightness()
    {
        try
        {
            File send=new File("D:\\","House.jpg");
            File getting=new File("D:\\","Brightness.jpg");
            FileOutputStream output = new FileOutputStream(getting);
            IOUtils.write(((TypedByteArray) selfieSvcApi.toBrightness(new TypedFile("image/jpg", send), 90).getBody()).getBytes(), output);
        }
        catch(Exception e)
        {
            Assert.fail(e.getMessage());
            e.printStackTrace();
        }
    }
}
