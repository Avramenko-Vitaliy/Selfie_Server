package com.deadpeace.selfie;

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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Виталий on 07.10.2015.
 */

@Controller
public class SelfieSvc
{
    @Autowired
    private SelfieRepository selfis;

    @Autowired
    private UserRepository users;

    @Lazy
    @Autowired
    private RoleRepository roles;

    @RequestMapping(value = SelfieSvcApi.SVC_SELFIE,method =RequestMethod.GET)
    public @ResponseBody List<Selfie> getSelfieList(Principal principal)
    {
        return selfis.findByCreatorOrderByDateDesc(users.findByUsername(principal.getName()));
    }

    @RequestMapping(value = SelfieSvcApi.SVC_SELFIE,method = RequestMethod.POST)
    public @ResponseBody Selfie addSelfie(@RequestBody Selfie selfie,Principal principal)
    {
        User user=users.findByUsername(principal.getName());
        if(null!=user)
        {
            selfie.setCreator(user);
            selfie.setDate(System.currentTimeMillis());
            return selfis.save(selfie);
        }
        return selfie;
    }

    @RequestMapping(value = SelfieSvcApi.SVC_SELFIE_ID,method = RequestMethod.GET)
    public @ResponseBody Selfie getSelfie(@PathVariable(SelfieSvcApi.ID_PARAM) long id)
    {
        return selfis.findById(id);
    }

    @RequestMapping(value = SelfieSvcApi.SVC_SELFIE_ID,method = RequestMethod.DELETE)
    public @ResponseBody boolean delSelfie(@PathVariable(SelfieSvcApi.ID_PARAM) long id,Principal principal,HttpServletResponse response)
    {
        User user=users.findByUsername(principal.getName());
        if(null!=user && selfis.exists(id))
        {
            Selfie selfie=selfis.findById(id);
            if(selfie.getCreator().getUsername().equals(user.getUsername()))
            {
                new File(".\\images","selfie_"+id+".jpg").delete();
                selfis.delete(selfie);
                response.setStatus(HttpServletResponse.SC_OK);
                return true;
            }
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }
        else
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        return false;
    }

    @RequestMapping(value = SelfieSvcApi.SVC_SELFIE_UPLOAD,method = RequestMethod.POST)
    public @ResponseBody String uploadSelfie(@RequestParam(value = SelfieSvcApi.FILE_PARAM)MultipartFile photo,@PathVariable(SelfieSvcApi.ID_PARAM) long id,HttpServletResponse response,Principal principal)throws IOException
    {
        if(selfis.exists(id))
        {
            Selfie selfie=selfis.findById(id);
            if(selfie.getCreator().getUsername().equals(principal.getName()))
                return SelfieUtil.saveFile(photo,"selfie_"+selfie.getId()+".jpg");
            else
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return "You not have privilege change this file!";
        }
        else
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        return null;
    }

    @RequestMapping(value = SelfieSvcApi.SVC_SELFIE_DOWNLOAD,method = RequestMethod.GET)
    public void downloadSelfie(@PathVariable(SelfieSvcApi.ID_PARAM) long id,HttpServletResponse response)throws IOException
    {
        File file=new File(".\\images","selfie_"+id+".jpg");
        response.getOutputStream().write(FileUtils.readFileToByteArray(file.exists()?file:new File(".\\images\\no_image","image_not_found.jpg")));
    }

    @RequestMapping(value = SelfieSvcApi.SVC_AVATAR_UPLOAD,method = RequestMethod.POST)
    public @ResponseBody String uploadAvatar(@RequestParam(value = SelfieSvcApi.FILE_PARAM)MultipartFile photo,Principal principal)throws IOException
    {
        return SelfieUtil.saveFile(photo,principal.getName()+".jpg");
    }

    @RequestMapping(value = SelfieSvcApi.SVC_AVATAR_DOWNLOAD,method = RequestMethod.GET)
    public void downloadAvatar(@PathVariable(SelfieSvcApi.USER_NAME_PARAM) String username,HttpServletResponse response)throws IOException
    {
        File file=new File(".\\images",username+".jpg");
        response.getOutputStream().write(FileUtils.readFileToByteArray(file.exists()?file:new File(".\\images\\no_image","noimage.jpg")));
    }

