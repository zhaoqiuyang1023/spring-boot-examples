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

import java.io.*;
import java.util.stream.Collectors;

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
      // String content="<html><body>这是有图片的邮件：<img src='cid:neo006'></body></html>";
        InputStream inputStream=new ClassPathResource("templates/emailTemplate.html").getInputStream();
       String content = new BufferedReader(new InputStreamReader(inputStream))
            .lines().collect(Collectors.joining(System.lineSeparator()));
        System.out.print(content);
   /*     HttpServletRequest request=ServletActionContext.getRequest();
        request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()*/
        Resource resource = new ClassPathResource("static/favicon.ico");
        File file = resource.getFile();

        mailService.sendInlineResourceMail("1186389154@qq.com", "主题：这是有图片的邮件", content, file.getPath(), rscId);
    }


    @Test
    public void sendTemplateMail() {
        //创建邮件正文
        Context context = new Context();
        context.setVariable("id", "006");
        String emailContent = templateEngine.process("emailTemplate", context);

        mailService.sendHtmlMail("1186389154@qq.com","主题：这是模板邮件",emailContent);
    }
}
