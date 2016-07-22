package org.h819.commons.net;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;


/**
 * Description : TODO(产品处工具)
 * User: h819
 * Date: 2015/10/26
 * Time: 15:09
 * To change this template use File | Settings | File Templates.
 */
public class HttpClientExample {

    BasicCookieStore cookieStore = new BasicCookieStore();
    CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
    private String url = "http://innerapp.capital-std.com/innerApp/";

    public static void main(String[] args) throws Exception {

        HttpClientExample utils = new HttpClientExample();
        System.out.println("do Post example : ");
        utils.doPost();
    }


    private void doGet() {


        CloseableHttpResponse response = null;

        try {
            HttpGet httpget = new HttpGet(url);
            response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();

            System.out.println("Login form get: " + response.getStatusLine());
            //  EntityUtils.consume(entity);

            System.out.println("Initial set of cookies:");
            List<Cookie> cookies = cookieStore.getCookies();

            if (cookies.isEmpty()) {
                System.out.println("None");
            } else {
                for (int i = 0; i < cookies.size(); i++) {
                    System.out.println("- " + cookies.get(i).toString());
                }
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            try {
                response.close();
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void doPost() {

        CloseableHttpResponse response = null;

        try {

            HttpUriRequest login = RequestBuilder.post()
                    .setUri(new URI(url))
                    .addParameter("username", "230103197404180351")
                    .addParameter("password", "password")
                    .build();

            //验证完成
            response = httpclient.execute(login);

            //如果登录成功后，保持登陆的状态信息，继续浏览其他页面
            if (response.getStatusLine().getStatusCode() == 200) {

                System.out.print("login succeed. ");

                //print head test
//                for (Header head : response.getAllHeaders()) {
//                   System.out.println(head);
//                }

                HttpEntity loginEntity = response.getEntity();
                //  System.out.println("登录后获取的页面:" + EntityUtils.toString(loginEntity));


                //继续浏览其他页面
                String urlOther = "http://innerapp.capital-std.com/innerApp/standardResource/trsSearchAction.do?trsWhere=standard_code=CASE('DB11%')&trsIDBuff=1101921&trs_page=1&showCols=&key=DB11&defautCols=standard_code&listtable.numPerPage=10&listtable.input_page0=1&listtable.export=&listtable.exporttype=&listtable.listtableindex=&deletedRows0=&method=doSearch";

                HttpUriRequest other = RequestBuilder.post()
                        .setUri(new URI(urlOther))
                        .build();

                response = httpclient.execute(other);

                if (response.getStatusLine().getStatusCode() == 200) {

                    System.out.println("request other url succeed.");
                    // System.out.println(EntityUtils.toString(response.getEntity()));
                }


            }


        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            try {
                response.close();
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
