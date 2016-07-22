package org.examples.spring.controller.weixi;


import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.*;
import me.chanjar.weixin.mp.bean.*;
import me.chanjar.weixin.mp.bean.result.WxMpMassUploadResult;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Map;


/**
 * Description : TODO(需要开通认证，才可以使用大部分功能。个人不提供认证)
 * Description : TODO(利用 weixin-java-tools 进行微信开发)
 * 参考 https://github.com/chanjarster/weixin-java-tools/wiki 进行代码封装
 * User: h819
 * Date: 2015-2-5
 * Time: 下午4:47
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/weixin/")
public class WeiXinController {

    private static Logger logger = LoggerFactory.getLogger(WeiXinController.class);

    protected WxMpInMemoryConfigStorage wxMpConfigStorage;
    protected WxMpService wxMpService;
    protected WxMpMessageRouter wxMpMessageRouter;

    /**
     * 初始化配置
     */
    private void init() {

        wxMpConfigStorage = new WxMpInMemoryConfigStorage();

        wxMpConfigStorage.setAppId("wxa0358d3445d0f8da"); // 设置微信公众号的 :  AppID(应用ID)
        wxMpConfigStorage.setSecret("dfb376ef80bccec8aae302a3711e0d45"); // 设置微信公众号的 : AppSecret(应用密钥)
        wxMpConfigStorage.setToken("h819infotokenstr"); // 设置微信公众号的 : Token(令牌)
        wxMpConfigStorage.setAesKey("au2AiRJQrM51q1obhYNZIzsw0dKQm1m2n7nbJ9xiLRv"); // 设置微信公众号的 : EncodingAESKey(消息加解密密钥)

        wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(wxMpConfigStorage);

        WxMpMessageHandler handler = new WxMpMessageHandler() {
            @Override
            public WxMpXmlOutMessage handle(WxMpXmlMessage wxMpXmlMessage, Map<String, Object> stringObjectMap, WxMpService wxMpService, WxSessionManager wxSessionManager) throws WxErrorException {
                WxMpXmlOutTextMessage m
                        = WxMpXmlOutMessage.TEXT().content("测试加密消息").fromUser(wxMpXmlMessage.getToUserName())
                        .toUser(wxMpXmlMessage.getFromUserName()).build();
                return m;
            }

        };

        wxMpMessageRouter = new WxMpMessageRouter(wxMpService);
        wxMpMessageRouter.rule().async(false).content("哈哈").handler(handler).end();  // 拦截内容为“哈哈”的消息
    }

    /**
     * 被动接收消息
     * -
     * http://mp.weixin.qq.com/wiki/17/2d4265491f12608cd170a95559800f2d.html
     *
     * @param signature    微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
     * @param nonce        随机数
     * @param timestamp    时间戳
     * @param encryptTypes
     * @param msgSignature
     * @param echostr      随机字符串
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/request")
    @ResponseBody // 必需返回字符串
    public String inMsg(@RequestParam(value = "signature", required = false) String signature,
                        @RequestParam(value = "nonce", required = false) String nonce,
                        @RequestParam(value = "timestamp", required = false) String timestamp,
                        @RequestParam(value = "encrypt_type", required = false) String encryptTypes,
                        @RequestParam(value = "msg_signature", required = false) String msgSignature,
                        @RequestParam(value = "echostr", required = false) String echostr,
                        HttpServletRequest request, HttpServletResponse response) throws IOException {


        logger.info("signature = " + signature + " , " +
                "nonce = " + nonce + " , " +
                "timestamp = " + timestamp + " , " +
                "encrypt_type = " + encryptTypes + " , " +
                "msg_signature = " + msgSignature + " , " +
                "echostr = " + echostr);

        init();

        //第一次连接时，要返回微信服务器传递过来的  echostr 字符串，用于微信进行验证，检验 URL 是否能连通。
        //如果连通，则微信 “开发者中心” 服务器配置成功
        if (StringUtils.isNotBlank(echostr)) {
            // 说明是一个仅仅用来验证的请求，回显echostr
            logger.info(echostr);
            return echostr;
        }


        //检验该信息是否为微信服务器发送过来的
        if (!wxMpService.checkSignature(timestamp, nonce, signature)) {
            // 消息签名不正确，说明不是公众平台发过来的消息
            logger.info("非法请求");
            return "非法请求";
        }

        //判断是否加密
        String encryptType = StringUtils.isBlank(encryptTypes) ? "raw" : request.getParameter("encrypt_type");
        WxMpXmlMessage inMessage;
        WxMpXmlOutMessage outMessage;

        //明文
        if ("raw".equals(encryptType)) {
            // 明文传输的消息
            inMessage = WxMpXmlMessage.fromXml(request.getInputStream());
            outMessage = wxMpMessageRouter.route(inMessage);
            logger.info(outMessage.toXml());  //回复
        }
        //aes 加密
        if ("aes".equals(encryptType)) {
            // 是aes加密的消息
            inMessage = WxMpXmlMessage.fromEncryptedXml(request.getInputStream(), wxMpConfigStorage, timestamp, nonce, msgSignature);
            outMessage = wxMpMessageRouter.route(inMessage);
            logger.info(outMessage.toEncryptedXml(wxMpConfigStorage)); //回复
        }

        //同步回复微信请求. 此功能应该用于用户点击公众账号的某个菜单
        //也可以用于自动回复用户发给公众账号的信息

        /**
         * 文本消息
         */

        WxMpXmlOutMessage.TEXT().content("content").fromUser("from").toUser("to").build();

        /**
         * 图片消息
         */

        WxMpXmlOutMessage.IMAGE().mediaId("ddfefesfsdfef").fromUser("from").toUser("to").build();

        /**
         * 语音消息
         */

        WxMpXmlOutMessage.VOICE().mediaId("ddfefesfsdfef").fromUser("from").toUser("to").build();

        /**
         * 视频消息
         */

        WxMpXmlOutMessage.VIDEO().mediaId("media_id").fromUser("fromUser").toUser("toUser").title("title").description("ddfff").build();

        /**
         * 音乐消息
         */

        WxMpXmlOutMessage.MUSIC().fromUser("fromUser").toUser("toUser").title("title").description("ddfff").hqMusicUrl("hQMusicUrl").musicUrl("musicUrl").thumbMediaId("thumbMediaId").build();

        /**
         * 图文消息
         */

        WxMpXmlOutNewsMessage.Item item = new WxMpXmlOutNewsMessage.Item();
        item.setDescription("description");
        item.setPicUrl("picUrl");
        item.setTitle("title");
        item.setUrl("url");

        WxMpXmlOutNewsMessage m = WxMpXmlOutMessage.NEWS().fromUser("fromUser").toUser("toUser").addArticle(item).build();


        logger.info("不可识别的加密类型");

        return "";
    }

    /**
     * 主动发送消息，单个用户
     *
     * @param openId 接收者 openId
     * @throws WxErrorException
     */
    protected void sendMsg(String openId) throws WxErrorException {

        WxMpCustomMessage message = new WxMpCustomMessage();

        //文本消息
        message.TEXT().toUser(openId).content("sfsfdsdf").build();

        // 图片消息
        message.IMAGE().toUser(openId).mediaId("MEDIA_ID").build();

        // 语音消息
        message.VOICE().toUser(openId).mediaId("MEDIA_ID").build();

        // 视频消息
        message.VIDEO().toUser(openId).title("TITLE").mediaId("MEDIA_ID").thumbMediaId("MEDIA_ID").description("DESCRIPTION").build();

        // 音乐消息
        message.MUSIC().toUser(openId).title("TITLE").thumbMediaId("MEDIA_ID").description("DESCRIPTION").musicUrl("MUSIC_URL").hqMusicUrl("HQ_MUSIC_URL").build();

        // 图文消息
        WxMpCustomMessage.WxArticle article1 = new WxMpCustomMessage.WxArticle();
        article1.setUrl("URL");
        article1.setPicUrl("PIC_URL");
        article1.setDescription("Is Really A Happy Day");
        article1.setTitle("Happy Day");

        WxMpCustomMessage.WxArticle article2 = new WxMpCustomMessage.WxArticle();
        article2.setUrl("URL");
        article2.setPicUrl("PIC_URL");
        article2.setDescription("Is Really A Happy Day");
        article2.setTitle("Happy Day");

        message.NEWS().toUser(openId).addArticle(article1).addArticle(article2).build();

        // 设置消息的内容等信息
        wxMpService.customMessageSend(message);

    }

    /**
     * 主动发送消息，多个用户
     *
     * @param openIds 接收者 openid 数组
     * @param media   待发送文件
     * @throws WxErrorException
     */
    protected void sendGroupMsg(String[] openIds, File media) throws WxErrorException, IOException {

        // 如果要使用分组群发，则使用WxMpMassGroupMessage即可。
        WxMpMassOpenIdsMessage massMessage = new WxMpMassOpenIdsMessage();
        //注册用户
        for (String id : openIds)
            massMessage.getToUsers().add(id);

        /**
         *文本消息
         */

        massMessage.setMsgType(WxConsts.MASS_MSG_TEXT);
        massMessage.setContent("消息内容");
        // massMessage.getToUsers().add(openIds);
        wxMpService.massOpenIdsMessageSend(massMessage);

        /**
         *视频消息
         */

        WxMediaUploadResult uploadMediaResVideo = wxMpService.mediaUpload(WxConsts.MEDIA_VIDEO, WxConsts.FILE_MP4, FileUtils.openInputStream(media));
        // 把视频变成可被群发的媒体
        WxMpMassVideo video = new WxMpMassVideo();
        video.setTitle("测试标题");
        video.setDescription("测试描述");
        video.setMediaId(uploadMediaResVideo.getMediaId());
        WxMpMassUploadResult uploadResult = wxMpService.massVideoUpload(video);

        massMessage.setMsgType(WxConsts.MASS_MSG_VIDEO);
        massMessage.setMediaId(uploadResult.getMediaId());
        // massMessage.getToUsers().add(openIds);
        wxMpService.massOpenIdsMessageSend(massMessage);


        /**
         * 图片消息
         */
        WxMediaUploadResult uploadMediaResImag = wxMpService.mediaUpload(WxConsts.MEDIA_IMAGE, WxConsts.FILE_JPG, FileUtils.openInputStream(media));
        massMessage.setMsgType(WxConsts.MASS_MSG_IMAGE);
        massMessage.setMediaId(uploadMediaResImag.getMediaId());
        // massMessage.getToUsers().add(openIds);
        wxMpService.massOpenIdsMessageSend(massMessage);

        /**
         *语音消息
         */
        WxMediaUploadResult uploadMediaResAudio = wxMpService.mediaUpload(WxConsts.MEDIA_VOICE, WxConsts.FILE_MP3, FileUtils.openInputStream(media));
        massMessage.setMsgType(WxConsts.MASS_MSG_VOICE);
        massMessage.setMediaId(uploadMediaResAudio.getMediaId());
        // massMessage.getToUsers().add(openIds);
        wxMpService.massOpenIdsMessageSend(massMessage);


        /**
         *图文消息
         */
        // 先上传图文消息里需要的图片
        WxMediaUploadResult uploadMediaRes = wxMpService.mediaUpload(WxConsts.MEDIA_IMAGE, WxConsts.FILE_JPG, FileUtils.openInputStream(media));

        WxMpMassNews news = new WxMpMassNews();
        WxMpMassNews.WxMpMassNewsArticle article1 = new WxMpMassNews.WxMpMassNewsArticle();
        article1.setTitle("标题1");
        article1.setContent("内容1");
        article1.setThumbMediaId(uploadMediaRes.getMediaId());
        news.addArticle(article1);

        WxMpMassNews.WxMpMassNewsArticle article2 = new WxMpMassNews.WxMpMassNewsArticle();
        article2.setTitle("标题2");
        article2.setContent("内容2");
        article2.setThumbMediaId(uploadMediaRes.getMediaId());
        article2.setShowCoverPic(true);
        article2.setAuthor("作者2");
        article2.setContentSourceUrl("www.baidu.com");
        article2.setDigest("摘要2");
        news.addArticle(article2);

        wxMpService.massNewsUpload(news);

        massMessage.setMsgType(WxConsts.MASS_MSG_NEWS);
        massMessage.setMediaId(uploadResult.getMediaId());
        //massMessage.getToUsers().add(openIds);

        wxMpService.massOpenIdsMessageSend(massMessage);

    }
}