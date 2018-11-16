package com.neo.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by summer on 2017/5/4.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MailServiceTest {

    @Autowired
    private MailService mailService;

    @Autowired
    private TemplateEngine templateEngine;

    @Test
    public void testSimpleMail() throws Exception {
        mailService.sendSimpleMail("1186389154@qq.com","test simple mail"," hello this is simple mail");
    }

    @Test
    public void testHtmlMail() throws Exception {
        String content="<html>\n" +
                "<body>\n" +
                "    <h3>hello world ! 这是一封html邮件!</h3>\n" +
                "</body>\n" +
                "</html>";
        mailService.sendHtmlMail("1186389154@qq.com","test simple mail",content);
    }

    @Test
    public void sendAttachmentsMail() {
        String filePath="e:\\tmp\\application.log";
        mailService.sendAttachmentsMail("1186389154@qq.com", "主题：带附件的邮件", "有附件，请查收！", filePath);
    }


    @Test
    public void sendInlineResourceMail() throws IOException {
        String rscId = "neo006";
        String content="<html><body>这是有图片的邮件：<img src=\'cid:" + rscId + "\' ></body></html>";
        Resource resource = new ClassPathResource("static/favicon.ico");
        File file = resource.getFile();
        System.out.println(content);
        mailService.sendInlineResourceMail("1186389154@qq.com", "主题：这是有图片的邮件", content, file.getPath(), rscId);
    }


    @Test
    public void sendTemplateMail() throws IOException {
        //创建邮件正文

      /*  final Context ctx = new Context();
        ctx.setVariable("name", "小明");
        ctx.setVariable("subscriptionDate", new Date());
        ctx.setVariable("hobbies", Arrays.asList("Cinema", "Sports", "Music"));
        ctx.setVariable("imageResourceName", "a.png"); // so that we can reference it from HTML
        Resource resource = new ClassPathResource("static/favicon.ico");

        File file = resource.getFile();


        final String htmlContent = this.templateEngine.process("email-inlineimage", ctx);
        System.out.print(htmlContent);
        mailService.sendInThymeleaf("1186389154@qq.com", "主题：这是有图片的邮件", htmlContent , "imageResourceName",file.getPath());*/
       inita();
    }

    private void inita() {
    }
}
