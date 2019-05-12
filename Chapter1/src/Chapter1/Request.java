package Chapter1;

import java.io.IOException;
import java.io.InputStream;

/**
 * @program: Tomcat
 * @description: 请求类
 * @author: Liu Hanyi
 * @create: 2019-05-02 18:44
 **/

public class Request {
    private String uri;
    private InputStream ins;

    public Request(InputStream ins) {
        this.ins = ins;
    }

    public String getUri() {
        return uri;
    }

    public void parse(){
        StringBuffer sb = new StringBuffer();
        byte[] b = new byte[2048];
        int i = 0;
        try {
            i = ins.read(b);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int j = 0; j < i; j++) {
            sb.append((char) b[j]);
        }


        System.out.println(sb.toString());
        uri = parseURI(sb.toString());

    }

    private String parseURI(String requestString){
        int index1, index2 = 0;
        index1 = requestString.indexOf(" ");
        if (index1 != -1){
            index2 = requestString.indexOf(" ",index1+1);
            if (index2 != -1)
                return  requestString.substring(index1+1,index2);
        }

        return null;
    }
}
