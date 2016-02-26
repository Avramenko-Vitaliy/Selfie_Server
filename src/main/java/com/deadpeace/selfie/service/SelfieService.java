package com.deadpeace.selfie.service;

import com.deadpeace.selfie.ImageUtil;
import com.deadpeace.selfie.SelfieUtil;
import com.deadpeace.selfie.client.SelfieSvcApi;
import com.deadpeace.selfie.model.Selfie;
import com.deadpeace.selfie.model.User;
import com.deadpeace.selfie.model.UserRole;
import com.deadpeace.selfie.repository.RoleRepository;
import com.deadpeace.selfie.repository.SelfieRepository;
import com.deadpeace.selfie.repository.UserRepository;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class SelfieService
{
    @Autowired
    private SelfieRepository selfis;

    @Autowired
    private UserRepository users;

    @Lazy
    @Autowired
    private RoleRepository roles;

    public List<Selfie> getSelfieList(String user)
    {
        return selfis.findByCreatorOrderByDateDesc(users.findByUsername(user));
    }

    public Selfie saveSelfie(Selfie selfie, String userName)
    {
        User user=users.findByUsername(userName);
        if(null!=user)
        {
            selfie.setCreator(user);
            selfie.setDate(System.currentTimeMillis());
            return selfis.save(selfie);
        }
        return selfie;
    }

    public Selfie getSelfie(long id)
    {
        return selfis.findById(id);
    }

    public boolean deleteSelfie(long id, String userName) throws ChangeSetPersister.NotFoundException
    {
        User user=users.findByUsername(userName);
        if(null!=user && selfis.exists(id))
        {
            Selfie selfie=selfis.findById(id);
            if(selfie.getCreator().getUsername().equals(user.getUsername()))
            {
                new File(".\\images", "selfie_"+id+".jpg").delete();
                selfis.delete(selfie);
                return true;
            }
            throw new SecurityException("You not have permissions for delete this Selfie!");
        }
        else
            throw new ChangeSetPersister.NotFoundException();
    }

    public String savePhotoSelfie(MultipartFile photo, long id, String userName) throws ChangeSetPersister.NotFoundException
    {
        if(selfis.exists(id))
        {
            Selfie selfie=selfis.findById(id);
            if(selfie.getCreator().getUsername().equals(userName))
                return SelfieUtil.saveFile(photo,"selfie_"+selfie.getId()+".jpg");
            else
                throw new SecurityException("You not have privilege change this file!");
        }
        else
            throw new ChangeSetPersister.NotFoundException();
    }

    public byte[] getBytesPhotoAvatar(String userName) throws IOException
    {
        File file=new File(".\\images",userName+".jpg");
        return FileUtils.readFileToByteArray(file.exists()?file:new File(".\\images\\no_image","noimage.jpg"));
    }

    public byte[] getBytesPhotoSelfie(long id) throws IOException
    {
        File file=new File(".\\images","selfie_"+id+".jpg");
        return FileUtils.readFileToByteArray(file.exists()?file:new File(".\\images\\no_image","image_not_found.jpg"));
    }

    public byte[] getBytesPreviewPhotoAvatar(String userName) throws IOException
    {
        File file=new File(".\\images",userName+".jpg");
        return FileUtils.readFileToByteArray(file.exists()?file:new File(".\\images\\no_image","noimage.jpg"));
    }

    public byte[] getBytesPreviewPhotoSelfie(long id) throws IOException
    {
        File file=new File(".\\images","selfie_"+id+".jpg");
        return FileUtils.readFileToByteArray(file.exists()?file:new File(".\\images\\no_image","image_not_found.jpg"));
    }

    public String saveAvatar(MultipartFile photo,String userName)
    {
        return SelfieUtil.saveFile(photo,userName+".jpg");
    }

    public User addUser(String username,String pass, String email)
    {
        User user=new User();
        user.setUsername(username);
        user.setPassword(pass);
        user.setEmail(email);
        user.setAuthorities(Arrays.asList(roles.findByAuthority("user")));
        return users.save(user);
    }


    public BufferedImage doRotate(MultipartFile photo, int degree) throws IOException
    {
        return ImageUtil.doRotate(ImageIO.read(photo.getInputStream()), degree);
    }

    public BufferedImage doSepia(MultipartFile photo, int deep) throws IOException
    {
        return ImageUtil.toSepia(ImageIO.read(photo.getInputStream()), deep);
    }

    public BufferedImage doBlackAndWhite(MultipartFile photo) throws IOException
    {
        return ImageUtil.toGray(ImageIO.read(photo.getInputStream()));
    }

    public BufferedImage doBrightness(MultipartFile photo, int bright) throws IOException
    {
        return ImageUtil.toBrightness(ImageIO.read(photo.getInputStream()),bright);
    }

    public User getUser(String name)
    {
        return users.findByUsername(name);
    }

    public User changeCredential(String password, String eMail, String name)
    {
        User user=users.findByUsername(name);
        if(user!=null)
        {
            if(password!=null && !password.equals(""))
                user.setPassword(password);
            if(eMail!=null && !eMail.equals(""))
                user.setEmail(eMail);
            return users.save(user);
        }
        else
            throw new UsernameNotFoundException("User not found");
    }
}
