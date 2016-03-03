package org.h819.commons.exe;


import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Description : TODO()
 * User: h819
 * Date: 2016-1-23
 * Time: 5:30
 * To change this template use File | Settings | File Templates.
 */
public class test {

    public static void main(String[] agrs){

        String s=new String("The=Java=platform=is=the=ideal=platform=for=network=computing");
        StringTokenizer st=new StringTokenizer(s,"=",true);
       // System.out.println("Token Total:"+st.countTokens());
//        while ( st.hasMoreElements() ){
//          //  System.out.println(st.nextToken());
//        }

        List<String> list = new ArrayList<String>();

        list.add("t 900");
        list.add("p");
        list.add("f");

        StringBuilder sb = new StringBuilder();
        for (String arg : list) {
            sb.append(" ").append(arg);
        }

        //System.out.println(sb.toString());

        CommandLine cmd =  CommandLine.parse("ping" + sb.toString());

        System.out.println(cmd.toString());

    }
}
