package com.deadpeace.selfie.controller;

import com.deadpeace.selfie.client.SelfieSvcApi;
import com.deadpeace.selfie.model.Selfie;
import com.deadpeace.selfie.model.User;
import com.deadpeace.selfie.service.SelfieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
public class SelfieController
{
    @Autowired
    private SelfieService service;

    @RequestMapping(value = SelfieSvcApi.SVC_SELFIE,method =RequestMethod.GET)
    public List<Selfie> getSelfieList(Principal principal)
    {
        return service.getSelfieList(principal.getName());
    }

    @RequestMapping(value = SelfieSvcApi.SVC_SELFIE,method = RequestMethod.POST)
    public Selfie addSelfie(@RequestBody Selfie selfie,Principal principal)
    {
        return service.saveSelfie(selfie,principal.getName());
    }

    @RequestMapping(value = SelfieSvcApi.SVC_SELFIE_ID,method = RequestMethod.GET)
    public Selfie getSelfie(@PathVariable(SelfieSvcApi.ID_PARAM) long id)
    {
        return service.getSelfie(id);
    }

    @RequestMapping(value = SelfieSvcApi.SVC_SELFIE_ID,method = RequestMethod.DELETE)
    public boolean deleteSelfie(@PathVariable(SelfieSvcApi.ID_PARAM) long id, Principal principal) throws ChangeSetPersister.NotFoundException
    {
        return service.deleteSelfie(id,principal.getName());
    }

    @RequestMapping(value = SelfieSvcApi.SVC_SELFIE_UPLOAD,method = RequestMethod.POST)
    public String uploadSelfie(@RequestParam(value = SelfieSvcApi.FILE_PARAM)MultipartFile photo,@PathVariable(SelfieSvcApi.ID_PARAM) long id,Principal principal) throws IOException, ChangeSetPersister.NotFoundException
    {
        return service.savePhotoSelfie(photo,id,principal.getName());
    }

    @RequestMapping(value = SelfieSvcApi.SVC_SELFIE_DOWNLOAD,method = RequestMethod.GET)
    public void downloadSelfie(@PathVariable(SelfieSvcApi.ID_PARAM) long id,HttpServletResponse response)throws IOException
    {
        response.getOutputStream().write(service.getBytesPhotoSelfie(id));
    }

    @RequestMapping(value = SelfieSvcApi.SVC_AVATAR_UPLOAD,method = RequestMethod.POST)
    public String uploadAvatar(@RequestParam(value = SelfieSvcApi.FILE_PARAM)MultipartFile photo,Principal principal)throws IOException
    {
        return service.saveAvatar(photo,principal.getName());
    }

    @RequestMapping(value = SelfieSvcApi.SVC_AVATAR_DOWNLOAD,method = RequestMethod.GET)
    public void downloadAvatar(@PathVariable(SelfieSvcApi.USER_NAME_PARAM) String username,HttpServletResponse response)throws IOException
    {
        response.getOutputStream().write(service.getBytesPhotoAvatar(username));
    }

    @RequestMapping(value = SelfieSvcApi.SVC_REGISTER,method = RequestMethod.POST)
    public void register(@RequestParam(SelfieSvcApi.USER_NAME_PARAM) String username,@RequestParam(SelfieSvcApi.PASSWORD_PARAM) String pass,@RequestParam(SelfieSvcApi.EMAIL_PARAM) String email)
    {
        service.addUser(username,pass,email);
    }

    @RequestMapping(value = SelfieSvcApi.SVC_AVATAR_DOWNLOAD_PREVIEW,method = RequestMethod.GET)
    public void downloadPreviewAvatar(@PathVariable(SelfieSvcApi.USER_NAME_PARAM) String username,HttpServletResponse response)throws IOException
    {
        response.getOutputStream().write(service.getBytesPreviewPhotoAvatar(username));
    }

    @RequestMapping(value = SelfieSvcApi.SVC_SELFIE_DOWNLOAD_PREVIEW,method = RequestMethod.GET)
    public void downloadPreviewSelfie(@PathVariable(SelfieSvcApi.ID_PARAM) long id,HttpServletResponse response)throws IOException
    {
        response.getOutputStream().write(service.getBytesPreviewPhotoSelfie(id));
    }

    @RequestMapping(value = SelfieSvcApi.SVC_SELFIE_ROTATE,method = RequestMethod.POST)
    public void rotate(@RequestParam(SelfieSvcApi.FILE_PARAM) MultipartFile photo,@PathVariable(SelfieSvcApi.DEGREE_PARAM)int degree,HttpServletResponse response)throws IOException
    {
        ImageIO.write(service.doRotate(photo, degree), "jpg", response.getOutputStream());
    }

    @RequestMapping(value = SelfieSvcApi.SVC_SELFIE_SEPIA,method = RequestMethod.POST)
    public void toSepia(@RequestParam(SelfieSvcApi.FILE_PARAM) MultipartFile photo,@PathVariable(SelfieSvcApi.DEEP_PARAM)int deep,HttpServletResponse response)throws IOException
    {
       ImageIO.write(service.doSepia(photo, deep), "jpg", response.getOutputStream());
    }

    @RequestMapping(value = SelfieSvcApi.SVC_SELFIE_BLACK_AND_WHITE,method = RequestMethod.POST)
    public void toBlackAndWhite(@RequestParam(SelfieSvcApi.FILE_PARAM) MultipartFile photo,HttpServletResponse response)throws IOException
    {
        ImageIO.write(service.doBlackAndWhite(photo),"jpg",response.getOutputStream());
    }

    @RequestMapping(value = SelfieSvcApi.SVC_SELFIE_BRIGHTNESS,method = RequestMethod.POST)
    public void brightness(@RequestParam(SelfieSvcApi.FILE_PARAM) MultipartFile photo,@PathVariable(SelfieSvcApi.BRIGHTNESS_PARAM)int bright,HttpServletResponse response)throws IOException
    {
        ImageIO.write(service.doBrightness(photo, bright),"jpg",response.getOutputStream());
    }

    @RequestMapping(value = SelfieSvcApi.SVC_USER,method = RequestMethod.GET)
    public User getUser(Principal principal)
    {
        return service.getUser(principal.getName());
    }

    @RequestMapping(value = SelfieSvcApi.SVC_CHANGE_CREDENTIAL,method = RequestMethod.POST)
    public User changeCredential(@RequestParam(SelfieSvcApi.PASSWORD_PARAM)String password,@RequestParam(SelfieSvcApi.EMAIL_PARAM)String eMail,Principal principal)
    {
        return service.changeCredential(password,eMail,principal.getName());
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleException(Exception exception)
    {
        return exception.getMessage();
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public void handleException(ChangeSetPersister.NotFoundException exception)
    {

    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String handleException(SecurityException exception)
    {
        return exception.getMessage();
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String handleException(UsernameNotFoundException exception)
    {
        return exception.getMessage();
    }
}
