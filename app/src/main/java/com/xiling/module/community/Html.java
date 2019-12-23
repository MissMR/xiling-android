package com.xiling.module.community;


public class Html {
    public static String toStandar(String content,String size) {
        return String.format(
                "<html><head><meta name=\"format-detection\" content=\"telephone=no\" /><style> img{width:auto;height:auto;max-width: 100%%;} *{ font-size:"+size+" px; }</style><head><body>%s</body></html>"
                ,content);
    }

    public static String toCourse(String content) {
        return String.format(
                "<html><head><meta name=\"viewport\" content=\"width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no\"><style> *{ margin:0;} body{padding:0;margin:0;} img{display:block; width: 100%%;}</style><head><body>%s</body></html>"
                ,content);
    }
}