    @RequestMapping(value = SelfieSvcApi.SVC_REGISTER,method = RequestMethod.POST)
    public void register(@RequestParam(SelfieSvcApi.USER_NAME_PARAM) String username,@RequestParam(SelfieSvcApi.PASSWORD_PARAM) String pass,@RequestParam(SelfieSvcApi.EMAIL_PARAM) String email,HttpServletResponse response)
    {
        UserRole role=roles.existsByAuthority("user")?roles.findByAuthority("user"):roles.save(new UserRole("user"));
        User user=new User();
        user.setUsername(username);
        user.setPassword(pass);
        user.setEmail(email);
        user.setAuthorities(Arrays.asList(role));
        users.save(user);
    }

    @RequestMapping(value = SelfieSvcApi.SVC_AVATAR_DOWNLOAD_PREVIEW,method = RequestMethod.GET)
    public void downloadPreviewAvatar(@PathVariable(SelfieSvcApi.USER_NAME_PARAM) String username,HttpServletResponse response)throws IOException
    {
        File file=new File(".\\images\\preview",username+".jpg");
        response.getOutputStream().write(FileUtils.readFileToByteArray(file.exists()?file:new File(".\\images\\no_image","noimage.jpg")));
    }

    @RequestMapping(value = SelfieSvcApi.SVC_SELFIE_DOWNLOAD_PREVIEW,method = RequestMethod.GET)
    public void downloadPreviewSelfie(@PathVariable(SelfieSvcApi.ID_PARAM) long id,HttpServletResponse response)throws IOException
    {
        File file=new File(".\\images\\preview","selfie_"+id+".jpg");
        response.getOutputStream().write(FileUtils.readFileToByteArray(file.exists()?file:new File(".\\images\\no_image","image_not_found.jpg")));
    }

    @RequestMapping(value = SelfieSvcApi.SVC_SELFIE_ROTATE,method = RequestMethod.POST)
    public void rotate(@RequestParam(SelfieSvcApi.FILE_PARAM) MultipartFile photo,@PathVariable(SelfieSvcApi.DEGREE_PARAM)int degree,HttpServletResponse response)throws IOException
    {
        ImageIO.write(ImageUtil.doRotate(ImageIO.read(photo.getInputStream()), degree), "jpg", response.getOutputStream());
    }

    @RequestMapping(value = SelfieSvcApi.SVC_SELFIE_SEPIA,method = RequestMethod.POST)
    public void toSepia(@RequestParam(SelfieSvcApi.FILE_PARAM) MultipartFile photo,@PathVariable(SelfieSvcApi.DEEP_PARAM)int deep,HttpServletResponse response)throws IOException
    {
       ImageIO.write(ImageUtil.toSepia(ImageIO.read(photo.getInputStream()), deep), "jpg", response.getOutputStream());
    }

    @RequestMapping(value = SelfieSvcApi.SVC_SELFIE_BLACK_AND_WHITE,method = RequestMethod.POST)
    public void toBlackAndWhite(@RequestParam(SelfieSvcApi.FILE_PARAM) MultipartFile photo,HttpServletResponse response)throws IOException
    {
        ImageIO.write(ImageUtil.toGray(ImageIO.read(photo.getInputStream())),"jpg",response.getOutputStream());
    }

    @RequestMapping(value = SelfieSvcApi.SVC_SELFIE_BRIGHTNESS,method = RequestMethod.POST)
    public void brightness(@RequestParam(SelfieSvcApi.FILE_PARAM) MultipartFile photo,@PathVariable(SelfieSvcApi.BRIGHTNESS_PARAM)int bright,HttpServletResponse response)throws IOException
    {
        ImageIO.write(ImageUtil.toBrightness(ImageIO.read(photo.getInputStream()),bright),"jpg",response.getOutputStream());
    }

    @RequestMapping(value = SelfieSvcApi.SVC_USER,method = RequestMethod.GET)
    public @ResponseBody User getUser(Principal principal)
    {
        return users.findByUsername(principal.getName());
    }

    @RequestMapping(value = SelfieSvcApi.SVC_CHANGE_CREDENTIAL,method = RequestMethod.POST)
    public @ResponseBody User changeCredential(@RequestParam(SelfieSvcApi.PASSWORD_PARAM)String password,@RequestParam(SelfieSvcApi.EMAIL_PARAM)String eMail,Principal principal)
    {
        User user=users.findByUsername(principal.getName());
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
