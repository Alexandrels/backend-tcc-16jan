package br.com.easygame.conexao;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpClientSingleton {
    
    private static final int JSON_CONNECTION_TIMEOUT = 30000;
    private static final int JSON_SOCKET_TIMEOUT = 5000;
    private static HttpClientSingleton instance;
    private InputStream is = null;

    public static void main(String[] args) throws MalformedURLException, IOException {
		HttpClientSingleton httpClientInstace = HttpClientSingleton.getHttpClientInstace();
		String string = httpClientInstace.get(new URL("http://www.google.com.br"));
		System.out.println(string);
	}

    
    private HttpClientSingleton() {
    }
    
    public static HttpClientSingleton getHttpClientInstace(){
     if(instance==null)
         instance = new HttpClientSingleton();
     return instance;
    }

    public String get(URL url) throws IOException {
       // URLConnection urlConnection = url.openConnection();
      //  urlConnection.setConnectTimeout(JSON_CONNECTION_TIMEOUT);
        //avaliar se retornou ok 200
     //  return readString(urlConnection.getInputStream());
    	HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    	
    	conn.setConnectTimeout(30000);
    	conn.setRequestMethod("GET");
    	conn.setDoInput(true);
    	conn.connect();
    	
    	int response = conn.getResponseCode();
    	System.out.println("Response: "+ response);
    	is = conn.getInputStream();
    	return readString(is);
    	
    }

    private  byte[] readBytes(InputStream in) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                bos.write(buf, 0, len);
            }

            byte[] bytes = bos.toByteArray();
            return bytes;
        } finally {
            bos.close();
        }
    }

    private  String readString(InputStream in) throws IOException {
        byte[] bytes = readBytes(in);
        String texto = new String(bytes);
        return texto;
    }

}